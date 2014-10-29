/**
 * 
 */
package cs414.fmaster.parking.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import cs414.fmaster.parking.controller.ParkingTime;
import cs414.fmaster.parking.controller.MainController;
import cs414.fmaster.parking.controller.ParkingRate;

/**
 * @author MasterF
 * 
 */
public class ReportsUI {

	private MainUI mainUI;
	private MainController mainController;
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

	private JPanel dailyReportGetMonthPnl = new JPanel();
	private JTextField dailyReportGetMonthTxt = new JTextField(5);
	private JPanel dailyRevenueReportPnl = new JPanel(new GridBagLayout());
	private JLabel dailyRevenueReportMonthLbl = new JLabel();
	private JTable dailyRevenueReportTbl = new JTable();
	private DefaultTableModel dailyRevenueReportModel = new DefaultTableModel(new Object[][] {}, new String[] { "Day", "Revenue" });

	private JPanel monthlyReportGetYearPnl = new JPanel();
	private JTextField monthlyReportGetYearTxt = new JTextField(5);
	private JPanel monthlyRevenueReportPnl = new JPanel(new GridBagLayout());
	private JLabel monthlyRevenueReportYearLbl = new JLabel();
	private JTable monthlyRevenueReportTbl = new JTable();
	private DefaultTableModel monthlyRevenueReportModel = new DefaultTableModel(new Object[][] {}, new String[] { "Month", "Revenue" });

	private JPanel dailyOccupancyReportPnl = new JPanel(new GridBagLayout());
	private JLabel lastMonth3Lbl = new JLabel();
	private JTable dailyOccupancyReportTbl = new JTable();
	private DefaultTableModel dailyOccupancyReportModel = new DefaultTableModel(new Object[][] {}, new String[] { "Day", "No. of tickets issued" });

	private JButton backToReportsBtn = new JButton("Go Back to Reports");
	private JButton backBtn = new JButton("Go Back");
	private JButton logoutBtn = new JButton("Logout");

	private static ReportsUI instance = null;

	private ReportsUI(MainUI mainUI, MainController mainController) {
		this.mainUI = mainUI;
		this.mainController = mainController;
	}

