package gameGUI;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import game.ClientRdF;

/**
 * classe per la visualizzazione del frame relativo alla schermata utente principale
 * @author Luca Cremonesi
 * @version 1.0
 */
public class PlayerFrame extends JFrame{
	private static final long serialVersionUID=1;

	/**
	 * costruttore
	 */
	public PlayerFrame() {
		super("Schermata Principale");

		PlayerPanel player = new PlayerPanel(this);

		try {
			BufferedImage image = ImageIO.read(new File("immagini/IconaRDF.png"));
			setIconImage(image);
		} catch (IOException e) {
			e.printStackTrace();
		}

		setLayout(null);
		player.setBounds(0,0,600,530);
		add(player);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e)
			{
				int r = JOptionPane.showConfirmDialog(null, "Vuoi davvero uscire?");
				if(r == 0) {
					dispose();
					try {
						ClientRdF.setOnline(false);
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
					System.exit(1);
				} 
			}
		});
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setSize(600, 500); //dimensioni finestra
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
}
