package gameGUI;

import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.border.Border;
import java.awt.Color;
import java.awt.Cursor;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import game.AdminRdF;
import game.Phrases;

import java.awt.Component;
import javax.swing.border.MatteBorder;

/**
 * classe per la definizione e visualizzazione del frame relativo alla schermata di gestione delle frasi per gli amministratori
 * @author Luca Cremonesi
 * @version 1.0
 */
public class PhraseMFrame {

	private JFrame frame;

	private JTable table;
	private JButton importExc;
	private JButton addPhrase;
	private JButton save;
	private Hashtable<Integer,Phrases> phrases;
	private Set<Integer> keys;
	private List<Integer> kList;
	private int lastId;
	private JButton delete;
	private JButton back;
	private int[] rows;

	/**
	 * costruttore
	 */
	public PhraseMFrame() {
		try {
			phrases = AdminRdF.getSt().getPhrases();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		initialize();
	}

	/**
	 * metodo utilizzato per inizializzare il frame
	 */
	private void initialize() {
		frame = new JFrame("Schermata Gestione Frasi");

		try {
			BufferedImage image = ImageIO.read(new File("immagini/IconaRDF.png"));
			frame.setIconImage(image);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Border style = BorderFactory.createRaisedBevelBorder();
		Border border = BorderFactory.createEtchedBorder(Color.CYAN.darker(), Color.CYAN.darker());
		Border finalStyle = BorderFactory.createCompoundBorder(border, style);

		back = new JButton(new ImageIcon("immagini/arrowicon.png"));
		back.setBounds(28, 24, 55, 23);
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
			public void mouseClicked(MouseEvent e) {
				frame.dispose();
				new AdminFrame();
			}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}
		});
		back.setBackground(Color.CYAN.brighter());
		frame.getContentPane().add(back);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(31, 70, 534, 357);
		frame.getContentPane().add(scrollPane);

