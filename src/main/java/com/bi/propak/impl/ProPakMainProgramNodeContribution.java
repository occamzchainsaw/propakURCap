package com.bi.propak.impl;

import java.util.Locale;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.String;

import com.bi.propak.before.BeforeProgramNodeService;
import com.bi.propak.pickups.PickupProgramNodeService;
import com.bi.propak.separator.SeparatorProgramNodeService;
import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.CreationContext.NodeCreationType;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.domain.UserInterfaceAPI;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.program.ProgramModel;
import com.ur.urcap.api.domain.program.nodes.ProgramNodeFactory;
import com.ur.urcap.api.domain.program.structure.TreeNode;
import com.ur.urcap.api.domain.program.structure.TreeStructureException;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.undoredo.UndoRedoManager;
import com.ur.urcap.api.domain.undoredo.UndoableChanges;
import com.ur.urcap.api.domain.userinteraction.RobotPositionCallback;
import com.ur.urcap.api.domain.userinteraction.robot.movement.MovementCompleteEvent;
import com.ur.urcap.api.domain.userinteraction.robot.movement.RobotMovement;
import com.ur.urcap.api.domain.userinteraction.robot.movement.RobotMovementCallback;
import com.ur.urcap.api.domain.value.Pose;
import com.ur.urcap.api.domain.value.jointposition.JointPositions;
import com.ur.urcap.api.domain.value.jointposition.JointPosition;
import com.ur.urcap.api.domain.value.simple.Angle;
import com.ur.urcap.api.domain.value.simple.Length;

import com.ur.urcap.api.domain.value.PoseFactory;
import com.ur.urcap.api.domain.value.jointposition.JointPositionFactory;


public class ProPakMainProgramNodeContribution implements ProgramNodeContribution{

	private final ProgramAPIProvider apiProvider;
	private DataModel dataModel;
	private ProPakMainProgramNodeView view;
	private final UndoRedoManager undoRedoManager;
	private final RobotMovement robotMovement;

	private final PoseFactory poseFactory;
	private final JointPositionFactory jointPositionFactory;
	private Pose homePose;
	private final Pose emptyPose;
	private JointPositions homeJoints;
	private final double[] D_emptyJoints = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
	private final JointPositions emptyJoints;
	private JointPosition[] tempHomeJoints;
	private double[] actualHomeJoints;

	private boolean IS_NAMED;
	private String TITLE;

	private final int number;
	
	private final static String fileName = "home_clipboard.txt";

	public ProPakMainProgramNodeContribution(ProgramAPIProvider apiProvider, final ProPakMainProgramNodeView view,
			DataModel model, CreationContext context) {
		this.IS_NAMED = false;
		this.apiProvider = apiProvider;
		this.dataModel = model;
		this.view = view;
		view.NODE_COUNT++;
		this.number = view.NODE_COUNT;
		if (view.NODE_COUNT > 24) {
			view.resetNodeCounter();
		}
		this.undoRedoManager = apiProvider.getProgramAPI().getUndoRedoManager();
		this.robotMovement = apiProvider.getUserInterfaceAPI().getUserInteraction().getRobotMovement();

		this.poseFactory = apiProvider.getProgramAPI().getValueFactoryProvider().getPoseFactory();
		this.homePose = poseFactory.createPose(0.141, -0.58, 0.512, 0.864, 3.052, 0.023, Length.Unit.M, Angle.Unit.RAD);

		this.jointPositionFactory = apiProvider.getProgramAPI().getValueFactoryProvider().getJointPositionFactory();
		this.homeJoints = jointPositionFactory.createJointPositions(1.52, -1.73, 2.0, -1.82, -1.6, 0.5, Angle.Unit.RAD);

		this.tempHomeJoints = new JointPosition[6];
		this.actualHomeJoints = new double[6];
		this.tempHomeJoints = homeJoints.getAllJointPositions();
		
		this.emptyJoints = jointPositionFactory.createJointPositions(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Angle.Unit.RAD);
		for (int i = 0; i < 6; ++i) {
			actualHomeJoints[i] = tempHomeJoints[0].getPosition(Angle.Unit.RAD);
		}

		this.emptyPose = poseFactory.createPose(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Length.Unit.M, Angle.Unit.RAD);

		if (context.getNodeCreationType().equals(NodeCreationType.NEW)) {
			dataModel.set("ABOVE_LEFT", this.emptyPose);
			dataModel.set("ABOVE_RIGHT", this.emptyPose);
			buildTree();
			undoRedoManager.recordChanges(new UndoableChanges() {
				@Override
				public void executeChanges() {
					dataModel.set("REF_NUMBER", view.NODE_COUNT);
				}
			});
		}
	}

