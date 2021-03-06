package com.bi.propak.separator;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.installation.CreationContext.NodeCreationType;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.domain.UserInterfaceAPI;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.io.DigitalIO;
import com.ur.urcap.api.domain.io.IOModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.undoredo.UndoRedoManager;
import com.ur.urcap.api.domain.undoredo.UndoableChanges;
import com.ur.urcap.api.domain.userinteraction.RobotPositionCallback;
import com.ur.urcap.api.domain.userinteraction.robot.movement.MovementCompleteEvent;
import com.ur.urcap.api.domain.userinteraction.robot.movement.RobotMovement;
import com.ur.urcap.api.domain.userinteraction.robot.movement.RobotMovementCallback;
import com.ur.urcap.api.domain.value.Pose;
import com.ur.urcap.api.domain.value.PoseFactory;
import com.ur.urcap.api.domain.value.jointposition.JointPositionFactory;
import com.ur.urcap.api.domain.value.jointposition.JointPositions;
import com.ur.urcap.api.domain.value.jointposition.JointPosition;
import com.ur.urcap.api.domain.value.simple.Angle;
import com.ur.urcap.api.domain.value.simple.Length;

public class SeparatorProgramNodeContribution implements ProgramNodeContribution{

	private final ProgramAPIProvider apiProvider;
	private DataModel dataModel;
	private final SeparatorProgramNodeView view;
	private final UndoRedoManager undoRedoManager;
	private final RobotMovement robotMovement;
	private final IOModel ioModel;
	private DigitalIO valve_out;

	private final int number;
	private final int pallet;

	private final static int NUMBER_OF_POSITIONS = 6;

	private boolean[] definedPoses = new boolean[NUMBER_OF_POSITIONS];
	private final static String[] poseNames = {"ABOVE_SEPARATOR",
											"PICKUP_SEPARATOR",
											"LIFTED_SEPARATOR",
											"BEFORE_PALLET",
											"ABOVE_PALLET",
											"RELEASE_SEPARATOR"};
	private final static String fileName = "clipboard.txt";

	private final PoseFactory poseFactory;
	private final JointPositionFactory jointPositionFactory;
	private final Pose emptyPose;
	private final JointPositions emptyJoints;
	
	public SeparatorProgramNodeContribution(ProgramAPIProvider provider, DataModel model, SeparatorProgramNodeView view, CreationContext context) {
		this.apiProvider = provider;
		this.dataModel = model;
		this.view = view;

		if (view.CT_FACH) {
			this.pallet = 2;
			view.CT_FACH = false;
		} else {
			view.NODE_COUNT++;
			this.pallet = 1;
			view.CT_FACH = true;
		}

		this.number = view.NODE_COUNT;
		if (view.NODE_COUNT > 24 && !view.CT_FACH) {
			view.resetNodeCounter();
		}

		this.undoRedoManager = apiProvider.getProgramAPI().getUndoRedoManager();
		this.robotMovement = apiProvider.getUserInterfaceAPI().getUserInteraction().getRobotMovement();
		this.ioModel = apiProvider.getProgramAPI().getIOModel();
		
		this.poseFactory = apiProvider.getProgramAPI().getValueFactoryProvider().getPoseFactory();
		this.emptyPose = poseFactory.createPose(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Length.Unit.M, Angle.Unit.RAD);

		this.jointPositionFactory = apiProvider.getProgramAPI().getValueFactoryProvider().getJointPositionFactory();
		this.emptyJoints = jointPositionFactory.createJointPositions(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Angle.Unit.RAD);

		if (context.getNodeCreationType().equals(NodeCreationType.NEW)) {
			for (int i = 0; i < NUMBER_OF_POSITIONS; ++i) {
				definedPoses[i] = false;
			}
		}
		
		deleteClipboard();
		view.clipboardResetText();
	}
	
