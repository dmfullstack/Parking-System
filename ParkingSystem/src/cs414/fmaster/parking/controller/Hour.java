/**
 * 
 */
package cs414.fmaster.parking.controller;

import java.util.Comparator;

/**
 * @author masterf
 * 
 */
public class Hour {
	private int hour;
	private int day;
	private int month;
	private int year;
	private int availability;
	private int parkingSize;
	private double occupancyPercent;
	private double payment;
	private int ticketCount;

	public double getPayment() {
		return payment;
	}

	public void setPayment(double payment) {
		this.payment = payment;
	}

	public Hour(int hour, int day) {
		super();
		this.hour = hour;
		this.day = day;
	}

	public Hour() {
		// TODO Auto-generated constructor stub
	}

	public int getHour() {
		return hour;
	}

	public int getDay() {
		return day;
	}

	public int getAvailability() {
		return availability;
	}

	public int getParkingSize() {
		return parkingSize;
	}

	public double getOccupancyPercent() {
		return occupancyPercent;
	}

	public void setAvailability(int availability) {
		this.availability = availability;
	}

	public void setParkingSize(int parkingSize) {
		this.parkingSize = parkingSize;
	}

	public void setOccupancyPercent(double occupancyPercent) {
		this.occupancyPercent = occupancyPercent;
	}

	public int getMonth() {
		return month;
	}

	public int getYear() {
		return year;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getTicketCount() {
		return ticketCount;
	}
	
	public void setTicketCount(int ticketCount) {
		this.ticketCount = ticketCount;
	}

}
