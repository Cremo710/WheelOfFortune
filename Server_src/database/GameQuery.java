package database;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

/**
 * la classe GameQuery contiene i metodi usati per le query effettuate durante il gioco
 * @author Andrea Peluso
 * @version 1.0
 */
public class GameQuery extends DBimplementation{
	
	/**
	 * metodo usato per ottenere l'id degli altri giocatori della partita
	 * @param idmatch indica l'identificativo della partita
	 * @param nick indica l'email del giocatore
	 * @return lista degli altri due giocatori della partita
	 */
	public ArrayList<String> otherPlayer_list(long idmatch,String nick){

		ArrayList<String> returned=new ArrayList<String>();
		PreparedStatement player_list_stmt=null;
		ResultSet player_list_exc=null;
		try {
			//ottengo la lista dei giocatori alla partita tranne chi ha chiamato il metodo
			player_list_stmt= conn.prepareStatement("select nickname from partecipazione natural join utente where idpartita=? and tipo=0");
			player_list_stmt.setLong(1, idmatch);
			player_list_exc = player_list_stmt.executeQuery();
			if(player_list_exc.isBeforeFirst()){
				while(player_list_exc.next()){
					if(!(player_list_exc.getString(1).equals(nick))){
						returned.add(player_list_exc.getString(1));
					}
				}
			}
		} catch (SQLException e) {
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
			disconnect();
		}
		return returned;
	}
	
	
	/**
	 * metodo usato dagli osservatori per ottenre la lista dei giocatori della partita
	 * @param idmatch indica l'identificativo della partita
	 * @return lista dei giocatori della partita
	 */
	public ArrayList<String> allPlayer_list(long idmatch){

		ArrayList<String> returned=new ArrayList<String>();
		PreparedStatement player_list_stmt=null;
		ResultSet player_list_exc=null;
		try {
			//ottengo la lista di tutti i giocatori alla partita
			player_list_stmt= conn.prepareStatement("select nickname from partecipazione natural join utente where idpartita=? and tipo=0");
			player_list_stmt.setLong(1, idmatch);
			player_list_exc = player_list_stmt.executeQuery();
			if(player_list_exc.isBeforeFirst()){
				while(player_list_exc.next()){
					returned.add(player_list_exc.getString(1));
				}
			}
		} catch (SQLException e) {
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
			disconnect();
		}
		return returned;
	}

	
	
