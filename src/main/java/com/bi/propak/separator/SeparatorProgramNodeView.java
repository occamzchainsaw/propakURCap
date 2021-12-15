package com.bi.propak.separator;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;

public class SeparatorProgramNodeView implements SwingProgramNodeView<SeparatorProgramNodeContribution>{

	@SuppressWarnings("unused")
	private final ViewAPIProvider apiProvider;

	public int NODE_COUNT;
	public boolean CT_FACH;
	
	private final static int BUTTON_WIDTH = 180;
	private final static int BUTTON_HEIGHT = 40;
	
	Icon okIcon = new ImageIcon(this.getClass().getResource("ChubbyTick.png"));
	Icon nokIcon = new ImageIcon(this.getClass().getResource("ChubbyX.png"));
	Icon trashIcon = new ImageIcon(this.getClass().getResource("ChubbyTrash.png"));
	
	JLabel aboveSeparatorLabel = new JLabel();
	JLabel pickSeparatorLabel = new JLabel();
	JLabel liftedSeparatorLabel = new JLabel();
	JLabel beforePalletLabel = new JLabel();
	JLabel abovePalletLabel = new JLabel();
	JLabel releaseSeparatorLabel = new JLabel();
	
	JLabel aboveSeparatorDefined = new JLabel();
	JLabel pickSeparatorDefined = new JLabel();
	JLabel liftedSeparatorDefined = new JLabel();
	JLabel beforePalletDefined = new JLabel();
	JLabel abovePalletDefined = new JLabel();
	JLabel releaseSeparatorDefined = new JLabel();
	
	JButton aboveSeparatorSetButton = new JButton();
	JButton pickSeparatorSetButton = new JButton();
	JButton liftedSeparatorSetButton = new JButton();
	JButton beforePalletSetButton = new JButton();
	JButton abovePalletSetButton = new JButton();
	JButton releaseSeparatorSetButton = new JButton();
	
	JButton aboveSeparatorMoveButton = new JButton();
	JButton pickSeparatorMoveButton = new JButton();
	JButton liftedSeparatorMoveButton = new JButton();
	JButton beforePalletMoveButton = new JButton();
	JButton abovePalletMoveButton = new JButton();
	JButton releaseSeparatorMoveButton = new JButton();

	JButton aboveSeparatorDeleteButton = new JButton(trashIcon);
	JButton pickSeparatorDeleteButton = new JButton(trashIcon);
	JButton liftedSeparatorDeleteButton = new JButton(trashIcon);
	JButton beforePalletDeleteButton = new JButton(trashIcon);
	JButton abovePalletDeleteButton = new JButton(trashIcon);
	JButton releaseSeparatorDeleteButton = new JButton(trashIcon);

	JButton valveButton = new JButton();

	JButton copyButton = new JButton();
	JButton pasteButton = new JButton();
	JLabel clipboardLabel = new JLabel();
	
	public SeparatorProgramNodeView(ViewAPIProvider provider) {
		this.apiProvider = provider;
		this.NODE_COUNT = 0;
		this.CT_FACH = false;
	}
	
