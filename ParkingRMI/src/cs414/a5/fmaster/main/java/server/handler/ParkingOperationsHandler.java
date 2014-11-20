/**
 * 
 */
package cs414.a5.fmaster.main.java.server.handler;

import java.util.List;

import cs414.a5.fmaster.main.java.server.database.ParkingDatabaseAccess;
import cs414.a5.fmaster.main.java.server.domain.Gate;
import cs414.a5.fmaster.main.java.server.domain.GateType;
import cs414.a5.fmaster.main.java.server.domain.ParkingRate;

/**
 * @author MasterF
 * 
 */
public class ParkingOperationsHandler {
	private ParkingDatabaseAccess db;
	private Gate entryGate;
	private Gate exitGate;
	private static ParkingOperationsHandler instance = null;

	private ParkingOperationsHandler(ParkingDatabaseAccess db) {
		this.db = db;
		entryGate = new Gate(GateType.Entry);
		exitGate = new Gate(GateType.Exit);
	}

	public static ParkingOperationsHandler getInstance(ParkingDatabaseAccess db) {
		if (instance == null) {
			instance = new ParkingOperationsHandler(db);
		}
		return instance;
	}

	public void openEntryGate() {
		entryGate.openGate();
	}

	public void closeEntryGate() {
		entryGate.closeGate();
	}

	public void openExitGate() {
		exitGate.openGate();
	}

	public void closeExitGate() {
		exitGate.closeGate();
	}

	public int getCurrentParkingSize() {
		int size = db.getCurrentParkingSize();
		return size;
	}

	public void updateParkingSize(int newSize) {
		int newAvailableSpots = newSize - getCurrentParkingSize();
		int newTotalAvailability = newAvailableSpots + getCurrentAvailability();
		db.updateParkingAvailability(newTotalAvailability);
		db.configureParkingSize(newSize);
	}

	public int getCurrentAvailability() {
		int avail = db.getCurrentParkingAvailability();
		return avail;
	}

	public int getCurrentParkingOccupancy() {
		return getCurrentParkingSize() - getCurrentAvailability();
	}

	public boolean isParkingAvailable() {
		int pAvailability = db.getCurrentParkingAvailability();
		if (pAvailability == 0) {
			return false;
		}
		return true;
	}

	public boolean isParkingEmpty() {
		int availableParking = db.getCurrentParkingAvailability();
		int parkingSize = db.getCurrentParkingSize();
		if (availableParking == parkingSize) {
			return true;
		}
		return false;
	}

	public List<ParkingRate> getParkingRates() {
		List<ParkingRate> parkingRates = db.getParkingRates();
		return parkingRates;
	}

	public void updateParkingRates(List<ParkingRate> parkingRates) {
		db.updateParkingRates(parkingRates);
	}

	public boolean isTicketValid(int ticketNumber) {
		boolean isValidTicket = db.isTicketValidForExit(ticketNumber);
		return isValidTicket;
	}

	public int getTicket() {
		boolean isParkingAvailable = isParkingAvailable();
		if (isParkingAvailable) {
			int ticketumber = db.insertTicket();
			decreaseParkingAvailability();
			return ticketumber;
		}
		return 0;
	}

	private void decreaseParkingAvailability() {
		int currentAvailability = db.getCurrentParkingAvailability();
		if (currentAvailability > 0) {
			db.decreaseParkingAvailability();
		}
	}
}
