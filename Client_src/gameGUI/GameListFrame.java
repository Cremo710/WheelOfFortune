package gameGUI;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.LinkedHashMap;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
//import javax.swing.SwingUtilities;
//import javax.swing.UIManager;
import javax.swing.border.Border;

import game.ClientRdF;

/**
 * classe per la visualizzazione e la definizione del frame relativo alla schermata che mostra l'elenco delle partite
 * @author Luca Cremonesi
 * @version 1.0
 */
public class GameListFrame extends JFrame{
	private static final long serialVersionUID=1;

	private static final Font FONT = new Font("Yu Gothic UI Semibold", Font.PLAIN, 15);

	private JButton back, play, observ, update;

	private LinkedHashMap<Long,String> dati;

	private JScrollPane window;

	private JPanel game;

	private GameInterface gameX;

	private Border style = BorderFactory.createRaisedBevelBorder();
	private Border cyan = BorderFactory.createEtchedBorder(Color.CYAN.darker(), Color.CYAN.darker());
	private Border finalStyle = BorderFactory.createCompoundBorder(cyan, style);

	private Border yellow = BorderFactory.createEtchedBorder(Color.YELLOW.darker(), Color.YELLOW.darker());
	private Border finalStyleOb = BorderFactory.createCompoundBorder(yellow, style);

	private Border green = BorderFactory.createEtchedBorder(Color.GREEN.darker(), Color.GREEN.darker());
	private Border finalStylePl = BorderFactory.createCompoundBorder(green, style);

	private GridBagConstraints g = new GridBagConstraints();

	private Set<Long> keys;

	/**
	 * costruttore
	 * @param d indica la LinkedHashMap contenente i dati delle partite attive
	 */
	public GameListFrame(LinkedHashMap<Long,String> d) {
		super("Elenco Partite");

		dati = d;

		try {
			BufferedImage image = ImageIO.read(new File("immagini/IconaRDF.png"));
			setIconImage(image);
		} catch (IOException e) {
			e.printStackTrace();
		}

		back = new JButton("Indietro");
		back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		back.addMouseListener(new MouseListener() {
			@Override
			public void mousePressed(MouseEvent e) {
				Border style = BorderFactory.createLoweredBevelBorder();
				Border border = BorderFactory.createEtchedBorder(Color.CYAN.darker(), Color.CYAN.darker());
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
				Border border = BorderFactory.createEtchedBorder(Color.CYAN.darker(), Color.CYAN.darker());
				Border finalStyle = BorderFactory.createCompoundBorder(border, style);
				back.setBorder(finalStyle);
				dispose();
				new PlayerFrame();
			}
		});
		back.setFont(FONT);
		back.setContentAreaFilled(false);
		back.setOpaque(true);
		back.setBackground(Color.CYAN.brighter());
		back.setBorder(finalStyle);
		back.setPreferredSize(new Dimension(100, 25));

		update = new JButton("Aggiorna");
		update.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		update.addMouseListener(new MouseListener() {
			@Override
			public void mousePressed(MouseEvent e) {
				Border style = BorderFactory.createLoweredBevelBorder();
				Border border = BorderFactory.createEtchedBorder(Color.CYAN.darker(), Color.CYAN.darker());
				Border finalStyle = BorderFactory.createCompoundBorder(border, style);
				update.setBorder(finalStyle);
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
				Border border = BorderFactory.createEtchedBorder(Color.CYAN.darker(), Color.CYAN.darker());
				Border finalStyle = BorderFactory.createCompoundBorder(border, style);
				update.setBorder(finalStyle);
				try {
					new GameListFrame(ClientRdF.gameList());
					dispose();
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		});
		update.setFont(FONT);
		update.setContentAreaFilled(false);
		update.setOpaque(true);
		update.setBackground(Color.CYAN.brighter());
		update.setBorder(finalStyle);
		update.setPreferredSize(new Dimension(100, 25));		

		inizialize();
	}

	/**
	 * metodo utilizzato per inizializzare il frame
	 */
	private void inizialize() {
		Container cont = new Container();
		cont.setLayout(new GridLayout());

		game = new JPanel();
		game.setLayout(new GridBagLayout());
		window = new JScrollPane(game, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		window.getVerticalScrollBar().setUnitIncrement(16);
		game.setBackground(new Color(155, 248, 214));

		generateGameList();		

		cont.add(window);			

		add(cont);

		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				dispose();
				new PlayerFrame();
			}
		});

		setLayout(new GridLayout());
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setSize(650, 450); //dimensioni finestra
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}

