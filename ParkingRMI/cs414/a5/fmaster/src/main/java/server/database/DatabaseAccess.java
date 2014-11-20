package a5.fmaster.src.main.java.server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Manages database access. Handles creation of connection, returning connection, closing connection and other resources.
 * 
 * @author masterf
 * 
 */
public class DatabaseAccess {
	private static Connection connection;

	private final static String DRIVER = "com.mysql.jdbc.Driver";
	private final static String CONFIGPROP1 = "?zeroDateTimeBehavior=convertToNull";
	/*private final static String URL = "jdbc:mysql://localhost/parking_system";
	private final static String USER = "masterf";
	private final static String PASSWORD = "cs414";*/
	private final static String URL = "jdbc:mysql://faure/master";
	private final static String USER = "master";
	private final static String PASSWORD = "829987005";

	protected DatabaseAccess() {
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Could not locate database driver", e);
		}
	}

	protected static Connection getConnection() throws SQLException {
		Connection connection = DriverManager.getConnection(URL + CONFIGPROP1, USER, PASSWORD);
		return connection;
	}

	protected void closeResources(Statement s, ResultSet r) {
		try {
			if (s != null)
				s.close();
		} catch (SQLException e) {
		}

		try {
			if (r != null)
				r.close();
		} catch (SQLException e) {
		}
		closeConnection();
	}

	protected void closeResources(Statement s) {
		try {
			if (s != null)
				s.close();
		} catch (SQLException e) {
		}
		closeConnection();
	}

	protected static void closeConnection() {
		if (connection != null) {
			try {
				connection.close();
			} catch (Exception ignore) {
			}
		}
	}
}