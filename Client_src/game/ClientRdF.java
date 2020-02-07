package game;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import accessGUI.*;
import gameGUI.FinalFrame;
import gameGUI.FirstPanel;
import gameGUI.LoadingFrame;
import gameGUI.ObserverFrame;
import gameGUI.PhrasePanel;
import gameGUI.PlayerFrame;
import gameGUI.WheelFrame;
import gameGUI.WheelPanel;

import java.awt.Color;
import java.io.*;

/** 
 *  classe che permette di interfacciare gli utenti con il SeverRdF(Ruota della Fortuna)
 * 	@author Luca Cremonesi
 * 	@version 1.0
 * */
public class ClientRdF implements Serializable,RemoteObserver{
	private static final long serialVersionUID=1;

	private static ServerInterface st;

	private static RemoteObserver clientStub;
	 
    private static Registry registry=null;
	 
	private static ClientRdF client=null;

	public static String email;
	
	public static boolean player=false;
	
	public static boolean observer=false;

	public static long idGame;
	/** 
	 * costruttore vuoto
	 * */
	protected ClientRdF() {}
	/** metodo per la gestione delle notifiche (generiche) che provengono dal ServerRdF
	 * 	@param updateMsg indica la stringa che contiene l'oggetto della notifica ricevuta dal ServerRdF
	 * 	@throws RemoteException per la gestione delle eccezioni
	 * */
	public void remoteUpdate(Object updateMsg) throws RemoteException{
		if(isPlayer()) {
			playerNotify(updateMsg);
		} else if(isObserver()) {
			observerNotify(updateMsg);
		}
	}
	
