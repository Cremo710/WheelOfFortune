package game;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * la classe ServerStarted crea l'interfaccia di avvio del server
 * @author Andrea Peluso
 * @version 1.0
 */
public class ServerStarted extends JFrame{

	private static final long serialVersionUID = 1L;
	private JLabel label;
	private JButton btn1;
	private JFrame frame;

	/**
	 * costruttore della classe
	 */
	public ServerStarted(){
		
		frame = new JFrame("ServerRdF");
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

		ImageIcon img = new ImageIcon("img\\icon.png");
		frame.setIconImage(img.getImage());

		JPanel MainPanel=new JPanel();
		MainPanel.setLayout(new BoxLayout(MainPanel, BoxLayout.Y_AXIS));

		JPanel panel=new JPanel(new BorderLayout());
		label = new JLabel("ServerRdF avviato");
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setVerticalAlignment(JLabel.CENTER);
		label.setFont(new Font("Sans Serif", Font.PLAIN, 12));
		panel.add(label);

		JPanel panel2=new JPanel();
		panel2.setBackground(new Color(222, 222, 222));

		btn1 = new JButton("Esci");
		btn1.setBackground(new Color(255, 255, 255));
		btn1.setPreferredSize(new Dimension(100, 50));
		btn1.addActionListener(new ButtonListener2());
		panel2.add(btn1);

		MainPanel.add(panel);
		MainPanel.add(panel2);
		frame.add(MainPanel);
		frame.setSize(new Dimension(450, 250));
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true); 
	}

	private class ButtonListener2 implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==btn1){//se premo il bottone 'esci'
				int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog(frame, "Sei sicuro di voler chiudere il Server?", "Exit", dialogButton);
				if(dialogResult == 0) {
					System.exit(getDefaultCloseOperation());//chiudi l'applicazione
				}
			}}}

}


