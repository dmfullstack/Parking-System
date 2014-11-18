package a5.fmaster.src.main.java.client.ui;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;

public interface MainUIInterface extends java.rmi.Remote {
	
	public void initializeChildUI() throws java.rmi.RemoteException;

	public void setupChildUI() throws java.rmi.RemoteException;

	public void setupMainUI() throws java.rmi.RemoteException;

	public void addGridBagComponent(JPanel parent, JComponent child, int gridBagFill, int gridx, int gridy) throws java.rmi.RemoteException;
		
	public void addGridBagComponent(JPanel parent, JComponent child, int gridBagFill, int gridx, int gridy, int gridWidth, int gridHeight) throws java.rmi.RemoteException;
		
	public void showHideContentPanel(JPanel pnlToShow, JPanel pnlToHide) throws java.rmi.RemoteException;

	public void updateWelcomeMessage() throws java.rmi.RemoteException;
	
	public void displayMessage(String message) throws java.rmi.RemoteException;

	public void populateParkingRatesInTable(JTable parkingRatesTbl) throws java.rmi.RemoteException;
	
}
