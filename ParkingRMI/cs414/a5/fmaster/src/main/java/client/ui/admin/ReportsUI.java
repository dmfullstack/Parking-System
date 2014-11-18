/**
 * 
 */
package a5.fmaster.src.main.java.client.ui.admin;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.text.DateFormatSymbols;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import a5.fmaster.src.main.java.server.domain.ReportUnit;
import a5.fmaster.src.main.java.common.ParkingServerInterface;

/**
 * @author MasterF
 * 
 */
public class ReportsUI {

	private AdminMainUI adminMainUI;
	private ParkingServerInterface parking;
	public JPanel mainContentPnl = new JPanel(new GridBagLayout());
	private JPanel reportTypesPnl = new JPanel(new GridBagLayout());

	private JPanel mostLeastUsedHourReportPnl = new JPanel(new GridBagLayout());
	private JLabel lastMonth1Lbl = new JLabel();
	private JTable mostLeastUsedHourTbl = new JTable();
	private DefaultTableModel mostLeastUsedHourModel = new DefaultTableModel(new Object[][] {}, new String[] { " ", "Day", "Hour", "Occupancy %" });

	private JPanel maxRevenueReportPnl = new JPanel(new GridBagLayout());
	private JLabel lastMonth2Lbl = new JLabel();
	private JTable maxRevenueTbl = new JTable();
	private DefaultTableModel maxRevenueModel = new DefaultTableModel(new Object[][] {}, new String[] { "Day", "Revenue" });

	private JPanel hourlyReportGetDayPnl = new JPanel();
	private JTextField hourlyReportGetDayTxt = new JTextField(5);
	private JPanel hourlyRevenueReportPnl = new JPanel(new GridBagLayout());
	private JLabel hourlyRevenueReportDayLbl = new JLabel();
	private JTable hourlyRevenueReportTbl = new JTable();
	private DefaultTableModel hourlyRevenueReportModel = new DefaultTableModel(new Object[][] {}, new String[] { "Hour", "Revenue" });
	private String dayMonthYearStr = "";
	
	private JPanel dailyReportGetMonthPnl = new JPanel();
	private JTextField dailyReportGetMonthTxt = new JTextField(5);
	private JPanel dailyRevenueReportPnl = new JPanel(new GridBagLayout());
	private JLabel dailyRevenueReportMonthLbl = new JLabel();
	private JTable dailyRevenueReportTbl = new JTable();
	private DefaultTableModel dailyRevenueReportModel = new DefaultTableModel(new Object[][] {}, new String[] { "Day", "Revenue" });
	String monthYearStr = "";

	private JPanel monthlyReportGetYearPnl = new JPanel();
	private JTextField monthlyReportGetYearTxt = new JTextField(5);
	private JPanel monthlyRevenueReportPnl = new JPanel(new GridBagLayout());
	private JLabel monthlyRevenueReportYearLbl = new JLabel();
	private JTable monthlyRevenueReportTbl = new JTable();
	private DefaultTableModel monthlyRevenueReportModel = new DefaultTableModel(new Object[][] {}, new String[] { "Month", "Revenue" });
	String yearStr = "";

	private JPanel dailyOccupancyReportPnl = new JPanel(new GridBagLayout());
	private JLabel lastMonth3Lbl = new JLabel();
	private JTable dailyOccupancyReportTbl = new JTable();
	private DefaultTableModel dailyOccupancyReportModel = new DefaultTableModel(new Object[][] {}, new String[] { "Day", "No. of tickets issued" });

	private JButton backToReportsBtn = new JButton("Go Back to Reports");
	private JButton backBtn = new JButton("Go Back");
	private JButton logoutBtn = new JButton("Logout");

	private static ReportsUI instance = null;

	private ReportsUI(AdminMainUI adminMainUI, ParkingServerInterface parking) {
		this.adminMainUI = adminMainUI;
		this.parking = parking;
	}

	public static ReportsUI getInstance(AdminMainUI adminMainUI, ParkingServerInterface parking) {
		if (instance == null) {
			instance = new ReportsUI(adminMainUI, parking);
		}
		return instance;
	}

