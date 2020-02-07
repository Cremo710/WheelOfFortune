package accessGUI;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * classe per la visualizzazione del frame relativo alla schermata di inserimento del codice di verifica
 * @author Luca Cremonesi
 * @version 1.0
 */
public class CodeFrame extends JFrame{
	private static final long serialVersionUID=1;
	
	/**
	 * costruttore
	 */
	public CodeFrame() {
		super("Codice di Verifica");
		
		CodePanel codP = new CodePanel(this);
		
		try {
            BufferedImage image = ImageIO.read(new File("immagini/IconaRDF.png"));
            setIconImage(image);
		} catch (IOException e) {
            e.printStackTrace();
		}
		
		setLayout(new FlowLayout());
		add(codP);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400); //dimensioni finestra
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
	}
}
