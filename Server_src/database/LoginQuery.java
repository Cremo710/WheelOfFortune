package database;
import game.Md5Hash;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * la classe LoginQuery contiene i metodi usati per effettuate le query durante la fase di login
 * @author Andrea Peluso
 * @version 1.0
 */
public class LoginQuery extends DBimplementation{

	/**
	 * metodo usato per effettuare il login da parte di un utente non amministratore
	 * @param name indica l'email di chi effettua il login 
	 * @param passw indica la password di chi effettua il login 
	 * @return indica se il login è andato a buon fine
	 */
	public boolean user_login(String name,String passw){
	    boolean returned=false;
	    PreparedStatement user_login_stmt=null;
	    ResultSet user_login_exc=null;

	    try {
	      //verifico se esiste utente(non amministratore) con quella email e password nel database 
	      user_login_stmt = conn.prepareStatement("select * from utente where email=? AND password=? AND admin=false AND oracodice=0 AND online=false");
	      user_login_stmt.setString(1, name);
	      user_login_stmt.setString(2, Md5Hash.Hash(passw));
	      user_login_exc = user_login_stmt.executeQuery();
	      if(user_login_exc.isBeforeFirst()){//se esiste e i dati sono corretti allora ritorno true e setto il campo online a true
	        online_update(name,true);
	        returned=true;
	      }else{
	        returned=false;//altrimenti i dati sono errati e ritorno false
	      }


	    } catch (SQLException e) {
	      e.printStackTrace();
	    }finally{
	      if(user_login_exc!=null){
	        try {
	          user_login_exc.close();
	        } catch (SQLException e1) {
	          e1.printStackTrace();
	        }}
	      if(user_login_stmt!=null){
	        try {
	          user_login_stmt.close();
	        } catch (SQLException e) {
	          e.printStackTrace();
	        }}
	      disconnect();//disconnetto 

	    }
	    return returned;
	  }
	
	/**
	 * metodo usato per effettuare il login da parte di un utente amministratore
	 * @param name indica l'email di chi effettua il login 
	 * @param passw indica la password di chi effettua il login 
	 * @return indica se il login è andato a buon fine
	 */
	public boolean admin_login(String name,String passw){
		boolean returned=false;
		ResultSet user_login_exc=null;
		PreparedStatement user_login_stmt=null;
		PreparedStatement set_online_stmt=null;

		try {
			//verifico se esiste amministratore(non utente) con quella email e password nel database 
			user_login_stmt = conn.prepareStatement("select * from utente where email=? AND password=? AND admin=true AND oracodice=0 AND online=false");
			user_login_stmt.setString(1, name);
			user_login_stmt.setString(2, Md5Hash.Hash(passw));
			user_login_exc = user_login_stmt.executeQuery();
			if(user_login_exc.isBeforeFirst()){
				set_online_stmt = conn.prepareStatement("UPDATE utente set online=true where email=?");
				set_online_stmt.setString(1, name);
				set_online_stmt.executeUpdate();
				returned=true;
			}else{
				returned=false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(user_login_exc!=null){
				try {
					user_login_exc.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(user_login_stmt!=null){
				try {
					user_login_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(set_online_stmt!=null){
				try {
					set_online_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			disconnect();

		}
		return returned;
	}
	
	/**
     * metodo usato per aggiornare lo stato di un utente (online/offline)
     * @param name indica l'email dell'utente
     * @param on indica lo stato del giocatore
     * @return indica se la modifica è andata a buon fine
     */
	public boolean online_update(String name, boolean on) {
	    boolean returned=false;
	    PreparedStatement set_online_stmt=null;
	    try {
	      set_online_stmt = conn.prepareStatement("UPDATE utente set online=? where email=?");
	      set_online_stmt.setBoolean(1, on);
	      set_online_stmt.setString(2, name);
	      set_online_stmt.executeUpdate();
	      returned=true;
	    } catch (SQLException e) {
	      e.printStackTrace();
	    } finally{
	      if(set_online_stmt!=null){
	        try {
	          set_online_stmt.close();
	        } catch (SQLException e) {
	          e.printStackTrace();
	        }}
	      disconnect();//disconnetto 
	    }
	    return returned;
	  }

	
}
