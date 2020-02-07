package gameGUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.border.*;

import game.ClientRdF;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * classe per la definizione del pannello per la frase misteriosa che compone la schermata osservatore
 * @author Luca Cremonesi
 * @version 1.0
 */
public class ObserverPhrasePanel  extends JPanel{
	private static final long serialVersionUID=1;

	private static final int LARGHEZZA = 35, ALTEZZA = 45,
			SPAZIO = 2, LARGHEZZA_FRASE = 15 * LARGHEZZA,
			ALTEZZA_FRASE = 4 * ALTEZZA;

	private static JLabel category;

	private static String phrase;

	private static String categ;

	private static Set<Character> GuessedLetters;

	/**
	 * costruttore
	 */
	 public ObserverPhrasePanel() {
		 super(); 

		 newPhrase();

		 category = new JLabel(categ);
		 category.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
		 category.setForeground(Color.GREEN.darker());
		 category.setLocation(450, 0);
		 Border raisedbevel = BorderFactory.createRaisedBevelBorder();
		 Border loweredbevel = BorderFactory.createLoweredBevelBorder();
		 Border compound = BorderFactory.createCompoundBorder(raisedbevel, loweredbevel);
		 category.setBorder(compound);

		 add(Box.createVerticalStrut(470));
		 add(category);

		 setBackground(new Color(155, 248, 214));
		 setPreferredSize(new Dimension(1000, 280));
		 repaint();
	 }

	 /**
	  * metodo utilizzato per stampare l'intero tabellone per la frase misteriosa
	  */
	 public void paintComponent(Graphics g) {
		 super.paintComponent(g);

		 // Disegna lo spazio per ogni lettera
		 for (int i = 0; i < phrase.length(); ++i) {
			 int row = i / 15; //ci sono righe da 15 colonne 
			 int col = i % 15; //ci sono 15 colonne 

			 paintLetterBox(g, row, col, phrase.charAt(i) == ' ');

			 // Disegna la lettera se è stata indovinata
			 if (GuessedLetters.contains(phrase.charAt(i))) {
				 g.setColor(Color.BLACK);
				 g.setFont(new Font("Impatto", Font.PLAIN, 35));
				 drawLetter(g, ("" + phrase.charAt(i)).toUpperCase(), row, col);
			 }
		 }
	 }

	 /**
	  * metodo utilizzato per ottenere una nuova frase dal ServerRdF, che viene formattata ed inserita nel pannello 
	  * adibito alla frase misteriosa nella schermata osservatore
	  */
	 public void newPhrase(){    	
		 ArrayList<String> phraseList = null;
		 GuessedLetters = new HashSet<Character>();
		 try {
			 phraseList = ClientRdF.getPhrase();
			 categ = ClientRdF.getCategory(ClientRdF.idGame);
			 ArrayList<String> letters = ClientRdF.getSt().getLetters(ClientRdF.idGame);
			 if(letters.size()!=0) {
				 for(String s: letters) {
					 char c = s.charAt(0);
					 GuessedLetters.add(c);
				 }
			 }
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
			 phrase = phrase.toUpperCase();
			 phrase = phrase.replaceAll("'", " "); //elimino le ricorrenze delle apostrofi
		 }
		 repaint();
	 }

	 /**
	  * metodo utilizzato per richiamare il metodo newPhrase ed impostare l'indizio nella schermata osservatore
	  */
	 public void newManche() {
		 newPhrase();
		 category.setText(categ);
	 }

	 /**
	  * metodo utilizzato per aggiungere un carattere alla lista delle lettere indovinate
	  * @param c indica il carattere da aggiungere alla lista 'GuessedLetters'
	  */
	 public static void revealsLetter(char c) {
		 GuessedLetters.add(c);
	 }

	 /**
	  * metodo utilizzato per aggiungere tutti i caratteri della frase misteriosa alla lista delle lettere indovinate
	  */
	 public static void revealsPhrase() {
		 for (char c : phrase.toCharArray()) {
			 GuessedLetters.add(c);
		 }
	 }

	 /**
	  * metodo utilizzato per stampare il ogni casella che andrà a comporre il pannello contenente la frase misteriosa
	  * @param g indica il riferimento all'oggetto di tipo Graphics
	  * @param row indica la riga corrispondente (coordinata x)
	  * @param col indica la colonna corrispondente (coordinata y)
	  * @param b indica un valore boolean che si riferisce alla presenza (true) o meno (false) di una lettera nella casella
	  */
	 private void paintLetterBox(Graphics g, int row, int col, boolean b) {
		 g.setColor(b ? Color.GREEN.darker() : Color.WHITE); //se b è true la casella viene colorata di VERDE(scuro), nel caso contrario viene colorata di BIANCO
		 g.fillRect((getWidth() - LARGHEZZA_FRASE) / 2 + col
				 * (LARGHEZZA + SPAZIO), (getHeight() - ALTEZZA_FRASE) / 3
				 + row * (ALTEZZA + SPAZIO), LARGHEZZA, ALTEZZA); //costruisce lo spazio per la casella
	 }

	 /**
	  * metodo utilizzato per stampare una lettera all'interno della casella che la ospita
	  * @param g indica l'oggetto di tipo Graphics
	  * @param str indica la stringa da stampare
	  * @param row indica la riga corrispondente (coordinata x)
	  * @param col indica la colonna corrispondente (coordinata y)
	  */
	 private void drawLetter(Graphics g, String str, int row, int col) {
		 g.drawString(str, (getWidth() - LARGHEZZA_FRASE) / 2 + col
				 * (LARGHEZZA + SPAZIO) + LARGHEZZA / 8,
				 (getHeight() - ALTEZZA_FRASE) / 3 + (row + 1)
				 * (ALTEZZA + SPAZIO) - ALTEZZA / 6); //stampa la stringa str nella casella corrispondente ai numeri di rige e colonne specificati
	 }
}
