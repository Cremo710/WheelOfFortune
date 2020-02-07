package game;
import java.io.Serializable;

/**
 * la classe QueryReturn rappresenta il risultato di una query (usata per le statistiche)
 * @author Andrea Peluso
 * @version 1.0
 */
public class QueryReturn implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private float score;
	private String owner;
	private String name;
	private String letter;
	
	/**
	 * costruttore della classe
	 * @param score indica il punteggio 
	 * @param owner indica l'utente
	 * @param name indica il nome
	 */
	public QueryReturn(float score,String owner,String name){
		this.score=score;
		this.owner=owner;
		this.name=name;
	}
	
	/**
	 * costruttore della classe
	 * @param letter indica la lettera
	 * @param owner indica l'utente
	 * @param name indica il nome
	 */
	public QueryReturn(String letter,String owner,String name){
		this.letter=letter;
		this.owner=owner;
		this.name=name;
	}

	/**
	 * metodo usato per ritornare il punteggio
	 * @return indica il punteggio
	 */
	public float getScore() {
		return score;
	}

	/**
	 * metodo usato per settare il punteggio
	 * @param score indica il punteggio
	 */
	public void setScore(float score) {
		this.score = score;
	}

	/**
	 * metodo usato per ottenere il nome dell'utente
	 * @return indica il nome dell'utente
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * metodo usato per settare il nome dell'utente
	 * @param owner indica il nome dell'utente
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * metodo usato per ritornare il nome 
	 * @return indica il nome
	 */
	public String getName() {
		return name;
	}

	/**
	 * metodo usato per settare il nome
	 * @param name indica il nome
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * metodo usato per ritornare la lettera
	 * @return indica la lettera
	 */
	public String getLetter() {
		return letter;
	}

	/**
	 * metodo usato per settare la lettera
	 * @param letter indica la lettera
	 */
	public void setLetter(String letter) {
		this.letter = letter;
	}
	
	
}
