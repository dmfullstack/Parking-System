package a5.fmaster.src.main.java.client.exitparking;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import a5.fmaster.src.main.java.client.RemoteObserver;
import a5.fmaster.src.main.java.client.ui.exitparking.ExitParkingMainUI;
import a5.fmaster.src.main.java.common.ParkingInterface;

/**
 * @author MasterF
 * 
 */
public class ExitParkingObserverImpl extends UnicastRemoteObject implements RemoteObserver {
	ExitParkingMainUI exitParkingMainUI;
	private static final long serialVersionUID = 1L;
	String clientType = "EXIT";

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
		return clientType;
	}
}