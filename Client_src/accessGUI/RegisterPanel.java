package accessGUI;

import javax.swing.JPanel;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.Border;

import game.ClientRdF;

import java.rmi.RemoteException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;

/**
 * classe per la definizione del pannello relativo alla schermata di registrazione
 * @author Luca Cremonesi
 * @version 1.0
 */
public class RegisterPanel extends JPanel{
	private static final long serialVersionUID=1;

	private static final Font FONT_1 = new Font("Open Sans Light", Font.BOLD, 20);
	
	private static final Font FONT_2 = new Font("Open Sans Light", Font.PLAIN, 20); 

	private RegisterFrame registerFrame;
	
	private JButton enter, back;

	private JLabel emailL, nameL, surnameL, nicknameL;

	private JTextField emailF, nameF, surnameF, nicknameF;

	/**
	 * costruttore
	 * @param frame indica il riferimento alla classe RegisterFrame
	 */
	public RegisterPanel(RegisterFrame frame) {
		super(new GridBagLayout());
		
		registerFrame = frame;
		
		Border style = BorderFactory.createRaisedBevelBorder();
		Border border = BorderFactory.createEtchedBorder(Color.CYAN.darker(), Color.CYAN.darker());
		Border finalStyle = BorderFactory.createCompoundBorder(border, style);

		enter = new JButton("INVIA");
		InputMap im = enter.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = enter.getActionMap();
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "clickMe");
        am.put("clickMe", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
            public void actionPerformed(ActionEvent e) {
				checkData();
            }
        });
		enter.addMouseListener(new MouseListener() {
		    @Override
		    public void mousePressed(MouseEvent e) {
		    	Border style = BorderFactory.createLoweredBevelBorder();
				Border border = BorderFactory.createEtchedBorder(Color.CYAN.darker(), Color.CYAN.darker());
				Border finalStyle = BorderFactory.createCompoundBorder(border, style);
				enter.setBorder(finalStyle);
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
				enter.setBorder(finalStyle);
				checkData();
			}
		});
		enter.setFont(FONT_1);
		enter.setContentAreaFilled(false);
		enter.setOpaque(true);
		enter.setBackground(Color.CYAN.brighter());
		enter.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		enter.setBorder(finalStyle);

		back = new JButton(new ImageIcon("immagini/arrowicon.png"));
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
				registerFrame.dispose();
				new LogFrame();
			}
		});
		back.setFont(FONT_2);
		back.setContentAreaFilled(false);
		back.setOpaque(true);
		back.setBackground(Color.CYAN.brighter());
		back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		back.setBorder(finalStyle);
		
		emailL = new JLabel("Email:", JLabel.LEFT);
		emailL.setFont(FONT_1);
		emailL.setForeground(Color.GRAY);

		nameL = new JLabel("Nome:", JLabel.LEFT);
		nameL.setFont(FONT_1);
		nameL.setForeground(Color.GRAY);
		
		surnameL = new JLabel("Cognome:", JLabel.LEFT);
		surnameL.setFont(FONT_1);
		surnameL.setForeground(Color.GRAY);
		
		nicknameL = new JLabel("Nickname:", JLabel.LEFT);
		nicknameL.setFont(FONT_1);
		nicknameL.setForeground(Color.GRAY);

		emailF = new JTextField("");
		emailF.setPreferredSize(new Dimension(200, 25));

		nameF = new JTextField("");
		nameF.setPreferredSize(new Dimension(200, 25));

		surnameF = new JTextField("");
		surnameF.setPreferredSize(new Dimension(200, 25));
		
		nicknameF = new JTextField("");
		nicknameF.setPreferredSize(new Dimension(200, 25));

		GridBagConstraints g = new GridBagConstraints();
		
		g.insets = new Insets(-30, 20, 20, 20);

		g.gridx = 0;
		g.gridy = 1;
		add(emailL, g);
		g.gridx = 1;
		g.gridy = 1;
		add(emailF, g);

		g.insets = new Insets(10, 20, 20, 20);

		g.gridx = 0;
		g.gridy = 2;
		add(nameL, g);
		g.gridx = 1;
		g.gridy = 2;
		add(nameF, g);

		g.insets = new Insets(10, 20, 20, 20);
		
		g.gridx = 0;
		g.gridy = 3;
		add(surnameL, g);
		g.gridx = 1;
		g.gridy = 3;
		add(surnameF, g);
		
		g.insets = new Insets(10, 20, 20, 20);

		g.gridx = 0;
		g.gridy = 4;
		add(nicknameL, g);
		g.gridx = 1;
		g.gridy = 4;
		add(nicknameF, g);
		
		g.insets = new Insets(20, 20, 20, 20);
		
		g.gridx = 0;
		g.gridy = 5;
		add(back, g);		

		g.insets = new Insets(20, 20, 20, 20);
		
		g.gridx = 1;
		g.gridy = 5;
		add(enter, g);
		
		setBackground(Color.BLUE.darker());
		setPreferredSize(new Dimension(600, 400));
	}
	
	/**
	 * metodo utilizzato per verificare la correttezza dell'input utente
	 */
	private void checkData() {
		try {
			boolean agg = false;
			if(!emailF.getText().equals("") && !nicknameF.getText().equals("") && !nameF.getText().equals("")) {
				agg = ClientRdF.addUser(emailF.getText().replace(" ", "") , nameF.getText(), surnameF.getText(), nicknameF.getText());
				if(agg) {
					ClientRdF.email = emailF.getText().replace(" ", "");
					registerFrame.dispose();
					new CodeFrame();
				} else {
					emailF.setText("");
					nicknameF.setText("");
					nameF.setText("");
					surnameF.setText("");
					JOptionPane.showMessageDialog(registerFrame, "Ops, registrazione fallita", "Errore", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				emailF.setText("");
				nicknameF.setText("");
				nameF.setText("");
				surnameF.setText("");
				JOptionPane.showMessageDialog(registerFrame, "Ops, nessun campo deve essere vuoto", "Campi Vuoti", JOptionPane.ERROR_MESSAGE);
			}
		} catch(RemoteException exc) {}
	}
}