	/** metodo per la gestione delle notifiche dirette agli osservatori e provenienti dal ServerRdF
	 * 	@param updateMsg indica la stringa che contiene l'oggetto della notifica ricevuta dal ServerRdF
	 * 	@throws RemoteException per la gestione delle eccezioni
	 * */
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public void observerNotify(Object updateMsg) throws RemoteException {
		if(updateMsg instanceof ArrayList<?>) {
			if(((ArrayList) updateMsg).get(0).equals("manche")) {
				String winner = (String) ((ArrayList) updateMsg).get(1);
				String[] data = st.getData(winner);
				winner = data[2];
				((ArrayList) updateMsg).remove(1);
				String score = (String) ((ArrayList) updateMsg).get(1);
				((ArrayList) updateMsg).remove(1);
				Iterator<String> it = ((ArrayList<String>) updateMsg).iterator();
				while(it.hasNext()) {
					if(it.next().equals(email)) {
						ObserverFrame.resetScore();
						if(ObserverFrame.getNickF1().getText().equals(winner)) {
							ObserverFrame.setBankF1(Integer.parseInt(score));
						} else if(ObserverFrame.getNickF2().getText().equals(winner)) {
							ObserverFrame.setBankF2(Integer.parseInt(score));
						} else {
							ObserverFrame.setBankF3(Integer.parseInt(score));
						}
						int nManche = ObserverFrame.getNManche() + 1;
						if(nManche == 5) {
							ObserverFrame.setTextArea("Il giocatore "+ winner + " ha indovinato la frase!\nL'ultima manche inizia tra 10 secondi");
						} else {
							ObserverFrame.setTextArea("Il giocatore "+ winner + " ha indovinato la frase!\nLa manche numero " + nManche +" inizia tra 10 secondi");
						}
						ObserverFrame.revealPhrase();
						ObserverFrame.stopTimer();
						ObserverFrame.startMancheTimer(10);
					}
				}
			} else if(((ArrayList) updateMsg).get(0).equals("victory")) {
				String winner = (String) ((ArrayList) updateMsg).get(1);
				String[] dt1 = st.getData(winner);
				winner = dt1[2];
				String mancheWinner = (String) ((ArrayList) updateMsg).get(2);
				String[] dt2 = st.getData(mancheWinner);
				mancheWinner = dt2[2];
				((ArrayList) updateMsg).remove(1);
				((ArrayList) updateMsg).remove(1);
				Iterator<String> it = ((ArrayList<String>) updateMsg).iterator();
				while(it.hasNext()) {
					if(it.next().equals(email)) {
						if(ObserverFrame.getNickF1().getText().equals(mancheWinner)) {
							ObserverFrame.getNickF1().setForeground(Color.GREEN);
							ObserverFrame.getNickF2().setForeground(Color.BLACK);
							ObserverFrame.getNickF3().setForeground(Color.BLACK);
							ObserverFrame.setBankF1(ObserverFrame.getScoreF1());
						} else if(ObserverFrame.getNickF2().getText().equals(mancheWinner)) {
							ObserverFrame.getNickF1().setForeground(Color.BLACK);
							ObserverFrame.getNickF2().setForeground(Color.GREEN);
							ObserverFrame.getNickF3().setForeground(Color.BLACK);
							ObserverFrame.setBankF2(ObserverFrame.getScoreF2());
						} else if(ObserverFrame.getNickF3().getText().equals(mancheWinner)) {
							ObserverFrame.getNickF1().setForeground(Color.BLACK);
							ObserverFrame.getNickF2().setForeground(Color.BLACK);
							ObserverFrame.getNickF3().setForeground(Color.GREEN);
							ObserverFrame.setBankF3(ObserverFrame.getScoreF3());
						}
						ObserverFrame.resetScore();
						ObserverFrame.revealPhrase();
						if(winner.equals(mancheWinner)) {
							ObserverFrame.setTextArea("Il giocatore "+ winner + " ha vinto la partita!\nPartita terminata, non ti resta che uscire");
						} else {
							ObserverFrame.setTextArea("Il giocatore "+ mancheWinner + " ha vinto la manche ma non la partita: il vincitore è "+ winner+"! Non ti resta che uscire");
						}
					}
				}
			} else if(((ArrayList) updateMsg).get(0).equals("frasi")) {
				Iterator<String> it = ((ArrayList<String>) updateMsg).iterator();
				while(it.hasNext()) {
					if(it.next().equals(email)) {
						PopUp pu = new PopUp("Frasi Terminate", "Ops, non ci sono nuove frasi disponibili! \nSarà inviata una mail agli amministratori", Color.RED, null);
						pu.add();
						new PlayerFrame();
						LoadingFrame.frame.dispose();
						observer = false;
						removeMe();
					}
				}
			} else if(((ArrayList) updateMsg).get(0).equals("exitlobby")) {
				Iterator<String> it4 = ((ArrayList<String>) updateMsg).iterator();
				while(it4.hasNext()) {
					if(it4.next().equals(email)) {
						LoadingFrame.addPlayer(-1);
						LoadingFrame.updatePlayers();
					}
				}
			} else if(((ArrayList) updateMsg).get(0).equals("SR")) {
				String owner = (String) ((ArrayList) updateMsg).get(2);
				((ArrayList) updateMsg).remove(2);
				Iterator<String> it5 = ((ArrayList<String>) updateMsg).iterator();
				while(it5.hasNext()) {
					if(it5.next().equals(email)) {
						String s = (String) ((ArrayList) updateMsg).get(1);
						String[] data = st.getData(owner);
						owner = data[2];
						if(s.contains("PASSA")) {
							ObserverFrame.setTextArea("La ruota si è fermata su "+ s + "\nAdesso "+ owner +" deve decidere se usare il jolly...");
						} else {
							ObserverFrame.setTextArea("La ruota si è fermata su "+ s + "\nAdesso "+ owner +" deve decidere quale cononante selezionare...");
						}
						ObserverFrame.startTimer(5);
					}
				}
			}else {
				Iterator<String> it2 = ((ArrayList<String>) updateMsg).iterator();
				while(it2.hasNext()) {
					if(it2.next().equals(email)) {
						LoadingFrame.addPlayer(1);
						LoadingFrame.updatePlayers();
						
						if(nPlayers(ClientRdF.idGame)==3) {
							try {
								new ObserverFrame(0);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						break;
					} 
				}
		   }
		} else if (updateMsg instanceof String) {
			String mail = (String) updateMsg;
			String[] dt = ClientRdF.getSt().getData(mail);
			ObserverFrame.stopTimer();
			if(ObserverFrame.getNickF1().getText().equals(dt[2])) {
				ObserverFrame.getNickF1().setForeground(Color.GREEN);
				ObserverFrame.getNickF2().setForeground(Color.BLACK);
				ObserverFrame.getNickF3().setForeground(Color.BLACK);
				ObserverFrame.startTimer(5);
			} else if(ObserverFrame.getNickF2().getText().equals(dt[2])) {
				ObserverFrame.getNickF1().setForeground(Color.BLACK);
				ObserverFrame.getNickF2().setForeground(Color.GREEN);
				ObserverFrame.getNickF3().setForeground(Color.BLACK);
				ObserverFrame.startTimer(5);
			} else if(ObserverFrame.getNickF3().getText().equals(dt[2])) {
				ObserverFrame.getNickF1().setForeground(Color.BLACK);
				ObserverFrame.getNickF2().setForeground(Color.BLACK);
				ObserverFrame.getNickF3().setForeground(Color.GREEN);
				ObserverFrame.startTimer(5);
			}

		} else if (updateMsg instanceof Move) {
			ArrayList<String> players = (ArrayList<String>)((Move) updateMsg).getTo();
			for(String p: players) {
				if(email.equals(p)) {
					if(((Move) updateMsg).getType().equals("esce")) {
						String[] data = st.getData(((Move) updateMsg).getOwner());
						ObserverFrame.quit(data[2]);
						idGame = 0;
						removeMe();
						st.exitObserver(ClientRdF.email, ClientRdF.idGame);
					} else if(((Move) updateMsg).getType().contains("allcns")) {
						ObserverFrame.stopTimer();
						String[] data = st.getData(((Move) updateMsg).getOwner());
						String nick = data[2];
						int score = ((Move) updateMsg).getPoints();
		
						if(nick.equals(ObserverFrame.getNickF1().getText())) {
							ObserverFrame.setScoreF1(score);
						} else if(nick.equals(ObserverFrame.getNickF2().getText())) {
							ObserverFrame.setScoreF2(score);
						} else {
							ObserverFrame.setScoreF3(score);
						}
						String n = ((Move) updateMsg).getType().substring(0,1);
						ObserverFrame.updatePanel(n);
						ObserverFrame.setTextArea("Il giocatore "+ nick + " ha selezionato la lettera " + n + "\ne ha guadagnato $" + score + "terminando le consonanti!");
						ObserverFrame.startTimer(5);
					} else if(((Move) updateMsg).getType().contains("allvoc")) {
						ObserverFrame.stopTimer();
						String[] data = st.getData(((Move) updateMsg).getOwner());
						String nick = data[2];
						int score = ((Move) updateMsg).getPoints();
		
						if(nick.equals(ObserverFrame.getNickF1().getText())) {
							ObserverFrame.setScoreF1(score);
						} else if(nick.equals(ObserverFrame.getNickF2().getText())) {
							ObserverFrame.setScoreF2(score);
						} else {
							ObserverFrame.setScoreF3(score);
						}
						String n = ((Move) updateMsg).getType().substring(0,1);
						ObserverFrame.updatePanel(n);
						ObserverFrame.setTextArea("Il giocatore "+ nick + " ha comprato la vocale " + n + "\ne ha terminato le vocali");
						ObserverFrame.startTimer(5);
					} else if (((Move) updateMsg).getType().equals("UJ")){
						ObserverFrame.stopTimer();
						String[] data = st.getData(((Move) updateMsg).getOwner());
						String nick = data[2];
						if(ObserverFrame.getNickF1().getText().equals(nick)) {
							ObserverFrame.addBonusF1(-1);
						} else if(ObserverFrame.getNickF2().getText().equals(nick)){
							ObserverFrame.addBonusF2(-1);
						} else {
							ObserverFrame.addBonusF3(-1);
						}
						ObserverFrame.setTextArea("Il giocatore "+ nick + " ha usato un jolly!\nOra ha 5 secondi per girare la ruota");
						ObserverFrame.startTimer(5);
					} else if(((Move) updateMsg).getType().equals("Perde")) {
						String[] data = st.getData(((Move) updateMsg).getOwner());
						String nick = data[2];
						if(ObserverFrame.getNickF1().getText().equals(nick)) {
							ObserverFrame.setScoreF1(0);
						} else if(ObserverFrame.getNickF2().getText().equals(nick)) {
							ObserverFrame.setScoreF2(0);
						} else {
							ObserverFrame.setScoreF3(0);
						}
						ObserverFrame.setTextArea("Il giocatore "+ nick + " ha pescato PERDE, \nil suo punteggio scende a zero e il turno passa al prossimo giocatore!");
					} else if(((Move) updateMsg).getType().equals("TJ")) {
						String[] data = st.getData(((Move) updateMsg).getOwner());
						String nick = data[2];
						if(ObserverFrame.getNickF1().getText().equals(nick)) {
							ObserverFrame.addBonusF1(1);
						} else if(ObserverFrame.getNickF2().getText().equals(nick)){
							ObserverFrame.addBonusF2(1);
						} else {
							ObserverFrame.addBonusF3(1);
						}
						ObserverFrame.setTextArea("Il giocatore "+ nick + " ha pescato un jolly!");
						ObserverFrame.startTimer(5);
					} else if(((Move) updateMsg).getType().equals("Passa")) {
						String[] data = st.getData(((Move) updateMsg).getOwner());
						String nick = data[2];
						ObserverFrame.setTextArea("Il giocatore "+ nick + " ha pescato PASSA, \nnon avendo bonus perde il turno!");
					} else if(((Move) updateMsg).getType().equals("GR")){
						ObserverFrame.stopTimer();
						String[] data = st.getData(((Move) updateMsg).getOwner());
						String nick = data[2];
						ObserverFrame.setTextArea("Il giocatore "+ nick + " ha girato la ruota, \nla ruota gira...");
					} else if(((Move) updateMsg).getType().equals("TS")) {
						String[] data = st.getData(((Move) updateMsg).getOwner());
						String nick = data[2];
						ObserverFrame.setTextArea("Il giocatore "+ nick + " ha perso tempo, \nil turno passa al prossimo giocatore");
					} else if(((Move) updateMsg).getType().equals("RF")){
						ObserverFrame.startTimer(10);
						String[] data = st.getData(((Move) updateMsg).getOwner());
						String nick = data[2];
						ObserverFrame.setTextArea("Il giocatore "+ nick + " sta provando a risolvere la frase misteriosa...");
					} else if(((Move) updateMsg).getType().equals("ERR")){
						String[] data = st.getData(((Move) updateMsg).getOwner());
						String nick = data[2];
						ObserverFrame.setTextArea("Il giocatore "+ nick + " ha sbagliato ad inserire la frase, \nil turno passa al prossimo giocatore");
					} else if(((Move) updateMsg).getType().equals("LR")){
						String[] data = st.getData(((Move) updateMsg).getOwner());
						String nick = data[2];
						ObserverFrame.setTextArea("Il giocatore "+ nick + " ha selezionato una lettera già presente, \nil turno passa al prossimo giocatore");
					} else {
						int score = ((Move) updateMsg).getPoints();
						String[] data = st.getData(((Move) updateMsg).getOwner());
						String nick = data[2];
						String n = ((Move) updateMsg).getType().substring(0,1);
						if(score != 0) {
							ObserverFrame.updatePanel(n);
							if(nick.equals(ObserverFrame.getNickF1().getText())) {
								ObserverFrame.setScoreF1(score);
							} else if(nick.equals(ObserverFrame.getNickF2().getText())) {
								ObserverFrame.setScoreF2(score);
							} else {
								ObserverFrame.setScoreF3(score);
							}
							ObserverFrame.setTextArea("Il giocatore "+ nick + " ha selezionato la lettera " + n + "\ne ha guadagnato $" + score + "!");
						} else {
							ObserverFrame.setTextArea("Il giocatore "+ nick + " ha sbagliato selezionando la lettera " + n + "\ne ha perso il turno!");
						}
						ObserverFrame.startTimer(5);
					}
				}
			}
		}
	}
	
	/** metodo per la gestione delle notifiche dirette ai giocatori e provenienti dal ServerRdF
	 * 	@param updateMsg indica la stringa che contiene l'oggetto della notifica ricevuta dal ServerRdF
	 * 	@throws RemoteException per la gestione delle eccezioni
	 * */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public synchronized void playerNotify(Object updateMsg) throws RemoteException {
		if(updateMsg instanceof ArrayList<?>) {
			if(((ArrayList) updateMsg).get(0).equals("manche")) {
				String winner = (String) ((ArrayList) updateMsg).get(1);
				String[] data = st.getData(winner);
				winner = data[2];
				((ArrayList) updateMsg).remove(1);
				String score = (String) ((ArrayList) updateMsg).get(1);
				((ArrayList) updateMsg).remove(1);
				Iterator<String> it1 = ((ArrayList<String>) updateMsg).iterator();
				while(it1.hasNext()) {
					if(it1.next().equals(email)) {
						WheelPanel.stopTimerObs();
						WheelPanel.startMancheTimerObs(10);
						if(PhrasePanel.getNick1().equals(winner)) {
							PhrasePanel.setBank1F(Integer.parseInt(score));
						} else {
							PhrasePanel.setBank2F(Integer.parseInt(score));
						}
						WheelPanel.revealPhrase();
						PhrasePanel.resetPoints();
						PopUp pu = new PopUp("Manche Terminata", "Un giocatore ha vinto la manche", Color.RED, null);
						pu.add();
						WheelPanel.setWheelState("Hai perso la manche numero " + WheelOfFortune.getManche() + " ma puoi ancora vincere la partita!");
					}
				}
			} else if(((ArrayList) updateMsg).get(0).equals("victory")) {
				String winner = (String) ((ArrayList) updateMsg).get(1);
				((ArrayList) updateMsg).remove(1);
				((ArrayList) updateMsg).remove(1);
				Iterator<String> it3 = ((ArrayList<String>) updateMsg).iterator();
				while(it3.hasNext()) {
					if(it3.next().equals(email)) {
						WheelPanel.stopTimerObs();
						WheelPanel.Close();
						new FinalFrame();
						if(email.equals(winner)) {
							PopUp pu = new PopUp("Vittoria Partita", "Hai vinto la partita!", Color.GREEN, null);
							pu.add();
						} else {
							PopUp pu = new PopUp("Game Over", "Ci dispiace, hai perso la partita", Color.RED, null);
							pu.add();
						}
					}
				}
			} else if(((ArrayList) updateMsg).get(0).equals("frasi")) {
				Iterator<String> it = ((ArrayList<String>) updateMsg).iterator();
				while(it.hasNext()) {
					if(it.next().equals(email)) {
						PopUp pu = new PopUp("Frasi Terminate", "Ops, non ci sono nuove frasi disponibili! \nSarà inviata una mail agli amministratori", Color.RED, null);
						pu.add();
						new PlayerFrame();
						LoadingFrame.frame.dispose();
						player = false;
						removeMe();
					}
				}
			} else if(((ArrayList) updateMsg).get(0).equals("exitlobby")) {
				Iterator<String> it4 = ((ArrayList<String>) updateMsg).iterator();
				while(it4.hasNext()) {
					if(it4.next().equals(email)) {
						LoadingFrame.addPlayer(-1);
						LoadingFrame.updatePlayers();
					}
				}
			} else if(((ArrayList) updateMsg).get(0).equals("SR")) {
				String owner = (String) ((ArrayList) updateMsg).get(2);
				((ArrayList) updateMsg).remove(2);
				Iterator<String> it6 = ((ArrayList<String>) updateMsg).iterator();
				while(it6.hasNext()) {
					if(it6.next().equals(email)) {
						WheelPanel.startTimerObs(5);
						String s = (String) ((ArrayList) updateMsg).get(1);
						String[] data = st.getData(owner);
						owner = data[2];
						if(s.contains("PASSA")) {
							WheelPanel.setWheelState("Il giocatore "+ owner +" ha pescato " + s +"\ne deve decidere se utilizzare il jolly");
						} else {
							WheelPanel.setWheelState("Il giocatore "+ owner +" ha pescato " + s +"\ne deve selezionare una consonante");
						}
					}
				}
			} else {
				Iterator<String> it2 = ((ArrayList<String>) updateMsg).iterator();
				while(it2.hasNext()) {
					if(it2.next().equals(email)) {
						LoadingFrame.addPlayer(1);
						LoadingFrame.updatePlayers();
						
						if(nPlayers(ClientRdF.idGame)==3) {
							try {
								new WheelFrame();
							} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
								e.printStackTrace();
							}
						}
						break;
					} 
				}
		   }

		} else if (updateMsg instanceof String) {
			String mail = (String)updateMsg; 
			if(email.equals(mail)) {
				WheelPanel.wheelstart(true);
				if(WheelOfFortune.isAllConsonantsGuessed()) {
					WheelPanel.unlockSolvePhrase(true);
					WheelPanel.disableLetters(true);
				}
				WheelPanel.setWheelState("Tocca a te!");
				FirstPanel.setNickForeground(Color.GREEN);
				PhrasePanel.setTurn(0);
				WheelPanel.stopMancheTimerObs();
				WheelPanel.stopTimerObs();
				WheelPanel.startTimer(5);
			} else {
				String[] dt = st.getData(mail);
				WheelPanel.setWheelState("Il turno è in mano a " + dt[2]);
				FirstPanel.setNickForeground(Color.BLACK);
				if(PhrasePanel.getNick1().equals(dt[2])) {
					PhrasePanel.setTurn(1);
				} else {
					PhrasePanel.setTurn(2);
				}
				WheelPanel.disableBottons(false);
				WheelPanel.stopTimer();
				WheelPanel.startTimerObs(5);
			}
		} else if (updateMsg instanceof Move) {
			ArrayList<String> players = (ArrayList<String>)((Move) updateMsg).getTo();
			for(String p: players) {
				if(email.equals(p)) {
					if(((Move) updateMsg).getType().equals("esce")) {
						JOptionPane.getRootFrame().dispose();
						String[] data = st.getData(((Move) updateMsg).getOwner());
						WheelPanel.quit(data[2]);
						removeMe();
					} else if(((Move) updateMsg).getType().contains("allcns")) {
						String[] data = st.getData(((Move) updateMsg).getOwner());
						String nick = data[2];
		
						if(nick.equals(PhrasePanel.getNick1())) {
							PhrasePanel.setPoints1F(((Move) updateMsg).getPoints());
						} else {
							PhrasePanel.setPoints2F(((Move) updateMsg).getPoints());
						}
						String n = ((Move) updateMsg).getType().substring(0,1);
						WheelPanel.updatePanel(n);
						PopUp pu = new PopUp("Consonanti Terminate", "Attenzione, le consonanti sono terminate \nevita di selezionarle", Color.RED, null);
						pu.add();
					} else if(((Move) updateMsg).getType().contains("allvoc")) {
						String[] data = st.getData(((Move) updateMsg).getOwner());
						String nick = data[2];
		
						if(nick.equals(PhrasePanel.getNick1())) {
							PhrasePanel.setPoints1F(((Move) updateMsg).getPoints());
						} else {
							PhrasePanel.setPoints2F(((Move) updateMsg).getPoints());
						}
						String n = ((Move) updateMsg).getType().substring(0,1);
						WheelPanel.updatePanel(n);
						PopUp pu = new PopUp("Vocali Terminate", "Attenzione, le vocali sono terminate \nevita di selezionarle", Color.RED, null);
						pu.add();
					} else if (((Move) updateMsg).getType().equals("UJ")){
						WheelPanel.stopTimerObs();
						String[] data = st.getData(((Move) updateMsg).getOwner());
						String nick = data[2];
						if(PhrasePanel.getNick1().equals(nick)) {
							PhrasePanel.addBonus1F(-1);
						} else {
							PhrasePanel.addBonus2F(-1);
						}
						WheelPanel.setWheelState("Il giocatore "+ nick +" ha usato il jolly e mantiene il turno!");
						WheelPanel.startTimerObs(5);
					} else if(((Move) updateMsg).getType().equals("Perde")) {
						String[] data = st.getData(((Move) updateMsg).getOwner());
						String nick = data[2];
						if(PhrasePanel.getNick1().equals(nick)) {
							PhrasePanel.resetPoints1F();
						} else {
							PhrasePanel.resetPoints2F();
						}
					} else if(((Move) updateMsg).getType().equals("Passa")) {
						String[] data = st.getData(((Move) updateMsg).getOwner());
						String nick = data[2];
						WheelPanel.setWheelState("Il giocatore "+ nick + " ha pescato PASSA e ha perso il turno");
					} else if(((Move) updateMsg).getType().equals("TJ")) {
						String[] data = st.getData(((Move) updateMsg).getOwner());
						String nick = data[2];
						if(PhrasePanel.getNick1().equals(nick)) {
							PhrasePanel.addBonus1F(1);
						} else {
							PhrasePanel.addBonus2F(1);
						}
						WheelPanel.startTimerObs(5);
					} else if(((Move) updateMsg).getType().equals("GR")){
						WheelPanel.stopTimerObs();
						String[] data = st.getData(((Move) updateMsg).getOwner());
						String nick = data[2];
						WheelPanel.setWheelState("Il giocatore "+ nick +" ha girato la ruota\nla ruota gira...");
					} else if(((Move) updateMsg).getType().equals("TS")) {
						String[] data = st.getData(((Move) updateMsg).getOwner());
						String nick = data[2];
						WheelPanel.addWheelStateTxt("Il giocatore "+ nick +" ha perso tempo");
					} else if(((Move) updateMsg).getType().equals("RF")){
						WheelPanel.startTimerObs(10);
						String[] data = st.getData(((Move) updateMsg).getOwner());
						String nick = data[2];
						WheelPanel.setWheelState("Il giocatore " + nick + " sta provando ad indovinare la frase misteriosa...");
					} else if(((Move) updateMsg).getType().equals("ERR")){
						String[] data = st.getData(((Move) updateMsg).getOwner());
						String nick = data[2];
						WheelPanel.setWheelState("Il giocatore " + nick + " ha sbagliato ad inserire la frase/n ha perso il turno");
					} else {
						String[] data = st.getData(((Move) updateMsg).getOwner());
						String nick = data[2];
						if(nick.equals(PhrasePanel.getNick1())) {
							PhrasePanel.setPoints1F(((Move) updateMsg).getPoints());
						} else {
							PhrasePanel.setPoints2F(((Move) updateMsg).getPoints());
						}
						WheelPanel.updatePanel(((Move) updateMsg).getType());
					}
				}
			}
		}
	}
	
	/** metodo utilizzato per rimuovere il riferimento all'utente dalla lista degli observer (coloro che ricevono le notifiche)
	 * 	@throws RemoteException per la gestione delle eccezioni
	 * */
	public static void removeMe() throws RemoteException {
		try{
			if(player) {
				player = false;
			} else if(observer) {
				observer = false;
			}
			st.removeObs(clientStub);
			idGame = 0;
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main (String args[]) {
		connectionManager("localhost");
	}
	
	/** metodo utilizzato per instaurare la connessione con il ServerRdF
	 *  @param ip indica la stringa contenente l'indirizzo ip del ServerRdF
	 * 	@throws RemoteException per la gestione delle eccezioni
	 * */
	private static void connectionManager(String ip) {
		try{
			registry = LocateRegistry.getRegistry(ip, 1099);
		    st = (ServerInterface) registry.lookup("AddService");
	    	client = new ClientRdF();
			clientStub = (RemoteObserver)UnicastRemoteObject.exportObject(client, 0);
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception ex) {}

			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run(){
					try {
						new LogFrame();
					} catch(Exception e) {}
				}
			});
		}catch(Exception e){
			String serverIP = JOptionPane.showInputDialog(null, "Inserisci l'indirizzo IP del server:", "Connessione A LocalHost Non Riuscita", JOptionPane.INFORMATION_MESSAGE);
			if(serverIP != null) {
				connectionManager(serverIP);
			} else {
				System.exit(1);
			}
		}
	}
	
