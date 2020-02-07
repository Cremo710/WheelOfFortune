package gameGUI;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.Border;

import java.awt.Color;
import java.awt.Container;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.Cursor;
import java.awt.Dimension;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * classe per la visualizzazione del frame relativo alla schermata delle statistiche
 * @author Luca Cremonesi
 * @version 1.0
 */
public class StatsFrame {

	private JFrame frame;
	
	private static final Font FONT = new Font("Open Sans Light", Font.BOLD, 20);
	
	/**
	 * costruttore
	 */
	public StatsFrame() {
		initialize();
	}

	/**
	 * metodo utilizzato per inizializzare il frame
	 */
	private void initialize() {
		frame = new JFrame("Schermata Statistiche");
		
		try {
            BufferedImage image = ImageIO.read(new File("immagini/IconaRDF.png"));
            frame.setIconImage(image);
		} catch (IOException e) {
            e.printStackTrace();
		}
		
		Border style = BorderFactory.createRaisedBevelBorder();
		Border border = BorderFactory.createEtchedBorder(Color.CYAN.darker(), Color.CYAN.darker());
		Border finalStyle = BorderFactory.createCompoundBorder(border, style);
		
		Container cont = new Container();
		cont.setLayout(new GridLayout());
		cont.setSize(650, 450);
		
		JPanel window = new JPanel();
		window.setBounds(0, 0, 650, 450);
		window.setBackground(Color.BLUE.darker());
		
		JScrollPane scroll = new JScrollPane(window, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.getVerticalScrollBar().setUnitIncrement(16);
		window.setLayout(new GridBagLayout());
		
		GridBagConstraints g = new GridBagConstraints();
		
		JButton back = new JButton("Indietro");
		back.setBounds(46, 42, 89, 23);
		back.setFont(FONT);
		back.setBorder(finalStyle);
		back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		back.setContentAreaFilled(false);
		back.setOpaque(true);
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
				frame.dispose();
			    new AdminFrame();
			}
		});
		back.setBackground(Color.CYAN.brighter());
		back.setBorder(finalStyle);
		back.setPreferredSize(new Dimension(100, 25));
		g.insets = new Insets(20, 0, 30, 0);
		g.gridy = 0;
		g.gridx = 0;
		window.add(back, g);
		
		g.insets = new Insets(0, 0, 15, 0);
		
		g.gridy = 1;
		g.gridx = 0;
		
		JLabel generalStatsL = new JLabel("Statistiche Generali");
		generalStatsL.setForeground(Color.GRAY.brighter());
		generalStatsL.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 20));
		generalStatsL.setBounds(0, 0, 265, 35);

		window.add(generalStatsL, g);
		
		GeneralStatsPanel gsp = new GeneralStatsPanel();
		gsp.setPreferredSize(new Dimension(500,200));
		g.gridy = 2;
		window.add(gsp, g);
	
		cont.add(scroll);
		
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e)
			{
				frame.dispose();
				new AdminFrame(); 
			}
		});
		
		frame.setVisible(true);
		frame.setBackground(new Color(0, 0, 255));
		frame.setSize(650, 450);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout());
		frame.setLocationRelativeTo(null);
		frame.getContentPane().add(cont);
	}
}
