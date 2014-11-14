package a5.fmaster.src.main.java.client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.*;

import a5.fmaster.src.main.java.client.ui.MainUI;
import a5.fmaster.src.main.java.common.ParkingServerInterface;

/**
 * @author MasterF
 * 
 */
public class ParkingClientImpl extends UnicastRemoteObject implements ParkingClientInterface {
	MainUI mainUI;
	private static final long serialVersionUID = 1L;

	protected ParkingClientImpl(ParkingServerInterface psi) throws RemoteException {
		super();
		this.mainUI = new MainUI(psi);
	}
	
	@Override
	public void updateMainMessage() throws RemoteException {
		System.out.println(mainUI);
		mainUI.updateWelcomeMessage();
	}
}