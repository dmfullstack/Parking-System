package cs414.a5.fmaster.main.java.server.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import cs414.a5.fmaster.main.java.server.domain.ParkingRate;
import cs414.a5.fmaster.main.java.server.domain.ReportUnit;

/**
 * This DB class contains methods to retrieve information from the database
 * 
 * @author masterf
 */

public class ParkingDatabaseAccess extends DatabaseAccess {
	private static ParkingDatabaseAccess instance = null;
	private static final String CREATE_PARKING_SIZE_HISTORY = "CREATE TABLE parking_size_history (size INTEGER NOT NULL, start_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, end_time TIMESTAMP NULL)";
	private static final String CREATE_PARKING_AVAILABILITY_HISTORY = "CREATE TABLE parking_availability_history (availability INTEGER NOT NULL, start_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, end_time TIMESTAMP NULL)";
	private static final String CREATE_PARKING_RATES = "CREATE TABLE parking_rates (time_interval DECIMAL(3,1) NOT NULL, rate DECIMAL(6,2) NOT NULL, PRIMARY KEY (time_interval))";
	private static final String CREATE_ACCOUNTS = "CREATE TABLE accounts (username VARCHAR(10), password VARCHAR(10), security_question VARCHAR(25), security_answer VARCHAR(10), is_logged_in BOOLEAN DEFAULT FALSE, is_active BOOLEAN DEFAULT TRUE, PRIMARY KEY (username))";
	private static final String CREATE_AUDIT = "CREATE TABLE audit (triggered_by VARCHAR(10), disabled_acc VARCHAR(10), triggered_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY (triggered_by) REFERENCES accounts(username), FOREIGN KEY (disabled_acc) REFERENCES accounts(username))";
	private static final String CREATE_TICKETS = "CREATE TABLE tickets (ticket_no INTEGER NOT NULL AUTO_INCREMENT, issue_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, is_submitted BOOLEAN DEFAULT FALSE, is_paid BOOLEAN DEFAULT FALSE, PRIMARY KEY (ticket_no))";
	private static final String CREATE_PAYMENTS = "CREATE TABLE payments (amount DECIMAL(6,2) NOT NULL, ticket_no INTEGER, paid_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY (ticket_no) REFERENCES tickets(ticket_no))";
	private static final String CREATE_PAYMENT_EXCEPTIONS = "CREATE TABLE payment_exceptions (name VARCHAR(20) NOT NULL, license VARCHAR(20) NOT NULL, amount DECIMAL(6,2) NOT NULL)";

	private static final String SELECT_PARKING_RATES = "SELECT time_interval AS Hours, rate as Rate FROM parking_rates";
	private static final String SELECT_MAXIMUM_RATE = "SELECT rate as Rate FROM parking_rates WHERE time_interval = 24";
	private static final String SELECT_CURRENT_PARKING_SIZE = "SELECT size FROM parking_size_history WHERE end_time IS NULL";
	private static final String SELECT_PARKING_SIZE_IN_HOUR = "SELECT size FROM parking_size_history WHERE start_time < ? AND (end_time > ? OR end_time IS NULL)";
	private static final String SELECT_CURRENT_PARKING_AVAILABILITY = "SELECT availability FROM parking_availability_history WHERE end_time IS NULL";
	private static final String SELECT_PARKING_AVAILABILITY_IN_HOUR = "SELECT availability FROM parking_availability_history WHERE start_time < ? AND (end_time > ? OR end_time IS NULL)";
	private static final String SELECT_COUNT_UNSUBMITTED_TICKET = "SELECT COUNT(*) c FROM tickets WHERE ticket_no = ? AND is_submitted = false";
	private static final String SELECT_TICKET_ISSUE_TIME = "SELECT issue_time FROM tickets WHERE ticket_no = ?";
	private static final String FIND_LAST_TICKET = "SELECT LAST_INSERT_ID() AS ticket_no";
	private static final String SELECT_TICKET_COUNT_IN_DAY = "SELECT COUNT(*) AS ticket_count FROM tickets WHERE issue_time >= ? AND issue_time < ?";
	private static final String SELECT_PAYMENTS_ON_TICKET = "SELECT SUM(amount) AS payment FROM payments WHERE ticket_no = ?";
	private static final String SELECT_PAYMENT_IN_MONTH = "SELECT SUM(amount) AS payment FROM payments WHERE paid_on >= ? AND paid_on < ?";
	private static final String SELECT_PAYMENT_IN_DAY = "SELECT SUM(amount) AS payment FROM payments WHERE paid_on >= ? AND paid_on < ?";
	private static final String SELECT_PAYMENT_IN_HOUR = "SELECT SUM(amount) AS payment FROM payments WHERE paid_on >= ? AND paid_on < ?";
	private static final String SELECT_COUNT_VALID_USERNAME = "SELECT COUNT(*) c FROM accounts WHERE username = ? AND is_active = true";
	private static final String SELECT_COUNT_USERNAME = "SELECT COUNT(*) c FROM accounts WHERE username = ?";
	private static final String SELECT_PASSWORD = "SELECT password FROM accounts WHERE username = ?";
	private static final String SELECT_SECURITY_QUESTION = "SELECT security_question FROM accounts WHERE username = ?";
	private static final String SELECT_SECURITY_ANSWER = "SELECT security_answer FROM accounts WHERE username = ?";
	private static final String SELECT_LOGGED_IN_ADMIN = "SELECT username FROM accounts WHERE is_logged_in = true";

