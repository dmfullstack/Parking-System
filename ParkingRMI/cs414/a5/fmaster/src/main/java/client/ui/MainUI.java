package a5.fmaster.src.main.java.client.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import a5.fmaster.src.main.java.server.common.ParkingInterface;
import a5.fmaster.src.main.java.server.domain.ParkingRate;

public class MainUI extends JFrame implements java.rmi.Remote {

	protected ParkingInterface parking;
	public JPanel mainPnl = new JPanel(new GridBagLayout());
	public JPanel mainContentPnl = new JPanel(new GridBagLayout());
	protected JLabel messageLbl = new JLabel("");
	
	public void initializeChildUI() throws java.rmi.RemoteException {

	}

	public void setupChildUI() throws java.rmi.RemoteException {

	}

	public void setupMainUI() throws java.rmi.RemoteException {

	}

	public void addGridBagComponent(JPanel parent, JComponent child, int gridBagFill, int gridx, int gridy) {
		GridBagConstraints localgbc = new GridBagConstraints();
		localgbc.fill = gridBagFill;
		localgbc.gridx = gridx;
		localgbc.gridy = gridy;
		parent.add(child, localgbc);
	}

	public void addGridBagComponent(JPanel parent, JComponent child, int gridBagFill, int gridx, int gridy, int gridWidth, int gridHeight) {
		GridBagConstraints localgbc = new GridBagConstraints();
		localgbc.fill = gridBagFill;
		localgbc.gridx = gridx;
		localgbc.gridy = gridy;
		localgbc.gridwidth = gridWidth;
		localgbc.gridheight = gridHeight;
		parent.add(child, localgbc);
	}

	public void showHideContentPanel(JPanel pnlToShow, JPanel pnlToHide) {
		pnlToHide.setVisible(false);
		pnlToShow.setVisible(true);
		if(pnlToShow.equals(mainContentPnl)) {
			messageLbl.setVisible(true);
		}
		else {
			messageLbl.setVisible(false);
		}
	}

	public void updateWelcomeMessage() throws RemoteException {
		int availableParking = parking.getCurrentAvailability();
		int parkingSize = parking.getCurrentParkingSize();
		displayMessage("Welcome to My Parking!! " + availableParking + " out of " + parkingSize + " parking spots available.");
	}

	public void displayMessage(String message) {
		messageLbl.setText(message);
	}

	public void populateParkingRatesInTable(JTable parkingRatesTbl) throws RemoteException {
		DefaultTableModel model = (DefaultTableModel) parkingRatesTbl.getModel();
		model.setRowCount(0);
		List<ParkingRate> parkingRatesList = new ArrayList<ParkingRate>();
		parkingRatesList = parking.getParkingRates();
		for (ParkingRate pr : parkingRatesList) {
			model.addRow(new Object[] { String.valueOf(pr.getHours()), String.valueOf(pr.getRate()) });
		}
	}


}
