package database;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

/**
 * la classe DBimplementation contiene le query generali che effettua il server al database
 * @author Andrea Peluso
 * @version 1.0
 */
public class DBimplementation extends DBadapter {

	public int update=0,update2=0;

	/**
	 * metodo usato per verificare se esiste un amministratore all'avvio del server
	 * @return indica se esiste o meno un amministratore
	 */
	public boolean AdminExist(){

		boolean returned=false;
		PreparedStatement admin_exist_stmt = null;
		ResultSet admin_exist_exc = null;
		try {
			admin_exist_stmt = conn.prepareStatement("select * from utente where admin=true");//cerco se esiste un amministratore nel database
			admin_exist_exc = admin_exist_stmt.executeQuery();//eseguo la query
			if(admin_exist_exc.isBeforeFirst()){//se il risultato non è vuoto ovvero il cursore è prima di qualcosa
				returned=true;//allora esiste il profilo amministratore
			}else{
				returned=false;//altrimenti no
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(admin_exist_exc!=null){
				try {
					admin_exist_exc.close();//chiudo il ResultSet
				} catch (SQLException e) {
					e.printStackTrace();
				}}

			if(admin_exist_stmt!=null){
				try {
					admin_exist_stmt.close();//chiudo il PreparedStatement
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			disconnect();//mi disconnetto
		}
		return returned;
	}

	/**
	 * metodo usato per inserire le frasi contenute in un file csv
	 * @return indica se le frasi sono state caricate nel database con successo
	 * @throws FileNotFoundException per la gestione delle eccezioni
	 * @throws IOException per la gestione delle eccezioni
	 */
	
	public boolean insert_phrases() throws FileNotFoundException, IOException{

		boolean returned = false;

		try (Connection conn = DriverManager.getConnection(jdbUrl, username, password)) {
			long rowsInserted = new CopyManager((BaseConnection) conn)
			.copyIn(
					"COPY frase(tema,corpo) FROM STDIN (FORMAT csv, HEADER,ENCODING 'UTF-8')", 
					new BufferedReader(new InputStreamReader(new FileInputStream("phrases/frasi.csv"),"UTF-8"))
					);
			update2=(int) rowsInserted;
		}catch (SQLException e) {
			e.printStackTrace();
		}

		if(update2>=0){
			returned = true;
		}

		return returned;

	}
	


}


