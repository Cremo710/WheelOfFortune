package accessGUI;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Cursor;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.InputMap;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import game.AdminRdF;
import gameGUI.AdminFrame;

import javax.swing.JButton;
import javax.swing.JComponent;

/**
 * classe per la visualizzazione del frame relativo alla schermata iniziale di accesso
 * @author Luca Cremonesi
 * @version 1.0
 */
public class LogFrame {

	private JFrame frame;
	private JTextField emailF;
	private JPasswordField passwF;

	/**
	 * costruttore
	 */
	public LogFrame() {
		initialize();
	}

	/**
	 * metodo utilizzato per inizializzare la schermata
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.BLUE.darker());
		frame.getContentPane().setLayout(null);
		
		try {
            BufferedImage image = ImageIO.read(new File("immagini/IconaRDF.png"));
            frame.setIconImage(image);
		} catch (IOException e) {
            e.printStackTrace();
		}
		
		Border style = BorderFactory.createRaisedBevelBorder();
		Border border = BorderFactory.createEtchedBorder(Color.CYAN.darker(), Color.CYAN.darker());
		Border finalStyle = BorderFactory.createCompoundBorder(border, style);
		
		JLabel logoL = new JLabel("");
		logoL.setIcon(new ImageIcon("Immagini\\logoPNG.png"));
		logoL.setBounds(75, 29, 436, 124);
		frame.getContentPane().add(logoL);
		
		JLabel emailL = new JLabel("Email:");
		emailL.setForeground(Color.GRAY.brighter());
		emailL.setFont(new Font("Tahoma", Font.PLAIN, 18));
		emailL.setBounds(177, 178, 56, 22);
		frame.getContentPane().add(emailL);
		
		JLabel passwL = new JLabel("Password:");
		passwL.setForeground(Color.LIGHT_GRAY);
		passwL.setFont(new Font("Tahoma", Font.PLAIN, 18));
		passwL.setBounds(145, 213, 88, 22);
		frame.getContentPane().add(passwL);
		
		emailF = new JTextField();
		emailF.setHorizontalAlignment(SwingConstants.LEFT);
		emailF.setBounds(238, 178, 185, 20);
		frame.getContentPane().add(emailF);
		emailF.setColumns(10);
		
		passwF = new JPasswordField();
		passwF.setHorizontalAlignment(SwingConstants.LEFT);
		passwF.setBounds(238, 215, 185, 20);
		frame.getContentPane().add(passwF);
		passwF.setColumns(10);
		
		
		
		JButton login = new JButton("Accedi");
		
		InputMap im = login.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = login.getActionMap();
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "clickMe");
        am.put("clickMe", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
            public void actionPerformed(ActionEvent e) {
				checkLogin();
            }
        });
        
		login.addMouseListener(new MouseListener() {
		    @Override
		    public void mousePressed(MouseEvent e) {
		    	Border style = BorderFactory.createLoweredBevelBorder();
				Border border = BorderFactory.createEtchedBorder(Color.CYAN.darker(), Color.CYAN.darker());
				Border finalStyle = BorderFactory.createCompoundBorder(border, style);
				login.setBorder(finalStyle);
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
				login.setBorder(finalStyle);
				checkLogin();
			}
		});
		login.setFont(new Font("Tahoma", Font.PLAIN, 18));
		login.setBounds(257, 259, 89, 23);
		login.setContentAreaFilled(false);
		login.setOpaque(true);
		login.setBackground(Color.CYAN.brighter());
		login.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		login.setBorder(finalStyle);
		frame.getContentPane().add(login);
		
		JLabel txtL = new JLabel("Non hai ancora un account?");
		txtL.setForeground(Color.LIGHT_GRAY);
		txtL.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtL.setBounds(145, 304, 185, 14);
		frame.getContentPane().add(txtL);
		
		JLabel regL = new JLabel("<html><u>Clicca per registrarti</u></html>");
		regL.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		regL.addMouseListener(new MouseListener() {
		    @Override
		    public void mousePressed(MouseEvent e) {
		    	regL.setForeground(Color.ORANGE);
		    }
			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {
				regL.setForeground(Color.GREEN);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				regL.setForeground(Color.YELLOW);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				new RegisterFrame();
				frame.dispose();
			}
		});
		regL.setForeground(Color.YELLOW);
		regL.setFont(new Font("Tahoma", Font.PLAIN, 15));
		regL.setBounds(333, 300, 126, 22);
		frame.getContentPane().add(regL);
		
		JLabel txtPL = new JLabel("Hai dimenticato la password?");
		txtPL.setForeground(Color.LIGHT_GRAY);
		txtPL.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtPL.setBounds(112, 327, 187, 26);
		frame.getContentPane().add(txtPL);
		
		JLabel resetL = new JLabel("<html><u>Clicca per riceverne una nuova</u></html>");
		resetL.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		resetL.addMouseListener(new MouseListener() {
		    @Override
		    public void mousePressed(MouseEvent e) {
		    	resetL.setForeground(Color.ORANGE);
		    }
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				resetL.setForeground(Color.GREEN);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				resetL.setForeground(Color.YELLOW);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				String email = JOptionPane.showInputDialog(null,"Inserisci l'email associata al tuo account: ",
						"Reset Password",JOptionPane.PLAIN_MESSAGE);

				if(email != null) {
					email = email.replace(" ", "");
					if(email.equals("")) {
						JOptionPane.showMessageDialog(null, "Il campo 'email' risulta vuoto", "Campo Vuoto", JOptionPane.ERROR_MESSAGE);
					} else {
						try {
							if(AdminRdF.resetPassword(email)) {
								JOptionPane.showMessageDialog(null, "Ti è stata inviata una mail con una nuova password",
										"Reset Password", JOptionPane.INFORMATION_MESSAGE);
							} else {
								JOptionPane.showMessageDialog(null, "Ops, operazione fallita", "Errore", JOptionPane.ERROR_MESSAGE);
							}
						} catch (RemoteException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});
		resetL.setForeground(Color.YELLOW);
		resetL.setFont(new Font("Tahoma", Font.PLAIN, 15));
		resetL.setBounds(309, 329, 205, 18);
		frame.getContentPane().add(resetL);
		
		frame.setBounds(100, 100, 600, 400);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
	}
	
	/**
	 * metodo utilizzato per verificare la correttezza dell'input utente
	 */
	private void checkLogin() {
		try {
			boolean acc = false;
			if(!emailF.getText().equals("") && !((String.valueOf(passwF.getPassword())).equals(""))) {
				acc = AdminRdF.loginAdmin(emailF.getText(), String.valueOf(passwF.getPassword()));
				if(acc) {
					AdminRdF.email = emailF.getText();
					frame.dispose();
					new AdminFrame();
				} else {
					emailF.setText("");
					passwF.setText("");
					JOptionPane.showMessageDialog(frame, "Ops, accesso negato", "Errore", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				emailF.setText("");
				passwF.setText("");
				JOptionPane.showMessageDialog(frame, "Ops, nessun campo può essere vuoto", "Campi Vuoti", JOptionPane.ERROR_MESSAGE);
			}
							
		} catch(RemoteException ex) {}
	}
}