	@Override
	public void buildUI(JPanel panel, final ContributionProvider<SeparatorProgramNodeContribution> provider) {
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		// SEPARATOR POSITIONS BUTTONS WITH DESCRIPTIONS
		// ABOVE SEPARATOR
		setTextLabel(aboveSeparatorLabel, "Nad Przekładką", "Above Separator");
		c.fill = GridBagConstraints.NONE;
		c.ipadx = 0;
		c.ipady = 0;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1.0;
		c.weighty = 0;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets = new Insets(10,0,3,0);
		panel.add(aboveSeparatorLabel, c);
		
		aboveSeparatorDefined.setIcon(nokIcon);
		c.insets = new Insets(10,0,6,0);
		c.weightx = 0.2;
		c.gridx = 1;
		panel.add(aboveSeparatorDefined, c);
		
		setupButton(aboveSeparatorSetButton, BUTTON_WIDTH, BUTTON_HEIGHT, "Ustaw Pozycję", "Set Position");
		c.insets = new Insets(0,0,6,0);
		c.weightx = 0.3;
		c.gridx = 2;
		aboveSeparatorSetButton.setActionCommand("ABOVE_SEPARATOR");
		aboveSeparatorSetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				provider.get().setButtonAction(e.getActionCommand());
			}
		});
		panel.add(aboveSeparatorSetButton, c);
		
		setupButton(aboveSeparatorMoveButton, BUTTON_WIDTH, BUTTON_HEIGHT, "Przesuń Tutaj", "Move Here");
		c.gridx = 3;
		aboveSeparatorMoveButton.setActionCommand("ABOVE_SEPARATOR");
		aboveSeparatorMoveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				provider.get().moveButtonAction(e.getActionCommand());
			}
		});
		panel.add(aboveSeparatorMoveButton, c);

//		c.gridx = 4;
//		aboveSeparatorDeleteButton.setPreferredSize(new Dimension(BUTTON_HEIGHT, BUTTON_HEIGHT));
//		aboveSeparatorDeleteButton.setActionCommand("ABOVE_SEPARATOR");
//		aboveSeparatorDeleteButton.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				provider.get().deleteButtonAction(e.getActionCommand());
//			}
//		});
//		panel.add(aboveSeparatorDeleteButton, c);
		
		// SEPARATOR PICK UP POSITION
		setTextLabel(pickSeparatorLabel, "Pobranie Przekładki", "Separator Pickup");
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1.0;
		c.weighty = 0;
		c.insets = new Insets(10,0,6,0);
		panel.add(pickSeparatorLabel, c);
		
		pickSeparatorDefined.setIcon(nokIcon);
		c.insets = new Insets(10,0,6,0);
		c.weightx = 0.2;
		c.gridx = 1;
		panel.add(pickSeparatorDefined, c);
		
		setupButton(pickSeparatorSetButton, BUTTON_WIDTH, BUTTON_HEIGHT, "Ustaw Pozycję", "Set Position");
		c.insets = new Insets(0,0,6,0);
		c.weightx = 0.3;
		c.gridx = 2;
		pickSeparatorSetButton.setActionCommand("PICKUP_SEPARATOR");
		pickSeparatorSetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				provider.get().setButtonAction(e.getActionCommand());
			}
		});
		panel.add(pickSeparatorSetButton, c);
		
		setupButton(pickSeparatorMoveButton, BUTTON_WIDTH, BUTTON_HEIGHT, "Przesuń Tutaj", "Move Here");
		c.gridx = 3;
		pickSeparatorMoveButton.setActionCommand("PICKUP_SEPARATOR");
		pickSeparatorMoveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				provider.get().moveButtonAction(e.getActionCommand());
			}
		});
		panel.add(pickSeparatorMoveButton, c);

//		c.gridx = 4;
//		pickSeparatorDeleteButton.setPreferredSize(new Dimension(BUTTON_HEIGHT, BUTTON_HEIGHT));
//		pickSeparatorDeleteButton.setActionCommand("PICKUP_SEPARATOR");
//		pickSeparatorDeleteButton.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				provider.get().deleteButtonAction(e.getActionCommand());
//			}
//		});
//		panel.add(pickSeparatorDeleteButton, c);
		
		// SEPARATOR LIFTED POSITION
		setTextLabel(liftedSeparatorLabel, "Przekładka Podniesiona", "Separator Lifted");
		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 1.0;
		c.weighty = 0;
		c.insets = new Insets(10,0,6,0);
		panel.add(liftedSeparatorLabel, c);
		
		liftedSeparatorDefined.setIcon(nokIcon);
		c.insets = new Insets(10,0,6,0);
		c.weightx = 0.2;
		c.gridx = 1;
		panel.add(liftedSeparatorDefined, c);
		
		setupButton(liftedSeparatorSetButton, BUTTON_WIDTH, BUTTON_HEIGHT, "Ustaw Pozycję", "Set Position");
		c.insets = new Insets(0,0,6,0);
		c.weightx = 0.3;
		c.gridx = 2;
		liftedSeparatorSetButton.setActionCommand("LIFTED_SEPARATOR");
		liftedSeparatorSetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				provider.get().setButtonAction(e.getActionCommand());
			}
		});
		panel.add(liftedSeparatorSetButton, c);
		
		setupButton(liftedSeparatorMoveButton, BUTTON_WIDTH, BUTTON_HEIGHT, "Przesuń Tutaj", "Move Here");
		c.gridx = 3;
		liftedSeparatorMoveButton.setActionCommand("LIFTED_SEPARATOR");
		liftedSeparatorMoveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				provider.get().moveButtonAction(e.getActionCommand());
			}
		});
		panel.add(liftedSeparatorMoveButton, c);