	/**
	 * metodo utilizzato per capire se l'utente è un giocatore
	 * @return valore del campo Client.player
	 * */
	public static boolean isPlayer() {
		return player;
	}
	
	/**
	 * metodo utilizzato per capire se l'utente è un osservatore
	 * @return valore del campo Client.observer
	 * */
	public static boolean isObserver() {
		return observer;
	}
	
	/**
	 * metodo utilizzato per richiamare il metodo c_turn di ServerRdF
	 * @throws RemoteException per la gestione delle eccezioni
	 * */
	public static void changeTurn() throws RemoteException {
		 st.c_turn(st.getNTurn(idGame),idGame,email,WheelOfFortune.getManche());
    }

	/**
	 * metodo utilizzato per richiamare il metodo addUser di ServerRdF
	 * @param email indica l'indirizzo email dell'utente
	 * @param name indica il nome dell'utente
	 * @param surname indica il cognome dell'utente
	 * @param nickname indica il soprannome dell'utente
	 * @throws RemoteException per la gestione delle eccezioni
	 * @return true se l'operazione va a buon fine, false nel caso contrario
	 * */
	public static boolean addUser(String email, String name, String surname, String nickname) throws RemoteException{
		return st.addUser(email, name, surname, nickname, false);		
	}

	/**
	 * metodo utilizzato per richiamare il metodo victory di ServerRdF
	 * @param points indica il punteggio partita
	 * @throws RemoteException per la gestione delle eccezioni
	 * @return vincitore della partita
	 * */
	public static String victory(int points) throws RemoteException {
		return st.victory(email, idGame, points);
	}

