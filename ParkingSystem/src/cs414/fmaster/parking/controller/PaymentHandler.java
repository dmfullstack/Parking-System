/**
 * 
 */
package cs414.fmaster.parking.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.StringTokenizer;

import cs414.fmaster.parking.database.ParkingDatabaseAccess;

/**
 * @author MasterF
 * 
 */
public class PaymentHandler {
	private ParkingDatabaseAccess db;
	private static PaymentHandler instance = null;
	private static double totalAmountPaidWithoutTicket = 0;

	private PaymentHandler(ParkingDatabaseAccess db) {
		this.db = db;
	}

	public static PaymentHandler getInstance(ParkingDatabaseAccess db) {
		if (instance == null) {
			instance = new PaymentHandler(db);
		}
		return instance;
	}

	public double calculateTotalPayment(int ticketNumber) {
		Calendar issueTime = db.getTicketIssueTime(ticketNumber);
		long issueTimeMS = issueTime.getTimeInMillis();
		long currentTimeMS = Calendar.getInstance().getTimeInMillis();
		long parkingTimeMS = currentTimeMS - issueTimeMS;
		int hours = (int) (parkingTimeMS / 1000 / 60 / 60);
		int minutes = (int) (parkingTimeMS / 1000 / 60);
		int seconds = (int) (parkingTimeMS / 1000);
		if (seconds > 0) {
			minutes++;
		}
		if ((hours > 0 && minutes > 0) || (hours == 0 && minutes > 30)) {
			hours++;
		}

		List<ParkingRate> parkingRates = db.getParkingRates();
		double _30minutes = parkingRates.get(0).getRate();
		double _1hour = parkingRates.get(1).getRate();
		double _2hours = parkingRates.get(2).getRate();
		double _10hours = parkingRates.get(3).getRate();
		double _24hours = parkingRates.get(4).getRate();

		double totalPayment = 0;
		if (hours > 10) {
			totalPayment = totalPayment + _24hours;
		} else if (hours > 2) {
			totalPayment = totalPayment + _10hours;
		} else if (hours > 1) {
			totalPayment = totalPayment + _2hours;
		} else if (hours == 1 || (hours == 0 && minutes > 30)) {
			totalPayment = totalPayment + _1hour;
		} else if (hours == 0 && minutes <= 30) {
			totalPayment = totalPayment + _30minutes;
		}
		return totalPayment;
	}

	public void enterPayment(int ticketNumber, double amount) {
		if (ticketNumber == 0) {
			db.insertPayment(amount);
			payOffWithoutTicket(amount);
		} else {
			db.insertPayment(amount, ticketNumber);
			payOffTicket(ticketNumber);
		}
	}

	private void payOffWithoutTicket(double amountPaidNow) {
		double totalCharge = getMaximumPayment();
		totalAmountPaidWithoutTicket = totalAmountPaidWithoutTicket + amountPaidNow;
		if (totalAmountPaidWithoutTicket == totalCharge) {
			db.increaseParkingAvailability();
			totalAmountPaidWithoutTicket = 0;
		}
	}

	private void payOffTicket(int ticketNumber) {
		double totalCharge = calculateTotalPayment(ticketNumber);
		double totalPaymentsMade = db.getPaymentForTicket(ticketNumber);
		if (totalPaymentsMade == totalCharge) {
			db.setTicketAsPaid(ticketNumber);
			db.increaseParkingAvailability();
		}
	}

	public boolean validateCreditPayment(String name, String address, String creditCard, String securityCode, String expDate) {
		boolean isValidMonthYearStrInFuture = isValidMonthYearInFuture(expDate);
		return isValidMonthYearStrInFuture;
	}

	public boolean isValidMonthYearInPast(String monthYear) {
		StringTokenizer st = new StringTokenizer(monthYear, "-");
		int month = Integer.parseInt(st.nextToken());
		if (month > 12 || month < 1) {
			return false;
		}
		int year = Integer.parseInt(st.nextToken());
		Calendar enteredMonthYear = new GregorianCalendar();
		enteredMonthYear.set(Calendar.YEAR, year);
		enteredMonthYear.set(Calendar.MONTH, month - 1);
		enteredMonthYear.set(Calendar.DATE, 1);
		enteredMonthYear.set(Calendar.HOUR_OF_DAY, 0);
		enteredMonthYear.set(Calendar.MINUTE, 0);
		enteredMonthYear.set(Calendar.SECOND, 0);

		Calendar thisMonth = new GregorianCalendar();
		thisMonth.set(Calendar.DATE, 1);
		thisMonth.set(Calendar.HOUR_OF_DAY, 0);
		thisMonth.set(Calendar.MINUTE, 0);
		thisMonth.set(Calendar.SECOND, 0);
		return enteredMonthYear.before(thisMonth);
	}

