package gameGUI;


import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import game.AdminRdF;
import game.PopUp;

/**
 * classe per la visualizzazione del frame relativo alla schermata amministratore principale
 * @author Luca Cremonesi
 * @version 1.0
 */
public class AdminFrame extends JFrame{
	private static final long serialVersionUID=1;

	/**
	 * costruttore con un argomento
	 * @param nickname indica il soprannome del giocatore che ha deciso di uscire dalla partita in corso
	 */
	public AdminFrame(String nickname) { //passo il giocatore che ha terminato e lo stampo
	    new AdminFrame();
	    PopUp p = new PopUp("Partita Terminata", "Ci dispiace, il giocatore " + nickname + " \nè uscito dalla partita", Color.RED, null);
	    p.add();
	  }

	/**
	 * costruttore senza argomenti
	 */
	public AdminFrame() {
		super("Schermata Iniziale Admin");

		AdminPanel admin = new AdminPanel(this);

		try {
			BufferedImage image = ImageIO.read(new File("immagini/IconaRDF.png"));
			setIconImage(image);
		} catch (IOException e) {
			e.printStackTrace();
		}

		setLayout(null);
		admin.setBounds(0,0,600,530);
		add(admin);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e)
			{
				int r = JOptionPane.showConfirmDialog(null, "Vuoi davvero uscire?");
				if(r == 0) {
					dispose();
					try {
						AdminRdF.setOnline(false);
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
					System.exit(1);
				} 
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 500); //dimensioni finestra
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}

}

