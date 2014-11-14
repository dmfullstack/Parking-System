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
public class ParkingClient {
	public static void main(String[] args) {
		try {
			final ParkingServerInterface psi = (ParkingServerInterface) Naming.lookup("rmi://" + args[0] + ":" + args[1] + "/ParkingService");
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					try {
						ParkingClientInterface client = new ParkingClientImpl(psi);
						psi.registerForCallback(client);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			});
		} catch (MalformedURLException murle) {
			System.out.println("MalformedURLException");
			System.out.println(murle);
		} catch (RemoteException re) {
			System.out.println("RemoteException");
			System.out.println(re);
		} catch (NotBoundException nbe) {
			System.out.println("NotBoundException");
			System.out.println(nbe);
		} catch (java.lang.ArithmeticException ae) {
			System.out.println("java.lang.ArithmeticException");
			System.out.println(ae);
		}
	}
}