	public void setupUI() {
		mainContentPnl.setVisible(false);

		// Report types Panel
		JButton hourlyReportBtn = new JButton("Get Hourly Revenue Report for a particular day");
		JButton dailyReportBtn = new JButton("Get Daily Revenue Report for a particular month");
		JButton monthlyReportBtn = new JButton("Get Monthly Revenue Report for a particular year");
		JButton mostLeastReportBtn = new JButton("Get most and least used hours in last month");
		JButton maxRevenueReportBtn = new JButton("Get maximum revenue day in last month");
		JButton dailyOccupancyReportBtn = new JButton("Get daily occupancy counts for last month");

		adminMainUI.addGridBagComponent(reportTypesPnl, hourlyReportBtn, GridBagConstraints.BOTH, 0, 0, 2, 1);
		adminMainUI.addGridBagComponent(reportTypesPnl, dailyReportBtn, GridBagConstraints.BOTH, 0, 1, 2, 1);
		adminMainUI.addGridBagComponent(reportTypesPnl, monthlyReportBtn, GridBagConstraints.BOTH, 0, 2, 2, 1);
		adminMainUI.addGridBagComponent(reportTypesPnl, mostLeastReportBtn, GridBagConstraints.BOTH, 0, 3, 2, 1);
		adminMainUI.addGridBagComponent(reportTypesPnl, maxRevenueReportBtn, GridBagConstraints.BOTH, 0, 4, 2, 1);
		adminMainUI.addGridBagComponent(reportTypesPnl, dailyOccupancyReportBtn, GridBagConstraints.BOTH, 0, 5, 2, 1);

		hourlyReportBtn.addActionListener(new ReportsUIListener());
		dailyReportBtn.addActionListener(new ReportsUIListener());
		monthlyReportBtn.addActionListener(new ReportsUIListener());
		mostLeastReportBtn.addActionListener(new ReportsUIListener());
		maxRevenueReportBtn.addActionListener(new ReportsUIListener());
		dailyOccupancyReportBtn.addActionListener(new ReportsUIListener());

		// Most Least Used Hour in last Month Report Panel
		mostLeastUsedHourTbl.setModel(mostLeastUsedHourModel);
		mostLeastUsedHourTbl.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		mostLeastUsedHourTbl.setFillsViewportHeight(true);
		mostLeastUsedHourTbl.setEnabled(false);

		adminMainUI.addGridBagComponent(mostLeastUsedHourReportPnl, new JLabel("Hours with most and least occupancy in last month"),
				GridBagConstraints.BOTH, 0, 0);
		adminMainUI.addGridBagComponent(mostLeastUsedHourReportPnl, lastMonth1Lbl, GridBagConstraints.BOTH, 0, 1);
		adminMainUI.addGridBagComponent(mostLeastUsedHourReportPnl, mostLeastUsedHourTbl.getTableHeader(), GridBagConstraints.BOTH, 0, 2);
		adminMainUI.addGridBagComponent(mostLeastUsedHourReportPnl, mostLeastUsedHourTbl, GridBagConstraints.BOTH, 0, 3);

		// Maximum Revenue day in last Month Report Panel
		maxRevenueTbl.setModel(maxRevenueModel);
		maxRevenueTbl.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		maxRevenueTbl.setFillsViewportHeight(true);
		maxRevenueTbl.setEnabled(false);

		adminMainUI.addGridBagComponent(maxRevenueReportPnl, new JLabel("Day with maximum revenue in last month"), GridBagConstraints.BOTH, 0, 0);
		adminMainUI.addGridBagComponent(maxRevenueReportPnl, lastMonth2Lbl, GridBagConstraints.BOTH, 0, 1);
		adminMainUI.addGridBagComponent(maxRevenueReportPnl, maxRevenueTbl.getTableHeader(), GridBagConstraints.BOTH, 0, 2);
		adminMainUI.addGridBagComponent(maxRevenueReportPnl, maxRevenueTbl, GridBagConstraints.BOTH, 0, 3);

		// Hourly Revenue Report of particular month Report
		hourlyReportGetDayPnl.add(new JLabel("Enter mm-dd-yyyy: "));
		hourlyReportGetDayPnl.add(hourlyReportGetDayTxt);

		hourlyRevenueReportTbl.setModel(hourlyRevenueReportModel);
		hourlyRevenueReportTbl.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		hourlyRevenueReportTbl.setFillsViewportHeight(true);
		hourlyRevenueReportTbl.setEnabled(false);

		adminMainUI.addGridBagComponent(hourlyRevenueReportPnl, hourlyRevenueReportDayLbl, GridBagConstraints.BOTH, 0, 0);
		adminMainUI.addGridBagComponent(hourlyRevenueReportPnl, hourlyRevenueReportTbl.getTableHeader(), GridBagConstraints.BOTH, 0, 1);
		adminMainUI.addGridBagComponent(hourlyRevenueReportPnl, hourlyRevenueReportTbl, GridBagConstraints.BOTH, 0, 2);

		// Daily Revenue Report of particular month Report
		dailyReportGetMonthPnl.add(new JLabel("Enter mm-yyyy: "));
		dailyReportGetMonthPnl.add(dailyReportGetMonthTxt);

		dailyRevenueReportTbl.setModel(dailyRevenueReportModel);
		dailyRevenueReportTbl.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		dailyRevenueReportTbl.setFillsViewportHeight(true);
		dailyRevenueReportTbl.setEnabled(false);

		adminMainUI.addGridBagComponent(dailyRevenueReportPnl, dailyRevenueReportMonthLbl, GridBagConstraints.BOTH, 0, 0);
		adminMainUI.addGridBagComponent(dailyRevenueReportPnl, dailyRevenueReportTbl.getTableHeader(), GridBagConstraints.BOTH, 0, 1);
		adminMainUI.addGridBagComponent(dailyRevenueReportPnl, dailyRevenueReportTbl, GridBagConstraints.BOTH, 0, 2);

		// Monthly Revenue Report of particular year Report
		monthlyReportGetYearPnl.add(new JLabel("Enter yyyy: "));
		monthlyReportGetYearPnl.add(monthlyReportGetYearTxt);

		monthlyRevenueReportTbl.setModel(monthlyRevenueReportModel);
		monthlyRevenueReportTbl.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		monthlyRevenueReportTbl.setFillsViewportHeight(true);
		monthlyRevenueReportTbl.setEnabled(false);

		adminMainUI.addGridBagComponent(monthlyRevenueReportPnl, monthlyRevenueReportYearLbl, GridBagConstraints.BOTH, 0, 0);
		adminMainUI.addGridBagComponent(monthlyRevenueReportPnl, monthlyRevenueReportTbl.getTableHeader(), GridBagConstraints.BOTH, 0, 1);
		adminMainUI.addGridBagComponent(monthlyRevenueReportPnl, monthlyRevenueReportTbl, GridBagConstraints.BOTH, 0, 2);

		// Daily Occupancy Report of last month
		dailyOccupancyReportTbl.setModel(dailyOccupancyReportModel);
		dailyOccupancyReportTbl.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		dailyOccupancyReportTbl.setFillsViewportHeight(true);
		dailyOccupancyReportTbl.setEnabled(false);

		adminMainUI.addGridBagComponent(dailyOccupancyReportPnl, new JLabel("Daily Occupancy Report of last month"), GridBagConstraints.BOTH, 0, 0);
		adminMainUI.addGridBagComponent(dailyOccupancyReportPnl, lastMonth3Lbl, GridBagConstraints.BOTH, 0, 1);
		adminMainUI.addGridBagComponent(dailyOccupancyReportPnl, dailyOccupancyReportTbl.getTableHeader(), GridBagConstraints.BOTH, 0, 2);
		adminMainUI.addGridBagComponent(dailyOccupancyReportPnl, dailyOccupancyReportTbl, GridBagConstraints.BOTH, 0, 3);

		// Back and logout buttons
		backToReportsBtn.addActionListener(new ReportsUIListener());
		backBtn.addActionListener(new ReportsUIListener());
		logoutBtn.addActionListener(new ReportsUIListener());

		// Main Content Panel
		adminMainUI.addGridBagComponent(mainContentPnl, reportTypesPnl, GridBagConstraints.BOTH, 0, 0, 2, 1);
		// add other panels of particular reports
		adminMainUI.addGridBagComponent(mainContentPnl, mostLeastUsedHourReportPnl, GridBagConstraints.BOTH, 0, 0, 2, 1);
		adminMainUI.addGridBagComponent(mainContentPnl, maxRevenueReportPnl, GridBagConstraints.BOTH, 0, 0, 2, 1);
		adminMainUI.addGridBagComponent(mainContentPnl, hourlyRevenueReportPnl, GridBagConstraints.BOTH, 0, 0, 2, 1);
		adminMainUI.addGridBagComponent(mainContentPnl, dailyRevenueReportPnl, GridBagConstraints.BOTH, 0, 0, 2, 1);
		adminMainUI.addGridBagComponent(mainContentPnl, monthlyRevenueReportPnl, GridBagConstraints.BOTH, 0, 0, 2, 1);
		adminMainUI.addGridBagComponent(mainContentPnl, dailyOccupancyReportPnl, GridBagConstraints.BOTH, 0, 0, 2, 1);

		adminMainUI.addGridBagComponent(mainContentPnl, backToReportsBtn, GridBagConstraints.BOTH, 0, 1);
		adminMainUI.addGridBagComponent(mainContentPnl, backBtn, GridBagConstraints.BOTH, 0, 1);
		adminMainUI.addGridBagComponent(mainContentPnl, logoutBtn, GridBagConstraints.BOTH, 1, 1);

		reportTypesPnl.setVisible(true);
		// hide other report panels
		mostLeastUsedHourReportPnl.setVisible(false);
		maxRevenueReportPnl.setVisible(false);
		hourlyRevenueReportPnl.setVisible(false);
		dailyRevenueReportPnl.setVisible(false);
		monthlyRevenueReportPnl.setVisible(false);
		dailyOccupancyReportPnl.setVisible(false);

		backToReportsBtn.setVisible(false);

		adminMainUI.addGridBagComponent(adminMainUI.mainPnl, mainContentPnl, GridBagConstraints.BOTH, 0, 0);
	}

