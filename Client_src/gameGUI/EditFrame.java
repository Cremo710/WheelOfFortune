package gameGUI;

import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * classe per la visualizzazione del frame relativo alla schermata di visualizzazione e modifica del profilo utente
 * @author Luca Cremonesi
 * @version 1.0
 */
public class EditFrame extends JFrame{
	private static final long serialVersionUID=1;
	
	protected static String[] data;
	
	/**
	 * costruttore
	 * @param d indica il vettore contenente i dati relativi all'utente
	 */
	public EditFrame(String[] d) {
		super("Schermata Profilo Utente");
		
		data = d;
		
		EditPanel edit = new EditPanel(this);
		
		try {
            BufferedImage image = ImageIO.read(new File("immagini/IconaRDF.png"));
            setIconImage(image);
		} catch (IOException e) {
            e.printStackTrace();
		}
		
		setLayout(new FlowLayout());
		add(edit);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e)
			{
				dispose();
				new PlayerFrame(); 
			}
		});
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(600, 450); //dimensioni finestra
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
	}
}
