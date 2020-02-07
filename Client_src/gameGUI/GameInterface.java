package gameGUI;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import java.awt.Font;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.SwingConstants;

/**
 * classe per la definizione del pannello relativo ad ogni partita che compone la schermata dell'elenco partite
 * @author Luca Cremonesi
 * @version 1.0
 */
public class GameInterface extends JPanel{

	private static final long serialVersionUID = 1L;

	/**
	 * costruttore
	 * @param id indica l'identificativo della partita
	 * @param n indica il numero di giocatori
	 * @param manager indica il giocatore che ha creato la partita
	 */
	public GameInterface(long id, int n, String manager) {
		Border style = BorderFactory.createLineBorder(Color.CYAN);
		Border border = BorderFactory.createEtchedBorder(Color.CYAN.darker(), Color.CYAN.darker());
		Border finalStyle = BorderFactory.createCompoundBorder(border, style);
		
		JLabel idGameL = new JLabel("ID: " + id, JLabel.CENTER);
		idGameL.setBounds(10, 11, 76, 28);
		idGameL.setBackground(new Color(0, 255, 255));
		idGameL.setForeground(new Color(0, 0, 0));
		idGameL.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 14));
		idGameL.setOpaque(true);
		idGameL.setBackground(Color.CYAN);
		idGameL.setBorder(finalStyle);
		
		JLabel playerL = new JLabel("Creata da: " + manager, JLabel.CENTER);
		playerL.setBounds(183, 53, 219, 29);
		playerL.setBackground(new Color(0, 255, 255));
		playerL.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 15));
		playerL.setForeground(new Color(255, 69, 0));
		playerL.setOpaque(true);
		playerL.setBorder(finalStyle);
		
		JLabel nPlayersL = new JLabel(n +"/3", JLabel.CENTER);
		nPlayersL.setBounds(539, 10, 41, 29);
		nPlayersL.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 15));
		nPlayersL.setBackground(new Color(0, 255, 255));
		nPlayersL.setOpaque(true);
		nPlayersL.setHorizontalAlignment(SwingConstants.CENTER);
		nPlayersL.setBorder(finalStyle);
		setLayout(null);
		
		add(idGameL);
		add(playerL);
		add(nPlayersL);
		
		setPreferredSize(new Dimension(590, 150));
		setBackground(Color.BLUE.darker());
	}
}
