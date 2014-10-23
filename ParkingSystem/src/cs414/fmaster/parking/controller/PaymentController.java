/**
 * 
 */
package cs414.fmaster.parking.controller;

import java.util.Calendar;
import java.util.List;

import cs414.fmaster.parking.database.ParkingDatabaseAccess;

/**
 * @author MasterF
 * 
 */
public class PaymentController {
	ParkingDatabaseAccess db;
	private static PaymentController instance = null;

	private PaymentController(ParkingDatabaseAccess db) {
		this.db = db;
	}

	public static PaymentController getInstance(ParkingDatabaseAccess db) {
		if (instance == null) {
			instance = new PaymentController(db);
		}
		return instance;
	}

	public double calculateTotalPayment(int ticketNumber) {
		Calendar issueTime = db.getTicketIssueTime(ticketNumber);
		long issueTimeMS = issueTime.getTimeInMillis();
		long currentTimeMS = Calendar.getInstance().getTimeInMillis();
		long parkingTimeMS = currentTimeMS - issueTimeMS;
		int hours = (int) (parkingTimeMS/1000/60/60);
		int minutes = (int) (parkingTimeMS/1000/60);
		int seconds = (int) (parkingTimeMS/1000);
		if(seconds > 0) {
			minutes++;
		}
		if((hours > 0 && minutes > 0) || (hours == 0 && minutes > 30)) {
			hours++;
		}
		
		List<ParkingRate> parkingRates = db.getParkingRates();
		double _30minutes = parkingRates.get(0).getRate();
		double _1hour = parkingRates.get(1).getRate();
		double _2hours = parkingRates.get(2).getRate();
		double _10hours = parkingRates.get(3).getRate();
		double _24hours = parkingRates.get(4).getRate();
		
		double totalPayment = 0;
		if(hours > 10) {
			totalPayment = totalPayment + _24hours;
		}
		else if(hours > 2) {
			totalPayment = totalPayment + _10hours;
		}
		else if(hours > 1) {
			totalPayment = totalPayment + _2hours;
		}
		else if(hours == 1 || (hours == 0 && minutes > 30)) {
			totalPayment = totalPayment + _1hour;
		}
		else if(hours == 0 && minutes <= 30) {
			totalPayment = totalPayment + _30minutes;
		}
		return totalPayment;
	}

	public void createPayment(int ticketNumber, double amount) {
		db.makePayment(amount, ticketNumber);
		payOffTicket(ticketNumber);		
	}

	private void payOffTicket(int ticketNumber) {
		double totalCharge = calculateTotalPayment(ticketNumber);
		double totalPaymentsMade = db.getPaymentForTicket(ticketNumber);
		if(totalPaymentsMade == totalCharge) {
			db.setTicketAsPaid(ticketNumber);
		}
	}
}