	/**
	 * metodo usato per ottenere i dati di un utente
	 * @param email indica l'email dell'utente
	 * @return rappresenta i dati dell'utente
	 */
	public String[] get_data(String email){

		String[] data=new String[3];
		PreparedStatement get_data_stmt=null;
		ResultSet get_data_exc=null;
		try {
			//seleziono i dati realativi all'utente
			get_data_stmt = conn.prepareStatement("select nome,cognome,nickname from utente where email=?");
			get_data_stmt.setString(1, email);
			get_data_exc = get_data_stmt.executeQuery();
			if(get_data_exc.next()){
				data[0]=get_data_exc.getString(1);
				data[1]=get_data_exc.getString(2);
				data[2]=get_data_exc.getString(3);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(get_data_exc!=null){
				try {
					get_data_exc.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}}
			if(get_data_stmt!=null){
				try {
					get_data_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}

			disconnect();
		}
		return data;
	}

	
	/**
	 * metodo usato per creare una nuova partita
	 * @param email indica l'email di chi ha creato la partita
	 * @return indica l'identificativo della partita creata
	 */
	public long create_match(String email){

		long returned=0;
		long id=0;
		PreparedStatement partecipation_stmt=null;
		ResultSet create_match_stmt_exc=null;
		PreparedStatement create_match_stmt=null;
		Random r = new Random();
		int n_random=r.nextInt(2);//numero casuale da 0 a 2

		try {
			String generatedColumns[] = {"idpartita"};
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			long time1= timestamp.getTime();//ora attuale

			//inserimento nuova partita
			create_match_stmt = conn.prepareStatement("INSERT into partita (datainizio, datafine,email,nmanche,winner,turno) VALUES (?,?,?,0,null,?);",generatedColumns );
			create_match_stmt.setLong(1, time1);
			create_match_stmt.setLong(2, 0);
			create_match_stmt.setString(3, email);
			create_match_stmt.setInt(4, n_random);
			update = create_match_stmt.executeUpdate();
			create_match_stmt_exc = create_match_stmt.getGeneratedKeys();
			if (create_match_stmt_exc.next()) {
				id = create_match_stmt_exc.getLong(1);
			}

			if(update==1){
				//inserisco partecipazione alla partita 
				partecipation_stmt = conn.prepareStatement("INSERT into partecipazione (email, idpartita,tipo) VALUES (?,?,0)");
				partecipation_stmt.setString(1, email);
				partecipation_stmt.setLong(2, id);
				update2 = partecipation_stmt.executeUpdate();
				if(update2==1){
					returned=id;
				}else{
					returned=0;
				}
			}else{
				returned=0;
			}


		} catch (SQLException e) {
			e.printStackTrace();
		}finally{

			if(partecipation_stmt!=null){
				try {
					partecipation_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(create_match_stmt_exc!=null){
				try {
					create_match_stmt_exc.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(create_match_stmt!=null){
				try {
					create_match_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}

			disconnect();
		}
		return returned;

	}

	/**
	 * metodo usato per ottenere la lista delle partite disponibili
	 * @return indica la lista delle partite
	 */
	public LinkedHashMap<Long,String> matches_list(){
		LinkedHashMap<Long,String> data = new LinkedHashMap<Long,String>();
		PreparedStatement match_list_stmt=null;
		ResultSet match_list_exc=null;
		try {
			//seleziono la lista delle partite (le ultime 5)
			match_list_stmt = conn.prepareStatement("select idpartita,nickname from partita natural join utente where datafine=0 ORDER BY idpartita DESC limit 5");
			match_list_exc = match_list_stmt.executeQuery();
			while(match_list_exc.next()){
				data.put(match_list_exc.getLong(1),match_list_exc.getString(2));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(match_list_exc!=null){
				try {
					match_list_exc.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(match_list_stmt!=null){
				try{
					match_list_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}

			disconnect();
		}
		return data;
	}

	/**
	 * metodo usato per ottenere la classifica della partita 
	 * @param idMatch indica l'identificativo della partita
	 * @return rappresenta la classifica
	 */
	public LinkedHashMap<String,Integer> ranking(long idMatch){
		LinkedHashMap<String,Integer> data = new LinkedHashMap<String,Integer>();
		PreparedStatement ranking_stmt=null;
		ResultSet ranking_exc=null;
		try {
			//ottengo la classifica
			ranking_stmt = conn.prepareStatement("select nickname,sum(punteggio) as score from partecipazione p left join manche m on p.email=m.vincitore and p.idpartita=m.idpartita inner join utente u on p.email=u.email where p.tipo=0 and p.idpartita=? group by(nickname) ORDER BY score DESC nulls last,nickname ASC");
			ranking_stmt.setLong(1, idMatch);
			ranking_exc = ranking_stmt.executeQuery();
			while(ranking_exc.next()){
				data.put(ranking_exc.getString(1),ranking_exc.getInt(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(ranking_exc!=null){
				try {
					ranking_exc.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(ranking_stmt!=null){
				try{
					ranking_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}

			disconnect();
		}
		return data;
	}

	/**
	 * metodo usato per ottenere il numero di player della partita
	 * @param idmatch indica l'identificativo della partita
	 * @return indica il numero di player della partita
	 */
	public int n_player(long idmatch){
		int returned=0;
		PreparedStatement n_player_stmt=null;
		ResultSet n_player_exc=null;
		try {
			//seleziono il numero di giocatori alla partita(escludendo gli osservatori)
			n_player_stmt = conn.prepareStatement("select count(*) from partecipazione where idpartita=? and tipo=0");
			n_player_stmt.setLong(1, idmatch);
			n_player_exc = n_player_stmt.executeQuery();
			if(n_player_exc.isBeforeFirst()){
				n_player_exc.next();
				returned=n_player_exc.getInt(1);

			}else{
				returned=0;//se non ci sono giocatori ritorna 0
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(n_player_exc!=null){
				try {
					n_player_exc.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}}
			if(n_player_stmt!=null){
				try {
					n_player_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}

			disconnect();
		}
		return returned;
	}

	/**
	 * metodo usato dagli osservatori per ottenere la lista dei giocatori della partita tranne il giocatore che chiama il metodo
	 * @param idmatch indica l'identificativo della partita
	 * @param email indica l'email di chi ha chiamato il metodo
	 * @return lista dei giocatori della partita
	 */
	public ArrayList<String> player_list(long idmatch,String email){

		ArrayList<String> returned=new ArrayList<String>();
		PreparedStatement player_list_stmt =null;
		ResultSet player_list_exc=null;
		try {
			//seleziono la lista dei giocatori e degli osservatori alla partita
			player_list_stmt = conn.prepareStatement("select email from partecipazione where idpartita=? and tipo <> 2");
			player_list_stmt.setLong(1, idmatch);
			player_list_exc = player_list_stmt.executeQuery();
			if(player_list_exc.isBeforeFirst()){
				while(player_list_exc.next()){
					if(!(player_list_exc.getString(1).equals(email))){
						returned.add(player_list_exc.getString(1));
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(player_list_exc!=null){
				try {
					player_list_exc.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(player_list_stmt!=null){
				try {
					player_list_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}

			disconnect();
		}
		return returned;
	}

	/**
	 * metodo usato per la creazione di una partita
	 * @param idMatch indica l'identificativo della partita
	 */
	public void start_match(long idMatch){

		long idphrase=0;
		ArrayList<String> players =new ArrayList<String>();
		PreparedStatement update_manche_stmt=null;
		PreparedStatement player_list_stmt=null;
		ResultSet player_list_exc=null;
		PreparedStatement randomphrases_stmt =null;
		ResultSet randomphrases_exc=null;
		PreparedStatement new_manche_stmt=null;
		try{
			//aggiorno la partita settando la manche a 1
			update_manche_stmt = conn.prepareStatement("update partita set nmanche=nmanche+1 where idpartita=?");
			update_manche_stmt.setLong(1, idMatch);
			update_manche_stmt.executeUpdate();

			//seleziono la lista dei giocatori alla partita
			player_list_stmt = conn.prepareStatement("select email from partecipazione where idpartita=? and tipo=0");
			player_list_stmt.setLong(1, idMatch);
			player_list_exc = player_list_stmt.executeQuery();
			if(player_list_exc.isBeforeFirst()){
				while(player_list_exc.next()){
					players.add(player_list_exc.getString(1));
				}

			}
			//scelgo una frase che non è mai stata usata dai tre giocatori
			randomphrases_stmt = conn.prepareStatement("SELECT idfrase FROM frase WHERE idfrase not in(select distinct idfrase from manche natural join partita inner join partecipazione on partita.idpartita=partecipazione.idpartita where (partecipazione.email=? OR partecipazione.email=? OR partecipazione.email=?) AND idfrase is not null) ORDER BY RANDOM() LIMIT 1");
			randomphrases_stmt.setString(1, players.get(0));
			randomphrases_stmt.setString(2, players.get(1));
			randomphrases_stmt.setString(3, players.get(2));
			randomphrases_exc = randomphrases_stmt.executeQuery();
			if(randomphrases_exc.isBeforeFirst()){
				randomphrases_exc.next();
				idphrase=randomphrases_exc.getLong(1);
			}
			//inserisco una nuova manche nella tabella manche associando la frase
			new_manche_stmt = conn.prepareStatement("INSERT into manche (idmanche,idpartita,idfrase,punteggio,vincitore) VALUES (1,?,?,0,null)");
			new_manche_stmt.setLong(1, idMatch);
			new_manche_stmt.setLong(2, idphrase);
			update = new_manche_stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(update_manche_stmt!=null){
				try {
					update_manche_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(player_list_exc!=null){
				try {
					player_list_exc.close();
				} catch (SQLException e) {
					e.printStackTrace();
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
			if(new_manche_stmt!=null){
				try {
					new_manche_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}

			disconnect();
		}

	}

	/**
	 * metodo usato per aggiornare l'ultima manche
	 * @param idMatch indica l'identificativo della partita
	 * @param email indica l'email del vincitore della manche
	 * @param score indica il punteggio del vincitore della manche
	 */
	public void update_last_manche(long idMatch,String email,int score){
		PreparedStatement update_manche_winner_stmt=null;
		try {
			//aggiorno l'ultima manche 
			update_manche_winner_stmt = conn.prepareStatement("update manche set vincitore=? , punteggio=? where idpartita=? and idmanche=(SELECT nmanche from partita WHERE idpartita=?)");
			update_manche_winner_stmt.setString(1, email);
			update_manche_winner_stmt.setInt(2, score);
			update_manche_winner_stmt.setLong(3, idMatch);
			update_manche_winner_stmt.setLong(4, idMatch);
			update_manche_winner_stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(update_manche_winner_stmt!=null){
				try {
					update_manche_winner_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}

			disconnect();
		}

	}

	/**
	 * metodo usato per aggiornare la manche
	 * @param idMatch indica l'identificativo della partita
	 * @param email indica l'email di chi ha vinto la manche
	 * @param score indica il punteggio del vincitore della manche
	 */
	public void update_manche(long idMatch,String email,int score){

		int nmanche=0;
		long idphrase=0;
		ArrayList<String> players =new ArrayList<String>();
		PreparedStatement update_manche_winner_stmt=null;
		PreparedStatement update_manche_stmt =null;
		PreparedStatement get_nmanche_stmt=null;
		ResultSet get_nmanche_exc=null;
		PreparedStatement player_list_stmt=null;
		ResultSet player_list_exc=null;
		PreparedStatement randomphrases_stmt=null;
		ResultSet randomphrases_exc=null;
		PreparedStatement new_manche_stmt=null;
		try {
			//aggiorno manche inserendo il vincitore e il suo punteggio
			update_manche_winner_stmt = conn.prepareStatement("update manche set vincitore=? , punteggio=? where idpartita=? and idmanche=(SELECT nmanche from partita WHERE idpartita=?)");
			update_manche_winner_stmt.setString(1, email);
			update_manche_winner_stmt.setInt(2, score);
			update_manche_winner_stmt.setLong(3, idMatch);
			update_manche_winner_stmt.setLong(4, idMatch);
			update_manche_winner_stmt.executeUpdate();
			//aggiorno la tabella partita cambiando il numero della manche
			update_manche_stmt = conn.prepareStatement("update partita set nmanche=nmanche+1 where idpartita=?");
			update_manche_stmt.setLong(1, idMatch);
			update_manche_stmt.executeUpdate();
			//seleziono il numero della manche
			get_nmanche_stmt = conn.prepareStatement("SELECT nmanche from partita where idpartita=?");
			get_nmanche_stmt.setLong(1, idMatch);
			get_nmanche_exc = get_nmanche_stmt.executeQuery();
			if(get_nmanche_exc.isBeforeFirst()){
				get_nmanche_exc.next();
				nmanche=get_nmanche_exc.getInt(1);
			}
			//seleziono i giocatori della partita
			player_list_stmt = conn.prepareStatement("select email from partecipazione where idpartita=? and tipo=0");
			player_list_stmt.setLong(1, idMatch);
			player_list_exc = player_list_stmt.executeQuery();
			if(player_list_exc.isBeforeFirst()){
				while(player_list_exc.next()){
					players.add(player_list_exc.getString(1));
				}
			}
			//seleziono una frase che non è mai stata usata dai giocatori
			randomphrases_stmt = conn.prepareStatement("SELECT idfrase FROM frase WHERE idfrase not in(select distinct idfrase from manche natural join partita inner join partecipazione on partita.idpartita=partecipazione.idpartita where (partecipazione.email=? OR partecipazione.email=? OR partecipazione.email=?) AND idfrase is not null) ORDER BY RANDOM() LIMIT 1");
			randomphrases_stmt.setString(1, players.get(0));
			randomphrases_stmt.setString(2, players.get(1));
			randomphrases_stmt.setString(3, players.get(2));
			randomphrases_exc = randomphrases_stmt.executeQuery();
			if(randomphrases_exc.isBeforeFirst()){
				randomphrases_exc.next();
				idphrase=randomphrases_exc.getLong(1);
			}
			//inserisco una nuova manche associandole la frase
			new_manche_stmt = conn.prepareStatement("INSERT into manche (idmanche,idpartita,idfrase,punteggio,vincitore) VALUES (?,?,?,0,null)");
			new_manche_stmt.setLong(1, nmanche);
			new_manche_stmt.setLong(2, idMatch);
			new_manche_stmt.setLong(3, idphrase);
			update = new_manche_stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(update_manche_winner_stmt!=null){
				try {
					update_manche_winner_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(update_manche_stmt!=null){
				try {
					update_manche_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(get_nmanche_exc!=null){
				try {
					get_nmanche_exc.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(get_nmanche_stmt!=null){
				try {
					get_nmanche_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(player_list_exc!=null){
				try {
					player_list_exc.close();
				} catch (SQLException e) {
					e.printStackTrace();
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
			if(new_manche_stmt!=null){
				try {
					new_manche_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			disconnect();
		}

	}

	
	/**
	 * metodo usato per ottenere il tema della frase
	 * @param id indica l'identificativo della partita
	 * @return rappresenta il tema associato alla frase
	 */
	public String get_theme(long id){

		int manche=0;
		String stringa="";
		PreparedStatement get_manche_theme_stmt =null;
		ResultSet get_manche_theme_exc=null;
		PreparedStatement get_theme_stmt=null;
		ResultSet get_theme_exc=null;
		try {
			//seleziono il numero della manche corrispondete alla partita
			get_manche_theme_stmt = conn.prepareStatement("select nmanche from partita where idpartita=?");
			get_manche_theme_stmt.setLong(1, id);
			get_manche_theme_exc = get_manche_theme_stmt.executeQuery();
			if(get_manche_theme_exc.isBeforeFirst()){
				get_manche_theme_exc.next();
				manche=get_manche_theme_exc.getInt(1);
			}
			//seleziono il tema della frase
			get_theme_stmt = conn.prepareStatement("select tema from frase natural join manche natural join partita where idmanche=? and partita.idpartita=? and partita.datafine = 0  ");
			get_theme_stmt.setLong(1, manche);
			get_theme_stmt.setLong(2, id);
			get_theme_exc = get_theme_stmt.executeQuery();
			if(get_theme_exc.isBeforeFirst()){
				get_theme_exc.next();
				stringa=get_theme_exc.getString(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(get_manche_theme_exc!=null){
				try {
					get_manche_theme_exc.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(get_manche_theme_stmt!=null){
				try {
					get_manche_theme_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(get_theme_exc!=null){
				try {
					get_theme_exc.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(get_theme_stmt!=null){
				try {
					get_theme_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}

			disconnect();
		}
		return stringa;
	}

	/**
	 * metodo usato per inserire una mossa nel database
	 * @param nmanche indica il numero della manche
	 * @param email indica l'email di chi ha effettuato la mossa
	 * @param point indica il punteggio della mossa
	 * @param move indica il tipo di mossa
	 * @param idMatch indica l'identificativo della partita
	 */
	public void update_move(int nmanche,String email, int point, String move,long idMatch){

		PreparedStatement update_move_stmt=null;
		try {
			//inserisco una nuova mossa nella tabella mossa
			update_move_stmt = conn.prepareStatement("INSERT into mossa (idmanche,idutente,idpartita,punti,nome) VALUES (?,?,?,?,?);");
			update_move_stmt.setInt(1, nmanche);
			update_move_stmt.setString(2, email);
			update_move_stmt.setLong(3, idMatch);
			update_move_stmt.setInt(4, point);
			update_move_stmt.setString(5, move);
			update = update_move_stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(update_move_stmt!=null){
				try {
					update_move_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			disconnect();
		}


	}

	/**
	 * metodo usato per settare la fine della partita
	 * @param idMatch indica l'identificativo della partita
	 */
	public void end_match(long idMatch){

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		long time1= timestamp.getTime();//ora attuale
		PreparedStatement end_match_stmt=null;

		try {
			//aggiorno la partita inserendo la data di fine della stessa
			end_match_stmt = conn.prepareStatement("update partita set datafine=? where idpartita=?");
			end_match_stmt.setLong(1, time1);
			end_match_stmt.setLong(2, idMatch);
			end_match_stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(end_match_stmt!=null){
				try {
					end_match_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}

			disconnect();
		}

	}

	/**
	 * metodo usato per aggiungere una partecipazione ad una partita
	 * @param email indica l'email di chi ha partecipato
	 * @param idmatch indica l'identificativo della partita
	 * @return indica se l'operazione è andata a buon fine
	 */
	public int join_match(String email,long idmatch){

		PreparedStatement join_match_stmt =null;
		try {
			//inserisco la partecipazione nella tabella partecipazione inserendo come tipo 0 (giocatore)
			join_match_stmt = conn.prepareStatement("INSERT into partecipazione (email, idpartita,tipo) VALUES (?,?,0)");
			join_match_stmt.setString(1, email);
			join_match_stmt.setLong(2, idmatch);
			update = join_match_stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(join_match_stmt!=null){
				try {
					join_match_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}

			disconnect();
		}
		return update;

	}

	

	/**
	 * metodo usato per ottenere il creatore di una partita
	 * @param idmatch indica l'identificativo della partita
	 * @return rappresenta l'email del creatore
	 */
	public String get_creator(long idmatch){
		String returned="";
		PreparedStatement get_creator_stmt=null;
		ResultSet get_creator_exc=null;
		try {
			//seleziono il creatore della partita
			get_creator_stmt = conn.prepareStatement("SELECT email from partita where idpartita=?");
			get_creator_stmt.setLong(1, idmatch);
			get_creator_exc = get_creator_stmt.executeQuery();
			if(get_creator_exc.isBeforeFirst()){
				get_creator_exc.next();
				returned=get_creator_exc.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(get_creator_exc!=null){
				try {
					get_creator_exc.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(get_creator_stmt!=null){
				try {
					get_creator_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}

			disconnect();
		}
		return returned;
	}

	/**
	 * metodo usato per ottenere il vincitore della partita
	 * @param idmatch indica l'identificativo della partita
	 * @return indica il vincitore
	 */
	public String get_winner(long idmatch){

		String returned="";
		Hashtable<String,Integer> players=new Hashtable<String,Integer>();
		PreparedStatement player_list_stmt=null;
		ResultSet player_list_exc=null;
		PreparedStatement update_winner_stmt=null;
		try {
			//seleziono i punteggi ottenuti dai 3 giocatori nella partita
			player_list_stmt = conn.prepareStatement("select sum(punteggio),vincitore from manche where idpartita=? and vincitore in (select email from partecipazione where idpartita=? and tipo=0) group by vincitore ");
			player_list_stmt.setLong(1, idmatch);
			player_list_stmt.setLong(2, idmatch);
			player_list_exc = player_list_stmt.executeQuery();
			if(player_list_exc.isBeforeFirst()){
				while(player_list_exc.next()){
					players.put(player_list_exc.getString(2), player_list_exc.getInt(1));
				}	
			}

			//ottengo il giocatore con il punteggio più alto
			String maxKey=null;
			int maxValue = Integer.MIN_VALUE; 
			for(Map.Entry<String,Integer> entry : players.entrySet()) {
				if(entry.getValue() > maxValue) {
					maxValue = entry.getValue();
					maxKey = entry.getKey();
				}
			}
			//aggiorno la partita impostando il vincitore
			update_winner_stmt = conn.prepareStatement("update partita set winner=? where idpartita=?");
			update_winner_stmt.setString(1, maxKey);
			update_winner_stmt.setLong(2, idmatch);
			update_winner_stmt.executeUpdate();
			returned=maxKey;//ritorno l'email del vincitore

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(player_list_exc!=null){
				try {
					player_list_exc.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}

			if(player_list_stmt!=null){
				try {
					player_list_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(update_winner_stmt!=null){
				try {
					update_winner_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}

			disconnect();
		}

		return returned;
	}
	
	

	/**
	 * metodo usato per ottenere il giocatore che ha il diritto di fare la prima mossa nella partita
	 * @param idmatch indica l'identificativo della partita
	 * @return indica il turno
	 */
	public String get_Turn(long idmatch){

		    PreparedStatement get_Turn_stmt=null;
		    ResultSet get_Turn_exc =null;
		    String turno=null;
		    try{
		    	
		   //select email from partecipazione where idpartita=? and tipo=0 LIMIT 1 OFFSET (select turno from partita where idpartita=?)
		    get_Turn_stmt = conn.prepareStatement("select email from partecipazione where idpartita=? and tipo=0 LIMIT 1 OFFSET (select turno from partita where idpartita=?)");
		    get_Turn_stmt.setLong(1, idmatch);
		    get_Turn_stmt.setLong(2, idmatch);
		    get_Turn_exc =get_Turn_stmt.executeQuery();
			while(get_Turn_exc.next()){
				turno=get_Turn_exc.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(get_Turn_exc!=null){
				try {
					get_Turn_exc.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}}
			if(get_Turn_stmt!=null){
				try {
					get_Turn_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}

			disconnect();
		}
			return turno;

	}

	/**
	 * metodo usato per ottenere il giocatore che ha il turno nella partita
	 * @param idmatch indica l'identificativo della partita
	 * @return indica il turno
	 */
	public int getNTurn(long idmatch){

	    PreparedStatement get_Turn_stmt=null;
	    ResultSet get_Turn_exc =null;
	    int turno=0;
	    try{
	    	
	    get_Turn_stmt = conn.prepareStatement("select turno from partita where idpartita=?");
	    get_Turn_stmt.setLong(1, idmatch);
	    get_Turn_exc =get_Turn_stmt.executeQuery();
		while(get_Turn_exc.next()){
			turno=get_Turn_exc.getInt(1);
		}
	} catch (SQLException e) {
		e.printStackTrace();
	}finally{
		if(get_Turn_exc!=null){
			try {
				get_Turn_exc.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}}
		if(get_Turn_stmt!=null){
			try {
				get_Turn_stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}}

		disconnect();
	}
		return turno;

}

	/**
	 * metodo usato aggiornare il turno nel database
	 * @param nTurn indica il numero del turno 
	 * @param idmatch indica l'identificativo della partita
	 * @param email indica l'email di chi ha il turno
	 * @param nmanche indica il numero della manche 
	 * @return indica il nome del giocatore che possiede il turno 
	 */
	public String c_Turn(int nTurn,long idmatch,String email,int nmanche){
		int turn=0;
		String move="CT";
		int point=0;
		String who=null;
		//cambio il turno passando al prossimo giocatore
		if(nTurn==2){turn=0;}
		else{turn=nTurn+1;}

		PreparedStatement c_turn_game=null;
		ResultSet c_turn_game_exc=null;
		PreparedStatement update_move_ct_stmt=null;
		PreparedStatement update_match_stmt=null;
		try {
			c_turn_game = conn.prepareStatement("select email from partecipazione where idpartita=? and tipo=0 LIMIT 1 OFFSET ?;");
			c_turn_game.setLong(1, idmatch);
			c_turn_game.setInt(2, turn);
			c_turn_game_exc = c_turn_game.executeQuery();
			while(c_turn_game_exc.next()){
				who=c_turn_game_exc.getString(1);
			}

			update_match_stmt = conn.prepareStatement("update partita set turno=? where idpartita=?");
			update_match_stmt.setInt(1, turn);
			update_match_stmt.setLong(2, idmatch);
			update_match_stmt.executeUpdate();

			//aggiorna mossa 
			update_move_ct_stmt = conn.prepareStatement("INSERT into mossa (idmanche,idutente,idpartita,punti,nome) VALUES (?,?,?,?,?);");
			update_move_ct_stmt.setInt(1, nmanche);
			update_move_ct_stmt.setString(2, email);
			update_move_ct_stmt.setLong(3, idmatch);
			update_move_ct_stmt.setInt(4, point);
			update_move_ct_stmt.setString(5, move);
			update = update_move_ct_stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(c_turn_game_exc!=null){
				try {
					c_turn_game_exc.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(c_turn_game!=null){
				try {
					c_turn_game.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(update_move_ct_stmt!=null){
				try {
					update_move_ct_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}

			disconnect();
		}
		return who;
	}
	
	
	/**
     * metodo usato per eliminare la partecipazione del giocatore nella partita
     * @param email indica l'email di chi è uscito dalla lobby
     * @param idmatch indica l'identificativo della partita
     * @return indica se l'uscita è andata a buon fine
     */
	public int exit_lobby(long idmatch,String email){
		PreparedStatement exit_match_stmt =null;
		try {
			//elimino la partecipazione dalla partita quando un osservatore o giocatore esce dalla lobby
			exit_match_stmt = conn.prepareStatement("DELETE FROM partecipazione where idpartita=? and email=?");
			exit_match_stmt.setLong(1, idmatch);
			exit_match_stmt.setString(2, email);
			update = exit_match_stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(exit_match_stmt!=null){
				try {
					exit_match_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			disconnect();
		}
		return update;
	}
	

	 /**
    * metodo usato per aggiornare lo stato dell'osservatore
    * @param email indica l'email dell'osservatore che ha smesso di osservare
    * @param idmatch indica l'identificativo della partita
    * @return indica se l'uscita è andata a buon fine
    */
	public int exit_lobbyObserver(long idmatch,String email){
		PreparedStatement exit_match_stmt =null;
		try {
			//elimino la partecipazione dalla partita quando un osservatore o giocatore esce dalla lobby
			exit_match_stmt = conn.prepareStatement("update partecipazione set tipo=2 where idpartita=? and email=?");
			exit_match_stmt.setLong(1, idmatch);
			exit_match_stmt.setString(2, email);
			update = exit_match_stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(exit_match_stmt!=null){
				try {
					exit_match_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			disconnect();
		}
		return update;
	}

	/**
	 * metodo usato per eliminare una partita
	 * @param idmatch indica l'identificativo della partita
	 * @return indica se l'eliminazione è andata a buon fine
	 */
	public int remove_match(long idmatch){
		PreparedStatement join_match_stmt =null;
		try {
			//elimino la partita
			join_match_stmt = conn.prepareStatement("DELETE FROM partita where idpartita=?");
			join_match_stmt.setLong(1, idmatch);
			update = join_match_stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(join_match_stmt!=null){
				try {
					join_match_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			disconnect();
		}
		return update;
	}

	/**
     * metodo usato per aggiungere un osservatore alla partita
     * @param email indica l'email dell'osservatore che ha smesso di osservare
     * @param idmatch indica l'identificativo della partita
     * @return indica se l'uscita è andata a buon fine
     */
	public int join_observer(String email,long idmatch){
		PreparedStatement join_match_stmt =null;
		PreparedStatement rejoin_match_stmt =null;
		PreparedStatement check_stmt =null;
		ResultSet check_stmt_exc=null;
		try {
			//controllo se esiste già un osservatore con quella email in quella partita
			check_stmt = conn.prepareStatement("select * from partecipazione where idpartita=? and email=?");
			check_stmt.setLong(1, idmatch);
			check_stmt.setString(2, email);
			check_stmt_exc = check_stmt.executeQuery();
			if(check_stmt_exc.isBeforeFirst()){//se esiste aggirno lo stato a 1
				rejoin_match_stmt = conn.prepareStatement("update partecipazione set tipo=1 where idpartita=? and email=?");
				rejoin_match_stmt.setLong(1, idmatch);
				rejoin_match_stmt.setString(2, email);
				update = rejoin_match_stmt.executeUpdate();
			}else{
				//inserisco la partecipazione di un osservatore nel database
				join_match_stmt = conn.prepareStatement("INSERT into partecipazione (email, idpartita,tipo) VALUES (?,?,1)");
				join_match_stmt.setString(1, email);
				join_match_stmt.setLong(2, idmatch);
				update = join_match_stmt.executeUpdate();			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(join_match_stmt!=null){
				try {
					join_match_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}

			if(rejoin_match_stmt!=null){
				try {
					rejoin_match_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(check_stmt!=null){
				try {
					check_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}

			if(check_stmt_exc!=null){
				try {
					check_stmt_exc.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}


			disconnect();
		}
		return update;
	}

	

	
	/**
	 * metodo usato per ottenere il punteggio dei giocatori della partita
	 * @param idMatch indica l'identificativo della partita
	 * @param nickname indica l'email del giocatore
	 * @return indica il punteggio del giocatore
	 */
	public int get_Score(long idMatch, String nickname) {

		int score=0;
		PreparedStatement score_stmt=null;
		ResultSet score_exc=null;

		try {
			score_stmt = conn.prepareStatement("select sum(punti) from mossa where idutente=(select email from utente where nickname=?) and idpartita=? and idmanche=(select nmanche from partita where idpartita=?)");
			score_stmt.setString(1, nickname);
			score_stmt.setLong(2,idMatch);
			score_stmt.setLong(3,idMatch);
			score_exc = score_stmt.executeQuery();
			if(score_exc.isBeforeFirst()){
				while(score_exc.next()){
					score=score_exc.getInt(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(score_exc!=null){
				try {
					score_exc.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}

			if(score_stmt!=null){
				try {
					score_stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}}

			disconnect();
		}

		return score;
	}

	/**
	 * metodo usato per ottenere il deposito della partita
	 * @param idMatch indica l'identificativo della partita
	 * @param nickname indica l'email del giocatore
	 * @return indica il depostio del giocatore
	 */
	public int get_Bank(long idMatch, String nickname) {

		int scoretot=0;
		PreparedStatement scoretot_stmt=null;
		ResultSet scoretot_exc=null;

		try {

			scoretot_stmt = conn.prepareStatement("select sum(punteggio) from manche where vincitore=(select email from utente where nickname=?) and idpartita=?");
			scoretot_stmt.setString(1, nickname);
			scoretot_stmt.setLong(2,idMatch);
			scoretot_exc = scoretot_stmt.executeQuery();
			if(scoretot_exc.isBeforeFirst()){
				while(scoretot_exc.next()){
					scoretot=scoretot_exc.getInt(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{

			if(scoretot_exc!=null){
				try {
					scoretot_exc.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}

			if(scoretot_stmt!=null){
				try {
					scoretot_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			disconnect();
		}


		return scoretot;
	}

	/**
	 * metodo usato per ottenere il numero della manche della partita
	 * @param idMatch indica l'identificativo della partita
	 * @return indica il numero della manche
	 */
	public int get_Nmanche(long idMatch) {
		int nmanche=0;
		PreparedStatement nmanche_stmt=null;
		ResultSet nmanche_exc=null;
		try {
			nmanche_stmt = conn.prepareStatement("select nmanche from partita where idpartita=?");
			nmanche_stmt.setLong(1,idMatch);
			nmanche_exc =nmanche_stmt.executeQuery();
			if(nmanche_exc.isBeforeFirst()){
				while(nmanche_exc.next()){
					nmanche=nmanche_exc.getInt(1);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(nmanche_exc!=null){
				try {
					nmanche_exc.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}

			if(nmanche_stmt!=null){
				try {
					nmanche_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}

			disconnect();
		}

		return nmanche;
	}

	/**
	 * metodo usato per ottenere il numero dei jolly del giocatore nella partita
	 * @param idMatch indica l'identificativo della partita
	 * @param nickname indica l'email del giocatore
	 * @return indica il numero di jolly
	 */
	public int get_jolly(long idMatch, String nickname) {
		int jolly=0;
		PreparedStatement jolly_stmt=null;
		ResultSet jolly_exc=null;

		try {

			jolly_stmt = conn.prepareStatement("select count(*) - (select count(*) from mossa where mossa.nome='UJ' and mossa.idpartita=? and idutente=(select email from utente where nickname=?)) as njolly from mossa where mossa.nome='TJ' and mossa.idpartita=? and idutente=(select email from utente where nickname=?) ");
			jolly_stmt.setLong(1,idMatch);
			jolly_stmt.setString(2,nickname);
			jolly_stmt.setLong(3,idMatch);
			jolly_stmt.setString(4,nickname);
			jolly_exc = jolly_stmt.executeQuery();
			if(jolly_exc.isBeforeFirst()){
				while(jolly_exc.next()){
					jolly=jolly_exc.getInt(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{

			if(jolly_exc!=null){
				try {
					jolly_exc.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}

			if(jolly_stmt!=null){
				try {
					jolly_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			disconnect();
		}


		return jolly;
	}

	/**
	 * metodo usato per ottenere le lettere della frase nel tabellone svelate nella partita
	 * @param idMatch indica l'identificativo della partita
	 * @return indica le lettere svelate
	 */
	public ArrayList<String> get_Letters(long idMatch) {
		ArrayList<String> get_Letters=new ArrayList<String>();
		PreparedStatement letters_stmt=null;
		ResultSet letters_exc=null;

		try {
			letters_stmt = conn.prepareStatement("select distinct nome from mossa where mossa.idpartita=? and (mossa.nome like '%cns' OR mossa.nome like '%voc') and idmanche=(select nmanche from partita where idpartita=?)");
			letters_stmt.setLong(1,idMatch);
			letters_stmt.setLong(2,idMatch);
			letters_exc =letters_stmt.executeQuery();
			if(letters_exc.isBeforeFirst()){
				while(letters_exc.next()){
					get_Letters.add(letters_exc.getString(1));
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(letters_exc!=null){
				try {
					letters_exc.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}

			if(letters_stmt!=null){
				try {
					letters_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}

			disconnect();
		}

		return get_Letters;
	}
	
	

}
