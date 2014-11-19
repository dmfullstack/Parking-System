/**
 * 
 */
package a5.fmaster.src.main.java.client.ui.enterparking;

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

import a5.fmaster.src.main.java.common.ParkingInterface;

/**
 * @author MasterF
 * 
 */
public class EnterParkingUI {
	private EnterParkingMainUI enterParkingMainUI;
	private ParkingInterface parking;
	public JPanel mainContentPnl = new JPanel(new GridBagLayout());
	public JTable parkingRatesTbl = new JTable();

	private static EnterParkingUI instance = null;

	private EnterParkingUI(EnterParkingMainUI enterParkingMainUI, ParkingInterface parking) {
		this.enterParkingMainUI = enterParkingMainUI;
		this.parking = parking;
	}

	public static EnterParkingUI getInstance(EnterParkingMainUI enterParkingMainUI, ParkingInterface parking) {
		if (instance == null) {
			instance = new EnterParkingUI(enterParkingMainUI, parking);
		}
		return instance;
	}

	public void setupUI() throws RemoteException {
		// Parking Rates Panel
		JPanel parkingRatesPnl = new JPanel(new GridBagLayout());
		JLabel parkingRateLbl = new JLabel("Current Parking Rates");
		DefaultTableModel parkingRatesModel = new DefaultTableModel(new Object[][] {}, new String[] { "Hours", "Rate" });
		parkingRatesTbl.setModel(parkingRatesModel);
		enterParkingMainUI.populateParkingRatesInTable(parkingRatesTbl);
		parkingRatesTbl.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		parkingRatesTbl.setFillsViewportHeight(true);
		parkingRatesTbl.setEnabled(false);

		enterParkingMainUI.addGridBagComponent(parkingRatesPnl, parkingRateLbl, GridBagConstraints.BOTH, 0, 0);
		enterParkingMainUI.addGridBagComponent(parkingRatesPnl, parkingRatesTbl.getTableHeader(), GridBagConstraints.BOTH, 0, 1);
		enterParkingMainUI.addGridBagComponent(parkingRatesPnl, parkingRatesTbl, GridBagConstraints.BOTH, 0, 2);

		// Get Ticket and Back buttons
		JButton getTicketBtn = new JButton("Get Ticket");
		JButton backBtn = new JButton("Go Back");
		getTicketBtn.addActionListener(new EnterParkingUIListener());
		backBtn.addActionListener(new EnterParkingUIListener());

		// Main Content Panel
		enterParkingMainUI.addGridBagComponent(mainContentPnl, parkingRatesPnl, GridBagConstraints.BOTH, 0, 0);
		enterParkingMainUI.addGridBagComponent(mainContentPnl, getTicketBtn, GridBagConstraints.NONE, 0, 1);
		enterParkingMainUI.addGridBagComponent(mainContentPnl, backBtn, GridBagConstraints.NONE, 0, 2);

		mainContentPnl.setVisible(false);
		enterParkingMainUI.addGridBagComponent(enterParkingMainUI.mainPnl, mainContentPnl, GridBagConstraints.BOTH, 0, 0);
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
			enterParkingMainUI.showHideContentPanel(enterParkingMainUI.mainContentPnl, mainContentPnl);
			try {
				enterParkingMainUI.updateWelcomeMessage();
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}

		private void getTicket() {
			try {
				int ticket = parking.getTicket();
				if (ticket == 0) {
					JOptionPane.showMessageDialog(enterParkingMainUI, "Ticket cannot be issued at this time. Parking lot is full.", "Error",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					parking.openEntryGate();
					JOptionPane.showMessageDialog(enterParkingMainUI, "Ticket Number: " + ticket + "\nEntry Gate Opened. Click OK to close it.",
							"Entry Gate opened.", JOptionPane.INFORMATION_MESSAGE);
					parking.closeEntryGate();
				}
				enterParkingMainUI.showHideContentPanel(enterParkingMainUI.mainContentPnl, mainContentPnl);
				enterParkingMainUI.updateWelcomeMessage();
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}
	}
}