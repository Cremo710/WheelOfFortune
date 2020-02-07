package game;

import javax.sound.sampled.AudioInputStream; 
import java.io.IOException;
import java.io.File;
import javax.sound.sampled.AudioSystem; 
import javax.sound.sampled.Clip; 
import javax.sound.sampled.LineUnavailableException; 
import javax.sound.sampled.UnsupportedAudioFileException; 

/**
 * classe per la lettura di file audio
 * @author Luca Cremonesi
 * @version 1.0
 * */
public class AudioPlayer {
	Clip audio;
	String path;
	AudioInputStream audioInputStream;
	
	/** 
	 * costruttore
	 * @param path indica la directory del file audio da leggere
	 * @throws UnsupportedAudioFileException per la gestione delle eccezioni
	 * @throws IOException per la gestione delle eccezioni
	 * @throws LineUnavailableException per la gestione delle eccezioni
	 *  */
	public AudioPlayer(String path) throws UnsupportedAudioFileException, 
    IOException, LineUnavailableException {
		this.path = path;
		audioInputStream = AudioSystem.getAudioInputStream(new File(path).getAbsoluteFile());
		audio = AudioSystem.getClip();
		audio.open(audioInputStream);
	}
	
	/**
	 * metodo utilizzato per avviare la lettura del file audio
	 * */
	public void play() {
		audio.start();
		audio.setMicrosecondPosition(0);
	}
	
	/**
	 * metodo utilizzato per interrompere la lettura del file audio
	 * */
	public void pause() {
		audio.stop();
	}
	
	/**
	 * metodo utilizzato per avviare la lettura continua(in loop) del file audio
	 * */
	public void loop() {
		audio.loop(Clip.LOOP_CONTINUOUSLY);
	}
}
