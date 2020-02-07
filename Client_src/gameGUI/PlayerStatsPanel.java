package gameGUI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.rmi.RemoteException;
import java.awt.Color;
import java.awt.Container;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import game.ClientRdF;

/**
 * classe per la definizione del pannello relativo alle statistiche di gioco relative all'utente 
 * @author Luca Cremonesi
 * @version 1.0
 */
public class PlayerStatsPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	
	public PlayerStatsPanel() {
		try {
			initialize();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * metodo utilizzato per inizializzare il pannello
	 * @throws RemoteException per la gestione delle eccezioni
	 */
	private void initialize() throws RemoteException {		
		Container cont = new Container();
		cont.setSize(320, 140);
		cont.setLocation(0, 0);
		cont.setLayout(new GridLayout());		
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.CYAN);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWeights = new double[]{1.0};
		panel.setLayout(new GridBagLayout());
		
		JScrollPane scroll = new JScrollPane(panel);
		scroll.getVerticalScrollBar().setUnitIncrement(16);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		GridBagConstraints g = new GridBagConstraints();
		g.insets = new Insets(10, 0, 0, 0);
		g.gridx=0;
		g.gridy=0;
		StatPanel st_1 = new StatPanel("Manche giocate", "" + ClientRdF.getSt().statistics9(ClientRdF.email));
		panel.add(st_1, g);
		g.gridy = 1;
		g.insets = new Insets(10, 0, 0, 0);
		StatPanel st_2 = new StatPanel("Partite giocate", "" + ClientRdF.getSt().statistics10(ClientRdF.email));
		panel.add(st_2, g);
		g.gridy = 2;
		StatPanel st_3= new StatPanel("Manche osservate",  "" + ClientRdF.getSt().statistics11(ClientRdF.email));
		panel.add(st_3, g);
		g.gridy = 3;
		StatPanel st_4 = new StatPanel("Partite osservate", "" + ClientRdF.getSt().statistics12(ClientRdF.email));
		panel.add(st_4, g);
		g.gridy = 4;
		StatPanel st_5= new StatPanel("Manche vinte",  "" + ClientRdF.getSt().statistics13(ClientRdF.email));
		panel.add(st_5, g);
		g.gridy = 5;
		StatPanel st_6 = new StatPanel("Partite vinte", "" + ClientRdF.getSt().statistics14(ClientRdF.email));
		panel.add(st_6, g);
		g.gridy = 6;
		StatPanel st_7= new StatPanel("Punteggio medio vinto",  "" + ClientRdF.getSt().statistics15(ClientRdF.email));
		panel.add(st_7, g);
		g.gridy = 7;
		StatPanel st_8 = new StatPanel("Numero medio passaggi di turno per manche", "" + ClientRdF.getSt().statistics16(ClientRdF.email));
		panel.add(st_8, g);
		g.gridy = 8;
		StatPanel st_9= new StatPanel("Numero medio passaggi di turno per partita",  "" + ClientRdF.getSt().statistics17(ClientRdF.email));
		panel.add(st_9, g);
		g.gridy = 9;
		StatPanel st_10= new StatPanel("Numero medio di PERDE per manche",  "" + ClientRdF.getSt().statistics18(ClientRdF.email));
		panel.add(st_10, g);
		g.gridy = 10;
		StatPanel st_11 = new StatPanel("Numero medio di PERDE per partita",  "" + ClientRdF.getSt().statistics19(ClientRdF.email));
		g.insets = new Insets(10, 0, 10, 0);
		panel.add(st_11, g);
		
		cont.add(scroll);

		add(cont);
		setSize(320, 140);
		setLocation(0,0);
		setVisible(true);
		setLayout(new GridLayout());
	}
}
