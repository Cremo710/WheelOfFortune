package gameGUI;

import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

/**
 * classe per la definizione del pannello relativo ad ogni singola statistica
 * @author Luca Cremonesi
 * @version 1.0
 */
public class StatPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private JTextField value;
	
	/**
	 * costruttore con quattro argomenti
	 * @param stat indica la stringa che descrive la statistica
	 * @param c indica la lettera
	 * @param phrase indica la frase
	 * @param player indica il soprannome di gioco del giocatore
	 */
	public StatPanel(String stat, String c, String phrase, String player) {
		setBackground(new Color(240, 230, 140));
		setBorder(new LineBorder(Color.GRAY, 2, true));
		setLayout(null);
		
		JLabel statL = new JLabel(stat);
		statL.setBounds(10, 11, 248, 21);
		add(statL);
		
		JLabel phraseL = new JLabel("Frase:");
		phraseL.setBounds(10, 76, 31, 14);
		add(phraseL);
		
		JTextField Ctxt = new JTextField();
		if(c.length()>0) {
			Ctxt.setText("" + c.charAt(0));
		} else {
			Ctxt.setText("");
		}
		Ctxt.setBackground(Color.WHITE);
		Ctxt.setEditable(false);
		Ctxt.setFont(new Font("Tahoma", Font.PLAIN, 17));
		Ctxt.setHorizontalAlignment(SwingConstants.CENTER);
		Ctxt.setBounds(399, 8, 31, 21);
		add(Ctxt);
		
		JTextField phraseTxt = new JTextField(phrase);
		phraseTxt.setBackground(Color.WHITE);
		phraseTxt.setEditable(false);
		phraseTxt.setHorizontalAlignment(SwingConstants.CENTER);
		phraseTxt.setBounds(57, 73, 373, 20);
		add(phraseTxt);
		setPreferredSize(new Dimension(450, 110));
		
		JLabel playerL = new JLabel("Giocatore:");
		playerL.setBounds(10, 43, 50, 14);
		add(playerL);
		
		JTextField playerTxt = new JTextField(player);
		playerTxt.setFont(new Font("Tahoma", Font.PLAIN, 15));
		playerTxt.setHorizontalAlignment(SwingConstants.CENTER);
		playerTxt.setBounds(310, 42, 120, 25);
		add(playerTxt);
	}

	/**
	 * costruttore con due argomenti
	 * @param stat indica la stringa che descrive la statistica
	 * @param x indica il valore associato
	 */
	public StatPanel(String stat, String x) {
		setBackground(new Color(240, 230, 140));
		setBorder(new LineBorder(Color.GRAY, 2, true));
		setLayout(null);
		
		JLabel st = new JLabel(stat);
		st.setBounds(10, 11, 284, 26);
		add(st);
		
		value = new JTextField(x);
		value.setBackground(Color.WHITE);
		value.setHorizontalAlignment(SwingConstants.CENTER);
		value.setFont(new Font("Tahoma", Font.PLAIN, 15));
		value.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		value.setEditable(false);
		value.setBounds(310, 10, 120, 25);
		
		add(value);
		setPreferredSize(new Dimension(450, 46));
	}
}