	@Override
	public void openView() {
//		for (String poseName : poseNames) {
//			setPositionDefined(poseName);
//		}

		valve_out = getDigitalIO("digital_out[0]");
		view.valveButtonColor(valve_out.getValue());
		
		if (existsClipboard()) {
			view.clipboardSetText(refNumberFromClipboard(), palletFromClipboard());
		}
	}

	@Override
	public void closeView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getTitle() {
		String pallet = (this.pallet == 1) ? "left" : "right";
		String title = "";
		if("pl".equals(Locale.getDefault().getLanguage())) {
			if (pallet.equals("left")) {
				title = "Trajektoria Przek??adki Lewej";
			} else {
				title = "Trajektoria Przek??adki Prawej";
			}
		} else {
			if (pallet.equals("left")) {
				title = "Left Separator Trajectory";
			} else {
				title = "Right Separator Trajectory";
			}
		}
		return title;
	}

	@Override
	public boolean isDefined() {
		return (this.number != 0) ? true : false;
	}

	@Override
	public void generateScript(ScriptWriter writer) {
		String rNum = Integer.toString(this.number);
		String pallet = (this.pallet == 1) ? "left" : "right";
		
		for (int i = 0; i < NUMBER_OF_POSITIONS; ++i)
		{
			String key1 = "POSE_1_" + String.valueOf(i);
			String key2 = "POSE_2_" + String.valueOf(i);
			Pose p1 = dataModel.get(key1, this.emptyPose);
			Pose p2 = dataModel.get(key2, this.emptyPose);
			
			String poseName1 = "";
			String poseName2 = "";
			switch(i) {
				case 0:
					poseName1 = "aboveSlip_1_" + String.valueOf(i);
					poseName2 = "aboveSlip_2_" + String.valueOf(i);
					break;
					
				case 1:
					poseName1 = "pickupSlip_1_" + String.valueOf(i);
					poseName2 = "pickupSlip_2_" + String.valueOf(i);
					break;
					
				case 2:
					poseName1 = "liftedSlip_1_" + String.valueOf(i);
					poseName2 = "liftedSlip_2_" + String.valueOf(i);
					break;
					
				case 3:
					poseName1 = "beforePalletSlip_1_" + String.valueOf(i);
					poseName2 = "beforePalletSlip_2_" + String.valueOf(i);
					break;
					
				case 4:
					poseName1 = "abovePalletSlip_1_" + String.valueOf(i);
					poseName2 = "abovePalletSlip_2_" + String.valueOf(i);
					break;
					
				case 5:
					poseName1 = "releaseSlip_1_" + String.valueOf(i);
					poseName2 = "releaseSlip_2_" + String.valueOf(i);
					break;
					
				default:
					break;
			}
			
			if (p1 != this.emptyPose) {
				writer.appendLine("global " + poseName1 + "_" + pallet + "_" + rNum + " = " + p1.toString());
			} else {
				writer.appendLine("global " + poseName1 + "_" + pallet + "_" + rNum + " = p[ 0.4, -0.58, 0.59, 0.12, 3.14, 0.0 ]");
			}
			
			if (p2 != this.emptyPose) {
				writer.appendLine("global " + poseName2 + "_" + pallet + "_" + rNum + " = " + p2.toString());
			} else {
				writer.appendLine("global " + poseName2 + "_" + pallet + "_" + rNum + " = p[ 0.4, -0.58, 0.59, 0.12, 3.14, 0.0 ]");
			}
		}
		
		view.clipboardResetText();
		view.enablePasteButton(false);
		deleteClipboard();
	}
	
