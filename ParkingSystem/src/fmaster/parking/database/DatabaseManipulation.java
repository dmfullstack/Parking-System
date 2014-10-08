package fmaster.parking.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * This DB class contains methods to retrieve information from the database
 * 
 * @author masterf
 */

public class DatabaseManipulation extends DatabaseAccess {
	private static final String CREATE_PARKING_SIZE_HISTORY = "CREATE TABLE parking_size_history (size INTEGER NOT NULL, start_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, end_time TIMESTAMP NULL)";
	private static final String INSERT_PARKING_SIZE = "INSERT INTO parking_size_history(size) VALUES (?)";
	private static final String UPDATE_PARKING_SIZE_ENDTIME = "UPDATE parking_size_history SET end_time=? WHERE end_time IS NULL";
	private static final String SELECT_CURRENT_PARKING_SIZE = "SELECT size FROM parking_size_history WHERE end_time IS NULL";
	private static final String CREATE_PARKING_RATES = "CREATE TABLE parking_rates (time_interval DECIMAL(3,1) NOT NULL, rate DECIMAL(7,2) NOT NULL)";
	private static final String INSERT_PARKING_RATE = "INSERT INTO parking_rates VALUES (?, ?)";
	private static final String CREATE_ACCOUNTS = "CREATE TABLE accounts (username VARCHAR(10), password VARCHAR(10), secret_question VARCHAR(25), secret_answer VARCHAR(10), is_logged_in BOOLEAN DEFAULT FALSE, is_active BOOLEAN DEFAULT TRUE)";
	private static final String INSERT_ACCOUNT = "INSERT INTO accounts(username, password, secret_question, secret_answer) values (?,?,?,?)";

	public DatabaseManipulation() {
		super();
	}

	public void initializeParkingSize(int size) {
		createParkingSize();
		insertParkingSize(size);
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
			int size = rs.getInt("size");
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
		insertParkingRate(0.5,1);
		insertParkingRate(1,1);
		insertParkingRate(2,1);
		insertParkingRate(10,1);
		insertParkingRate(24,1);		
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
}
