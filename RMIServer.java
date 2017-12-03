/**
 * Created by Empyreans on 15.11.2017.
 */

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Observable;
import java.util.Vector;

public class RMIServer extends Observable implements RMIServerInterface, Runnable {

    private CSVParser csvParser = new CSVParser("src/tempdata.CSV");
    private Vector<RMIClientInterface> clientVector = new Vector<>();

    public static void main(String[] args) {
        RMIServer rmiServer = new RMIServer();
//      Thread t1 = new Thread(rmiServer);
//      t1.start();
        rmiServer.run();
    }

    public RMIServer(){
        try {
            setupRMI();
        } catch (RemoteException e){
            e.printStackTrace();
        }
    }

    public void setupRMI() throws RemoteException {

        LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        // Server Stub: Client zu Server Kommunikation
        // der Server ist hier das Remote Object, mit dem der Client kommunizieren kann
        RMIServerInterface rmiServerInterface = this;
        RMIServerInterface stub = (RMIServerInterface) UnicastRemoteObject.exportObject(rmiServerInterface,0);
        Registry registry = LocateRegistry.getRegistry();
        registry.rebind("weather", stub);

    }

    // Anfragen fuer den Client---
    @Override
    public String printDayWeatherData(String dayString) throws RemoteException {
        return csvParser.printDayWeatherData(dayString);
    }

    @Override
    public void addObserver(RMIClientInterface rmiClientInterface) {
        clientVector.add(rmiClientInterface);
        System.out.println("Client added!");
    }
    // ---------------------------

    @Override
    public void notifyObservers() {
        for (RMIClientInterface client:clientVector) {
            try {
                for (Day day: csvParser.updatedDays) {
                    client.callback(csvParser.printDayWeatherData(day.getDate()));
                }
            } catch (RemoteException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        try {
            while (true){
                csvParser.updateCSVFile();
                if (!clientVector.isEmpty() && !(csvParser.updatedDays.isEmpty())){
                    notifyObservers();
                    break;
                } else {
                    Thread.sleep(5000);
                }
            }
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