	/**
	 * metodo utilizzato per richiamare il metodo login di ServerRdF
	 * @param email indica l'indirizzo email dell'utente
	 * @param password indica la stringa relativa al codice di accesso
	 * @throws RemoteException per la gestione delle eccezioni
	 * @return true se l'operazione va a buon fine, false nel caso contrario
	 * */
	public static boolean login(String email, String password) throws RemoteException{
		return st.login(email, password);
	}
	
	/**
	 * metodo utilizzato per richiamare il metodo setOnline di ServerRdF
	 * @param on indica il valore booleano che indica lo stato dell'utente (true = online/false = offline)
	 * @throws RemoteException per la gestione delle eccezioni
	 * @return true se l'operazione va a buon fine, false nel caso contrario
	 * */
	public static boolean setOnline(boolean on) throws RemoteException {
		return st.setOnline(email, on);
	}
	
	/**
	 * metodo utilizzato per richiamare il metodo checkCode di ServerRdF
	 * @param email indica l'indirizzo email dell'utente
	 * @param code indica il codice d'accesso (password)
	 * @throws RemoteException per la gestione delle eccezioni
	 * @return true se l'operazione va a buon fine, false nel caso contrario
	 * */
	public static boolean checkCode(String email, String code) throws RemoteException{
		return st.checkCode(email, code);
	}
	
