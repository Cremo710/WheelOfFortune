package gameGUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.border.*;

import game.ClientRdF;
import game.WheelOfFortune;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;;

/**
 * classe per la definizione del pannello per la frase misteriosa che compone la schermata di gioco
 * @author Luca Cremonesi
 * @version 1.0
 */
public class PhrasePanel  extends JPanel{
	private static final long serialVersionUID=1;
	
	private static final Font FONT = new Font("Tahoma", Font.PLAIN, 20);
	
	private static final int LARGHEZZA = 35, ALTEZZA = 45,
            SPAZIO = 2, LARGHEZZA_FRASE = 15 * LARGHEZZA,
            ALTEZZA_FRASE = 4 * ALTEZZA;
	
	private WheelOfFortune game;
	
	private JLabel points1L, points2L, bank1L, bank2L, bonus1L, bonus2L ,nick1L, nick2L; 
	
	private static JTextField points1F, points2F, bank1F, bank2F, nick1F, nick2F, bonus1F, bonus2F;
	
    private JLabel category;
    
    private ArrayList<String> otherPlayers;
    
    /**
     * costruttore
     * @param g indica il riferimento all'oggetto di tipo WheelOfFortune
     * @param nickname indica il soprannome del giocatore
     */
    public PhrasePanel(WheelOfFortune g, String nickname) {
    	super();
    	
    	this.game = g;
    	
    	String ownerNick = nickname;
    	try {
    		String[] data = ClientRdF.getSt().getData(ClientRdF.email);
			String nick = data[2];
			otherPlayers = ClientRdF.getSt().otherPlayerList(ClientRdF.idGame, nick);
		} catch (RemoteException e) {
			e.printStackTrace();
		}    	
    	
    	bonus1L = new JLabel(new ImageIcon("immagini/bonus_img.png"));
    	bank1L = new JLabel(new ImageIcon("immagini/depos.png"));
    	points1L = new JLabel(new ImageIcon("immagini/points.png"));
    	nick1L = new JLabel("Player:", JLabel.RIGHT);
    	nick1L.setFont(FONT);
    	
    	bonus2L = new JLabel(new ImageIcon("immagini/bonus_img.png"));
    	bank2L = new JLabel(new ImageIcon("immagini/depos.png"));
    	points2L = new JLabel(new ImageIcon("immagini/points.png"));
    	nick2L = new JLabel("Player:", JLabel.RIGHT);
    	nick2L.setFont(FONT);    	
    	
    	category = new JLabel();
    	category.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
    	category.setForeground(Color.GREEN.darker());
    	category.setLocation(450, 0);
    	category.setBackground(Color.WHITE);
    	category.setOpaque(true);
    	Border raisedbevel = BorderFactory.createRaisedBevelBorder();
    	Border loweredbevel = BorderFactory.createLoweredBevelBorder();
    	Border compound = BorderFactory.createCompoundBorder(raisedbevel, loweredbevel);
    	category.setBorder(compound);
    	
    	bonus1F = new JTextField("0");
        bonus1F.setEditable(false);
        bonus1F.setBackground(Color.WHITE);
        bonus1F.setHorizontalAlignment(SwingConstants.CENTER);
        bank1F = new JTextField("$0");
        bank1F.setEditable(false);
        bank1F.setBackground(Color.WHITE);
        bank1F.setHorizontalAlignment(SwingConstants.CENTER);
    	points1F = new JTextField("$0");
    	points1F.setEditable(false);
    	points1F.setBackground(Color.WHITE);
    	points1F.setHorizontalAlignment(SwingConstants.CENTER);
    	nick1F = new JTextField("" + otherPlayers.get(0)); //il server deve tornare i nomi degli altri 2 players --> qui metto il nick di uno dei due
    	if(ownerNick.equals(otherPlayers.get(0))) {
    		nick1F.setForeground(Color.GREEN);
    	}
    	nick1F.setMinimumSize(new Dimension(120,30));
    	nick1F.setMaximumSize(new Dimension(120, 30));
    	nick1F.setPreferredSize(new Dimension(120,30));
    	nick1F.setEditable(false);
    	nick1F.setBackground(Color.WHITE);
    	nick1F.setHorizontalAlignment(SwingConstants.CENTER);
        
        bonus1F.setFont(FONT);
    	bank1F.setFont(FONT);
    	nick1F.setFont(FONT);
    	points1F.setFont(FONT);
    	
    	bonus2F = new JTextField("0");
        bonus2F.setEditable(false); 
        bonus2F.setBackground(Color.WHITE);
        bonus2F.setHorizontalAlignment(SwingConstants.CENTER);
        bank2F = new JTextField("$0");
        bank2F.setEditable(false);
        bank2F.setBackground(Color.WHITE);
        bank2F.setHorizontalAlignment(SwingConstants.CENTER);
    	points2F = new JTextField("$0");
    	points2F.setEditable(false);
    	points2F.setBackground(Color.WHITE);
    	points2F.setHorizontalAlignment(SwingConstants.CENTER);
    	nick2F = new JTextField("" + otherPlayers.get(1)); //il server deve tornare i nomi degli altri 2 players --> qui metto il nick di uno dei due
    	if(ownerNick.equals(otherPlayers.get(1))) {
    		nick2F.setForeground(Color.GREEN);
		}
    	nick2F.setMinimumSize(new Dimension(120,30));
    	nick2F.setMaximumSize(new Dimension(120, 30));
    	nick2F.setPreferredSize(new Dimension(120,30));
    	nick2F.setEditable(false);
    	nick2F.setBackground(Color.WHITE);
    	nick2F.setHorizontalAlignment(SwingConstants.CENTER);
    	
    	bonus2F.setFont(FONT);
    	bank2F.setFont(FONT);
    	nick2F.setFont(FONT);
    	points2F.setFont(FONT);
    	
    	Box player1LabelBox = Box.createVerticalBox(); //blocco di JLabel per il player1
    	player1LabelBox.add(nick1L);
    	player1LabelBox.add(Box.createVerticalStrut(10));
    	player1LabelBox.add(bonus1L);
    	player1LabelBox.add(Box.createVerticalStrut(10));
    	player1LabelBox.add(bank1L);
    	player1LabelBox.add(Box.createVerticalStrut(10));
    	player1LabelBox.add(points1L);
    	
    	Box player1TextBox = Box.createVerticalBox(); //blocco di JTextField per il player1
    	player1TextBox.add(nick1F);
    	player1TextBox.add(Box.createVerticalStrut(10));
    	player1TextBox.add(bonus1F);
    	player1TextBox.add(Box.createVerticalStrut(10));
    	player1TextBox.add(bank1F);
    	player1TextBox.add(Box.createVerticalStrut(10));
    	player1TextBox.add(points1F);
    	
    	
    	Box player2LabelBox = Box.createVerticalBox(); //blocco di JLabel per il player2
    	player2LabelBox.add(nick2L);
    	player2LabelBox.add(Box.createVerticalStrut(10));
    	player2LabelBox.add(bonus2L);
    	player2LabelBox.add(Box.createVerticalStrut(10));
    	player2LabelBox.add(bank2L);
    	player2LabelBox.add(Box.createVerticalStrut(10));
    	player2LabelBox.add(points2L);
    	
    	Box player2TextBox = Box.createVerticalBox(); //blocco di JTextField per il player2
    	player2TextBox.add(nick2F);
    	player2TextBox.add(Box.createVerticalStrut(10));
    	player2TextBox.add(bonus2F);
    	player2TextBox.add(Box.createVerticalStrut(10));
    	player2TextBox.add(bank2F);
    	player2TextBox.add(Box.createVerticalStrut(10));
    	player2TextBox.add(points2F);
    	
    	Box playersBox = Box.createHorizontalBox();
    	playersBox.add(Box.createHorizontalStrut(10));
    	playersBox.add(player1LabelBox);
    	playersBox.add(Box.createHorizontalStrut(10));
    	playersBox.add(player1TextBox);
    	playersBox.add(Box.createHorizontalStrut(590));
    	playersBox.add(player2LabelBox);
    	playersBox.add(Box.createHorizontalStrut(10));
    	playersBox.add(player2TextBox);
    	
    	Box categoryBox = Box.createHorizontalBox();
    	categoryBox.add(category);
    	
    	Box finalBox = Box.createVerticalBox();
        finalBox.add(Box.createVerticalStrut(20));
        finalBox.add(playersBox);
        finalBox.add(Box.createVerticalStrut(40));
        finalBox.add(categoryBox);
        
        add(finalBox);
        setBackground(new Color(155, 248, 214));
        setPreferredSize(new Dimension(1000, 265));
    }
    
