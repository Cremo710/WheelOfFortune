package game;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * l'interfaccia RemoteObserver è usata per richiamare un metodo dei client da parte del server
 * @author Andrea Peluso
 * @version 1.0
 */
public interface RemoteObserver extends Remote {
	/**
	 * metodo usato per inviare un messaggio dal server ai client
	 * @param msg oggetto inviato dal server
	 * @throws RemoteException per la gestione delle eccezioni
	 */
	void remoteUpdate(Object msg) throws RemoteException;
}