package database;
import game.MailSender;
import game.Md5Hash;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.xml.bind.DatatypeConverter;

/**
 * la classe RegistrationQuery contiene i metodi usati per effettuare le query di registrazione del profilo 
 * @author Andrea Peluso
 * @version 1.0
 */
public class RegistrationQuery extends DBimplementation{
	
	/**
	 * metodo usato per aggiungere un nuovo utente
	 * @param email indica l'email dell'utente
	 * @param name indica il nome dell'utente
	 * @param surname indica il cognome dell'utente
	 * @param nickname indica il nickname dell'utente
	 * @param isAdmin indica se l'utente è o no un amministatore
	 * @return indica se l'utente è stato aggiunto correttamente
	 * @throws UnsupportedEncodingException per la gestione delle eccezioni
	 * @throws NoSuchAlgorithmException per la gestione delle eccezioni
	 * @throws SendFailedException per la gestione delle eccezioni
	 * @throws MessagingException per la gestione delle eccezioni
	 */
	public boolean insert_newuser(String email, String name ,String surname, String nickname,boolean isAdmin) throws UnsupportedEncodingException, NoSuchAlgorithmException, SendFailedException, MessagingException{

		boolean returned = false;
		LocalDateTime now = LocalDateTime.now();//ottengo la data di adesso
		byte[] array = new byte[7]; 
		new Random().nextBytes(array);
		String generatedString = new String(array, Charset.forName("UTF-8"));
		String passw=email+now+generatedString;//concateno la data a una stringa casuale
		long time=0;
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(passw.getBytes());//cripto la stringa in md5
		byte[] digest = md.digest();
		passw = DatatypeConverter.printHexBinary(digest).toUpperCase();
		passw = passw.substring(0,10);//prendo i primi 10 caratteri

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		time= timestamp.getTime();

		PreparedStatement find_user_stmt =null;
		PreparedStatement insert_user_stmt=null;
		PreparedStatement get_codetime_stmt=null;
		PreparedStatement update_code_stmt=null;
		ResultSet find_user_exc=null;
		ResultSet get_codetime_exc=null;


		try {
			//controllo se esiste un utente già registrato con questa email o nickname
			find_user_stmt = conn.prepareStatement("select * from utente where email=? or nickname=?");
			find_user_stmt.setString(1, email);
			find_user_stmt.setString(2, nickname);
			find_user_exc = find_user_stmt.executeQuery();
			//se non ci sono utenti registrati con questa email
			if(!find_user_exc.isBeforeFirst()){
				//inserisco i dati dell'utente nel database
				insert_user_stmt = conn.prepareStatement("INSERT INTO utente(email, nome, cognome, nickname, password, admin,oracodice,online)VALUES (?, ? , ?, ?, ?, ?, ?,?)");
				insert_user_stmt.setString(1, email);
				insert_user_stmt.setString(2, name);
				insert_user_stmt.setString(3, surname);
				insert_user_stmt.setString(4, nickname);
				insert_user_stmt.setString(5, passw);
				insert_user_stmt.setBoolean(6, isAdmin);
				insert_user_stmt.setLong(7, time);
				insert_user_stmt.setBoolean(8, false);
				update=insert_user_stmt.executeUpdate();
				if(update==1){//se aggiorno i dati nel database invio email contente il codice di attivazione
					new MailSender("ruotadellafortunamanagement@gmail.com", "Fortuna98", email, "Codice attivazione", "Benvenuto nel gioco La Ruota Della Fortuna! Ecco il suo codice di attivazione: "+ passw +" , le ricordiamo che il codice ha una validità di 10 minuti.");
					returned=true;			
				}			
			}  else { 
				//controllo che l'utente abbia effettivamente completato la registrazione altrimenti lo faccio registrare di nuovo

				get_codetime_stmt = conn.prepareStatement("select oracodice from utente where email=?");
				get_codetime_stmt.setString(1, email);
				get_codetime_exc = get_codetime_stmt.executeQuery();
				get_codetime_exc.next();
				long codetime=get_codetime_exc.getLong(1);
				if(codetime!=0){//se non ha confermato il codice di attivazione permetto di resgistrare di nuovo con la stessa email
					update_code_stmt = conn.prepareStatement("UPDATE utente SET nome=?, cognome=?,nickname=?,password=?,admin=?,oracodice=? where email=?");
					update_code_stmt.setString(1, name);
					update_code_stmt.setString(2, surname);
					update_code_stmt.setString(3, nickname);
					update_code_stmt.setString(4, passw);
					update_code_stmt.setBoolean(5, isAdmin);
					update_code_stmt.setLong(6, time);
					update_code_stmt.setString(7, email);
					update = update_code_stmt.executeUpdate();	
					if(update==1){
						new MailSender("ruotadellafortunamanagement@gmail.com", "Fortuna98", email, "Codice attivazione", "Benvenuto nel gioco La Ruota Della Fortuna! Ecco il suo codice di attivazione: "+ passw +" , le ricordiamo che il codice ha una validità di 10 minuti.");
						returned=true;
					}else{
						returned=false;
					}

				}else{

					returned=false;//utente già registrato correttamente
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(insert_user_stmt!=null){
				try {
					insert_user_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(find_user_exc!=null){
				try {
					find_user_exc.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(find_user_stmt!=null){
				try {
					find_user_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(get_codetime_exc!=null){
				try {
					get_codetime_exc.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(get_codetime_stmt!=null){
				try {
					get_codetime_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(update_code_stmt!=null){
				try {
					update_code_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			disconnect();
		}
		return returned;
	}
	
	/**
	 * metodo usato per la creazione dell'amministratore da parte del gestore del server in caso non sia presente sul database
	 * @param email indica l'email
	 * @param name indica il nome
	 * @param surname indica il cognome
	 * @param nickname indica il nickname
	 * @param passw indica la password
	 * @return indica se l'inserimento è andato a buon fine
	 */
	public boolean insert_admin(String email,String name,String surname,String nickname,String passw){

		boolean returned = false;

		PreparedStatement insert_admin_stmt=null;
		try {
			//inserisco i dati dell'amministratore nella tabella utente del database con il campo admin settato a true
			insert_admin_stmt = conn.prepareStatement("INSERT INTO public.utente(email, nome, cognome, nickname, password, admin,oracodice,online)VALUES (?, ? , ?, ?, ?, ?, ?,?)");
			insert_admin_stmt.setString(1, email);
			insert_admin_stmt.setString(2, name);
			insert_admin_stmt.setString(3, surname);
			insert_admin_stmt.setString(4, nickname);
			insert_admin_stmt.setString(5, Md5Hash.Hash(passw));
			insert_admin_stmt.setBoolean(6, true);
			insert_admin_stmt.setLong(7, 0);
			insert_admin_stmt.setBoolean(8, false);
			insert_admin_stmt.executeUpdate();
			returned=true;

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(insert_admin_stmt!=null){
				try {
					insert_admin_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			disconnect();
		}
		return returned;

	}

	/**
	 * metodo usato per verificare se il codice di registrazione corrisponde a quello inviato per email e non è scaduto
	 * @param name indica l'email dell'utente
	 * @param passw rappresenta il codice inviato per email
	 * @return indica se il codice è corretto e non è scaduto
	 */
	public boolean verify_code(String name,String passw){
		boolean returned=false;
		long time1=0;
		long time2=0;
		PreparedStatement verify_code_stmt=null;
		ResultSet verify_code_exc=null;
		PreparedStatement delete_user_stmt=null;
		try {
			//verifico se il codice inserito dall'utente corrisponde a quello nel database
			verify_code_stmt = conn.prepareStatement("select oracodice from utente where email=? AND password=?");
			verify_code_stmt.setString(1, name);
			verify_code_stmt.setString(2, passw);
			verify_code_exc = verify_code_stmt.executeQuery();

			if(verify_code_exc.isBeforeFirst()){//se corrisponde

				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				time1= timestamp.getTime();//ora attuale
				verify_code_exc.next();
				time2=verify_code_exc.getLong(1);//ora codice attivazione
				long milliseconds = time1 - time2; //calcolo tempo passato dall'invio (ora attuale meno ora codice di attivazione)
				int seconds = (int) milliseconds / 1000;
				int minutes = (seconds % 3600) / 60;//converto in minuti
				seconds = (seconds % 3600) % 60;
				if(minutes>=10)//verifico se non sono passati più di 10 minuti dall'invio del codice di attivazione
				{
					delete_user_stmt = conn.prepareStatement("DELETE FROM utente WHERE email=?");//se si elimino utente
					delete_user_stmt.setString(1, name);
					delete_user_stmt.executeUpdate();
					returned=false;//codice scaduto
				}else{
					returned=true;//codice corrisponde e non scaduto
				}
			}else{
				returned=false;//codice non corrisponde
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{

			if(delete_user_stmt!=null){
				try {
					delete_user_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}

			if(verify_code_exc!=null){
				try {
					verify_code_exc.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}

			if(verify_code_stmt!=null){
				try {
					verify_code_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}

			disconnect();
		}
		return returned;
	}

	
}
