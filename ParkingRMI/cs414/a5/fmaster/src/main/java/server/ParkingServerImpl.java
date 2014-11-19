package a5.fmaster.src.main.java.server;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import a5.fmaster.src.main.java.client.RemoteObserver;
import a5.fmaster.src.main.java.client.admin.AdminParkingObserverImpl;
import a5.fmaster.src.main.java.common.ParkingInterface;
import a5.fmaster.src.main.java.database.ParkingDatabaseAccess;
import a5.fmaster.src.main.java.server.domain.ParkingRate;
import a5.fmaster.src.main.java.server.domain.ReportUnit;
import a5.fmaster.src.main.java.server.handler.AdminOperationsHandler;
import a5.fmaster.src.main.java.server.handler.ParkingOperationsHandler;
import a5.fmaster.src.main.java.server.handler.PaymentHandler;
import a5.fmaster.src.main.java.server.handler.ReportsHandler;

public class ParkingServerImpl extends Observable implements ParkingInterface {
	// Implementations must have an explicit constructor
	// in order to declare the RemoteException exception
	private class WrappedObserver implements Observer, Serializable {

		private static final long serialVersionUID = 1L;

		private RemoteObserver ro = null;

		public WrappedObserver(RemoteObserver ro) {
			this.ro = ro;
		}

		@Override
		public void update(Observable o, Object arg) {
			try {
				ro.update(o.toString(), arg);
			} catch (RemoteException e) {
				System.out.println("Removing observer: " + this);
				o.deleteObserver(this);
				clientList.removeElement(o);
			}
		}

	}

	private static final long serialVersionUID = 1L;
	private ParkingDatabaseAccess db;
	public ParkingOperationsHandler parkingOpsHandler;
	public PaymentHandler paymentHandler;
	public AdminOperationsHandler adminOpsHandler;
	public ReportsHandler reportsHandler;
	private Vector clientList;

	public ParkingServerImpl() throws java.rmi.RemoteException {
		super();
		initializeDB();
		initializeHandlers();
		clientList = new Vector();
	}

	private void initializeDB() {
		db = ParkingDatabaseAccess.getInstance();
		db.runScript("cs414/a5/fmaster/src/main/java/database/populateDBScript.sql");
	}

	private void initializeHandlers() {
		parkingOpsHandler = ParkingOperationsHandler.getInstance(db);
		paymentHandler = PaymentHandler.getInstance(db);
		adminOpsHandler = AdminOperationsHandler.getInstance(db);
		reportsHandler = ReportsHandler.getInstance(db);
	}

	@Override
	public void addObserver(RemoteObserver o) throws RemoteException {
		WrappedObserver mo = new WrappedObserver(o);
		addObserver(mo);
		System.out.println("Added observer:" + mo);
		addClient(o);
	}

	@Override
	public void addClient(RemoteObserver o) throws RemoteException {
		clientList.addElement(o);
	}

	@Override
	public boolean isAdminClientStarted() throws RemoteException {
		for (int i = 0; i < clientList.size(); i++) {
			RemoteObserver ro = (RemoteObserver) clientList.elementAt(i);
			try {
				if (ro.getClientType().equals("ADMIN")) {
					return true;
				}
			} catch (RemoteException re) {
				System.out.println("Removing client: " + ro);
				clientList.removeElement(ro);
			}
		}
		return false;
	}

	@Override
	public int getCurrentAvailability() throws RemoteException {
		return parkingOpsHandler.getCurrentAvailability();
	}

	@Override
	public int getCurrentParkingSize() throws RemoteException {
		return parkingOpsHandler.getCurrentParkingSize();
	}

	@Override
	public List<ParkingRate> getParkingRates() throws RemoteException {
		return parkingOpsHandler.getParkingRates();
	}

	@Override
	public boolean isParkingAvailable() throws RemoteException {
		return parkingOpsHandler.isParkingAvailable();
	}

	@Override
	public boolean isParkingEmpty() throws RemoteException {
		return parkingOpsHandler.isParkingEmpty();
	}

	@Override
	public int getTicket() {
		return parkingOpsHandler.getTicket();
	}

	@Override
	public void openEntryGate() {
		parkingOpsHandler.openEntryGate();
		// Update for availability
		setChanged();
		notifyObservers(new Date());
	}

	@Override
	public void closeEntryGate() {
		parkingOpsHandler.closeEntryGate();
	}

	@Override
	public boolean isTicketValid(int ticketNumber) {
		return parkingOpsHandler.isTicketValid(ticketNumber);
	}

	@Override
	public void submitTicket(int ticketNumber) {
		paymentHandler.submitTicket(ticketNumber);
		// Update for occupancy reports
		setChanged();
		notifyObservers(new Date());
	}

	@Override
	public double calculateTotalPayment(int ticketNumber) {
		return paymentHandler.calculateTotalPayment(ticketNumber);
	}

	@Override
	public double getMaximumPayment() {
		return paymentHandler.getMaximumPayment();
	}

