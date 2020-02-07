package gameGUI;

import javax.swing.BorderFactory;
import javax.swing.border.*;

import game.*;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.rmi.RemoteException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;


/**
 * classe per la definizione del pannello relativo alla schermata di gioco per i giocatori
 * @author Luca Cremonesi
 * @version 1.0
 */
public class WheelPanel extends JPanel {
	private static final long serialVersionUID=1;
	
	private static final Font FONT = new Font("Open Sans Light", Font.BOLD, 20);

	private static WheelOfFortune game;

	private static FirstPanel firstP;

	private static PhrasePanel phraseP;

	private static final Map<String, Integer> VALUES;

	private static final int DEGREES = 15; //20

	private static final String[] IMG_NAME;

	private final Map<String, Image> IMAGES;

	private List<String> ImagesNames; //contiene le immagini relative ad ogni spicchio della ruota

	private final AudioPlayer WHEEL_START;

	private static AudioPlayer GREAT;

	private static  AudioPlayer BAD_CHOICE;

	private static  AudioPlayer VOWELS_TERMINATED;

	private ButtonListener buttonListener;

	private static Timer wheelTimer;

	private static Timer timer, mancheTimer, timerObs, mancheTimerObs, jollyTimer;

	private int nLaps;

	private static int seconds;

	private Random rnd;

	private static String spaceLanded;

	private JPanel lettersPanel;

	private static JButton[] letterButtons;

	private static JButton startWheel;

	private static JButton solvePhrase;

	private JButton quit;

	private static JTextArea wheelState;

	private static JTextField timeF;

	private JLabel timeL, mancheL;
	
	private static JTextField mancheF;

	static {

		IMG_NAME =
				new String[] {"300B.png", "600R.png", "400C.png", "500V.png",
						"300G.png", "600RS.png", "1000B.png", "PERDER.png", "300AZ.png",
						"400V.png", "700G.png", "500RS.png", "300B1.png", "400R.png",
						"PERDEA.png", "300V.png", "500G.png", "400RS.png", "600B.png",
						"500R.png", "400C1.png", "300V.png", "Jolly.png", "PASSAV.png"};

		VALUES = new HashMap<String, Integer>();

		VALUES.put("300B", new Integer(300));
		VALUES.put("600R", new Integer(600));
		VALUES.put("400C", new Integer(400));
		VALUES.put("500V", new Integer(500));
		VALUES.put("300G", new Integer(300));
		VALUES.put("600RS", new Integer(600));
		VALUES.put("1000B", new Integer(1000));
		VALUES.put("300AZ", new Integer(300));
		VALUES.put("400V", new Integer(400));
		VALUES.put("700G", new Integer(700));
		VALUES.put("500RS", new Integer(500));
		VALUES.put("300B1", new Integer(300));
		VALUES.put("400R", new Integer(400));
		VALUES.put("300V", new Integer(300));
		VALUES.put("500G", new Integer(500));
		VALUES.put("400RS", new Integer(400));
		VALUES.put("600B", new Integer(600));
		VALUES.put("500R", new Integer(500));
		VALUES.put("400C1", new Integer(400));
		VALUES.put("300V", new Integer(300));
		VALUES.put("Jolly", new Integer(0));
		VALUES.put("PASSAV", new Integer(0));
		VALUES.put("PERDEA", new Integer(0));
		VALUES.put("PERDER", new Integer(0));
	}

