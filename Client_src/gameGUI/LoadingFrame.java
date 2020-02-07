package gameGUI;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import game.ClientRdF;
import game.PopUp;


/**
 * classe per la visualizzazione e la definizione della schermata di attesa prepartita(lobby) 
 * @author Luca Cremonesi
 * @version 1.0
 */
public class LoadingFrame extends JFrame{
	private static final long serialVersionUID=1;
	
	private static final Font FONT = new Font("Open Sans Light", Font.BOLD, 10);
	
	public static JLabel playersLabel;
	
	public static LoadingFrame frame;

	private JLabel label;
	
	private JButton back;
	
	private static int i;
	
	/**
	 * costruttore
	 * @param n indica il numero di giocatori in attesa
	 */
	public LoadingFrame(int n) {
		super("Schermata di Caricamento");
		i = n;
		
		frame = this;
		
		if(i == 3) {
			if(ClientRdF.player) {
				try {
					new WheelFrame();
				} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
					e.printStackTrace();
				}
			} else if (ClientRdF.observer) {
				try {
					new ObserverFrame(0);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		} else {
			try {
				BufferedImage image = ImageIO.read(new File("immagini/IconaRDF.png"));
				setIconImage(image);
			} catch (IOException e) {
				e.printStackTrace();
			}

			Icon icon = new ImageIcon("gif/loading.gif");

			Border style = BorderFactory.createRaisedBevelBorder();
			Border border = BorderFactory.createEtchedBorder(Color.RED, Color.RED.darker());
			Border finalStyle = BorderFactory.createCompoundBorder(border, style);

			back = new JButton("ESCI");
			back.addMouseListener(new MouseListener() {
			    @Override
			    public void mousePressed(MouseEvent e) {
			    	Border style = BorderFactory.createLoweredBevelBorder();
					Border border = BorderFactory.createEtchedBorder(Color.RED, Color.RED);
					Border finalStyle = BorderFactory.createCompoundBorder(border, style);
					back.setBorder(finalStyle);
			    }
				@Override
				public void mouseClicked(MouseEvent e) {}
				@Override
				public void mouseEntered(MouseEvent e) {}
				@Override
				public void mouseExited(MouseEvent e) {}
				@Override
				public void mouseReleased(MouseEvent e) {
					Border style = BorderFactory.createRaisedBevelBorder();
					Border border = BorderFactory.createEtchedBorder(Color.RED, Color.RED);
					Border finalStyle = BorderFactory.createCompoundBorder(border, style);
					back.setBorder(finalStyle);
					try {
						ClientRdF.exitLobby();
						dispose();
						ClientRdF.removeMe();
						new GameListFrame(ClientRdF.gameList());
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
				}
			});
			back.setFont(FONT);
			back.setContentAreaFilled(false);
			back.setOpaque(true);
			back.setBackground(Color.RED.brighter());
			back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			back.setBorder(finalStyle);

			label = new JLabel(icon);
			playersLabel = new JLabel();

			playersLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
			playersLabel.setText( i + "/3");
			playersLabel.setForeground(Color.WHITE);

			JPanel gif = new JPanel();
			gif.setLayout(new GridBagLayout());

			GridBagConstraints g = new GridBagConstraints();        

			g.gridy = 0;
			g.gridx = 0;
			g.insets = new Insets(5, 0, 0, 0);
			gif.add(playersLabel, g);
			g.insets = new Insets(60, 0, 0, 0);
			gif.add(back, g);
			g.insets = new Insets(0, 0, 0, 0);
			gif.add(label, g);


			setLayout(new GridBagLayout());
			add(gif);

			addWindowListener(new WindowAdapter()
	        {
	            @Override
	            public void windowClosing(WindowEvent e)
	            {
	            	try {
						ClientRdF.exitLobby();
						dispose();
						ClientRdF.removeMe();
						new GameListFrame(ClientRdF.gameList());
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
	            }
	        });
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			setSize(400, 400); //dimensioni finestra
			setLocationRelativeTo(null);
			setResizable(false);
			setVisible(true); 
		}
	}
	
	/**
	 * metodo utilizzato per gestire il numero di giocatori
	 * @throws RemoteException per la gestione delle eccezioni
	 */
	public static void updatePlayers() throws RemoteException {
		playersLabel.setText(i + "/3");
		if(i==3) {
			frame.dispose();			
		} else if(i==0) {
			frame.dispose();
			if(ClientRdF.player) {
				ClientRdF.player = false;
			} else if(ClientRdF.observer) {
				ClientRdF.observer = false;
			}
			new GameListFrame(ClientRdF.gameList());
			PopUp pu = new PopUp("Partita Annullata", "Ops, tutti i giocatori sono usciti! \nLa partita è stata annullata", Color.RED, null);
			pu.add();
		}
	}
	
	/**
	 * metodo utilizzato per modificare il numero di giocatori
	 * @param n indica il numero di giocatori da aggiungere
	 */
	public static void addPlayer(int n) {
		i = i+n;
	}

}