//		c.gridx = 4;
//		liftedSeparatorDeleteButton.setPreferredSize(new Dimension(BUTTON_HEIGHT, BUTTON_HEIGHT));
//		liftedSeparatorDeleteButton.setActionCommand("LIFTED_SEPARATOR");
//		liftedSeparatorDeleteButton.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				provider.get().deleteButtonAction(e.getActionCommand());
//			}
//		});
//		panel.add(liftedSeparatorDeleteButton, c);
		
		// BEFORE PALLET POSITION
		setTextLabel(beforePalletLabel, "Pozycja Przed Paletą", "Before Pallet Pose");
		c.gridx = 0;
		c.gridy = 3;
		c.weightx = 1.0;
		c.weighty = 0;
		c.insets = new Insets(10,0,6,0);
		panel.add(beforePalletLabel, c);
		
		beforePalletDefined.setIcon(nokIcon);
		c.insets = new Insets(10,0,6,0);
		c.weightx = 0.2;
		c.gridx = 1;
		panel.add(beforePalletDefined, c);
		
		setupButton(beforePalletSetButton, BUTTON_WIDTH, BUTTON_HEIGHT, "Ustaw Pozycję", "Set Position");
		c.insets = new Insets(0,0,6,0);
		c.weightx = 0.3;
		c.gridx = 2;
		beforePalletSetButton.setActionCommand("BEFORE_PALLET");
		beforePalletSetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				provider.get().setButtonAction(e.getActionCommand());
			}
		});
		panel.add(beforePalletSetButton, c);
		
		setupButton(beforePalletMoveButton, BUTTON_WIDTH, BUTTON_HEIGHT, "Przesuń Tutaj", "Move Here");
		c.gridx = 3;
		beforePalletMoveButton.setActionCommand("BEFORE_PALLET");
		beforePalletMoveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				provider.get().moveButtonAction(e.getActionCommand());
			}
		});
		panel.add(beforePalletMoveButton, c);

//		c.gridx = 4;
//		beforePalletDeleteButton.setPreferredSize(new Dimension(BUTTON_HEIGHT, BUTTON_HEIGHT));
//		beforePalletDeleteButton.setActionCommand("BEFORE_PALLET");
//		beforePalletDeleteButton.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				provider.get().deleteButtonAction(e.getActionCommand());
//			}
//		});
//		panel.add(beforePalletDeleteButton, c);
		
		// ABOVE PALLET POSITION
		setTextLabel(abovePalletLabel, "Pozycja Nad Paletą", "Above Pallet Pose");
		c.gridx = 0;
		c.gridy = 4;
		c.weightx = 1.0;
		c.weighty = 0;
		c.insets = new Insets(10,0,6,0);
		panel.add(abovePalletLabel, c);
		
		abovePalletDefined.setIcon(nokIcon);
		c.insets = new Insets(10,0,6,0);
		c.weightx = 0.2;
		c.gridx = 1;
		panel.add(abovePalletDefined, c);
		
		setupButton(abovePalletSetButton, BUTTON_WIDTH, BUTTON_HEIGHT, "Ustaw Pozycję", "Set Position");
		c.insets = new Insets(0,0,6,0);
		c.weightx = 0.3;
		c.gridx = 2;
		abovePalletSetButton.setActionCommand("ABOVE_PALLET");
		abovePalletSetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				provider.get().setButtonAction(e.getActionCommand());
			}
		});
		panel.add(abovePalletSetButton, c);
		
		setupButton(abovePalletMoveButton, BUTTON_WIDTH, BUTTON_HEIGHT, "Przesuń Tutaj", "Move Here");
		c.gridx = 3;
		abovePalletMoveButton.setActionCommand("ABOVE_PALLET");
		abovePalletMoveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				provider.get().moveButtonAction(e.getActionCommand());
			}
		});
		panel.add(abovePalletMoveButton, c);

