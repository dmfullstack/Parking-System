package cs414.fmaster.parking;

import java.util.ArrayList;
import java.util.List;

import cs414.fmaster.parking.database.ParkingDatabase;
import cs414.fmaster.parking.ui.ParkingUI;

/**
 * @author MasterF
 * 
 */

public class ParkingSystem {
	String name;
	Gate entryGate;
	Gate exitGate;
	public ParkingHandler parkingHndlr;
	public ParkingDatabase db;
	public ParkingUI ui;

	public ParkingSystem() {
		super();
		this.name = name;
		initializeGates();
		parkingHndlr = ParkingHandler.getInstance();
		db = ParkingDatabase.getInstance();
		ui = ParkingUI.getInstance(this);
	}
	
	public String getName() {
		return name;
	}

	private void initializeGates() {
		entryGate = new Gate(GateType.ENTRY);
		exitGate = new Gate(GateType.EXIT);
	}

	public void configureParkingSize(int parkingSize) {
		db.configureParkingSize(parkingSize);
	}

	public int getCurrentParkingSize() {
		int pSize = db.getCurrentParkingSize();
		return pSize;
	}

	public List<ParkingRate> getParkingRates() {
		List<ParkingRate> parkingRates = new ArrayList<ParkingRate>();
		parkingRates = db.getParkingRates();
		return parkingRates;
	}
	
	private void configureParkingRate(double timeInterval, double amount) {

	}

	public void configureParkingRates() {

	}

	private void openEntryGate() {
		entryGate.openGate();
	}

	private void openExitGate() {
		exitGate.openGate();
	}

	private void closeEntryGate() {
		entryGate.closeGate();
	}

	private void closeExitGate() {
		exitGate.closeGate();
	}

	public int enterParking() {
		return parkingHndlr.enterParking();
	}

	public ParkingDatabase getDatabaseManipulation() {
		return db;
	}
	
	public ParkingUI getParkingUI() {
		return ui;
	}
	
	public int getCurrentParkingAvailability() {
		int pAvailability = db.getCurrentParkingAvailability();
		return pAvailability;
	}
	
	public boolean isParkingAvailable() {
		int pAvailability = getCurrentParkingAvailability();
		if (pAvailability == 0) {
			return false;
		}
		return true;
	}
	
	public void decreaseParkingAvailability() {
		db.decreaseParkingAvailability();
	}
	
	public void increaseParkingAvailability() {
		db.increaseParkingAvailability();
	}
	
	public static void main(String[] args) {
		ParkingSystem pg = new ParkingSystem("My Parking System");
	}

	
}

