package a5.fmaster.src.main.java.client;

import java.rmi.RemoteException;
import java.util.List;

import a5.fmaster.src.main.java.server.domain.ParkingRate;
import a5.fmaster.src.main.java.server.domain.ReportUnit;

public interface ParkingClientInterface extends java.rmi.Remote {
	public void updateMainMessage() throws RemoteException;
}
