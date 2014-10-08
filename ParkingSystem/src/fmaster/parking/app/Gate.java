/**
 * 
 */
package fmaster.parking.app;

/**
 * @author masterf
 * 
 */
public class Gate {
	GateType type;
	Boolean isOpen;

	public Gate(GateType type) {
		super();
		this.type = type;
		this.isOpen = false;
	}

	public String getType() {
		String type = this.type.toString();
		String firstLetter = type.substring(0, 1);
		String restLetters = type.substring(1).toLowerCase();
		return firstLetter + restLetters;
	}

	void openGate() {
		this.isOpen = true;
		System.out.println(this.type + " gate is opened.");
	}

	void closeGate() {
		this.isOpen = true;
		System.out.println(this.type + " gate is closed.");
	}
}