	private void buildTree() {
		final ProgramModel programModel = apiProvider.getProgramAPI().getProgramModel();
		final ProgramNodeFactory nodeFactory = programModel.getProgramNodeFactory();
		final TreeNode rootNode = programModel.getRootTreeNode(this);
		undoRedoManager.recordChanges(new UndoableChanges() {

			@Override
			public void executeChanges() {
				try {
					// Add child nodes
					// LEFT
					rootNode.addChild(nodeFactory.createURCapProgramNode(BeforeProgramNodeService.class));
					rootNode.addChild(nodeFactory.createURCapProgramNode(PickupProgramNodeService.class));
					rootNode.addChild(nodeFactory.createURCapProgramNode(SeparatorProgramNodeService.class));
					// RIGHT
					rootNode.addChild(nodeFactory.createURCapProgramNode(BeforeProgramNodeService.class));
					rootNode.addChild(nodeFactory.createURCapProgramNode(PickupProgramNodeService.class));
					rootNode.addChild(nodeFactory.createURCapProgramNode(SeparatorProgramNodeService.class));

					rootNode.setChildSequenceLocked(true);
				} catch (TreeStructureException e) {
					// FUCK EXCEPTIONS. MY CODE IS TOO FUCKING GOOD FOR THAT.
				}
			}

		});
	}

	@Override
	public void openView() {
		setPoseDefined("HOME_POSITION");
		setPoseDefined("ABOVE_LEFT");
		setPoseDefined("ABOVE_RIGHT");
		
		if (existsClipboard()) {
			view.clipboardSetText(refNumberFromClipboard());
		}
	}

	@Override
	public void closeView() {
		// TODO Auto-generated method stub
	}

	@Override
	public String getTitle() {
		// String rNum = Integer.toString(dataModel.get("REF_NUMBER", 0));
		String rNum = Integer.toString(this.number);
		if (!this.IS_NAMED) {
			this.TITLE = "Program " + rNum;
		}
		this.IS_NAMED = true;
		return this.TITLE;
	}

	@Override
	public boolean isDefined() {
		return this.IS_NAMED;
	}

	@Override
	public void generateScript(ScriptWriter writer) {
		String rNum = Integer.toString(this.number);

		Pose p = dataModel.get("HOME_POSITION", this.emptyPose);
		JointPositions q = dataModel.get("HOME_POSITION_J", this.emptyJoints);
		JointPosition qp[] = q.getAllJointPositions();
		String default_q_str = "[1.52, 4.56, 2.0, 4.46, 4.68, 0.5]";

		if (!p.equals(this.emptyPose) && !q.equals(this.D_emptyJoints)) {
			writer.appendLine("global homePosition_" + rNum + " = " + p.toString());
			writer.appendLine("global home_pos" + rNum + "_j = [" + 
					qp[0].getPosition(Angle.Unit.RAD) + "," +
					qp[1].getPosition(Angle.Unit.RAD) + "," + 
					qp[2].getPosition(Angle.Unit.RAD) + "," +
					qp[3].getPosition(Angle.Unit.RAD) + "," +
					qp[4].getPosition(Angle.Unit.RAD) + "," +
					qp[5].getPosition(Angle.Unit.RAD) + "]");			
		} else {
			writer.appendLine("global homePosition_" + rNum + " = " + this.homePose.toString());
			writer.appendLine("global home_pos" + rNum + "_j = " + default_q_str);
		}
		
		p = dataModel.get("ABOVE_LEFT", this.emptyPose);
		if (p != this.emptyPose) {
			writer.appendLine("global above_left" + "_" + rNum + " = " + p.toString());
		} else {
			writer.appendLine("global above_left" + "_" + rNum + " = p[ 0.4, -0.58, 0.59, 0.12, 3.14, 0.0 ]");
		}
		
		p = dataModel.get("ABOVE_RIGHT", this.emptyPose);
		if (p != this.emptyPose) {
			writer.appendLine("global above_right" + "_" + rNum + " = " + p.toString());
		} else {
			writer.appendLine("global above_right" + "_" + rNum + " = p[ 0.4, -0.58, 0.59, 0.12, 3.14, 0.0 ]");
		}

		view.clipboardResetText();
		view.enablePasteButton(false);
		deleteClipboard();
		writer.writeChildren();
	}

