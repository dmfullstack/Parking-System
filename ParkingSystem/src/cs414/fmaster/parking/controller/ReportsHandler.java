/**
 * 
 */
package cs414.fmaster.parking.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;

import cs414.fmaster.parking.database.ParkingDatabaseAccess;

/**
 * @author MasterF
 * 
 */
public class ReportsHandler {
	private ParkingDatabaseAccess db;
	private static ReportsHandler instance = null;

	private ReportsHandler(ParkingDatabaseAccess db) {
		this.db = db;
	}

	public static ReportsHandler getInstance(ParkingDatabaseAccess db) {
		if (instance == null) {
			instance = new ReportsHandler(db);
		}
		return instance;
	}

	// HAS SYSOUTS

	public ParkingTime getMostUsedHourInLastMonth() {
		ParkingTime lastMonth = getLastMonth();
		List<ParkingTime> hoursInLastMonth = getHoursDaysInMonth(lastMonth);
		setHourlyStatsOfMonth(hoursInLastMonth);
		ParkingTime mostUsedHour = Collections.max(hoursInLastMonth, new OccupancyPercentComparator());
		return mostUsedHour;
	}

	public ParkingTime getLeastUsedHourInLastMonth() {
		ParkingTime lastMonth = getLastMonth();
		List<ParkingTime> hoursInLastMonth = getHoursDaysInMonth(lastMonth);
		setHourlyStatsOfMonth(hoursInLastMonth);
		ParkingTime leastUsedHour = Collections.min(hoursInLastMonth, new OccupancyPercentComparator());
		return leastUsedHour;
	}

	public ParkingTime getMaxRevenueDayInLastMonth() {
		ParkingTime lastMonth = getLastMonth();
		List<ParkingTime> daysInLastMonth = getDaysInMonth(lastMonth);
		setDailyRevenueOfMonth(daysInLastMonth);
		ParkingTime maxRevenueDay = Collections.max(daysInLastMonth, new PaymentComparator());
		return maxRevenueDay;
	}

	public List<ParkingTime> getHourlyRevenueForDayMonthYear(ParkingTime dayMonthYear) {
		List<ParkingTime> hoursInDay = getHoursInDay(dayMonthYear);
		setHourlyRevenueOfDay(hoursInDay);
		return hoursInDay;
	}

	public List<ParkingTime> getDailyRevenueForMonthYear(ParkingTime monthYear) {
		List<ParkingTime> daysInMonth = getDaysInMonth(monthYear);
		setDailyRevenueOfMonth(daysInMonth);
		return daysInMonth;
	}

	public List<ParkingTime> getMonthlyRevenueForYear(ParkingTime year) {
		List<ParkingTime> monthsInYear = getMonthsInYear(year);
		setMonthlyRevenueOfYear(monthsInYear);
		return monthsInYear;
	}

	public List<ParkingTime> getDailyOccupancyForLastMonth() {
		ParkingTime lastMonth = getLastMonth();
		List<ParkingTime> daysInLastMonth = getDaysInMonth(lastMonth);
		setDailyOccupancyOfMonth(daysInLastMonth);
		return daysInLastMonth;
	}

	public ParkingTime getLastMonth() {
		Calendar today = Calendar.getInstance();
		int currentMonth = today.get(Calendar.MONTH) + 1;
		int currentYear = today.get(Calendar.YEAR);
		ParkingTime lastMonth = new ParkingTime();
		if (currentMonth == 1) {
			lastMonth.setMonth(12);
			lastMonth.setYear(currentYear - 1);
		} else {
			lastMonth.setMonth(currentMonth - 1);
			lastMonth.setYear(currentYear);
		}
		return lastMonth;
	}

	private List<ParkingTime> getHoursInDay(ParkingTime dayMonthYear) {
		List<ParkingTime> hoursInDay = new ArrayList<ParkingTime>();
		int day = dayMonthYear.getDay();
		int month = dayMonthYear.getMonth();
		int year = dayMonthYear.getYear();
		for (int j = 1; j <= 24; j++) {
			ParkingTime hr = new ParkingTime();
			hr.setHour(j);
			hr.setDay(day);
			hr.setMonth(month);
			hr.setYear(year);
			hoursInDay.add(hr);
		}
		return hoursInDay;
	}

