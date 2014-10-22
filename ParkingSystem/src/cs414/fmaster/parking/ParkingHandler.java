/**
 * 
 */
package cs414.fmaster.parking;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;

import cs414.fmaster.parking.database.ParkingDatabase;
import cs414.fmaster.parking.ui.ParkingUI;

/**
 * @author MasterF
 * 
 */
public class ParkingHandler z{
	ParkingSystem ps;
	ParkingDatabase db;
	private static ParkingHandler instance = null;

	protected ParkingHandler(ParkingSystem ps) {
		super();
		this.db = ps.getDatabaseManipulation();
		this.ps = ps;
	}

	public static ParkingHandler getInstance(ParkingSystem ps) {
		if (instance == null) {
			instance = new ParkingHandler(ps);
		}
		return instance;
	}

	public int enterParking() {
		boolean isParkingAvailable = ps.isParkingAvailable();
		if (isParkingAvailable) {
			int ticketNumber = issueTicket();
			ps.decreaseParkingAvailability();
			return ticketNumber;
		}
		return 0;
	}

	public int issueTicket() {
		Random randomGenerator = new Random();
		int ticketNumber;
		boolean isIssued;
		do {
			ticketNumber = randomGenerator.nextInt(999999 - 100000 + 1) + 100000;
			isIssued = db.isTicketIssued(ticketNumber);
		} while (isIssued);
		db.addTicket(ticketNumber);
		return ticketNumber;
	}

	public DefaultTableModel getParkingRatesInModel() {
		DefaultTableModel model = new DefaultTableModel();
		String[] columnNames = { "Hours", "Rate" };
		model.setColumnIdentifiers(columnNames);
		List<ParkingRate> parkingRates = db.getParkingRates();
		for (ParkingRate pr : parkingRates) {
			model.addRow(new Object[] { pr.getHours(), pr.getRate() });
		}
		return model;
	}
}
