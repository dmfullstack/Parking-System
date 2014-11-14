/**
 * 
 */
package a5.fmaster.src.main.java.client.ui;

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

	private MainUI mainUI;
	private ParkingServerInterface parking;
	public JPanel mainContentPnl = new JPanel(new GridBagLayout());
	JTable parkingRatesTbl = new JTable();

	private static ConfigRatesUI instance = null;

	private ConfigRatesUI(MainUI mainUI, ParkingServerInterface parking) {
		this.mainUI = mainUI;
		this.parking = parking;
	}

	public static ConfigRatesUI getInstance(MainUI mainUI, ParkingServerInterface parking) {
		if (instance == null) {
			instance = new ConfigRatesUI(mainUI, parking);
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
		mainUI.populateParkingRatesInTable(parkingRatesTbl);
		parkingRatesTbl.getColumnModel().getColumn(1).setCellEditor(parkingRatesTbl.getDefaultEditor(String.class));
		parkingRatesTbl.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		parkingRatesTbl.setFillsViewportHeight(true);

		mainUI.addGridBagComponent(parkingRatesPnl, parkingRateLbl, GridBagConstraints.BOTH, 0, 0);
		mainUI.addGridBagComponent(parkingRatesPnl, parkingRatesTbl.getTableHeader(), GridBagConstraints.BOTH, 0, 1);
		mainUI.addGridBagComponent(parkingRatesPnl, parkingRatesTbl, GridBagConstraints.BOTH, 0, 2);

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
		mainUI.addGridBagComponent(mainContentPnl, parkingRatesPnl, GridBagConstraints.HORIZONTAL, 0, 0, 2, 1);
		mainUI.addGridBagComponent(mainContentPnl, updateRatesBtn, GridBagConstraints.NONE, 0, 1, 2, 1);
		mainUI.addGridBagComponent(mainContentPnl, backBtn, GridBagConstraints.NONE, 0, 2);
		mainUI.addGridBagComponent(mainContentPnl, logoutBtn, GridBagConstraints.NONE, 1, 2);

		mainUI.addGridBagComponent(mainUI.mainPnl, mainContentPnl, GridBagConstraints.BOTH, 0, 0);
	}

	private class ConfigRatesUIListener implements ActionListener {
		ConfigRatesUIListener() {
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("Update Rates")) {
				updateRates();
			}
			if (e.getActionCommand().equals("Go Back")) {
				mainUI.showHideContentPanel(mainUI.adminUI.mainContentPnl, mainContentPnl);
			}
			if (e.getActionCommand().equals("Logout")) {
				logout();
			}
		}

		private void logout() {
			try {
				JOptionPane.showMessageDialog(mainUI, "Logging out...", "Logout", JOptionPane.INFORMATION_MESSAGE);
				parking.logout();
				mainUI.showHideContentPanel(mainUI.mainContentPnl, mainContentPnl);
				mainUI.updateWelcomeMessage();
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
								.showMessageDialog(mainUI, "Enter rates in decimal format between 0 and 99.99", "Error", JOptionPane.ERROR_MESSAGE);
						break;
					}
				}
				if (validRates) {
					try {
						parking.updateParkingRates(parkingRates);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					JOptionPane.showMessageDialog(mainUI, "Parking Rates updated.", "Message", JOptionPane.INFORMATION_MESSAGE);
					mainUI.showHideContentPanel(mainUI.adminUI.mainContentPnl, mainContentPnl);
				}
			}
		}
	}

}