	public void setPositionAction(final String posKey) {
		undoRedoManager.recordChanges(new UndoableChanges() {
			@Override
			public void executeChanges() {
				UserInterfaceAPI uiapi = apiProvider.getUserInterfaceAPI();
				uiapi.getUserInteraction().getUserDefinedRobotPosition(new RobotPositionCallback() {
					@Override
					public void onOk(Pose pose, JointPositions q) {
						dataModel.set(posKey, pose);
						
						dataModel.set(posKey + "_J", q);
						
						dataModel.set(posKey + "_DEF", true);
						setPoseDefined(posKey);

						// System.out.println("Pose:\t" + homePose.toString());
						// System.out.println("Joints:\t[" + homeJoints[0].getPosition(Angle.Unit.RAD) + ", " +
						// homeJoints[1].getPosition(Angle.Unit.RAD) + ", " +
						// homeJoints[2].getPosition(Angle.Unit.RAD) + ", " +
						// homeJoints[3].getPosition(Angle.Unit.RAD) + ", " +
						// homeJoints[4].getPosition(Angle.Unit.RAD) + ", " +
						// homeJoints[5].getPosition(Angle.Unit.RAD) + "]");
					}
				});
			}
		});
	}
	
	public void moveAction(final String posKey) {
		JointPositions q = jointPositionFactory.createJointPositions(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Angle.Unit.RAD);
		q = dataModel.get(posKey + "_J", emptyJoints);

		if (q != this.emptyJoints) {
			robotMovement.requestUserToMoveRobot(q, new RobotMovementCallback() {
				@Override
				public void onComplete(MovementCompleteEvent event) {
					
				}
			});
		}
	}
	
	private void setPoseDefined(final String key) {
		int i = 0;
		
		if (key.contains("HOME")) {
			i = 0;
		} else if (key.contains("LEFT")) {
			i = 1;
		} else {
			i = 2;
		}
		
		boolean check = dataModel.get(key + "_DEF", false);

		if (check) {
			view.setDefinedIcon(i, true);
		} else if (!check) {
			view.setDefinedIcon(i, false);
		}
			
	}
	
	public void copy(){
		//	Create the file
		try {
			File clipboard = new File(fileName);
			clipboard.createNewFile();
		} catch (IOException e) {
			System.out.println("IOExcpetion thrown.");
			e.printStackTrace();
		}

		//	Write the proper contents of the file
		try {
			FileWriter writer = new FileWriter(fileName);
			//String palletStr = (this.pallet == 1) ? "Left" : "Right";
			//	Home Position
			String contents = this.number + "\n";
			String key = "HOME_POSITION";
			double[] doubleQ = dataModel.get(key + "_J", D_emptyJoints);
			Pose p = dataModel.get(key, this.emptyPose);
			JointPositions q = dataModel.get(key + "_J", this.emptyJoints);
			JointPosition qp[] = q.getAllJointPositions();
			
			String tempQ = "";
			for (JointPosition j : qp) {
				tempQ = tempQ + j.getPosition(Angle.Unit.RAD) + ",";
			}
			tempQ = tempQ.substring(0, tempQ.length() - 1);
			
			contents += p.toString() + "\n" + tempQ + "\n";

			boolean isDef = dataModel.get(key + "_DEF", false);
			
			contents += Boolean.toString(isDef) + "\n";

			//	Above Left Pallet Pose
			key = "ABOVE_LEFT";
			p = dataModel.get(key, emptyPose);
			q = dataModel.get(key + "_J", this.emptyJoints);
			qp = q.getAllJointPositions();
			
			tempQ = "";
			for (JointPosition j : qp) {
				tempQ = tempQ + j.getPosition(Angle.Unit.RAD) + ",";
			}
			tempQ = tempQ.substring(0, tempQ.length() - 1);
			
			//System.out.println("left joints: " + tempQ);

			contents += p.toString() + "\n" + tempQ + "\n";

			isDef = dataModel.get(key + "_DEF", false);
			
			contents += Boolean.toString(isDef) + "\n";
			
//			Above Right Pallet Pose
			key = "ABOVE_RIGHT";
			p = dataModel.get(key, emptyPose);
			q = dataModel.get(key + "_J", this.emptyJoints);
			qp = q.getAllJointPositions();
			
			tempQ = "";
			for (JointPosition j : qp) {
				tempQ = tempQ + j.getPosition(Angle.Unit.RAD) + ",";
			}
			tempQ = tempQ.substring(0, tempQ.length() - 1);

			contents += p.toString() + "\n" + tempQ + "\n";

			isDef = dataModel.get(key + "_DEF", false);
			
			contents += Boolean.toString(isDef) + "\n";
			
			writer.write(contents);
			writer.close();
			// System.out.print(contents);
		} catch (IOException e) {
			System.out.println("IOException on writing to the file.");
			e.printStackTrace();
		}

		view.clipboardSetText(this.number);
		view.enablePasteButton(true);
	}

