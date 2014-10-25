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
import cs414.fmaster.parking.controller.ParkingOperationsHandler;

/**
 * @author MasterF
 * 
 */
public class EnterParkingUI {
	private MainUI mainUI;
	private MainController mainController;
	public JPanel mainContentPnl = new JPanel();
	JTable parkingRatesTbl = new JTable();

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

		// Parking Rates Panel
		JPanel parkingRatesPnl = new JPanel(new GridBagLayout());
		JLabel parkingRateLbl = new JLabel("Current Parking Rates");
		DefaultTableModel parkingRatesModel = new DefaultTableModel();
		parkingRatesTbl.setModel(parkingRatesModel);
		mainUI.populateParkingRatesInTable(parkingRatesTbl);
		parkingRatesTbl.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		parkingRatesTbl.setFillsViewportHeight(true);
		parkingRatesTbl.setEnabled(false);

		mainUI.addGridBagComponent(parkingRatesPnl, parkingRateLbl, GridBagConstraints.BOTH, 0, 0);
		mainUI.addGridBagComponent(parkingRatesPnl, parkingRatesTbl, GridBagConstraints.BOTH, 0, 1);

		// Get Ticket and Back buttons
		JButton getTicketBtn = new JButton("Get Ticket");
		JButton backBtn = new JButton("Go Back");
		getTicketBtn.addActionListener(new EnterParkingUIListener());
		backBtn.addActionListener(new EnterParkingUIListener());

		// Main Content Panel
		mainUI.addGridBagComponent(mainContentPnl, parkingRatesPnl, GridBagConstraints.BOTH, 0, 0);
		mainUI.addGridBagComponent(mainContentPnl, getTicketBtn, GridBagConstraints.NONE, 0, 1);
		mainUI.addGridBagComponent(mainContentPnl, backBtn, GridBagConstraints.NONE, 0, 2);

		mainContentPnl.setVisible(false);
		mainUI.addGridBagComponent(mainUI.mainPnl, mainContentPnl, GridBagConstraints.BOTH, 0, 0);
	}

	private class EnterParkingUIListener implements ActionListener {

		EnterParkingUIListener() {
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("Get Ticket")) {
				int ticket = mainController.parkingOpsHandler.getTicket();
				mainController.parkingOpsHandler.openEntryGate();
				JOptionPane.showMessageDialog(mainUI, "Ticket Number: " + ticket + "\nEntry Gate Opened. Click OK to close it.",
						"Entry Gate opened.", JOptionPane.INFORMATION_MESSAGE);
				mainController.parkingOpsHandler.closeEntryGate();
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