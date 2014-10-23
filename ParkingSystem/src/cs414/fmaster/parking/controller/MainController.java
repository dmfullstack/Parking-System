/**
 * 
 */
package cs414.fmaster.parking.controller;

import cs414.fmaster.parking.database.ParkingDatabaseAccess;

/**
 * @author MasterF
 * 
 */
public class MainController {
	ParkingDatabaseAccess db;
	public ParkingOperationsController parkingController;
	public PaymentController paymentController;
	private static MainController instance = null;

	private MainController() {
		this.db = ParkingDatabaseAccess.getInstance();
		this.parkingController = ParkingOperationsController.getInstance(db);
		this.paymentController = PaymentController.getInstance(db);
	}
	
	public static MainController getInstance() {
		if (instance == null) {
			instance = new MainController();
		}
		return instance;
	}

}
