package cs414.a5.fmaster.main.java.client.enterparking;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import cs414.a5.fmaster.main.java.client.RemoteObserver;
import cs414.a5.fmaster.main.java.client.RemoteObserver.ClientType;
import cs414.a5.fmaster.main.java.client.ui.enterparking.EnterParkingMainUI;
import cs414.a5.fmaster.main.java.server.ParkingInterface;

/**
 * @author MasterF
 * 
 */
public class EnterParkingObserverImpl extends UnicastRemoteObject implements RemoteObserver {
	EnterParkingMainUI enterParkingMainUI;
	private static final long serialVersionUID = 1L;
	ClientType clientType = ClientType.ENTER;

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
		return clientType.toString();
	}
}