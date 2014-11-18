package a5.fmaster.src.main.java.client.admin;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import a5.fmaster.src.main.java.client.RemoteObserver;
import a5.fmaster.src.main.java.client.ui.admin.AdminMainUI;
import a5.fmaster.src.main.java.common.ParkingServerInterface;

/**
 * @author MasterF
 * 
 */
public class AdminParkingObserverImpl extends UnicastRemoteObject implements RemoteObserver {
	AdminMainUI adminMainUI;
	private static final long serialVersionUID = 1L;

	protected AdminParkingObserverImpl(ParkingServerInterface psi) throws RemoteException {
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
		 * adminMainUI.reportsUI.populateMostLeastUsedHourReport(); adminMainUI.reportsUI.populateMaxRevenueDayReport();
		 * adminMainUI.reportsUI.populateDailyOccupancyReport();
		 */
		adminMainUI.reportsUI.updateHourlyRevenueReport();
		adminMainUI.reportsUI.updateDailyRevenueReport();
		adminMainUI.reportsUI.updateMonthlyRevenueReport();
	}
}