	private void resetUI() {
		reportTypesPnl.setVisible(true);
		mostLeastUsedHourReportPnl.setVisible(false);
		maxRevenueReportPnl.setVisible(false);
		hourlyReportGetDayTxt.setText("");
		hourlyRevenueReportPnl.setVisible(false);
		dailyReportGetMonthTxt.setText("");
		dailyRevenueReportPnl.setVisible(false);
		monthlyReportGetYearTxt.setText("");
		monthlyRevenueReportPnl.setVisible(false);
		dailyOccupancyReportPnl.setVisible(false);
		backBtn.setVisible(true);
		backToReportsBtn.setVisible(false);
	}

	private class ReportsUIListener implements ActionListener {
		private ReportsUIListener() {
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("Get Hourly Revenue Report for a particular day")) {
				getHourlyRevenueReport();
			}
			if (e.getActionCommand().equals("Get Daily Revenue Report for a particular month")) {
				getDailyRevenueReport();
			}
			if (e.getActionCommand().equals("Get Monthly Revenue Report for a particular year")) {
				getMonthlyRevenueReport();
			}
			if (e.getActionCommand().equals("Get most and least used hours in last month")) {
				getMostLeastUsedHoursInLastMonth();
			}
			if (e.getActionCommand().equals("Get maximum revenue day in last month")) {
				getMaxRevenueDayInLastMonth();
			}
			if (e.getActionCommand().equals("Get daily occupancy counts for last month")) {
				getDailyOccupancyCountsForLastMonth();
			}
			if (e.getActionCommand().equals("Go Back to Reports")) {
				resetUI();
			}
			if (e.getActionCommand().equals("Go Back")) {
				resetUI();
				adminMainUI.showHideContentPanel(adminMainUI.adminUI.mainContentPnl, mainContentPnl);
			}
			if (e.getActionCommand().equals("Logout")) {
				logout();
			}
		}

