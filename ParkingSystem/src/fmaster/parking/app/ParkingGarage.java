package fmaster.parking.app;

import fmaster.parking.database.DatabaseManipulation;

/**
 * @author MasterF
 * 
 */

public class ParkingGarage {
	String name;
	Gate entryGate;
	Gate exitGate;
	protected DatabaseManipulation db;

	public ParkingGarage(String name, int size) {
		super();
		db = new DatabaseManipulation();
		this.initializeGates();
		this.initializeParkingSize(size);
		this.initializeParkingRates();
		this.initializeAdminAccount();
	}
	
	private void initializeGates() {
		entryGate = new Gate(GateType.ENTRY);
		exitGate = new Gate(GateType.EXIT);
	}

	private void initializeParkingSize(int size) {
		db.initializeParkingSize(size);
	}

	private void initializeParkingRates() {
		db.initializeParkingRates();
	}

	private void initializeAdminAccount() {
		db.initializeAdminAccount();
	}

	public void configureParkingSize(int parkingSize) {
		db.configureParkingSize(parkingSize);
	}

	public int getCurrentParkingSize() {
		int pSize = db.getCurrentParkingSize();
		return pSize;
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
	
	public static void main(String[] args) {
		ParkingGarage pg = new ParkingGarage("My Garage", 50);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pg.configureParkingSize(100);
	}
}
