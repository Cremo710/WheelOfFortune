package game;
import java.rmi.RemoteException;
import java.util.Observable;
import java.util.Observer;
/**
 * la classe WrappedObserver è usata per registrare un osservatore interessato a ricevere le notifiche
 * <p>
 * Una classe può implementare l'interfaccia Observer quando desidera essere informata dei cambiamenti negli oggetti osservabili.
 * @author Andrea Peluso
 * @version 1.0
 */
public class WrappedObserver implements Observer{
private RemoteObserver remoteClient = null;
/**
 * costruttore della classe
 * @param ro rappresenta l'osservatore remoto
 */
public WrappedObserver(RemoteObserver ro) {
this.remoteClient = ro;
}
/**
 * metodo usato per inviare una notifica all'osservatore remoto
 * @param o rappresenta l'osservatore remoto
 * @param msg indica il messaggio 
 */
public void update(Observable o,Object msg) {
try {
remoteClient.remoteUpdate(msg);
} catch (RemoteException e) {
System.out.println(
"Remote exception removing observer:" + this);
e.printStackTrace();
o.deleteObserver(this);
}
}
/**
 * metodo usato per ottenere la stub del client 
 * @return ritorna la stub del client
 */
public RemoteObserver getObs(){
	return remoteClient;
}
}