	/**
	 * metodo utilizzato per generare il pannello che mostra l'elenco delle partite
	 */
	private void generateGameList() {
		String mail = null;
		keys = dati.keySet();

		if(keys.size()==0) {
			JLabel txt = new JLabel("<html>Al momento non è stata creata alcuna partita<br/>Torna indietro per crearne una o aggiorna la lista</html");
			txt.setFont(new Font("Times New Roman", Font.PLAIN, 15));
			g.insets = new Insets(10, 0, 0, 0);

			game.add(txt, g);

			g.insets = new Insets(10, -125, 0, 0);

			g.gridx = 0;
			g.gridy = 1;
			game.add(back, g);

			g.gridx = 0;
			g.insets = new Insets(10, 125, 0, 0);
			game.add(update, g);
		} else {
			g.insets = new Insets(10, -125, 0, 0);

			g.gridx = 0;
			g.gridy = 0;
			game.add(back, g);

			g.gridx = 0;
			g.insets = new Insets(10, 125, 0, 0);
			game.add(update, g);

			g.insets = new Insets(10, 0, 0, 0);

			for(Long c: keys) {
				mail = dati.get(c);
				if(!(mail.equals(ClientRdF.email))) {
					g.gridy += 1;
					try {
						observ = new JButton(" OSSERVA ");
						observ.setFont(FONT);
						observ.setContentAreaFilled(false);
						observ.setOpaque(true);
						observ.setBackground(Color.YELLOW);
						observ.setBorder(finalStyleOb);
						observ.setBounds(313, 96, 89, 23);
						observ.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
						observ.setName("obId:" + c);
						observ.addMouseListener(new MouseListener() {
							@Override
							public void mousePressed(MouseEvent e) {
								Border style = BorderFactory.createLoweredBevelBorder();
								Border border = BorderFactory.createEtchedBorder(Color.YELLOW.darker(), Color.YELLOW.darker());
								Border fS = BorderFactory.createCompoundBorder(border, style);
								JButton source = (JButton)e.getSource();
								source.setBorder(fS);
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
								Border border = BorderFactory.createEtchedBorder(Color.YELLOW.darker(), Color.YELLOW.darker());
								Border fS = BorderFactory.createCompoundBorder(border, style);
								JButton source = (JButton)e.getSource();
								source.setBorder(fS);
								String s = source.getName();
								String sub = s.substring(s.indexOf(':')+1, s.length());
								long idG = Long.parseLong(sub);
								ClientRdF.idGame = idG;
								try {
									if(ClientRdF.nPlayers(ClientRdF.idGame)==0) {
										JOptionPane.showMessageDialog(null, "Ops, la partita è stata cancellata per assenza di giocatori!", "Partita Eliminata", JOptionPane.ERROR_MESSAGE);
										new GameListFrame(ClientRdF.gameList());
										ClientRdF.idGame = 0;
										dispose();
									} else {
										if(ClientRdF.logObGame()) {
											dispose();
											ClientRdF.observer = true;
											ClientRdF.addObs();
											if(ClientRdF.nPlayers(ClientRdF.idGame)!=3) {
												new LoadingFrame(ClientRdF.nPlayers(ClientRdF.idGame));
											} else {
												new ObserverFrame(1);
											}
										}
									}
								} catch (RemoteException e1) {
									e1.printStackTrace();
								}
							}
						});

						play = new JButton(" GIOCA ");
						play.setFont(FONT);
						play.setContentAreaFilled(false);
						play.setOpaque(true);
						play.setBackground(Color.GREEN);
						play.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
						play.setBorder(finalStylePl);
						play.setBounds(183, 96, 89, 23);
						play.setName("plId:" + c);
						play.addMouseListener(new MouseListener() {
							@Override
							public void mousePressed(MouseEvent e) {
								Border style = BorderFactory.createLoweredBevelBorder();
								Border border = BorderFactory.createEtchedBorder(Color.GREEN.darker(), Color.GREEN.darker());
								Border fS = BorderFactory.createCompoundBorder(border, style);
								JButton source = (JButton)e.getSource();
								source.setBorder(fS);
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
								Border border = BorderFactory.createEtchedBorder(Color.GREEN.darker(), Color.GREEN.darker());
								Border fS = BorderFactory.createCompoundBorder(border, style);
								JButton source = (JButton)e.getSource();
								source.setBorder(fS);
								String s = source.getName();
								String sub = s.substring(s.indexOf(':')+1, s.length());
								long idG = Long.parseLong(sub);
								ClientRdF.idGame = idG;				
								try {
									if(ClientRdF.nPlayers(ClientRdF.idGame)==3) {
										JOptionPane.showMessageDialog(null, "Ops, la partita è già iniziata!", "Partita Iniziata", JOptionPane.ERROR_MESSAGE);
										new GameListFrame(ClientRdF.gameList());
										ClientRdF.idGame = 0;	
										dispose();
									} else if(ClientRdF.nPlayers(ClientRdF.idGame)==0) {
										JOptionPane.showMessageDialog(null, "Ops, la partita è stata cancellata per assenza di giocatori!", "Partita Eliminata", JOptionPane.ERROR_MESSAGE);
										new GameListFrame(ClientRdF.gameList());
										ClientRdF.idGame = 0;	
										dispose();
									} else {
										dispose();
										if(ClientRdF.logGame()) { 
											ClientRdF.player = true;
											ClientRdF.addObs();
											if(ClientRdF.nPlayers(ClientRdF.idGame)!=3) {
												new LoadingFrame(ClientRdF.nPlayers(ClientRdF.idGame));
											} else {
												try {
													new WheelFrame();
												} catch (UnsupportedAudioFileException | IOException
														| LineUnavailableException e1) {
													e1.printStackTrace();
												}
											}
										} else {
											JOptionPane.showMessageDialog(null, "Ops, non ci sono nuove frasi disponibili! \\nSarà inviata una mail agli amministratori", "Errore", JOptionPane.ERROR_MESSAGE);
											new PlayerFrame();
										}
									}
								} catch (RemoteException e2) {
									e2.printStackTrace();
								}
							}
						});

						gameX = new GameInterface(c, ClientRdF.nPlayers(c), mail);
						if(ClientRdF.nPlayers(c)==3) {
							play.setEnabled(false);
							play.setBackground(Color.GRAY.brighter());
							Border bdr = BorderFactory.createEtchedBorder(Color.GRAY.darker(), Color.GRAY.darker());
							Border fS = BorderFactory.createCompoundBorder(bdr, style);
							play.setBorder(fS);
						}

						gameX.add(play);

						gameX.add(observ);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					game.add(gameX, g);	
				}
			}
		}


	}

}
