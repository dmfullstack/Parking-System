/**
 * 
 */
package cs414.fmaster.parking.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
		StringTokenizer st = new StringTokenizer(expDate, "-");
		int month = Integer.parseInt(st.nextToken());
		if(month > 12) {
			return false;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("MM-yyyy");
		Date expDt = null;
		try {
			expDt = sdf.parse(expDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date now = new Date();
		return expDt.after(now);
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
}