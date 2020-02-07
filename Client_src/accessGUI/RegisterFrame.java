package accessGUI;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * classe per la visualizzazione del frame relativo alla schermata di registrazione
 * @author Luca Cremonesi
 * @version 1.0
 */
public class RegisterFrame extends JFrame{
	private static final long serialVersionUID=1;
	
	/**
	 * costruttore
	 */
	public RegisterFrame() {
		super("Schermata di Registrazione");
		
		RegisterPanel register = new RegisterPanel(this);
		
		try {
            BufferedImage image = ImageIO.read(new File("immagini/IconaRDF.png"));
            setIconImage(image);
		} catch (IOException e) {
            e.printStackTrace();
		}
		
		setLayout(new FlowLayout());
		add(register);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400); //dimensioni finestra
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
	}
}
