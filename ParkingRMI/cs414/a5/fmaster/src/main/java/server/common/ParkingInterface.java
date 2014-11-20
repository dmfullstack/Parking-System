package a5.fmaster.src.main.java.server.common;

import java.rmi.RemoteException;
import java.util.List;

import a5.fmaster.src.main.java.client.RemoteObserver;
import a5.fmaster.src.main.java.server.domain.ParkingRate;
import a5.fmaster.src.main.java.server.domain.ReportUnit;

public interface ParkingInterface extends java.rmi.Remote {
	public void addObserver(RemoteObserver observer) throws java.rmi.RemoteException;
	
	public void addClient(RemoteObserver o) throws RemoteException;
	
	public boolean isAdminClientStarted() throws RemoteException;
		
	public int getCurrentAvailability() throws java.rmi.RemoteException;

	public int getCurrentParkingSize() throws java.rmi.RemoteException;

	public List<ParkingRate> getParkingRates() throws java.rmi.RemoteException;

	public boolean isParkingAvailable() throws java.rmi.RemoteException;

	public boolean isParkingEmpty() throws java.rmi.RemoteException;

	public int getTicket() throws java.rmi.RemoteException;

	public void openEntryGate() throws java.rmi.RemoteException;

	public void closeEntryGate() throws java.rmi.RemoteException;

	public boolean isTicketValid(int ticketNumber) throws java.rmi.RemoteException;

	public void submitTicket(int ticketNumber) throws java.rmi.RemoteException;

	public double calculateTotalPayment(int ticketNumber) throws java.rmi.RemoteException;

	public double getMaximumPayment() throws java.rmi.RemoteException;

	public void enterPayment(int ticketNumber, double amountDue) throws java.rmi.RemoteException;

	public void openExitGate() throws java.rmi.RemoteException;

	public void closeExitGate() throws java.rmi.RemoteException;

	public boolean validateCreditPayment(String name, String address, String creditCard, String securityCode, String expDate)
			throws java.rmi.RemoteException;

	public void enterPaymentException(String name, String license, double amountDue) throws java.rmi.RemoteException;

	public int getCurrentParkingOccupancy() throws java.rmi.RemoteException;

	public boolean addAdminAccount(String addUsername, String password, String securityQues, String securityAns) throws java.rmi.RemoteException;

	public boolean disableAccount(String disableUsername) throws java.rmi.RemoteException;

	public boolean verifyPassword(String oldPwd) throws java.rmi.RemoteException;

	public void setPassword(String newPwd) throws java.rmi.RemoteException;

	public void logout() throws java.rmi.RemoteException;

	public void updateParkingSize(int newSizeInt) throws java.rmi.RemoteException;

	public void updateParkingRates(List<ParkingRate> parkingRates) throws java.rmi.RemoteException;

	public void setPassword(String username, String newPwd) throws java.rmi.RemoteException;

	public boolean verifySecurityAnswer(String username, String securityAns) throws java.rmi.RemoteException;

	public boolean isActiveUserExist(String username) throws java.rmi.RemoteException;

	public String getSecurityQuestion(String username) throws java.rmi.RemoteException;

	public boolean login(String username, String password) throws java.rmi.RemoteException;

	public ReportUnit getMostUsedHourInLastMonth() throws java.rmi.RemoteException;

	public ReportUnit getLeastUsedHourInLastMonth() throws java.rmi.RemoteException;

	public ReportUnit getLastMonth() throws java.rmi.RemoteException;

	public List<ReportUnit> getDailyOccupancyForLastMonth() throws java.rmi.RemoteException;

	public ReportUnit getMaxRevenueDayInLastMonth() throws java.rmi.RemoteException;

	public boolean isValidDayMonthYearNotInFuture(String dayMonthYearStr) throws java.rmi.RemoteException;

	public List<ReportUnit> getHourlyRevenueForDayMonthYear(ReportUnit dayMonthYear) throws java.rmi.RemoteException;

	public boolean isValidMonthYearNotInFuture(String monthYearStr) throws java.rmi.RemoteException;

	public List<ReportUnit> getDailyRevenueForMonthYear(ReportUnit monthYear) throws java.rmi.RemoteException;

	public boolean isValidYearNotInFuture(String yearStr) throws java.rmi.RemoteException;

	public List<ReportUnit> getMonthlyRevenueForYear(ReportUnit year) throws java.rmi.RemoteException;
}