	private static final String INSERT_PARKING_SIZE = "INSERT INTO parking_size_history(size) VALUES (?)";
	private static final String INSERT_PARKING_AVAILABILITY = "INSERT INTO parking_availability_history(availability) VALUES (?)";
	private static final String INSERT_PARKING_RATE = "INSERT INTO parking_rates VALUES (?, ?)";
	private static final String INSERT_ACCOUNT = "INSERT INTO accounts(username, password, security_question, security_answer) VALUES (?,?,?,?)";
	private static final String INSERT_AUDIT_RECORD = "INSERT INTO audit(triggered_by, disabled_acc) VALUES (?, ?)";
	private static final String INSERT_TICKET = "INSERT INTO tickets(issue_time) VALUES (?)";
	private static final String INSERT_PAYMENT = "INSERT INTO payments(amount, ticket_no) VALUES (?, ?)";
	private static final String INSERT_PAYMENT_WITHOUT_TICKET = "INSERT INTO payments(amount) VALUES (?)";
	private static final String INSERT_PAYMENT_EXCEPTION = "INSERT INTO payment_exceptions(name, license, amount) VALUES (?, ?, ?)";

	private static final String UPDATE_PARKING_SIZE_ENDTIME = "UPDATE parking_size_history SET end_time=? WHERE end_time IS NULL";
	private static final String UPDATE_PARKING_AVAILABILITY_ENDTIME = "UPDATE parking_availability_history SET end_time=? WHERE end_time IS NULL";
	private static final String UPDATE_PARKING_RATE = "UPDATE parking_rates SET rate = ? WHERE time_interval = ?";
	private static final String UPDATE_TICKET_PAID = "UPDATE tickets SET is_paid = true WHERE ticket_no = ?";
	private static final String UPDATE_TICKET_SUBMITTED = "UPDATE tickets SET is_submitted = true WHERE ticket_no = ?";
	private static final String UPDATE_ADMIN_LOGGED_IN = "UPDATE accounts SET is_logged_in = true WHERE username = ?";
	private static final String UPDATE_ADMIN_LOGGED_OUT = "UPDATE accounts SET is_logged_in = false WHERE is_logged_in = true";
	private static final String UPDATE_ADMIN_PASSWORD = "UPDATE accounts SET password = ? WHERE username = ?";
	private static final String UPDATE_DISABLE_ACCOUNT = "UPDATE accounts SET is_active = false WHERE username = ?";

	private static final String ALTER_TICKETS = "ALTER TABLE tickets AUTO_INCREMENT = 100000";

	private ParkingDatabaseAccess() {
		super();
		runScript("cs414/a5/fmaster/main/java/server/database/resources/cleanupDBScript.sql");
		initializeParkingDatabase();
	}

	public static ParkingDatabaseAccess getInstance() {
		if (instance == null) {
			instance = new ParkingDatabaseAccess();
		}
		return instance;
	}