		private void logout() {
			try {
				JOptionPane.showMessageDialog(adminMainUI, "Logging out...", "Logout", JOptionPane.INFORMATION_MESSAGE);
				parking.logout();
				resetUI();
				adminMainUI.showHideContentPanel(adminMainUI.mainContentPnl, mainContentPnl);
				adminMainUI.updateWelcomeMessage();
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}

		private void getDailyOccupancyCountsForLastMonth() {
			boolean isReportPopulated = adminMainUI.reportsUI.populateDailyOccupancyReport();
			if (isReportPopulated) {
				reportTypesPnl.setVisible(false);
				dailyOccupancyReportPnl.setVisible(true);
				backBtn.setVisible(false);
				backToReportsBtn.setVisible(true);
			}
		}

		private void getMaxRevenueDayInLastMonth() {
			boolean isReportPopulated = adminMainUI.reportsUI.populateMaxRevenueDayReport();
			if (isReportPopulated) {
				reportTypesPnl.setVisible(false);
				maxRevenueReportPnl.setVisible(true);
				backBtn.setVisible(false);
				backToReportsBtn.setVisible(true);
			}
		}

		private void getMostLeastUsedHoursInLastMonth() {
			boolean isReportPopulated = adminMainUI.reportsUI.populateMostLeastUsedHourReport();
			if (isReportPopulated) {
				reportTypesPnl.setVisible(false);
				mostLeastUsedHourReportPnl.setVisible(true);
				backBtn.setVisible(false);
				backToReportsBtn.setVisible(true);
			}
		}

		private void getMonthlyRevenueReport() {
			// display the JOptionPane showConfirmDialog
			int reply = JOptionPane.showConfirmDialog(adminMainUI, monthlyReportGetYearPnl, "Monthly Revenue Report", JOptionPane.OK_CANCEL_OPTION);
			if (reply == JOptionPane.OK_OPTION) {
				yearStr = monthlyReportGetYearTxt.getText();
				if (yearStr.matches("[0-9]{4}")) {
					boolean isReportPopulated = populateMonthlyRevenueReport(yearStr);
					if (isReportPopulated) {
						monthlyRevenueReportPnl.setVisible(true);
						reportTypesPnl.setVisible(false);
						backBtn.setVisible(false);
						backToReportsBtn.setVisible(true);
					} else {
						JOptionPane
								.showMessageDialog(adminMainUI, "Enter valid year in the past in format yyyy.", "Error", JOptionPane.ERROR_MESSAGE);
						monthlyReportGetYearTxt.setText("");
					}
				} else {
					JOptionPane.showMessageDialog(adminMainUI, "Enter year in format yyyy.", "Error", JOptionPane.ERROR_MESSAGE);
					monthlyReportGetYearTxt.setText("");
				}
			}
		}

		private void getHourlyRevenueReport() {
			// display the JOptionPane showConfirmDialog
			int reply = JOptionPane.showConfirmDialog(adminMainUI, hourlyReportGetDayPnl, "Hourly Revenue Report", JOptionPane.OK_CANCEL_OPTION);
			if (reply == JOptionPane.OK_OPTION) {
				dayMonthYearStr = hourlyReportGetDayTxt.getText();
				if (dayMonthYearStr.matches("[0-9]{2}-[0-9]{2}-[0-9]{4}")) {
					boolean isReportPopulated = populateHourlyRevenueReport(dayMonthYearStr);
					if (isReportPopulated) {
						hourlyRevenueReportPnl.setVisible(true);
						reportTypesPnl.setVisible(false);
						backBtn.setVisible(false);
						backToReportsBtn.setVisible(true);
					} else {
						JOptionPane.showMessageDialog(adminMainUI, "Enter valid day, month, year in the past in format mm-dd-yyyy.", "Error",
								JOptionPane.ERROR_MESSAGE);
						hourlyReportGetDayTxt.setText("");
					}
				} else {
					JOptionPane.showMessageDialog(adminMainUI, "Enter day in format mm-dd-yyyy.", "Error", JOptionPane.ERROR_MESSAGE);
					hourlyReportGetDayTxt.setText("");
				}
			}
		}

		private void getDailyRevenueReport() {
			// display the JOptionPane showConfirmDialog
			int reply = JOptionPane.showConfirmDialog(adminMainUI, dailyReportGetMonthPnl, "Daily Revenue Report", JOptionPane.OK_CANCEL_OPTION);
			if (reply == JOptionPane.OK_OPTION) {
				monthYearStr = dailyReportGetMonthTxt.getText();
				if (monthYearStr.matches("[0-9]{2}-[0-9]{4}")) {
					boolean isReportPopulated = populateDailyRevenueReport(monthYearStr);
					if (isReportPopulated) {
						dailyRevenueReportPnl.setVisible(true);
						reportTypesPnl.setVisible(false);
						backBtn.setVisible(false);
						backToReportsBtn.setVisible(true);
					} else {
						JOptionPane.showMessageDialog(adminMainUI, "Enter valid month in the past in format mm-yyyy.", "Error",
								JOptionPane.ERROR_MESSAGE);
						dailyReportGetMonthTxt.setText("");
					}
				} else {
					JOptionPane.showMessageDialog(adminMainUI, "Enter month in format mm-yyyy.", "Error", JOptionPane.ERROR_MESSAGE);
					dailyReportGetMonthTxt.setText("");
				}
			}
		}
	}