	public boolean isValidMonthYearInFuture(String monthYear) {
		StringTokenizer st = new StringTokenizer(monthYear, "-");
		int month = Integer.parseInt(st.nextToken());
		if (month > 12 || month < 1) {
			return false;
		}
		int year = Integer.parseInt(st.nextToken());
		Calendar enterMonthYear = new GregorianCalendar();
		enterMonthYear.set(Calendar.YEAR, year);
		enterMonthYear.set(Calendar.MONTH, month - 1);
		enterMonthYear.set(Calendar.DATE, 1);
		enterMonthYear.set(Calendar.HOUR_OF_DAY, 0);
		enterMonthYear.set(Calendar.MINUTE, 0);
		enterMonthYear.set(Calendar.SECOND, 0);

		Calendar thisMonth = new GregorianCalendar();
		thisMonth.set(Calendar.DATE, 1);
		thisMonth.set(Calendar.HOUR_OF_DAY, 0);
		thisMonth.set(Calendar.MINUTE, 0);
		thisMonth.set(Calendar.SECOND, 0);
		return enterMonthYear.after(thisMonth);
	}

	public double getMaximumPayment() {
		double maxPayment = db.getMaximumRate();
		return maxPayment;
	}

	public void submitTicket(int ticketNumber) {
		db.setTicketAsSubmitted(ticketNumber);
	}

	public void enterPaymentException(String name, String license, double amountDue) {
		db.insertPaymentException(name, license, amountDue);
		db.increaseParkingAvailability();
	}

	public boolean isValidDayMonthYearInPast(String dayMonthYearStr) {
		StringTokenizer st = new StringTokenizer(dayMonthYearStr, "-");
		int month = Integer.parseInt(st.nextToken());
		if (month > 12 || month < 1) {
			return false;
		}
		
		int day = Integer.parseInt(st.nextToken());
		if (day > 31 || day < 1) {
			return false;
		}
		
		int year = Integer.parseInt(st.nextToken());
		
		Calendar daysInMonthCal = new GregorianCalendar(year, month - 1, 1);
		int maxDaysInMonth = daysInMonthCal.getActualMaximum(Calendar.DAY_OF_MONTH);
		if (day > maxDaysInMonth) {
			return false;
		}
		
		Calendar enteredDayMonthYear = new GregorianCalendar();
		enteredDayMonthYear.set(Calendar.YEAR, year);
		enteredDayMonthYear.set(Calendar.MONTH, month - 1);
		enteredDayMonthYear.set(Calendar.DATE, day);
		enteredDayMonthYear.set(Calendar.HOUR_OF_DAY, 0);
		enteredDayMonthYear.set(Calendar.MINUTE, 0);
		enteredDayMonthYear.set(Calendar.SECOND, 0);

		Calendar today = new GregorianCalendar();
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		return enteredDayMonthYear.before(today);
	}

	public boolean isValidYearInPast(String yearStr) {
		int year = Integer.parseInt(yearStr);
		
		Calendar enteredYear = new GregorianCalendar();
		enteredYear.set(Calendar.YEAR, year);
		enteredYear.set(Calendar.MONTH, 0);
		enteredYear.set(Calendar.DATE, 1);
		enteredYear.set(Calendar.HOUR_OF_DAY, 0);
		enteredYear.set(Calendar.MINUTE, 0);
		enteredYear.set(Calendar.SECOND, 0);

		Calendar thisYear = new GregorianCalendar();
		thisYear.set(Calendar.MONTH, 0);
		thisYear.set(Calendar.DATE, 1);
		thisYear.set(Calendar.HOUR_OF_DAY, 0);
		thisYear.set(Calendar.MINUTE, 0);
		thisYear.set(Calendar.SECOND, 0);
		return enteredYear.before(thisYear);
	}
}