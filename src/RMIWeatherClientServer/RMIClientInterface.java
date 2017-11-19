package RMIWeatherClientServer;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Empyreans on 15.11.2017.
 */
public interface RMIClientInterface extends Remote {
    void callback(String test) throws RemoteException;
}
