package a5.fmaster.src.main.java.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteObserver extends Remote {
    void update(Object observable, Object updateMsg) throws RemoteException;
    
    String getClientType() throws RemoteException;
}