	@Override
	public void enterPayment(int ticketNumber, double amount) {
		paymentHandler.enterPayment(ticketNumber, amount);
		// Update for revenue reports
		setChanged();
		notifyObservers(new Date());
	}

	@Override
	public void openExitGate() {
		parkingOpsHandler.openExitGate();
		// Update for availability
		setChanged();
		notifyObservers(new Date());
	}

	@Override
	public void closeExitGate() {
		parkingOpsHandler.closeExitGate();
	}

	@Override
	public boolean validateCreditPayment(String name, String address, String creditCard, String securityCode, String expDate) {
		return paymentHandler.validateCreditPayment(name, address, creditCard, securityCode, expDate);
	}

	@Override
	public void enterPaymentException(String name, String license, double amountDue) {
		paymentHandler.enterPaymentException(name, license, amountDue);
	}

	@Override
	public int getCurrentParkingOccupancy() throws RemoteException {
		return parkingOpsHandler.getCurrentParkingOccupancy();
	}

	@Override
	public boolean addAdminAccount(String username, String password, String securityQuestion, String securityAnswer) throws RemoteException {
		return adminOpsHandler.addAdminAccount(username, password, securityQuestion, securityAnswer);
	}

	@Override
	public boolean disableAccount(String username) throws RemoteException {
		return adminOpsHandler.disableAccount(username);
	}

	@Override
	public boolean verifyPassword(String oldPwd) throws RemoteException {
		return adminOpsHandler.verifyPassword(oldPwd);
	}

	@Override
	public void setPassword(String newPwd) throws RemoteException {
		adminOpsHandler.setPassword(newPwd);
	}

	@Override
	public void logout() throws RemoteException {
		adminOpsHandler.logout();
	}

	@Override
	public void updateParkingSize(int newSize) throws RemoteException {
		parkingOpsHandler.updateParkingSize(newSize);
		// Update for parking size
		setChanged();
		notifyObservers(new Date());
	}

	@Override
	public void updateParkingRates(List<ParkingRate> parkingRates) throws RemoteException {
		parkingOpsHandler.updateParkingRates(parkingRates);
		// Update for parking rates
		setChanged();
		notifyObservers(new Date());
	}

	@Override
	public void setPassword(String username, String newPwd) throws RemoteException {
		adminOpsHandler.setPassword(username, newPwd);
	}

	@Override
	public boolean verifySecurityAnswer(String username, String securityAns) throws RemoteException {
		return adminOpsHandler.verifySecurityAnswer(username, securityAns);
	}

	@Override
	public boolean isActiveUserExist(String username) throws RemoteException {
		return adminOpsHandler.isActiveUserExist(username);
	}

	@Override
	public String getSecurityQuestion(String username) throws RemoteException {
		return adminOpsHandler.getSecurityQuestion(username);
	}

	@Override
	public boolean login(String username, String password) throws RemoteException {
		return adminOpsHandler.login(username, password);
	}

	@Override
	public ReportUnit getMostUsedHourInLastMonth() throws RemoteException {
		return reportsHandler.getMostUsedHourInLastMonth();
	}

	@Override
	public ReportUnit getLeastUsedHourInLastMonth() throws RemoteException {
		return reportsHandler.getLeastUsedHourInLastMonth();
	}

	@Override
	public ReportUnit getLastMonth() throws RemoteException {
		return reportsHandler.getLastMonth();
	}

	@Override
	public List<ReportUnit> getDailyOccupancyForLastMonth() throws RemoteException {
		return reportsHandler.getDailyOccupancyForLastMonth();
	}

	@Override
	public ReportUnit getMaxRevenueDayInLastMonth() throws RemoteException {
		return reportsHandler.getMaxRevenueDayInLastMonth();
	}

	@Override
	public boolean isValidDayMonthYearNotInFuture(String dayMonthYear) throws RemoteException {
		return reportsHandler.isValidDayMonthYearNotInFuture(dayMonthYear);
	}

	@Override
	public List<ReportUnit> getHourlyRevenueForDayMonthYear(ReportUnit dayMonthYear) throws RemoteException {
		return reportsHandler.getHourlyRevenueForDayMonthYear(dayMonthYear);
	}

	@Override
	public boolean isValidMonthYearNotInFuture(String monthYear) throws RemoteException {
		return reportsHandler.isValidMonthYearNotInFuture(monthYear);
	}

	@Override
	public List<ReportUnit> getDailyRevenueForMonthYear(ReportUnit monthYear) throws RemoteException {
		return reportsHandler.getDailyRevenueForMonthYear(monthYear);
	}

	@Override
	public boolean isValidYearNotInFuture(String year) throws RemoteException {
		return reportsHandler.isValidYearNotInFuture(year);
	}

	@Override
	public List<ReportUnit> getMonthlyRevenueForYear(ReportUnit year) throws RemoteException {
		return reportsHandler.getMonthlyRevenueForYear(year);
	}
}