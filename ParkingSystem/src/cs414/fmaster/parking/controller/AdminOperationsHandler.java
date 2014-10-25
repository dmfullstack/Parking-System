/**
 * 
 */
package cs414.fmaster.parking.controller;

import cs414.fmaster.parking.database.ParkingDatabaseAccess;

/**
 * @author MasterF
 * 
 */
public class AdminOperationsHandler {
	private ParkingDatabaseAccess db;
	private static AdminOperationsHandler instance = null;

	private AdminOperationsHandler(ParkingDatabaseAccess db) {
		this.db = db;
	}

	public static AdminOperationsHandler getInstance(ParkingDatabaseAccess db) {
		if (instance == null) {
			instance = new AdminOperationsHandler(db);
		}
		return instance;
	}

	public boolean login(String username, String password) {
		boolean isActiveUser = isActiveUserExist(username);
		if (isActiveUser) {
			String pwdInDb = db.getPassword(username);
			if (pwdInDb.equals(password)) {
				db.setAdminLoggedIn(username);
				return true;
			}
		}
		return false;
	}

	public boolean isActiveUserExist(String username) {
		boolean activeUserExists = db.isValidAccount(username);
		if (activeUserExists) {
			return true;
		}
		return false;
	}

	public String getSecurityQuestion(String username) {
		String securityQues = db.getSecurityQuestion(username);
		return securityQues;
	}

	public boolean verifySecurityAnswer(String username, String securityAns) {
		String securityAnsFromDb = db.getSecurityAnswer(username);
		if (securityAnsFromDb.equalsIgnoreCase(securityAns)) {
			return true;
		}
		return false;
	}

	public void setPassword(String username, String password) {
		db.setPassword(username, password);
	}

	public void logout() {
		db.setAdminLoggedOut();
	}

	public void setPassword(String newPwd) {
		String loggedInAdmin = db.getLoggedInAdmin();
		setPassword(loggedInAdmin, newPwd);
	}

	public boolean verifyPassword(String password) {
		String loggedInAdmin = db.getLoggedInAdmin();
		String pwdInDb = db.getPassword(loggedInAdmin);
		if (pwdInDb.equals(password)) {
			return true;
		}
		return false;
	}

	public boolean addAdminAccount(String username, String password, String securityQuestion, String securityAnswer) {
		boolean accountExists = db.isAccountExist(username);
		if(!accountExists) {
			db.insertAccountDetails(username, password, securityQuestion, securityAnswer);
			return true;
		}
		return false;
	}

	public boolean disableAccount(String username) {
		String loggedInAdmin = db.getLoggedInAdmin();
		if(loggedInAdmin.equals(username)) {
			return false;
		}
		boolean isExistAndEnabled = db.isValidAccount(username);
		if(isExistAndEnabled) {
			db.disableAccount(username);
			db.insertAuditRecord(loggedInAdmin, username);
			return true;
		}
		return false;
	}
}