	/**
	 * costruttore
	 * @param g indica il riferimento all'oggetto di tipo WheelOfFortune associato alla partita
	 * @param fP indica il riferimento all'oggetto di tipo FirstPanel associato al pannello superiore che compone la schermata
	 * @param phP indica il riferimento all'oggetto di tipo PhrasePanel associato al pannello contenente il tabellone raffigurante la frase misteriosa
	 * @param frame indica il riferimento all'oggetto di tipo WheelFrame associato all'intera schermata di gioco
	 * @throws UnsupportedAudioFileException per la gestione delle eccezioni
	 * @throws IOException per la gestione delle eccezioni
	 * @throws LineUnavailableException per la gestione delle eccezioni
	 */
	public WheelPanel(WheelOfFortune g, FirstPanel fP, PhrasePanel phP, WheelFrame frame) throws UnsupportedAudioFileException, 
	IOException, LineUnavailableException {
		super();
		
		game = g;
		firstP = fP;
		phraseP = phP;


		WHEEL_START = new AudioPlayer("suoni/spinningWheel.wav");
		GREAT = new AudioPlayer("suoni/goodGuess.wav");
		BAD_CHOICE = new AudioPlayer("suoni/badGuess.wav");
		//BANKRUPT = new AudioPlayer("suoni/bankrupt.wav");
		VOWELS_TERMINATED = new AudioPlayer("suoni/noMoreVowels.wav");

		Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
		IMAGES = new HashMap<String, Image>();

		for (String imageName : IMG_NAME) {
			IMAGES.put(imageName,defaultToolkit.getImage("immagini/" + imageName));
		}

		IMAGES.put("arrow.png",defaultToolkit.getImage("immagini/arrow.png"));

		rnd = new Random();

		wheelTimer = new Timer(25, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String value = ImagesNames.get(0);
				ImagesNames.remove(0);
				ImagesNames.add(value);
				wheelTimer.setDelay((wheelTimer.getDelay())+5);
				repaint();
				nLaps = rnd.nextInt(150)+200;
				if(wheelTimer.getDelay() >= nLaps) {
					wheelTimer.stop();
					WHEEL_START.pause();
					if(!wheelTimer.isRunning()) {
						spaceLanded = ImagesNames.get(11);
						resultManager(spaceLanded); 
						try {
							String x = spaceLanded.substring(0,spaceLanded.indexOf('.'));
							int v = VALUES.get(x); 
							if(v>0) {
								String s = String.valueOf(v);
								ClientRdF.notifyWheelStop("$"+ s);
							} else {
								if((x.contains("PASSA")) && (game.getBonus()>0)) {
									String s = String.valueOf(v);
									ClientRdF.notifyWheelStop(s);
								}
							}
						} catch (RemoteException e1) {
							e1.printStackTrace();
						}
						wheelTimer.setDelay(25); //reset del timer
					}
				}
			}
		});

		timer = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timeF.setText("" + seconds);
				if(seconds==0) {
					timeF.setText("");
					timer.stop();
					JOptionPane.getRootFrame().dispose();
					if(!checkBonus()) {
						timeOut();
						try {
							ClientRdF.updateMove(WheelOfFortune.getManche(), ClientRdF.email, 0, "TS");
						} catch (RemoteException e1) {
							e1.printStackTrace();
						}
					}
				} else {
					seconds = seconds-1;
				}
			}
		});
		
		timerObs = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timeF.setText("" + seconds);
				if(seconds==0) {
					timeF.setText("");
					timerObs.stop();
				} else {
					seconds = seconds-1;
				}
			}
		});
		
		mancheTimer = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timeF.setText("" + seconds);
				if(seconds==0) {
					addManche();
					newManche();
					timeF.setText("");
					seconds = 5;
					timer.start();
					mancheTimer.stop();
				} else {
					seconds = seconds-1;
				}
			}
		});
		
		mancheTimerObs = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timeF.setText("" + seconds);
				if(seconds==0) {
					updateMancheObs();
					timeF.setText("");
					setWheelState("Manche "+ mancheF.getText() + " iniziata\nIl giocatore ha 5 secondi per girare la ruota");
					startTimerObs(5);
					mancheTimerObs.stop();
				} else {
					seconds = seconds-1;
				}
			}
		});
		
		jollyTimer = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timeF.setText("" + seconds);
				if(seconds==0) {
					JOptionPane.getRootFrame().dispose();
					setWheelState("Hai perso tempo...\nperdi il turno ma non il jolly");
					timeF.setText("");
					jollyTimer.stop();
				} else {
					seconds = seconds-1;
				}
			}
		});

		letterButtons = new JButton[26];
		buttonListener = new ButtonListener();

		for (int i = 0; i < letterButtons.length; i++) {
			JButton b = new JButton("" + (char)(i + 65));
			b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			b.setFont(new Font("Open Sans Light", Font.BOLD, 15));
			b.addMouseListener(new MouseListener() {
				private char c = b.getText().charAt(0);
				private Color previousBG = (c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U') ? Color.GREEN : Color.BLUE;
				
			    @Override
			    public void mousePressed(MouseEvent e) {
			    	if(b.isEnabled()) {
			    		b.setBackground(Color.ORANGE);
			    	}
			    }
				@Override
				public void mouseClicked(MouseEvent e) {}
				@Override
				public void mouseEntered(MouseEvent e) {
					if(b.isEnabled()) {
						b.setBackground(Color.YELLOW);
					}
				}
				@Override
				public void mouseExited(MouseEvent e) {
					if(b.isEnabled()) {
						b.setBackground(previousBG);
					}
				}
				@Override
				public void mouseReleased(MouseEvent e) {
					b.setBackground(previousBG);
				}
			});
			letterButtons[i] = b;
			letterButtons[i].setContentAreaFilled(false);
			letterButtons[i].setOpaque(true);
			letterButtons[i].addActionListener(buttonListener);
			letterButtons[i].setEnabled(false);
		}

		lettersPanel = new JPanel();
		lettersPanel.setPreferredSize(new Dimension(300, 200));
		lettersPanel.setLayout(new GridLayout(6, 5, 2, 2));
		lettersPanel.setBackground(new Color(155, 248, 214));

		//vocali rosse e consonanti blu
		for (int i = 0; i < letterButtons.length; i++) {
			letterButtons[i].setBackground((i == 0 || i == 4 || i == 8 || i == 14 || i == 20) ? Color.GREEN : Color.BLUE);
			lettersPanel.add(letterButtons[i]);
		}
		
		mancheL = new JLabel("Manche:", JLabel.RIGHT);
		mancheL.setFont(FONT);
		
		mancheF = new JTextField("" + WheelOfFortune.getManche(), JTextField.CENTER);
		mancheF.setPreferredSize(new Dimension(20,20));
		mancheF.setEditable(false);
		mancheF.setHorizontalAlignment(SwingConstants.CENTER);
        mancheF.setFont(FONT);
		
		Border style = BorderFactory.createRaisedBevelBorder();
		
		Border bR = BorderFactory.createEtchedBorder(Color.RED, Color.RED.darker());
		Border fS1 = BorderFactory.createCompoundBorder(bR, style);
		
		Border bG = BorderFactory.createEtchedBorder(Color.GREEN, Color.GREEN.darker());
		Border fS2 = BorderFactory.createCompoundBorder(bG, style);
		
		Border bY = BorderFactory.createEtchedBorder(Color.YELLOW, Color.YELLOW.darker());
		Border fS3 = BorderFactory.createCompoundBorder(bY, style);
		
		startWheel = new JButton(" GIRA LA RUOTA ");
		startWheel.addMouseListener(new MouseListener() {
		    @Override
		    public void mousePressed(MouseEvent e) {
		    	Border style = BorderFactory.createLoweredBevelBorder();
				Border border = BorderFactory.createEtchedBorder(Color.GREEN, Color.GREEN);
				Border finalStyle = BorderFactory.createCompoundBorder(border, style);
				startWheel.setBorder(finalStyle);
		    }
			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {
				startWheel.setBorder(fS2);
			}
		});
		startWheel.addActionListener(buttonListener);
		startWheel.setContentAreaFilled(false);
		startWheel.setOpaque(true);
		startWheel.setBackground(Color.GREEN);
		startWheel.setBorder(fS2);
		startWheel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		startWheel.setFont(new Font("Tahoma", Font.BOLD, 12));

		solvePhrase = new JButton(" RISOLVI FRASE ");
		solvePhrase.addMouseListener(new MouseListener() {
		    @Override
		    public void mousePressed(MouseEvent e) {
		    	Border style = BorderFactory.createLoweredBevelBorder();
				Border border = BorderFactory.createEtchedBorder(Color.YELLOW, Color.YELLOW);
				Border finalStyle = BorderFactory.createCompoundBorder(border, style);
				solvePhrase.setBorder(finalStyle);
		    }
			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {
				solvePhrase.setBorder(fS3);
			}
		});
		solvePhrase.addActionListener(buttonListener);
		solvePhrase.setContentAreaFilled(false);
		solvePhrase.setOpaque(true);
		solvePhrase.setBackground(Color.YELLOW);
		solvePhrase.setBorder(fS3);
		solvePhrase.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		solvePhrase.setFont(new Font("Tahoma", Font.BOLD, 12));

		quit = new JButton(" ESCI ");
		quit.addMouseListener(new MouseListener() {
		    @Override
		    public void mousePressed(MouseEvent e) {
		    	Border style = BorderFactory.createLoweredBevelBorder();
				Border border = BorderFactory.createEtchedBorder(Color.RED, Color.RED);
				Border finalStyle = BorderFactory.createCompoundBorder(border, style);
				quit.setBorder(finalStyle);
		    }
			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {
				quit.setBorder(fS1);
			}
		});
		quit.addActionListener(buttonListener);
		quit.setContentAreaFilled(false);
		quit.setOpaque(true);
		quit.setBackground(Color.RED);
		quit.setBorder(fS1);
		quit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		quit.setFont(new Font("Tahoma", Font.BOLD, 12));

		timeL = new JLabel("Timer:", JLabel.LEFT);
		timeL.setFont(new Font("Tahoma", Font.PLAIN, 20));
		timeF = new JTextField();
		timeF.setEditable(false);
		timeF.setMinimumSize(new Dimension(25,23));
		timeF.setMaximumSize(new Dimension(25, 23));
		timeF.setPreferredSize(new Dimension(25,23));
		timeF.setFont(new Font("Tahoma", Font.PLAIN, 20));
		timeF.setEditable(false);

		wheelState = new JTextArea();
		wheelState.setFont(new Font("Tahoma", Font.PLAIN, 18));
		wheelState.setEditable(false);
		wheelState.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		wheelState.setLineWrap(true);
		wheelState.setWrapStyleWord(true);
		
		Box mancheBox = Box.createHorizontalBox();
		mancheBox.add(Box.createHorizontalStrut(50));
		mancheBox.add(mancheL);
		mancheBox.add(Box.createHorizontalStrut(5));
		mancheBox.add(mancheF);
		
		mancheBox.setMaximumSize(new Dimension(270,25));

		Box optionButtonsBox = Box.createVerticalBox();
		optionButtonsBox.add(mancheBox);
		optionButtonsBox.add(Box.createVerticalStrut(30));
		optionButtonsBox.add(startWheel);
		optionButtonsBox.add(Box.createVerticalStrut(10));
		optionButtonsBox.add(solvePhrase);
		optionButtonsBox.add(Box.createVerticalStrut(40));
		optionButtonsBox.add(quit);
		
		optionButtonsBox.setMaximumSize(new Dimension(300, 300));

		Box timerButtonsBox = Box.createHorizontalBox();
		timerButtonsBox.add(timeL);
		timerButtonsBox.add(Box.createHorizontalStrut(5));
		timerButtonsBox.add(timeF);
		timerButtonsBox.add(Box.createHorizontalStrut(150));

		Box letterButtonsBox = Box.createVerticalBox();
		letterButtonsBox.add(Box.createVerticalStrut(50));
		letterButtonsBox.add(lettersPanel);
		letterButtonsBox.add(Box.createVerticalStrut(10));
		letterButtonsBox.add(wheelState);
		letterButtonsBox.add(Box.createVerticalStrut(10));
		letterButtonsBox.add(timerButtonsBox);
		letterButtonsBox.add(Box.createVerticalStrut(200));

		Box outsideBox = Box.createHorizontalBox();
		outsideBox.add(Box.createHorizontalStrut(20));
		outsideBox.add(optionButtonsBox);
		outsideBox.add(Box.createHorizontalStrut(550));
		outsideBox.add(letterButtonsBox);
		outsideBox.add(Box.createHorizontalStrut(20));
		
		outsideBox.setPreferredSize(new Dimension(1000, 450));

		add(outsideBox);
		
		setBackground(new Color(155, 248, 214));

		newGame();
	}

	/**
	 * metodo utilizzato per resettare i valori della schermata e richiamare il metodo newGame della classe PhrasePanel
	 */
	public void newGame() {
		wheelState.setText("Benvenuti a: LA RUOTA DELLA FORTUNA!");
		firstP.resetScore();

		phraseP.newGame();

		ImagesNames = new ArrayList<String>();

		for (String name : IMG_NAME) {
			ImagesNames.add(name);
		}

		disableConsonants(false);
		disableVowels(false);
		solvePhrase.setEnabled(false);
		startWheel.setEnabled(true);

		repaint();
	}

	@Override
	/**
	 * metodo utilizzato per stampare le immagini che compongono la ruota della fortuna 
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2D = (Graphics2D)g.create();

		// imposta l'origine e ruota
		g2D.translate(485, 190);
		g2D.rotate(Math.toRadians(-107));

		// Disegno lo spazio per la ruota
		for (int i = 0; i < ImagesNames.size(); ++i) {
			g2D.drawImage(IMAGES.get(ImagesNames.get(i)), -29, -25, this);  
			g2D.rotate(Math.toRadians(-DEGREES));
		}

		// Disegna il puntatore della ruota
		g.drawImage(IMAGES.get("arrow.png"), 275, 180, this);

		// reset dell'origine
		g2D.translate(-485, -285);
	}

	/**
	 * classe per la gestione dei comandi associati ad ogni oggetto di tipo JButton presente nella schermata di gioco
	 * @author Luca Cremonesi
	 * @version 1.0
	 */
	private class ButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton source = (JButton)e.getSource();

			for (int c = 0; c < letterButtons.length; c++) {
				if (source == letterButtons[c]) {	
					timer.stop();
					timeF.setText("");
					int occorrenze = 0;
					char l = (char)(c+65);
					String s = "" + l;
					if (c == 0 || c == 4 || c == 8 || c == 14 || c == 20) {
						if(WheelOfFortune.isAllVowelsGuessed()) {
							BAD_CHOICE.play();
							PopUp pu = new PopUp("Vocali Terminate", "Non puoi selezionare le vocali se sono finite...", Color.RED, null);
							pu.add();
							checkBonus();
						} else {
							if(wheelState.getText().contains("consonante")) {
								BAD_CHOICE.play();
								PopUp pu = new PopUp("Lettera Errata", "Hai selezionato una vocale...", Color.RED, null);
								pu.add();
								checkBonus();
							} else if(firstP.getScore()<1000){
								BAD_CHOICE.play();
								PopUp pu = new PopUp("Punteggio Insufficiente", "Non hai un ammontare di punti sufficiente", Color.RED, null);
								pu.add();
								checkBonus();
							} else if(game.getGuessedLetters().contains(l)) {
								BAD_CHOICE.play();
								PopUp pu = new PopUp("Lettera Ripetuta", "La lettera selezionata è già stata inserita in precedenza", Color.RED, null);
								pu.add();
								try {
									ClientRdF.updateMove(WheelOfFortune.getManche(),ClientRdF.email, 0 , "LR");
								} catch (RemoteException e1) {
									e1.printStackTrace();
								}
								checkBonus();
							} else {
								occorrenze = game.revealsLetter((char)(c + 65));
								firstP.scoreAdder(-1000); //sottrae 1000$ per ogni vocale comprata
								
								if(WheelOfFortune.isAllVowelsGuessed()) {
									VOWELS_TERMINATED.play();
									PopUp pu = new PopUp("Vocali Terminate", "Attenzione, le vocali sono terminate \nevita di selezionarle", Color.RED, null);
									pu.add();
									try {
										ClientRdF.updateMove(WheelOfFortune.getManche(),ClientRdF.email, -1000 ,s+"allvoc");
									} catch (RemoteException e1) {
										e1.printStackTrace();
									}
								} else {
									try {
										ClientRdF.updateMove(WheelOfFortune.getManche(),ClientRdF.email, -1000 ,s+"voc");
									} catch (RemoteException e1) {
										e1.printStackTrace();
									}
								}
								
								if (occorrenze > 0) {
									GREAT.play();
									disableLetters(false);
									phraseP.repaint();
									wheelState.setText((occorrenze == 1 ? "C'è " : "Ci sono ")
											+ occorrenze + " " + (char)(c + 65)
											+ " nella frase! Gira la ruota");
									seconds = 5;
									timer.start();
								} else {
									BAD_CHOICE.play();
									if(!checkBonus()) {
										wheelState.setText("Ci dispiace, non ci sono "
												+ (char)(c + 65) + " nella frase");
									}
								}
							}
						}
					} else {
						//se la lettera è una consonante
						if(WheelOfFortune.isAllConsonantsGuessed()) {
							BAD_CHOICE.play();
							PopUp pu = new PopUp("Consonanti Terminate", "Non puoi selezionare le consonanti se sono finite...", Color.RED, null);
							pu.add();
							checkBonus();
						} else if(game.getGuessedLetters().contains(l)) {
							BAD_CHOICE.play();
							PopUp pu = new PopUp("Lettera Ripetuta", "La lettera selezionata è già stata inserita in precedenza", Color.RED, null);
							pu.add();
							try {
								ClientRdF.updateMove(WheelOfFortune.getManche(),ClientRdF.email, 0 , "LR");
							} catch (RemoteException e1) {
								e1.printStackTrace();
							}
							checkBonus();
						} else {
							solvePhrase.setEnabled(true);
							startWheel.setEnabled(true);
							occorrenze = game.revealsLetter((char)(c + 65));
							phraseP.repaint();
							if (occorrenze > 0) {
								seconds = 5;
								timer.start();
								GREAT.play();

								int punti = VALUES.get(spaceLanded.substring(0,spaceLanded.indexOf('.')));

								firstP.scoreAdder(punti * occorrenze);
								
								if(WheelOfFortune.isAllConsonantsGuessed()) {
									VOWELS_TERMINATED.play();
									PopUp pu = new PopUp("Consonanti Terminate", "Attenzione, le consonanti sono terminate \nevita di selezionarle", Color.RED, null);
									pu.add();
									try {
										ClientRdF.updateMove(WheelOfFortune.getManche(),ClientRdF.email, (punti * occorrenze),s+"allcns");
									} catch (RemoteException e1) {
										e1.printStackTrace();
									}
								} else {
									try {
										ClientRdF.updateMove(WheelOfFortune.getManche(),ClientRdF.email, (punti * occorrenze),s+"cns");
									} catch (RemoteException e1) {
										e1.printStackTrace();
									}
								}

								wheelState.setText((occorrenze == 1 ? "C'è" : "Ci sono") + " "
										+ occorrenze + " " + (char)(c + 65)
										+ " nella frase! Hai guadagnato $" + punti * occorrenze
										+ "! Gira ancora la ruota o "
										+ "compra una vocale");
								
								disableConsonants(false);
							} else {
								BAD_CHOICE.play();
								try {
									ClientRdF.updateMove(WheelOfFortune.getManche(),ClientRdF.email, 0 ,s);
								} catch (RemoteException e1) {
									e1.printStackTrace();
								}
								PopUp pu = new PopUp("Lettera Errata", "Ci dispiace, la lettera non è presente nella frase misteriosa", Color.RED, null);
								pu.add();
								checkBonus();
							}
						}
						
					}
				}
			}


			if (source == startWheel) {
				if(WheelOfFortune.isAllConsonantsGuessed()) {
					PopUp pu = new PopUp("Consonanti Terminate", "Non puoi girare la ruota se le consonanti sono finite...\nil turno passa al prossimo giocatore", Color.RED, null);
					pu.add();
					try {
						ClientRdF.changeTurn();
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
				} else {
					stopTimer();
					try {
						ClientRdF.updateMove(WheelOfFortune.getManche(), ClientRdF.email, 0, "GR");
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
					WHEEL_START.loop();
					wheelTimer.start();
					disableBottons(false);
					wheelState.setText("La ruota gira...");
				}
			}else if (source == solvePhrase) {
				try {
					ClientRdF.updateMove(WheelOfFortune.getManche(), ClientRdF.email, 0, "RF");
				} catch (RemoteException e2) {
					e2.printStackTrace();
				}
				solvePhrase.setEnabled(false);
				if(timer.isRunning()) {
					timer.stop();
					timeF.setText("");
				} 
				seconds = 10;
				timer.start();
				
				String risolvi = JOptionPane.showInputDialog(null,"Perfavore, risolvi la frase: ",
						"Risolvi la Frase",JOptionPane.PLAIN_MESSAGE);
				
				if (risolvi != null) {
					risolvi = risolvi.replaceAll("'", " ");
					risolvi = risolvi.replaceAll("è", "e");
					risolvi = risolvi.replaceAll("à", "a");
					risolvi = risolvi.replaceAll("ì", "i");
					risolvi = risolvi.replaceAll("ò", "o");
					risolvi = risolvi.replaceAll("ù", "u");
					risolvi = risolvi.toUpperCase();
					
					StringBuilder risolviT = new StringBuilder();
					String frase = game.getPhrase();
					StringBuilder fraseT = new StringBuilder();
					for (int i = 0; i < frase.length(); ++i) {
						if (frase.charAt(i) != ' ') {
							fraseT.append(frase.charAt(i)); //aggiunge la sequenza di lettere di frase a fraseT
						}
					}
					
					for (int i = 0; i < risolvi.length(); ++i) {
						if (risolvi.charAt(i) != ' ') {
							risolviT.append(risolvi.charAt(i)); //aggiunge la sequenza di lettere inserite a risolviT
						}
					}

					if (risolviT.toString() != "") {
						timer.stop();
						timeF.setText("");
						if (risolviT.toString().compareToIgnoreCase(fraseT.toString()) == 0) { //se risolviT è uguale a fraseT
							victory();
						} else {
							disableBottons(false);
							try {
								ClientRdF.updateMove(WheelOfFortune.getManche(), ClientRdF.email, 0, "ERR");
							} catch (RemoteException e2) {
								e2.printStackTrace();
							}
							PopUp p = new PopUp("Frase Errata", "Ci dispiace, la frase inserita non corrisponde \nalla frase misteriosa", Color.RED, null);
							p.add();
							wheelState.setText("Frase errata, hai perso il turno.");
							checkBonus();
						}
					}
				} else if (risolvi == null){
					if(timer.isRunning()) {
						timer.stop();
						timeF.setText("");
						try {
							ClientRdF.updateMove(WheelOfFortune.getManche(), ClientRdF.email, 0, "ERR");
						} catch (RemoteException e2) {
							e2.printStackTrace();
						}
						PopUp pu = new PopUp("Azione Proibita", "Non puoi tornare indietro!\nIl turno passa al prossimo giocatore", Color.RED, null);
						pu.add();
						checkBonus();
					}
				}

			} else if (source == quit) {
				JOptionPane.getRootFrame().dispose();
				stopWheelTimer();
				stopTimer();
				stopTimerObs();
				stopMancheTimerObs();
				WheelFrame.disposeFrame();
				new PlayerFrame();
				try {
					ClientRdF.updateMove(WheelOfFortune.getManche(),ClientRdF.email, 0, "esce");
					ClientRdF.endGame();
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}	
		}
	} 

	/**
	 * metodo utilizzato per la gestione dell'evento 'vittoria manche'/'vittoria partita'
	 */
	private void victory() { //controllo vincitore da aggiungere
		if(WheelOfFortune.getManche() == 5){
			firstP.addTotScore(firstP.getScore());
			try {
				String winner = ClientRdF.victory(firstP.getScore());
				Close();
				new FinalFrame();
				if(winner.equals(ClientRdF.email)) {
					PopUp pu = new PopUp("Vittoria Partita", "Congratulazioni, hai vinto l'ultima mance e la partita!", Color.GREEN, null);
					pu.add();
				} else {
					PopUp pu = new PopUp("Game Over", "Hai vinto l'ultima mance ma hai perso la partita!", Color.RED, null);
					pu.add();
				}
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		} else {
			revealPhrase();
			firstP.addTotScore(firstP.getScore()); 
			PhrasePanel.resetPoints();
			PopUp pu = new PopUp("Vittoria Manche", "Congratulazioni, hai vinto la mance " + WheelOfFortune.getManche() + "!", Color.GREEN, null);
			pu.add();
			wheelState.setText("Hai vinto la mance numero " + WheelOfFortune.getManche()+"\nLa prossima manche inizia tra 10 secondi");
			disableConsonants(false);
			disableVowels(false);
			solvePhrase.setEnabled(false);
			startWheel.setEnabled(false);
			try {
				ClientRdF.updateManche(firstP.getScore());
			} catch (RemoteException exc) {
				exc.printStackTrace();
			}
			firstP.resetScore();
			seconds = 10;
			mancheTimer.start();
		}
	}
	
	/**
	 * metodo che richiama il metodo revealsPhrase della classe WheelOfFortune utilizzato per rivelare l'intera frase misteriosa
	 */
	public static void revealPhrase() {
		game.revealsPhrase();
		phraseP.repaint();
	}

	/**
	 * metodo utilizzato per il cambio di manche per i giocatori che non sono in possesso del turno
	 */
	public static void updateMancheObs() {
		addManche();
		newManche();
		startWheel.setEnabled(false);
	}

	/**
	 * metodo utilizzato per verificare se il giocatore possiede almeno un jolly in caso di errore o di pescata di 'PASSA'
	 * @return true se viene utilizzato un jolly, false nel caso contrario o se il giocatore non ne è in possesso
	 */
	private boolean checkBonus() {
		if(game.getBonus() > 0) {
			wheelState.setText("Ci dispiace, hai perso il turno.\nMa hai la possibilità di utilizzare il jolly");
			stopTimer();
			stopTimerObs();
			startJollyTimer(5);
			JOptionPane.getRootFrame().dispose();
			int r = JOptionPane.showConfirmDialog(null, "Vuoi utilizzare il Jolly?");
			if(r==0) { //il giocatore mantiene il turno
				stopJollyTimer();
				try {
					ClientRdF.updateMove(WheelOfFortune.getManche(),ClientRdF.email, 0 ,"UJ");
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
				firstP.diminuisciBonus();
				startWheel.setEnabled(true);
				solvePhrase.setEnabled(true);
				wheelState.setText("Hai utilizzato il Jolly!\nOra puoi continuare a giocare il tuo turno");
				startTimer(5);
				return true;
			} else {
				stopJollyTimer();
				disableBottons(false);
				try {
					ClientRdF.changeTurn();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				return false;
			}
		} else {
			disableBottons(false);
			try {
				ClientRdF.changeTurn();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			return false;
		}
	}

	/**
	 * metodo utilizzato per la gestione dei risultati nei quali può fermarsi la ruota della fortuna
	 * @param spaceLanded indica il risultato ottenuto girando la ruota
	 */
	private void resultManager(String spaceLanded) {
		if (spaceLanded.equals("PASSAV.png")) { 
			if(!checkBonus()) {
				try {
					ClientRdF.updateMove(WheelOfFortune.getManche(),ClientRdF.email, 0 ,"Passa");
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		} else if (spaceLanded.contains("PERDE")) {
			disableBottons(false);
			firstP.resetScore();
			wheelState.setText("Ci dispiace, hai perso un turno, e "
					+ "il tuo punteggio scende a 0");
			PopUp pu = new PopUp("Che Sfortuna!", "Hai pescato il PERDE e quindi il tuo punteggio scende a zero;\nIl turno passa al prossimo giocatore", Color.RED, null);
			pu.add();
			try {
				ClientRdF.updateMove(WheelOfFortune.getManche(),ClientRdF.email, 0 ,"Perde");
				ClientRdF.changeTurn();
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		} else if (spaceLanded.equals("Jolly.png")){
			firstP.aggiungiBonus();
			startWheel.setEnabled(true);
			solvePhrase.setEnabled(true);
			disableLetters(false);
			try {
				ClientRdF.updateMove(WheelOfFortune.getManche(), ClientRdF.email, 0, "TJ");
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			wheelState.setText("Hai guadagnato un Jolly!\nGira La ruota per continuare");
			startTimer(5);
		}else {
			startWheel.setEnabled(false); 
			disableConsonants(true);
			disableVowels(true);
			if(WheelOfFortune.isAllConsonantsGuessed()) {
				wheelState.setText("Consonanti terminate, seleziona una vocale");
			} else {
				wheelState.setText("Seleziona una consonante");
			}
			startTimer(5);
		}
	}

	/**
	 * metodo utilizzato per incrementare la manche nel pannello di gioco e richiamare il metodo addManche della classe WheelOfFortune
	 */
	private static void addManche() {
		game.addManche();
		mancheF.setText("" + WheelOfFortune.getManche());
	}
	
	/**
	 * metodo utilizzato per la gestione dell'evento 'tempo scaduto'
	 */
	private void timeOut() {
		PopUp pu = new PopUp("Tempo Scaduto", "Ci dispiace, il tempo è scaduto", Color.RED, null);
		pu.add();
		disableBottons(false);
		timeF.setText("");
	}

	/**
	 * metodo utilizzato per disporre la schermata di gioco
	 * @throws RemoteException per la gestione delle eccezioni
	 */
	public static void Close() throws RemoteException {
		stopWheelTimer();
		stopTimer();
		stopTimerObs();
		stopMancheTimerObs();
		JOptionPane.getRootFrame().dispose();
		WheelFrame.disposeFrame();
	}

	/**
	 * metodo utilizzato per aggiornare il pannello della frase misteriosa
	 * @param name indica la stringa che al primo elemento contiene la lettera da rivelare
	 */
	public static void updatePanel(String name) {
		char n = name.charAt(0);				
		game.revealsLetter(n);
		phraseP.repaint();
	}
	
	/**
	 * metodo utilizzato per impostare il testo visualizzato dai giocatori che accompagna il gioco
	 * @param s indica la stringa da impostare come testo
	 */
	public static void setWheelState(String s) {
		wheelState.setText(s);
	}
	
	/**
	 * metodo utilizzato per concatenare una nuova stringa al testo visualizzato dai giocatori
	 * @param s indica la stringa da concatenare
	 */
	public static void addWheelStateTxt(String s) {
		String txt = wheelState.getText();
		wheelState.setText(txt + "\n" + s);
	}

	/**
	 * metodo utilizzato per disporre la schermata di caso di uscita di un giocatore dalla partita
	 * @param nick indica il soprannome del giocatore che è uscito dalla partita
	 */
	public static void quit(String nick) {
		stopWheelTimer();
		stopTimer();
		stopTimerObs();
		stopMancheTimerObs();
		WheelFrame.disposeFrame();
		new PlayerFrame();
		PopUp p = new PopUp("Partita Terminata", "Ci dispiace, il giocatore " + nick + "\nè uscito dalla partita", Color.RED, null);
		p.add();
	}

	/**
	 * metodo utilizzato per gestire la schermata in caso di cambio di manche
	 */
	private static void newManche() {
		if(WheelOfFortune.getManche() == 5) {
			wheelState.setText("Inizia l'ultima manche!");
		} else {
			wheelState.setText("Inizia la manche numero " + WheelOfFortune.getManche());
		}
		firstP.resetScore();
		phraseP.newGame();
		startWheel.setEnabled(true);
	}

	/**
	 * metodo utilizzato per attivare/disattivare tutti i bottoni associati ad una lettera dell'alfabeto, della schermata di gioco
	 * @param b indica un valore boolean: true per attivare i tasti, false per disattivarli 
	 */
	public static void disableLetters(boolean b) {
		disableConsonants(b);
		disableVowels(b);
	}

	/**
	 * metodo utilizzato per attivare/disattivare i bottoni, della schermata di gioco, che si riferiscono alle vocali 
	 * @param b indica un valore booleano: true per attivare i tasti, false per disattivarli 
	 */
	public static void disableVowels(boolean b) {
		letterButtons[0].setEnabled(b);
		letterButtons[4].setEnabled(b);
		letterButtons[8].setEnabled(b);
		letterButtons[14].setEnabled(b);
		letterButtons[20].setEnabled(b);
	}

	/**
	 * metodo utilizzato per attivare/disattivare i bottoni, della schermata di gioco, associati ad una consonante
	 * @param b indica un valore booleano: true per attivare i tasti, false per disattivarli
	 */
	public static void disableConsonants(boolean b) {
		for (int i = 0; i < letterButtons.length; ++i) {
			if (!(i == 0 || i == 4 || i == 8 || i == 14 || i == 20)) {
				letterButtons[i].setEnabled(b);
			}
		}
	}

	/**
	 * metodo utilizzato per attivare/disattivare tutti i bottoni della schermata di gioco, ad eccezione del tasto per uscire dalla partita
	 * @param b indica un valore booleano: true per attivare i tasti, false per disattivarli
	 */
	public static void disableBottons(boolean b) {
		disableConsonants(b);
		disableVowels(b);
		solvePhrase.setEnabled(b);
		startWheel.setEnabled(b);
	}

	/**
	 * metodo utilizzato per attivare/disattivare il bottone che permette di girare la ruota
	 * @param b indica un valore booleano: true per attivare il tasto, false per disattivarlo
	 */
	public static void wheelstart(boolean b) {
		startWheel.setEnabled(b);
	}
	
	/**
	 * metodo utilizzato per attivare/disattivare il bottone che permette di risolvere la frase misteriosa
	 * @param b indica un valore booleano: true per attivare il tasto, false per disattivarlo
	 */
	public static void unlockSolvePhrase(boolean b) {
		solvePhrase.setEnabled(b);
	}
	
	/**
	 * metodo utilizzato per far partire l'oggetto Timer 'mancheTimerObs'
	 * @param s indica i secondi
	 */
	public static void startMancheTimerObs(int s) {
		stopAllTimer();
		seconds = s;
		mancheTimerObs.start();
	}
	
	/**
	 * metodo utilizzato per interrompere l'oggetto Timer 'mancheTimerObs'
	 */
	public static void stopMancheTimerObs() {
		if(mancheTimerObs.isRunning()) {
			mancheTimerObs.stop();
			timeF.setText("");
		}
	}
	
	/**
	 * metodo utilizzato per far partire l'oggetto Timer 'timerObs'
	 * @param s indica i secondi
	 */
	public static void startTimerObs(int s) {
		stopAllTimer();
		seconds = s;
		timerObs.start();
	}
	
	/**
	 * metodo utilizzato per interrompere l'oggetto Timer 'timerObs'
	 */
	public static void stopTimerObs() {
		if(timerObs.isRunning()) {
			timerObs.stop();
			timeF.setText("");
		}
	}
	
	/**
	 * metodo utilizzato per far partire l'oggetto Timer 'timer'
	 * @param s indica i secondi
	 */
	public static void startTimer(int s) {
		stopAllTimer();
		seconds = s;
		timer.start();
	}
	
	/**
	 * metodo utilizzato per interrompere l'oggetto Timer 'timer'
	 */
	public static void stopTimer() {
		if(timer.isRunning()) {
			timer.stop();
			timeF.setText("");
		}
	}
	
	/**
	 * metodo utilizzato per interrompere l'oggetto Timer 'wheelTimer'
	 */
	public static void stopWheelTimer() {
		if(wheelTimer.isRunning()) {
			wheelTimer.stop();
		}
	}
	
	/**
	 * metodo utilizzato per far partire l'oggetto Timer 'jollyTimer'
	 * @param s indica i secondi
	 */
	public static void startJollyTimer(int s) {
		seconds = s;
		jollyTimer.start();
	}
	
	/**
	 * metodo utilizzato per interrompere l'oggetto Timer 'jollyTimer'
	 */
	public static void stopJollyTimer() {
		if(jollyTimer.isRunning()) {
			jollyTimer.stop();
			timeF.setText("");
		}
	}
	
	public static void stopAllTimer() {
		if(timer.isRunning()) {
			timer.stop();
		} else if(mancheTimer.isRunning()) {
			mancheTimer.stop();
		} else if(timerObs.isRunning()) {
			timerObs.stop();
		} else if(mancheTimerObs.isRunning()) {
			mancheTimerObs.stop();
		} else if(jollyTimer.isRunning()) {
			jollyTimer.stop();
		}
		timeF.setText("");
	}
}

