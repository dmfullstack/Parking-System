/**
 * 
 */
package a5.fmaster.src.main.java.client.ui.admin;

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

import a5.fmaster.src.main.java.client.ui.enterparking.EnterParkingMainUI;
import a5.fmaster.src.main.java.common.ParkingServerInterface;

/**
 * @author MasterF
 * 
 */
public class AdminViewRatesUI {
	private AdminMainUI adminMainUI;
	private ParkingServerInterface parking;
	JPanel mainContentPnl = new JPanel(new GridBagLayout());
	public JTable parkingRatesTbl = new JTable();

	private static AdminViewRatesUI instance = null;

	private AdminViewRatesUI(AdminMainUI adminMainUI, ParkingServerInterface parking) {
		this.adminMainUI = adminMainUI;
		this.parking = parking;
	}

	public static AdminViewRatesUI getInstance(AdminMainUI adminMainUI, ParkingServerInterface parking) {
		if (instance == null) {
			instance = new AdminViewRatesUI(adminMainUI, parking);
		}
		return instance;
	}

	public void setupUI() throws RemoteException {
		// Parking Rates Panel
		JPanel parkingRatesPnl = new JPanel(new GridBagLayout());
		JLabel parkingRateLbl = new JLabel("Current Parking Rates");
		DefaultTableModel parkingRatesModel = new DefaultTableModel(new Object[][] {}, new String[] {"Hours", "Rate"});
		parkingRatesTbl.setModel(parkingRatesModel);
		adminMainUI.populateParkingRatesInTable(parkingRatesTbl);
		parkingRatesTbl.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		parkingRatesTbl.setFillsViewportHeight(true);
		parkingRatesTbl.setEnabled(false);

		adminMainUI.addGridBagComponent(parkingRatesPnl, parkingRateLbl, GridBagConstraints.BOTH, 0, 0);
		adminMainUI.addGridBagComponent(parkingRatesPnl, parkingRatesTbl.getTableHeader(), GridBagConstraints.BOTH, 0, 1);
		adminMainUI.addGridBagComponent(parkingRatesPnl, parkingRatesTbl, GridBagConstraints.BOTH, 0, 2);

		// Back button
		JButton backBtn = new JButton("Go Back");
		backBtn.addActionListener(new ViewRatesListener());

		// Main Content Panel
		adminMainUI.addGridBagComponent(mainContentPnl, parkingRatesPnl, GridBagConstraints.BOTH, 0, 0);
		adminMainUI.addGridBagComponent(mainContentPnl, backBtn, GridBagConstraints.NONE, 0, 1);

		mainContentPnl.setVisible(false);
		adminMainUI.addGridBagComponent(adminMainUI.mainPnl, mainContentPnl, GridBagConstraints.BOTH, 0, 0);
	}

	private class ViewRatesListener implements ActionListener {

		private ViewRatesListener() {
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("Go Back")) {
				adminMainUI.showHideContentPanel(adminMainUI.mainContentPnl, mainContentPnl);
				try {
					adminMainUI.updateWelcomeMessage();
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
}