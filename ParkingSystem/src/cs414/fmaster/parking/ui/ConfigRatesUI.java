/**
 * 
 */
package cs414.fmaster.parking.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import cs414.fmaster.parking.controller.MainController;
import cs414.fmaster.parking.controller.ParkingRate;

/**
 * @author MasterF
 * 
 */
public class ConfigRatesUI {

	private MainUI mainUI;
	private MainController mainController;
	public JPanel mainContentPnl = new JPanel(new GridBagLayout());
	JTable parkingRatesTbl = new JTable();

	private static ConfigRatesUI instance = null;

	private ConfigRatesUI(MainUI mainUI, MainController mainController) {
		this.mainUI = mainUI;
		this.mainController = mainController;
	}

	public static ConfigRatesUI getInstance(MainUI mainUI, MainController mainController) {
		if (instance == null) {
			instance = new ConfigRatesUI(mainUI, mainController);
		}
		return instance;
	}

	public void setupUI() {
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
							JOptionPane.showMessageDialog(mainUI, "Enter rates in decimal format between 0 and 99.99", "Error", JOptionPane.ERROR_MESSAGE);
							break;
						}
					}
					if (validRates) {
						mainController.parkingOpsHandler.updateParkingRates(parkingRates);
						JOptionPane.showMessageDialog(mainUI, "Parking Rates updated.", "Message", JOptionPane.INFORMATION_MESSAGE);
						mainUI.showHideContentPanel(mainUI.adminUI.mainContentPnl, mainContentPnl);
					}
				}
			}
			if (e.getActionCommand().equals("Go Back")) {
				mainUI.showHideContentPanel(mainUI.adminUI.mainContentPnl, mainContentPnl);
			}
			if (e.getActionCommand().equals("Logout")) {
				JOptionPane.showMessageDialog(mainUI, "Logging out...", "Logout", JOptionPane.INFORMATION_MESSAGE);
				mainController.adminOpsHandler.logout();
				mainUI.showHideContentPanel(mainUI.mainContentPnl, mainContentPnl);
				mainUI.displayWelcomeMessage();
			}
		}
	}

}
