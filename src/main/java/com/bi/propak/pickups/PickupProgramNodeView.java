package com.bi.propak.pickups;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
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

public class PickupProgramNodeView implements SwingProgramNodeView<PickupProgramNodeContribution>{

	@SuppressWarnings("unused")
	private final ViewAPIProvider apiProvider;
	public int NODE_COUNT;
	public boolean CT_FACH;
	
	public PickupProgramNodeView(final ViewAPIProvider provider) {
		this.apiProvider = provider;
		this.NODE_COUNT = 0;
		this.CT_FACH = false;
	}

	private final static int NUMBER_OF_POSITIONS = 8;
	private final static int BUTTON_WIDTH = 180;
	private final static int BUTTON_HEIGHT = 40;

	JButton setButtons[] = new JButton[NUMBER_OF_POSITIONS];
	JButton moveButtons[] = new JButton[NUMBER_OF_POSITIONS];
	JButton deleteButtons[] = new JButton[NUMBER_OF_POSITIONS];
	JLabel descriptions[] = new JLabel[NUMBER_OF_POSITIONS];
	JLabel defIcons[] = new JLabel[NUMBER_OF_POSITIONS];

	JButton abovePalletSetButton = new JButton();
	JButton abovePalletMoveButton = new JButton();
	
	JLabel abovePalletDescription = new JLabel();
	JLabel abovePalletDefined = new JLabel();
	JLabel clipboardLabel = new JLabel();
	
	JLabel pressureLabel = new JLabel();

	JButton valveButton = new JButton();
	JButton copyButton = new JButton();
	JButton pasteButton = new JButton();

	Icon okIcon = new ImageIcon(this.getClass().getResource("ChubbyTick.png"));
	Icon nokIcon = new ImageIcon(this.getClass().getResource("ChubbyX.png"));
	Icon trashIcon = new ImageIcon(this.getClass().getResource("ChubbyTrash.png"));

	@Override
	public void buildUI(final JPanel panel, final ContributionProvider<PickupProgramNodeContribution> provider) {
		panel.setLayout(new GridBagLayout());
		final GridBagConstraints c = new GridBagConstraints();

		for (int i = 0; i < NUMBER_OF_POSITIONS; ++i) {
			// GENERATE SET POSITION BUTTONS
			final JButton btn = new JButton();
			final int n = i + 1;
			if ("pl".equals(Locale.getDefault().getLanguage())) {
				btn.setText("Ustaw Pozycję");
			} else {
				btn.setText("Set Position");
			}
			btn.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
			btn.setActionCommand("PICKUP_" + String.valueOf(i));
			btn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(final ActionEvent e) {
					provider.get().setPositionAction(e.getActionCommand());
				}

			});
			setButtons[i] = btn;

