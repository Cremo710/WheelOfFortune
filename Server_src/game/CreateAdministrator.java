package game;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import database.RegistrationQuery;

/**
 * la classe CreateAdministrator è usata per permettere al gestore del server di creare un amministratore nel caso non ce ne sia uno
 * @author Andrea Peluso
 * @version 1.0
 */
public class CreateAdministrator extends JFrame{

	private static final long serialVersionUID = 1L;
	private JLabel label1,label2,label3,label4,label5,label6;
	private JTextField text1,text2,text3,text4;
	private JPasswordField text5;
	private JButton btn1;
	private JFrame frame;
	
	/**
	 * costruttore della classe
	 */
	public CreateAdministrator() {


		frame = new JFrame("Server");
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

		ImageIcon img = new ImageIcon("img\\icon.png");
		frame.setIconImage(img.getImage());

		JPanel MainPanel=new JPanel();
		MainPanel.setLayout(new BoxLayout(MainPanel, BoxLayout.Y_AXIS));

		JPanel panel= new JPanel();
		panel.setBackground(new Color(222, 222, 222));
		panel.setLayout(new GridLayout(10,1,5,5));

		panel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10,50));

		label1 = new JLabel("Nome:");
		label1.setFont(new Font("Sans Serif", Font.PLAIN, 12));
		panel.add(label1);
		text1 = new JTextField("", 10);
		panel.add(text1);
		label2 = new JLabel("Cognome:");
		label2.setFont(new Font("Sans Serif", Font.PLAIN, 12));
		panel.add(label2);
		text2 = new JTextField("", 10);
		panel.add(text2);
		label3 = new JLabel("Nickname:");
		label3.setFont(new Font("Sans Serif", Font.PLAIN, 12));
		panel.add(label3);
		text3 = new JTextField("", 10);
		panel.add(text3);
		label5 = new JLabel("E-mail:");
		label5.setFont(new Font("Sans Serif", Font.PLAIN, 12));
		panel.add(label5);
		text4 = new JTextField("", 10);
		panel.add(text4);
		label6 = new JLabel("Password:");
		label6.setFont(new Font("Sans Serif", Font.PLAIN, 12));
		panel.add(label6);
		text5 = new JPasswordField("", 10);
		panel.add(text5);

		JPanel panel2=new JPanel();//flow layout
		panel2.setBackground(new Color(222, 222, 222));

		btn1 = new JButton("Crea");
		btn1.setBackground(new Color(255, 255, 255));
		btn1.setPreferredSize(new Dimension(100, 50));
		btn1.addActionListener(new ButtonListener3());
		panel2.add(btn1);


		JPanel panel3=new JPanel(new BorderLayout());
		label4 = new JLabel("Registrazione profilo amministratore:");
		label4.setHorizontalAlignment(JLabel.CENTER);
		label4.setVerticalAlignment(JLabel.CENTER);
		label4.setFont(new Font("Sans Serif", Font.PLAIN, 12));
		panel3.add(label4);

		MainPanel.add(panel3);
		MainPanel.add(panel);
		MainPanel.add(panel2);
		frame.add(MainPanel);
		frame.setSize(new Dimension(450, 450));
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);            // "super" Frame shows
	}



	private class ButtonListener3 implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==btn1){
				if(text1.getText().equals("")||text2.getText().equals("")||text3.getText().equals("")||text4.getText().equals("")||String.valueOf(text5.getPassword()).equals("")){
					JOptionPane.showMessageDialog(null, "Compila tutti i campi!");
				}else{
					RegistrationQuery DataBase=new RegistrationQuery();
					if(DataBase.connect()){
					if(DataBase.insert_admin(text4.getText(), text1.getText(), text2.getText(), text3.getText(), String.valueOf(text5.getPassword()))){
						if(AddServer.Start())//avvia il server
						{
							frame.setVisible(false);//rendi non visibile il frame corrente
							new ServerStarted();//apri nuova finestra Jframe
						}
					}else{
						JOptionPane.showMessageDialog(null, "Errore");

					}
					}else{
						JOptionPane.showMessageDialog(null, "Errore connessione");

					}
				}

			}

		}}}
