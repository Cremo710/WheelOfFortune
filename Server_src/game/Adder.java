package game;
import java.util.ArrayList;
import java.rmi.*;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.io.*;

import javax.mail.MessagingException;

import database.GameQuery;
import database.LoginQuery;
import database.PhraseQuery;
import database.ProfileUpdateQuery;
import database.RegistrationQuery;
import database.StatisticsQuery;

/**
 * la classe Adder rappresenta la classe contenente i metodi del server accessibili dal client attraverso l'interfaccia condivisa
 * <p>
 * Un oggetto osservabile può avere uno o più osservatori. 
 * Un osservatore può essere qualsiasi oggetto che implementa l'interfaccia Observer.
 * Dopo la modifica di un'istanza osservabile, un'applicazione che chiama il metodo notificationObservers dell'Osservabile 
 * fa sì che tutti i suoi osservatori vengano informati della modifica mediante una chiamata al loro metodo di aggiornamento.
 * L'ordine in cui verranno consegnate le notifiche non è specificato. 
 * L'implementazione predefinita fornita nella classe Osservabile avviserà gli Osservatori nell'ordine in cui hanno registrato 
 * gli interessi
 * Quando un oggetto osservabile viene appena creato, il suo set di osservatori è vuoto. 
 * @author Andrea Peluso
 * @version 1.0
 */
public class Adder extends Observable implements ServerInterface,Serializable {
	private static final long serialVersionUID = 1L;
	private ArrayList<WrappedObserver> WrappedObserver;

	/**
	 * costruisco la lista contenente i client connessi
	 * @throws RemoteException per la gestione delle eccezioni
	 */
	Adder() throws RemoteException{
		WrappedObserver= new ArrayList<WrappedObserver>();
	}