	public boolean populateMostLeastUsedHourReport() {
		// Populate most and least used hours in last month report
		try {
			mostLeastUsedHourModel.setRowCount(0);
			ReportUnit mostUsedHourInLastMonth = parking.getMostUsedHourInLastMonth();
			ReportUnit leastUsedHourInLastMonth = parking.getLeastUsedHourInLastMonth();
			ReportUnit lastMonth = parking.getLastMonth();
			lastMonth1Lbl.setText("Last month: " + new DateFormatSymbols().getMonths()[lastMonth.getMonth() - 1] + " " + lastMonth.getYear());
			if (!mostUsedHourInLastMonth.equals(null)) {
				mostLeastUsedHourModel.addRow(new Object[] { "Most used hour", String.valueOf(mostUsedHourInLastMonth.getDay()),
						String.valueOf(mostUsedHourInLastMonth.getHour()), String.valueOf(mostUsedHourInLastMonth.getOccupancyPercent()) });
			} else {
				mostLeastUsedHourModel.addRow(new Object[] { "Most used hour", "NA", "NA", "NA" });
			}
			if (!leastUsedHourInLastMonth.equals(null)) {
				mostLeastUsedHourModel.addRow(new Object[] { "Least used hour", String.valueOf(leastUsedHourInLastMonth.getDay()),
						String.valueOf(leastUsedHourInLastMonth.getHour()), String.valueOf(leastUsedHourInLastMonth.getOccupancyPercent()) });
			} else {
				mostLeastUsedHourModel.addRow(new Object[] { "Least used hour", "NA", "NA", "NA" });
			}
			if (!mostUsedHourInLastMonth.equals(null) || !leastUsedHourInLastMonth.equals(null)) {
				return true;
			}
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
		return false;
	}

	public boolean populateDailyOccupancyReport() {
		// Populate daily occupancy report of last month
		try {
			List<ReportUnit> dailyOccupancyList = parking.getDailyOccupancyForLastMonth();
			ReportUnit lastMonth = parking.getLastMonth();
			lastMonth3Lbl.setText("Last month: " + new DateFormatSymbols().getMonths()[lastMonth.getMonth() - 1] + " " + lastMonth.getYear());

			dailyOccupancyReportModel.setRowCount(0);
			if (!dailyOccupancyList.equals(null)) {
				for (ReportUnit day : dailyOccupancyList) {
					dailyOccupancyReportModel.addRow(new Object[] { String.valueOf(day.getDay()), String.valueOf(day.getTicketCount()) });
				}
			} else {
				return false;
			}
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
		return true;
	}

	public boolean populateMaxRevenueDayReport() {
		// Maximum revenue day in last month report
		try {
			maxRevenueModel.setRowCount(0);
			ReportUnit maxRevenueDayInLastMonth = parking.getMaxRevenueDayInLastMonth();
			ReportUnit lastMonth = parking.getLastMonth();
			lastMonth2Lbl.setText("Last month: " + new DateFormatSymbols().getMonths()[lastMonth.getMonth() - 1] + " " + lastMonth.getYear());
			if (!maxRevenueDayInLastMonth.equals(null)) {
				maxRevenueModel.addRow(new Object[] { maxRevenueDayInLastMonth.getDay(), maxRevenueDayInLastMonth.getPayment() });
				return true;
			}
			maxRevenueModel.addRow(new Object[] { "NA", "NA" });
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
		return false;
	}

	public boolean populateHourlyRevenueReport(String dayMonthYearStr) {
		// Hourly Revenue Report
		try {
			boolean isValidDay = parking.isValidDayMonthYearNotInFuture(dayMonthYearStr);
			if (isValidDay) {
				ReportUnit dayMonthYear = new ReportUnit();
				dayMonthYear.setMonth(Integer.parseInt(dayMonthYearStr.substring(0, 2)));
				dayMonthYear.setDay(Integer.parseInt(dayMonthYearStr.substring(3, 5)));
				dayMonthYear.setYear(Integer.parseInt(dayMonthYearStr.substring(6)));
				List<ReportUnit> hourlyRevenueList = parking.getHourlyRevenueForDayMonthYear(dayMonthYear);

				hourlyRevenueReportModel.setRowCount(0);
				hourlyRevenueReportDayLbl.setText("Hourly Revenue for: " + dayMonthYear.getDay() + " "
						+ new DateFormatSymbols().getMonths()[dayMonthYear.getMonth() - 1] + " " + dayMonthYear.getYear());

				if (!hourlyRevenueList.equals(null)) {
					for (ReportUnit hour : hourlyRevenueList) {
						hourlyRevenueReportModel.addRow(new Object[] { String.valueOf(hour.getHour()), String.valueOf(hour.getPayment()) });
					}
				} else {
					return false;
				}
			}
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
		return true;
	}

	public void updateHourlyRevenueReport() {
		if(!dayMonthYearStr.equals("")) {
			populateHourlyRevenueReport(dayMonthYearStr);
		}
	}

	public boolean populateDailyRevenueReport(String monthYearStr) {
		// Populate Daily Revenue Report
		try {
			boolean isValidMonth = parking.isValidMonthYearNotInFuture(monthYearStr);
			if (isValidMonth) {
				ReportUnit monthYear = new ReportUnit();
				monthYear.setMonth(Integer.parseInt(monthYearStr.substring(0, 2)));
				monthYear.setYear(Integer.parseInt(monthYearStr.substring(3)));
				List<ReportUnit> dailyRevenueList = parking.getDailyRevenueForMonthYear(monthYear);

				dailyRevenueReportModel.setRowCount(0);
				dailyRevenueReportMonthLbl.setText("Daily Revenue for: " + new DateFormatSymbols().getMonths()[monthYear.getMonth() - 1] + " "
						+ monthYear.getYear());

				if (!dailyRevenueList.equals(null)) {
					for (ReportUnit day : dailyRevenueList) {
						dailyRevenueReportModel.addRow(new Object[] { String.valueOf(day.getDay()), String.valueOf(day.getPayment()) });
					}
				} else {
					return false;
				}
			}
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
		return true;
	}

	public void updateDailyRevenueReport() {
		if(!monthYearStr.equals("")) {
			populateDailyRevenueReport(monthYearStr);
		}
	}

	public boolean populateMonthlyRevenueReport(String yearStr) {
		// Populate Monthly Revenue Report
		try {
			boolean isValidYear = parking.isValidYearNotInFuture(yearStr);
			if (isValidYear) {
				ReportUnit year = new ReportUnit();
				year.setYear(Integer.parseInt(yearStr));
				List<ReportUnit> monthlyRevenueList = parking.getMonthlyRevenueForYear(year);

				monthlyRevenueReportModel.setRowCount(0);
				monthlyRevenueReportYearLbl.setText("Monthly Revenue for: " + year.getYear());

				if (!monthlyRevenueList.equals(null)) {
					for (ReportUnit month : monthlyRevenueList) {
						monthlyRevenueReportModel.addRow(new Object[] { String.valueOf(month.getMonth()), String.valueOf(month.getPayment()) });
					}
				} else {
					return false;
				}
			}
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
		return true;
	}

	public void updateMonthlyRevenueReport() {
		if(!yearStr.equals("")) {
			populateMonthlyRevenueReport(yearStr);
		}
	}
}