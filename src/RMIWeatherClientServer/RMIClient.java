package RMIWeatherClientServer; /**
 * Created by Empyreans on 15.11.2017.
 */

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class RMIClient implements RMIClientInterface {

    Scanner reader = new Scanner(System.in);

    public static void main(String[] args) {
        RMIClient rmiClient = new RMIClient();
    }

    public RMIClient(){
        try {
            execute();
        } catch (IOException e){
            e.printStackTrace();
        } catch (NotBoundException e){
            e.printStackTrace();
        }
    }

    public void execute() throws RemoteException, NotBoundException{
        Registry registry = LocateRegistry.getRegistry();

        // Callback-Spezifikationen
        RMIClientInterface client = this;
        RMIClientInterface clientStub = (RMIClientInterface) UnicastRemoteObject.exportObject(client, 0);

        // Client fragt an Server an, gibt sich selbst mit um als Observer registriert zu werden
        // Alternative: client bekommt eigenes Register(?)
        RMIServerInterface rmiServerInterface = (RMIServerInterface) registry.lookup("weather");
        rmiServerInterface.addObserver(clientStub);

        // Anfragen an Server
        String userInput;
        while (true){
            System.out.println("bitte Datum eingeben / oder quit zum Beenden:\n");
            userInput = reader.nextLine();
            if (userInput.equals("quit")){
                break;
            }
            System.out.println(rmiServerInterface.printDayWeatherData(userInput));
        }

    }

    @Override
    public void callback(String test) {
        System.out.println(test);
    }

}
