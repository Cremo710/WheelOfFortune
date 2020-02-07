package game;
import java.io.Serializable;

/**
 * la classe Phrases per la memorizzazione delle frasi
 * @author Andrea Peluso
 * @version 1.0
 */
public class Phrases implements Serializable{


	private static final long serialVersionUID = 1L;
	private String tema;
	private String corpo;

	/**
	 * costruttore della classe
	 * @param corpo indica il corpo della frase
	 * @param tema indica il tema della frase
	 */
	public Phrases(String corpo,String tema){
	this.corpo=corpo;
	this.tema=tema;
	}
	
	/**
	 * ritorna il tema della frase
	 * @return tema della frase
	 */
	public String getTema() {
		return tema;
	}

	/**
	 * setta il tema della frase
	 * @param tema indica il tema della frase
	 */
	public void setTema(String tema) {
		this.tema = tema;
	}

	/**
	 * ritorna il corpo della frase
	 * @return corpo della frase
	 */
	public String getCorpo() {
		return corpo;
	}

	/**
	 * setta il corpo della frase
	 * @param corpo indica il corpo della frase
	 */
	public void setCorpo(String corpo) {
		this.corpo = corpo;
	}

}
