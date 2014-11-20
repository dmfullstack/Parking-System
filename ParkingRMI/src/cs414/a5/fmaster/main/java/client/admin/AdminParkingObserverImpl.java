package cs414.a5.fmaster.main.java.client.admin;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import cs414.a5.fmaster.main.java.client.RemoteObserver;
import cs414.a5.fmaster.main.java.client.ui.admin.AdminMainUI;
import cs414.a5.fmaster.main.java.server.ParkingInterface;

/**
 * @author MasterF
 * 
 */
public class AdminParkingObserverImpl extends UnicastRemoteObject implements RemoteObserver {
	AdminMainUI adminMainUI;
	private static final long serialVersionUID = 1L;
	ClientType clientType = ClientType.ADMIN;

	protected AdminParkingObserverImpl(ParkingInterface psi) throws RemoteException {
		super();
		this.adminMainUI = new AdminMainUI(psi);
	}

	@Override
	public void update(Object observable, Object updateMsg) throws RemoteException {
		adminMainUI.updateWelcomeMessage();
		adminMainUI.populateParkingRatesInTable(adminMainUI.viewRatesUI.parkingRatesTbl);
		adminMainUI.populateParkingRatesInTable(adminMainUI.configRatesUI.parkingRatesTbl);
		adminMainUI.configParkingSizeUI.updateAvailabilityAndSize();

		// These reports need not be updated by callback. They have data of last month which cannot be changed at runtime.
		/*
		 * adminMainUI.reportsUI.populateMostLeastUsedHourReport(); 
		 * adminMainUI.reportsUI.populateMaxRevenueDayReport();
		 * adminMainUI.reportsUI.populateDailyOccupancyReport();
		 */
		adminMainUI.reportsUI.updateHourlyRevenueReport();
		adminMainUI.reportsUI.updateDailyRevenueReport();
		adminMainUI.reportsUI.updateMonthlyRevenueReport();
	}

	@Override
	public String getClientType() throws RemoteException {
		return clientType.toString();
	}
}