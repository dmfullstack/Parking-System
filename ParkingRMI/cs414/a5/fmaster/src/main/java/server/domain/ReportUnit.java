/**
 * 
 */
package a5.fmaster.src.main.java.server.domain;

import java.io.Serializable;

/**
 * @author masterf
 * 
 */
public class ReportUnit implements Serializable {
	private static final long serialVersionUID = 1L;
	private int hour;
	private int day;
	private int month;
	private int year;
	private int availability;
	private int parkingSize;
	private double occupancyPercent;
	private double payment;
	private int ticketCount;

	public ReportUnit() {
	}

	public int getHour() {
		return hour;
	}

	public int getDay() {
		return day;
	}

	public int getMonth() {
		return month;
	}

	public int getYear() {
		return year;
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

	public double getPayment() {
		return payment;
	}

	public int getTicketCount() {
		return ticketCount;
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

	public void setAvailability(int availability) {
		this.availability = availability;
	}

	public void setParkingSize(int parkingSize) {
		this.parkingSize = parkingSize;
	}

	public void setOccupancyPercent(double occupancyPercent) {
		this.occupancyPercent = occupancyPercent;
	}

	public void setPayment(double payment) {
		this.payment = payment;
	}

	public void setTicketCount(int ticketCount) {
		this.ticketCount = ticketCount;
	}

}
