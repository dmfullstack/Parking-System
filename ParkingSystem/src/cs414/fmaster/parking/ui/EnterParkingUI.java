/**
 * 
 */
package cs414.fmaster.parking.ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import cs414.fmaster.parking.ParkingHandler;
import cs414.fmaster.parking.ParkingRate;
import cs414.fmaster.parking.ParkingSystem;

/**
 * @author MasterF
 * 
 */
public class EnterParkingUI {
	ParkingSystem ps;
	ParkingUI mainUI;
	JPanel enterParkingMainPanel = new JPanel();
	JPanel parkingRatesPanel = new JPanel();
	JButton getTicketBtn = new JButton("Get Ticket");

	private static EnterParkingUI instance = null;

	protected EnterParkingUI(ParkingSystem ps, ParkingUI mainUI) {
		super();
		this.ps = ps;
		this.mainUI = mainUI;
	}

	public static EnterParkingUI getInstance(ParkingSystem ps, ParkingUI mainUI) {
		if (instance == null) {
			instance = new EnterParkingUI(ps, mainUI);
		}
		return instance;
	}

	public void generateEnterParkingScreen() {
		enterParkingMainPanel.setLayout(new GridBagLayout());
		displayParkingRates();
		getTicketBtn.addActionListener(new EnterParkingListener());
		mainUI.addComponent(enterParkingMainPanel, getTicketBtn, GridBagConstraints.NONE, 0, 1);
		mainUI.addComponent(mainUI.getMainPanel(), enterParkingMainPanel, GridBagConstraints.BOTH, 0, 0);
		mainUI.showContentPanel(enterParkingMainPanel);
		mainUI.setMessage("");
	}

	private void displayParkingRates() {
		List<ParkingRate> parkingRates = new ArrayList<ParkingRate>();
		parkingRates = ps.getParkingRates();
		DefaultTableModel model = new DefaultTableModel(new Object[][] {}, new String[] { "Hours", "Rate" });
		for (ParkingRate pr : parkingRates) {
			model.addRow(new Object[] { pr.getHours(), pr.getRate() });
		}
		JTable parkingRatesTbl = new JTable();
		parkingRatesTbl.setModel(model);
		parkingRatesTbl.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		parkingRatesTbl.setFillsViewportHeight(true);

		parkingRatesPanel.add(parkingRatesTbl);
		mainUI.addComponent(enterParkingMainPanel, parkingRatesPanel, GridBagConstraints.BOTH, 0, 0);
	}

	public class EnterParkingListener implements ActionListener {
		EnterParkingListener() {
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("Get Ticket")) {
				int ticketNumber = ps.enterParking();
				if(ticketNumber == 0) {
					mainUI.setMessage("Sorry. Parking not available.");
				}
				else {
					mainUI.setMessage("Ticket Number: " + ticketNumber);
				}
				
			}
		}

	}

}
