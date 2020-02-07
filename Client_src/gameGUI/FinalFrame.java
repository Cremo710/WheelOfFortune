package gameGUI;


import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import game.ClientRdF;
import javax.swing.JButton;

/**
 * classe per la visualizzazione del frame relativo alla schermata per la visualizzazione dei risultati finali di ogni partita
 * @author Luca Cremonesi
 * @version 1.0
 */
public class FinalFrame {

	private JFrame frame;

	/**
	 * costruttore
	 */
	public FinalFrame() {
		initialize();
	}

	/**
	 * metodo utilizzato per inizializzare il frame
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(155, 248, 214));;
		frame.setResizable(false);
		frame.setBounds(100, 100, 640, 520);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		try {
            BufferedImage image = ImageIO.read(new File("immagini/IconaRDF.png"));
            frame.setIconImage(image);
		} catch (IOException e) {
            e.printStackTrace();
		}
		
		Border style = BorderFactory.createRaisedBevelBorder();
	    Border border = BorderFactory.createEtchedBorder(Color.RED.darker(), Color.RED.darker());
	    Border finalStyle = BorderFactory.createCompoundBorder(border, style);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon("Immagini\\goldThropy.png"));
		lblNewLabel.setBounds(240, 130, 150, 234);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon("Immagini\\winnerImg.png"));
		lblNewLabel_1.setBounds(147, 11, 348, 176);
		frame.getContentPane().add(lblNewLabel_1);
		
		JLabel firstPlayerL = new JLabel("");
		firstPlayerL.setForeground(new Color(255, 69, 0));
		firstPlayerL.setHorizontalAlignment(SwingConstants.CENTER);
		firstPlayerL.setFont(new Font("Tahoma", Font.BOLD, 30));
		firstPlayerL.setBounds(204, 367, 226, 41);
		frame.getContentPane().add(firstPlayerL);
		
		JLabel lblNewLabel_3 = new JLabel("");
		lblNewLabel_3.setIcon(new ImageIcon("Immagini\\secondMedal.png"));
		lblNewLabel_3.setBounds(52, 228, 100, 119);
		frame.getContentPane().add(lblNewLabel_3);
		
		JLabel secondPlayerL = new JLabel("");
		secondPlayerL.setForeground(new Color(75, 0, 130));
		secondPlayerL.setHorizontalAlignment(SwingConstants.CENTER);
		secondPlayerL.setFont(new Font("Tahoma", Font.BOLD, 20));
		secondPlayerL.setBounds(35, 355, 141, 26);
		frame.getContentPane().add(secondPlayerL);
		
		JLabel lblNewLabel_4 = new JLabel("");
		lblNewLabel_4.setIcon(new ImageIcon("Immagini\\thirdMedal.png"));
		lblNewLabel_4.setBounds(485, 266, 70, 81);
		frame.getContentPane().add(lblNewLabel_4);
		
		JLabel thirdPlayerL = new JLabel("");
		thirdPlayerL.setForeground(new Color(75, 0, 130));
		thirdPlayerL.setFont(new Font("Tahoma", Font.BOLD, 20));
		thirdPlayerL.setHorizontalAlignment(SwingConstants.CENTER);
		thirdPlayerL.setBounds(453, 355, 141, 26);
		
		JLabel score1L = new JLabel("");
		score1L.setFont(new Font("Tahoma", Font.PLAIN, 20));
		score1L.setHorizontalAlignment(SwingConstants.CENTER);
		score1L.setBounds(240, 407, 161, 26);
		frame.getContentPane().add(score1L);
		
		JLabel score2L = new JLabel("");
		score2L.setFont(new Font("Tahoma", Font.PLAIN, 20));
		score2L.setHorizontalAlignment(SwingConstants.CENTER);
		score2L.setBounds(35, 382, 141, 26);
		frame.getContentPane().add(score2L);
		
		JLabel score3L = new JLabel("");
		score3L.setFont(new Font("Tahoma", Font.PLAIN, 20));
		score3L.setHorizontalAlignment(SwingConstants.CENTER);
		score3L.setBounds(444, 382, 150, 26);
		frame.getContentPane().add(score3L);
		
		try {
			LinkedHashMap<String, Integer> players = ClientRdF.getSt().ranking(ClientRdF.idGame);
			ArrayList<String> nicknames = new ArrayList<String>();
			ArrayList<String> points = new ArrayList<String>();
			
			for (String key : players.keySet())
			{
			   nicknames.add(key);
			   if(players.get(key)!= null) {
				   points.add(String.valueOf(players.get(key)));
			   } else {
				   points.add("0");
			   }
			}
			
			firstPlayerL.setText(nicknames.get(0));
			secondPlayerL.setText(nicknames.get(1));
			thirdPlayerL.setText(nicknames.get(2));
			
			score1L.setText("$" + points.get(0));
			score2L.setText("$" + points.get(1));
			score3L.setText("$" + points.get(2));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		frame.getContentPane().add(thirdPlayerL);
		
		JButton quit = new JButton("ESCI");
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
				new PlayerFrame();
			}
		});
		quit.setContentAreaFilled(false);
		quit.setOpaque(true);
		quit.setBackground(Color.RED);
		quit.setFont(new Font("Tahoma", Font.BOLD, 12));
		quit.setBorder(finalStyle);
		quit.setBounds(275, 457, 89, 23);
		quit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		frame.getContentPane().add(quit);
		
		JLabel victoryGifL = new JLabel("");
		victoryGifL.setIcon(new ImageIcon("gif\\victory.gif"));
		victoryGifL.setBounds(157, 11, 318, 446);
		frame.getContentPane().add(victoryGifL);
		
		frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
            	frame.dispose();
            	new PlayerFrame();
            }
        });
		
		frame.setLocationRelativeTo(null);		
		frame.setVisible(true);
		try {
			ClientRdF.removeMe();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
