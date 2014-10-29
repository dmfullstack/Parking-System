/**
 * 
 */
package cs414.fmaster.parking.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cs414.fmaster.parking.controller.MainController;

/**
 * @author MasterF
 * 
 */
public class ConfigParkingSizeUI {

	private MainUI mainUI;
	private MainController mainController;
	public JPanel mainContentPnl = new JPanel(new GridBagLayout());
	JLabel currentSizeLbl = new JLabel();
	JLabel currentOccupancyLbl = new JLabel();
	private JTextField newSizeTxt = new JTextField(5);

	private static ConfigParkingSizeUI instance = null;

	private ConfigParkingSizeUI(MainUI mainUI, MainController mainController) {
		this.mainUI = mainUI;
		this.mainController = mainController;
	}

	public static ConfigParkingSizeUI getInstance(MainUI mainUI, MainController mainController) {
		if (instance == null) {
			instance = new ConfigParkingSizeUI(mainUI, mainController);
		}
		return instance;
	}

	public void setupUI() {
		mainContentPnl.setVisible(false);

		// Configure Parking Size Panel
		JPanel configSizePanel = new JPanel(new GridBagLayout());
		currentSizeLbl.setText("Current Parking Size: " + mainController.parkingOpsHandler.getCurrentParkingSize());
		currentOccupancyLbl.setText("Current Occupied Slots: " + mainController.parkingOpsHandler.getCurrentParkingOccupancy());
		JLabel enterNewSizeLbl = new JLabel("Enter new size: ");
		JButton updateSizeBtn = new JButton("Update Parking Size");
		mainUI.addGridBagComponent(configSizePanel, currentSizeLbl, GridBagConstraints.HORIZONTAL, 0, 0, 2, 1);
		mainUI.addGridBagComponent(configSizePanel, currentOccupancyLbl, GridBagConstraints.HORIZONTAL, 0, 1, 2, 1);
		mainUI.addGridBagComponent(configSizePanel, enterNewSizeLbl, GridBagConstraints.BOTH, 0, 2);
		mainUI.addGridBagComponent(configSizePanel, newSizeTxt, GridBagConstraints.NONE, 1, 2);
		mainUI.addGridBagComponent(configSizePanel, updateSizeBtn, GridBagConstraints.NONE, 0, 3, 2, 1);
		updateSizeBtn.addActionListener(new ConfigParkingSizeUIListener());

		// Main Content Panel
		mainUI.addGridBagComponent(mainContentPnl, configSizePanel, GridBagConstraints.NONE, 0, 0, 2, 1);
		JButton backBtn = new JButton("Go Back");
		JButton logoutBtn = new JButton("Logout");
		mainUI.addGridBagComponent(mainContentPnl, backBtn, GridBagConstraints.BOTH, 0, 1);
		mainUI.addGridBagComponent(mainContentPnl, logoutBtn, GridBagConstraints.BOTH, 1, 1);
		backBtn.addActionListener(new ConfigParkingSizeUIListener());
		logoutBtn.addActionListener(new ConfigParkingSizeUIListener());

		mainUI.addGridBagComponent(mainUI.mainPnl, mainContentPnl, GridBagConstraints.BOTH, 0, 0);
	}

	public void resetUI() {
		newSizeTxt.setText("");
	}

	private class ConfigParkingSizeUIListener implements ActionListener {
		private ConfigParkingSizeUIListener() {
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("Update Parking Size")) {
				String newSize = newSizeTxt.getText();
				if (newSize.matches("[0-9]+")) {
					if (newSize.matches("[0]+")) {
						JOptionPane.showMessageDialog(mainUI, "New size cannot be zero.", "Error", JOptionPane.ERROR_MESSAGE);
					} else {
						int newSizeInt = Integer.parseInt(newSize);
						if (newSizeInt < mainController.parkingOpsHandler.getCurrentParkingOccupancy()) {
							JOptionPane.showMessageDialog(mainUI, "New Parking Size cannot be less than number of occupied slots.", "Error",
									JOptionPane.ERROR_MESSAGE);
						} else {
							if (newSizeInt == mainController.parkingOpsHandler.getCurrentParkingSize()) {
								JOptionPane.showMessageDialog(mainUI, "New Parking Size is same as current parking size.", "Message",
										JOptionPane.INFORMATION_MESSAGE);
							} else {
								mainController.parkingOpsHandler.updateParkingSize(newSizeInt);
								JOptionPane.showMessageDialog(mainUI, "Parking Size updated.", "Message",
										JOptionPane.INFORMATION_MESSAGE);
							}
							resetUI();
							mainUI.showHideContentPanel(mainUI.adminUI.mainContentPnl, mainContentPnl);
						}
					}
				} else {
					JOptionPane.showMessageDialog(mainUI, "Enter valid new size.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			if (e.getActionCommand().equals("Go Back")) {
				resetUI();
				mainUI.showHideContentPanel(mainUI.adminUI.mainContentPnl, mainContentPnl);
			}
			if (e.getActionCommand().equals("Logout")) {
				JOptionPane.showMessageDialog(mainUI, "Logging out...", "Logout", JOptionPane.INFORMATION_MESSAGE);
				mainController.adminOpsHandler.logout();
				resetUI();
				mainUI.showHideContentPanel(mainUI.mainContentPnl, mainContentPnl);
				mainUI.displayWelcomeMessage();
			}
		}
	}

}
