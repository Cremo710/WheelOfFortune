package database;
import game.MailSender;
import game.Phrases;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * la classe PhraseQuery contiene i metodi usati per effettuate le query per la gestione delle frasi
 * @author Andrea Peluso
 * @version 1.0
 */
public class PhraseQuery extends DBimplementation{

	/**
	 * metodo usato per aggiungere delle frasi
	 * @param phrases indica le frasi da aggiungere
	 * @return indica se l'aggiunta è andata a buon fine
	 */
	public int add_Phrases(Object[][] phrases) {
		//controllo sui dati
		if(phrases[0].length!=3 || hasEmptyValues(phrases)){
			return 0;
		}

		int returned=0; 
		PreparedStatement updatePhrase_stmt = null;

		try {
			for(int i=0;i<phrases.length;i++){//inserisco le nuove frasi nel database
				updatePhrase_stmt = conn.prepareStatement("INSERT INTO frase(tema,corpo)VALUES (? , ?)");
				updatePhrase_stmt.setString(1, (String)phrases[i][1]);
				updatePhrase_stmt.setString(2, (String)phrases[i][2]);
				returned = updatePhrase_stmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(updatePhrase_stmt!=null){
				try {
					updatePhrase_stmt.close();//chiudo lo Statement
				} catch (SQLException e) {
					e.printStackTrace();
				}}

			disconnect();//disconnetto
		}
		return returned;
	}

	/**
	 * metodo usato per rimuovere una frase
	 * @param id indica l'id della frase da rimuovere
	 * @return indica se l'eliminazione è andata a buon fine
	 */
	public int remove_Phrase(int id){
		PreparedStatement remove_phrase_stmt =null;
		try {
			//elimino la frase in base all'id
			remove_phrase_stmt = conn.prepareStatement("DELETE FROM frase where idfrase=?");
			remove_phrase_stmt.setLong(1, id);
			update = remove_phrase_stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(remove_phrase_stmt!=null){
				try {
					remove_phrase_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			disconnect();
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
	public int update_Phrase(int id,String tema,String corpo){
		PreparedStatement update_phrase_stmt =null;
		try {
			//aggiorno il tema e il corpo della frase in base all'id
			update_phrase_stmt = conn.prepareStatement("UPDATE frase SET tema=?, corpo=? where idfrase=?");
			update_phrase_stmt.setString(1, tema);
			update_phrase_stmt.setString(2, corpo);
			update_phrase_stmt.setLong(3, id);
			update = update_phrase_stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(update_phrase_stmt!=null){
				try {
					update_phrase_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			disconnect();
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
	public int insert_Phrase(int id, String tema,String corpo){
	    PreparedStatement insert_phrase_stmt =null;
	    try {
	      //inserisco nuova frase
	      insert_phrase_stmt = conn.prepareStatement("INSERT INTO frase(idfrase,tema,corpo) VALUES (?,?,?)");
	      insert_phrase_stmt.setInt(1, id);
	      insert_phrase_stmt.setString(2, tema);
	      insert_phrase_stmt.setString(3, corpo);
	      update = insert_phrase_stmt.executeUpdate();
	    } catch (SQLException e) {
	      e.printStackTrace();
	    }finally{
	      if(insert_phrase_stmt!=null){
	        try {
	          insert_phrase_stmt.close();
	        } catch (SQLException e) {
	          e.printStackTrace();
	        }}
	      disconnect();
	    }
	    return update;
	  }

	/**
	 * metodo usato per verificare se esistono almeno 5 frasi da utlizzare per la partita che non siano mai state usate dai giocatori
	 * o osservate , altrimenti notifica gli amministratori di aggiungere nuove frasi
	 * @param idMatch indica l'identificativo della partita
	 * @return indica se esistono almeno 5 frasi da usare per la partita
	 */
	public boolean checkPhrases(long idMatch){

		boolean returned=false;

		int nphrases=0;
		ArrayList<String> players =new ArrayList<String>();
		ArrayList<String> admin =new ArrayList<String>();
		PreparedStatement player_list_stmt=null;
		ResultSet player_list_exc=null;
		PreparedStatement randomphrases_stmt=null;
		ResultSet randomphrases_exc=null;
		PreparedStatement admin_list_stmt=null;
		ResultSet admin_list_exc=null;
		PreparedStatement delete_partecipation_stmt=null;
		PreparedStatement delete_match_stmt=null;

		try{
			//seleziono i partecipanti(giocatori) alla partita
			player_list_stmt = conn.prepareStatement("select email from partecipazione where idpartita=? and tipo=0");
			player_list_stmt.setLong(1, idMatch);
			player_list_exc = player_list_stmt.executeQuery();
			if(player_list_exc.isBeforeFirst()){
				while(player_list_exc.next()){
					players.add(player_list_exc.getString(1));
				}
			}
			//controllo che ci siano almeno 5 frasi che i giocatori non hanno mai visto
			randomphrases_stmt = conn.prepareStatement("SELECT count(*) as total FROM frase WHERE idfrase not in(select distinct idfrase from manche natural join partita inner join partecipazione on partita.idpartita=partecipazione.idpartita where (partecipazione.email=? OR partecipazione.email=? OR partecipazione.email=?) AND idfrase is not null)");
			randomphrases_stmt.setString(1, players.get(0));
			randomphrases_stmt.setString(2, players.get(1));
			randomphrases_stmt.setString(3, players.get(2));
			randomphrases_exc = randomphrases_stmt.executeQuery();
			if(randomphrases_exc.isBeforeFirst()){
				while(randomphrases_exc.next()){
					nphrases=randomphrases_exc.getInt("total");			}
			}

			if(nphrases<5){//se non ci sono almeno 5 frasi disponibili
				//seleziona tutti gli amministratori
				admin_list_stmt = conn.prepareStatement("select email from utente where admin=true");
				admin_list_exc = admin_list_stmt.executeQuery();
				if(admin_list_exc.isBeforeFirst()){
					while(admin_list_exc.next()){
						admin.add(admin_list_exc.getString(1));
					}
				}

				for(String a:admin){
					//manda e-mail agli amministratori
					new MailSender("ruotadellafortunamanagement@gmail.com", "Fortuna98", a, "Aggiungere nuove Frasi", "Attenzione! Aggiungere nuove frasi al gioco la Ruota della Fortuna!.");		
				}

				//elimino la partecipazione e la partita
				delete_partecipation_stmt = conn.prepareStatement("DELETE FROM partecipazione WHERE idpartita=?");
				delete_partecipation_stmt.setLong(1, idMatch);
				delete_partecipation_stmt.executeUpdate();
				//elimina la partita
				delete_match_stmt = conn.prepareStatement("DELETE FROM partita WHERE idpartita=?");
				delete_match_stmt.setLong(1, idMatch);
				delete_match_stmt.executeUpdate();

				returned=false;

			}else{
				returned=true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{

			if(player_list_exc!=null){
				try {
					player_list_exc.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}}
			if(player_list_stmt!=null){
				try {
					player_list_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(randomphrases_exc!=null){
				try {
					randomphrases_exc.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(randomphrases_stmt!=null){
				try {
					randomphrases_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(admin_list_exc!=null){
				try {
					admin_list_exc.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(admin_list_stmt!=null){
				try {
					admin_list_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(delete_partecipation_stmt!=null){
				try {
					delete_partecipation_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(delete_match_stmt!=null){
				try {
					delete_match_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}

			disconnect();
		}

		return returned;

	}

	/**
	 * metodo usatro per ottenere la frase della manche della partita
	 * @param id indica l'id della partita
	 * @return data rappresenta la frase
	 */
	public ArrayList<String> get_phrase(long id){

		int nwords;
		ArrayList<String> result=new ArrayList<String>();
		int manche=0;
		String string="";
		PreparedStatement get_manche_stmt=null;
		ResultSet get_manche_exc=null;
		PreparedStatement get_phrase_stmt=null;
		ResultSet get_phrase_exc=null;
		try {
			//seleziono il numero della manche corrispondete alla partita
			get_manche_stmt = conn.prepareStatement("select nmanche from partita where idpartita=?");
			get_manche_stmt.setLong(1, id);
			get_manche_exc = get_manche_stmt.executeQuery();
			if(get_manche_exc.isBeforeFirst()){
				get_manche_exc.next();
				manche=get_manche_exc.getInt(1);
			}
			//seleziono il corpo della frase
			get_phrase_stmt = conn.prepareStatement("select corpo from frase natural join manche natural join partita where idmanche=? and partita.idpartita=? and partita.datafine = 0 ");
			get_phrase_stmt.setLong(1, manche);
			get_phrase_stmt.setLong(2, id);
			get_phrase_exc = get_phrase_stmt.executeQuery();
			if(get_phrase_exc.isBeforeFirst()){
				get_phrase_exc.next();
				string=get_phrase_exc.getString(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{		
			if(get_manche_exc!=null){
				try {
					get_manche_exc.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(get_manche_stmt!=null){
				try {
					get_manche_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}		
			if(get_phrase_exc!=null){
				try {
					get_phrase_exc.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(get_phrase_stmt!=null){
				try {
					get_phrase_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}

			disconnect();
		}
		//algoritmo usato per restituire al client una frase formattata in quattro righe di lunghezza 15
		if(string.length()<=60){// se la stringa ha lunghezza inferiore di 60
			String[] strings_to_be_joined= string.split(" ");//divido la stringa in parole
			nwords = strings_to_be_joined.length;//conto il numero di parole
			ArrayList<String> words=new ArrayList<String>();//creo la lista di stringhe che conterra le parole
			ArrayList<String> joinedwords=new ArrayList<String>();//creo la lista che conterrà la frase formattata da passare al client
			//inserisco le parole nella lista
			for(String c: strings_to_be_joined){
				words.add(c);	}
			String wordssum = null; //inizializzo variabile di appoggio
			for (int i = 0; i < words.size(); i++) {//ciclo sul numero di parole totali
				wordssum=words.get(i);
				if(wordssum.length()<=15){//se la concatenazione non supera lunghezza 15(numero massimo di lettere per riga)		
					for(int c=0;c<=(nwords/4);c++){//divido la frase in quattro righe
						//inserisco le parole che stanno su una riga di lunghezza 15
						if((i+1<words.size()) && wordssum.length()+1+words.get(i+1).length()<=15){	
							wordssum+=" "+words.get(i+1);
							//non considero le parole già concatenate
							i++;
						}
					}		
				}else{
					wordssum=words.get(i); 
					i++;
				}
				//aggiungo nuova riga
				joinedwords.add(wordssum);
			}
			//inserisco la frase formattata per ritornarla al client
			result=joinedwords;
		}

		for(String x:result){System.out.println(x);}
		return result;
	}


	/**
	 * metodo usato per ottenere tutte le frasi
	 * @return indica la lista di tutte le frasi
	 */
	public Hashtable<Integer,Phrases> get_phrases() {

		Hashtable<Integer,Phrases> phrases=new Hashtable<Integer,Phrases>();
		PreparedStatement phrases_stmt=null;
		ResultSet phrases_exc=null;
		try {
			phrases_stmt = conn.prepareStatement("select idfrase,tema,corpo from frase order by idfrase");
			phrases_exc = phrases_stmt.executeQuery();
			if(phrases_exc.isBeforeFirst()){
				while(phrases_exc.next()){
					phrases.put(phrases_exc.getInt(1),new Phrases(phrases_exc.getString(3),phrases_exc.getString(2)));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(phrases_exc!=null){
				try {
					phrases_exc.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}

			if(phrases_stmt!=null){
				try {
					phrases_stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}}

			disconnect();
		}
		return phrases;
	}

	/**
	 * metodo usato per verificare la corretta rappresentazione delle frasi
	 * @param phrases indica le frasi
	 * @return indica se le frasi sono corrette o meno
	 */
	public boolean hasEmptyValues(Object[][] phrases){

		for(int i=0;i<phrases.length;i++)
			for(int j=0;j<phrases[0].length;j++)
				if(phrases[i][j].equals(""))
					return true;
		return false;

	}

}