	private List<ParkingTime> getHoursDaysInMonth(ParkingTime selectedMonth) {
		List<ParkingTime> hoursInMonth = new ArrayList<ParkingTime>();
		int month = selectedMonth.getMonth();
		int year = selectedMonth.getYear();
		Calendar mycal = new GregorianCalendar(year, month - 1, 1);
		int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
		for (int i = 1; i <= daysInMonth; i++) {
			for (int j = 1; j <= 24; j++) {
				ParkingTime hr = new ParkingTime();
				hr.setHour(j);
				hr.setDay(i);
				hr.setMonth(month);
				hr.setYear(year);
				hoursInMonth.add(hr);
			}
		}
		return hoursInMonth;
	}

	private List<ParkingTime> getDaysInMonth(ParkingTime selectedMonth) {
		List<ParkingTime> hoursInMonth = new ArrayList<ParkingTime>();
		int month = selectedMonth.getMonth();
		int year = selectedMonth.getYear();
		Calendar mycal = new GregorianCalendar(year, month - 1, 1);
		int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
		for (int i = 1; i <= daysInMonth; i++) {
			ParkingTime day = new ParkingTime();
			day.setDay(i);
			day.setMonth(month);
			day.setYear(year);
			hoursInMonth.add(day);
		}
		return hoursInMonth;
	}
	
	private List<ParkingTime> getMonthsInYear(ParkingTime selectedYear) {
		int year = selectedYear.getYear();
		List<ParkingTime> monthsInYear = new ArrayList<ParkingTime>();
		for (int i = 1; i <= 12; i++) {
			ParkingTime month = new ParkingTime();
			month.setMonth(i);
			month.setYear(year);
			monthsInYear.add(month);
		}
		return monthsInYear;
	}

	private void setHourlyStatsOfMonth(List<ParkingTime> hoursInSelectedMonth) {
		for (ParkingTime hour : hoursInSelectedMonth) {
			db.setParkingAvailabilityInHourInParkingTime(hour);
			db.setParkingAvailabilityInHourInParkingTime(hour);
			int size = hour.getParkingSize();
			int availability = hour.getAvailability();
			int occupancy = size - availability;
			if (!(size == 0)) {
				double occupancyPercent = occupancy * 100 / size;
				hour.setOccupancyPercent(occupancyPercent);
			}
		}
	}

	private void setHourlyRevenueOfDay(List<ParkingTime> hoursInSelectedDay) {
		for (ParkingTime hr : hoursInSelectedDay) {
			db.setPaymentForHourInParkingTime(hr);
		}
	}

	private void setDailyRevenueOfMonth(List<ParkingTime> daysInSelectedMonth) {
		for (ParkingTime day : daysInSelectedMonth) {
			db.setPaymentForDayInParkingTime(day);
		}
	}

	private void setMonthlyRevenueOfYear(List<ParkingTime> monthsInSelectedYear) {
		for (ParkingTime month : monthsInSelectedYear) {
			db.setPaymentForMonthInParkingTime(month);
		}
	}
	
	private void setDailyOccupancyOfMonth(List<ParkingTime> daysInSelectedMonth) {
		for (ParkingTime day : daysInSelectedMonth) {
			db.setOccupancyForDayInParkingTime(day);
		}
	}

	private class OccupancyPercentComparator implements Comparator<ParkingTime> {

		@Override
		public int compare(ParkingTime first, ParkingTime second) {
			return new Double(first.getOccupancyPercent()).compareTo(new Double(second.getOccupancyPercent()));
		}
	}

	private class PaymentComparator implements Comparator<ParkingTime> {

		@Override
		public int compare(ParkingTime first, ParkingTime second) {
			return new Double(first.getPayment()).compareTo(new Double(second.getPayment()));
		}
	}
}