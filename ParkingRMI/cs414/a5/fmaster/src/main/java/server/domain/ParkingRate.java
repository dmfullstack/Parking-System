/**
 * 
 */
package a5.fmaster.src.main.java.server.domain;

import java.io.Serializable;

/**
 * @author MasterF
 *
 */
public class ParkingRate implements Serializable {
	private static final long serialVersionUID = 1L;
	private double hours;
	private double rate;

	public double getHours() {
		return hours;
	}
	public double getRate() {
		return rate;
	}
	public void setHours(double hours) {
		this.hours = hours;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}

}