//		c.gridx = 4;
//		abovePalletDeleteButton.setPreferredSize(new Dimension(BUTTON_HEIGHT, BUTTON_HEIGHT));
//		abovePalletDeleteButton.setActionCommand("ABOVE_PALLET");
//		abovePalletDeleteButton.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				provider.get().deleteButtonAction(e.getActionCommand());
//			}
//		});
//		panel.add(abovePalletDeleteButton, c);
		
		// RELEASE POSITION
		setTextLabel(releaseSeparatorLabel, "Pozycja Odłożenia Przekładki", "Separator Release Pose");
		c.gridx = 0;
		c.gridy = 5;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.insets = new Insets(10,0,6,0);
		panel.add(releaseSeparatorLabel, c);
		
		releaseSeparatorDefined.setIcon(nokIcon);
		c.insets = new Insets(10,0,6,0);
		c.weightx = 0.2;
		c.gridx = 1;
		panel.add(releaseSeparatorDefined, c);
		
		setupButton(releaseSeparatorSetButton, BUTTON_WIDTH, BUTTON_HEIGHT, "Ustaw Pozycję", "Set Position");
		c.insets = new Insets(0,0,6,0);
		c.weightx = 0.3;
		c.gridx = 2;
		releaseSeparatorSetButton.setActionCommand("RELEASE_SEPARATOR");
		releaseSeparatorSetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				provider.get().setButtonAction(e.getActionCommand());
			}
		});
		panel.add(releaseSeparatorSetButton, c);
		
		setupButton(releaseSeparatorMoveButton, BUTTON_WIDTH, BUTTON_HEIGHT, "Przesuń Tutaj", "Move Here");
		c.gridx = 3;
		releaseSeparatorMoveButton.setActionCommand("RELEASE_SEPARATOR");
		releaseSeparatorMoveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				provider.get().moveButtonAction(e.getActionCommand());
			}
		});
		panel.add(releaseSeparatorMoveButton, c);

