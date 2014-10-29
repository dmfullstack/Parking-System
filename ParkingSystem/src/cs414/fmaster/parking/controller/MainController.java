/**
 * 
 */
package cs414.fmaster.parking.controller;

import javax.swing.SwingUtilities;

import cs414.fmaster.parking.database.ParkingDatabaseAccess;
import cs414.fmaster.parking.ui.MainUI;

/**
 * @author MasterF
 * 
 */
public class MainController {
	private ParkingDatabaseAccess db;
	private MainUI ui;
	public ParkingOperationsHandler parkingOpsHandler;
	public PaymentHandler paymentHandler;
	public AdminOperationsHandler adminOpsHandler;
	public ReportsHandler reportsHandler;
	
	private static MainController instance = null;

	private MainController() {
	
	}

	private static MainController getInstance() {
		if (instance == null) {
			instance = new MainController();
		}
		return instance;
	}

	private void initializeDB() {
		db = ParkingDatabaseAccess.getInstance();
		db.runScript("src/cs414/fmaster/parking/database/populateDBScript.sql");
	}

	private void initializeHandlers() {
		parkingOpsHandler = ParkingOperationsHandler.getInstance(db);
		paymentHandler = PaymentHandler.getInstance(db);
		adminOpsHandler = AdminOperationsHandler.getInstance(db);
		reportsHandler = ReportsHandler.getInstance(db);
	}
	
	private void initializeUI() {
		ui = MainUI.getInstance(this);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainController mainController = MainController.getInstance();
				mainController.initializeDB();
				mainController.initializeHandlers();
				mainController.initializeUI();
			}
		});
	}
}
