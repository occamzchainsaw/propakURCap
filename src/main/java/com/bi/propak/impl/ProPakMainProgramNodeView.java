package com.bi.propak.impl;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardNumberInput;

public class ProPakMainProgramNodeView implements SwingProgramNodeView<ProPakMainProgramNodeContribution>{

	@SuppressWarnings("unused")
	private final ViewAPIProvider apiProvider;
	public int NODE_COUNT;
	
	public ProPakMainProgramNodeView(ViewAPIProvider apiProvider) {
		this.apiProvider = apiProvider;
		this.NODE_COUNT = 0;
	}
	
	private final static int BUTTON_WIDTH = 180;
	private final static int BUTTON_HEIGHT = 40;
	
	private static final String LOW_V_KEY = "LOW_V";
	private static final String HIGH_V_KEY = "HIGH_V";
	private static final String RANGE_P_KEY = "RANGE_P";
	
	JLabel homeDescLabel = new JLabel();
	JLabel leftDescLabel = new JLabel();
	JLabel rightDescLabel = new JLabel();
	
	JLabel homeIsDefinedLabel = new JLabel();
	JLabel leftIsDefinedLabel = new JLabel();
	JLabel rightIsDefinedLabel = new JLabel();
	
	JButton homeSetButton = new JButton();
	JButton leftSetButton = new JButton();
	JButton rightSetButton = new JButton();
	
	JButton homeMoveButton = new JButton();
	JButton leftMoveButton = new JButton();
	JButton rightMoveButton = new JButton();
	
	JButton copyButton = new JButton();
	JButton pasteButton = new JButton();
	
	JLabel clipboardLabel = new JLabel();
	
	JLabel logoLabel = new JLabel();
	
	JTextField lowVoltageInput = new JTextField();
	
	Icon okIcon = new ImageIcon(this.getClass().getResource("ChubbyTick.png"));
	Icon nokIcon = new ImageIcon(this.getClass().getResource("ChubbyX.png"));
	Icon logoIcon = new ImageIcon(this.getClass().getResource("ProPakLogo_480.png"));