	/**
	 * metodo utilizzato per richiamare il metodo updatePassword di ServerRdF
	 * @param passwN indica la nuova password 
	 * @param passwA indica la vecchia password
	 * @throws RemoteException per la gestione delle eccezioni
	 * @return true se l'operazione va a buon fine, false nel caso contrario
	 * */
	public static boolean updatePassword(String passwN, String passwA) throws RemoteException{
		return st.updatePassword(email, passwN, passwA);
	}
	
	/**
	 * metodo utilizzato per richiamare il metodo updateCode di ServerRdF
	 * @param passwN indica la nuova password 
	 * @param passwA indica il codice d'attivazione da rimuovere
	 * @throws RemoteException per la gestione delle eccezioni
	 * @return true se l'operazione va a buon fine, false nel caso contrario
	 * */
	public static boolean updateCode(String passwN, String passwA) throws RemoteException{
		return st.updateCode(email, passwN, passwA);
	}

	/**
	 * metodo utilizzato per richiamare il metodo getData di ServerRdF
	 * @throws RemoteException per la gestione delle eccezioni
	 * @return dati relativi all'utente che esegue il metodo
	 * */
	public static String[] getData() throws RemoteException{
		return st.getData(email);
	}

	/**
	 * metodo utilizzato per richiamare il metodo updateProfile di ServerRdF
	 * @param name indica il nome dell'utente
	 * @param surname indica il cognome dell'utente
	 * @param nickname indica il soprannome dell'utente
	 * @throws RemoteException per la gestione delle eccezioni
	 * */
	public static void updateProfile(String name, String surname, String nickname) throws RemoteException{
		st.updateProfile(email, name, surname, nickname);
	}

