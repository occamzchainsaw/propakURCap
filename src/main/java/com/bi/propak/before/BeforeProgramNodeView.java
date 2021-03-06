package com.bi.propak.before;

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

public class BeforeProgramNodeView implements SwingProgramNodeView<BeforeProgramNodeContribution>{

	@SuppressWarnings("unused")
	private final ViewAPIProvider apiProvider;
	public int NODE_COUNT;
	public boolean CT_FACH;
	
	public BeforeProgramNodeView(final ViewAPIProvider provider) {
		this.apiProvider = provider;
		this.NODE_COUNT = 0;
		this.CT_FACH = false;
	}

	private final static int NUMBER_OF_POSITIONS = 8;
	private final static int BUTTON_WIDTH = 180;
	private final static int BUTTON_HEIGHT = 40;

	JButton setButtons[] = new JButton[NUMBER_OF_POSITIONS];
	JButton moveButtons[] = new JButton[NUMBER_OF_POSITIONS];
	JLabel descriptions[] = new JLabel[NUMBER_OF_POSITIONS];
	JLabel defIcons[] = new JLabel[NUMBER_OF_POSITIONS];

	JLabel clipboardLabel = new JLabel();

	JButton copyButton = new JButton();
	JButton pasteButton = new JButton();

	Icon okIcon = new ImageIcon(this.getClass().getResource("/ChubbyTick.png"));
	Icon nokIcon = new ImageIcon(this.getClass().getResource("/ChubbyX.png"));
	Icon trashIcon = new ImageIcon(this.getClass().getResource("/ChubbyTrash.png"));

	@Override
	public void buildUI(final JPanel panel, final ContributionProvider<BeforeProgramNodeContribution> provider) {
		panel.setLayout(new GridBagLayout());
		final GridBagConstraints c = new GridBagConstraints();

		for (int i = 0; i < NUMBER_OF_POSITIONS; ++i) {
			// GENERATE SET POSITION BUTTONS
			final JButton btn = new JButton();
			final int n = i + 1;
			if ("pl".equals(Locale.getDefault().getLanguage())) {
				btn.setText("Ustaw Pozycj??");
			} else {
				btn.setText("Set Position");
			}
			btn.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
			btn.setActionCommand("POSE_" + String.valueOf(i));
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
				btn1.setText("Przesu?? Tutaj");
			} else {
				btn1.setText("Move Here");
			}
			btn1.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
			btn1.setActionCommand("POSE_" + String.valueOf(i));
			btn1.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(final ActionEvent e) {
					provider.get().moveToPositionAction(e.getActionCommand());
				}

			});
			moveButtons[i] = btn1;

			// GENERATE DESCRIPTIONS FOR EACH POSITION
			final JLabel dsc = new JLabel();
			if ("pl".equals(Locale.getDefault().getLanguage())) {
				dsc.setText("Przed Pobraniem " + n);
			} else {
				dsc.setText("Before Pickup " + n);
			}
			descriptions[i] = dsc;

			// GENERATE ICON LABELS TO SIGNIFY IF POSITION IS DEFINED OR NOT
			final JLabel defIcon = new JLabel(nokIcon);
			defIcons[i] = defIcon;

			// ADD THE BUTTONS AND DESCIRPTIONS TO THE VIEW
			createPickUpRow(panel, c, descriptions, setButtons, moveButtons, defIcons, i);
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
			final JButton[] setBtns, final JButton[] moveBtns, final JLabel defIcns[], final int n) {
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
	}

	public void setDefinedIcon(final int i, final boolean isDef) {
		if (isDef) {
			defIcons[i].setIcon(okIcon);
		} else {
			defIcons[i].setIcon(nokIcon);
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
			out = "<html>Zawarto???? Schowka:<br>Referencja " + refNumber + ", Paleta " + pallet + "</html>";
		} else {
			out = "<html>Clipboard Contents:<br>Reference " + refNumber + ", " + pallet + " Pallet</html>";
		}
		
		clipboardLabel.setText(out);
	}

	public void clipboardResetText() {
		clipboardLabel.setText("");
	}

}
