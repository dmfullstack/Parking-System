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
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import a5.fmaster.src.main.java.common.ParkingServerInterface;

/**
 * @author MasterF
 * 
 */
public class ViewRatesUI {
	private MainUI mainUI;
	private ParkingServerInterface parking;
	JPanel mainContentPnl = new JPanel(new GridBagLayout());
	JTable parkingRatesTbl = new JTable();

	private static ViewRatesUI instance = null;

	private ViewRatesUI(MainUI mainUI, ParkingServerInterface parking) {
		this.mainUI = mainUI;
		this.parking = parking;
	}

	public static ViewRatesUI getInstance(MainUI mainUI, ParkingServerInterface parking) {
		if (instance == null) {
			instance = new ViewRatesUI(mainUI, parking);
		}
		return instance;
	}

	public void setupUI() throws RemoteException {
		// Parking Rates Panel
		JPanel parkingRatesPnl = new JPanel(new GridBagLayout());
		JLabel parkingRateLbl = new JLabel("Current Parking Rates");
		DefaultTableModel parkingRatesModel = new DefaultTableModel(new Object[][] {}, new String[] {"Hours", "Rate"});
		parkingRatesTbl.setModel(parkingRatesModel);
		mainUI.populateParkingRatesInTable(parkingRatesTbl);
		parkingRatesTbl.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		parkingRatesTbl.setFillsViewportHeight(true);
		parkingRatesTbl.setEnabled(false);

		mainUI.addGridBagComponent(parkingRatesPnl, parkingRateLbl, GridBagConstraints.BOTH, 0, 0);
		mainUI.addGridBagComponent(parkingRatesPnl, parkingRatesTbl.getTableHeader(), GridBagConstraints.BOTH, 0, 1);
		mainUI.addGridBagComponent(parkingRatesPnl, parkingRatesTbl, GridBagConstraints.BOTH, 0, 2);

		// Back button
		JButton backBtn = new JButton("Go Back");
		backBtn.addActionListener(new ViewRatesListener());

		// Main Content Panel
		mainUI.addGridBagComponent(mainContentPnl, parkingRatesPnl, GridBagConstraints.BOTH, 0, 0);
		mainUI.addGridBagComponent(mainContentPnl, backBtn, GridBagConstraints.NONE, 0, 1);

		mainContentPnl.setVisible(false);
		mainUI.addGridBagComponent(mainUI.mainPnl, mainContentPnl, GridBagConstraints.BOTH, 0, 0);
	}

	private class ViewRatesListener implements ActionListener {

		private ViewRatesListener() {
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("Go Back")) {
				mainUI.showHideContentPanel(mainUI.mainContentPnl, mainContentPnl);
				try {
					mainUI.updateWelcomeMessage();
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
}