    /**
     * metodo che richiama il metodo newPhrase della classe WheelOfFortune ed imposta l'indizio
     */
    public void newGame() {
    	game.newPhrase();
        category.setText(game.getCategory());
        
        repaint();
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        String frase = game.getPhrase();         

        // Disegna lo spazio per ogni lettera
        for (int i = 0; i < frase.length(); ++i) {
            int row = i / 15; //ci sono righe da 15 colonne 
            int col = i % 15; //ci sono 15 colonne 

            paintLetterBox(g, row, col, frase.charAt(i) == ' ');

            // Disegna la lettera se è stata indovinata
            if (game.getGuessedLetters().contains(frase.charAt(i))) {
                g.setColor(Color.BLACK);
                g.setFont(new Font("Impatto", Font.PLAIN, 35));
                drawLetter(g, ("" + frase.charAt(i)).toUpperCase(), row, col);
            }
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
        g.fillRect((990 - LARGHEZZA_FRASE) / 2 + col
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
        g.drawString(str, (990 - LARGHEZZA_FRASE) / 2 + col
            * (LARGHEZZA + SPAZIO) + LARGHEZZA / 8,
            (getHeight() - ALTEZZA_FRASE) / 3 + (row + 1)
                * (ALTEZZA + SPAZIO) - ALTEZZA / 6); //stampa la stringa str nella casella corrispondente ai numeri di rige e colonne specificati
    }
    
    /**
     * metodo che richiama i metodi resetPoints1F e resetPoints2F
     */
    public static void resetPoints() {
    	resetPoints1F();
    	resetPoints2F();
    }
    
    /**
     * metodo utilizzato per impostare il colore VERDE per il testo relativo al soprannome del giocatore che ha il possesso del turno
     * @param x indica il numero relativo al giocatore che ha il turno
     */
    public static void setTurn(int x) {
    	if(x==1) {
    		nick1F.setForeground(Color.GREEN);
    		nick2F.setForeground(Color.BLACK);
    	} else if(x==2) {
    		nick1F.setForeground(Color.BLACK);
    		nick2F.setForeground(Color.GREEN);
    	} else if(x==0) {
    		nick1F.setForeground(Color.BLACK);
    		nick2F.setForeground(Color.BLACK);
    	}
    }
    
    /**
     * metodo utilizzato per ottenere il contenuto dell'oggetto JTextField 'nick1F'
     * @return il testo contenuto in 'nick1F'
     */
    public static String getNick1(){
    	return nick1F.getText();
    }
    
    /**
     * metodo utilizzato per ottenere il contenuto dell'oggetto JTextField 'nick2F'
     * @return il testo contenuto in 'nick2F'
     */
    public static String getNick2(){
    	return nick2F.getText();
    }
    
    /**
     * metodo utilizzato per resettare il valore contenuto nell'oggetto JTextField 'points1F'
     */
    public static void resetPoints1F() {
    	points1F.setText("$0");
    }
    
    /**
     * metodo utilizzato per resettare il valore contenuto nell'oggetto JTextField 'points2F'
     */
    public static void resetPoints2F() {
    	points2F.setText("$0");
    }

    /**
     * metodo utilizzato per incrementare il valore contenuto nell'oggetto JTextField 'points1F'
     * @param points1f indica il valore da aggiungere
     */
	public static void setPoints1F(int points1f) {
		int points = Integer.parseInt(points1F.getText().substring(1));
		points1F.setText("$"+ (points + points1f));
	}

	/**
     * metodo utilizzato per incrementare il valore contenuto nell'oggetto JTextField 'points2F'
     * @param points2f indica il valore da aggiungere
     */
	public static void setPoints2F(int points2f) {
		int points = Integer.parseInt(points2F.getText().substring(1));
		points2F.setText("$"+ (points + points2f));
	}

	/**
     * metodo utilizzato per incrementare il valore contenuto nell'oggetto JTextField 'bank1F'
     * @param bank1f indica il valore da aggiungere
     */
	public static void setBank1F(int bank1f) {
		int points = Integer.parseInt(bank1F.getText().substring(1));
		bank1F.setText("$" + (points + bank1f));
	}

	/**
     * metodo utilizzato per incrementare il valore contenuto nell'oggetto JTextField 'bank2F'
     * @param bank2f indica il valore da aggiungere
     */
	public static void setBank2F(int bank2f) {
		int points = Integer.parseInt(bank2F.getText().substring(1));
		bank2F.setText("$"+ (points + bank2f));
	}

	/**
     * metodo utilizzato per incrementare il valore contenuto nell'oggetto JTextField 'bonus1F'
     * @param i indica il valore da aggiungere
     */
	public static void addBonus1F(int i) {
		int b = Integer.parseInt(bonus1F.getText());
		bonus1F.setText("" + (b+i));;
	}

	/**
     * metodo utilizzato per incrementare il valore contenuto nell'oggetto JTextField 'bonus2F'
     * @param i indica il valore da aggiungere
     */
	public static void addBonus2F(int i) {
		int b = Integer.parseInt(bonus2F.getText());
		bonus2F.setText("" + (b+i));;
	}
}
