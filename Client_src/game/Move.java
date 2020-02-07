package game;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * classe per la memorizzazione delle mosse che un utente può effettuare durante il gioco
 * @author Andrea Peluso
 * @version 1.0
 * */
public class Move implements Serializable{
	private static final long serialVersionUID = 1L;
	private String type;
	private String owner;
	private int points;
	private ArrayList<String> to;

	/**
	 * costruttore
	 * @param type indica il tipo di mossa
	 * @param owner indica l'autore della mossa
	 * @param points indica il punteggio relativo alla mossa
	 * @param to indic la lista dei giocatori/osservatori che ricevono la notifica
	 */
	public Move(String type,String owner,int points,ArrayList<String> to){
		this.type=type;
		this.owner=owner;
		this.points=points;
		this.to=to;
	}
	
	/**
	 * metodo get per il tipo della mossa
	 * @return tipo della mossa
	 */
	public String getType() {
		return type;
	}

	/**
	 * metodo set per il tipo della mossa
	 * @param type indica il tipo della mossa
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * metodo get per l'autore della mossa
	 * @return autore della mossa
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * metodo set per l'autore della mossa
	 * @param owner indica l'autore della mossa
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * metodo get per il punteggio relativo alla mossa
	 * @return punteggio della mossa
	 */
	public int getPoints() {
		return points;
	}

	/**
	 * metodo set per il punteggio della mossa
	 * @param points indica il punteggio
	 */
	public void setPoints(int points) {
		this.points = points;
	}

	/**
	 * metodo get per la lista dei giocatori/osservatori che ricevono la notifica
	 * @return lista giocatori/osservatori
	 */
	public ArrayList<String> getTo() {
		return to;
	}

	/**
	 * metodo set per la lista dei giocatori/osservatori che ricevono la notifica
	 * @param to indica la lista giocatori/oservatori
	 */
	public void setTo(ArrayList<String> to) {
		this.to = to;
	}

}