	public void removeObs(RemoteObserver observer) {
		ArrayList<WrappedObserver> observertoremove = new ArrayList<WrappedObserver>();

		for(WrappedObserver w:WrappedObserver){
			if(w.getObs().equals(observer)){
				try{
					observertoremove.add(w);
					deleteObserver(w);	
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}

		for(WrappedObserver wtoremove:observertoremove){
			WrappedObserver.remove(wtoremove);
		}
	}
	
	/**
	 * metodo usato per aggiungere un client quando deve ricever le notifiche dal server
	 * @param o indica il client da aggiungere
	 */
	public void addObs(RemoteObserver o) {
		System.out.println(o);
		WrappedObserver mo=new WrappedObserver(o);
		WrappedObserver.add(mo);//aggiunge nuovo osservatore alla lista degli osservatori 
		System.out.println("Aggiunto");
		try{
			addObserver(mo);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * metodo usato per inviare le notifiche ai client interessati
	 * @param idMatch indica l'identificativo della partita
	 * @param email indica 
	 */
	public void notify(long idMatch,String email){
		ArrayList<String> result=new ArrayList<String>();
		GameQuery RdFDB = new GameQuery();
		if(RdFDB.connect()){//connette al database
			try{
				result=RdFDB.player_list(idMatch,email);//ottiene la lista dei giocatori da notificare
			}catch(Exception e){}
		}else{
			System.out.println("problem of connection!");
		}
		setChanged();
		try{
			notifyObservers(result);//notifica tutti gli osservatori mandandogli la lista contenente le e-mail degli osservatori interessati alla notifica
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * metodo usato per notificare ai client che un giocatore della partita è uscito dalla lobby
	 * @param idMatch indica l'identificativo della parita
	 * @param email indica l'email di chi è uscito dalla lobby
	 */
	public void notifyExitLobby(long idMatch,String email){
		ArrayList<String> result=new ArrayList<String>();
		ArrayList<String> players=new ArrayList<String>();
		GameQuery RdFDB = new GameQuery();

		if(RdFDB.connect()){//connette al database
			try{
				players=RdFDB.otherPlayer_list(idMatch,email);//ottiene la lista dei giocatori nella lobby
			}catch(Exception e){}
		}

		if(RdFDB.connect()){//connette al database
			try{
				result=RdFDB.player_list(idMatch,email);//ottiene la lista dei giocatori da notificare
			}catch(Exception e){}
			if(!players.isEmpty()){
				result.add(0,"exitlobby");
				setChanged();
				try{
					notifyObservers(result);//notifica tutti gli osservatori mandandogli la lista contenente le e-mail degli osservatori interessati alla notifica
				}catch(Exception e){
					e.printStackTrace();
				}
			}else{
				if(RdFDB.connect()){
					try{
						RdFDB.remove_match(idMatch);
					}catch(Exception e){
						e.printStackTrace();
					}
					if(!result.isEmpty()){
						result.add(0,"exitlobby");
						setChanged();
						try{
							notifyObservers(result);//notifica tutti gli osservatori mandandogli la lista contenente le e-mail degli osservatori interessati alla notifica
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}
			}
		}else{
			System.out.println("problem of connection!");
		}
	}
	
	/**
	 * metodo usato per ottenere l'id degli altri giocatori della partita
	 * @param idMatch indica l'identificativo della partita
	 * @param nick indica l'email del giocatore
	 * @return lista degli altri due giocatori della partita
	 */
	public ArrayList<String> otherPlayerList(long idMatch,String nick){
		ArrayList<String> result=new ArrayList<String>();
		GameQuery RdFDB = new GameQuery();
		if(RdFDB.connect()){//connette al database
			try{
				result=RdFDB.otherPlayer_list(idMatch,nick);//ottiene la lista degli altri 2 giocatori della partita 
			}catch(Exception e){}
		}else{
			System.out.println("problem of connection!");
		}
		return result;

	}

	/**
	 * metodo usato dagli osservatori per ottenre la lista dei giocatori della partita
	 * @param idMatch indica l'identificativo della partita
	 * @return lista dei giocatori della partita
	 */
	public ArrayList<String> allPlayerList(long idMatch){
		ArrayList<String> result=new ArrayList<String>();
		GameQuery RdFDB = new GameQuery();
		if(RdFDB.connect()){//connette al database
			try{
				result=RdFDB.allPlayer_list(idMatch);//ottiene la lista di tutti i giocatori della partita
			}catch(Exception e){}
		}else{
			System.out.println("problem of connection!");
		}
		return result;

	}
	
	/**
	 * metodo usato per ottenere il punteggio dei giocatori della partita
	 * @param idMatch indica l'identificativo della partita
	 * @param nickname indica l'email del giocatore
	 * @return indica il punteggio del giocatore
	 */
	public int getScore(long idMatch,String nickname){
		int score=0;
		GameQuery RdFDB = new GameQuery();
		if(RdFDB.connect()){//connette al database
			try{
				score=RdFDB.get_Score(idMatch,nickname);//ottiene i punteggi dei giocatori(usato dagli osservatori)
				System.out.println(score);
			}catch(Exception e){}
		}else{
			System.out.println("problem of connection!");
		}
		return score;

	}

	/**
	 * metodo usato per ottenere il deposito della partita
	 * @param idMatch indica l'identificativo della partita
	 * @param nickname indica l'email del giocatore
	 * @return indica il depostio del giocatore
	 */
	public int getBank(long idMatch,String nickname){
		int bank=0;
		GameQuery RdFDB = new GameQuery();
		if(RdFDB.connect()){//connette al database
			try{
				bank=RdFDB.get_Bank(idMatch,nickname);//ottiene i punteggi dei giocatori(usato dagli osservatori)
			}catch(Exception e){}
		}else{
			System.out.println("problem of connection!");
		}
		return bank;

	}

	/**
	 * metodo usato per ottenere il numero della manche della partita
	 * @param idMatch indica l'identificativo della partita
	 * @return indica il numero della manche
	 */
	public int getNmanche(long idMatch){
		int nmanche=0;
		GameQuery RdFDB = new GameQuery();
		if(RdFDB.connect()){//connette al database
			try{
				nmanche=RdFDB.get_Nmanche(idMatch);//ottiene il numero della manche per gli osservatori
			}catch(Exception e){}
		}else{
			System.out.println("problem of connection!");
		}
		return nmanche;

	}

	/**
	 * metodo usato per ottenere il giocatore che ha il diritto di fare la prima mossa nella partita
	 * @param idMatch indica l'identificativo della partita
	 * @return indica il turno
	 */
	public String getFirstTurn(long idMatch){
		String turn=null;
		GameQuery RdFDB = new GameQuery();
		if(RdFDB.connect()){//connette al database
			try{
				turn=RdFDB.get_Turn(idMatch);//ottiene il numero della manche per gli osservatori
			}catch(Exception e){}
		}else{
			System.out.println("problem of connection!");
		}
		return turn;

	}
	
	/**
	 * metodo usato per ottenere il giocatore che ha il turno nella partita
	 * @param idMatch indica l'identificativo della partita
	 * @return indica il turno
	 */
	public int getNTurn(long idMatch){
		int turn=0;
		GameQuery RdFDB = new GameQuery();
		if(RdFDB.connect()){//connette al database
			try{
				turn=RdFDB.getNTurn(idMatch);//ottiene il giocatore che ha il turno
			}catch(Exception e){}
		}else{
			System.out.println("problem of connection!");
		}
		return turn;
	}

	/**
	 * metodo usato per ottenere il numero dei jolly del giocatore nella partita
	 * @param idMatch indica l'identificativo della partita
	 * @param nickname indica l'email del giocatore
	 * @return indica il numero di jolly
	 */
	public int getJolly(long idMatch,String nickname){
		int jolly=0;
		GameQuery RdFDB = new GameQuery();
		if(RdFDB.connect()){//connette al database
			try{
				jolly=RdFDB.get_jolly(idMatch,nickname);//ottiene il numero dei jolly dei giocatori(usato dagli osservatori)
			}catch(Exception e){}
		}else{
			System.out.println("problem of connection!");
		}
		return jolly;

	}

	/**
	 * metodo usato per ottenere le lettere della frase nel tabellone svelate nella partita
	 * @param idMatch indica l'identificativo della partita
	 * @return indica le lettere svelate
	 */
	public ArrayList<String> getLetters(long idMatch){
		ArrayList<String> letters=new ArrayList<String>();
		GameQuery RdFDB = new GameQuery();
		if(RdFDB.connect()){//connette al database
			try{
				letters=RdFDB.get_Letters(idMatch);//ottiene le lettere della frase (per gli osservatori)
			}catch(Exception e){}
		}else{
			System.out.println("problem of connection!");
		}
		return letters;

	}

	/**
	 * metodo usato per notificare la non disponibilità di frasi per avviare la partita
	 * @param players indica l'identificativo dei giocatori da notificare
	 */
	public void notify_nophrases(ArrayList<String> players){
		ArrayList<String> result=new ArrayList<String>();
		result=players;
		result.add(0,"frasi");
		setChanged();
		try{
			notifyObservers(result);//notifica tutti gli osservatori mandandogli la lista contenente le e-mail degli osservatori interessati alla notifica
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * metodo usato per notificare il cambiamento della manche nella partita
	 * @param idMatch indica l'identificativo della partita
	 * @param email indica l'email del giocatore
	 * @param score il punteggio di chi ha vinto la manche
	 */
	public void notifyManche(long idMatch,String email,int score){
		ArrayList<String> result=new ArrayList<String>();
		GameQuery RdFDB = new GameQuery();
		result.add("manche");
		String winner = email;
		result.add(winner);
		result.add(String.valueOf(score));

		if(RdFDB.connect()){//connette al database
			try{
				for(String s:RdFDB.player_list(idMatch,email)){
					result.add(s);
				}
			}catch(Exception e){}
		}else{
			System.out.println("problem of connection!");
		}

		setChanged();
		try{
			notifyObservers(result);//notifica tutti gli osservatori mandandogli la lista contenente le e-mail degli osservatori interessati alla notifica
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * metodo usato per notificare la vittoria dellla partita
	 * @param email indica l'email del giocatore
	 * @param idMatch indica l'identificativo della partita
	 * @param score il punteggio del vincitore dell'ultima manche
	 * @return indica il vincitore dell'ultima manche
	 */
	public String victory(String email,long idMatch,int score){
		ArrayList<String> result=new ArrayList<String>();
		GameQuery RdFDB = new GameQuery();
		String winner = null;

		if(RdFDB.connect()){//connette al database
			try{
				RdFDB.update_last_manche(idMatch,email,score);//aggiorna il numero della manche
			}catch(Exception e){}
		}else{
			System.out.println("problem of connection!");
		}

		if(RdFDB.connect()){//connette al database
			try{
				RdFDB.end_match(idMatch);//imposto data fine partita
			}catch(Exception e){}
		}else{
			System.out.println("problem of connection!");
		}

		result.add("victory");


		if(RdFDB.connect()){//connette al database
			try{
				winner=RdFDB.get_winner(idMatch);//ottiene il vincitore partita
			}catch(Exception e){}

		}else{
			System.out.println("problem of connection!");
		}

		result.add(winner);
		
		result.add(email);
		
		if(RdFDB.connect()){//connette al database
			try{
				for(String s:RdFDB.player_list(idMatch,email)){
					result.add(s);
				}
			}catch(Exception e){}
		}else{
			System.out.println("problem of connection!");
		}
		setChanged();
		try{
			notifyObservers(result);//notifica tutti gli osservatori mandandogli la lista contenente le e-mail degli osservatori interessati alla notifica
		}catch(Exception e){
			e.printStackTrace();
		}

		return winner;
	}

	/**
	 * metodo usato per notificare il cambio di turno
	 * @param data indica l'email di chi ha perso il turno
	 */
	public void notifyCTurn(String data){
		setChanged();
		try{
			notifyObservers(data);//notifica gli osservatori assegnando il turno a chi deve girare la ruota
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * metodo usato per notificare una mossa effettuata
	 * @param move indica la mossa effettuata
	 */
	public void notifyMove(Move move){
		setChanged();
		try{
			notifyObservers(move);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * metodo usato per aggiungere un nuovo utente
	 * @param email indica l'email dell'utente
	 * @param name indica il nome dell'utente
	 * @param surname indica il cognome dell'utente
	 * @param nickname indica il nickname dell'utente
	 * @param isAdmin indica se l'utente è o no un amministatore
	 * @return indica se l'utente è stato aggiunto correttamente
	 */
	public boolean addUser(String email,String name,String surname, String nickname,boolean isAdmin){
		boolean returned=false;
		RegistrationQuery RdFDB = new RegistrationQuery();
		if(RdFDB.connect()){//prova a connettersi al database
			try {
				returned=RdFDB.insert_newuser(email,name,surname,nickname,isAdmin);//inserisce un nuovo utenten nel database
			} catch (UnsupportedEncodingException
					| NoSuchAlgorithmException | MessagingException e) {
				e.printStackTrace();
			}}else{
				System.out.println("problem of connection!");
			}

		return returned;

	}

	/**
	 * metodo usato per effettuare il login da parte di un utente non amministratore
	 * @param email indica l'email di chi effettua il login 
	 * @param passw indica la password di chi effettua il login 
	 * @return indica se il login è andato a buon fine
	 */
	public boolean login(String email,String passw){
		boolean returned=false;
		LoginQuery RdFDB = new LoginQuery();
		if(RdFDB.connect()){
			try{
				returned=RdFDB.user_login(email,passw);//prova ad effettuare il login
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			System.out.println("problem of connection!");
		}
		return returned;

	}

	/**
	 * metodo usato per effettuare il login da parte di un utente amministratore
	 * @param email indica l'email di chi effettua il login 
	 * @param passw indica la password di chi effettua il login 
	 * @return indica se il login è andato a buon fine
	 */
	public boolean loginAdmin(String email,String passw){
		boolean returned=false;
		LoginQuery RdFDB = new LoginQuery();
		if(RdFDB.connect()){
			try{
				returned=RdFDB.admin_login(email,passw);//prova ad effettuare il login
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			System.out.println("problem of connection!");
		}
		return returned;

	}

	/**
	 * metodo usato per verificare se il codice di registrazione corrisponde a quello inviato per email e non è scaduto
	 * @param email indica l'email dell'utente
	 * @param code rappresenta il codice inviato per email
	 * @return indica se il codice è corretto e non è scaduto
	 */
	public boolean checkCode(String email,String code){
		boolean returned=false;
		RegistrationQuery RdFDB = new RegistrationQuery();
		if(RdFDB.connect()){
			try{
				returned=RdFDB.verify_code(email,code);//verifica la correttezza del codice di verifica inserito
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			System.out.println("problem of connection!");
		}
		return returned;
	}

	/**
	 * metodo usato per aggiornare la password 
	 * @param email indica l'email associata alla password
	 * @param passw indica la password nuova
	 * @param code indica la password vecchia
	 * @return indica se l'aggiornamento della password è andato a buon fine
	 */
	public boolean updateCode(String email,String passw,String code){
		boolean returned=false;
		ProfileUpdateQuery RdFDB = new ProfileUpdateQuery();
		if(RdFDB.connect()){
			try{
				returned=RdFDB.code_update(email,passw,code);//dopo aver verificato il codice aggiorna la password
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			System.out.println("problem of connection!");
		}
		return returned;

	}
	
	/**
	 * metodo usato per impostare la password
	 * @param email indica l'email associata alla password
	 * @param passw indica la password nuova
	 * @param code indica la password vecchia
	 * @return indica se l'aggiornamento della password è andato a buon fine
	 */
	public boolean updatePassword(String email,String passw,String code){
		boolean returned=false;
		ProfileUpdateQuery RdFDB = new ProfileUpdateQuery();
		if(RdFDB.connect()){
			try{
				returned=RdFDB.password_update(email,passw,code);//dopo aver verificato il codice aggiorna la password
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			System.out.println("problem of connection!");
		}
		return returned;

	}

	/**
	 * metodo usato per aggiornare il profilo di un utente
	 * @param email indica l'email dell'utente
	 * @param name indica il nome dell'utente
	 * @param surname indica il cognome dell'utente
	 * @param nickname indica il nickname dell'utente
	 */
	public void updateProfile(String email,String name,String surname,String nickname){
		ProfileUpdateQuery RdFDB = new ProfileUpdateQuery();
		if(RdFDB.connect()){
			try{
				RdFDB.profile_update(email,name,surname,nickname);//aggiorna i dati del profilo utente
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			System.out.println("problem of connection!");
		}

	}

	/**
	 * metodo usato per ottenere i dati di un utente
	 * @param email indica l'email dell'utente
	 * @return rappresenta i dati dell'utente
	 */
	public String[] getData(String email){
		String[] data=new String[3];
		GameQuery RdFDB = new GameQuery();
		if(RdFDB.connect()){
			try{
				data= RdFDB.get_data(email);//ottiene i dati del profilo utente
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			System.out.println("problem of connection!");
		}
		return data;
	}

	/**
	 * metodo usatro per ottenere la frase della manche della partita
	 * @param id indica l'id della partita
	 * @return data rappresenta la frase
	 */
	public ArrayList<String> getPhrase(long id){
		ArrayList<String> data=new ArrayList<String>();
		PhraseQuery RdFDB = new PhraseQuery();
		if(RdFDB.connect()){
			try{
				data= RdFDB.get_phrase(id);//ottiene la frase della partita in corso
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			System.out.println("problem of connection!");
		}
		return data;
	}
	
	/**
	 * metodo usato per notificare che la ruota ha finito di girare
	 * @param idMatch indica l'identificativo della partita
	 * @param email indica la email del giocatore a cui si è fermata la ruota
	 * @param score indica il punteggio ottenuto dal giro di ruota
	 */
	public void notifyWheelStop(long idMatch,String email,String score){
		ArrayList<String> result=new ArrayList<String>();
		GameQuery RdFDB = new GameQuery();
		if(RdFDB.connect()){//connette al database
			try{
				result=RdFDB.player_list(idMatch,email);//ottiene la lista dei giocatori da notificare
			}catch(Exception e){}
			result.add(0,email);
			result.add(0,score);
			result.add(0,"SR");
			setChanged();
			try{
				notifyObservers(result);//notifica tutti gli osservatori mandandogli la lista contenente le e-mail degli osservatori interessati alla notifica
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			System.out.println("problem of connection!");
		}
	}
	
	/**
	 * metodo usato per ottenere il tema della frase
	 * @param id indica l'identificativo della partita
	 * @return rappresenta il tema associato alla frase
	 */

	public String getTheme(long id){
		String data = null;
		GameQuery RdFDB = new GameQuery();
		if(RdFDB.connect()){
			try{
				data= RdFDB.get_theme(id);//ottiene il tema della partita in corso
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			System.out.println("problem of connection!");
		}
		return data;
	}

	/**
	 * metodo usato per creare una nuova partita
	 * @param email indica l'email di chi ha creato la partita
	 * @return indica l'identificativo della partita creata
	 */
	public long createMatch(String email){
		long returned=0;
		GameQuery RdFDB = new GameQuery();
		if(RdFDB.connect()){
			try{
				returned=RdFDB.create_match(email);//inserisce nella tabella 'partita' del database i dati relativi alla partita creata
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			System.out.println("problem of connection!");
		}
		return returned;
	}

	/**
	 * metodo usato per ottenere la lista delle partite disponibili
	 * @return indica la lista delle partite
	 */
	public LinkedHashMap<Long,String>  matchesList(){
		LinkedHashMap<Long,String> data = new LinkedHashMap<Long,String>();
		GameQuery RdFDB = new GameQuery();
		if(RdFDB.connect()){
			try{
				data= RdFDB.matches_list();//ottiene la lista delle partite dal database
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			System.out.println("problem of connection!");
		}
		return data;
	}

	/**
	 * metodo usato per ottenere la classifica della partita 
	 * @param idMatch indica l'identificativo della partita
	 * @return rappresenta la classifica
	 */
	public LinkedHashMap<String,Integer>  ranking(long idMatch){
		LinkedHashMap<String,Integer> data = new LinkedHashMap<String,Integer>();
		GameQuery RdFDB = new GameQuery();
		if(RdFDB.connect()){
			try{
				data= RdFDB.ranking(idMatch);//ottiene la classifica
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			System.out.println("problem of connection!");
		}
		return data;
	}
	
	/**
	 * metodo usato per resettare la password in caso di password dimenticata
	 * @param email indica l'utente che ha chiesto il reset
	 * @return indica se il reset è andato a buon fine
	 */
	public boolean resetPassword(String email){
		boolean returned=false;
		ProfileUpdateQuery RdFDB = new ProfileUpdateQuery();
		if(RdFDB.connect()){
			try {
				returned=RdFDB.reset_Password(email);//in caso di password dimenticata invia e-mail con nuova password e la aggiorna nel database
			} catch (UnsupportedEncodingException | NoSuchAlgorithmException
					| MessagingException e) {
				e.printStackTrace();
			}}else
			{
				System.out.println("problem of connection!");
			}

		return returned;

	}

	/**
	 * metodo usato per ottenere il numero di player della partita
	 * @param idMatch indica l'identificativo della partita
	 * @return indica il numero di player della partita
	 */
	public int nPlayers(long idMatch){
		int data=0;
		GameQuery RdFDB = new GameQuery();
		if(RdFDB.connect()){
			try{
				data=RdFDB.n_player(idMatch);//ottiene il numero di giocatori di una specifica partita
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			System.out.println("problem of connection!");
		}
		return data;
	}

	/**
	 * metodo usato per notificare che un utente ha effettuato una mossa
	 * @param nManche indica il numero della manche
	 * @param email indica l'email di chi ha effettuato la mossa
	 * @param score indica il punteggio della mossa
	 * @param move indica il tipo di mossa
	 * @param idMatch indica l'identificativo della partita
	 */
	public void updateMove(int nManche,String email, int score, String move,long idMatch){
		GameQuery RdFDB = new GameQuery();
		ArrayList<String> result=new ArrayList<String>();
		String m=null;
		if(RdFDB.connect()){
			try{
				if(move.contains("all")){
					m=move.substring(0,1);
				}else{
					m=move;
				}
				RdFDB.update_move(nManche,email,score,m,idMatch);//aggiunge la mossa effettutata nella tabella 'mossa' del database	
			}catch(Exception e){
				e.printStackTrace();
			}
			if(RdFDB.connect()){
				try{
					result=RdFDB.player_list(idMatch,email);
				}catch(Exception e){
					e.printStackTrace();
				}
			}

			Move newmove=new Move(move,email,score,result);
			notifyMove(newmove);

		}else{
			System.out.println("problem of connection!");
		}
	}
	
	/**
	 * metodo usato per aggiungere una partecipazione ad una partita
	 * @param email indica l'email di chi ha partecipato
	 * @param idMatch indica l'identificativo della partita
	 * @return indica se l'operazione è andata a buon fine
	 */

	public boolean joinMatch(String email,long idMatch){
		GameQuery RdFDB = new GameQuery();
		if(RdFDB.connect()){
			try{
				RdFDB.join_match(email,idMatch);//aggiunge una partecipazione nella tabella 'partecipazione' alla specifica partita da parte di un player
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			System.out.println("problem of connection!");
		}

		if(nPlayers(idMatch)==3){
			ArrayList<String> players=new ArrayList<String>();
			if(RdFDB.connect()){
				players=RdFDB.player_list(idMatch, email);
			}
			PhraseQuery RdFDB2 = new PhraseQuery();
			if(RdFDB2.connect()){
				if(RdFDB2.checkPhrases(idMatch)){
					if(RdFDB.connect()){
						RdFDB.start_match(idMatch);
					}
					notify(idMatch,email);
					return true;
				}else{
					notify_nophrases(players);
					return false;
				}
			}
			//devo verificare se esistono frasi non ancora utilizzate dai 3 giocatori in tutte le partite
			//se non esistono mando e-mail ad amministratore e notifico dell'annullamento della partita
			//elimino la partita
			//altrimenti inserisco le frasi e inizio la partita
		}	
		else{
			notify(idMatch,email);//notifica gli osservatori interessati a questo aggiornamento,ovvero quelli nella lobby
			return true;
		}

		return false;
	}

	/**
	 * metodo usato per indicare la fine della partita
	 * @param idMatch indica l'identificativo della partita
	 */
	public void endMatch(long idMatch){
		GameQuery RdFDB = new GameQuery();
		if(RdFDB.connect()){
			try{
				RdFDB.end_match(idMatch);
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			System.out.println("problem of connection!");
		}
	}

	/**
	 * metodo usato per ottenere il creatore di una partita
	 * @param idMatch indica l'identificativo della partita
	 * @return rappresenta l'email del creatore
	 */
	public String getCreator(long idMatch){
		String data = null;
		GameQuery RdFDB = new GameQuery();
		if(RdFDB.connect()){
			try{
				data= RdFDB.get_creator(idMatch);//ottiene l'email del creatore della partita
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			System.out.println("problem of connection!");
		}
		return data;
	}

	/**
	 * metodo usato per notificare il cambio di manche
	 * @param email indica l'email di chi ha vinto la manche
	 * @param idMatch indica l'identificativo della partita
	 * @param score indica il punteggio del vincitore della manche
	 */
	public void updateManche(String email,long idMatch,int score){
		GameQuery RdFDB = new GameQuery();
		if(RdFDB.connect()){
			try{
				RdFDB.update_manche(idMatch,email,score);//aggiorna il numero della manche
			}catch(Exception e){
				e.printStackTrace();
			}
			if(RdFDB.connect()){
				notifyManche(idMatch,email,score);		
			}
		}else{	
			System.out.println("problem of connection!");
		}
	}
	

	/**
	 * metodo usato per notifica il cambio di turno
	 * @param nTurn indica il numero del turno 
	 * @param idGame indica l'identificativo della partita
	 * @param email indica l'email di chi ha il turno
	 * @param nmanche indica il numero della manche 
	 */
	public void c_turn(int nTurn, long idGame,String email,int nmanche){
		String data=null;
		GameQuery RdFDB = new GameQuery();
		if(RdFDB.connect()){
			try{
				data=RdFDB.c_Turn(nTurn,idGame,email,nmanche);//cambia il turno decidendo chi è il prossimo a giocare
			}catch(Exception e){
				e.printStackTrace();
			}
			notifyCTurn(data);//notifica il cambiamento
		}else{
			System.out.println("problem of connection!");
		}
	}
	
	
	 /**
     * metodo usato per notificare che un giocatore è uscito dalla lobby
     * @param email indica l'email di chi è uscito dalla lobby
     * @param idMatch indica l'identificativo della partita
     * @return indica se l'uscita è andata a buon fine
     */

	public int exitLobby(String email,long idMatch){
		int update=0;
		GameQuery RdFDB = new GameQuery();
		if(RdFDB.connect()){
			try{
				update=RdFDB.exit_lobby(idMatch,email);//
			}catch(Exception e){
				e.printStackTrace();
			}
			notifyExitLobby(idMatch,email);
		}else{
			System.out.println("problem of connection!");
		}

		return update;
	}

	 /**
     * metodo usato per notificare che un osservatore è uscito dalla lobby
     * @param email indica l'email di chi è uscito dalla lobby
     * @param idMatch indica l'identificativo della partita
     * @return indica se l'uscita è andata a buon fine
     */

	public int exitLobbyObs(String email,long idMatch){
		int update=0;
		GameQuery RdFDB = new GameQuery();
		if(RdFDB.connect()){
			try{
				update=RdFDB.exit_lobby(idMatch,email);//
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			System.out.println("problem of connection!");
		}

		return update;
	}

	
	 /**
     * metodo usato per notificare che un osservatore è uscito dalla partita
     * @param email indica l'email dell'osservatore che ha smesso di osservare
     * @param idMatch indica l'identificativo della partita
     * @return indica se l'uscita è andata a buon fine
     */
	
	public int exitObserver(String email,long idMatch){
		int update=0;
		GameQuery RdFDB = new GameQuery();
		if(RdFDB.connect()){
			try{
				update=RdFDB.exit_lobbyObserver(idMatch,email);//
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			System.out.println("problem of connection!");
		}
		return update;

	}

	 /**
     * metodo usato per notificare che un osservatore è entrato nella lobby
     * @param email indica l'email di chi è entr dalla lobby
     * @param idMatch indica l'identificativo della partita
     * @return indica se l'uscita è andata a buon fine
     */
	
	public boolean joinObserver(String email,long idMatch){
		GameQuery RdFDB = new GameQuery();
		if(RdFDB.connect()){
			try{
				RdFDB.join_observer(email,idMatch);//aggiunge una partecipazione nella tabella 'partecipazione' alla specifica partita da parte di un osservatore
			}catch(Exception e){
				e.printStackTrace();
			}
			return true;
		}else{
			System.out.println("problem of connection!");
			return false;
		}
	}

	 /**
     * metodo usato per ottenere il giocatore che detiene il primato di punteggio raggiunto per manche
     * @return indica il risultato della query
     */
	public QueryReturn statistics1(){

		QueryReturn result=null;	
		StatisticsQuery RdFDB = new StatisticsQuery();

		if(RdFDB.connect()){
			try{
				result=RdFDB.statistics1();
			}catch(Exception e){
				e.printStackTrace();
			}
			return result;
		}else{
			System.out.println("problem of connection!");
			return null;
		}

	}

	/**
     * metodo usato per ottenere il giocatore che detiene il primato di punteggio raggiunto per partita
     * @return indica il risultato della query
     */
	
	public QueryReturn statistics2(){

		QueryReturn result=null;	
		StatisticsQuery RdFDB = new StatisticsQuery();

		if(RdFDB.connect()){
			try{
				result=RdFDB.statistics2();
			}catch(Exception e){
				e.printStackTrace();
			}
			return result;
		}else{
			System.out.println("problem of connection!");
			return null;
		}

	}
	
	/**
     * metodo usato per ottenere il giocatore che ha giocato più manche in assoluto
     * @return indica il risultato della query
     */

	public QueryReturn statistics3(){

		QueryReturn result=null;	
		StatisticsQuery RdFDB = new StatisticsQuery();

		if(RdFDB.connect()){
			try{
				result=RdFDB.statistics3();
			}catch(Exception e){
				e.printStackTrace();
			}
			return result;
		}else{
			System.out.println("problem of connection!");
			return null;
		}

	}
	
	/**
     * metodo usato per ottenere il giocatore con la media più alta di punti acquisiti per manche
     * @return indica il risultato della query
     */

	public QueryReturn statistics4(){

		QueryReturn result=null;	
		StatisticsQuery RdFDB = new StatisticsQuery();

		if(RdFDB.connect()){
			try{
				result=RdFDB.statistics4();
			}catch(Exception e){
				e.printStackTrace();
			}
			return result;
		}else{
			System.out.println("problem of connection!");
			return null;
		}

	}

	/**
     * metodo usato per ottenere il giocatore che ha dovuto cedere più volte il turno di gioco in seguito ad errori
     * @return indica il risultato della query
     */
	
	public QueryReturn statistics5(){

		QueryReturn result=null;	
		StatisticsQuery RdFDB = new StatisticsQuery();

		if(RdFDB.connect()){
			try{
				result=RdFDB.statistics5();
			}catch(Exception e){
				e.printStackTrace();
			}
			return result;
		}else{
			System.out.println("problem of connection!");
			return null;
		}

	}

	/**
     * metodo usato per ottenere il giocatore che ha perso tutto in seguito ad un giro di ruota per il maggior numero di volte
     * @return indica il risultato della query
     */
	
	public QueryReturn statistics6(){

		QueryReturn result=null;	
		StatisticsQuery RdFDB = new StatisticsQuery();

		if(RdFDB.connect()){
			try{
				result=RdFDB.statistics6();
			}catch(Exception e){
				e.printStackTrace();
			}
			return result;
		}else{
			System.out.println("problem of connection!");
			return null;
		}

	}
	
	/**
     * metodo usato per ottenere il numero medio mosse per manche con le quali viene indovinata una frase
     * @return indica il risultato della query
     */

	public float statistics7(){

		float result=0;	
		StatisticsQuery RdFDB = new StatisticsQuery();

		if(RdFDB.connect()){
			try{
				result=RdFDB.statistics7();
			}catch(Exception e){
				e.printStackTrace();
			}
			return result;
		}else{
			System.out.println("problem of connection!");
			return 0;
		}

	}

	/**
     * metodo usato per mostrare la chiamata di consonante, riferita ad una manche già giocata, 
     * che ha portato all’acquisizione della maggior quantità punti, la frase misteriosa associata, 
     * e l’utente che ha fatto la mossa.
     * @return indica il risultato della query
     */
	
	public QueryReturn statistics8(){

		QueryReturn result=null;	
		StatisticsQuery RdFDB = new StatisticsQuery();

		if(RdFDB.connect()){
			try{
				result=RdFDB.statistics8();
			}catch(Exception e){
				e.printStackTrace();
			}
			return result;
		}else{
			System.out.println("problem of connection!");
			return null;
		}

	}

	//RELATIVE A UTENTE
	
	/**
     * metodo usato per mostrare il numero di manche giocate
     * @param email indica l'email dell'utente
     * @return indica il risultato della query
     */

	public int statistics9(String email){

		int result=0;	
		StatisticsQuery RdFDB = new StatisticsQuery();

		if(RdFDB.connect()){
			try{
				result=RdFDB.statistics9(email);
			}catch(Exception e){
				e.printStackTrace();
			}
			return result;
		}else{
			System.out.println("problem of connection!");
			return 0;
		}

	}

	/**
     * metodo usato per mostrare il numero di partite giocate
     * @param email indica l'email dell'utente
     * @return indica il risultato della query
     */
	
	public int statistics10(String email){

		int result=0;	
		StatisticsQuery RdFDB = new StatisticsQuery();

		if(RdFDB.connect()){
			try{
				result=RdFDB.statistics10(email);
			}catch(Exception e){
				e.printStackTrace();
			}
			return result;
		}else{
			System.out.println("problem of connection!");
			return 0;
		}

	}

	/**
     * metodo usato per mostrare il numero di manche osservate
     * @param email indica l'email dell'utente
     * @return indica il risultato della query
     */
	
	public int statistics11(String email){

		int result=0;	
		StatisticsQuery RdFDB = new StatisticsQuery();

		if(RdFDB.connect()){
			try{
				result=RdFDB.statistics11(email);
			}catch(Exception e){
				e.printStackTrace();
			}
			return result;
		}else{
			System.out.println("problem of connection!");
			return 0;
		}

	}
	
	/**
     * metodo usato per mostrare il numero di partite osservate
     * @param email indica l'email dell'utente
     * @return indica il risultato della query
     */

	public int statistics12(String email){

		int result=0;	
		StatisticsQuery RdFDB = new StatisticsQuery();

		if(RdFDB.connect()){
			try{
				result=RdFDB.statistics12(email);
			}catch(Exception e){
				e.printStackTrace();
			}
			return result;
		}else{
			System.out.println("problem of connection!");
			return 0;
		}

	}

	/**
     * metodo usato per mostrare il numero di manche vinte
     * @param email indica l'email dell'utente
     * @return indica il risultato della query
     */
	
	public int statistics13(String email){

		int result=0;	
		StatisticsQuery RdFDB = new StatisticsQuery();

		if(RdFDB.connect()){
			try{
				result=RdFDB.statistics13(email);
			}catch(Exception e){
				e.printStackTrace();
			}
			return result;
		}else{
			System.out.println("problem of connection!");
			return 0;
		}

	}

	
	/**
     * metodo usato per mostrare il numero di partite vinte
     * @param email indica l'email dell'utente
     * @return indica il risultato della query
     */
	
	public int statistics14(String email){

		int result=0;	
		StatisticsQuery RdFDB = new StatisticsQuery();

		if(RdFDB.connect()){
			try{
				result=RdFDB.statistics14(email);
			}catch(Exception e){
				e.printStackTrace();
			}
			return result;
		}else{
			System.out.println("problem of connection!");
			return 0;
		}

	}

	/**
     * metodo usato per mostrare il numero di manche giocate
     * @param email indica l'email dell'utente
     * @return indica il risultato della query
     */
	
	public int statistics15(String email){

		int result=0;	
		StatisticsQuery RdFDB = new StatisticsQuery();

		if(RdFDB.connect()){
			try{
				result=RdFDB.statistics15(email);
			}catch(Exception e){
				e.printStackTrace();
			}
			return result;
		}else{
			System.out.println("problem of connection!");
			return 0;
		}

	}

	 /**
     * metodo usato per mostrare il numero medio di passaggio turno per manche
     * @param email indica l'email dell'utente
     */

	public int statistics16(String email){

		int result=0;	
		StatisticsQuery RdFDB = new StatisticsQuery();

		if(RdFDB.connect()){
			try{
				result=RdFDB.statistics16(email);
			}catch(Exception e){
				e.printStackTrace();
			}
			return result;
		}else{
			System.out.println("problem of connection!");
			return 0;
		}

	}

	/**
     * metodo usato per mostrare il numero medio di volte, per manche e partita, che ha dovuto cedere il turno di gioco
     * @param email indica l'email dell'utente
     * @return indica il risultato della query
     */
	
	public int statistics17(String email){

		int result=0;	
		StatisticsQuery RdFDB = new StatisticsQuery();

		if(RdFDB.connect()){
			try{
				result=RdFDB.statistics17(email);
			}catch(Exception e){
				e.printStackTrace();
			}
			return result;
		}else{
			System.out.println("problem of connection!");
			return 0;
		}

	}

	/**
     * metodo usato per mostrare il numero medio di volte, per manche che perso tutto
     * @param email indica l'email dell'utente
     * @return indica il risultato della query
     */
	public int statistics18(String email){

		int result=0;	
		StatisticsQuery RdFDB = new StatisticsQuery();

		if(RdFDB.connect()){
			try{
				result=RdFDB.statistics18(email);
			}catch(Exception e){
				e.printStackTrace();
			}
			return result;
		}else{
			System.out.println("problem of connection!");
			return 0;
		}

	}

	/**
     * metodo usato per mostrare il numero medio di volte, per partita che perso tutto
     * @param email indica l'email dell'utente
     * @return indica il risultato della query
     */
	
	public int statistics19(String email){

		int result=0;	
		StatisticsQuery RdFDB = new StatisticsQuery();

		if(RdFDB.connect()){
			try{
				result=RdFDB.statistics19(email);
			}catch(Exception e){
				e.printStackTrace();
			}
			return result;
		}else{
			System.out.println("problem of connection!");
			return 0;
		}

	}

	/**
     * metodo usato per ottenere tutte le frasi
     * @return indica la lista di tutte le frasi
     */
	
	public Hashtable<Integer,Phrases> getPhrases(){
		Hashtable<Integer,Phrases> data=new Hashtable<Integer,Phrases>();
		PhraseQuery RdFDB = new PhraseQuery();
		if(RdFDB.connect()){
			try{
				data= RdFDB.get_phrases();//ottiene le frasi 
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			System.out.println("problem of connection!");
		}
		return data;
	}

	/**
     * metodo usato per aggiungere delle frasi
     * @param phrases indica le frasi da aggiungere
     * @return indica se l'aggiunta è andata a buon fine
     */
	public int addPhrases(Object[][] phrases){
		int result=0; 
		PhraseQuery RdFDB = new PhraseQuery();
		if(RdFDB.connect()){
			try{
				result=RdFDB.add_Phrases(phrases);
			}catch(Exception e){
				e.printStackTrace();
			}
			return result;
		}else{
			System.out.println("problem of connection!");
			return 0;
		}
	}
	
	/**
     * metodo usato per rimuovere una frase
     * @param id indica l'id della frase da rimuovere
     * @return indica se l'eliminazione è andata a buon fine
     */
	public int removePhrase(int id){
		int update=0;
		PhraseQuery RdFDB = new PhraseQuery();
		if(RdFDB.connect()){
			try{
				update=RdFDB.remove_Phrase(id);//
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			System.out.println("problem of connection!");
		}
		return update;
	}
	
	/**
     * metodo usato per modificare una frase
     * @param id indica l'id della frase da modificare
     * @param tema indica il tema della frase da modificare
     * @param corpo indica il corpo della frase da modificare
     * @return indica se la modifica è andata a buon fine
     */
	public int updatePhrase(int id,String tema,String corpo){
		int update=0;
		PhraseQuery RdFDB = new PhraseQuery();
		if(RdFDB.connect()){
			try{
				update=RdFDB.update_Phrase(id,tema,corpo);//
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			System.out.println("problem of connection!");
		}
		return update;
	}
	
	/**
     * metodo usato per inserire una frase
     * @param id indica l'identificativo della frase
     * @param tema indica il tema della frase da modificare
     * @param corpo indica il corpo della frase da modificare
     * @return indica se la modifica è andata a buon fine
     */
	
	public int insertPhrase(int id,String tema,String corpo){
		int update=0;
		PhraseQuery RdFDB = new PhraseQuery();
		if(RdFDB.connect()){
			try{
				update=RdFDB.insert_Phrase(id,tema,corpo);//
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			System.out.println("problem of connection!");
		}
		return update;
	}
	
	/**
     * metodo usato per aggiornare lo stato di un utente (online/offline)
     * @param email indica l'email dell'utente
     * @param online indica lo stato del giocatore
     * @return indica se la modifica è andata a buon fine
     */
	
	public boolean setOnline(String email, boolean online) {
	    boolean returned=false;
	    LoginQuery RdFDB = new LoginQuery();
	    if(RdFDB.connect()){
	      try{
	        returned = RdFDB.online_update(email,online);//aggiorna il campo online a true/false
	      }catch(Exception e){
	        e.printStackTrace();
	      }
	    }else{
	      System.out.println("problem of connection!");
	    }
	    return returned;
	  }

	/**
     * metodo usato per controllare se la password passata per l'aggiornamento della steasa corrisponde a quella nel database
     * @param email indica l'email dell'utente
     * @param passw indica la password
     * @return indica se la modifica è andata a buon fine
     */
	
	public boolean isPasswordCorrect(String email, String passw) {
	    boolean returned=false;
	    ProfileUpdateQuery RdFDB = new ProfileUpdateQuery();
	    if(RdFDB.connect()){
	      try{
	        returned=RdFDB.code_control(email,passw);
	      }catch(Exception e){
	        e.printStackTrace();
	      }
	    }else{
	      System.out.println("problem of connection!");
	    }
	    return returned;
	  }
	
}

