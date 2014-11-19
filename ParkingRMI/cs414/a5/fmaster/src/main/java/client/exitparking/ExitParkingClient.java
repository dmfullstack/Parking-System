package a5.fmaster.src.main.java.client.exitparking;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.*;

import a5.fmaster.src.main.java.client.RemoteObserver;
import a5.fmaster.src.main.java.common.ParkingInterface;

/**
 * @author MasterF
 * 
 */
public class ExitParkingClient {
	public static void main(String[] args) {
		try {
			final ParkingInterface psi = (ParkingInterface) Naming.lookup("rmi://" + args[0] + ":" + args[1] + "/ParkingService");
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					try {
						RemoteObserver client = new ExitParkingObserverImpl(psi);
						psi.addObserver(client);
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