	@Override
	public void buildUI(JPanel panel, final ContributionProvider<ProPakMainProgramNodeContribution> provider) {
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		// HOME POSITION SET AND MOVE BUTTONS
		// description
		if ("pl".equals(Locale.getDefault().getLanguage())) {
			homeDescLabel.setText("Pozycja Domowa");
			homeSetButton.setText("Ustaw Pozycję");
			homeMoveButton.setText("Przesuń Tutaj");
		} else {
			homeDescLabel.setText("Home Position");
			homeSetButton.setText("Set Position");
			homeMoveButton.setText("Move Here");
		}
		c.fill = GridBagConstraints.NONE;
		c.ipadx = 0;
		c.ipady = 0;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(10, 0, 6, 0);
		c.weightx = 1.0;
		c.anchor = GridBagConstraints.NORTHWEST;
		//panel.add(homeDescLabel, c);
		
		// defined indicator
		homeIsDefinedLabel.setVerticalAlignment(JLabel.CENTER);
		c.insets = new Insets(7, 0, 6, 0);
		c.gridx = 1;
		c.weightx = 0.5;
		homeIsDefinedLabel.setIcon(nokIcon);
		//panel.add(homeIsDefinedLabel, c);
		
		// set button
		c.insets = new Insets(0, 0, 6, 0);
		c.gridx = 2;
		c.weightx = 0.5;
		homeSetButton.setPreferredSize(new Dimension(200, 40));
		homeSetButton.setActionCommand("HOME_POSITION");
		homeSetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				provider.get().setPositionAction(e.getActionCommand());
			}
		});
		//panel.add(homeSetButton, c);
		
		// move button
		c.gridx = 3;
		c.weightx = 0.5;
		homeMoveButton.setPreferredSize(new Dimension(200, 40));
		homeMoveButton.setActionCommand("HOME_POSITION");
		homeMoveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				provider.get().moveAction(e.getActionCommand());
			}
		});
		//panel.add(homeMoveButton, c);
		
		// ABOVE LEFT PALLET
		c.gridy++;
		
		// description
		if ("pl".equals(Locale.getDefault().getLanguage())) {
			leftDescLabel.setText("Pozycja Przed Paletą Lewą");
			leftSetButton.setText("Ustaw Pozycję");
			leftMoveButton.setText("Przesuń Tutaj");
		} else {
			leftDescLabel.setText("Before Left Pallet Pose");
			leftSetButton.setText("Set Position");
			leftMoveButton.setText("Move Here");
		}
		c.fill = GridBagConstraints.NONE;
		c.ipadx = 0;
		c.ipady = 0;
		c.gridx = 0;
		c.insets = new Insets(10, 0, 6, 0);
		c.weightx = 1.0;
		c.anchor = GridBagConstraints.NORTHWEST;
		//panel.add(leftDescLabel, c);
		
		// defined indicator
		leftIsDefinedLabel.setVerticalAlignment(JLabel.CENTER);
		c.insets = new Insets(7, 0, 6, 0);
		c.gridx = 1;
		c.weightx = 0.5;
		leftIsDefinedLabel.setIcon(nokIcon);
		//panel.add(leftIsDefinedLabel, c);
		
		// set button
		c.insets = new Insets(0, 0, 6, 0);
		c.gridx = 2;
		c.weightx = 0.5;
		leftSetButton.setPreferredSize(new Dimension(200, 40));
		leftSetButton.setActionCommand("ABOVE_LEFT");
		leftSetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				provider.get().setPositionAction(e.getActionCommand());
			}
		});
		//panel.add(leftSetButton, c);
		
		// move button
		c.gridx = 3;
		c.weightx = 0.5;
		leftMoveButton.setPreferredSize(new Dimension(200, 40));
		leftMoveButton.setActionCommand("ABOVE_LEFT");
		leftMoveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				provider.get().moveAction(e.getActionCommand());
			}
		});
		//panel.add(leftMoveButton, c);
		
		// ABOVE RIGHT PALLET
		c.gridy++;
		
		// description
		if ("pl".equals(Locale.getDefault().getLanguage())) {
			rightDescLabel.setText("Pozycja Przed Paletą Prawą");
			rightSetButton.setText("Ustaw Pozycję");
			rightMoveButton.setText("Przesuń Tutaj");
		} else {
			rightDescLabel.setText("Before Right Pallet Pose");
			rightSetButton.setText("Set Position");
			rightMoveButton.setText("Move Here");
		}
		c.fill = GridBagConstraints.NONE;
		c.ipadx = 0;
		c.ipady = 0;
		c.gridx = 0;
		c.insets = new Insets(10, 0, 6, 0);
		c.weightx = 1.0;
		c.anchor = GridBagConstraints.NORTHWEST;
		//panel.add(rightDescLabel, c);
		
		// defined indicator
		rightIsDefinedLabel.setVerticalAlignment(JLabel.CENTER);
		c.insets = new Insets(7, 0, 6, 0);
		c.gridx = 1;
		c.weightx = 0.5;
		rightIsDefinedLabel.setIcon(nokIcon);
		//panel.add(rightIsDefinedLabel, c);
		
		// set button
		c.insets = new Insets(0, 0, 6, 0);
		c.gridx = 2;
		c.weightx = 0.5;
		rightSetButton.setPreferredSize(new Dimension(200, 40));
		rightSetButton.setActionCommand("ABOVE_RIGHT");
		rightSetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				provider.get().setPositionAction(e.getActionCommand());
			}
		});
		//panel.add(rightSetButton, c);
		
		// move button
		c.gridx = 3;
		c.weightx = 0.5;
		rightMoveButton.setPreferredSize(new Dimension(200, 40));
		rightMoveButton.setActionCommand("ABOVE_RIGHT");
		rightMoveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				provider.get().moveAction(e.getActionCommand());
			}
		});
		//panel.add(rightMoveButton, c);
		
		// ANALOG INPUT SETUP STUFF
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		lowVoltageInput.setHorizontalAlignment(JTextField.RIGHT);
		panel.add(createDoubleInput(lowVoltageInput, "Min Voltage:", new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				KeyboardNumberInput<Double> keyboardInput = provider.get().getKeyboardForDouble();
				keyboardInput.show(lowVoltageInput, provider.get().getCallbackForDouble(LOW_V_KEY));
			}
		}));

		
		//	PRO PAK LOGO AT THE BOTTOM
