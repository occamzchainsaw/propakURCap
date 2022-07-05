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

	private final ViewAPIProvider apiProvider;

	public int NODE_COUNT;
	public boolean CT_FACH;
	
	private final static int BUTTON_WIDTH = 150;
	private final static int BUTTON_HEIGHT = 80;
	private final static int NUMBER_OF_POSITIONS = 6;
	
	Icon okIcon = new ImageIcon(this.getClass().getResource("/ChubbyTick.png"));
	Icon nokIcon = new ImageIcon(this.getClass().getResource("/ChubbyX.png"));
	Icon trashIcon = new ImageIcon(this.getClass().getResource("/ChubbyTrash.png"));

	JButton valveButton = new JButton();

	JButton copyButton = new JButton();
	JButton pasteButton = new JButton();
	JLabel clipboardLabel = new JLabel();
	
	
	// NEW SET BUTTONS
	JButton[] setButtons1 = new JButton[NUMBER_OF_POSITIONS];
	JButton[] setButtons2 = new JButton[NUMBER_OF_POSITIONS];
	
	// NEW MOVE BUTTONS
	JButton[] moveButtons1 = new JButton[NUMBER_OF_POSITIONS];
	JButton[] moveButtons2 = new JButton[NUMBER_OF_POSITIONS];
	
	// NEW DESCRIPTIONS
	JLabel[] descriptionLabels = new JLabel[NUMBER_OF_POSITIONS];
	
	public SeparatorProgramNodeView(ViewAPIProvider provider) {
		this.apiProvider = provider;
		this.NODE_COUNT = 0;
		this.CT_FACH = false;
	}
	
	@Override
	public void buildUI(JPanel panel, final ContributionProvider<SeparatorProgramNodeContribution> provider) {
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		// DESCRIPTION
		c.fill = GridBagConstraints.NONE;
		c.ipadx = 0;
		c.ipady = 0;
		c.gridx = 1;
		c.gridy = 0;
		c.weighty = 1.0;
		c.insets = new Insets(0, 0, 0, 0);
		c.anchor = GridBagConstraints.NORTHWEST;
		JLabel descType1 = new JLabel();
		if ("pl".equals(Locale.getDefault().getLanguage())) {
			descType1.setText("Typ 1");
		} else {
			descType1.setText("Type 1");
		}
		descType1.setHorizontalTextPosition(SwingConstants.CENTER);
		descType1.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(descType1, c);
		
		c.gridx = 3;
		JLabel descType2 = new JLabel();
		if ("pl".equals(Locale.getDefault().getLanguage())) {
			descType2.setText("Typ 2");
		} else {
			descType2.setText("Type 2");
		}
		descType2.setHorizontalTextPosition(SwingConstants.CENTER);
		descType2.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(descType2, c);
		
		// NEW BUTTON ROWS
		for (int i = 0; i < NUMBER_OF_POSITIONS; ++i) {
			// GENERATE TYPE 1 SET BUTTONS
			final JButton btn = new JButton();
			if ("pl".equals(Locale.getDefault().getLanguage())) {
				btn.setText("<html>Ustaw<br>Pozycję</html>");
			} else {
				btn.setText("<html>Set<br>Position</html>");
			}
			btn.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
			btn.setActionCommand("POSE_1_" + String.valueOf(i));
			btn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(final ActionEvent e) {
					provider.get().setButtonAction(e.getActionCommand());
				}

			});
			setButtons1[i] = btn;
			
			//GENERATE TYPE 1 MOVE BUTTONS
			final JButton btn1 = new JButton();
			if ("pl".equals(Locale.getDefault().getLanguage())) {
				btn1.setText("<html>Przesuń<br>Tutaj<html>");
			} else {
				btn1.setText("<html>Move<br>Here</html>");
			}
			btn1.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
			btn1.setActionCommand("POSE_1_" + String.valueOf(i));
			btn1.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(final ActionEvent e) {
					provider.get().moveButtonAction(e.getActionCommand());
				}

			});
			moveButtons1[i] = btn1;
			
			// GENERATE TYPE 2 SET BUTTONS
			final JButton btn3 = new JButton();
			if ("pl".equals(Locale.getDefault().getLanguage())) {
				btn3.setText("<html>Ustaw<br>Pozycję</html>");
			} else {
				btn3.setText("<html>Set<br>Position</html>");
			}
			btn3.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
			btn3.setActionCommand("POSE_2_" + String.valueOf(i));
			btn3.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(final ActionEvent e) {
					provider.get().setButtonAction(e.getActionCommand());
				}

			});
			setButtons2[i] = btn3;
			
			//GENERATE TYPE 2 MOVE BUTTONS
			final JButton btn4 = new JButton();
			if ("pl".equals(Locale.getDefault().getLanguage())) {
				btn4.setText("<html>Przesuń<br>Tutaj</html>");
			} else {
				btn4.setText("<html>Move<br>Here</html>");
			}
			btn4.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
			btn4.setActionCommand("POSE_2_" + String.valueOf(i));
			btn4.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(final ActionEvent e) {
					provider.get().moveButtonAction(e.getActionCommand());
				}

			});
			moveButtons2[i] = btn4;
			
			// GENERATE LABELS
			final JLabel dsc = new JLabel();
			switch (i) {
				case 0:
					if ("pl".equals(Locale.getDefault().getLanguage())) {
						dsc.setText("Nad przekładką");
					} else {
						dsc.setText("Above separator");
					}
					break;
					
				case 1:
					if ("pl".equals(Locale.getDefault().getLanguage())) {
						dsc.setText("Pobranie przekładki");
					} else {
						dsc.setText("Separator pickup");
					}
					break;
					
				case 2:
					if ("pl".equals(Locale.getDefault().getLanguage())) {
						dsc.setText("Przekładka podniesiona");
					} else {
						dsc.setText("Separator Lifted");
					}
					break;
					
				case 3:
					if ("pl".equals(Locale.getDefault().getLanguage())) {
						dsc.setText("Pozycja przed paletą");
					} else {
						dsc.setText("Before pallet pose");
					}
					break;
					
				case 4:
					if ("pl".equals(Locale.getDefault().getLanguage())) {
						dsc.setText("Pozycja nad paletą");
					} else {
						dsc.setText("Above pallet pose");
					}
					break;
					
				case 5:
					if ("pl".equals(Locale.getDefault().getLanguage())) {
						dsc.setText("Pozycja odłożenia");
					} else {
						dsc.setText("Release pose");
					}
					break;
					
				default:
					break;
			}
			descriptionLabels[i] = dsc;
			
			createButtonRow(panel, c, descriptionLabels, setButtons1, moveButtons1, setButtons2, moveButtons2, i);
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
//		setupButton(valveButton, 220, BUTTON_HEIGHT, "Włącz Ssanie", "Turn On Suction");
//		valveButton.setActionCommand("ACTIVATE_VALVE");
//		valveButton.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(final ActionEvent e) {
//				provider.get().activateValveAction();
//			}
//		});
//
//		c.fill = GridBagConstraints.NONE;
//		c.ipadx = 0;
//		c.ipady = 0;
//		c.gridx = 0;
//		c.gridy++;
//		c.weighty = 1.0;
//		c.weightx = 1.0;
//		c.insets = new Insets(10, 0, 6, 0);
//		c.anchor = GridBagConstraints.SOUTHWEST;
//		panel.add(valveButton, c);

		// ADD COPY AND PASTE BUTTONS WITH A LITTLE DESCRIPTION OF WHAT IS IN THE CLIPBOARD
		setupButton(copyButton, BUTTON_WIDTH+20, BUTTON_HEIGHT, "Kopiuj Pozycje", "Copy Positions");
		copyButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				provider.get().copy();
			}
		});
		c.gridy++;
		c.gridx = 0;
		c.gridwidth = 1;
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
		c.gridwidth = 4;
		c.weightx = 3.0;
		panel.add(clipboardLabel, c);

		setupButton(pasteButton, BUTTON_WIDTH+20, BUTTON_HEIGHT, "Wklej Pozycje", "Paste Positions");
		pasteButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				provider.get().paste();
			}
		});
		pasteButton.setEnabled(false);
		c.gridx = 4;
		c.gridwidth = 1;
		c.weightx = 0.3;
		c.anchor = GridBagConstraints.SOUTHEAST;
		c.fill = GridBagConstraints.NONE;
		panel.add(pasteButton, c);
	}
	
	private void createButtonRow(final JPanel panel, final GridBagConstraints c, final JLabel[] descs, 
			final JButton[] setBtns1, final JButton[] moveBtns1, final JButton[] setBtns2, final JButton[] moveBtns2, final int n) {
		c.fill = GridBagConstraints.NONE;
		c.ipadx = 0;
		c.ipady = 0;
		c.gridx = 0;
		c.gridy = n+1;
		c.weighty = 0.0;
		c.insets = new Insets(0, 0, 0, 0);
		c.anchor = GridBagConstraints.NORTHWEST;
		
		final JLabel desc = descs[n];
		desc.setHorizontalAlignment(JLabel.LEFT);
		desc.setVerticalAlignment(JLabel.CENTER);
		c.insets = new Insets(10, 0, 6, 0);
		c.weightx = 0.3;
		panel.add(desc, c);
		
		final JButton setBtn = setBtns1[n];
//		c.insets = new Insets(0, 0, 6, 0);
		c.gridx = 1;
		c.gridy = n+1;
		c.weightx = 0.5;
		panel.add(setBtn, c);
		
		final JButton moveBtn = moveBtns1[n];
		c.gridx = 2;
		c.gridy = n+1;
		c.weightx = 0.5;
		panel.add(moveBtn, c);
		
		final JButton setBtn2 = setBtns2[n];
		c.gridx = 3;
		c.gridy = n+1;
		c.weightx = 0.5;
		panel.add(setBtn2, c);
		
		final JButton moveBtn2 = moveBtns2[n];
		c.gridx = 4;
		c.gridy = n+1;
		c.weightx = 0.5;
		panel.add(moveBtn2, c);
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
	
//	public void setDefinedIcon(int i, boolean isDefined) {
//		switch(i) {
//		case 0:
//			iconSetter(aboveSeparatorDefined, isDefined);
//			break;
//		case 1:
//			iconSetter(pickSeparatorDefined, isDefined);
//			break;
//		case 2:
//			iconSetter(liftedSeparatorDefined, isDefined);
//			break;
//		case 3:
//			iconSetter(beforePalletDefined, isDefined);
//			break;
//		case 4:
//			iconSetter(abovePalletDefined, isDefined);
//			break;
//		case 5:
//			iconSetter(releaseSeparatorDefined, isDefined);
//			break;
//		}
//	}

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
