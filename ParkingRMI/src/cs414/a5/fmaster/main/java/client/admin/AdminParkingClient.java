package cs414.a5.fmaster.main.java.client.admin;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.*;

import cs414.a5.fmaster.main.java.client.RemoteObserver;
import cs414.a5.fmaster.main.java.server.ParkingInterface;

/**
 * @author MasterF
 * 
 */
public class AdminParkingClient {
	public static void main(String[] args) {
		try {
			final ParkingInterface psi = (ParkingInterface) Naming.lookup("rmi://" + args[0] + ":" + args[1] + "/ParkingService");
			if (!psi.isAdminClientStarted()) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						try {
							RemoteObserver client = new AdminParkingObserverImpl(psi);
							psi.addObserver(client);
						} catch (RemoteException re) {
							System.out.println("RemoteException");
							System.out.println(re);
						}
					}
				});
			}
			else {
				System.out.println("Admin client already started. It cannot be started again.");
			}
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
		} catch (Exception e) {
			System.out.println("Exception");
			System.out.println(e);
		}
	}
}