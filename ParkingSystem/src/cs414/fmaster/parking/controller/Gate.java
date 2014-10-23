/**
 * 
 */
package cs414.fmaster.parking.controller;

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

	void openGate() {
		this.isOpen = true;
	}

	void closeGate() {
		this.isOpen = false;
	}
}