			// GENERATE MOVE HERE BUTTONS
			final JButton btn1 = new JButton("Move Here");
			if ("pl".equals(Locale.getDefault().getLanguage())) {
				btn1.setText("Przesuń Tutaj");
			} else {
				btn1.setText("Move Here");
			}
			btn1.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
			btn1.setActionCommand("PICKUP_" + String.valueOf(i));
			btn1.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(final ActionEvent e) {
					provider.get().moveToPositionAction(e.getActionCommand());
				}

			});
			moveButtons[i] = btn1;

			//GENERATE DELETE BUTTONS
			final JButton btn2 = new JButton(trashIcon);
			btn2.setPreferredSize(new Dimension(BUTTON_HEIGHT, BUTTON_HEIGHT));
			btn2.setActionCommand("PICKUP_" + String.valueOf(i));
			btn2.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(final ActionEvent e) {
					provider.get().deletePickUp(e.getActionCommand());
				}

			});
			deleteButtons[i] = btn2;

			// GENERATE DESCRIPTIONS FOR EACH PICK UP POSITION
			final JLabel dsc = new JLabel();
			if ("pl".equals(Locale.getDefault().getLanguage())) {
				dsc.setText("Pozycja Pobrania " + n);
			} else {
				dsc.setText("Pick Up Position " + n);
			}
			descriptions[i] = dsc;

			// GENERATE ICON LABELS TO SIGNIFY IF POSITION IS DEFINED OR NOT
			final JLabel defIcon = new JLabel(nokIcon);
			defIcons[i] = defIcon;

			// ADD THE BUTTONS AND DESCIRPTIONS TO THE VIEW
			createPickUpRow(panel, c, descriptions, setButtons, moveButtons, deleteButtons, defIcons, i);
		}

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
		valveButton.setPreferredSize(new Dimension(220, BUTTON_HEIGHT));
		if ("pl".equals(Locale.getDefault().getLanguage())) {
			valveButton.setText("Włącz Ssanie");
		} else {
			valveButton.setText("Turn On Suction");
		}
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
		c.anchor = GridBagConstraints.LINE_START;
		panel.add(valveButton, c);
		
		//	ADD THE PRESSURE READOUT FROM THE ANALOGUE SENSOR
		pressureLabel.setText("PRESSURE");
		//Font f = pressureLabel.getFont();
		//pressureLabel.setFont(f.deriveFont(Font.BOLD));
		//pressureLabel.setFont(f.deriveFont(18.0f));
		pressureLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		c.gridx = 1;
		c.weightx = 1.0;
		c.anchor = GridBagConstraints.LINE_END;
		c.insets = new Insets(0, 0, 0, 20);
		panel.add(pressureLabel, c);

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
		panel.add(copyButton, c);

		//	Clipboard Label
		clipboardLabel.setText("");
		clipboardLabel.setPreferredSize(new Dimension(340, BUTTON_HEIGHT));
		clipboardLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		clipboardLabel.setHorizontalAlignment(SwingConstants.CENTER);
		clipboardLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		c.gridx = 1;
		c.weightx = 1.0;
		panel.add(clipboardLabel, c);

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
		panel.add(pasteButton, c);
	}

	private void createPickUpRow(final JPanel panel, final GridBagConstraints c, final JLabel[] descs,
			final JButton[] setBtns, final JButton[] moveBtns, final JButton[] delBtns, final JLabel defIcns[], final int n) {
		c.fill = GridBagConstraints.NONE;
		c.ipadx = 0;
		c.ipady = 0;
		c.gridx = 0;
		c.gridy = n;
		c.weighty = 0.0;
		c.insets = new Insets(0, 0, 0, 0);
		c.anchor = GridBagConstraints.NORTHWEST;

		final JLabel desc = descs[n];
		desc.setHorizontalAlignment(JLabel.LEFT);
		desc.setVerticalAlignment(JLabel.CENTER);
		c.insets = new Insets(10, 0, 6, 0);
		c.weightx = 1.0;
		panel.add(desc, c);

		final JLabel ic = defIcns[n];
		ic.setVerticalAlignment(JLabel.CENTER);
		c.insets = new Insets(7, 0, 6, 0);
		c.gridx = 1;
		c.gridy = n;
		c.weightx = 0.5;
		panel.add(ic, c);

		final JButton setBtn = setBtns[n];
		c.insets = new Insets(0, 0, 6, 0);
		c.gridx = 2;
		c.gridy = n;
		c.weightx = 0.5;
		panel.add(setBtn, c);

		final JButton moveBtn = moveBtns[n];
		c.gridx = 3;
		c.gridy = n;
		c.weightx = 0.5;
		panel.add(moveBtn, c);

		final JButton delBtn = delBtns[n];
		c.gridx = 4;
		c.gridy = n;
		c.weightx = 0.5;
		panel.add(delBtn, c);
	}

	public void setDefinedIcon(final int i, final boolean isDef) {
		if (isDef) {
			defIcons[i].setIcon(okIcon);
		} else {
			defIcons[i].setIcon(nokIcon);
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
	
	public void setPressureDisplay(double input) {
		if ("pl".contentEquals(Locale.getDefault().getLanguage())) {
			pressureLabel.setText("<html>Podciśnienie: " + "<b><font size=+1>" + Double.valueOf(input).intValue() + "</font></b>" + " mbar</html>");
		} else {
			pressureLabel.setText("<html>Vacuum: " + "<b><font size=+1>" + Double.valueOf(input).intValue() + "</font></b>" + " mbar</html>");
		}
	}

	public void resetNodeCounter() {
		this.NODE_COUNT = 0;
	}

	public void enablePasteButton(boolean enable) {
		pasteButton.setEnabled(enable);
	}

	public void clipboardSetText(int refNumber, String pallet) {
		String out = "";
		if ("pl".equals(Locale.getDefault().getLanguage())) {
			out = "<html>Zawartość Schowka:<br>Referencja " + refNumber + ", Paleta " + pallet + "</html>";
		} else {
			out = "<html>Clipboard Contents:<br>Reference " + refNumber + ", " + pallet + " Pallet</html>";
		}
		
		clipboardLabel.setText(out);
	}

	public void clipboardResetText() {
		clipboardLabel.setText("");
	}

}