	public static ReportsUI getInstance(MainUI mainUI, MainController mainController) {
		if (instance == null) {
			instance = new ReportsUI(mainUI, mainController);
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

		mainUI.addGridBagComponent(reportTypesPnl, hourlyReportBtn, GridBagConstraints.BOTH, 0, 0, 2, 1);
		mainUI.addGridBagComponent(reportTypesPnl, dailyReportBtn, GridBagConstraints.BOTH, 0, 1, 2, 1);
		mainUI.addGridBagComponent(reportTypesPnl, monthlyReportBtn, GridBagConstraints.BOTH, 0, 2, 2, 1);
		mainUI.addGridBagComponent(reportTypesPnl, mostLeastReportBtn, GridBagConstraints.BOTH, 0, 3, 2, 1);
		mainUI.addGridBagComponent(reportTypesPnl, maxRevenueReportBtn, GridBagConstraints.BOTH, 0, 4, 2, 1);
		mainUI.addGridBagComponent(reportTypesPnl, dailyOccupancyReportBtn, GridBagConstraints.BOTH, 0, 5, 2, 1);

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

		mainUI.addGridBagComponent(mostLeastUsedHourReportPnl, new JLabel("Hours with most and least occupancy in last month"),
				GridBagConstraints.BOTH, 0, 0);
		mainUI.addGridBagComponent(mostLeastUsedHourReportPnl, lastMonth1Lbl, GridBagConstraints.BOTH, 0, 1);
		mainUI.addGridBagComponent(mostLeastUsedHourReportPnl, mostLeastUsedHourTbl.getTableHeader(), GridBagConstraints.BOTH, 0, 2);
		mainUI.addGridBagComponent(mostLeastUsedHourReportPnl, mostLeastUsedHourTbl, GridBagConstraints.BOTH, 0, 3);

		// Maximum Revenue day in last Month Report Panel
		maxRevenueTbl.setModel(maxRevenueModel);
		maxRevenueTbl.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		maxRevenueTbl.setFillsViewportHeight(true);
		maxRevenueTbl.setEnabled(false);

		mainUI.addGridBagComponent(maxRevenueReportPnl, new JLabel("Day with maximum revenue in last month"), GridBagConstraints.BOTH, 0, 0);
		mainUI.addGridBagComponent(maxRevenueReportPnl, lastMonth2Lbl, GridBagConstraints.BOTH, 0, 1);
		mainUI.addGridBagComponent(maxRevenueReportPnl, maxRevenueTbl.getTableHeader(), GridBagConstraints.BOTH, 0, 2);
		mainUI.addGridBagComponent(maxRevenueReportPnl, maxRevenueTbl, GridBagConstraints.BOTH, 0, 3);

		// Hourly Revenue Report of particular month Report
		hourlyReportGetDayPnl.add(new JLabel("Enter mm-dd-yyyy: "));
		hourlyReportGetDayPnl.add(hourlyReportGetDayTxt);

		hourlyRevenueReportTbl.setModel(hourlyRevenueReportModel);
		hourlyRevenueReportTbl.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		hourlyRevenueReportTbl.setFillsViewportHeight(true);
		hourlyRevenueReportTbl.setEnabled(false);

		mainUI.addGridBagComponent(hourlyRevenueReportPnl, hourlyRevenueReportDayLbl, GridBagConstraints.BOTH, 0, 0);
		mainUI.addGridBagComponent(hourlyRevenueReportPnl, hourlyRevenueReportTbl.getTableHeader(), GridBagConstraints.BOTH, 0, 1);
		mainUI.addGridBagComponent(hourlyRevenueReportPnl, hourlyRevenueReportTbl, GridBagConstraints.BOTH, 0, 2);

		// Daily Revenue Report of particular month Report
		dailyReportGetMonthPnl.add(new JLabel("Enter mm-yyyy: "));
		dailyReportGetMonthPnl.add(dailyReportGetMonthTxt);

		dailyRevenueReportTbl.setModel(dailyRevenueReportModel);
		dailyRevenueReportTbl.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		dailyRevenueReportTbl.setFillsViewportHeight(true);
		dailyRevenueReportTbl.setEnabled(false);

		mainUI.addGridBagComponent(dailyRevenueReportPnl, dailyRevenueReportMonthLbl, GridBagConstraints.BOTH, 0, 0);
		mainUI.addGridBagComponent(dailyRevenueReportPnl, dailyRevenueReportTbl.getTableHeader(), GridBagConstraints.BOTH, 0, 1);
		mainUI.addGridBagComponent(dailyRevenueReportPnl, dailyRevenueReportTbl, GridBagConstraints.BOTH, 0, 2);

		// Monthly Revenue Report of particular year Report
		monthlyReportGetYearPnl.add(new JLabel("Enter yyyy: "));
		monthlyReportGetYearPnl.add(monthlyReportGetYearTxt);

		monthlyRevenueReportTbl.setModel(monthlyRevenueReportModel);
		monthlyRevenueReportTbl.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		monthlyRevenueReportTbl.setFillsViewportHeight(true);
		monthlyRevenueReportTbl.setEnabled(false);

		mainUI.addGridBagComponent(monthlyRevenueReportPnl, monthlyRevenueReportYearLbl, GridBagConstraints.BOTH, 0, 0);
		mainUI.addGridBagComponent(monthlyRevenueReportPnl, monthlyRevenueReportTbl.getTableHeader(), GridBagConstraints.BOTH, 0, 1);
		mainUI.addGridBagComponent(monthlyRevenueReportPnl, monthlyRevenueReportTbl, GridBagConstraints.BOTH, 0, 2);

		// Daily Occupancy Report of last month
		dailyOccupancyReportTbl.setModel(dailyOccupancyReportModel);
		dailyOccupancyReportTbl.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		dailyOccupancyReportTbl.setFillsViewportHeight(true);
		dailyOccupancyReportTbl.setEnabled(false);

		mainUI.addGridBagComponent(dailyOccupancyReportPnl, new JLabel("Daily Occupancy Report of last month"), GridBagConstraints.BOTH, 0, 0);
		mainUI.addGridBagComponent(dailyOccupancyReportPnl, lastMonth3Lbl, GridBagConstraints.BOTH, 0, 1);
		mainUI.addGridBagComponent(dailyOccupancyReportPnl, dailyOccupancyReportTbl.getTableHeader(), GridBagConstraints.BOTH, 0, 2);
		mainUI.addGridBagComponent(dailyOccupancyReportPnl, dailyOccupancyReportTbl, GridBagConstraints.BOTH, 0, 3);

		// Back and logout buttons
		backToReportsBtn.addActionListener(new ReportsUIListener());
		backBtn.addActionListener(new ReportsUIListener());
		logoutBtn.addActionListener(new ReportsUIListener());

		// Main Content Panel
		mainUI.addGridBagComponent(mainContentPnl, reportTypesPnl, GridBagConstraints.BOTH, 0, 0, 2, 1);
		// add other panels of particular reports
		mainUI.addGridBagComponent(mainContentPnl, mostLeastUsedHourReportPnl, GridBagConstraints.BOTH, 0, 0, 2, 1);
		mainUI.addGridBagComponent(mainContentPnl, maxRevenueReportPnl, GridBagConstraints.BOTH, 0, 0, 2, 1);
		mainUI.addGridBagComponent(mainContentPnl, hourlyRevenueReportPnl, GridBagConstraints.BOTH, 0, 0, 2, 1);
		mainUI.addGridBagComponent(mainContentPnl, dailyRevenueReportPnl, GridBagConstraints.BOTH, 0, 0, 2, 1);
		mainUI.addGridBagComponent(mainContentPnl, monthlyRevenueReportPnl, GridBagConstraints.BOTH, 0, 0, 2, 1);
		mainUI.addGridBagComponent(mainContentPnl, dailyOccupancyReportPnl, GridBagConstraints.BOTH, 0, 0, 2, 1);

		mainUI.addGridBagComponent(mainContentPnl, backToReportsBtn, GridBagConstraints.BOTH, 0, 1);
		mainUI.addGridBagComponent(mainContentPnl, backBtn, GridBagConstraints.BOTH, 0, 1);
		mainUI.addGridBagComponent(mainContentPnl, logoutBtn, GridBagConstraints.BOTH, 1, 1);

		reportTypesPnl.setVisible(true);
		// hide other report panels
		mostLeastUsedHourReportPnl.setVisible(false);
		maxRevenueReportPnl.setVisible(false);
		hourlyRevenueReportPnl.setVisible(false);
		dailyRevenueReportPnl.setVisible(false);
		monthlyRevenueReportPnl.setVisible(false);
		dailyOccupancyReportPnl.setVisible(false);

		backToReportsBtn.setVisible(false);

		mainUI.addGridBagComponent(mainUI.mainPnl, mainContentPnl, GridBagConstraints.BOTH, 0, 0);
	}

	public void resetUI() {
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
		ReportsUIListener() {
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("Get Hourly Revenue Report for a particular day")) {
				// display the JOptionPane showConfirmDialog
				int reply = JOptionPane.showConfirmDialog(mainUI, hourlyReportGetDayPnl, "Hourly Revenue Report", JOptionPane.OK_CANCEL_OPTION);
				if (reply == JOptionPane.OK_OPTION) {
					String dayMonthYearStr = hourlyReportGetDayTxt.getText();
					if (dayMonthYearStr.matches("[0-9]{2}-[0-9]{2}-[0-9]{4}")) {
						boolean isReportPopulated = populateHourlyRevenueReport(dayMonthYearStr);
						if (isReportPopulated) {
							hourlyRevenueReportPnl.setVisible(true);
							reportTypesPnl.setVisible(false);
							backBtn.setVisible(false);
							backToReportsBtn.setVisible(true);
						} else {
							JOptionPane.showMessageDialog(mainUI, "Enter valid day, month, year in the past in format mm-dd-yyyy.", "Error",
									JOptionPane.ERROR_MESSAGE);
							hourlyReportGetDayTxt.setText("");
						}
					} else {
						JOptionPane.showMessageDialog(mainUI, "Enter day in format mm-dd-yyyy.", "Error", JOptionPane.ERROR_MESSAGE);
						hourlyReportGetDayTxt.setText("");
					}
				}

			}
			if (e.getActionCommand().equals("Get Daily Revenue Report for a particular month")) {
				// display the JOptionPane showConfirmDialog
				int reply = JOptionPane.showConfirmDialog(mainUI, dailyReportGetMonthPnl, "Daily Revenue Report", JOptionPane.OK_CANCEL_OPTION);
				if (reply == JOptionPane.OK_OPTION) {
					String monthYearStr = dailyReportGetMonthTxt.getText();
					if (monthYearStr.matches("[0-9]{2}-[0-9]{4}")) {
						boolean isReportPopulated = populateDailyRevenueReport(monthYearStr);
						if (isReportPopulated) {
							dailyRevenueReportPnl.setVisible(true);
							reportTypesPnl.setVisible(false);
							backBtn.setVisible(false);
							backToReportsBtn.setVisible(true);
						} else {
							JOptionPane.showMessageDialog(mainUI, "Enter valid month in the past in format mm-yyyy.", "Error",
									JOptionPane.ERROR_MESSAGE);
							dailyReportGetMonthTxt.setText("");
						}
					} else {
						JOptionPane.showMessageDialog(mainUI, "Enter month in format mm-yyyy.", "Error", JOptionPane.ERROR_MESSAGE);
						dailyReportGetMonthTxt.setText("");
					}
				}
			}
			if (e.getActionCommand().equals("Get Monthly Revenue Report for a particular year")) {
				// display the JOptionPane showConfirmDialog
				int reply = JOptionPane.showConfirmDialog(mainUI, monthlyReportGetYearPnl, "Monthly Revenue Report", JOptionPane.OK_CANCEL_OPTION);
				if (reply == JOptionPane.OK_OPTION) {
					String yearStr = monthlyReportGetYearTxt.getText();
					if (yearStr.matches("[0-9]{4}")) {
						boolean isReportPopulated = populateMonthlyRevenueReport(yearStr);
						if (isReportPopulated) {
							monthlyRevenueReportPnl.setVisible(true);
							reportTypesPnl.setVisible(false);
							backBtn.setVisible(false);
							backToReportsBtn.setVisible(true);
						} else {
							JOptionPane.showMessageDialog(mainUI, "Enter valid year in the past in format yyyy.", "Error", JOptionPane.ERROR_MESSAGE);
							monthlyReportGetYearTxt.setText("");
						}
					} else {
						JOptionPane.showMessageDialog(mainUI, "Enter year in format yyyy.", "Error", JOptionPane.ERROR_MESSAGE);
						monthlyReportGetYearTxt.setText("");
					}
				}
			}
			if (e.getActionCommand().equals("Get most and least used hours in last month")) {
				boolean isReportPopulated = mainUI.reportsUI.populateMostLeastUsedHourReport();
				if (isReportPopulated) {
					reportTypesPnl.setVisible(false);
					mostLeastUsedHourReportPnl.setVisible(true);
					backBtn.setVisible(false);
					backToReportsBtn.setVisible(true);
				}
			}
			if (e.getActionCommand().equals("Get maximum revenue day in last month")) {
				boolean isReportPopulated = mainUI.reportsUI.populateMaxRevenueDayReport();
				if (isReportPopulated) {
					reportTypesPnl.setVisible(false);
					maxRevenueReportPnl.setVisible(true);
					backBtn.setVisible(false);
					backToReportsBtn.setVisible(true);
				}
			}
			if (e.getActionCommand().equals("Get daily occupancy counts for last month")) {
				boolean isReportPopulated = mainUI.reportsUI.populateDailyOccupancyReport();
				if (isReportPopulated) {
					reportTypesPnl.setVisible(false);
					dailyOccupancyReportPnl.setVisible(true);
					backBtn.setVisible(false);
					backToReportsBtn.setVisible(true);
				}
			}
			if (e.getActionCommand().equals("Go Back to Reports")) {
				resetUI();
			}
			if (e.getActionCommand().equals("Go Back")) {
				resetUI();
				mainUI.showHideContentPanel(mainUI.adminUI.mainContentPnl, mainContentPnl);
			}
			if (e.getActionCommand().equals("Logout")) {
				JOptionPane.showMessageDialog(mainUI, "Logging out...", "Logout", JOptionPane.INFORMATION_MESSAGE);
				mainController.adminOpsHandler.logout();
				resetUI();
				mainUI.showHideContentPanel(mainUI.mainContentPnl, mainContentPnl);
				mainUI.displayWelcomeMessage();
			}
		}
	}

