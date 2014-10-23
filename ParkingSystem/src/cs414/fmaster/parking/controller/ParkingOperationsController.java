/**
 * 
 */
package cs414.fmaster.parking.controller;

import java.util.List;
import cs414.fmaster.parking.database.ParkingDatabaseAccess;

/**
 * @author MasterF
 * 
 */
public class ParkingOperationsController {
	ParkingDatabaseAccess db;
	Gate entryGate, exitGate;
	private static ParkingOperationsController instance = null;

	private ParkingOperationsController(ParkingDatabaseAccess db) {
		this.db = db;
		entryGate = new Gate(GateType.Entry);
		exitGate = new Gate(GateType.Exit);
	}

	public static ParkingOperationsController getInstance(ParkingDatabaseAccess db) {
		if (instance == null) {
			instance = new ParkingOperationsController(db);
		}
		return instance;
	}

	public int getCurrentParkingSize() {
		int size = db.getCurrentParkingSize();
		return size;
	}

	public int getCurrentAvailability() {
		int avail = db.getCurrentParkingAvailability();
		return avail;
	}

	public List<ParkingRate> getParkingRates() {
		List<ParkingRate> parkingRates = db.getParkingRates();
		return parkingRates;
	}

	public int getTicket() {
		boolean isParkingAvailable = isParkingAvailable();
		if (isParkingAvailable) {
			int ticketumber = db.insertTicket();
			db.decreaseParkingAvailability();
			return ticketumber;
		}
		return 0;
	}

	public boolean isParkingAvailable() {
		int pAvailability = db.getCurrentParkingAvailability();
		if (pAvailability == 0) {
			return false;
		}
		return true;
	}

	public void openEntryGate() {
		entryGate.openGate();
	}

	public void openExitGate() {
		exitGate.openGate();
	}

	public void closeEntryGate() {
		entryGate.closeGate();
	}

	public void closeExitGate() {
		exitGate.closeGate();
	}

	public boolean isTicketValid(int ticketNumber) {
		boolean isValidTicket = db.isTicketValidForExit(ticketNumber);
		return isValidTicket;
	}

}
