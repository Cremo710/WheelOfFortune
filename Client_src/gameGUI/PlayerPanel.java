package gameGUI;

import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import accessGUI.LogFrame;
import game.ClientRdF;

import java.awt.Image;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;

/**
 * classe utilizzata per la definizione del pannello relativo alla schermata utente principale
 * @author Luca Cremonesi
 * @version 1.0
 */
public class PlayerPanel extends JPanel{
	private static final long serialVersionUID=1;
	
	private static final Font FONT = new Font("Open Sans Light", Font.BOLD, 20);
	
	private ButtonListener buttonListener;
	
	private PlayerFrame playerFrame;
	
	private JLabel imgL, info;
	
	private Image img;
	
	private JButton newGame, logGame, editProfile, statsList, editPassw, logOut;
	
	/**
	 * costruttore
	 * @param frame indica il riferimento all'oggetto di tipo PlayerFrame
	 */
	public PlayerPanel(PlayerFrame frame) {
		super(new GridBagLayout());
		
		playerFrame = frame;
		
		buttonListener = new ButtonListener();
		
		Border style = BorderFactory.createRaisedBevelBorder();
		Border border = BorderFactory.createEtchedBorder(Color.CYAN.darker(), Color.CYAN.darker());
		Border finalStyle = BorderFactory.createCompoundBorder(border, style);
		
		Border borderR = BorderFactory.createEtchedBorder(Color.RED.darker(), Color.RED.darker());
		Border finalStyleR = BorderFactory.createCompoundBorder(borderR, style);
		
		try {
			img = ImageIO.read(new File("immagini/RDF.png"));
		}catch (IOException e) {}
		
		imgL = new JLabel(new ImageIcon(img));
		imgL.setOpaque(true);
		
		info = new JLabel(new ImageIcon("immagini/info.png"));
		info.setToolTipText("Clicca per imparare a giocare!");
		info.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		info.addMouseListener(new MouseListener() {
		    @Override
		    public void mousePressed(MouseEvent e) {
		    	
		    }
			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			@Override
			public void mouseExited(MouseEvent e) {
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				JDialog d = new JDialog();
				d.setTitle("Regole del Gioco");
				d.setBackground(Color.CYAN);
				d.setBounds(0, 0, 700, 400);
				d.setLayout(new BorderLayout(0,0));
				JScrollPane scroll = new JScrollPane();
				scroll.setPreferredSize(new Dimension(700, 400));
				scroll.getVerticalScrollBar().setUnitIncrement(16);
				d.add(scroll, BorderLayout.CENTER);
				JPanel p = new JPanel();
				p.setBackground(Color.CYAN);
				p.setLayout(new GridBagLayout());
				JLabel t = new JLabel("<html><h1>Come Si Gioca?</h1></html>", SwingConstants.CENTER);
				t.setForeground(Color.BLUE.darker());
				JLabel b = new JLabel("<html><body><br><p align=\"center\">"
						+ "Il gioco è strutturato in 5 manche, a cui partecipano 3 concorrenti il cui obiettivo è indovinare una<br>"
						+ "frase misteriosa (su un argomento prefissato) presente in un tabellone in cui tutte le lettere sono<br>"
						+ "state inizialmente oscurate. All'inizio della manche il primo concorrente (nella prima manche scelto<br>"
						+ "da un generatore casuale) “gira la ruota”, cioè fa partire una roulette che si ferma su una casella<br>"
						+ "recante un messaggio casuale.<br>"
						+ "<br>Il messaggio può rappresentare un jolly (utile per evitare di perdere<br>"
						+ "il turno di gioco); un comando (“Passa” o “Perde”) o un premio in punti. Il comando “Passa” costringe<br>"
						+ "il giocatore a cedere il turno di gioco al concorrente alla sua destra; il comando “Perde” invece,<br>"
						+ "azzera il montepremi guadagnato dal giocatore in quella manche.<br>"
						+ "<br>Se il messaggio invece rappresenta un<br>"
						+ "premio in punti il concorrente deve “chiamare una consonante”, cioè cercare di indovinare (entro 5 <br>"
						+ "secondi) una consonante che secondo lui è presente nella frase misteriosa. Se la consonante chiamata è<br>"
						+ "effettivamente presente nella frase misteriosa, le caselle che nel tabellone celano la consonante<br>"
						+ "richiesta si rivelano e il concorrente accumula in un suo portafoglio virtuale il premio indicato dalla<br>"
						+ "ruota (da 300 a 1000 punti) moltiplicato per il numero di volte che quella consonante è presente nella<br>"
						+ "frase.<br>"
						+ "<br>Dopo aver individuato la consonante corretta il giocatore può ancora “girare la ruota” (ripetendo<br>"
						+ "l’esperienza precedente) o tentare di indovinare la frase nascosta. Altra possibilità è di “comprare<br>"
						+ "una vocale”, cioè togliere dal proprio portafoglio virtuale (se si hanno fondi a sufficienza) 1000 punti<br>"
						+ "per far scoprire, nella frase misteriosa, le celle che nascondono la vocale prescelta. Se la lettera<br>"
						+ "chiamata (vocale o consonante) NON è presente nella frase si perde il turno e il gioco passa a un altro<br>"
						+ "giocatore. Si può evitare il passaggio del turno sacrificando un “jolly” eventualmente accumulato in<br>"
						+ "precedenza (anche in una manche precedente).<br>"
						+ "<br>Quando tutte le consonanti della frase sono state scoperte tutti i giocatori vengono avvisati e il<br>"
						+ "concorrente di turno può soltanto tentare di indovinare la frase (o comprare una vocale). Se il concorrente<br>"
						+ "non indovina la frase, il turno passa al giocatore successivo.<br>"
						+ "<br>La manche finisce quando uno dei tre giocatori indovina la frase. I punti di manche accumulati dal<br>"
						+ "vincitore sono messi in un deposito; agli altri due giocatori invece i punti di manche sono azzerati.<br>"
						+ "Il vincitore del gioco è chi, al termine delle 5 manche, ha accumulato più punti in deposito.<br><br></p></body></html>", SwingConstants.CENTER);
				b.setFont(new Font("Open Sans Light", Font.PLAIN, 13));
				GridBagConstraints c = new GridBagConstraints();
				c.insets = new Insets(0, 0, 0, 0);
				
				c.gridy=0;
				p.add(t, c);
				c.gridy=1;
				p.add(b, c);

				scroll.setViewportView(p);
				d.setLocationRelativeTo(null);
				d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				d.setResizable(false);
				d.setVisible(true);
			}
		});
		
		logOut = new JButton("DISCONNETTI");
		logOut.setBorder(finalStyleR);
		logOut.addMouseListener(new MouseListener() {
		    @Override
		    public void mousePressed(MouseEvent e) {
		    	Border style = BorderFactory.createLoweredBevelBorder();
				Border border = BorderFactory.createEtchedBorder(Color.RED.darker(), Color.RED.darker());
				Border finalStyle = BorderFactory.createCompoundBorder(border, style);
				logOut.setBorder(finalStyle);
		    }
			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {
				logOut.setForeground(Color.GREEN.darker());
			}
			@Override
			public void mouseExited(MouseEvent e) {
				logOut.setForeground(Color.BLACK);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				Border style = BorderFactory.createRaisedBevelBorder();
				Border border = BorderFactory.createEtchedBorder(Color.RED.darker(), Color.RED.darker());
				Border finalStyle = BorderFactory.createCompoundBorder(border, style);
				logOut.setBorder(finalStyle);
				int r = JOptionPane.showConfirmDialog(null, "Vuoi davvero uscire?");
				if(r == 0) {
					try {
						ClientRdF.setOnline(false);
						frame.dispose();
						new LogFrame();
					} catch (RemoteException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(null, "Disconnessione Non Riuscita" + "\nQualcosa è andato storto, riprovare",
								"Errore", JOptionPane.ERROR_MESSAGE);
					}
				} 
			}
		});
		logOut.setFont(new Font("Open Sans Light", Font.BOLD, 11));
		logOut.setContentAreaFilled(false);
		logOut.setOpaque(true);
		logOut.setBackground(Color.RED);
		logOut.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		editPassw = new JButton("MODIFICA PASSWORD");
		editPassw.addMouseListener(new MouseListener() {
		    @Override
		    public void mousePressed(MouseEvent e) {
		    	Border style = BorderFactory.createLoweredBevelBorder();
				Border border = BorderFactory.createEtchedBorder(Color.CYAN.darker(), Color.CYAN.darker());
				Border finalStyle = BorderFactory.createCompoundBorder(border, style);
				editPassw.setBorder(finalStyle);
		    }
			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {
				editPassw.setForeground(Color.GREEN.darker());
			}
			@Override
			public void mouseExited(MouseEvent e) {
				editPassw.setForeground(new Color(96, 24, 149));
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				Border style = BorderFactory.createRaisedBevelBorder();
				Border border = BorderFactory.createEtchedBorder(Color.CYAN.darker(), Color.CYAN.darker());
				Border finalStyle = BorderFactory.createCompoundBorder(border, style);
				editPassw.setBorder(finalStyle);
				
				JPanel p = new JPanel();
				
				p.setLayout(new GridBagLayout());
				GridBagConstraints g = new GridBagConstraints();
				g.insets = new Insets(0, 0, 0, 0);
				JLabel txt = new JLabel("Inserisci la password attuale:");
				final JPasswordField pfA = new JPasswordField();
				pfA.setPreferredSize(new Dimension(200, 25));
				
				g.gridy = 0;
				p.add(txt, g);
				
				g.insets = new Insets(10, 0, 0, 0);
				
				g.gridy = 1;
				p.add(pfA, g);
				
				JOptionPane pane = new JOptionPane(p, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
			    JDialog dialog = pane.createDialog("Password Attuale");
			    dialog.addComponentListener(new ComponentListener(){

			        @Override
			        public void componentShown(ComponentEvent e) {
			            pfA.requestFocusInWindow();
			        }
			        @Override public void componentHidden(ComponentEvent e) {}
			        @Override public void componentResized(ComponentEvent e) {}
			        @Override public void componentMoved(ComponentEvent e) {}
			        });

			    dialog.setVisible(true);
			    int value;
			    try {
			    	value = ((Integer)pane.getValue()).intValue();
			    } catch(NullPointerException npe) {
			    	value = JOptionPane.CLOSED_OPTION;
			    }
				if(value == JOptionPane.OK_OPTION) {
					String pwA = String.valueOf(pfA.getPassword());
					try {
						if(ClientRdF.getSt().isPasswordCorrect(ClientRdF.email, pwA)) {
							p = new JPanel();
							p.setLayout(new GridBagLayout());
							g.insets = new Insets(0, 0, 0, 0);
							
							txt = new JLabel("Inserisci la nuova password:");
							final JPasswordField pfN = new JPasswordField();
							pfN.setPreferredSize(new Dimension(200, 25));
							
							g.gridy = 0;
							p.add(txt, g);
							
							g.insets = new Insets(10, 0, 0, 0);
							g.gridy = 1;
							p.add(pfN, g);
							
							pane = new JOptionPane(p, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
						    dialog = pane.createDialog("Nuova Password");
						    dialog.addComponentListener(new ComponentListener(){

						        @Override
						        public void componentShown(ComponentEvent e) {
						            pfN.requestFocusInWindow();
						        }
						        @Override public void componentHidden(ComponentEvent e) {}
						        @Override public void componentResized(ComponentEvent e) {}
						        @Override public void componentMoved(ComponentEvent e) {}
						        });

						    dialog.setVisible(true);
						    try {
						    	value = ((Integer)pane.getValue()).intValue();
						    } catch(NullPointerException npe) {
						    	value = JOptionPane.CLOSED_OPTION;
						    }
						    
							if(value == JOptionPane.OK_OPTION) {
								String pwNew = String.valueOf(pfN.getPassword());
								if(pwNew.length() != 0) {
									if(ClientRdF.updatePassword(pwNew, pwA)) {
										JOptionPane.showMessageDialog(null, "Password Aggiornata" + "\nLa password è stata aggiornata correttamente",
												"Aggiornamento Riuscito", JOptionPane.INFORMATION_MESSAGE);
									} else {
										JOptionPane.showMessageDialog(null, "Errore" + "\nOps, qualcosa è andato storto",
												"Aggiornamento Non Riuscito", JOptionPane.ERROR_MESSAGE);
									}
								} else {
									JOptionPane.showMessageDialog(null, "Campo Vuoto" + "\nIl campo 'password' risulta vuoto",
											"Aggiornamento Non Riuscito", JOptionPane.ERROR_MESSAGE);
								}
							}
						} else {
							JOptionPane.showMessageDialog(null, "Password Errata" + "\nLa password attuale inserita non è valida, riprovare",
									"Password Non Valida", JOptionPane.ERROR_MESSAGE);
						}
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		editPassw.setForeground(new Color(96, 24, 149));
		editPassw.setFont(FONT);
		editPassw.setContentAreaFilled(false);
		editPassw.setOpaque(true);
		editPassw.setBackground(Color.YELLOW);
		editPassw.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		editPassw.setBorder(finalStyle);
		
		newGame = new JButton("NUOVO GIOCO");
		newGame.addMouseListener(new MouseListener() {
		    @Override
		    public void mousePressed(MouseEvent e) {
		    	Border style = BorderFactory.createLoweredBevelBorder();
				Border border = BorderFactory.createEtchedBorder(Color.CYAN.darker(), Color.CYAN.darker());
				Border finalStyle = BorderFactory.createCompoundBorder(border, style);
				newGame.setBorder(finalStyle);
		    }
			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {
				newGame.setForeground(Color.GREEN.darker());
			}
			@Override
			public void mouseExited(MouseEvent e) {
				newGame.setForeground(new Color(96, 24, 149));
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				Border style = BorderFactory.createRaisedBevelBorder();
				Border border = BorderFactory.createEtchedBorder(Color.CYAN.darker(), Color.CYAN.darker());
				Border finalStyle = BorderFactory.createCompoundBorder(border, style);
				newGame.setBorder(finalStyle);
			}
		});
		newGame.setForeground(new Color(96, 24, 149));
		newGame.addActionListener(buttonListener);
		newGame.setFont(FONT);
		newGame.setContentAreaFilled(false);
		newGame.setOpaque(true);
		newGame.setBackground(Color.YELLOW);
		newGame.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		newGame.setBorder(finalStyle);
		
		logGame = new JButton("ELENCO PARTITE");
		logGame.addMouseListener(new MouseListener() {
		    @Override
		    public void mousePressed(MouseEvent e) {
		    	Border style = BorderFactory.createLoweredBevelBorder();
				Border border = BorderFactory.createEtchedBorder(Color.CYAN.darker(), Color.CYAN.darker());
				Border finalStyle = BorderFactory.createCompoundBorder(border, style);
				logGame.setBorder(finalStyle);
		    }
			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {
				logGame.setForeground(Color.GREEN.darker());
			}
			@Override
			public void mouseExited(MouseEvent e) {
				logGame.setForeground(new Color(96, 24, 149));
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				Border style = BorderFactory.createRaisedBevelBorder();
				Border border = BorderFactory.createEtchedBorder(Color.CYAN.darker(), Color.CYAN.darker());
				Border finalStyle = BorderFactory.createCompoundBorder(border, style);
				logGame.setBorder(finalStyle);
			}
		});
		logGame.setForeground(new Color(96, 24, 149));
		logGame.addActionListener(buttonListener);
		logGame.setFont(FONT);
		logGame.setContentAreaFilled(false);
		logGame.setOpaque(true);
		logGame.setBackground(Color.YELLOW);
		logGame.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		logGame.setBorder(finalStyle);
		
		editProfile = new JButton("PROFILO");
		editProfile.addMouseListener(new MouseListener() {
		    @Override
		    public void mousePressed(MouseEvent e) {
		    	Border style = BorderFactory.createLoweredBevelBorder();
				Border border = BorderFactory.createEtchedBorder(Color.CYAN.darker(), Color.CYAN.darker());
				Border finalStyle = BorderFactory.createCompoundBorder(border, style);
				editProfile.setBorder(finalStyle);
		    }
			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {
				editProfile.setForeground(Color.GREEN.darker());
			}
			@Override
			public void mouseExited(MouseEvent e) {
				editProfile.setForeground(new Color(96, 24, 149));
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				Border style = BorderFactory.createRaisedBevelBorder();
				Border border = BorderFactory.createEtchedBorder(Color.CYAN.darker(), Color.CYAN.darker());
				Border finalStyle = BorderFactory.createCompoundBorder(border, style);
				editProfile.setBorder(finalStyle);
			}
		});
		editProfile.setForeground(new Color(96, 24, 149));
		editProfile.addActionListener(buttonListener);
		editProfile.setFont(FONT);
		editProfile.setContentAreaFilled(false);
		editProfile.setOpaque(true);
		editProfile.setBackground(Color.YELLOW);
		editProfile.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		editProfile.setBorder(finalStyle);
		
		statsList = new JButton("STATISTICHE DI GIOCO");
		statsList.addMouseListener(new MouseListener() {
		    @Override
		    public void mousePressed(MouseEvent e) {
		    	Border style = BorderFactory.createLoweredBevelBorder();
				Border border = BorderFactory.createEtchedBorder(Color.CYAN.darker(), Color.CYAN.darker());
				Border finalStyle = BorderFactory.createCompoundBorder(border, style);
				statsList.setBorder(finalStyle);
		    }
			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {
				statsList.setForeground(Color.GREEN.darker());
			}
			@Override
			public void mouseExited(MouseEvent e) {
				statsList.setForeground(new Color(96, 24, 149));
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				Border style = BorderFactory.createRaisedBevelBorder();
				Border border = BorderFactory.createEtchedBorder(Color.CYAN.darker(), Color.CYAN.darker());
				Border finalStyle = BorderFactory.createCompoundBorder(border, style);
				statsList.setBorder(finalStyle);
			}
		});
		statsList.setForeground(new Color(96, 24, 149));
		statsList.addActionListener(buttonListener);
		statsList.setFont(FONT);
		statsList.setContentAreaFilled(false);
		statsList.setOpaque(true);
		statsList.setBackground(Color.YELLOW);
		statsList.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		statsList.setBorder(finalStyle);
		
		GridBagConstraints g = new GridBagConstraints();
		
		g.insets = new Insets(-200, -420, 0, 0);
		
		g.gridy = 0;
		add(logOut, g);
		
		g.insets = new Insets(-200, 0, 0, -180);
		
		add(info, g);
		
		g.insets = new Insets(-30, 20, 20, 20);
		
		g.gridy = 1;
		add(newGame, g);
		
		g.insets = new Insets(10, 20, 20, 20);
		
		g.gridy = 2;
		add(logGame, g);
		
		g.gridy = 3;
		add(editProfile, g);
		
		g.gridy = 4;
		add(editPassw, g);
		
		g.gridy = 5;
		add(statsList, g);
		
		setPreferredSize(new Dimension(600, 550));
	}
	
	/**
	 * classe per la gestione dei comandi associati ad ogni oggetto di tipo JButton presente nella schermata utente principale
	 * @author Luca Cremonesi
	 * @version 1.0
	 */
	private class ButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			JButton source = (JButton)e.getSource();
			if(source == newGame) {
				ClientRdF.player = true;
				playerFrame.dispose();
				try {
					ClientRdF.newGame();
					if((ClientRdF.idGame)!= 0) {
						ClientRdF.addObs();
						new LoadingFrame(1);
					} else {
						JOptionPane.showMessageDialog(null, "Errore" + "\nOps, qualcosa è andato storto",
								"Errore", JOptionPane.WARNING_MESSAGE);
					}
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			} else if(source == logGame) {
				playerFrame.dispose();
				
				try {
					new GameListFrame(ClientRdF.gameList());
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			} else if(source == editProfile) {
				playerFrame.dispose();
				try {
					new EditFrame(ClientRdF.getData());
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			} else if(source == statsList) {
				playerFrame.dispose();
				new StatsFrame();
			}

		}
	}
	
	@Override
	/**
	 * metodo utilizzato per impostare l'immagine di sfondo della schermata
	 */
	public void paintComponent(Graphics g) {
		g.drawImage(img, -150, 0, null);
	}
}
