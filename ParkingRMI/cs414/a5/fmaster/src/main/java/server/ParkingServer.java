package a5.fmaster.src.main.java.server;

import a5.fmaster.src.main.java.common.ParkingServerInterface;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ParkingServer {
	public ParkingServer(int port) {
		try {
			Registry registry = LocateRegistry.createRegistry(port);
			ParkingServerInterface parking = new ParkingServerImpl();
			UnicastRemoteObject.unexportObject(parking, true);
			registry.rebind("ParkingService", UnicastRemoteObject.exportObject(parking, port));
			System.out.println("ParkingService running...");
		} catch (Exception e) {
			System.out.println("Trouble: " + e);
		}
	}

	// run the program using
	// java ParkingServer <host> <port>

	public static void main(String args[]) {
		new ParkingServer(Integer.parseInt(args[0]));
	}
}
