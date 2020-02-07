package gameGUI;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import game.ClientRdF;
import game.WheelOfFortune;

/**
 * classe per la visualizzazione del frame relativo alla schermata di gioco per i giocatori che partecipano alla partita
 * @author Luca Cremonesi
 * @version 1.0
 */
public class WheelFrame{
	private static JFrame frame;
	
	/**
	 * costruttore
	 * @throws UnsupportedAudioFileException per la gestione delle eccezioni
	 * @throws IOException per la gestione delle eccezioni
	 * @throws LineUnavailableException per la gestione delle eccezioni
	 */
	public WheelFrame() throws UnsupportedAudioFileException, 
    IOException, LineUnavailableException {
		String owner = ClientRdF.getSt().getFirstTurn(ClientRdF.idGame);
		String[] ownerData = ClientRdF.getSt().getData(owner);
		String ownerNick = ownerData[2];
		initialize(ownerNick);
		if(ClientRdF.email.equals(owner)) {
			WheelPanel.wheelstart(true);
			WheelPanel.setWheelState("Partita iniziata!\nTi è stato assegnato il turno!");
			WheelPanel.startTimer(5);
		} else {
			WheelPanel.wheelstart(false);
			WheelPanel.setWheelState("Partita iniziata!\nIl turno è in mano a " + ownerNick);
		}
	}
	
	/**
	 * metodo utilizzato per inizializzare il frame
	 * @param nick indica il soprannome di gioco del giocatore
	 * @throws UnsupportedAudioFileException per la gestione delle eccezioni
	 * @throws IOException per la gestione delle eccezioni
	 * @throws LineUnavailableException per la gestione delle eccezioni
	 */
	private void initialize(String nick) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		frame = new JFrame("Schermata di Gioco");
		frame.setLayout(new FlowLayout());
		frame.getContentPane().setBackground(new Color(155, 248, 214));
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(1050, 720); //dimensioni finestra di gioco
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
		
		WheelOfFortune game = new WheelOfFortune();
		PhrasePanel frase = new PhrasePanel(game, nick);
		FirstPanel firstP = new FirstPanel(game, nick);
		WheelPanel ruota = new WheelPanel(game, firstP, frase, this);
		
		try {
            BufferedImage image = ImageIO.read(new File("immagini/IconaRDF.png"));
            frame.setIconImage(image);
		} catch (IOException e) {
            e.printStackTrace();
		}
		
		frame.add(firstP);
		frame.add(frase);
		frame.add(ruota);
		
		frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
            	frame.dispose();
            	WheelPanel.stopWheelTimer();
            	WheelPanel.stopTimer();
            	WheelPanel.stopTimerObs();
            	WheelPanel.stopMancheTimerObs();
            	try {
            		ClientRdF.updateMove(WheelOfFortune.getManche(),ClientRdF.email, 0, "esce");
            		ClientRdF.endGame();
            	} catch (RemoteException e1) {
            		e1.printStackTrace();
            	}
            	JOptionPane.getRootFrame().dispose();
            	new PlayerFrame();
            }
        });
        frame.setVisible(true);
	}
	
	/**
	 * metodo utilizzato per disporre la schermata
	 */
	public static void disposeFrame() {
		frame.dispose();
	}
}