	public boolean populateMostLeastUsedHourReport() {
		// Populate most and least used hours in last month report
		mostLeastUsedHourModel.setRowCount(0);
		ParkingTime mostUsedHourInLastMonth = mainController.reportsHandler.getMostUsedHourInLastMonth();
		ParkingTime leastUsedHourInLastMonth = mainController.reportsHandler.getLeastUsedHourInLastMonth();
		ParkingTime lastMonth = mainController.reportsHandler.getLastMonth();
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
		return false;
	}

	private boolean populateDailyOccupancyReport() {
		// Populate daily occupancy report of last month
		List<ParkingTime> dailyOccupancyList = mainController.reportsHandler.getDailyOccupancyForLastMonth();
		ParkingTime lastMonth = mainController.reportsHandler.getLastMonth();
		lastMonth3Lbl.setText("Last month: " + new DateFormatSymbols().getMonths()[lastMonth.getMonth() - 1] + " " + lastMonth.getYear());

		dailyOccupancyReportModel.setRowCount(0);
		if (!dailyOccupancyList.equals(null)) {
			for (ParkingTime day : dailyOccupancyList) {
				dailyOccupancyReportModel.addRow(new Object[] { String.valueOf(day.getDay()), String.valueOf(day.getTicketCount()) });
			}
		} else {
			return false;
		}

		return true;

	}

