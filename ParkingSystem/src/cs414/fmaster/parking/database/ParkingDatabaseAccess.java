package cs414.fmaster.parking.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cs414.fmaster.parking.controller.ParkingRate;


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
	private static final String CREATE_ACCOUNTS = "CREATE TABLE accounts (username VARCHAR(10), password VARCHAR(10), secret_question VARCHAR(25), secret_answer VARCHAR(10), is_logged_in BOOLEAN DEFAULT FALSE, is_active BOOLEAN DEFAULT TRUE, PRIMARY KEY (username))";
	private static final String CREATE_AUDIT = "CREATE TABLE audit (triggered_by VARCHAR(10), disabled_acc VARCHAR(10), triggered_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY (triggered_by) REFERENCES accounts(username), FOREIGN KEY (disabled_acc) REFERENCES accounts(username))";
	private static final String CREATE_TICKETS = "CREATE TABLE tickets (ticket_no INTEGER NOT NULL AUTO_INCREMENT, issue_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, is_paid BOOLEAN DEFAULT FALSE, PRIMARY KEY (ticket_no))";
	private static final String CREATE_PAYMENTS = "CREATE TABLE payments (amount DECIMAL(6,2) NOT NULL, ticket_no INTEGER NOT NULL, paid_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY (ticket_no) REFERENCES tickets(ticket_no))";

	private static final String SELECT_PARKING_RATES = "SELECT time_interval AS Hours, rate as Rate FROM parking_rates";
	private static final String SELECT_CURRENT_PARKING_SIZE = "SELECT size FROM parking_size_history WHERE end_time IS NULL";
	private static final String SELECT_CURRENT_PARKING_AVAILABILITY = "SELECT availability FROM parking_availability_history WHERE end_time IS NULL";
	private static final String SELECT_COUNT_UNPAID_TICKET = "SELECT COUNT(*) c FROM tickets WHERE ticket_no = ? AND is_paid = false";
	private static final String SELECT_TICKET_ISSUE_TIME = "SELECT issue_time FROM tickets WHERE ticket_no = ?";
	private static final String FIND_LAST_TICKET = "SELECT LAST_INSERT_ID() AS ticket_no";
	private static final String SELECT_PAYMENTS_ON_TICKET = "SELECT SUM(amount) as payment where ticket_no = ?";

	private static final String INSERT_PARKING_SIZE = "INSERT INTO parking_size_history(size) VALUES (?)";
	private static final String INSERT_PARKING_AVAILABILITY = "INSERT INTO parking_availability_history(availability) VALUES (?)";
	private static final String INSERT_PARKING_RATE = "INSERT INTO parking_rates VALUES (?, ?)";
	private static final String INSERT_ACCOUNT = "INSERT INTO accounts(username, password, secret_question, secret_answer) values (?,?,?,?)";
	private static final String INSERT_TICKET = "INSERT INTO tickets(issue_time) VALUES (?)";
	private static final String INSERT_PAYMENT = "INSERT INTO payments(amount, ticket_no) VALUES (?, ?)";

	private static final String UPDATE_PARKING_SIZE_ENDTIME = "UPDATE parking_size_history SET end_time=? WHERE end_time IS NULL";
	private static final String UPDATE_PARKING_AVAILABILITY_ENDTIME = "UPDATE parking_availability_history SET end_time=? WHERE end_time IS NULL";
private static final String UPDATE_TICKET_PAID = "UPDATE tickets SET is_paid = false WHERE ticket_no = ?";
	
	private static final String ALTER_TICKETS = "ALTER TABLE tickets AUTO_INCREMENT = 100000";
	
	protected ParkingDatabaseAccess() {
		super();
		initializeParkingDatabase();
	}

	public static ParkingDatabaseAccess getInstance() {
		if (instance == null) {
			instance = new ParkingDatabaseAccess();
		}
		return instance;
	}

	public void initializeParkingDatabase() {
		createParkingAvailability();
		insertParkingAvailability(1);
		
		createParkingSize();
		insertParkingSize(1);
		
		createParkingRates();		
		insertParkingRate(0.5, 0.5);
		insertParkingRate(1, 1);
		insertParkingRate(2, 2);
		insertParkingRate(10, 10);
		insertParkingRate(24, 24);
		
		createAccountTable();
		insertAccountDetails("admin", "admin", "admin", "admin");
		
		createAudit();
		createTickets();
		createPayments();
	}

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

	private void insertAccountDetails(String username, String password, String secretQuestion, String secretAnswer) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(INSERT_ACCOUNT);
			ps.setString(1, username);
			ps.setString(2, password);
			ps.setString(3, secretQuestion);
			ps.setString(4, secretAnswer);
			ps.executeUpdate();
		} catch (SQLException sqe) {
			throw new RuntimeException(sqe);
		} finally {
			closeResources(ps, rs);
		}
	}

	public void createTickets() {
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

	public void createPayments() {
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

	public void createAudit() {
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

	public boolean isTicketValidForExit(int ticketNumber) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(SELECT_COUNT_UNPAID_TICKET);
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

	public void decreaseParkingAvailability() {
		int avail = getCurrentParkingAvailability();
		updateCurrentParkingAvailabilityEndTime();
		insertParkingAvailability(avail - 1);
	}
	
	public void increaseParkingAvailability() {
		int avail = getCurrentParkingAvailability();
		updateCurrentParkingAvailabilityEndTime();
		insertParkingAvailability(avail + 1);
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

	public void makePayment(double amount, int ticketNumber) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(INSERT_PAYMENT);
			ps.setDouble(1, amount);
			ps.setInt(2, ticketNumber);
			System.out.println(ticketNumber);
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

}
