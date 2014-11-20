package cs414.a5.fmaster.main.java.client.exitparking;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import cs414.a5.fmaster.main.java.client.RemoteObserver;
import cs414.a5.fmaster.main.java.client.RemoteObserver.ClientType;
import cs414.a5.fmaster.main.java.client.ui.exitparking.ExitParkingMainUI;
import cs414.a5.fmaster.main.java.server.ParkingInterface;

/**
 * @author MasterF
 * 
 */
public class ExitParkingObserverImpl extends UnicastRemoteObject implements RemoteObserver {
	ExitParkingMainUI exitParkingMainUI;
	private static final long serialVersionUID = 1L;
	ClientType clientType = ClientType.EXIT;

	protected ExitParkingObserverImpl(ParkingInterface psi) throws RemoteException {
		super();
		this.exitParkingMainUI = new ExitParkingMainUI(psi);
	}
	
	@Override
    public void update(Object observable, Object updateMsg)
            throws RemoteException {
		exitParkingMainUI.updateWelcomeMessage();
		exitParkingMainUI.populateParkingRatesInTable(exitParkingMainUI.exitParkingUI.parkingRatesTbl);
		exitParkingMainUI.populateParkingRatesInTable(exitParkingMainUI.viewRatesUI.parkingRatesTbl);
    }
	
	@Override
	public String getClientType() throws RemoteException {
		return clientType.toString();
	}
}