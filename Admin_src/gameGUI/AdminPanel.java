package gameGUI;


import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.Border;
import accessGUI.LogFrame;
import game.AdminRdF;
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
 * classe utilizzata per la definizione del pannello relativo alla schermata amministratore principale
 * @author Luca Cremonesi
 * @version 1.0
 */
public class AdminPanel extends JPanel{
	private static final long serialVersionUID=1;
	
	private static final Font FONT = new Font("Open Sans Light", Font.BOLD, 20);
	
	private ButtonListener buttonListener;
	
	private AdminFrame adminFrame;
	
	private JLabel imgL;
	
	private Image img;
	
	private JButton logGame, editProfile, statsList, editPassw, phraseManagement, logOut;
	
	/**
	 * costruttore
	 * @param frame indica il riferimento all'oggetto di tipo PlayerFrame
	 */
	public AdminPanel(AdminFrame frame) {
		super(new GridBagLayout());
		
		adminFrame = frame;
		
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
						AdminRdF.setOnline(false);
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
		editPassw.setForeground(new Color(96, 24, 149));
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
						if(AdminRdF.getSt().isPasswordCorrect(AdminRdF.email, pwA)) {
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
						    //Add a listener to the dialog to request focus of Password Field
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
						    value = ((Integer)pane.getValue()).intValue();
		
							if(value == JOptionPane.OK_OPTION) {
								String pwNew = String.valueOf(pfN.getPassword());
								if(pwNew.length() != 0) {
									if(AdminRdF.updatePassword(pwNew, pwA)) {
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
		editPassw.addActionListener(buttonListener);
		editPassw.setFont(FONT);
		editPassw.setContentAreaFilled(false);
		editPassw.setOpaque(true);
		editPassw.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		editPassw.setBackground(Color.YELLOW);
		editPassw.setBorder(finalStyle);
		
		logGame = new JButton("ELENCO PARTITE");
		logGame.setForeground(new Color(96, 24, 149));
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
		logGame.addActionListener(buttonListener);
		logGame.setFont(FONT);
		logGame.setContentAreaFilled(false);
		logGame.setOpaque(true);
		logGame.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		logGame.setBackground(Color.YELLOW);
		logGame.setBorder(finalStyle);
		
		editProfile = new JButton("PROFILO");
		editProfile.setForeground(new Color(96, 24, 149));
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
		editProfile.addActionListener(buttonListener);
		editProfile.setFont(FONT);
		editProfile.setContentAreaFilled(false);
		editProfile.setOpaque(true);
		editProfile.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		editProfile.setBackground(Color.YELLOW);
		editProfile.setBorder(finalStyle);
		
		statsList = new JButton("STATISTICHE DI GIOCO");
		statsList.setForeground(new Color(96, 24, 149));
		statsList.addActionListener(buttonListener);
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
		statsList.setFont(FONT);
		statsList.setContentAreaFilled(false);
		statsList.setOpaque(true);
		statsList.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		statsList.setBackground(Color.YELLOW);
		statsList.setBorder(finalStyle);
		
		phraseManagement = new JButton("GESTIONE FRASI");
		phraseManagement.setForeground(new Color(96, 24, 149));
		phraseManagement.addActionListener(buttonListener);
		phraseManagement.addMouseListener(new MouseListener() {
		    @Override
		    public void mousePressed(MouseEvent e) {
		    	Border style = BorderFactory.createLoweredBevelBorder();
				Border border = BorderFactory.createEtchedBorder(Color.CYAN.darker(), Color.CYAN.darker());
				Border finalStyle = BorderFactory.createCompoundBorder(border, style);
				phraseManagement.setBorder(finalStyle);
		    }
			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {
				phraseManagement.setForeground(Color.GREEN.darker());
			}
			@Override
			public void mouseExited(MouseEvent e) {
				phraseManagement.setForeground(new Color(96, 24, 149));
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				Border style = BorderFactory.createRaisedBevelBorder();
				Border border = BorderFactory.createEtchedBorder(Color.CYAN.darker(), Color.CYAN.darker());
				Border finalStyle = BorderFactory.createCompoundBorder(border, style);
				phraseManagement.setBorder(finalStyle);
			}
		});
		phraseManagement.setFont(FONT);
		phraseManagement.setContentAreaFilled(false);
		phraseManagement.setOpaque(true);
		phraseManagement.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		phraseManagement.setBackground(Color.YELLOW);
		phraseManagement.setBorder(finalStyle);
		
		GridBagConstraints g = new GridBagConstraints();
		
		g.insets = new Insets(-200, -420, 0, 0);
		
		g.gridy = 0;
		add(logOut, g);
		
		g.insets = new Insets(-30, 20, 20, 20);
		
		g.gridy = 1;
		add(logGame, g);
		
		g.insets = new Insets(10, 20, 20, 20);
		
		g.gridy = 2;
		add(editProfile, g);
		
		g.gridy = 3;
		add(editPassw, g);
		
		g.gridy = 4;
		add(statsList, g);
		
		g.gridy = 5;
		add(phraseManagement, g);
		
		setPreferredSize(new Dimension(600, 550));
	}
	
	/**
	 * classe per la gestione dei comandi associati ad ogni oggetto di tipo JButton presente nella schermata amministratore principale
	 * @author Luca Cremonesi
	 * @version 1.0
	 */
	private class ButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			JButton source = (JButton)e.getSource();
			if(source == logGame) {
				adminFrame.dispose();
				
				try {
					new GameListFrame(AdminRdF.gameList());
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			} else if(source == editProfile) {
				adminFrame.dispose();
				try {
					new EditFrame(AdminRdF.getData());
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			} else if(source == phraseManagement){
				adminFrame.dispose();
				new PhraseMFrame();
			} else if(source == statsList) {
				adminFrame.dispose();
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
