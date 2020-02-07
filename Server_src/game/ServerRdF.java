package game;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import database.DBadapter;
import database.DBimplementation;
/**
 * la classe ServerRdF rappresenta il server che gestisce il gioco La Ruota Della Fortuna
 * @author Andrea Peluso
 * @version 1.0
 */
public class ServerRdF extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JLabel label1,label2,label3,label4;
	private JTextField text1,text3;
	private JPasswordField text2;
	private JButton btn1;
	private JFrame frame;
	/**
	 * Costruisco l'interfaccia grafica del server
	 */
	public ServerRdF() {

		frame = new JFrame("ServerRdF");//assegna titolo a finestra
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);//operazione di chiusura della finestra

		ImageIcon img = new ImageIcon("img\\icon.png");//recupero l'icona 
		frame.setIconImage(img.getImage());//setto l'icona 

		JPanel MainPanel=new JPanel();//creo un pannello principale che conterrà gli altri
		MainPanel.setLayout(new BoxLayout(MainPanel, BoxLayout.Y_AXIS));//assegno il layout BoxLayout con allignamento verticale

		JPanel panel= new JPanel();//creo nuovo pannello JPanel
		panel.setBackground(new Color(222, 222, 222));//setto lo sfondo 
		panel.setLayout(new GridLayout(4,2,5,5));//setto il layout GridLayout(int rows, int cols, int hgap, int vgap)

		panel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10,10));//creo un margine createEmptyBorder(int top, int left, int bottom, int right)

		label1 = new JLabel("Username:");//creo una JLabel 
		label1.setFont(new Font("Sans Serif", Font.PLAIN, 12));//setto il font
		panel.add(label1);//la aggiungo al pannello
		text1 = new JTextField("postgres", 10);//creo una casella di testo
		panel.add(text1);//la aggiungo al pannello
		label2 = new JLabel("Password:");
		label2.setFont(new Font("Sans Serif", Font.PLAIN, 12));
		panel.add(label2);
		text2 = new JPasswordField("qwerty", 10);
		panel.add(text2);
		label3 = new JLabel("Host:");
		label3.setFont(new Font("Sans Serif", Font.PLAIN, 12));
		panel.add(label3);
		text3 = new JTextField("jdbc:postgresql://localhost:5432/dbrdf", 10);
		panel.add(text3);

		JPanel panel2=new JPanel();//creo un nuovo pannello
		panel2.setBackground(new Color(222, 222, 222));

		btn1 = new JButton("Connetti");//creo un nuovo bottone
		btn1.setBackground(new Color(255, 255, 255));
		btn1.setPreferredSize(new Dimension(100, 50));//imposto la dimensione 
		btn1.addActionListener(new ButtonListener());//aggiungo al bottone un ActionListener
		panel2.add(btn1);


		JPanel panel3=new JPanel(new BorderLayout());
		label4 = new JLabel("Credenziali accesso Database:");
		label4.setHorizontalAlignment(JLabel.CENTER);
		label4.setVerticalAlignment(JLabel.CENTER);
		label4.setFont(new Font("Sans Serif", Font.PLAIN, 12));
		panel3.add(label4);

		MainPanel.add(panel3);
		MainPanel.add(panel);
		MainPanel.add(panel2);
		frame.add(MainPanel);
		frame.setSize(new Dimension(600, 250));
		frame.setResizable(false);//rendo il frame non ridimensionabile
		frame.setLocationRelativeTo(null);//lo posiziono al centro dello schermo
		frame.setVisible(true);//lo rendo visibile           
	}
	
	
	public static void main(String[] args) {
		new ServerRdF();  
	}

	
	private class ButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==btn1){//se premo il bottone 'connetti'
				DBadapter.username=text1.getText();//username database
				DBadapter.password=String.valueOf(text2.getPassword());//password database
				DBadapter.jdbUrl=text3.getText();//url database


				DBimplementation DataBase=new DBimplementation();
				if(DataBase.connect()){//se riesco a connettermi
					JOptionPane.showMessageDialog(null, "Connesso al Database");
						try {
							if(DataBase.insert_phrases()){//se risco a inserire le frasi
								if(DataBase.AdminExist()){//se esiste il profilo amministratore
									if(AddServer.Start())//avvia il server
									{
										frame.setVisible(false);//rendi non visibile il frame corrente
										new ServerStarted();//apri nuova finestra Jframe
									}
									else{
										JOptionPane.showMessageDialog(null, "Impossibile avviare il Server sulla porta 1099");
									}

								}else{//se non esiste profilo amministratore
									frame.setVisible(false);
									new CreateAdministrator();//apri finestra per la creazione del profilo amministratore
								}
							}
						} catch (FileNotFoundException e1) {
							JOptionPane.showMessageDialog(null, "Errore nell'apertura del file contenente le frasi, file non trovato");

						} catch (IOException e1) {
							JOptionPane.showMessageDialog(null, "Errore di tipo: IOException");

						}
				}
				else{//altrimenti stampa messaggio di connessione fallita
					JOptionPane.showMessageDialog(null, "Non connesso, riprova perfavore");
				}

			}
		}
	}
}