	public boolean populateMaxRevenueDayReport() {
		// Maximum revenue day in last month report
		maxRevenueModel.setRowCount(0);
		ParkingTime maxRevenueDayInLastMonth = mainController.reportsHandler.getMaxRevenueDayInLastMonth();
		ParkingTime lastMonth = mainController.reportsHandler.getLastMonth();
		lastMonth2Lbl.setText("Last month: " + new DateFormatSymbols().getMonths()[lastMonth.getMonth() - 1] + " " + lastMonth.getYear());
		if (!maxRevenueDayInLastMonth.equals(null)) {
			maxRevenueModel.addRow(new Object[] { maxRevenueDayInLastMonth.getDay(), maxRevenueDayInLastMonth.getPayment() });
			return true;
		}
		maxRevenueModel.addRow(new Object[] { "NA", "NA" });
		return false;
	}

	private boolean populateHourlyRevenueReport(String dayMonthYearStr) {
		// TODO Auto-generated method stub
		boolean isValidDay = mainController.paymentHandler.isValidDayMonthYearInPast(dayMonthYearStr);
		if (isValidDay) {
			ParkingTime dayMonthYear = new ParkingTime();
			dayMonthYear.setMonth(Integer.parseInt(dayMonthYearStr.substring(0, 2)));
			dayMonthYear.setDay(Integer.parseInt(dayMonthYearStr.substring(3, 5)));
			dayMonthYear.setYear(Integer.parseInt(dayMonthYearStr.substring(6)));
			List<ParkingTime> hourlyRevenueList = mainController.reportsHandler.getHourlyRevenueForDayMonthYear(dayMonthYear);

			hourlyRevenueReportModel.setRowCount(0);
			hourlyRevenueReportDayLbl.setText("Hourly Revenue for: " + dayMonthYear.getDay() + " "
					+ new DateFormatSymbols().getMonths()[dayMonthYear.getMonth() - 1] + " " + dayMonthYear.getYear());

			if (!hourlyRevenueList.equals(null)) {
				for (ParkingTime hour : hourlyRevenueList) {
					hourlyRevenueReportModel.addRow(new Object[] { String.valueOf(hour.getHour()), String.valueOf(hour.getPayment()) });
				}
			} else {
				return false;
			}

			return true;
		}
		return false;
	}

