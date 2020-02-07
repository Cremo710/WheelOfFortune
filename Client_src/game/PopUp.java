package game;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * classe per la creazione di messaggi di avviso durante il gioco
 * @author Luca Cremonesi
 * @version 1.0
 */
public class PopUp {
	private JDialog d;
	private JLabel title;
	private JLabel body;
	
	/**
	 * costruttore
	 * @param t indica il titolo del PopUp
	 * @param msg indica il messaggio del PopUp
	 * @param col indica il colore del titolo
	 * @param ref indica il frame di riferimento
	 */
	public PopUp(String t, String msg, Color col, Component ref) {
		d=new JDialog();
		d.setSize(400, 150);
		title = new JLabel(t);
		title.setFont(new Font("Open Sans Light", Font.BOLD, 15));
		title.setForeground(col);
		title.setBounds(130,10,200,20);
		body =new JLabel("<html><p align=\"center\">" + msg.replaceAll("\n", "<br/>") + "</p></html>", SwingConstants.CENTER);
		body.setBounds(50,50,30,20);
		d.setModal(false);
		d.setAlwaysOnTop(true);
		d.setResizable(false);
		d.setVisible(true);
		d.setLocationRelativeTo(ref);
	}
	
	/**
	 * metodo utilizzato per rendere visibile il PopUp creato
	 */
	public void add() {
		d.add(title);
		d.add(body);
	}
	
	/**
	 * metodo utilizzato per impostare le dimensioni del PopUp
	 * @param width indica la larghezza
	 * @param height indica l'altezza
	 */
	public void setSize(int width, int height) {
		d.setSize(width, height);
	}
	 /**
	  * metodo utilizzato per impostare la posizione e le dimensioni del PopUp
	  * @param x indica il valore corrispondente all'ascissa
	  * @param y indica il valore corrispondente all'ordinata
	  * @param width indica la larghezza
	  * @param height indica l'altezza
	  */
	public void setBodyBounds(int x, int y, int width, int height) {
		body.setBounds(x, y, width, height);
	}
	
	/**
	 * metodo utilizzato per impostare la posizione e le dimensioni del titolo del PopUp
	 * @param x indica il valore corrispondente all'ascissa
	 * @param y indica il valore corrispondente all'ordinata
	 * @param width indica la larghezza
	 * @param height indica l'altezza
	 */
	public void setTitleBounds(int x, int y, int width, int height) {
		title.setBounds(x, y, width, height);
	}
}
