package gameGUI;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.rmi.RemoteException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import game.ClientRdF;

/**
 * classe per la definizione del pannello relativo alle statistiche di gioco generali 
 * @author Luca Cremonesi
 * @version 1.0
 */
public class GeneralStatsPanel extends JPanel{
	private static final long serialVersionUID = 1L;

	/**
	 * costruttore
	 */
	public GeneralStatsPanel() {
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
		StatPanel st_1 = new StatPanel("Giocatore con punteggio manche max", ClientRdF.getSt().statistics1().getName());
		panel.add(st_1, g);
		g.gridy = 1;
		g.insets = new Insets(10, 0, 0, 0);
		StatPanel st_2 = new StatPanel("Giocatore con punteggio partita max", ClientRdF.getSt().statistics2().getName());
		panel.add(st_2, g);
		g.gridy = 2;
		StatPanel st_3= new StatPanel("Giocatore che ha giocato più manche",  ClientRdF.getSt().statistics3().getName());
		panel.add(st_3, g);
		g.gridy = 3;
		StatPanel st_4 = new StatPanel("Giocatore con media punteggio per manche max",  ClientRdF.getSt().statistics4().getName());
		panel.add(st_4, g);
		g.gridy = 4;
		StatPanel st_5 = new StatPanel("Giocatore che ha commesso più errori",  ClientRdF.getSt().statistics5().getName());
		panel.add(st_5, g);
		g.gridy = 5;
		StatPanel st_6 = new StatPanel("Giocatore che ha pescato PERDE più volte",  ClientRdF.getSt().statistics6().getName());
		panel.add(st_6, g);
		g.gridy = 6;
		StatPanel st_7 = new StatPanel("Media mosse per manche",  "" + ClientRdF.getSt().statistics7());
		panel.add(st_7, g);
		g.gridy = 7;
		String[] data = ClientRdF.getSt().getData(ClientRdF.getSt().statistics8().getOwner());
		StatPanel st_8 = new StatPanel("Consonante che ha conferito il punteggio più alto",  ClientRdF.getSt().statistics8().getLetter() ,ClientRdF.getSt().statistics8().getName(), data[2]);
		g.insets = new Insets(10, 0, 10, 0);
		panel.add(st_8, g);
		
		cont.add(scroll);

		add(cont);
		setSize(320, 140);
		setLocation(0,0);
		setVisible(true);
		setLayout(new GridLayout());
	}
}
