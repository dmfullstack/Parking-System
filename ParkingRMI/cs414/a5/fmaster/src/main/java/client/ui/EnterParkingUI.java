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
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import a5.fmaster.src.main.java.common.ParkingServerInterface;

/**
 * @author MasterF
 * 
 */
public class EnterParkingUI {
	private MainUI mainUI;
	private ParkingServerInterface parking;
	public JPanel mainContentPnl = new JPanel(new GridBagLayout());
	JTable parkingRatesTbl = new JTable();

	private static EnterParkingUI instance = null;

	private EnterParkingUI(MainUI mainUI, ParkingServerInterface parking) {
		this.mainUI = mainUI;
		this.parking = parking;
	}

	public static EnterParkingUI getInstance(MainUI mainUI, ParkingServerInterface parking) {
		if (instance == null) {
			instance = new EnterParkingUI(mainUI, parking);
		}
		return instance;
	}

	public void setupUI() throws RemoteException {
		// Parking Rates Panel
		JPanel parkingRatesPnl = new JPanel(new GridBagLayout());
		JLabel parkingRateLbl = new JLabel("Current Parking Rates");
		DefaultTableModel parkingRatesModel = new DefaultTableModel(new Object[][] {}, new String[] { "Hours", "Rate" });
		parkingRatesTbl.setModel(parkingRatesModel);
		mainUI.populateParkingRatesInTable(parkingRatesTbl);
		parkingRatesTbl.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		parkingRatesTbl.setFillsViewportHeight(true);
		parkingRatesTbl.setEnabled(false);

		mainUI.addGridBagComponent(parkingRatesPnl, parkingRateLbl, GridBagConstraints.BOTH, 0, 0);
		mainUI.addGridBagComponent(parkingRatesPnl, parkingRatesTbl.getTableHeader(), GridBagConstraints.BOTH, 0, 1);
		mainUI.addGridBagComponent(parkingRatesPnl, parkingRatesTbl, GridBagConstraints.BOTH, 0, 2);

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

		private EnterParkingUIListener() {
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("Get Ticket")) {
				getTicket();
			}
			if (e.getActionCommand().equals("Go Back")) {
				goBack();
			}
		}

		private void goBack() {
			mainUI.showHideContentPanel(mainUI.mainContentPnl, mainContentPnl);
			try {
				mainUI.updateWelcomeMessage();
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}

		private void getTicket() {
			try {
				int ticket = parking.getTicket();
				parking.openEntryGate();
				JOptionPane.showMessageDialog(mainUI, "Ticket Number: " + ticket + "\nEntry Gate Opened. Click OK to close it.",
						"Entry Gate opened.", JOptionPane.INFORMATION_MESSAGE);
				parking.closeEntryGate();
				mainUI.showHideContentPanel(mainUI.mainContentPnl, mainContentPnl);
				mainUI.updateWelcomeMessage();
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}
	}
}