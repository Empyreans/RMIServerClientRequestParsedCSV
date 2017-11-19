package RMIWeatherClientServer; /**
 * Created by Empyreans on 15.11.2017.
 */

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServerInterface extends Remote {
    String printDayWeatherData(String dayString) throws RemoteException;
    void addObserver(RMIClientInterface rmiClientInterface) throws RemoteException;
}
