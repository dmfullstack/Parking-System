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
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import a5.fmaster.src.main.java.client.ui.ViewRatesUIInterface;
import a5.fmaster.src.main.java.common.ParkingInterface;

/**
 * @author MasterF
 * 
 */
public class EnterParkingViewRatesUI implements ViewRatesUIInterface {
	private EnterParkingMainUI enterParkingMainUI;
	private ParkingInterface parking;
	JPanel mainContentPnl = new JPanel(new GridBagLayout());
	public JTable parkingRatesTbl = new JTable();

	private static EnterParkingViewRatesUI instance = null;

	private EnterParkingViewRatesUI(EnterParkingMainUI enterParkingMainUI, ParkingInterface parking) {
		this.enterParkingMainUI = enterParkingMainUI;
		this.parking = parking;
	}

	public static EnterParkingViewRatesUI getInstance(EnterParkingMainUI enterParkingMainUI, ParkingInterface parking) {
		if (instance == null) {
			instance = new EnterParkingViewRatesUI(enterParkingMainUI, parking);
		}
		return instance;
	}

	public void setupUI() throws RemoteException {
		// Parking Rates Panel
		JPanel parkingRatesPnl = new JPanel(new GridBagLayout());
		JLabel parkingRateLbl = new JLabel("Current Parking Rates");
		DefaultTableModel parkingRatesModel = new DefaultTableModel(new Object[][] {}, new String[] {"Hours", "Rate"});
		parkingRatesTbl.setModel(parkingRatesModel);
		enterParkingMainUI.populateParkingRatesInTable(parkingRatesTbl);
		parkingRatesTbl.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		parkingRatesTbl.setFillsViewportHeight(true);
		parkingRatesTbl.setEnabled(false);

		enterParkingMainUI.addGridBagComponent(parkingRatesPnl, parkingRateLbl, GridBagConstraints.BOTH, 0, 0);
		enterParkingMainUI.addGridBagComponent(parkingRatesPnl, parkingRatesTbl.getTableHeader(), GridBagConstraints.BOTH, 0, 1);
		enterParkingMainUI.addGridBagComponent(parkingRatesPnl, parkingRatesTbl, GridBagConstraints.BOTH, 0, 2);

		// Back button
		JButton backBtn = new JButton("Go Back");
		backBtn.addActionListener(new ViewRatesListener());

		// Main Content Panel
		enterParkingMainUI.addGridBagComponent(mainContentPnl, parkingRatesPnl, GridBagConstraints.BOTH, 0, 0);
		enterParkingMainUI.addGridBagComponent(mainContentPnl, backBtn, GridBagConstraints.NONE, 0, 1);

		mainContentPnl.setVisible(false);
		enterParkingMainUI.addGridBagComponent(enterParkingMainUI.mainPnl, mainContentPnl, GridBagConstraints.BOTH, 0, 0);
	}

	private class ViewRatesListener implements ActionListener {

		private ViewRatesListener() {
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("Go Back")) {
				enterParkingMainUI.showHideContentPanel(enterParkingMainUI.mainContentPnl, mainContentPnl);
				try {
					enterParkingMainUI.updateWelcomeMessage();
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
}