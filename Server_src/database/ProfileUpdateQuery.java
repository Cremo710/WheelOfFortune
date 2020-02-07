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
import java.time.LocalDateTime;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.xml.bind.DatatypeConverter;

/**
 * la classe ProfileUpdateQuery contiene i metodi usati per effettuare le query di aggiornamento del profilo 
 * @author Andrea Peluso
 * @version 1.0
 */
public class ProfileUpdateQuery extends DBimplementation{
	
	/**
	 * metodo usato per aggiornare il profilo di un utente
	 * @param email indica l'email dell'utente
	 * @param name indica il nome dell'utente
	 * @param surname indica il cognome dell'utente
	 * @param nickname indica il nickname dell'utente
	 * @return indica se l'aggiornamento è anadato a buon fine
	 */
	public int profile_update(String email,String name,String surname,String nickname){

		PreparedStatement update_profile_stmt=null;

		try {
			//aggiorno i dati del profilo dell'utente
			update_profile_stmt = conn.prepareStatement("UPDATE utente SET nome=? , cognome=? , nickname=? where email=?");
			update_profile_stmt.setString(1, name);
			update_profile_stmt.setString(2, surname);
			update_profile_stmt.setString(3, nickname);
			update_profile_stmt.setString(4, email);
			update = update_profile_stmt.executeUpdate();
			if(update==1){
				//invio email comunicando che i dati sono stati aggiornati
				new MailSender("ruotadellafortunamanagement@gmail.com", "Fortuna98", email, "Profilo aggiornato", "Salve " +name+", Le comunichiamo che il suo profilo è stato aggiornato! ");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(update_profile_stmt!=null){
				try {
					update_profile_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}

			disconnect();
		}
		return update;
	}   

	/**
	 * metodo usato per resettare la password in caso di password dimenticata
	 * @param email indica l'utente che ha chiesto il reset
	 * @return indica se il reset è andato a buon fine
	 * @throws UnsupportedEncodingException per la gestione delle eccezioni
	 * @throws NoSuchAlgorithmException per la gestione delle eccezioni
	 * @throws SendFailedException per la gestione delle eccezioni
	 * @throws MessagingException per la gestione delle eccezioni
	 */
	public boolean reset_Password(String email) throws UnsupportedEncodingException, NoSuchAlgorithmException, SendFailedException, MessagingException{

		LocalDateTime now = LocalDateTime.now();
		byte[] array = new byte[7]; // length is bounded by 7
		new Random().nextBytes(array);
		String generatedString = new String(array, Charset.forName("UTF-8"));
		String passw=email+now+generatedString;
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(passw.getBytes());
		byte[] digest = md.digest();
		passw = DatatypeConverter.printHexBinary(digest).toUpperCase();
		passw = passw.substring(0,10);
		boolean returned=false;

		PreparedStatement reset_password=null;

		try {
			//aggiorno password inviando una nuova passowrd generata casualmente
			reset_password = conn.prepareStatement("UPDATE utente SET password = ? where email=?");
			reset_password.setString(1, Md5Hash.Hash(passw));
			reset_password.setString(2, email);
			update = reset_password.executeUpdate();
			if(update==1){
				//invio nuova password per email
				new MailSender("ruotadellafortunamanagement@gmail.com", "Fortuna98", email, "Nuova Password", "Gentile utente, questa è la sua nuova passowrd: "+ passw );
				returned=true;
			}else{
				returned=false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(reset_password!=null){
				try {
					reset_password.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}

			disconnect();
		}
		return returned;

	}
	
	/**
	 * metodo usato per aggiornare la password 
	 * @param name indica l'email associata alla password
	 * @param passw indica la password nuova
	 * @param code indica la password vecchia
	 * @return indica se l'aggiornamento della password è andato a buon fine
	 */
	public boolean code_update(String name,String passw,String code){
	    boolean returned=false;
	    PreparedStatement update_password_stmt=null;

	    try {
	      //se il codice corrisponde e non è scaduto allora aggiorno la password
	      update_password_stmt = conn.prepareStatement("UPDATE utente SET password = ? , oracodice=0 where email=? AND password=?");
	      update_password_stmt.setString(1, Md5Hash.Hash(passw));
	      update_password_stmt.setString(2, name);
	      update_password_stmt.setString(3, code);
	      update = update_password_stmt.executeUpdate();  
	      if(update==1){
	        //invio email comunicando che la password è stata aggiornata
	        new MailSender("ruotadellafortunamanagement@gmail.com", "Fortuna98", name, "Password aggiornata", "Salve, Le comunichiamo che la sua password è stata aggiornata! ");
	        returned=true;
	      }else{
	        returned=false;
	      }
	    } catch (SQLException e) {
	      e.printStackTrace();
	    }finally{
	      if(update_password_stmt!=null){
	        try {
	          update_password_stmt.close();
	        } catch (SQLException e) {
	          e.printStackTrace();
	        }}

	      disconnect();
	    }

	    return returned;
	  }
	
	/**
	 * metodo usato per impostare la password
	 * @param name indica l'email associata alla password
	 * @param passw indica la password nuova
	 * @param code indica la password vecchia
	 * @return indica se l'aggiornamento della password è andato a buon fine
	 */
	public boolean password_update(String name,String passw,String code){
	    boolean returned=false;
	    PreparedStatement update_password_stmt=null;

	    try {
	      //se il codice corrisponde e non è scaduto allora aggiorno la password
	      update_password_stmt = conn.prepareStatement("UPDATE utente SET password = ? , oracodice=0 where email=? AND password=?");
	      update_password_stmt.setString(1, Md5Hash.Hash(passw));
	      update_password_stmt.setString(2, name);
	      update_password_stmt.setString(3, Md5Hash.Hash(code));
	      update = update_password_stmt.executeUpdate();  
	      if(update==1){
	        //invio email comunicando che la password è stata aggiornata
	        new MailSender("ruotadellafortunamanagement@gmail.com", "Fortuna98", name, "Password aggiornata", "Salve, Le comunichiamo che la sua password è stata aggiornata! ");
	        returned=true;
	      }else{
	        returned=false;
	      }
	    } catch (SQLException e) {
	      e.printStackTrace();
	    }finally{
	      if(update_password_stmt!=null){
	        try {
	          update_password_stmt.close();
	        } catch (SQLException e) {
	          e.printStackTrace();
	        }}

	      disconnect();
	    }

	    return returned;
	  }
	
	/**
     * metodo usato per controllare se la password passata per l'aggiornamento della stessa corrisponde a quella nel database
     * @param name indica l'email dell'utente
     * @param passw indica la password
     * @return indica se la modifica è andata a buon fine
     */
	
	public boolean code_control(String name, String passw) {
	    boolean returned=true;
	    PreparedStatement update_password_stmt=null;
	    ResultSet code_control_exc = null;
	    try {
	      //Verifica se il codice corrisponde a quello contenuto nel Database 
	      update_password_stmt = conn.prepareStatement("SELECT *  FROM utente WHERE email=? AND password=?");
	      update_password_stmt.setString(1, name);
	      update_password_stmt.setString(2, Md5Hash.Hash(passw));
	      code_control_exc = update_password_stmt.executeQuery();  
	      if(code_control_exc.isBeforeFirst()){
	        returned=true;
	      }else{
	        returned=false;
	      }
	    } catch (SQLException e) {
	      e.printStackTrace();
	    }finally{
	      if(code_control_exc!=null){
	        try {
	          code_control_exc.close();
	        } catch (SQLException e) {
	          e.printStackTrace();
	        }}
	      if(update_password_stmt!=null){
	        try {
	          update_password_stmt.close();
	        } catch (SQLException e) {
	          e.printStackTrace();
	        }}

	      disconnect();
	    }

	    return returned;
	  }

}
