package RMIWeatherClientServer; /**
 * Created by Empyreans on 15.11.2017.
 */

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Observable;
import java.util.Vector;
// ich muss nicht Observable implementieren, oder?
public class RMIServer extends Observable implements RMIServerInterface, Runnable {

    CSVParser csvParser = new CSVParser("tempdata.CSV");
    private Vector<RMIClientInterface> clientVector = new Vector<>();

    public static void main(String[] args) {
        RMIServer rmiServer = new RMIServer();
    }

    public RMIServer(){
        try {
            execute();
        } catch (RemoteException e){
            e.printStackTrace();
        } catch (NotBoundException e){
            e.printStackTrace();
        }
    }

    public void execute() throws RemoteException, NotBoundException {

        LocateRegistry.createRegistry(Registry.REGISTRY_PORT);

        // Server Stub: Client zu Server Kommunikation
        // der Server ist hier das Remote Object, mit dem der Client kommunizieren kann
        RMIServerInterface rmiServerInterface = this;
        RMIServerInterface stub = (RMIServerInterface) UnicastRemoteObject.exportObject(rmiServerInterface,0);
        Registry registry = LocateRegistry.getRegistry();
        registry.rebind("weather", stub);

    }

    @Override
    public String printDayWeatherData(String dayString) throws RemoteException {
        return csvParser.printDayWeatherData(dayString);
    }

    @Override
    public void addObserver(RMIClientInterface rmiClientInterface) {
        clientVector.add(rmiClientInterface);
        System.out.println("Client added!");
    }

    @Override
    public void notifyObservers() {
        for (RMIClientInterface client:clientVector) {
            try {
                client.callback("Test");
            } catch (RemoteException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        try {
            while (true){
//                csvParser.updateCSVFile();
                if (!clientVector.isEmpty() && csvParser.checkUpdated()){
                    notifyObservers();
                    break;
                } else {
                    Thread.sleep(2000);
                }
            }
        } catch (InterruptedException e){
            e.printStackTrace();
        }

    }
}
