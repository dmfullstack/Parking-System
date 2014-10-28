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

	public Hour getMostUsedHourInLastMonth() {
		Hour lastMonth = getLastMonth();
		List<Hour> hoursInLastMonth = getHoursDaysInMonth(lastMonth);
		setHourlyStatsOfMonth(hoursInLastMonth);
		Hour mostUsedHour = Collections.max(hoursInLastMonth, new OccupancyPercentComparator());
		return mostUsedHour;
	}

	public Hour getLeastUsedHourInLastMonth() {
		Hour lastMonth = getLastMonth();
		List<Hour> hoursInLastMonth = getHoursDaysInMonth(lastMonth);
		setHourlyStatsOfMonth(hoursInLastMonth);
		Hour leastUsedHour = Collections.min(hoursInLastMonth, new OccupancyPercentComparator());
		return leastUsedHour;
	}

	public Hour getLastMonth() {
		Calendar today = Calendar.getInstance();
		int currentMonth = today.get(Calendar.MONTH) + 1;
		int currentYear = today.get(Calendar.YEAR);
		Hour lastMonth = new Hour();
		if (currentMonth == 1) {
			lastMonth.setMonth(12);
			lastMonth.setYear(currentYear - 1);
		} else {
			lastMonth.setMonth(currentMonth - 1);
			lastMonth.setYear(currentYear);
		}
		return lastMonth;
	}

	private List<Hour> getHoursDaysInMonth(Hour selectedMonth) {
		List<Hour> hoursInMonth = new ArrayList<Hour>();
		int month = selectedMonth.getMonth();
		int year = selectedMonth.getYear();
		Calendar mycal = new GregorianCalendar(year, month - 1, 1);
		int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
		for (int i = 1; i <= daysInMonth; i++) {
			for (int j = 1; j <= 24; j++) {
				Hour hr = new Hour();
				hr.setHour(j);
				hr.setDay(i);
				hr.setMonth(month);
				hr.setYear(year);
				hoursInMonth.add(hr);
			}
		}
		return hoursInMonth;
	}

	private List<Hour> getDaysInMonth(Hour selectedMonth) {
		List<Hour> hoursInMonth = new ArrayList<Hour>();
		int month = selectedMonth.getMonth();
		int year = selectedMonth.getYear();
		Calendar mycal = new GregorianCalendar(year, month - 1, 1);
		int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
		for (int i = 1; i <= daysInMonth; i++) {
			Hour day = new Hour();
			day.setDay(i);
			day.setMonth(month);
			day.setYear(year);
			hoursInMonth.add(day);
		}
		return hoursInMonth;
	}
	
	private List<Hour> getMonthsInYear(Hour selectedYear) {
		List<Hour> monthsInYear = new ArrayList<Hour>();
		int year = selectedYear.getYear();
		
		for (int i = 1; i <= 12; i++) {
			Hour month = new Hour();
			month.setMonth(i);
			month.setYear(year);
			monthsInYear.add(month);
		}
		return monthsInYear;
	}

	private void setHourlyStatsOfMonth(List<Hour> hoursInSelectedMonth) {
		for (Hour hour : hoursInSelectedMonth) {
			db.setParkingAvailabilityInHour(hour);
			db.setParkingSizeInHour(hour);
			int size = hour.getParkingSize();
			int availability = hour.getAvailability();
			int occupancy = size - availability;
			if (!(size == 0)) {
				double occupancyPercent = occupancy * 100 / size;
				hour.setOccupancyPercent(occupancyPercent);
			}
		}
	}

	private void setMonthlyRevenueOfYear(List<Hour> monthsInSelectedYear) {
		for (Hour month : monthsInSelectedYear) {
			db.setPaymentForMonthInHour(month);
		}
	}
	
	private void setDailyRevenueOfMonth(List<Hour> daysInSelectedMonth) {
		for (Hour day : daysInSelectedMonth) {
			db.setPaymentForDayInHour(day);
		}
	}
	
	private void setDailyOccupancyOfMonth(List<Hour> daysInSelectedMonth) {
		for (Hour day : daysInSelectedMonth) {
			db.setOccupancyForDayInHour(day);
		}
	}

	private void setHourlyRevenueOfDay(List<Hour> hoursInSelectedDay) {
		for (Hour hr : hoursInSelectedDay) {
			db.setPaymentForHourInHour(hr);
		}
	}

	public Hour getMaxRevenueDayInLastMonth() {
		Hour lastMonth = getLastMonth();
		List<Hour> daysInLastMonth = getDaysInMonth(lastMonth);
		setDailyRevenueOfMonth(daysInLastMonth);
		Hour maxRevenueDay = Collections.max(daysInLastMonth, new PaymentComparator());
		return maxRevenueDay;
	}

	public class OccupancyPercentComparator implements Comparator<Hour> {

		@Override
		public int compare(Hour first, Hour second) {
			return new Double(first.getOccupancyPercent()).compareTo(new Double(second.getOccupancyPercent()));
		}
	}

	public class PaymentComparator implements Comparator<Hour> {

		@Override
		public int compare(Hour first, Hour second) {
			return new Double(first.getPayment()).compareTo(new Double(second.getPayment()));
		}
	}

	public List<Hour> getDailyRevenueForMonthYear(Hour monthYear) {
		List<Hour> daysInMonth = getDaysInMonth(monthYear);
		setDailyRevenueOfMonth(daysInMonth);
		return daysInMonth;
	}

	public List<Hour> getHourlyRevenueForDayMonthYear(Hour dayMonthYear) {
		List<Hour> hoursInDay = getHoursInDay(dayMonthYear);
		setHourlyRevenueOfDay(hoursInDay);
		return hoursInDay;
	}

	private List<Hour> getHoursInDay(Hour dayMonthYear) {
		List<Hour> hoursInDay = new ArrayList<Hour>();
		int day = dayMonthYear.getDay();
		int month = dayMonthYear.getMonth();
		int year = dayMonthYear.getYear();
		for (int j = 1; j <= 24; j++) {
			Hour hr = new Hour();
			hr.setHour(j);
			hr.setDay(day);
			hr.setMonth(month);
			hr.setYear(year);
			hoursInDay.add(hr);
		}
		return hoursInDay;
	}

	public List<Hour> getMonthlyRevenueForYear(Hour year) {
		List<Hour> monthsInYear = getMonthsInYear(year);
		setMonthlyRevenueOfYear(monthsInYear);
		return monthsInYear;
	}

	public List<Hour> getDailyOccupancyForLastMonth() {
		Hour lastMonth = getLastMonth();
		List<Hour> daysInLastMonth = getDaysInMonth(lastMonth);
		setDailyOccupancyOfMonth(daysInLastMonth);
		return daysInLastMonth;
	}
}