	private boolean populateDailyRevenueReport(String monthYearStr) {
		// TODO Auto-generated method stub
		boolean isValidMonth = mainController.paymentHandler.isValidMonthYearInPast(monthYearStr);
		if (isValidMonth) {
			ParkingTime monthYear = new ParkingTime();
			monthYear.setMonth(Integer.parseInt(monthYearStr.substring(0, 2)));
			monthYear.setYear(Integer.parseInt(monthYearStr.substring(3)));
			List<ParkingTime> dailyRevenueList = mainController.reportsHandler.getDailyRevenueForMonthYear(monthYear);

			dailyRevenueReportModel.setRowCount(0);
			dailyRevenueReportMonthLbl.setText("Daily Revenue for: " + new DateFormatSymbols().getMonths()[monthYear.getMonth() - 1] + " "
					+ monthYear.getYear());

			if (!dailyRevenueList.equals(null)) {
				for (ParkingTime day : dailyRevenueList) {
					dailyRevenueReportModel.addRow(new Object[] { String.valueOf(day.getDay()), String.valueOf(day.getPayment()) });
				}
			} else {
				return false;
			}

			return true;
		}
		return false;
	}

	public boolean populateMonthlyRevenueReport(String yearStr) {
		// TODO Auto-generated method stub
		boolean isValidYear = mainController.paymentHandler.isValidYearInPast(yearStr);
		if (isValidYear) {
			ParkingTime year = new ParkingTime();
			year.setYear(Integer.parseInt(yearStr));
			List<ParkingTime> monthlyRevenueList = mainController.reportsHandler.getMonthlyRevenueForYear(year);

			monthlyRevenueReportModel.setRowCount(0);
			monthlyRevenueReportYearLbl.setText("Monthly Revenue for: " + year.getYear());

			if (!monthlyRevenueList.equals(null)) {
				for (ParkingTime month : monthlyRevenueList) {
					monthlyRevenueReportModel.addRow(new Object[] { String.valueOf(month.getMonth()), String.valueOf(month.getPayment()) });
				}
			} else {
				return false;
			}

			return true;
		}
		return false;
	}
}
