package gameGUI;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.MatteBorder;

import game.AdminRdF;
import game.PopUp;

/**
 * classe per la visualizzazione del frame relativo alla schermata per gli osservatori della partita
 * @author Luca Cremonesi
 * @version 1.0
 */
public class ObserverFrame {
	private static JFrame frame;
	private static ObserverPhrasePanel phrase;
	private static JTextField nickF1;
	private static ArrayList<String> players;
	private static JTextField bonusF1;
	private static JTextField bankF1;
	private static JTextField scoreF1;
	private static JTextField nickF2;
	private static JTextField bonusF2;
	private static JTextField bankF2;
	private static JTextField scoreF2;
	private static JTextField nickF3;
	private static JTextField bonusF3;
	private static JTextField bankF3;
	private static JTextField scoreF3;
	private static JTextField mancheF;
	private static JLabel txt;
	private static JTextField timerF;
	private static Timer timer, mancheTimer;
	private static int seconds;

	/**
	 * costruttore
	 * @param type indica il tipo (0 se l'osservatore era presente nella lobby, 1 se l'osservatore entra a partita in corso)
	 * @throws RemoteException per la gestione delle eccezioni
	 */
	public ObserverFrame(int type) throws RemoteException {
		timer = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timerF.setText("" + seconds);
				if(seconds==0) {
					timerF.setText("");
					timer.stop();
				}  
				seconds = seconds-1;
			}
		});
		
		mancheTimer = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timerF.setText("" + seconds);
				if(seconds==0) {
					newPhrase();
					increaseNManche();
					timerF.setText("");
					mancheTimer.stop();
					setTextArea("Manche "+ mancheF.getText() + " iniziata\nIl giocatore ha 5 secondi per girare la ruota");
					startTimer(5);
				}  
				seconds = seconds-1;
			}
		});
		
		players = AdminRdF.getPlayerList();
		phrase = new ObserverPhrasePanel();
		String owner = AdminRdF.getSt().getFirstTurn(AdminRdF.idGame);
		String[] ownerData = AdminRdF.getSt().getData(owner);
		String ownerNick = ownerData[2];
		initialize(ownerNick, type);
	}

	/**
	 * metodo utilizzato per inizializzare il frame
	 * @param nickname indica il soprannome dell'osservatore
	 * @param type indica il tipo (0/1)
	 * @throws RemoteException per la gestione delle eccezioni
	 */
	private void initialize(String nickname, int type) throws RemoteException {
		frame = new JFrame("Schermata Osservatore");
		frame.getContentPane().setBackground(new Color(155, 248, 214));
		
		try {
			BufferedImage image = ImageIO.read(new File("immagini/IconaRDF.png"));
			frame.setIconImage(image);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String nick = nickname;
		
		txt = new JLabel("");
		txt.setAutoscrolls(true);
		txt.setBorder(new MatteBorder(3, 3, 3, 3, (Color) Color.GREEN));
		txt.setOpaque(true);
		txt.setHorizontalAlignment(SwingConstants.CENTER);
		txt.setBackground(Color.WHITE);
		txt.setFont(new Font("Tahoma", Font.PLAIN, 20));
		txt.setBounds(194, 300, 655, 63);
		frame.getContentPane().add(txt);
		
		if(type == 0) {
			setTextArea("La partita è iniziata ed il turno è stato assegnato a "+ nick +"\nIn attesa di una mossa...");
		} else if (type == 1){
			setTextArea("Turno in mano a "+nick+"\nIn attesa di una mossa...");
		}
				
		
		Border style = BorderFactory.createRaisedBevelBorder();
		Border border = BorderFactory.createEtchedBorder(Color.RED, Color.RED.darker());
		Border finalStyle = BorderFactory.createCompoundBorder(border, style);
		
		phrase.setBounds(0,0,1023,261);
		frame.getContentPane().add(phrase);
		
		frame.setBounds(0, 0, 1050, 720);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel nick1L = new JLabel("Player:");
		nick1L.setFont(new Font("Tahoma", Font.PLAIN, 20));
		nick1L.setBounds(100, 417, 62, 30);
		frame.getContentPane().add(nick1L);
		
		nickF1 = new JTextField(players.get(0));
		nickF1.setBackground(Color.WHITE);
		if(players.get(0).equals(nick)) {
			nickF1.setForeground(Color.GREEN);
		}
		nickF1.setHorizontalAlignment(SwingConstants.CENTER);
		nickF1.setEditable(false);
		nickF1.setBounds(172, 417, 120, 30);
		nickF1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		frame.getContentPane().add(nickF1);
		nickF1.setColumns(10);
		
		JLabel bonus1L = new JLabel(new ImageIcon("immagini/bonus_img.png"));
		bonus1L.setBounds(116, 458, 46, 30);
		frame.getContentPane().add(bonus1L);
		
		JLabel bank1L = new JLabel(new ImageIcon("immagini/depos.png"));
		bank1L.setBounds(116, 499, 46, 30);
		frame.getContentPane().add(bank1L);
		
		JLabel score1L = new JLabel(new ImageIcon("immagini/points.png"));
		score1L.setBounds(116, 540, 46, 30);
		frame.getContentPane().add(score1L);
		
		bonusF1 = new JTextField(""+ AdminRdF.getSt().getJolly(AdminRdF.idGame, players.get(0)));
		bonusF1.setHorizontalAlignment(SwingConstants.CENTER);
		bonusF1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		bonusF1.setBackground(Color.WHITE);
		bonusF1.setEditable(false);
		bonusF1.setBounds(172, 458, 120, 30);
		frame.getContentPane().add(bonusF1);
		bonusF1.setColumns(10);
		
		try {
			bankF1 = new JTextField("$" + AdminRdF.getSt().getBank(AdminRdF.idGame, players.get(0)));
			bankF1.setHorizontalAlignment(SwingConstants.CENTER);
			bankF1.setFont(new Font("Tahoma", Font.PLAIN, 20));
			bankF1.setBackground(Color.WHITE);
			bankF1.setEditable(false);
			bankF1.setBounds(172, 499, 120, 30);
			frame.getContentPane().add(bankF1);
			bankF1.setColumns(10);

			scoreF1 = new JTextField("$" + AdminRdF.getSt().getScore(AdminRdF.idGame, players.get(0)));
			scoreF1.setHorizontalAlignment(SwingConstants.CENTER);
			scoreF1.setFont(new Font("Tahoma", Font.PLAIN, 20));
			scoreF1.setBackground(Color.WHITE);
			scoreF1.setEditable(false);
			scoreF1.setBounds(172, 540, 120, 30);
			frame.getContentPane().add(scoreF1);
			scoreF1.setColumns(10);

		} catch (RemoteException e2) {
			e2.printStackTrace();
		}
		
		JLabel nick2L = new JLabel("Player:");
		nick2L.setFont(new Font("Tahoma", Font.PLAIN, 20));
		nick2L.setBounds(428, 417, 62, 30);
		frame.getContentPane().add(nick2L);
		
		JLabel bonus2L = new JLabel(new ImageIcon("immagini/bonus_img.png"));
		bonus2L.setBounds(444, 458, 46, 30);
		frame.getContentPane().add(bonus2L);
		
		JLabel bank2L = new JLabel(new ImageIcon("immagini/depos.png"));
		bank2L.setBounds(444, 499, 46, 30);
		frame.getContentPane().add(bank2L);
		
		JLabel score2L = new JLabel(new ImageIcon("immagini/points.png"));
		score2L.setBounds(444, 540, 46, 30);
		frame.getContentPane().add(score2L);
		
		nickF2 = new JTextField(players.get(1));
		nickF2.setBackground(Color.WHITE);
		if(players.get(1).equals(nick)) {
			nickF2.setForeground(Color.GREEN);
		}
		nickF2.setEditable(false);
		nickF2.setBounds(500, 417, 120, 30);
		nickF2.setHorizontalAlignment(SwingConstants.CENTER);
		nickF2.setFont(new Font("Tahoma", Font.PLAIN, 20));
		frame.getContentPane().add(nickF2);
		nickF2.setColumns(10);
		bonusF2 = new JTextField(""+ AdminRdF.getSt().getJolly(AdminRdF.idGame, players.get(1)));
		bonusF2.setFont(new Font("Tahoma", Font.PLAIN, 20));
		bonusF2.setHorizontalAlignment(SwingConstants.CENTER);
		bonusF2.setBackground(Color.WHITE);
		bonusF2.setEditable(false);
		bonusF2.setBounds(500, 458, 120, 30);
		frame.getContentPane().add(bonusF2);
		bonusF2.setColumns(10);
		
		try {			
			bankF2 = new JTextField("$"+ AdminRdF.getSt().getBank(AdminRdF.idGame, players.get(1)));
			bankF2.setHorizontalAlignment(SwingConstants.CENTER);
			bankF2.setFont(new Font("Tahoma", Font.PLAIN, 20));
			bankF2.setBackground(Color.WHITE);
			bankF2.setEditable(false);
			bankF2.setBounds(500, 499, 120, 30);
			frame.getContentPane().add(bankF2);
			bankF2.setColumns(10);

			scoreF2 = new JTextField("$"+ AdminRdF.getSt().getScore(AdminRdF.idGame, players.get(1)));
			scoreF2.setFont(new Font("Tahoma", Font.PLAIN, 20));
			scoreF2.setHorizontalAlignment(SwingConstants.CENTER);
			scoreF2.setBackground(Color.WHITE);
			scoreF2.setEditable(false);
			scoreF2.setBounds(500, 540, 120, 30);
			frame.getContentPane().add(scoreF2);
			scoreF2.setColumns(10);

		} catch (RemoteException e2) {
			e2.printStackTrace();
		}
		
		JLabel nick3L = new JLabel("Player:");
		nick3L.setFont(new Font("Tahoma", Font.PLAIN, 20));
		nick3L.setBounds(752, 417, 62, 30);
		frame.getContentPane().add(nick3L);
		
		nickF3 = new JTextField(players.get(2));
		nickF3.setHorizontalAlignment(SwingConstants.CENTER);
		nickF3.setBackground(Color.WHITE);
		if(players.get(2).equals(nick)) {
			nickF3.setForeground(Color.GREEN);
		}
		nickF3.setEditable(false);
		nickF3.setBounds(824, 417, 120, 30);
		nickF3.setFont(new Font("Tahoma", Font.PLAIN, 20));
		frame.getContentPane().add(nickF3);
		nickF3.setColumns(10);
		
		JLabel bonus3L = new JLabel(new ImageIcon("immagini/bonus_img.png"));
		bonus3L.setBounds(768, 458, 46, 30);
		frame.getContentPane().add(bonus3L);
		
		JLabel bank3L = new JLabel(new ImageIcon("immagini/depos.png"));
		bank3L.setBounds(768, 499, 46, 30);
		frame.getContentPane().add(bank3L);
		
		JLabel score3L = new JLabel(new ImageIcon("immagini/points.png"));
		score3L.setBounds(768, 540, 46, 30);
		frame.getContentPane().add(score3L);
		
		bonusF3 = new JTextField(""+ AdminRdF.getSt().getJolly(AdminRdF.idGame, players.get(1)));
		bonusF3.setHorizontalAlignment(SwingConstants.CENTER);
		bonusF3.setFont(new Font("Tahoma", Font.PLAIN, 20));
		bonusF3.setBackground(Color.WHITE);
		bonusF3.setEditable(false);
		bonusF3.setBounds(824, 458, 120, 30);
		frame.getContentPane().add(bonusF3);
		bonusF3.setColumns(10);
		
		try {
			bankF3 = new JTextField("$"+ AdminRdF.getSt().getBank(AdminRdF.idGame, players.get(2)));
			bankF3.setFont(new Font("Tahoma", Font.PLAIN, 20));
			bankF3.setHorizontalAlignment(SwingConstants.CENTER);
			bankF3.setBackground(Color.WHITE);
			bankF3.setEditable(false);
			bankF3.setBounds(824, 499, 120, 30);
			frame.getContentPane().add(bankF3);
			bankF3.setColumns(10);

			scoreF3 = new JTextField("$"+ AdminRdF.getSt().getScore(AdminRdF.idGame, players.get(2)));
			scoreF3.setHorizontalAlignment(SwingConstants.CENTER);
			scoreF3.setFont(new Font("Tahoma", Font.PLAIN, 20));
			scoreF3.setBackground(Color.WHITE);
			scoreF3.setEditable(false);
			scoreF3.setBounds(824, 540, 120, 30);
			frame.getContentPane().add(scoreF3);
			scoreF3.setColumns(10);
		} catch (RemoteException e2) {
			e2.printStackTrace();
		}
		
		JButton quit = new JButton("ESCI");
		quit.setBounds(488, 630, 89, 23);
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
				Border style = BorderFactory.createRaisedBevelBorder();
				Border border = BorderFactory.createEtchedBorder(Color.RED, Color.RED.darker());
				Border finalStyle = BorderFactory.createCompoundBorder(border, style);
				quit.setBorder(finalStyle);
				frame.dispose();
				new AdminFrame();
				try {
					AdminRdF.exitObserver();
					AdminRdF.removeMe();
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		});
		quit.setContentAreaFilled(false);
		quit.setOpaque(true);
		quit.setBackground(Color.RED);
		quit.setBorder(finalStyle);
		quit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		quit.setFont(new Font("Tahoma", Font.BOLD, 15));
		frame.getContentPane().add(quit);
		
		JLabel mancheL = new JLabel("Manche:");
		mancheL.setFont(new Font("Tahoma", Font.PLAIN, 20));
		mancheL.setBounds(467, 374, 75, 23);
		frame.getContentPane().add(mancheL);
		
		try {
			mancheF = new JTextField("" + AdminRdF.getSt().getNmanche(AdminRdF.idGame));
			mancheF.setHorizontalAlignment(SwingConstants.CENTER);
			mancheF.setFont(new Font("Tahoma", Font.BOLD, 20));
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
		mancheF.setEditable(false);
		mancheF.setBounds(552, 374, 25, 23);
		frame.getContentPane().add(mancheF);
		mancheF.setColumns(10);
		
		
		
		JLabel timerL = new JLabel("Timer:");
		timerL.setFont(new Font("Tahoma", Font.BOLD, 20));
		timerL.setBounds(477, 272, 65, 23);
		frame.getContentPane().add(timerL);
		
		timerF = new JTextField();
		timerF.setBackground(Color.WHITE);
		timerF.setFont(new Font("Tahoma", Font.BOLD, 20));
		timerF.setEditable(false);
		timerF.setBounds(552, 272, 32, 23);
		frame.getContentPane().add(timerF);
		timerF.setColumns(10);
		
		frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
				frame.dispose();
				new AdminFrame();
				try {
					AdminRdF.exitObserver();
					AdminRdF.removeMe();
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
            }
        });
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
	
	/**
	 * metodo utilizzato per impostare il testo relativo alle mosse eseguite, che viene visualizzato dall'osservatore
	 * @param s indica la stringa da stampare
	 */
	public static void setTextArea(String s) {
		txt.setText("<html><p align=\"center\">" + s.replaceAll("\n", "<br/>") +"</p></html>");
	}
	
	/**
	 * metodo get per l'oggetto JTextField 'nickF1'
	 * @return il contenuto di 'nickF1'
	 */
	public static JTextField getNickF1() {
		return nickF1;
	}

	/**
	 * metodo set per il testo dell'oggetto JTextField 'nickF1'
	 * @param nick1 indica il soprannome da impostare 
	 */
	public static void setNickF1(String nick1) {
		nickF1.setText(nick1);
	}

	/**
	 * metodo get per la lista 'players'
	 * @return un'ArrayList conenente i nicknames dei giocatori
	 */
	public static ArrayList<String> getPlayers() {
		return players;
	}

	/**
	 * metodo get per l'oggetto JTextField 'bonusF1'
	 * @return il riferimento all'oggetto 'bonusF1'
	 */
	public static JTextField getBonusF1() {
		return bonusF1;
	}

	/**
	 * metodo set per il testo dell'oggetto JTextField 'bonusF1'
	 * @param bonus indica il numero di jolly da impostare 
	 */
	public static void setBonusF1(int bonus) {
		bonusF1.setText(""+ bonus);
	}
	
	/**
	 * metodo utilizzato per incrementare il contenuto dell'oggetto JTextField 'bonusF1'
	 * @param bonus indica il valore di jolly da aggiungere
	 */
	public static void addBonusF1(int bonus) {
		int b = Integer.parseInt(bonusF1.getText());
		bonusF1.setText(""+ (bonus+b));
	}

	/**
	 * metodo get per l'oggetto JTextField 'bankF1'
	 * @return il riferimento all'oggetto 'bankF1'
	 */
	public static JTextField getBankF1() {
		return bankF1;
	}

	/**
	 * metodo utilizzato per incrementare il contenuto dell'oggetto JTextField 'bankF1'
	 * @param b indica il valore da incrementare
	 */
	public static void setBankF1(int b) {
		int p = Integer.parseInt(bankF1.getText().substring(1));
		bankF1.setText("$"+ (b+p));
	}

	/**
	 * metodo get per il valore contenuto nell'oggetto JTextField 'scoreF1'
	 * @return il valore contenuto nella variabile 'score'
	 */
	public static int getScoreF1() {
		int score = Integer.parseInt(scoreF1.getText().substring(1));
		return score;
	}
	
	/**
	 * metodo utilizzato per resettare il valore contenuto nell'oggetto JTextField 'scoreF1'
	 */
	public static void resetScoreF1() {
		scoreF1.setText("$0");
	}

	/**
	 * metodo utilizzato per incrementare il valore contenuto nell'oggetto JTextField 'scoreF1'
	 * @param s indica il valore da incrementare
	 */
	public static void setScoreF1(int s) {
		int p = Integer.parseInt(scoreF1.getText().substring(1));
		scoreF1.setText("$"+ (p+s));
	}

	/**
	 * metodo get per l'oggetto JTextField 'nickF2'
	 * @return il riferimento all'oggetto 'nickF2'
	 */
	public static JTextField getNickF2() {
		return nickF2;
	}

	/**
	 * metodo set per il testo dell'oggetto JTextField 'nickF2'
	 * @param nick2 indica il soprannome da impostare 
	 */
	public static void setNickF2(String nick2) {
		nickF2.setText(nick2);
	}

	/**
	 * metodo get per l'oggetto JTextField 'bonusF2'
	 * @return il riferimento all'oggetto 'bonusF2'
	 */
	public static JTextField getBonusF2() {
		return bonusF2;
	}

	/**
	 * metodo set per il testo dell'oggetto JTextField 'bonusF2'
	 * @param bonus indica il numero di jolly da impostare 
	 */
	public static void setBonusF2(int bonus) {
		bonusF2.setText(""+ bonus);
	}
	
	/**
	 * metodo utilizzato per incrementare il contenuto dell'oggetto JTextField 'bonusF2'
	 * @param bonus indica il valore di jolly da aggiungere
	 */
	public static void addBonusF2(int bonus) {
		int b = Integer.parseInt(bonusF2.getText());
		bonusF2.setText(""+ (bonus+b));
	}

	/**
	 * metodo get per l'oggetto JTextField 'bankF2'
	 * @return il riferimento all'oggetto 'bankF2'
	 */
	public static JTextField getBankF2() {
		return bankF2;
	}

	/**
	 * metodo utilizzato per incrementare il contenuto dell'oggetto JTextField 'bankF2'
	 * @param b indica il valore da incrementare
	 */
	public static void setBankF2(int b) {
		int p = Integer.parseInt(bankF2.getText().substring(1));
		bankF2.setText("$"+ (b+p));
	}

	/**
	 * metodo get per il valore contenuto nell'oggetto JTextField 'scoreF2'
	 * @return il valore contenuto nella variabile 'score'
	 */
	public static int getScoreF2() {
		int score = Integer.parseInt(scoreF2.getText().substring(1));
		return score;
	}
	
	/**
	 * metodo utilizzato per resettare il valore contenuto nell'oggetto JTextField 'scoreF1'
	 */
	public static void resetScoreF2() {
		scoreF2.setText("$0");
	}

	/**
	 * metodo utilizzato per incrementare il valore contenuto nell'oggetto JTextField 'scoreF2'
	 * @param s indica il valore da incrementare
	 */
	public static void setScoreF2(int s) {
		int p = Integer.parseInt(scoreF2.getText().substring(1));
		scoreF2.setText("$"+ (p+s));
	}

	public static JTextField getNickF3() {
		return nickF3;
	}

	/**
	 * metodo set per il testo dell'oggetto JTextField 'nickF3'
	 * @param nick3 indica il soprannome da impostare 
	 */
	public static void setNickF3(String nick3) {
		nickF3.setText(nick3);;
	}

	/**
	 * metodo get per l'oggetto JTextField 'bonusF3'
	 * @return il riferimento all'oggetto 'bonusF3'
	 */
	public static JTextField getBonusF3() {
		return bonusF3;
	}

	/**
	 * metodo set per il testo dell'oggetto JTextField 'bonusF3'
	 * @param bonus indica il numero di jolly da impostare 
	 */
	public static void setBonusF3(int bonus) {
		bonusF3.setText(""+ bonus);;
	}
	
	/**
	 * metodo utilizzato per incrementare il contenuto dell'oggetto JTextField 'bonusF3'
	 * @param bonus indica il valore di jolly da aggiungere
	 */
	public static void addBonusF3(int bonus) {
		int b = Integer.parseInt(bonusF3.getText());
		bonusF3.setText(""+ (bonus+b));
	}

	/**
	 * metodo get per l'oggetto JTextField 'bankF3'
	 * @return il riferimento all'oggetto 'bankF3'
	 */
	public static JTextField getBankF3() {
		return bankF3;
	}

	/**
	 * metodo utilizzato per incrementare il contenuto dell'oggetto JTextField 'bankF3'
	 * @param b indica il valore da incrementare
	 */
	public static void setBankF3(int b) {
		int p = Integer.parseInt(bankF3.getText().substring(1));
		bankF3.setText("$"+ (b+p));
	}

	/**
	 * metodo get per il valore contenuto nell'oggetto JTextField 'scoreF3'
	 * @return il valore contenuto nella variabile 'score'
	 */
	public static int getScoreF3() {
		int score = Integer.parseInt(scoreF3.getText().substring(1));
		return score;
	}
	
	/**
	 * metodo utilizzato per resettare il valore contenuto nell'oggetto JTextField 'scoreF1'
	 */
	public static void resetScoreF3() {
		scoreF3.setText("$0");
	}

	/**
	 * metodo utilizzato per incrementare il valore contenuto nell'oggetto JTextField 'scoreF3'
	 * @param s indica il valore da incrementare
	 */
	public static void setScoreF3(int s) {
		int p = Integer.parseInt(scoreF3.getText().substring(1));
		scoreF3.setText("$"+ (p+s));
	}

	/**
	 * metodo utilizzato per ottenere il contenuto dell'oggetto JTextField 'mancheF'
	 * @return il valore contenuto il 'mancheF'
	 */
	public static int getNManche() {
		return Integer.parseInt(mancheF.getText());
	}

	/**
	 * metodo utilizzato per modificare il contenuto dell'oggetto JTextField 'mancheF'
	 * @param m indica il valore da impostare
	 */
	public static void setMancheF(int m) {
		mancheF.setText(""+ m);
	}
	
	/**
	 * metodo utilizzato per resettare i contenuti degli oggetti JTextField 'scoreF1', 'scoreF2', 'scoreF3'
	 */
	public static void resetScore() {
		scoreF1.setText("$0");
		scoreF2.setText("$0");
		scoreF3.setText("$0");
	}
	
	/**
	 * metodo utilizzato per disporre la schermata una volta che un giocatore decide di uscire a partita in corso
	 * @param nick indica il soprannome del giocatore che è uscito dalla partita
	 */
	public static void quit(String nick) {
		frame.dispose();
		new AdminFrame();
		PopUp p = new PopUp("Partita Terminata", "Ci dispiace, il giocatore " + nick + "\nè uscito dalla partita", Color.RED, null);
		p.add();
	}
	
	/**
	 * metodo utilizzato per aumentare di 1 il valore contenuto nell'oggetto JTextField 'mancheF'
	 */
	public static void increaseNManche() {
		String value = mancheF.getText();
		mancheF.setText("" + ((Integer.parseInt(value))+1));
	}
	
	/**
	 * metodo utilizzato per aggiornare il pannello della frase misteriosa
	 * @param name indica la stringa che al primo elemento contiene la lettera da rivelare
	 */
	public static void updatePanel(String name) {
		char n = name.charAt(0);				
		ObserverPhrasePanel.revealsLetter(n);
		phrase.repaint();
	}
	
	/**
	 * metodo utilizzato per rivelare l'intera frase sul pannello osservatore
	 */
	public static void revealPhrase() {
		ObserverPhrasePanel.revealsPhrase();
		phrase.repaint();
	}
	
	/**
	 * metodo utilizzato per richiamare il metodo newManche della classe ObserverPhrasePanel
	 */
	public static void newPhrase() {
		phrase.newManche();
	}
	
	/**
	 * metodo utilizzato per impostare i secondi per gli oggetti Timer
	 * @param s indica i secondi da impostare
	 */
	public static void setTimerSecs(int s) {
		seconds = s;
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
			timerF.setText("");
			seconds = 0;
		}
	}
	
	/**
	 * metodo utilizzato per far partire l'oggetto Timer 'mancheTimer'
	 * @param s indica i secondi
	 */
	public static void startMancheTimer(int s) {
		stopAllTimer();
		seconds = s;
		mancheTimer.start();
	}
	
	/**
	 * metodo utilizzato per interrompere l'oggetto Timer 'mancheTimer'
	 */
	public static void stopMancheTimer() {
		if(mancheTimer.isRunning()) {
			mancheTimer.stop();
			timerF.setText("");
			seconds = 0;
		}
	}
	
	/**
	 * metodo utilizzato per interrompere tutti gli oggetti Timer, nel caso siano in esecuzione
	 */
	public static void stopAllTimer() {
		if(timer.isRunning()) {
			timer.stop();
		} else if(mancheTimer.isRunning()) {
			mancheTimer.stop();
		}
		timerF.setText("");
	}
}
