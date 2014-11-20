package a5.fmaster.src.main.java.client.enterparking;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import a5.fmaster.src.main.java.client.RemoteObserver;
import a5.fmaster.src.main.java.client.ui.enterparking.EnterParkingMainUI;
import a5.fmaster.src.main.java.server.common.ParkingInterface;

/**
 * @author MasterF
 * 
 */
public class EnterParkingObserverImpl extends UnicastRemoteObject implements RemoteObserver {
	EnterParkingMainUI enterParkingMainUI;
	private static final long serialVersionUID = 1L;
	String clientType = "ENTER";

	protected EnterParkingObserverImpl(ParkingInterface psi) throws RemoteException {
		super();
		this.enterParkingMainUI = new EnterParkingMainUI(psi);
	}
	
	@Override
    public void update(Object observable, Object updateMsg)
            throws RemoteException {
		enterParkingMainUI.updateWelcomeMessage();
		enterParkingMainUI.populateParkingRatesInTable(enterParkingMainUI.enterParkingUI.parkingRatesTbl);
		enterParkingMainUI.populateParkingRatesInTable(enterParkingMainUI.viewRatesUI.parkingRatesTbl);
	}

	@Override
	public String getClientType() throws RemoteException {
		return clientType;
	}
}