package game;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;

/**
 * la classe AddServer crea un registro RMI sulla porta 1099
 * @author Andrea Peluso
 * @version 1.0
 */
public class AddServer {
	
	private static ServerInterface stub = null;
	private static ServerInterface addService=null;

	/**
	 * metodo usato per creare un registro RMI sulla porta 1099 ed esportare la classe Adder come oggetto remoto
	 * @return indica se il registro è stato creato sulla porta 1099
	 */
	public static boolean Start(){
		try{
			LocateRegistry.createRegistry(1099); //creazione del registro RMI sulla porta 1099
			addService=new Adder(); //istanziazione dell'oggetto da registrare
			stub = (ServerInterface)UnicastRemoteObject.exportObject(addService, 0); //Utilizzato per esportare un oggetto e ottenere una stub che comunica con l'oggetto remoto.
			Naming.rebind("AddService",stub); //Ricollega il nome specificato a un nuovo oggetto remoto. 
			return true;
		}catch(Exception e){
			return false;
		}
	}

}