//		c.anchor = GridBagConstraints.SOUTH;
//		c.fill = GridBagConstraints.NONE;
//		c.gridx = 0;
//		c.gridy++;
//		c.weightx = 0.5;
//		c.weighty = 1.0;
//		c.gridwidth = 4;
//		c.gridheight = 1;
//		c.ipadx = 0;
//		c.ipady = 0;
//		logoLabel.setIcon(logoIcon);
//		panel.add(logoLabel, c);
		
		//	ADD BUTTONS TO COPY AND PASTE PICKUP POSES
		//	LABEL IN THE MIDDLE TO SEE THE CONTENTS OF THE CLIPBOARD
		//	Copy
		copyButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		if ("pl".equals(Locale.getDefault().getLanguage())) {
			copyButton.setText("Kopiuj Pozycje");
		} else {
			copyButton.setText("Copy Positions");
		}
		copyButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				provider.get().copy();
			}
		});
		c.gridy++;
		c.gridx = 0;
		c.weightx = 0.3;
		c.weighty = 1.0;
		c.anchor = GridBagConstraints.SOUTHWEST;
		c.fill = GridBagConstraints.NONE;
		//panel.add(copyButton, c);

		//	Clipboard Label
		clipboardLabel.setText("");
		clipboardLabel.setPreferredSize(new Dimension(340, BUTTON_HEIGHT));
		clipboardLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		clipboardLabel.setHorizontalAlignment(SwingConstants.CENTER);
		clipboardLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		c.gridx = 1;
		c.weightx = 1.0;
		//panel.add(clipboardLabel, c);

		//	Paste
		pasteButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		if ("pl".equals(Locale.getDefault().getLanguage())) {
			pasteButton.setText("Wklej Pozycje");
		} else {
			pasteButton.setText("Paste Positions");
		}
		pasteButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				provider.get().paste();
			}
		});
		pasteButton.setEnabled(false);
		c.gridx = 2;
		c.weightx = 0.3;
		c.anchor = GridBagConstraints.SOUTHEAST;
		c.fill = GridBagConstraints.NONE;
		//panel.add(pasteButton, c);
	}
	

	public void resetNodeCounter() {
		this.NODE_COUNT = 0;
	}
	
	public void setDefinedIcon(final int i, final boolean isDef) {
		switch (i) {
		case 0: 
			if (isDef) {
				homeIsDefinedLabel.setIcon(okIcon);
			} else {
				homeIsDefinedLabel.setIcon(nokIcon);
			}
			break;
		case 1:
			if (isDef) {
				leftIsDefinedLabel.setIcon(okIcon);
			} else {
				leftIsDefinedLabel.setIcon(nokIcon);
			}
			break;
		case 2:
			if (isDef) {
				rightIsDefinedLabel.setIcon(okIcon);
			} else {
				rightIsDefinedLabel.setIcon(nokIcon);
			}
			break;
		default: return;
		}
	}
	
	private Box createDoubleInput(JTextField inputField, String labelText, MouseAdapter mouseAdapter) {
		Box horizontalBox = Box.createHorizontalBox();
		horizontalBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JLabel label = new JLabel(labelText);
		
		lowVoltageInput.setFocusable(false);
		lowVoltageInput.setPreferredSize(new Dimension(120, 40));
		lowVoltageInput.addMouseListener(mouseAdapter);
		
		horizontalBox.add(label);
		horizontalBox.add(inputField);
		
		return horizontalBox;
	}
	
	public void setLowVoltage(Double value) {
		DecimalFormat df = new DecimalFormat("#0.0");
		df.setMaximumFractionDigits(1);
		String stringValue = df.format((double) value);
		lowVoltageInput.setText(stringValue);
	}
	
	public void enablePasteButton(boolean enable) {
		pasteButton.setEnabled(enable);
	}

	public void clipboardSetText(int refNumber) {
		String out = "";
		if ("pl".equals(Locale.getDefault().getLanguage())) {
			out = "<html>Zawartość Schowka:<br>Referencja " + refNumber + "</html>";
		} else {
			out = "<html>Clipboard Contents:<br>Reference " + refNumber + "</html>";
		}
		
		clipboardLabel.setText(out);
	}

	public void clipboardResetText() {
		clipboardLabel.setText("");
	}

}
