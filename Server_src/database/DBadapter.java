package database;
import java.sql.*;

/**
 * la classe DBadapter serve per interfacciare il server con il database
 * @author Andrea Peluso
 * @version 1.0
 */
public class DBadapter {
		
	public static String jdbUrl = null;
	public static String username = null;
	public static String password = null;

	Connection conn = null;
	
	/**
	 * costruttore della classe
	 */
	public DBadapter(){

	}
	/**
	 * metodo usato per la connessione dal database
	 * @return indica se la connessione è riuscita
	 */
	public boolean connect (){
		try {
			conn = DriverManager.getConnection(jdbUrl , username, password);
			return true;
		} catch (SQLException e) {
			return false;
			
		}
	}
	
	/**
	 * metodo usato per la disconnessione dal database
	 */
	public void disconnect(){
		try {
			if (conn != null){
				conn.close();
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}