	// Method to initialize database tables
	private void initializeParkingDatabase() {
		int initialSize = 5;
		createParkingSize();
		insertParkingSize(initialSize);

		createParkingAvailability();
		insertParkingAvailability(initialSize);

		createParkingRates();
		insertParkingRate(0.5, 20.5);
		insertParkingRate(1, 1);
		insertParkingRate(2, 2);
		insertParkingRate(10, 10);
		insertParkingRate(24, 24);

		createAccountTable();
		insertAccountDetails("admin", "admin", "admin", "admin");
		createAudit();

		createTickets();
		createPayments();
		createPaymentExceptions();
	}

	// Methods for Parking Size
	private void createParkingSize() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(CREATE_PARKING_SIZE_HISTORY);
			ps.executeUpdate();
		} catch (SQLException sqe) {
			throw new RuntimeException(sqe);
		} finally {
			closeResources(ps, rs);
		}
	}

	private void insertParkingSize(int parkingSize) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(INSERT_PARKING_SIZE);
			ps.setInt(1, parkingSize);
			ps.executeUpdate();
		} catch (SQLException sqe) {
			throw new RuntimeException(sqe);
		} finally {
			closeResources(ps, rs);
		}
	}

	public void configureParkingSize(int parkingSize) {
		updateCurrentParkingSizeEndTime();
		insertParkingSize(parkingSize);
	}

	private void updateCurrentParkingSizeEndTime() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(UPDATE_PARKING_SIZE_ENDTIME);
			Timestamp now = new Timestamp(new Date().getTime());
			ps.setTimestamp(1, now);
			ps.executeUpdate();
		} catch (SQLException sqe) {
			throw new RuntimeException(sqe);
		} finally {
			closeResources(ps, rs);
		}
	}

	public int getCurrentParkingSize() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(SELECT_CURRENT_PARKING_SIZE);
			rs = ps.executeQuery();
			int size = 0;
			if (rs.next() == true) {
				size = rs.getInt("size");
			}
			return size;
		} catch (SQLException sqe) {
			throw new RuntimeException(sqe);
		} finally {
			closeResources(ps, rs);
		}
	}

	public void setParkingSizeInHourInReportUnit(ReportUnit selectedHour) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(SELECT_PARKING_SIZE_IN_HOUR);
			Calendar aCal = new GregorianCalendar();
			aCal.set(Calendar.YEAR, selectedHour.getYear());
			aCal.set(Calendar.MONTH, selectedHour.getMonth() - 1);
			aCal.set(Calendar.DATE, selectedHour.getDay());
			aCal.set(Calendar.HOUR_OF_DAY, selectedHour.getHour());
			aCal.set(Calendar.MINUTE, 0);
			aCal.set(Calendar.SECOND, 0);
			Timestamp atimestamp = new Timestamp(aCal.getTimeInMillis());
			ps.setTimestamp(1, atimestamp);
			ps.setTimestamp(2, atimestamp);
			rs = ps.executeQuery();
			int size = 0;
			if (rs.next() == true) {
				size = rs.getInt("size");
			}
			selectedHour.setParkingSize(size);
		} catch (SQLException sqe) {
			throw new RuntimeException(sqe);
		} finally {
			closeResources(ps, rs);
		}
	}

	// Methods for Parking Availability
	private void createParkingAvailability() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(CREATE_PARKING_AVAILABILITY_HISTORY);
			ps.executeUpdate();
		} catch (SQLException sqe) {
			throw new RuntimeException(sqe);
		} finally {
			closeResources(ps, rs);
		}
	}

	private void insertParkingAvailability(int parkingAvailability) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(INSERT_PARKING_AVAILABILITY);
			ps.setInt(1, parkingAvailability);
			ps.executeUpdate();
		} catch (SQLException sqe) {
			throw new RuntimeException(sqe);
		} finally {
			closeResources(ps, rs);
		}
	}

	public int getCurrentParkingAvailability() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(SELECT_CURRENT_PARKING_AVAILABILITY);
			rs = ps.executeQuery();
			int availability = 0;
			if (rs.next() == true) {
				availability = rs.getInt("availability");
			}
			return availability;
		} catch (SQLException sqe) {
			throw new RuntimeException(sqe);
		} finally {
			closeResources(ps, rs);
		}
	}

	public void increaseParkingAvailability() {
		int avail = getCurrentParkingAvailability();
		updateCurrentParkingAvailabilityEndTime();
		insertParkingAvailability(avail + 1);
	}

	public void decreaseParkingAvailability() {
		int avail = getCurrentParkingAvailability();
		updateCurrentParkingAvailabilityEndTime();
		insertParkingAvailability(avail - 1);
	}

	public void updateParkingAvailability(int availability) {
		updateCurrentParkingAvailabilityEndTime();
		insertParkingAvailability(availability);
	}

	private void updateCurrentParkingAvailabilityEndTime() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(UPDATE_PARKING_AVAILABILITY_ENDTIME);
			Timestamp now = new Timestamp(new Date().getTime());
			ps.setTimestamp(1, now);
			ps.executeUpdate();
		} catch (SQLException sqe) {
			throw new RuntimeException(sqe);
		} finally {
			closeResources(ps, rs);
		}
	}

	public void setParkingAvailabilityInHourInReportUnit(ReportUnit selectedHour) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(SELECT_PARKING_AVAILABILITY_IN_HOUR);
			Calendar aCal = new GregorianCalendar();
			aCal.set(Calendar.YEAR, selectedHour.getYear());
			aCal.set(Calendar.MONTH, selectedHour.getMonth() - 1);
			aCal.set(Calendar.DATE, selectedHour.getDay());
			aCal.set(Calendar.HOUR_OF_DAY, selectedHour.getHour());
			aCal.set(Calendar.MINUTE, 0);
			aCal.set(Calendar.SECOND, 0);
			Timestamp atimestamp = new Timestamp(aCal.getTimeInMillis());
			ps.setTimestamp(1, atimestamp);
			ps.setTimestamp(2, atimestamp);
			rs = ps.executeQuery();
			int availability = 0;
			if (rs.next() == true) {
				availability = rs.getInt("availability");
			}
			selectedHour.setAvailability(availability);
		} catch (SQLException sqe) {
			throw new RuntimeException(sqe);
		} finally {
			closeResources(ps, rs);
		}
	}

	// Methods for Parking Rates
	private void createParkingRates() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(CREATE_PARKING_RATES);
			ps.executeUpdate();
		} catch (SQLException sqe) {
			throw new RuntimeException(sqe);
		} finally {
			closeResources(ps, rs);
		}
	}

	private void insertParkingRate(double timeInterval, double rate) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(INSERT_PARKING_RATE);
			ps.setDouble(1, timeInterval);
			ps.setDouble(2, rate);
			ps.executeUpdate();
		} catch (SQLException sqe) {
			throw new RuntimeException(sqe);
		} finally {
			closeResources(ps, rs);
		}
	}

	public List<ParkingRate> getParkingRates() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ParkingRate> parkingRates = new ArrayList<ParkingRate>();

		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(SELECT_PARKING_RATES);
			rs = ps.executeQuery();

			while (rs.next()) {
				ParkingRate pr = new ParkingRate();
				pr.setHours(rs.getDouble("Hours"));
				pr.setRate(rs.getDouble("Rate"));
				parkingRates.add(pr);
			}
		} catch (SQLException sqe) {
			throw new RuntimeException(sqe);
		} finally {
			closeResources(ps, rs);
		}
		return parkingRates;
	}

	public void updateParkingRates(List<ParkingRate> parkingRates) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseAccess.getConnection();
			for (ParkingRate pr : parkingRates) {
				ps = conn.prepareStatement(UPDATE_PARKING_RATE);
				ps.setDouble(1, pr.getRate());
				ps.setDouble(2, pr.getHours());
				ps.executeUpdate();
			}
		} catch (SQLException sqe) {
			throw new RuntimeException(sqe);
		} finally {
			closeResources(ps, rs);
		}
	}

	public double getMaximumRate() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		double maxRate = 0;

		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(SELECT_MAXIMUM_RATE);
			rs = ps.executeQuery();

			if (rs.next()) {
				maxRate = rs.getDouble("Rate");
			}
		} catch (SQLException sqe) {
			throw new RuntimeException(sqe);
		} finally {
			closeResources(ps, rs);
		}
		return maxRate;
	}

	// Methods for Accounts
	private void createAccountTable() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(CREATE_ACCOUNTS);
			ps.executeUpdate();
		} catch (SQLException sqe) {
			throw new RuntimeException(sqe);
		} finally {
			closeResources(ps, rs);
		}
	}

	public void insertAccountDetails(String username, String password, String securityQuestion, String securityAnswer) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(INSERT_ACCOUNT);
			ps.setString(1, username);
			ps.setString(2, password);
			ps.setString(3, securityQuestion);
			ps.setString(4, securityAnswer);
			ps.executeUpdate();
		} catch (SQLException sqe) {
			throw new RuntimeException(sqe);
		} finally {
			closeResources(ps, rs);
		}
	}

	public boolean isValidAccount(String username) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(SELECT_COUNT_VALID_USERNAME);
			ps.setString(1, username);
			int count = 0;
			rs = ps.executeQuery();
			if (rs.next() == true) {
				count = rs.getInt("c");
			}
			return count == 1;
		} catch (SQLException sqe) {
			throw new RuntimeException(sqe);
		} finally {
			closeResources(ps, rs);
		}
	}

	public boolean isAccountExist(String username) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(SELECT_COUNT_USERNAME);
			ps.setString(1, username);
			int count = 0;
			rs = ps.executeQuery();
			if (rs.next() == true) {
				count = rs.getInt("c");
			}
			return count == 1;
		} catch (SQLException sqe) {
			throw new RuntimeException(sqe);
		} finally {
			closeResources(ps, rs);
		}
	}

	public String getPassword(String username) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String password = null;
		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(SELECT_PASSWORD);
			ps.setString(1, username);
			rs = ps.executeQuery();
			if (rs.next()) {
				password = rs.getString("password");
			}
		} catch (SQLException sqe) {
			throw new RuntimeException(sqe);
		} finally {
			closeResources(ps, rs);
		}
		return password;
	}

	public String getSecurityQuestion(String username) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String securityQues = null;
		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(SELECT_SECURITY_QUESTION);
			ps.setString(1, username);
			rs = ps.executeQuery();
			if (rs.next()) {
				securityQues = rs.getString("security_question");
			}
		} catch (SQLException sqe) {
			throw new RuntimeException(sqe);
		} finally {
			closeResources(ps, rs);
		}
		return securityQues;
	}

	public String getSecurityAnswer(String username) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String securityAns = null;
		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(SELECT_SECURITY_ANSWER);
			ps.setString(1, username);
			rs = ps.executeQuery();
			if (rs.next()) {
				securityAns = rs.getString("security_answer");
			}
		} catch (SQLException sqe) {
			throw new RuntimeException(sqe);
		} finally {
			closeResources(ps, rs);
		}
		return securityAns;
	}

	public void setPassword(String username, String password) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(UPDATE_ADMIN_PASSWORD);
			ps.setString(1, password);
			ps.setString(2, username);
			ps.executeUpdate();
		} catch (SQLException sqe) {
			throw new RuntimeException(sqe);
		} finally {
			closeResources(ps, rs);
		}
	}

	public void setAdminLoggedIn(String username) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(UPDATE_ADMIN_LOGGED_IN);
			ps.setString(1, username);
			ps.executeUpdate();
		} catch (SQLException sqe) {
			throw new RuntimeException(sqe);
		} finally {
			closeResources(ps, rs);
		}
	}

	public String getLoggedInAdmin() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(SELECT_LOGGED_IN_ADMIN);
			rs = ps.executeQuery();
			String loggedInAdmin = null;
			if (rs.next() == true) {
				loggedInAdmin = rs.getString("username");
			}
			return loggedInAdmin;
		} catch (SQLException sqe) {
			throw new RuntimeException(sqe);
		} finally {
			closeResources(ps, rs);
		}
	}

	public void setAdminLoggedOut() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(UPDATE_ADMIN_LOGGED_OUT);
			ps.executeUpdate();
		} catch (SQLException sqe) {
			throw new RuntimeException(sqe);
		} finally {
			closeResources(ps, rs);
		}
	}

	public void disableAccount(String username) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(UPDATE_DISABLE_ACCOUNT);
			ps.setString(1, username);
			ps.executeUpdate();
		} catch (SQLException sqe) {
			throw new RuntimeException(sqe);
		} finally {
			closeResources(ps, rs);
		}
	}

	private void createAudit() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(CREATE_AUDIT);
			ps.executeUpdate();
		} catch (SQLException sqe) {
			throw new RuntimeException(sqe);
		} finally {
			closeResources(ps, rs);
		}
	}

	public void insertAuditRecord(String loggedInAdmin, String disableAccount) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(INSERT_AUDIT_RECORD);
			ps.setString(1, loggedInAdmin);
			ps.setString(2, disableAccount);
			ps.executeUpdate();
		} catch (SQLException sqe) {
			throw new RuntimeException(sqe);
		} finally {
			closeResources(ps, rs);
		}
	}

	// Methods for Tickets
	private void createTickets() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(CREATE_TICKETS);
			ps.executeUpdate();
			ps = conn.prepareStatement(ALTER_TICKETS);
			ps.executeUpdate();
		} catch (SQLException sqe) {
			throw new RuntimeException(sqe);
		} finally {
			closeResources(ps, rs);
		}
	}

	public int insertTicket() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(INSERT_TICKET);
			Timestamp now = new Timestamp(new Date().getTime());
			ps.setTimestamp(1, now);
			ps.executeUpdate();
			ps = conn.prepareStatement(FIND_LAST_TICKET);
			int ticketNumber = 0;
			rs = ps.executeQuery();
			if (rs.next() == true) {
				ticketNumber = rs.getInt("ticket_no");
			}
			return ticketNumber;
		} catch (SQLException sqe) {
			throw new RuntimeException(sqe);
		} finally {
			closeResources(ps, rs);
		}
	}

	public boolean isTicketValidForExit(int ticketNumber) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(SELECT_COUNT_UNSUBMITTED_TICKET);
			ps.setInt(1, ticketNumber);
			int count = 0;
			rs = ps.executeQuery();
			if (rs.next() == true) {
				count = rs.getInt("c");
			}
			return count == 1;
		} catch (SQLException sqe) {
			throw new RuntimeException(sqe);
		} finally {
			closeResources(ps, rs);
		}
	}

	public Calendar getTicketIssueTime(int ticketNumber) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(SELECT_TICKET_ISSUE_TIME);
			ps.setInt(1, ticketNumber);
			rs = ps.executeQuery();
			Timestamp issueTime = new Timestamp(new Date().getTime());
			if (rs.next() == true) {
				issueTime = rs.getTimestamp("issue_time");
			}
			Calendar issueTimeCal = Calendar.getInstance();
			issueTimeCal.setTime(issueTime);
			issueTimeCal.set(Calendar.MILLISECOND, 0);
			return issueTimeCal;
		} catch (SQLException sqe) {
			throw new RuntimeException(sqe);
		} finally {
			closeResources(ps, rs);
		}
	}

	public void setTicketAsPaid(int ticketNumber) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(UPDATE_TICKET_PAID);
			ps.setInt(1, ticketNumber);
			ps.executeUpdate();
		} catch (SQLException sqe) {
			throw new RuntimeException(sqe);
		} finally {
			closeResources(ps, rs);
		}
	}

	public void setTicketAsSubmitted(int ticketNumber) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(UPDATE_TICKET_SUBMITTED);
			ps.setInt(1, ticketNumber);
			ps.executeUpdate();
		} catch (SQLException sqe) {
			throw new RuntimeException(sqe);
		} finally {
			closeResources(ps, rs);
		}
	}

	public void setOccupancyForDayInReportUnit(ReportUnit selectedDay) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(SELECT_TICKET_COUNT_IN_DAY);
			Calendar aCal = new GregorianCalendar();
			aCal.set(Calendar.YEAR, selectedDay.getYear());
			aCal.set(Calendar.MONTH, selectedDay.getMonth() - 1);
			aCal.set(Calendar.DATE, selectedDay.getDay());
			aCal.set(Calendar.HOUR_OF_DAY, 0);
			aCal.set(Calendar.MINUTE, 0);
			aCal.set(Calendar.SECOND, 0);
			Timestamp atimestamp = new Timestamp(aCal.getTimeInMillis());
			Calendar bCal = new GregorianCalendar();
			bCal.set(Calendar.YEAR, selectedDay.getYear());
			bCal.set(Calendar.MONTH, selectedDay.getMonth() - 1);
			bCal.set(Calendar.DATE, selectedDay.getDay() + 1);
			bCal.set(Calendar.HOUR_OF_DAY, 0);
			bCal.set(Calendar.MINUTE, 0);
			bCal.set(Calendar.SECOND, 0);
			Timestamp btimestamp = new Timestamp(bCal.getTimeInMillis());

			ps.setTimestamp(1, atimestamp);
			ps.setTimestamp(2, btimestamp);
			rs = ps.executeQuery();
			int ticketCount = 0;
			if (rs.next() == true) {
				ticketCount = rs.getInt("ticket_count");
			}
			selectedDay.setTicketCount(ticketCount);
		} catch (SQLException sqe) {
			throw new RuntimeException(sqe);
		} finally {
			closeResources(ps, rs);
		}
	}

	// Methods for Payments
	private void createPayments() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(CREATE_PAYMENTS);
			ps.executeUpdate();
		} catch (SQLException sqe) {
			throw new RuntimeException(sqe);
		} finally {
			closeResources(ps, rs);
		}
	}

	public void insertPayment(double amount, int ticketNumber) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(INSERT_PAYMENT);
			ps.setDouble(1, amount);
			ps.setInt(2, ticketNumber);
			ps.executeUpdate();
		} catch (SQLException sqe) {
			throw new RuntimeException(sqe);
		} finally {
			closeResources(ps, rs);
		}
	}

	public void insertPayment(double amount) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(INSERT_PAYMENT_WITHOUT_TICKET);
			ps.setDouble(1, amount);
			ps.executeUpdate();
		} catch (SQLException sqe) {
			throw new RuntimeException(sqe);
		} finally {
			closeResources(ps, rs);
		}
	}

	public double getPaymentForTicket(int ticketNumber) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(SELECT_PAYMENTS_ON_TICKET);
			ps.setInt(1, ticketNumber);
			rs = ps.executeQuery();
			double payments = 0;
			if (rs.next() == true) {
				payments = rs.getDouble("payment");
			}
			return payments;
		} catch (SQLException sqe) {
			throw new RuntimeException(sqe);
		} finally {
			closeResources(ps, rs);
		}
	}

	public void setPaymentForHourInReportUnit(ReportUnit selectedHour) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(SELECT_PAYMENT_IN_HOUR);
			Calendar aCal = new GregorianCalendar();
			aCal.set(Calendar.YEAR, selectedHour.getYear());
			aCal.set(Calendar.MONTH, selectedHour.getMonth() - 1);
			aCal.set(Calendar.DATE, selectedHour.getDay());
			aCal.set(Calendar.HOUR_OF_DAY, selectedHour.getHour() - 1);
			aCal.set(Calendar.MINUTE, 0);
			aCal.set(Calendar.SECOND, 0);
			Timestamp atimestamp = new Timestamp(aCal.getTimeInMillis());
			Calendar bCal = new GregorianCalendar();
			bCal.set(Calendar.YEAR, selectedHour.getYear());
			bCal.set(Calendar.MONTH, selectedHour.getMonth() - 1);
			bCal.set(Calendar.DATE, selectedHour.getDay());
			bCal.set(Calendar.HOUR_OF_DAY, selectedHour.getHour());
			bCal.set(Calendar.MINUTE, 0);
			bCal.set(Calendar.SECOND, 0);
			Timestamp btimestamp = new Timestamp(bCal.getTimeInMillis());

			ps.setTimestamp(1, atimestamp);
			ps.setTimestamp(2, btimestamp);
			rs = ps.executeQuery();
			double payment = 0;
			if (rs.next() == true) {
				payment = rs.getDouble("payment");
			}
			selectedHour.setPayment(payment);
		} catch (SQLException sqe) {
			throw new RuntimeException(sqe);
		} finally {
			closeResources(ps, rs);
		}
	}

	public void setPaymentForDayInReportUnit(ReportUnit selectedDay) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(SELECT_PAYMENT_IN_DAY);
			Calendar aCal = new GregorianCalendar();
			aCal.set(Calendar.YEAR, selectedDay.getYear());
			aCal.set(Calendar.MONTH, selectedDay.getMonth() - 1);
			aCal.set(Calendar.DATE, selectedDay.getDay());
			aCal.set(Calendar.HOUR_OF_DAY, 0);
			aCal.set(Calendar.MINUTE, 0);
			aCal.set(Calendar.SECOND, 0);
			Timestamp atimestamp = new Timestamp(aCal.getTimeInMillis());
			Calendar bCal = new GregorianCalendar();
			bCal.set(Calendar.YEAR, selectedDay.getYear());
			bCal.set(Calendar.MONTH, selectedDay.getMonth() - 1);
			bCal.set(Calendar.DATE, selectedDay.getDay() + 1);
			bCal.set(Calendar.HOUR_OF_DAY, 0);
			bCal.set(Calendar.MINUTE, 0);
			bCal.set(Calendar.SECOND, 0);
			Timestamp btimestamp = new Timestamp(bCal.getTimeInMillis());

			ps.setTimestamp(1, atimestamp);
			ps.setTimestamp(2, btimestamp);
			rs = ps.executeQuery();
			double payment = 0;
			if (rs.next() == true) {
				payment = rs.getDouble("payment");
			}
			selectedDay.setPayment(payment);
		} catch (SQLException sqe) {
			throw new RuntimeException(sqe);
		} finally {
			closeResources(ps, rs);
		}
	}

	public void setPaymentForMonthInReportUnit(ReportUnit selectedMonth) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(SELECT_PAYMENT_IN_MONTH);
			Calendar aCal = new GregorianCalendar();
			aCal.set(Calendar.YEAR, selectedMonth.getYear());
			aCal.set(Calendar.MONTH, selectedMonth.getMonth() - 1);
			aCal.set(Calendar.DATE, 1);
			aCal.set(Calendar.HOUR_OF_DAY, 0);
			aCal.set(Calendar.MINUTE, 0);
			aCal.set(Calendar.SECOND, 0);
			Timestamp atimestamp = new Timestamp(aCal.getTimeInMillis());
			Calendar bCal = new GregorianCalendar();
			bCal.set(Calendar.YEAR, selectedMonth.getYear());
			bCal.set(Calendar.MONTH, selectedMonth.getMonth());
			bCal.set(Calendar.DATE, 1);
			bCal.set(Calendar.HOUR_OF_DAY, 0);
			bCal.set(Calendar.MINUTE, 0);
			bCal.set(Calendar.SECOND, 0);
			Timestamp btimestamp = new Timestamp(bCal.getTimeInMillis());

			ps.setTimestamp(1, atimestamp);
			ps.setTimestamp(2, btimestamp);
			rs = ps.executeQuery();
			double payment = 0;
			if (rs.next() == true) {
				payment = rs.getDouble("payment");
			}
			selectedMonth.setPayment(payment);
		} catch (SQLException sqe) {
			throw new RuntimeException(sqe);
		} finally {
			closeResources(ps, rs);
		}
	}

	// Methods for Payment Exceptions
	private void createPaymentExceptions() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(CREATE_PAYMENT_EXCEPTIONS);
			ps.executeUpdate();
		} catch (SQLException sqe) {
			throw new RuntimeException(sqe);
		} finally {
			closeResources(ps, rs);
		}
	}

	public void insertPaymentException(String name, String license, double amount) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(INSERT_PAYMENT_EXCEPTION);
			ps.setString(1, name);
			ps.setString(2, license);
			ps.setDouble(3, amount);
			ps.executeUpdate();
		} catch (SQLException sqe) {
			throw new RuntimeException(sqe);
		} finally {
			closeResources(ps, rs);
		}
	}

	public void runScript(String scriptFilePath) {
		Connection conn = null;
		Statement stmt = null;
		String str = new String();
		StringBuffer sb = new StringBuffer();
		try {
			File f = new File(scriptFilePath);
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			while ((str = br.readLine()) != null) {
				sb.append(str);
			}
			br.close();

			String[] instr = sb.toString().split(";");

			conn = DatabaseAccess.getConnection();
			stmt = conn.createStatement();
			for (int i = 0; i < instr.length; i++) {
				if (!instr[i].trim().equals("")) {
					stmt.executeUpdate(instr[i]);
					//System.out.println(">>" + instr[i]);
				}
			}

		} catch (SQLException sqe) {
			//throw new RuntimeException(sqe);
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
		finally {
			closeResources(stmt);
		}
	}

}