	/**
	 * metodo utilizzato per richiamare il metodo resetPassword di ServerRdF
	 * @param email indica l'indirizzo email dell'utente
	 * @throws RemoteException per la gestione delle eccezioni
	 * @return true se l'operazione va a buon fine, false nel caso contrario
	 * */
	public static boolean resetPassword(String email) throws RemoteException{
		return st.resetPassword(email);
	}

	/**
	 * metodo utilizzato per richiamare il metodo newGame di ServerRdF
	 * @throws RemoteException per la gestione delle eccezioni
	 * @return identificativo della partita creata
	 * */
	public static long newGame() throws RemoteException{
		return idGame = st.createMatch(email);
	}

	/**
	 * metodo utilizzato per richiamare il metodo gameList di ServerRdF
	 * @throws RemoteException per la gestione delle eccezioni
	 * @return LinkedHashMap contenente le partite in corso
	 * */
	public static LinkedHashMap<Long,String> gameList() throws RemoteException{
		return st.matchesList();
	}

	/**
	 * metodo utilizzato per richiamare il metodo nPlayers di ServerRdF
	 * @param idGame indica l'id della partita
	 * @throws RemoteException per la gestione delle eccezioni
	 * @return numero di giocatori che stanno partecipando alla partita (o alla lobby) identificata da idGame 
	 * */
	public static int nPlayers(long idGame) throws RemoteException{
		return st.nPlayers(idGame);
	}

