/**
 * 
 */
package a5.fmaster.src.main.java.client.ui;

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

import a5.fmaster.src.main.java.common.ParkingServerInterface;

/**
 * @author MasterF
 * 
 */
public class ConfigParkingSizeUI {

	private MainUI mainUI;
	private ParkingServerInterface parking;
	public JPanel mainContentPnl = new JPanel(new GridBagLayout());
	JLabel currentSizeLbl = new JLabel();
	JLabel currentOccupancyLbl = new JLabel();
	private JTextField newSizeTxt = new JTextField(5);

	private static ConfigParkingSizeUI instance = null;

	private ConfigParkingSizeUI(MainUI mainUI, ParkingServerInterface parking) {
		this.mainUI = mainUI;
		this.parking = parking;
	}

	public static ConfigParkingSizeUI getInstance(MainUI mainUI, ParkingServerInterface parking) {
		if (instance == null) {
			instance = new ConfigParkingSizeUI(mainUI, parking);
		}
		return instance;
	}

	public void setupUI() throws RemoteException {
		mainContentPnl.setVisible(false);

		// Configure Parking Size Panel
		JPanel configSizePanel = new JPanel(new GridBagLayout());
		currentSizeLbl.setText("Current Parking Size: " + parking.getCurrentParkingSize());
		currentOccupancyLbl.setText("Current Occupied Slots: " + parking.getCurrentParkingOccupancy());
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
				JOptionPane.showMessageDialog(mainUI, "Logging out...", "Logout", JOptionPane.INFORMATION_MESSAGE);
				parking.logout();
				resetUI();
				mainUI.showHideContentPanel(mainUI.mainContentPnl, mainContentPnl);
				mainUI.updateWelcomeMessage();
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}

		private void goBack() {
			resetUI();
			mainUI.showHideContentPanel(mainUI.adminUI.mainContentPnl, mainContentPnl);
		}

		private void updateParkingSize() {
			try {
				String newSize = newSizeTxt.getText();
				if (newSize.matches("[0-9]+")) {
					if (newSize.matches("[0]+")) {
						JOptionPane.showMessageDialog(mainUI, "New size cannot be zero.", "Error", JOptionPane.ERROR_MESSAGE);
					} else {
						int newSizeInt = Integer.parseInt(newSize);
						if (newSizeInt < parking.getCurrentParkingOccupancy()) {
							JOptionPane.showMessageDialog(mainUI, "New Parking Size cannot be less than number of occupied slots.", "Error",
									JOptionPane.ERROR_MESSAGE);
						} else {
							if (newSizeInt == parking.getCurrentParkingSize()) {
								JOptionPane.showMessageDialog(mainUI, "New Parking Size is same as current parking size.", "Message",
										JOptionPane.INFORMATION_MESSAGE);
							} else {
								parking.updateParkingSize(newSizeInt);
								JOptionPane.showMessageDialog(mainUI, "Parking Size updated.", "Message", JOptionPane.INFORMATION_MESSAGE);
							}
							goBack();
						}
					}
				} else {
					JOptionPane.showMessageDialog(mainUI, "Enter valid new size.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}
	}

}
