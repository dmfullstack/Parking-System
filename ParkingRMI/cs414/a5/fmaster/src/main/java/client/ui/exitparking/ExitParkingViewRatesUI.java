/**
 * 
 */
package a5.fmaster.src.main.java.client.ui.exitparking;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import a5.fmaster.src.main.java.common.ParkingInterface;

/**
 * @author MasterF
 * 
 */
public class ExitParkingViewRatesUI {
	private ExitParkingMainUI exitParkingMainUI;
	private ParkingInterface parking;
	JPanel mainContentPnl = new JPanel(new GridBagLayout());
	public JTable parkingRatesTbl = new JTable();

	private static ExitParkingViewRatesUI instance = null;

	private ExitParkingViewRatesUI(ExitParkingMainUI exitParkingMainUI, ParkingInterface parking) {
		this.exitParkingMainUI = exitParkingMainUI;
		this.parking = parking;
	}

	public static ExitParkingViewRatesUI getInstance(ExitParkingMainUI exitParkingMainUI, ParkingInterface parking) {
		if (instance == null) {
			instance = new ExitParkingViewRatesUI(exitParkingMainUI, parking);
		}
		return instance;
	}

	public void setupUI() throws RemoteException {
		// Parking Rates Panel
		JPanel parkingRatesPnl = new JPanel(new GridBagLayout());
		JLabel parkingRateLbl = new JLabel("Current Parking Rates");
		DefaultTableModel parkingRatesModel = new DefaultTableModel(new Object[][] {}, new String[] {"Hours", "Rate"});
		parkingRatesTbl.setModel(parkingRatesModel);
		exitParkingMainUI.populateParkingRatesInTable(parkingRatesTbl);
		parkingRatesTbl.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		parkingRatesTbl.setFillsViewportHeight(true);
		parkingRatesTbl.setEnabled(false);

		exitParkingMainUI.addGridBagComponent(parkingRatesPnl, parkingRateLbl, GridBagConstraints.BOTH, 0, 0);
		exitParkingMainUI.addGridBagComponent(parkingRatesPnl, parkingRatesTbl.getTableHeader(), GridBagConstraints.BOTH, 0, 1);
		exitParkingMainUI.addGridBagComponent(parkingRatesPnl, parkingRatesTbl, GridBagConstraints.BOTH, 0, 2);

		// Back button
		JButton backBtn = new JButton("Go Back");
		backBtn.addActionListener(new ViewRatesListener());

		// Main Content Panel
		exitParkingMainUI.addGridBagComponent(mainContentPnl, parkingRatesPnl, GridBagConstraints.BOTH, 0, 0);
		exitParkingMainUI.addGridBagComponent(mainContentPnl, backBtn, GridBagConstraints.NONE, 0, 1);

		mainContentPnl.setVisible(false);
		exitParkingMainUI.addGridBagComponent(exitParkingMainUI.mainPnl, mainContentPnl, GridBagConstraints.BOTH, 0, 0);
	}

	private class ViewRatesListener implements ActionListener {

		private ViewRatesListener() {
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("Go Back")) {
				exitParkingMainUI.showHideContentPanel(exitParkingMainUI.mainContentPnl, mainContentPnl);
				try {
					exitParkingMainUI.updateWelcomeMessage();
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
}