	/**
	 * metodo utilizzato per richiamare il metodo getPhrase di ServerRdF
	 * @throws RemoteException per la gestione delle eccezioni
	 * @return frase relativa alla partita idGame (della manche corrente)
	 * */
	public static ArrayList<String> getPhrase() throws RemoteException{
		return st.getPhrase(idGame);
	}

	/**
	 * metodo utilizzato per richiamare il metodo getTheme di ServerRdF
	 * @param idGame indica l'id della partita
	 * @throws RemoteException per la gestione delle eccezioni
	 * @return indizio utile al giocatore per indovinare la frase relativo alla partita identificata con idGame
	 * */
	public static String getCategory(long idGame) throws RemoteException{
		return st.getTheme(idGame);
	}  

	/**
	 * metodo utilizzato per richiamare il metodo endMatch di ServerRdF ed il metodo ClientRdF.removeMe 
	 * @throws RemoteException per la gestione delle eccezioni
	 * */
	public static void endGame() throws RemoteException{
		st.endMatch(idGame);
		idGame = 0;
		removeMe();
	}

	/**
	 * metodo utilizzato per richiamare il metodo updateMove di ServerRdF
	 * @param nManche indica il numero della manche
	 * @param email indica l'indirizzo email dell'utente 
	 * @param points indica il punteggio
	 * @param move indica il tipo della mossa
	 * @throws RemoteException per la gestione delle eccezioni
	 * 
	 * */
	public static void updateMove(int nManche, String email, int points, String move) throws RemoteException{
		st.updateMove(nManche,email, points, move, idGame);
	}