	public void setButtonAction(final String posKey) {
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

						//setPositionDefined(posKey);
					}
				});
			}
		});
	}
	
	
	// KANDYDAT DO WYJEBANIA
	private void setPositionDefined(String posKey) {
		final int i = findIndex(posKey);

		boolean check = dataModel.get(posKey + "_DEF", false);

		definedPoses[i] = check;
		//view.setDefinedIcon(i, check);


		// Pose p = dataModel.get(posKey, this.emptyPose);
		// JointPositions q = dataModel.get(posKey + "_J", this.emptyJoints);

		// for (int i = 0; i < NUMBER_OF_POSITIONS; ++i) {
		// 	if (posKey.equals(poseNames[i])) {
		// 		if (!p.equals(this.emptyPose) && !q.equals(this.emptyJoints)) {
		// 			definedPoses[i] = true;
		// 			view.setDefinedIcon(i, true);
		// 		} else {
		// 			definedPoses[i] = false;
		// 			view.setDefinedIcon(i, false);
		// 		}
		// 	}
		// }
	}

	// KANDYDAT DO WYJEBANIA
	private int findIndex(String search) {
		int len = poseNames.length;
		int i = 0;
		while (i < len) {
			if (poseNames[i] == search) {
				return i;
			} else {
				++i;
			}
		}
		return -1;
	}
	
	public void moveButtonAction(final String posKey) {
		JointPositions q = dataModel.get(posKey + "_J", this.emptyJoints);

		if (q != this.emptyJoints) {
			robotMovement.requestUserToMoveRobot(q, new RobotMovementCallback() {
				@Override
				public void onComplete(MovementCompleteEvent event) {
					
				}
			});
		}
	}

	// KANDYDAT DO WYJEBANIA
	public void deleteButtonAction(final String posKey) {
		for (int i = 0; i < NUMBER_OF_POSITIONS; ++i) {
			if (posKey.equals(poseNames[i]) && definedPoses[i]) {
				definedPoses[i] = false;
				//view.setDefinedIcon(i, false);
			}
		}
	}

	// KANDYDAT DO WYJEBANIA
	public void activateValveAction() {
		valve_out = getDigitalIO("digital_out[0]");
		if (valve_out.getValue()) {
			valve_out.setValue(false);
			this.view.valveButtonColor(false);
		} else {
			valve_out.setValue(true);
			this.view.valveButtonColor(true);
		}
		
		// RANDOM TESTS
	}

	// KANDYDAT DO WYJEBANIA
	public DigitalIO getDigitalIO(String defaultName) {
		Collection<DigitalIO> IOCollection = ioModel.getIOs(DigitalIO.class);
		int IO_count = IOCollection.size();
		if (IO_count > 0) {
			Iterator<DigitalIO> itr = IOCollection.iterator();
			while(itr.hasNext()) {
				DigitalIO thisIO = itr.next();
				String thisDefaultName = thisIO.getDefaultName();
				if (thisDefaultName.contentEquals(defaultName)) {
					return thisIO;
				}
			}
		}
		return null;
	}

	public void copy() {
		// Try to create the file
		try {
			File clipboard = new File(fileName);
			clipboard.createNewFile();
		} catch (IOException e) {
			System.out.println("IOException thrown.");
			e.printStackTrace();
		}
		
		// Write some stuff to the file
		try {
			FileWriter writer = new FileWriter(fileName);
			String palletString = (this.pallet == 1) ? "Left" : "Right";
			String contents = this.number + "," + palletString + "\n";
			
			for (int i = 0; i < NUMBER_OF_POSITIONS; ++i) {
				String key1 = "POSE_1_" + String.valueOf(i);
				String key2 = "POSE_2_" + String.valueOf(i);
				
				Pose p1 = dataModel.get(key1, this.emptyPose);
				Pose p2 = dataModel.get(key2, this.emptyPose);
				
				JointPositions q1 = dataModel.get(key1 + "_J", this.emptyJoints);
				JointPositions q2 = dataModel.get(key2 + "_J", this.emptyJoints);
				
				JointPosition qp1[] = q1.getAllJointPositions();
				JointPosition qp2[] = q2.getAllJointPositions();
				
				String tempQ1 = "";
				for (JointPosition j : qp1) {
					tempQ1 = tempQ1 + j.getPosition(Angle.Unit.RAD) + ",";
				}
				tempQ1 = tempQ1.substring(0, tempQ1.length() - 1);
				String tempQ2 = "";
				for (JointPosition j : qp2) {
					tempQ2 = tempQ2 + j.getPosition(Angle.Unit.RAD) + ",";
				}
				tempQ2 = tempQ2.substring(0, tempQ2.length() - 1);
				
				boolean isDef1 = dataModel.get(key1 + "_DEF", false);
				boolean isDef2 = dataModel.get(key2 + "_DEF", false);
				
				contents += p1.toString() + "\n" + tempQ1 + "\n" + Boolean.toString(isDef1) + "\n"
						+ p2.toString() + "\n" + tempQ2 + "\n" + Boolean.toString(isDef2) + "\n";
			}
			
			writer.write(contents);
			writer.close();
		} catch (IOException e) {
			System.out.println("IOException thrown upon writing to the file.");
			e.printStackTrace();
		}
		
		String palletString = "";
		if ("pl".equals(Locale.getDefault().getLanguage())) {
			palletString = (this.pallet == 1) ? "Lewa" : "Prawa";
		} else {
			palletString = (this.pallet == 1) ? "Left" : "Right";
		}
		view.clipboardSetText(this.number, palletString);
		view.enablePasteButton(true);
	}
	
	public void paste() {
		undoRedoManager.recordChanges(new UndoableChanges() {
			@Override
			public void executeChanges() {
				try {
					File clipboard = new File(fileName);
					Scanner readHead = new Scanner(clipboard);
					// skip the first line, cause there, the reference number and pallet side are held.
					readHead.nextLine();
					int i = 0;
					int k = 1;
					while (readHead.hasNextLine()) {
						String data = readHead.nextLine();
						Pose p = poseFromString(data);
						String key = "POSE_1_" + String.valueOf(i);
						dataModel.set(key, p);

						data = readHead.nextLine();
						JointPositions q = jointPositionsFromString(data);
						dataModel.set(key + "_J", q);

						data = readHead.nextLine();
						boolean isDef = Boolean.parseBoolean(data);
						dataModel.set(key + "_DEF", isDef);
						//setPositionDefined(key);
						
						key = "POSE_2_" + String.valueOf(1);
						data = readHead.nextLine();
						p = poseFromString(data);
						dataModel.set(key, p);
						
						data = readHead.nextLine();
						q = jointPositionsFromString(data);
						dataModel.set(key + "_J", q);
						
						data = readHead.nextLine();
						isDef = Boolean.parseBoolean(data);
						dataModel.set(key + "_DEF", isDef);
						//setPositionDefined(key);
						
						++i;
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
	
	private int refNumberFromClipboard() {
		File clipboard = new File(fileName);
		int ret = 0;
		
		try {
			Scanner readHead = new Scanner(clipboard);
			String data = readHead.nextLine();
			String splitData[] = data.split(",");
			ret = Integer.valueOf(splitData[0]);
			readHead.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	private String palletFromClipboard() {
		File clipboard = new File(fileName);
		String ret = "kappa";
		String temp = "";
		
		try {
			Scanner readHead = new Scanner(clipboard);
			String data = readHead.nextLine();
			String splitData[] = data.split(",");
			temp = splitData[1];
			readHead.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		if ("pl".equals(Locale.getDefault().getLanguage())) {
			if (temp.contains("Left")) {
				ret = "Lewa";
			} else if (temp.contains("Right")) {
				ret = "Prawa";
			}
		} else {
			ret = temp;
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
		//String subInput = input.substring(1, input.length() - 5);
		//String splitInput[] = subInput.split(" rad, ");
		String splitInput[] = input.split(",");
		
		for (int i = 0; i < 6; ++i) {
			dJoints[i] = Double.parseDouble(splitInput[i]);
		}
		
		JointPositions q = jointPositionFactory.createJointPositions(dJoints[0], dJoints[1], dJoints[2], dJoints[3], dJoints[4], dJoints[5], Angle.Unit.RAD);
		return q;
	}

}
