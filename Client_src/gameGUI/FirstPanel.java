package gameGUI;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.rmi.RemoteException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
//import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import game.ClientRdF;
import game.WheelOfFortune;

/**
 * classe per la definizione del pannello superiore che compone la schermata di gioco
 * @author Luca Cremonesi
 * @version 1.0
 */
public class FirstPanel  extends JPanel{
	private static final long serialVersionUID=1;
	
	private static final Font FONT = new Font("Tahoma", Font.PLAIN, 20);
	
	private WheelOfFortune game;
	
	private JLabel pointsL, bankL, nickL, bonusL;

    private JTextField pointsF, bankF;

	private static JTextField nickF;

	private JTextField bonusF;
    
	/**
	 * costruttore
	 * @param game indica il riferimento alla classe WheelOfFortune e quindi alla partita
	 * @param nick indica il soprannome dell'utente
	 */
    public FirstPanel(WheelOfFortune game, String nick){
    	super();
    	
    	this.game = game;
    	
    	String ownerNick = nick;
    	
    	bonusL = new JLabel(new ImageIcon("immagini/bonus_img.png"));
    	bankL = new JLabel(new ImageIcon("immagini/depos.png"));
    	pointsL = new JLabel(new ImageIcon("immagini/points.png"));
    	nickL = new JLabel("Nickname:", JLabel.RIGHT);
    	nickL.setFont(FONT);
    	
    	bonusF = new JTextField("" + this.game.getBonus());
        bonusF.setEditable(false); 
        bonusF.setBackground(Color.WHITE);
        bonusF.setHorizontalAlignment(SwingConstants.CENTER);
        bankF = new JTextField("$" + this.game.getTotScore());
        bankF.setEditable(false);
        bankF.setBackground(Color.WHITE);
        bankF.setHorizontalAlignment(SwingConstants.CENTER);
    	pointsF = new JTextField("$ " + this.game.getScore());
    	pointsF.setEditable(false);
    	pointsF.setBackground(Color.WHITE);
    	pointsF.setHorizontalAlignment(SwingConstants.CENTER);
    	try {
    		String[] data = ClientRdF.getSt().getData(ClientRdF.email);
			nickF = new JTextField(data[2]);
			if(ownerNick.equals(data[2])) {
	    		nickF.setForeground(Color.GREEN);
	    	} 
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    	
        nickF.setEditable(false);
        nickF.setBackground(Color.WHITE);
        nickF.setHorizontalAlignment(SwingConstants.CENTER);
        nickF.setMaximumSize(new Dimension(120, 30));
    	nickF.setPreferredSize(new Dimension(120,30));
        
        bonusF.setFont(FONT);
    	bankF.setFont(FONT);
    	nickF.setFont(FONT);
    	pointsF.setFont(FONT);
    	
    	setBackground(new Color(155, 248, 214));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(Box.createHorizontalStrut(50));
        add(nickL);
        add(Box.createHorizontalStrut(10));
        add(nickF);
        add(Box.createHorizontalStrut(100));
        add(bonusL);
        add(Box.createHorizontalStrut(10));
        add(bonusF);
        add(Box.createHorizontalStrut(100));
        add(bankL);
        add(Box.createHorizontalStrut(10));
        add(bankF);
        add(Box.createHorizontalStrut(100));
        add(pointsL);
        add(Box.createHorizontalStrut(10));
        add(pointsF);
        add(Box.createHorizontalStrut(20));
        setPreferredSize(new Dimension(900, 30));
    }
    
    /**
     * metodo utilizzato per ritornare il punteggio
     * @return il punteggio ritornato dal metodo getScore della classe WheelOfFortune
     */
    public int getScore() {
    	return game.getScore();
    }
    
    /**
     * metodo utilizzato per incrementare il punteggio sia nel pannello che nell'oggetto 'game' relativo alla partita
     * @param score indica il punteggio da aggiungere
     */
    public void scoreAdder(int score) {
        game.addScore(score);
        pointsF.setText("$" + game.getScore());
    }
    
    /**
     * metodo utilizzato per incrementare il punteggio totale(deposito partita) sia nel pannello che nell'oggetto 'game' relativo alla partita
     * @param score indica il punteggio da aggiungere
     */
    public void addTotScore(int score) {
    	game.addTotScore(score);
    	bankF.setText("$" + game.getTotScore());
    }
    
    /**
     * metodo utilizzato per ottenere il punteggio totale(deposito partita)
     * @return il punteggio ritornato dal metodo getTotScore della classe WheelOfFortune
     */
    public int getTotScore() {
    	return game.getTotScore();
    }

    /**
     * metodo utilizzato per resettare il punteggio sia nel pannello che nell'oggetto 'game' relativo alla partita
     */
    public void resetScore() {
        game.resetScore();
        pointsF.setText("$" + game.getScore());
    }
    
    /**
     * metodo set per impostare il soprannome dell'utente nel pannello 
     * @param nick indica il soprannome dell'utente
     */
    public void setNickname(String nick) {
    	nickF.setText(nick);
    }
    
    /**
     * metodo set per impostare il colore del soprannome dell'utente nel pannello
     * @param c indica il colore
     */
    public static void setNickForeground(Color c) {
    	nickF.setForeground(c);
    }
    
    /**
     * metodo utilizzato per incrementare il numero di jolly nel pannello e nell'oggetto 'game' relativo alla partita
     */
    public void aggiungiBonus() {
    	game.addBonus();
    	bonusF.setText("" + game.getBonus());
    }
    
    /**
     * metodo utilizzato per diminuire il numero di jolly nel pannello e nell'oggetto 'game' relativo alla partita
     */
    public void diminuisciBonus() {
    	game.removeBonus();
    	bonusF.setText("" + game.getBonus());
    }
}

