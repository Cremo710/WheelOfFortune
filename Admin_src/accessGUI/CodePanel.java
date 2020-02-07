package accessGUI;

import javax.swing.JPanel;
import javax.swing.JPasswordField;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.Border;

import game.AdminRdF;
import gameGUI.AdminFrame;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.rmi.RemoteException;

/**
 * classe per la definizione del pannello relativo alla schermata di inserimento del codice di verifica
 * @author Luca Cremonesi
 * @version 1.0
 */
public class CodePanel extends JPanel{
	private static final long serialVersionUID=1;
	
	private static final Font FONT_1 = new Font("Open Sans Light", Font.BOLD, 20);
	
	private static final Font FONT_2 = new Font("Open Sans Light", Font.PLAIN, 20);
	
	private CodeFrame codeFrame;
	
	private JLabel codeL, infoL;
	
	private JButton enter, back;
	
	private JTextField codeF;
	
	/**
	 * costruttore
	 * @param frame indica il riferimento alla classe CodeFrame
	 */
	public CodePanel(CodeFrame frame) {
		super(new GridBagLayout());
		
		codeFrame = frame;
		
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
				checkCode();
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
				checkCode();
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
				codeFrame.dispose();
				new RegisterFrame();
			}
		});
		back.setFont(FONT_2);
		back.setContentAreaFilled(false);
		back.setOpaque(true);
		back.setBackground(Color.CYAN.brighter());
		back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		back.setBorder(finalStyle);
		
		infoL = new JLabel("Ti è stato inviato un codice per email");
		infoL.setFont(FONT_1);
		infoL.setForeground(Color.ORANGE);
		
		codeL = new JLabel("Inserisci il codice:");
		codeL.setFont(FONT_1.deriveFont(Font.ITALIC));
		codeL.setForeground(Color.GRAY);
		
		codeF = new JTextField("");
		codeF.setPreferredSize(new Dimension(200, 25));
		
		GridBagConstraints g = new GridBagConstraints();
		
		g.insets = new Insets(-20, 80, 20, 100);
		
		g.gridy = 1;
		add(infoL, g);
		
		g.insets = new Insets(30, 20, 20, 240);
		
		g.gridx = 0;
		g.gridy = 2;
		add(codeL, g);
		
		g.insets = new Insets(30, 180, 20, 20);
	
		g.gridy = 2;
		add(codeF, g);
		
		g.insets = new Insets(20, 220, 20, 20);
		
		g.gridx = 0;
		g.gridy = 3;
		add(enter, g);
		
		g.insets = new Insets(20, 20, 20, 200);
		
		g.gridy = 3;
		add(back, g);
		
		setBackground(Color.BLUE.darker());
		setPreferredSize(new Dimension(600, 400));
	}
	
	/**
	 * metodo utilizzato per la verifica della correttezza dell'input utente
	 */
	private void checkCode() {
		try {
			if(!codeF.getText().equals("")) {
				if(AdminRdF.checkCode(AdminRdF.email, codeF.getText())) {
					JPanel p = new JPanel();
					
					p.setLayout(new GridBagLayout());
					GridBagConstraints g = new GridBagConstraints();
					g.insets = new Insets(0, 0, 0, 0);
					JLabel txt = new JLabel("Inserisci la password per terminare la registrazione:");
					JPasswordField pf = new JPasswordField();
					pf.setPreferredSize(new Dimension(200, 25));
					
					g.gridy = 0;
					p.add(txt, g);
					
					g.insets = new Insets(10, 0, 0, 0);
					
					g.gridy = 1;
					p.add(pf, g);
					
					JOptionPane pane = new JOptionPane(p, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
				    JDialog dialog = pane.createDialog("Richiesta Password");
				    dialog.addComponentListener(new ComponentListener(){

				        @Override
				        public void componentShown(ComponentEvent e) {
				            pf.requestFocusInWindow();
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
				    	if(AdminRdF.updateCode(String.valueOf(pf.getPassword()), codeF.getText())) {
							JOptionPane.showMessageDialog(null, "Password Aggiornata" + "\nBenvenuto! La registrazione è andata a buon fine",
									"Registrazione Completata", JOptionPane.INFORMATION_MESSAGE);
							codeFrame.dispose();
							AdminRdF.setOnline(true);
							new AdminFrame();
						} else {
							JOptionPane.showMessageDialog(null, "Aggiornamento Non Riuscito" + "\nQualcosa è andato storto, riprovare",
									"Errore", JOptionPane.ERROR_MESSAGE);
						}
				    } else {
				    	JOptionPane.showMessageDialog(null, "Password Non Inserita" + "\nBenvenuto! La tua password corrisponde al codice di attivazione, consigliamo di modificarla",
								"Registrazione Completata", JOptionPane.INFORMATION_MESSAGE);
						AdminRdF.updateCode(codeF.getText(), codeF.getText());
						AdminRdF.setOnline(true);
						codeFrame.dispose();
						new AdminFrame();
				    }
			  } else {
			  		codeF.setText("");
			  		JOptionPane.showMessageDialog(null, "Il codice non corrisponde, riprova",
					"Codice Errato", JOptionPane.ERROR_MESSAGE);
			  }
			} else {
				JOptionPane.showMessageDialog(null, "Ops, il campo non può essere vuoto", "Campo Vuoto", JOptionPane.ERROR_MESSAGE);
			}
		} catch (HeadlessException | RemoteException e1) {
			e1.printStackTrace();
		}
	}
}