	/**
	 * metodo utilizzato per richiamare il metodo logGame di ServerRdF
	 * @throws RemoteException per la gestione delle eccezioni
	 * @return true se joinMatch restituisce true, false nel caso contrario
	 * */
	public static boolean logGame() throws RemoteException{
		if(st.joinMatch(email, idGame)) {			
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * metodo utilizzato per richiamare il metodo logObGame di ServerRdF
	 * @throws RemoteException per la gestione delle eccezioni
	 * @return true se joinObserver restituisce true, false nel caso contrario
	 * */
	public static boolean logObGame() throws RemoteException{
		if(st.joinObserver(email, idGame)) {			
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * metodo utilizzato per richiamare il metodo getPlayerList di ServerRdF
	 * @throws RemoteException per la gestione delle eccezioni
	 * @return lista dei giocatori che stanno partecipando alla partita identificata da idGame
	 * */
	public static ArrayList<String> getPlayerList() throws RemoteException{
		return st.allPlayerList(idGame);
	}

	/**
	 * metodo utilizzato per richiamare il metodo getCreator di ServerRdF
	 * @param idGame indica l'id della partita
	 * @throws RemoteException per la gestione delle eccezioni
	 * @return creatore della partita identificata da idGame
	 * */
	public static String getCreator(long idGame) throws RemoteException{
		return st.getCreator(idGame);
	}

	/**
	 * metodo utilizzato per richiamare il metodo updateManche di ServerRdF
	 * @param points indica il punteggio relativo al giocatore che ha vinto la manche
	 * @throws RemoteException per la gestione delle eccezioni
	 * */
	public static void updateManche(int points) throws RemoteException{
		st.updateManche(email, idGame, points); 
	}
	
	/**
	 * metodo utilizzato per richiamare il metodo addObs di ServerRdF
	 * @throws RemoteException per la gestione delle eccezioni
	 * */
	public static void addObs() throws RemoteException {
		st.addObs(clientStub);
	}
	
	/**
	 * metodo utilizzato per ottenere il riferimento all'oggetto remoto, che permette di invocare i metodi di ServerRdF
	 * @return stub dell'utente
	 * */
	public static ServerInterface getSt() {
		return st;
	}
	
	/**
	 * metodo utilizzato per richiamare i metodi exitLobby(se l'utente è un giocatore) 
	 * ed exitObserver (se l'utente è un osservatore) di ServerRdF
	 * @throws RemoteException per la gestione delle eccezioni
	 * */
	public static void exitLobby() throws RemoteException {
		if(player) {
			player = false;
			st.exitLobby(email, idGame);
		} else if(observer) {
			observer = false;
			st.exitLobbyObs(email, idGame);
		}
	}
	
	/**
	 * metodo utilizzato per richiamare il metodo exitObserver di ServerRdF
	 * @throws RemoteException per la gestione delle eccezioni
	 * */
	public static void exitObserver() throws RemoteException {
		st.exitObserver(email, idGame);
	}
	
	/**
	 * metodo utilizzato per richiamare il metodo notifyWheelStop di ServerRdF
	 * @param s indica il punteggio sotto forma di stringa
	 * @throws RemoteException per la gestione delle eccezioni 
	 * */
	public static void notifyWheelStop(String s) throws RemoteException {
		st.notifyWheelStop(idGame, email, s);
	}
} 