//		c.gridx = 4;
//		releaseSeparatorDeleteButton.setPreferredSize(new Dimension(BUTTON_HEIGHT, BUTTON_HEIGHT));
//		releaseSeparatorDeleteButton.setActionCommand("RELEASE_SEPARATOR");
//		releaseSeparatorDeleteButton.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				provider.get().deleteButtonAction(e.getActionCommand());
//			}
//		});
//		panel.add(releaseSeparatorDeleteButton, c);

		// A LITTLE SEPARATOR FIRST
		final JSeparator spacer = new JSeparator();
		spacer.setOrientation(SwingConstants.HORIZONTAL);
		spacer.setPreferredSize(new Dimension(1, 2));
		c.gridy++;
		c.gridx = 0;
		c.insets = new Insets(10, 0, 10, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 5;
		c.weightx = 1.0;
		c.weighty = 0;
		panel.add(spacer, c);

		// ADD BUTTON TO ACTIVATE SUCTION
		setupButton(valveButton, 220, BUTTON_HEIGHT, "Włącz Ssanie", "Turn On Suction");
		valveButton.setActionCommand("ACTIVATE_VALVE");
		valveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				provider.get().activateValveAction();
			}
		});

		c.fill = GridBagConstraints.NONE;
		c.ipadx = 0;
		c.ipady = 0;
		c.gridx = 0;
		c.gridy++;
		c.weighty = 1.0;
		c.weightx = 1.0;
		c.insets = new Insets(10, 0, 6, 0);
		c.anchor = GridBagConstraints.CENTER;
		panel.add(valveButton, c);

		// ADD COPY AND PASTE BUTTONS WITH A LITTLE DESCRIPTION OF WHAT IS IN THE CLIPBOARD
		setupButton(copyButton, BUTTON_WIDTH, BUTTON_HEIGHT, "Kopiuj Pozycje", "Copy Positions");
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
		panel.add(copyButton, c);

		setTextLabel(clipboardLabel, "", "");
		clipboardLabel.setPreferredSize(new Dimension(340, BUTTON_HEIGHT));
		clipboardLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		clipboardLabel.setHorizontalAlignment(SwingConstants.LEFT);
		clipboardLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		c.gridx = 1;
		c.weightx = 1.0;
		panel.add(clipboardLabel, c);

		setupButton(pasteButton, BUTTON_WIDTH, BUTTON_HEIGHT, "Wklej Pozycje", "Paste Positions");
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
		panel.add(pasteButton, c);
	}
	
	private void setTextLabel(JLabel label, String plString, String enString) {
		if ("pl".equals(Locale.getDefault().getLanguage())) {
			label.setText(plString);
		} else {
			label.setText(enString);
		}
	}
	
	private void iconSetter(JLabel label, boolean defined) {
		if (defined) {
			label.setIcon(okIcon);
		} else {
			label.setIcon(nokIcon);
		}
	}
	
	public void setDefinedIcon(int i, boolean isDefined) {
		switch(i) {
		case 0:
			iconSetter(aboveSeparatorDefined, isDefined);
			break;
		case 1:
			iconSetter(pickSeparatorDefined, isDefined);
			break;
		case 2:
			iconSetter(liftedSeparatorDefined, isDefined);
			break;
		case 3:
			iconSetter(beforePalletDefined, isDefined);
			break;
		case 4:
			iconSetter(abovePalletDefined, isDefined);
			break;
		case 5:
			iconSetter(releaseSeparatorDefined, isDefined);
			break;
		}
	}

	public void valveButtonColor(final boolean set) {
		if (!set) {
			valveButton.setBackground(new Color(86, 160, 211));
			if ("pl".equals(Locale.getDefault().getLanguage())) {
				valveButton.setText("Wyłącz Ssanie");
			} else {
				valveButton.setText("Turn Off Suction");
			}
		} else {
			valveButton.setBackground(new Color(255, 255, 255));
			if ("pl".equals(Locale.getDefault().getLanguage())) {
				valveButton.setText("Włącz Ssanie");
			} else {
				valveButton.setText("Turn On Suction");
			}
		}
	}

	public void resetNodeCounter() {
		this.NODE_COUNT = 0;
	}

	private void setupButton(JButton button, int width, int height, String plDescription, String enDescription) {
		button.setPreferredSize(new Dimension(width, height));
		if ("pl".equals(Locale.getDefault().getLanguage())) {
			button.setText(plDescription);
		} else {
			button.setText(enDescription);
		}
	}
	
	public void enablePasteButton(boolean enable) {
		pasteButton.setEnabled(enable);
	}
	
	public void clipboardSetText(int refNumber, String pallet) {
		String polString = "<html>Zawartość Schowka:<br>Referencja " + refNumber + ", Paleta " + pallet + "</html>";
		String engString = "<html>Clipboard Contents:<br>Reference " + refNumber + ", " + pallet + " Pallet</html>";
		
		setTextLabel(clipboardLabel, polString, engString);
	}
	
	public void clipboardResetText() {
		setTextLabel(clipboardLabel, "", "");
	}

}