	public void paste(){
		undoRedoManager.recordChanges(new UndoableChanges() {
			@Override
			public void executeChanges() {
				try {
					File clipboard = new File(fileName);
					Scanner readHead = new Scanner(clipboard);
					// skip the first line, cause there, the reference number is held.
					readHead.nextLine();
					int i = 0;
					String data = "";
					String key = "";
					Pose p = emptyPose;
					JointPositions q = emptyJoints;
					boolean isDef = false;
					//	Home Position
					if (readHead.hasNextLine()) {
						data = readHead.nextLine();
						p = poseFromString(data);
						key = "HOME_POSITION";
						dataModel.set(key, p);
						
						data = readHead.nextLine();
						q = jointPositionsFromString(data);
						dataModel.set(key + "_J", q);
						
						data = readHead.nextLine();
						isDef = Boolean.parseBoolean(data);
						dataModel.set(key + "_DEF", isDef);
						setPoseDefined(key);
					}
					
					// Above Left Pallet Pose
					if (readHead.hasNextLine()) {
						data = readHead.nextLine();
						p = poseFromString(data);
						key = "ABOVE_LEFT";
						dataModel.set(key, p);
						
						data = readHead.nextLine();
						q = jointPositionsFromString(data);
						dataModel.set(key + "_J", q);
						
						data = readHead.nextLine();
						isDef = Boolean.parseBoolean(data);
						dataModel.set(key + "_DEF", isDef);
						setPoseDefined(key);
					}
					
					// Above Right Pallet Pose
					if (readHead.hasNextLine()) {
						data = readHead.nextLine();
						p = poseFromString(data);
						key = "ABOVE_RIGHT";
						dataModel.set(key, p);
						
						data = readHead.nextLine();
						q = jointPositionsFromString(data);
						dataModel.set(key + "_J", q);
						
						data = readHead.nextLine();
						isDef = Boolean.parseBoolean(data);
						dataModel.set(key + "_DEF", isDef);
						setPoseDefined(key);
					}
					
					readHead.close();
				} catch (FileNotFoundException e) {
					System.out.println("FileNotFoundException thrown.");
					e.printStackTrace();
				}
			}
		});
	}

	private void deleteClipboard() {
		File clipboard = new File(fileName);
		clipboard.delete();
	}

	private boolean existsClipboard() {
		File clipboard = new File(fileName);
		boolean ret = false;
		try {
			ret = clipboard.createNewFile();
		} catch (IOException e) {
			System.out.println("Error while deleting the file");
			e.printStackTrace();
		}
		
		if (ret) {
			deleteClipboard();
		}
		
		return !ret;
	}
	
	
	//	HEPLERS FOR COPYING AND PASTING
	//	Mostly stuff to work with strings, since copying is done via a text file
	private int refNumberFromClipboard() {
		File clipboard = new File(fileName);
		int ret = 0;
		
		try {
			Scanner readHead = new Scanner(clipboard);
			String data = readHead.nextLine();
			ret = Integer.valueOf(data);
			readHead.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return ret;
	}

	private Pose poseFromString(String input) {
		double dPose[] = new double[6];
		String subInput = input.substring(2, input.length() - 1);
		String splitInput[] = subInput.split(",");
		
		for (int i = 0; i < 6; ++i) {
			dPose[i] = Double.parseDouble(splitInput[i]);
		}
		
		Pose p = poseFactory.createPose(dPose[0], dPose[1], dPose[2], dPose[3], dPose[4], dPose[5], Length.Unit.M, Angle.Unit.RAD);
		return p;
	}

	private JointPositions jointPositionsFromString(String input) {
		double dJoints[] = new double[6];
		String splitInput[] = input.split(",");
		
		for (int i = 0; i < 6; ++i) {
			dJoints[i] = Double.parseDouble(splitInput[i]);
		}
		
		JointPositions q = jointPositionFactory.createJointPositions(dJoints[0], dJoints[1], dJoints[2], dJoints[3], dJoints[4], dJoints[5], Angle.Unit.RAD);
		return q;
	}

}
