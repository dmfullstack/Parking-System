/**
 * 
 */
package cs414.a5.fmaster.main.java.server.handler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.StringTokenizer;

import cs414.a5.fmaster.main.java.server.database.ParkingDatabaseAccess;
import cs414.a5.fmaster.main.java.server.domain.ReportUnit;

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

	public ReportUnit getMostUsedHourInLastMonth() {
		ReportUnit lastMonth = getLastMonth();
		List<ReportUnit> hoursInLastMonth = getHoursDaysInMonth(lastMonth);
		setHourlyStatsOfMonth(hoursInLastMonth);
		ReportUnit mostUsedHour = Collections.max(hoursInLastMonth, new OccupancyPercentComparator());
		return mostUsedHour;
	}

	public ReportUnit getLeastUsedHourInLastMonth() {
		ReportUnit lastMonth = getLastMonth();
		List<ReportUnit> hoursInLastMonth = getHoursDaysInMonth(lastMonth);
		setHourlyStatsOfMonth(hoursInLastMonth);
		ReportUnit leastUsedHour = Collections.min(hoursInLastMonth, new OccupancyPercentComparator());
		return leastUsedHour;
	}

	public ReportUnit getMaxRevenueDayInLastMonth() {
		ReportUnit lastMonth = getLastMonth();
		List<ReportUnit> daysInLastMonth = getDaysInMonth(lastMonth);
		setDailyRevenueOfMonth(daysInLastMonth);
		ReportUnit maxRevenueDay = Collections.max(daysInLastMonth, new PaymentComparator());
		return maxRevenueDay;
	}

	public List<ReportUnit> getHourlyRevenueForDayMonthYear(ReportUnit dayMonthYear) {
		List<ReportUnit> hoursInDay = getHoursInDay(dayMonthYear);
		setHourlyRevenueOfDay(hoursInDay);
		return hoursInDay;
	}

	public List<ReportUnit> getDailyRevenueForMonthYear(ReportUnit monthYear) {
		List<ReportUnit> daysInMonth = getDaysInMonth(monthYear);
		setDailyRevenueOfMonth(daysInMonth);
		return daysInMonth;
	}

	public List<ReportUnit> getMonthlyRevenueForYear(ReportUnit year) {
		List<ReportUnit> monthsInYear = getMonthsInYear(year);
		setMonthlyRevenueOfYear(monthsInYear);
		return monthsInYear;
	}

	public List<ReportUnit> getDailyOccupancyForLastMonth() {
		ReportUnit lastMonth = getLastMonth();
		List<ReportUnit> daysInLastMonth = getDaysInMonth(lastMonth);
		setDailyOccupancyOfMonth(daysInLastMonth);
		return daysInLastMonth;
	}

	public ReportUnit getLastMonth() {
		Calendar today = Calendar.getInstance();
		int currentMonth = today.get(Calendar.MONTH) + 1;
		int currentYear = today.get(Calendar.YEAR);
		ReportUnit lastMonth = new ReportUnit();
		if (currentMonth == 1) {
			lastMonth.setMonth(12);
			lastMonth.setYear(currentYear - 1);
		} else {
			lastMonth.setMonth(currentMonth - 1);
			lastMonth.setYear(currentYear);
		}
		return lastMonth;
	}

	
	private List<ReportUnit> getHoursInDay(ReportUnit dayMonthYear) {
		List<ReportUnit> hoursInDay = new ArrayList<ReportUnit>();
		int day = dayMonthYear.getDay();
		int month = dayMonthYear.getMonth();
		int year = dayMonthYear.getYear();
		for (int j = 1; j <= 24; j++) {
			ReportUnit hr = new ReportUnit();
			hr.setHour(j);
			hr.setDay(day);
			hr.setMonth(month);
			hr.setYear(year);
			hoursInDay.add(hr);
		}
		return hoursInDay;
	}

	private List<ReportUnit> getHoursDaysInMonth(ReportUnit selectedMonth) {
		List<ReportUnit> hoursInMonth = new ArrayList<ReportUnit>();
		int month = selectedMonth.getMonth();
		int year = selectedMonth.getYear();
		Calendar mycal = new GregorianCalendar(year, month - 1, 1);
		int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
		for (int i = 1; i <= daysInMonth; i++) {
			for (int j = 1; j <= 24; j++) {
				ReportUnit hr = new ReportUnit();
				hr.setHour(j);
				hr.setDay(i);
				hr.setMonth(month);
				hr.setYear(year);
				hoursInMonth.add(hr);
			}
		}
		return hoursInMonth;
	}

	private List<ReportUnit> getDaysInMonth(ReportUnit selectedMonth) {
		List<ReportUnit> hoursInMonth = new ArrayList<ReportUnit>();
		int month = selectedMonth.getMonth();
		int year = selectedMonth.getYear();
		Calendar mycal = new GregorianCalendar(year, month - 1, 1);
		int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
		for (int i = 1; i <= daysInMonth; i++) {
			ReportUnit day = new ReportUnit();
			day.setDay(i);
			day.setMonth(month);
			day.setYear(year);
			hoursInMonth.add(day);
		}
		return hoursInMonth;
	}
	
	private List<ReportUnit> getMonthsInYear(ReportUnit selectedYear) {
		int year = selectedYear.getYear();
		List<ReportUnit> monthsInYear = new ArrayList<ReportUnit>();
		for (int i = 1; i <= 12; i++) {
			ReportUnit month = new ReportUnit();
			month.setMonth(i);
			month.setYear(year);
			monthsInYear.add(month);
		}
		return monthsInYear;
	}

	private void setHourlyStatsOfMonth(List<ReportUnit> hoursInSelectedMonth) {
		for (ReportUnit hour : hoursInSelectedMonth) {
			db.setParkingAvailabilityInHourInReportUnit(hour);
			db.setParkingSizeInHourInReportUnit(hour);
			int size = hour.getParkingSize();
			int availability = hour.getAvailability();
			int occupancy = size - availability;
			if (!(size == 0)) {
				double occupancyPercent = occupancy * 100 / size;
				hour.setOccupancyPercent(occupancyPercent);
			}
		}
	}

	private void setHourlyRevenueOfDay(List<ReportUnit> hoursInSelectedDay) {
		for (ReportUnit hr : hoursInSelectedDay) {
			db.setPaymentForHourInReportUnit(hr);
		}
	}

	private void setDailyRevenueOfMonth(List<ReportUnit> daysInSelectedMonth) {
		for (ReportUnit day : daysInSelectedMonth) {
			db.setPaymentForDayInReportUnit(day);
		}
	}

	private void setMonthlyRevenueOfYear(List<ReportUnit> monthsInSelectedYear) {
		for (ReportUnit month : monthsInSelectedYear) {
			db.setPaymentForMonthInReportUnit(month);
		}
	}
	
	private void setDailyOccupancyOfMonth(List<ReportUnit> daysInSelectedMonth) {
		for (ReportUnit day : daysInSelectedMonth) {
			db.setOccupancyForDayInReportUnit(day);
		}
	}

	public boolean isValidYearNotInFuture(String yearStr) {
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
		return !enteredYear.after(thisYear);
	}

	public boolean isValidMonthYearNotInFuture(String monthYear) {
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
		return !enterMonthYear.after(thisMonth);
	}

	public boolean isValidDayMonthYearNotInFuture(String dayMonthYearStr) {
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
		return !enteredDayMonthYear.after(today);
	}
	
	private class OccupancyPercentComparator implements Comparator<ReportUnit> {

		@Override
		public int compare(ReportUnit first, ReportUnit second) {
			return new Double(first.getOccupancyPercent()).compareTo(new Double(second.getOccupancyPercent()));
		}
	}

	private class PaymentComparator implements Comparator<ReportUnit> {

		@Override
		public int compare(ReportUnit first, ReportUnit second) {
			return new Double(first.getPayment()).compareTo(new Double(second.getPayment()));
		}
	}
}