/**
 * 
 */
package a5.fmaster.src.main.java.client.ui.admin;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import a5.fmaster.src.main.java.common.ParkingServerInterface;
import a5.fmaster.src.main.java.server.domain.ParkingRate;

/**
 * @author MasterF
 * 
 */
public class ConfigRatesUI {

	private AdminMainUI adminMainUI;
	private ParkingServerInterface parking;
	public JPanel mainContentPnl = new JPanel(new GridBagLayout());
	public JTable parkingRatesTbl = new JTable();

	private static ConfigRatesUI instance = null;

	private ConfigRatesUI(AdminMainUI adminMainUI, ParkingServerInterface parking) {
		this.adminMainUI = adminMainUI;
		this.parking = parking;
	}

	public static ConfigRatesUI getInstance(AdminMainUI adminMainUI, ParkingServerInterface parking) {
		if (instance == null) {
			instance = new ConfigRatesUI(adminMainUI, parking);
		}
		return instance;
	}

	public void setupUI() throws RemoteException {
		mainContentPnl.setVisible(false);

		// Parking Rates Panel
		JPanel parkingRatesPnl = new JPanel(new GridBagLayout());
		JLabel parkingRateLbl = new JLabel("Configure Parking Rates");
		DefaultTableModel parkingRatesModel = new DefaultTableModel(new Object[][] {}, new String[] { "Hours", "Rate" });

		parkingRatesTbl.setModel(parkingRatesModel);
		adminMainUI.populateParkingRatesInTable(parkingRatesTbl);
		parkingRatesTbl.getColumnModel().getColumn(1).setCellEditor(parkingRatesTbl.getDefaultEditor(String.class));
		parkingRatesTbl.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		parkingRatesTbl.setFillsViewportHeight(true);

		adminMainUI.addGridBagComponent(parkingRatesPnl, parkingRateLbl, GridBagConstraints.BOTH, 0, 0);
		adminMainUI.addGridBagComponent(parkingRatesPnl, parkingRatesTbl.getTableHeader(), GridBagConstraints.BOTH, 0, 1);
		adminMainUI.addGridBagComponent(parkingRatesPnl, parkingRatesTbl, GridBagConstraints.BOTH, 0, 2);

		// Update Rates button
		JButton updateRatesBtn = new JButton("Update Rates");
		updateRatesBtn.addActionListener(new ConfigRatesUIListener());
		// Back button
		JButton backBtn = new JButton("Go Back");
		backBtn.addActionListener(new ConfigRatesUIListener());
		// Logout button
		JButton logoutBtn = new JButton("Logout");
		logoutBtn.addActionListener(new ConfigRatesUIListener());

		// Main Content Panel
		adminMainUI.addGridBagComponent(mainContentPnl, parkingRatesPnl, GridBagConstraints.HORIZONTAL, 0, 0, 2, 1);
		adminMainUI.addGridBagComponent(mainContentPnl, updateRatesBtn, GridBagConstraints.NONE, 0, 1, 2, 1);
		adminMainUI.addGridBagComponent(mainContentPnl, backBtn, GridBagConstraints.NONE, 0, 2);
		adminMainUI.addGridBagComponent(mainContentPnl, logoutBtn, GridBagConstraints.NONE, 1, 2);

		adminMainUI.addGridBagComponent(adminMainUI.mainPnl, mainContentPnl, GridBagConstraints.BOTH, 0, 0);
	}

	private class ConfigRatesUIListener implements ActionListener {
		ConfigRatesUIListener() {
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("Update Rates")) {
				updateRates();
			}
			if (e.getActionCommand().equals("Go Back")) {
				adminMainUI.showHideContentPanel(adminMainUI.adminUI.mainContentPnl, mainContentPnl);
			}
			if (e.getActionCommand().equals("Logout")) {
				logout();
			}
		}

		private void logout() {
			try {
				JOptionPane.showMessageDialog(adminMainUI, "Logging out...", "Logout", JOptionPane.INFORMATION_MESSAGE);
				parking.logout();
				adminMainUI.showHideContentPanel(adminMainUI.mainContentPnl, mainContentPnl);
				adminMainUI.updateWelcomeMessage();
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}

		private void updateRates() {
			if (parkingRatesTbl.isEditing()) {
				parkingRatesTbl.getDefaultEditor(String.class).stopCellEditing();
				boolean validRates = true;
				List<ParkingRate> parkingRates = new ArrayList<ParkingRate>();
				for (int i = 0; i < parkingRatesTbl.getRowCount(); i++) {
					String noOfHoursStr = (String) parkingRatesTbl.getValueAt(i, 0);
					String newRateStr = (String) parkingRatesTbl.getValueAt(i, 1);
					if (newRateStr.matches("[0-9]{1,2}\\.[0-9]{0,2}")) {
						double noOfHours = Double.parseDouble(noOfHoursStr);
						double newRate = Double.parseDouble(newRateStr);
						ParkingRate pr = new ParkingRate();
						pr.setHours(noOfHours);
						pr.setRate(newRate);
						parkingRates.add(pr);
					} else {
						validRates = false;
						JOptionPane
								.showMessageDialog(adminMainUI, "Enter rates in decimal format between 0 and 99.99", "Error", JOptionPane.ERROR_MESSAGE);
						break;
					}
				}
				if (validRates) {
					try {
						parking.updateParkingRates(parkingRates);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					JOptionPane.showMessageDialog(adminMainUI, "Parking Rates updated.", "Message", JOptionPane.INFORMATION_MESSAGE);
					adminMainUI.showHideContentPanel(adminMainUI.adminUI.mainContentPnl, mainContentPnl);
				}
			}
		}
	}

}
