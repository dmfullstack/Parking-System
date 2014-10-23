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
import cs414.fmaster.parking.controller.ParkingOperationsController;

/**
 * @author MasterF
 * 
 */
public class EnterParkingUI {
	MainUI mainUI;
	MainController mainController;
	JPanel mainContentPnl = new JPanel();
	JButton getTicketBtn = new JButton("Get Ticket");
	JButton backButton = new JButton("Go Back");

	private static EnterParkingUI instance = null;

	private EnterParkingUI(MainUI mainUI, MainController mainController) {
		this.mainUI = mainUI;
		this.mainController = mainController;
	}

	public static EnterParkingUI getInstance(MainUI mainUI, MainController mainController) {
		if (instance == null) {
			instance = new EnterParkingUI(mainUI, mainController);
		}
		return instance;
	}

	public void setupUI() {
		mainContentPnl.setLayout(new GridBagLayout());

		JPanel parkingRatesPanel = new JPanel();
		parkingRatesPanel.setLayout(new GridBagLayout());
		JLabel parkingRateLbl = new JLabel("Current Parking Rates");
		mainUI.addGridBagComponent(parkingRatesPanel, parkingRateLbl, GridBagConstraints.BOTH, 0, 0);

		List<ParkingRate> parkingRates = new ArrayList<ParkingRate>();
		parkingRates = mainController.parkingController.getParkingRates();
		DefaultTableModel model = new DefaultTableModel(new Object[][] {}, new String[] { "Hours", "Rate" });
		for (ParkingRate pr : parkingRates) {
			model.addRow(new Object[] { pr.getHours(), pr.getRate() });
		}
		JTable parkingRatesTbl = new JTable();
		parkingRatesTbl.setModel(model);
		parkingRatesTbl.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		parkingRatesTbl.setFillsViewportHeight(true);

		mainUI.addGridBagComponent(parkingRatesPanel, parkingRatesTbl, GridBagConstraints.BOTH, 0, 1);
		mainUI.addGridBagComponent(mainContentPnl, parkingRatesPanel, GridBagConstraints.BOTH, 0, 0);

		getTicketBtn.addActionListener(new EnterParkingUIListener());
		backButton.addActionListener(new EnterParkingUIListener());

		mainUI.addGridBagComponent(mainContentPnl, getTicketBtn, GridBagConstraints.NONE, 0, 1);
		mainUI.addGridBagComponent(mainContentPnl, backButton, GridBagConstraints.NONE, 0, 2);
		mainContentPnl.setVisible(false);

		mainUI.addGridBagComponent(mainUI.mainPnl, mainContentPnl, GridBagConstraints.BOTH, 0, 0);
	}

	private class EnterParkingUIListener implements ActionListener {

		EnterParkingUIListener() {
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("Get Ticket")) {
				// Ticket ticket = new Ticket();
				int ticket = mainController.parkingController.getTicket();
				mainController.parkingController.openEntryGate();
				JOptionPane.showMessageDialog(mainUI, "Ticket Number: " + ticket + "\nEntry Gate Opened. Click OK to close it.",
						"Entry Gate opened.", JOptionPane.INFORMATION_MESSAGE);
				mainController.parkingController.closeEntryGate();
				mainUI.showHideContentPanel(mainUI.mainContentPnl, mainContentPnl);
				mainUI.displayWelcomeMessage();
			}
			if (e.getActionCommand().equals("Go Back")) {
				mainUI.showHideContentPanel(mainUI.mainContentPnl, mainContentPnl);
				mainUI.displayWelcomeMessage();
			}
		}
	}
}