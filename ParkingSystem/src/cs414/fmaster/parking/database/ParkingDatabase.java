package cs414.fmaster.parking.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cs414.fmaster.parking.ParkingRate;

/**
 * This DB class contains methods to retrieve information from the database
 * 
 * @author masterf
 */

public class ParkingDatabase extends DatabaseAccess {
	private static ParkingDatabase instance = null;
	private static final String CREATE_PARKING_SIZE_HISTORY = "CREATE TABLE parking_size_history (size INTEGER NOT NULL, start_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, end_time TIMESTAMP NULL)";
	private static final String CREATE_PARKING_AVAILABILITY_HISTORY = "CREATE TABLE parking_availability_history (availability INTEGER NOT NULL, start_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, end_time TIMESTAMP NULL)";
	private static final String CREATE_PARKING_RATES = "CREATE TABLE parking_rates (time_interval DECIMAL(3,1) NOT NULL, rate DECIMAL(7,2) NOT NULL, PRIMARY KEY (time_interval))";
	private static final String CREATE_ACCOUNTS = "CREATE TABLE accounts (username VARCHAR(10), password VARCHAR(10), secret_question VARCHAR(25), secret_answer VARCHAR(10), is_logged_in BOOLEAN DEFAULT FALSE, is_active BOOLEAN DEFAULT TRUE, PRIMARY KEY (username))";
	private static final String CREATE_AUDIT = "CREATE TABLE audit (triggered_by VARCHAR(10), disabled_acc VARCHAR(10), triggered_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY (triggered_by) REFERENCES accounts(username), FOREIGN KEY (disabled_acc) REFERENCES accounts(username))";
	private static final String CREATE_TICKETS = "CREATE TABLE tickets (ticket_no INTEGER NOT NULL, issue_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, is_paid BOOLEAN DEFAULT FALSE, PRIMARY KEY (ticket_no))";
	private static final String CREATE_PAYMENTS = "CREATE TABLE payments (amount INTEGER NOT NULL, ticket_no INTEGER, paid_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY (ticket_no) REFERENCES tickets(ticket_no))";

	private static final String SELECT_PARKING_RATES = "SELECT time_interval AS Hours, rate as Rate FROM parking_rates";
	private static final String SELECT_CURRENT_PARKING_SIZE = "SELECT size FROM parking_size_history WHERE end_time IS NULL";
	private static final String SELECT_CURRENT_PARKING_AVAILABILITY = "SELECT availability FROM parking_availability_history WHERE end_time IS NULL";
	private static final String SELECT_COUNT_TICKET = "SELECT COUNT(*) c FROM tickets WHERE ticket_no = ?";

	private static final String INSERT_PARKING_SIZE = "INSERT INTO parking_size_history(size) VALUES (?)";
	private static final String INSERT_PARKING_AVAILABILITY = "INSERT INTO parking_availability_history(availability) VALUES (?)";
	private static final String INSERT_PARKING_RATE = "INSERT INTO parking_rates VALUES (?, ?)";
	private static final String INSERT_ACCOUNT = "INSERT INTO accounts(username, password, secret_question, secret_answer) values (?,?,?,?)";
	private static final String INSERT_TICKET = "INSERT INTO tickets(ticket_no) VALUES (?)";

	private static final String UPDATE_PARKING_SIZE_ENDTIME = "UPDATE parking_size_history SET end_time=? WHERE end_time IS NULL";
	private static final String UPDATE_PARKING_AVAILABILITY_ENDTIME = "UPDATE parking_availability_history SET end_time=? WHERE end_time IS NULL";

	protected ParkingDatabase() {
		super();
		initializeParkingDatabase();
	}

	public static ParkingDatabase getInstance() {
		if (instance == null) {
			instance = new ParkingDatabase();
		}
		return instance;
	}

	public void initializeParkingDatabase() {
		initializeParkingAvailability();
		initializeParkingSize();
		initializeParkingRates();
		initializeAdminAccount();
		initializeAudit();
		initializeTicketSystem();
		initializePaymentSystem();
	}

	private void initializeParkingAvailability() {
		createParkingAvailability();
		insertInitialParkingAvailability();
	}

	private void insertInitialParkingAvailability() {
		insertParkingAvailability(1);
	}

	public void initializeParkingSize() {
		createParkingSize();
		insertInitialParkingSize();
	}

	private void insertInitialParkingSize() {
		insertParkingSize(1);		
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

	public void initializeParkingRates() {
		createParkingRates();
		insertInitialParkingRates();
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

	private void insertInitialParkingRates() {
		insertParkingRate(0.5, 1);
		insertParkingRate(1, 1);
		insertParkingRate(2, 1);
		insertParkingRate(10, 1);
		insertParkingRate(24, 1);
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

	public void initializeAdminAccount() {
		createAccountTable();
		insertAccountDetails("admin", "admin", "admin", "admin");
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

	public void initializeTicketSystem() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(CREATE_TICKETS);
			ps.executeUpdate();
		} catch (SQLException sqe) {
			throw new RuntimeException(sqe);
		} finally {
			closeResources(ps, rs);
		}
	}

	public void initializePaymentSystem() {
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

	public void initializeAudit() {
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

	public boolean isTicketIssued(int ticketNumber) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(SELECT_COUNT_TICKET);
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

	public void addTicket(int ticketNumber) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseAccess.getConnection();
			ps = conn.prepareStatement(INSERT_TICKET);
			ps.setInt(1, ticketNumber);
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
		updateCurrentParkingAvailabilityEndTime();
		insertParkingAvailability(getCurrentParkingAvailability() - 1);
	}
	
	public void increaseParkingAvailability() {
		updateCurrentParkingAvailabilityEndTime();
		insertParkingAvailability(getCurrentParkingAvailability() + 1);
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

}
