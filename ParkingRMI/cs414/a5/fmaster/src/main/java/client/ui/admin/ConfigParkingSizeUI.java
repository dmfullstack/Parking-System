/**
 * 
 */
package a5.fmaster.src.main.java.client.ui.admin;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import a5.fmaster.src.main.java.common.ParkingInterface;

/**
 * @author MasterF
 * 
 */
public class ConfigParkingSizeUI {

	private AdminMainUI adminMainUI;
	private ParkingInterface parking;
	public JPanel mainContentPnl = new JPanel(new GridBagLayout());
	JLabel currentSizeLbl = new JLabel();
	JLabel currentOccupancyLbl = new JLabel();
	private JTextField newSizeTxt = new JTextField(5);

	private static ConfigParkingSizeUI instance = null;

	private ConfigParkingSizeUI(AdminMainUI adminMainUI, ParkingInterface parking) {
		this.adminMainUI = adminMainUI;
		this.parking = parking;
	}

	public static ConfigParkingSizeUI getInstance(AdminMainUI adminMainUI, ParkingInterface parking) {
		if (instance == null) {
			instance = new ConfigParkingSizeUI(adminMainUI, parking);
		}
		return instance;
	}

	public void setupUI() throws RemoteException {
		mainContentPnl.setVisible(false);

		// Configure Parking Size Panel
		JPanel configSizePanel = new JPanel(new GridBagLayout());
		updateAvailabilityAndSize();
		JLabel enterNewSizeLbl = new JLabel("Enter new size: ");
		JButton updateSizeBtn = new JButton("Update Parking Size");
		adminMainUI.addGridBagComponent(configSizePanel, currentSizeLbl, GridBagConstraints.HORIZONTAL, 0, 0, 2, 1);
		adminMainUI.addGridBagComponent(configSizePanel, currentOccupancyLbl, GridBagConstraints.HORIZONTAL, 0, 1, 2, 1);
		adminMainUI.addGridBagComponent(configSizePanel, enterNewSizeLbl, GridBagConstraints.BOTH, 0, 2);
		adminMainUI.addGridBagComponent(configSizePanel, newSizeTxt, GridBagConstraints.NONE, 1, 2);
		adminMainUI.addGridBagComponent(configSizePanel, updateSizeBtn, GridBagConstraints.NONE, 0, 3, 2, 1);
		updateSizeBtn.addActionListener(new ConfigParkingSizeUIListener());

		// Main Content Panel
		adminMainUI.addGridBagComponent(mainContentPnl, configSizePanel, GridBagConstraints.NONE, 0, 0, 2, 1);
		JButton backBtn = new JButton("Go Back");
		JButton logoutBtn = new JButton("Logout");
		adminMainUI.addGridBagComponent(mainContentPnl, backBtn, GridBagConstraints.BOTH, 0, 1);
		adminMainUI.addGridBagComponent(mainContentPnl, logoutBtn, GridBagConstraints.BOTH, 1, 1);
		backBtn.addActionListener(new ConfigParkingSizeUIListener());
		logoutBtn.addActionListener(new ConfigParkingSizeUIListener());

		adminMainUI.addGridBagComponent(adminMainUI.mainPnl, mainContentPnl, GridBagConstraints.BOTH, 0, 0);
	}

	public void updateAvailabilityAndSize() throws RemoteException {
		currentSizeLbl.setText("Current Parking Size: " + parking.getCurrentParkingSize());
		currentOccupancyLbl.setText("Current Occupied Slots: " + parking.getCurrentParkingOccupancy());
	}

	public void resetUI() {
		newSizeTxt.setText("");
	}

	private class ConfigParkingSizeUIListener implements ActionListener {
		private ConfigParkingSizeUIListener() {
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("Update Parking Size")) {
				updateParkingSize();
			}
			if (e.getActionCommand().equals("Go Back")) {
				goBack();
			}
			if (e.getActionCommand().equals("Logout")) {
				logout();
			}
		}

		private void logout() {
			try {
				JOptionPane.showMessageDialog(adminMainUI, "Logging out...", "Logout", JOptionPane.INFORMATION_MESSAGE);
				parking.logout();
				resetUI();
				adminMainUI.showHideContentPanel(adminMainUI.mainContentPnl, mainContentPnl);
				adminMainUI.updateWelcomeMessage();
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}

		private void goBack() {
			resetUI();
			adminMainUI.showHideContentPanel(adminMainUI.adminUI.mainContentPnl, mainContentPnl);
		}

		private void updateParkingSize() {
			try {
				String newSize = newSizeTxt.getText();
				if (newSize.matches("[0-9]+")) {
					if (newSize.matches("[0]+")) {
						JOptionPane.showMessageDialog(adminMainUI, "New size cannot be zero.", "Error", JOptionPane.ERROR_MESSAGE);
					} else {
						int newSizeInt = Integer.parseInt(newSize);
						if (newSizeInt < parking.getCurrentParkingOccupancy()) {
							JOptionPane.showMessageDialog(adminMainUI, "New parking size cannot be less than number of occupied slots.", "Error",
									JOptionPane.ERROR_MESSAGE);
						} else {
							if (newSizeInt == parking.getCurrentParkingSize()) {
								JOptionPane.showMessageDialog(adminMainUI, "New parking size is same as current parking size.", "Message",
										JOptionPane.INFORMATION_MESSAGE);
							} else {
								parking.updateParkingSize(newSizeInt);
								JOptionPane.showMessageDialog(adminMainUI, "Parking size successfully updated.", "Message", JOptionPane.INFORMATION_MESSAGE);
							}
							goBack();
						}
					}
				} else {
					JOptionPane.showMessageDialog(adminMainUI, "Enter valid size in numbers.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}
	}
}