		table = new JTable();
		table.setBorder(new MatteBorder(1, 1, 1, 1, (Color) Color.LIGHT_GRAY));
		table.setRowSelectionAllowed(false);
		table.setAlignmentX(Component.RIGHT_ALIGNMENT);
		DefaultTableModel dtm = new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
						"Id_Frase", "Tema", "Corpo"
				}
				) {

			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			} 
		};
		table.setModel(dtm);
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(1).setResizable(true);
		table.getColumnModel().getColumn(2).setResizable(false);
		table.getColumnModel().getColumn(2).setPreferredWidth(270);

		keys = phrases.keySet();
		kList = new ArrayList<Integer>(keys) ;        //set -> list

		//Sort the list
		Collections.sort(kList);

		for(Integer c: kList) {
			Phrases p = phrases.get(c);
			dtm.addRow(new Object[] {c , p.getTema(), p.getCorpo() });
			lastId = c;
		}

		scrollPane.setViewportView(table);

		frame.getContentPane().setLayout(null);

		delete = new JButton("Elimina");
		delete.setBounds(343, 24, 89, 23);
		delete.setBorder(finalStyle);
		delete.setToolTipText("Elimina le frasi attualmente selezionate");
		delete.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		delete.setFont(new Font("Dialog", Font.BOLD, 17));
		delete.setContentAreaFilled(false);
		delete.setOpaque(true);
		delete.setBackground(Color.CYAN.brighter());
		delete.addMouseListener(new MouseListener() {
		    @Override
		    public void mousePressed(MouseEvent e) {
		    	Border style = BorderFactory.createLoweredBevelBorder();
				Border border = BorderFactory.createEtchedBorder(Color.CYAN.darker(), Color.CYAN.darker());
				Border finalStyle = BorderFactory.createCompoundBorder(border, style);
				delete.setBorder(finalStyle);
		    }
			@Override
			public void mouseClicked(MouseEvent e) {
				removeSelectedRows(table);
				try {
					phrases = AdminRdF.getSt().getPhrases();
				} catch (RemoteException ex) {
					ex.printStackTrace();
				}
				keys = phrases.keySet();
				kList = new ArrayList<Integer>(keys) ;        //set -> list

				//Sort the list
				Collections.sort(kList);
				
				lastId = kList.get(kList.size()-1);
				table.setModel(dtm);
				table.repaint();
			}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {
				Border style = BorderFactory.createRaisedBevelBorder();
				Border border = BorderFactory.createEtchedBorder(Color.CYAN.darker(), Color.CYAN.darker());
				Border finalStyle = BorderFactory.createCompoundBorder(border, style);
				delete.setBorder(finalStyle);
			}
		});
		frame.getContentPane().add(delete);

		save = new JButton("Modifica");
		save.setBounds(256, 437, 89, 23);
		save.setBorder(finalStyle);
		save.setToolTipText("Permette di modificare le frasi attualmente selezionate");
		save.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		save.setFont(new Font("Dialog", Font.BOLD, 17));
		save.setContentAreaFilled(false);
		save.setOpaque(true);
		save.setBackground(Color.CYAN.brighter());
		save.addMouseListener(new MouseListener() {
		    @Override
		    public void mousePressed(MouseEvent e) {
		    	Border style = BorderFactory.createLoweredBevelBorder();
				Border border = BorderFactory.createEtchedBorder(Color.CYAN.darker(), Color.CYAN.darker());
				Border finalStyle = BorderFactory.createCompoundBorder(border, style);
				save.setBorder(finalStyle);
		    }
			@Override
			public void mouseClicked(MouseEvent e) {
				if(table.getSelectedRows().length != 0) {
					if(save.getText().equals("Modifica")) {
						save.setText("Salva");
						unlockSelectedRows(table);
						table.getColumnModel().getColumn(0).setResizable(false);
						table.getColumnModel().getColumn(1).setResizable(false);
						table.getColumnModel().getColumn(2).setResizable(false);
						table.getColumnModel().getColumn(2).setPreferredWidth(270);
						table.repaint();
					} else if(save.getText().equals("Salva")) {
						save.setText("Modifica");
						DefaultTableModel dtm = new DefaultTableModel(
								getTableData(table),
								new String[] {
										"Id_Frase", "Tema", "Corpo"
								}
								) {

							private static final long serialVersionUID = 1L;

							public boolean isCellEditable(int row, int column) {
								return false;
							} 
						};

						for(int i=0; i<rows.length; i++) {
							try {
								AdminRdF.getSt().updatePhrase((int)dtm.getValueAt(rows[i], 0), (String)dtm.getValueAt(rows[i], 1), (String)dtm.getValueAt(rows[i], 2));
							} catch (RemoteException exc) {
								exc.printStackTrace();
							}
						}
						table.setModel(dtm);
						table.getColumnModel().getColumn(0).setResizable(false);
						table.getColumnModel().getColumn(1).setResizable(true);
						table.getColumnModel().getColumn(2).setResizable(false);
						table.getColumnModel().getColumn(2).setPreferredWidth(270);
						table.repaint();
					}
				} else {
					JOptionPane.showMessageDialog(frame, "Seleziona una o più righe per effettuare la modifica", "Nessuna Riga Selezionata", JOptionPane.ERROR_MESSAGE);
				}
			}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {
				Border style = BorderFactory.createRaisedBevelBorder();
				Border border = BorderFactory.createEtchedBorder(Color.CYAN.darker(), Color.CYAN.darker());
				Border finalStyle = BorderFactory.createCompoundBorder(border, style);
				save.setBorder(finalStyle);
			}
		});
		frame.getContentPane().add(save);

		importExc = new JButton("Importa(Excel)");
		importExc.setBounds(171, 23, 136, 26);
		importExc.setBorder(finalStyle);
		importExc.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		importExc.setFont(new Font("Dialog", Font.BOLD, 17));
		importExc.setContentAreaFilled(false);
		importExc.setOpaque(true);
		importExc.setToolTipText("Inserire solo file in formato csv");
		importExc.setBackground(Color.CYAN.brighter());
		importExc.addMouseListener(new MouseListener() {
		    @Override
		    public void mousePressed(MouseEvent e) {
		    	Border style = BorderFactory.createLoweredBevelBorder();
				Border border = BorderFactory.createEtchedBorder(Color.CYAN.darker(), Color.CYAN.darker());
				Border finalStyle = BorderFactory.createCompoundBorder(border, style);
				importExc.setBorder(finalStyle);
		    }
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int result= fileChooser.showOpenDialog(frame);
				if (result == JFileChooser.APPROVE_OPTION) {
					String url = fileChooser.getSelectedFile().getAbsolutePath();
					if(url.contains(".csv")) {
						SendCsvFile(url);
						
						DefaultTableModel dtm = new DefaultTableModel(
								new Object[][] {
								},
								new String[] {
										"Id_Frase", "Tema", "Corpo"
								}
								) {

							private static final long serialVersionUID = 1L;

							public boolean isCellEditable(int row, int column) {
								return false;
							} 
						};
						
						try {
							phrases = AdminRdF.getSt().getPhrases();
						} catch (RemoteException e1) {
							e1.printStackTrace();
						}
						keys = phrases.keySet();
						lastId = keys.size();
						kList = new ArrayList<Integer>(keys) ;        //set -> list

						//Sort the list
						Collections.sort(kList);

						for(Integer c: kList) {
							Phrases p = phrases.get(c);
							dtm.addRow(new Object[] {c , p.getTema(), p.getCorpo() });
						}
						table.setModel(dtm);
						table.getColumnModel().getColumn(0).setResizable(false);
						table.getColumnModel().getColumn(1).setResizable(false);
						table.getColumnModel().getColumn(2).setResizable(false);
						table.getColumnModel().getColumn(2).setPreferredWidth(270);
					} else {
						JOptionPane.showMessageDialog(frame, "Ops, formato del file non valido", "Errore", JOptionPane.ERROR_MESSAGE);
					}					
				}
			}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {
				Border style = BorderFactory.createRaisedBevelBorder();
				Border border = BorderFactory.createEtchedBorder(Color.CYAN.darker(), Color.CYAN.darker());
				Border finalStyle = BorderFactory.createCompoundBorder(border, style);
				importExc.setBorder(finalStyle);
			}
		});
		frame.getContentPane().add(importExc);

		addPhrase = new JButton("Aggiungi");
		addPhrase.setBounds(464, 23, 101, 26);
		addPhrase.setBorder(finalStyle);
		addPhrase.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		addPhrase.setFont(new Font("Dialog", Font.BOLD, 17));
		addPhrase.setContentAreaFilled(false);
		addPhrase.setOpaque(true);
		addPhrase.setBackground(Color.CYAN.brighter());
		addPhrase.addMouseListener(new MouseListener() {
		    @Override
		    public void mousePressed(MouseEvent e) {
		    	Border style = BorderFactory.createLoweredBevelBorder();
				Border border = BorderFactory.createEtchedBorder(Color.CYAN.darker(), Color.CYAN.darker());
				Border finalStyle = BorderFactory.createCompoundBorder(border, style);
				addPhrase.setBorder(finalStyle);
		    }
			@Override
			public void mouseClicked(MouseEvent e) {
				lastId++;
				dtm.addRow(new Object[] {lastId , "<< tema >>", "<< corpo >>" });
				try {
					AdminRdF.getSt().insertPhrase(lastId, " ", " ");
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
				table.setModel(dtm);
				table.getColumnModel().getColumn(0).setResizable(false);
				table.getColumnModel().getColumn(1).setResizable(false);
				table.getColumnModel().getColumn(2).setResizable(false);
				table.getColumnModel().getColumn(2).setPreferredWidth(270);
				table.repaint();
				JScrollBar vertical = scrollPane.getVerticalScrollBar();
				frame.validate();
				vertical.setValue(vertical.getMaximum());
			}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {
				Border style = BorderFactory.createRaisedBevelBorder();
				Border border = BorderFactory.createEtchedBorder(Color.CYAN.darker(), Color.CYAN.darker());
				Border finalStyle = BorderFactory.createCompoundBorder(border, style);
				addPhrase.setBorder(finalStyle);
			}
		});
		frame.getContentPane().add(addPhrase);
		
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e)
			{
				frame.dispose();
				new AdminFrame(); 
			}
		});
		
		frame.setSize(600, 500);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setResizable(false);
		frame.getContentPane().setBackground(Color.BLUE.darker());
		frame.setVisible(true);
	}
	
	/**
	 * metodo utilizzato per rendere modificabili le righe attualmente selezionate della tabella delle frasi
	 * @param table indica il riferimento all'oggetto di tipo JTable
	 */
	public void unlockSelectedRows(JTable table){
		DefaultTableModel model = (DefaultTableModel) this.table.getModel();
		rows = table.getSelectedRows();
		model = new DefaultTableModel(
				getTableData(table),
				new String[] {
						"Id_Frase", "Tema", "Corpo"
				}
				) {

			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				for(int i=0; i<rows.length; i++) {
					if(row == rows[i]) {
						return column == 1 || column ==2;
					} 
				}
				return false;
			} 
		};
		
		table.setModel(model);
	}

	/**
	 * metodo utilizzato per rimuovere le righe attualmente selezionate della tabella delle frasi
	 * @param table indica il riferimento all'oggetto di tipo JTable
	 */
	public void removeSelectedRows(JTable table){
		DefaultTableModel model = (DefaultTableModel) this.table.getModel();
		int[] rows = table.getSelectedRows();
		for(int i=0;i<rows.length;i++){
			try {
				AdminRdF.getSt().removePhrase((int) model.getValueAt(rows[i]-i, 0));
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			model.removeRow(rows[i]-i);
		}
	}

	/**
	 * metodo utilizzato per ottenere una matrice contenente i dati presenti nella tabella delle frasi
	 * @param table indica il riferimento all'oggetto di tipo JTable
	 * @return matrice di tipo Object contenente le frasi presenti nella tabella 'table' passata come argomento
	 */
	public Object[][] getTableData (JTable table) { 
		DefaultTableModel dtm = (DefaultTableModel) table.getModel(); 
		int nRow = dtm.getRowCount(), nCol = dtm.getColumnCount(); 
		Object[][] tableData = new Object[nRow][nCol]; 
		for (int i = 0 ; i < nRow ; i++) 
			for (int j = 0 ; j < nCol ; j++) 
				tableData[i][j] = dtm.getValueAt(i,j); 
		return tableData; 
	}

	/**
	 * metodo utilizzato per importare i dati da un file '.csv' al database del ServerRdF
	 * @param url indica la directory del file da importare
	 */
	public void SendCsvFile(String url){
		ArrayList<Object[]> rowList = new ArrayList<Object[]>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(url),"UTF-8"))) {
			String line;
			int i=0;
			while ((line = br.readLine()) != null) {
				line=i+","+line;
				Object[] lineItems = line.split(",");
				rowList.add(lineItems);
				i++;
			}
			br.close();
		}
		catch(Exception e){ 
		}
		rowList.remove(0);
		
		Object[][] matrix = new Object[rowList.size()][];
		for (int i = 0; i < rowList.size(); i++) {
			Object[] row = rowList.get(i);
			matrix[i] = row;
		}

		try {
			//invio dati sotto forma di matrice al metodo updatePhrases
			AdminRdF.getSt().addPhrases(matrix);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
