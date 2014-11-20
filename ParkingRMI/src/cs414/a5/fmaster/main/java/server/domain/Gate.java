/**
 * 
 */
package cs414.a5.fmaster.main.java.server.domain;


/**
 * @author masterf
 * 
 */
public class Gate {
	
	private GateType type;
	
	private Boolean isOpen;

	public Gate(GateType type) {
		super();
		this.type = type;
		this.isOpen = false;
	}

	public void openGate() {
		this.isOpen = true;
	}

	public void closeGate() {
		this.isOpen = false;
	}
}
