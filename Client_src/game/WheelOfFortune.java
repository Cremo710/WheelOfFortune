package game;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * classe per la creazione e gestione delle partite
 * @author Luca Cremonesi
 * @version 1.0
 */
public class WheelOfFortune {

	private String category;

	private static String phrase;

	private static Set<Character> GuessedLetters;

	private int points, bank;

	private static int manche;

	private int bonus;

	/**
	 * costruttore
	 */
	public WheelOfFortune() {
		points = 0;
		manche = 1;
		bonus = 0;
		bank = 0;
	}

	/**
	 * metodo utilizzato per l'importazione e la formattazione della frase misteriosa per ogni manche di ogni partita
	 */
	public void newPhrase(){    	
		ArrayList<String> phraseList = null;
		try {
			phraseList = ClientRdF.getPhrase();
			category = ClientRdF.getCategory(ClientRdF.idGame);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		//algoritmo di formattazione della frase (la frase è già divisa in 'righe' di lunghezza massima 15(larghezza pannello) che possono contenere più di una parola)
		if(!phraseList.isEmpty()) {
			String tmp = "";
			phrase = "";
			for (String s : phraseList){ //per ogni riga della frase
				if(s.length() == 15) { //se la lunghezza della riga è pari a 15 lettere
					phrase += s; //aggiungo la riga alla variabile che conterrà la frase finale
				}else { //se invece la lunghezza della riga è inferiore a 15 lettere
					//scelgo il numero di spazi da aggiungere all'inizio della riga
					for(int i = 0; i<(15/s.length()); i++) { //tante volte quante il risultato del rapporto tra 15 e la lunghezza della riga
						tmp += " "; //aggiungo uno spazio alla variabile tmp
					}
					tmp += s; //inserisco nella variabile tmp la riga con l'aggiunta degli spazi iniziali
					//aggiungo gli spazi necessari a completare le 15 lettere della riga
					while(tmp.length() < 15) { //fino a quando la lunghezza di tmp non è pari a 15
						tmp += " "; //aggiungo uno spazio alla variabile tmp
					}
					phrase += tmp; //inserisco il contenuto della variabile tmp alla variabile phrase
					tmp = ""; //elimino il contenuto della variabile tmp e ricomincio o termino il ciclo
				}
			}
			phrase = phrase.replaceAll("'", " "); //elimino le ricorrenze delle apostrofi
			phrase = phrase.replaceAll("è", "e");
			phrase = phrase.replaceAll("à", "a");
			phrase = phrase.replaceAll("ì", "i");
			phrase = phrase.replaceAll("ò", "o");
			phrase = phrase.replaceAll("ù", "u");
			phrase = phrase.toUpperCase();
			System.out.println(phrase);
		}
		GuessedLetters = new HashSet<Character>(); //inizializzo l'HashSet delle lettere indovinate
	} 

	/**
	 * metodo utilizzato per mostrare la lettera indovinata sul pannello di gioco
	 * @param lettera indica il carattere indovinato
	 * @return il numero di occorrenze del carattere nella variabile phrase
	 */
	public int revealsLetter(char lettera) {
		if (GuessedLetters.contains(lettera)) {
			return 0;
		}

		GuessedLetters.add(lettera);

		int occorrenze = 0;
		for(int i=0; i<phrase.length(); i++) {
			if(lettera == phrase.charAt(i)) {
				occorrenze += 1;
			}
		}

		return occorrenze;
	}

	/**
	 * metodo utilizzato per verificare la correttezza della frase inserita dall'utente
	 * @return true se il contenuto della variabile 'phrase' corrisponde con i caratteri contenuti in 'GuessedLetters', false nel caso contrario
	 */
	public boolean phraseResolved() {
		for (char c : phrase.toCharArray()) {
			if (!GuessedLetters.contains(c)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * metodo utilizzato per mostrare l'intera frase misteriosa sul pannello di gioco
	 */
	public void revealsPhrase() {
		for (char c : phrase.toCharArray()) {
			GuessedLetters.add(c);
		}
	}
	
	/**
	 * metodo get per la variabile 'category'
	 * @return il contenuto della variabile 'category'
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * metodo get per la variabile 'phrase'
	 * @return il contenuto della variabile 'phrase'
	 */
	public String getPhrase() {
		return phrase;
	}

	/**
	 * metodo get per il set di caratteri 'GuessedLetters'
	 * @return il contenuto di 'GuessedLetters'
	 */
	public Set<Character> getGuessedLetters() {
		return GuessedLetters;
	}

	/**
	 * metodo get per la variabile 'score'
	 * @return il contenuto della variabile 'score'
	 */
	public int getScore() {
		return points;
	}

	/**
     * metodo get per la variabile 'bank'
     * @return il contenuto della variabile 'bank'
     */
	public int getTotScore() {
		return bank;
	}

	/**
     * metodo get per la variabile 'bonus'
     * @return il contenuto della variabile 'bonus'
     */
	public int getBonus() {
		return bonus;
	}

	/**
     * metodo get per la variabile 'manche'
     * @return il contenuto della variabile 'manche'
     */
	public static int getManche() {
		return manche;
	}

	/**
	 * metodo utilizzato per incrementare di 1 il valore della variabile 'manche'
	 */
	public void addManche() {
		manche = manche+1;
	}

	/**
	 * metodo utilizzato per incrementare di 1 il valore della variabile 'bonus'
	 */
	public void addBonus() {
		bonus = bonus+1;
	}

	/**
	 * metodo utilizzato per diminuire di 1 il valore della variabile 'bonus'
	 */
	public void removeBonus() {
		bonus = bonus-1;
	}

	/**
	 * metodo utilizzato per incrementare il valore della variabile 'points'
	 * @param points indica il punteggio da aggiungere
	 */
	public void addScore(int points) {
		this.points += points;
	}
	
	/**
	 * metodo utilizzato per incrementare il valore della variabile 'bank'
	 * @param points indica il punteggio da aggiungere
	 */
	public void addTotScore(int points) {
		bank += points;
	}

	/**
	 * metodo utilizzato per resettare il valore della variabile 'points'
	 */
	public void resetScore() {
		points = 0;
	}

	/**
	 * metodo utilizzato per resettare il valore delle varibili 'manche' e 'points'
	 */
	public void resetValues() {
		manche = 1;
		points = 0;
	}

	/**
	 * metodo utilizzato per verificare che tutte le vocali contenute nella frase misteriosa siano state indovinate
	 * @return true se non ci sono più vocali da indovinare, false nel caso ce ne siano ancora
	 */
	public static boolean isAllVowelsGuessed() {
		return !(phrase.contains("A") && !GuessedLetters.contains('A')
				|| phrase.contains("E") && !GuessedLetters.contains('E')
				|| phrase.contains("I") && !GuessedLetters.contains('I')
				|| phrase.contains("O") && !GuessedLetters.contains('O') 
				|| phrase.contains("U") && !GuessedLetters.contains('U'));
	}

	/**
	 * metodo utilizzato per verificare che tutte le consonanti contenute nella frase misteriosa siano state indovinate
	 * @return true se non ci sono più consonanti da indovinare, false nel caso ce ne siano ancora
	 */
	public static boolean isAllConsonantsGuessed() {
		return !(phrase.contains("Q") && !GuessedLetters.contains('Q')
				|| phrase.contains("R") && !GuessedLetters.contains('R')
				|| phrase.contains("T") && !GuessedLetters.contains('T')
				|| phrase.contains("Y") && !GuessedLetters.contains('Y') 
				|| phrase.contains("P") && !GuessedLetters.contains('P')
				|| phrase.contains("S") && !GuessedLetters.contains('S')
				|| phrase.contains("D") && !GuessedLetters.contains('D')
				|| phrase.contains("F") && !GuessedLetters.contains('F')
				|| phrase.contains("G") && !GuessedLetters.contains('G')
				|| phrase.contains("H") && !GuessedLetters.contains('H')
				|| phrase.contains("J") && !GuessedLetters.contains('J')
				|| phrase.contains("K") && !GuessedLetters.contains('K')
				|| phrase.contains("L") && !GuessedLetters.contains('L')
				|| phrase.contains("Z") && !GuessedLetters.contains('Z')
				|| phrase.contains("X") && !GuessedLetters.contains('X')
				|| phrase.contains("C") && !GuessedLetters.contains('C')
				|| phrase.contains("V") && !GuessedLetters.contains('V')
				|| phrase.contains("B") && !GuessedLetters.contains('B')
				|| phrase.contains("N") && !GuessedLetters.contains('N')
				|| phrase.contains("M") && !GuessedLetters.contains('M'));
	}

}
