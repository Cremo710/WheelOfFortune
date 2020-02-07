package database;
import game.QueryReturn;

import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * la classe StatisticsQuery contiene i metodi usati per effettuare le query delle statistiche 
 * @author Andrea Peluso
 * @version 1.0
 */
public class StatisticsQuery extends DBimplementation{
	
	 /**
     * metodo usato per ottenere il giocatore che detiene il primato di punteggio raggiunto per manche
     * @return indica il risultato della query
     */
	public QueryReturn statistics1(){
		QueryReturn returned=null;
		PreparedStatement statistics1_stmt=null;
		ResultSet statistics1_exc=null;
		try {
			statistics1_stmt = conn.prepareStatement("SELECT manche.punteggio,manche.vincitore,utente.nickname FROM manche inner join utente on manche.vincitore=utente.email WHERE manche.punteggio >= all( SELECT manche.punteggio FROM manche ) ORDER BY idmanche ASC LIMIT 1");
			statistics1_exc = statistics1_stmt.executeQuery();
			if(statistics1_exc.isBeforeFirst()){
				statistics1_exc.next();
				returned=new QueryReturn(statistics1_exc.getFloat(1),statistics1_exc.getString(2),statistics1_exc.getString(3));
			}else{
				returned=new QueryReturn(0,"","");;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(statistics1_exc!=null){
				try {
					statistics1_exc.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(statistics1_stmt!=null){
				try {
					statistics1_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}

			disconnect();
		}
		return returned;
	}

	/**
     * metodo usato per ottenere il giocatore che detiene il primato di punteggio raggiunto per partita
     * @return indica il risultato della query
     */
	public QueryReturn statistics2(){
		QueryReturn returned=null;
		PreparedStatement statistics2_stmt=null;
		ResultSet statistics2_exc=null;
		try {
			statistics2_stmt = conn.prepareStatement("select sum(manche.punteggio) as score,manche.vincitore,utente.nickname from manche inner join utente on manche.vincitore=utente.email group by(manche.idpartita,manche.vincitore,utente.nickname) HAVING sum(manche.punteggio) >= all( select sum(manche.punteggio) from manche group by(manche.idpartita,manche.vincitore,utente.nickname)) ORDER BY manche.idpartita ASC LIMIT 1");
			statistics2_exc = statistics2_stmt.executeQuery();
			if(statistics2_exc.isBeforeFirst()){
				statistics2_exc.next();
				returned=new QueryReturn(statistics2_exc.getFloat(1),statistics2_exc.getString(2),statistics2_exc.getString(3));
			}else{
				returned=new QueryReturn(0,"","");;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(statistics2_exc!=null){
				try {
					statistics2_exc.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(statistics2_stmt!=null){
				try {
					statistics2_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}

			disconnect();
		}
		return returned;
	}
	
	/**
     * metodo usato per ottenere il giocatore che ha giocato più manche in assoluto
     * @return indica il risultato della query
     */
	public QueryReturn statistics3(){
		QueryReturn returned=null;
		PreparedStatement statistics3_stmt=null;
		ResultSet statistics3_exc=null;
		try {
			statistics3_stmt = conn.prepareStatement("select count(*) as ntime,utente.email,utente.nickname from manche inner join partita on manche.idpartita=partita.idpartita inner join partecipazione on partita.idpartita=partecipazione.idpartita inner join utente on partecipazione.email=utente.email where utente.admin='false' group by(utente.email,utente.nickname) having count(*) >= all( select count(*) as ntime from manche inner join partita on manche.idpartita=partita.idpartita inner join partecipazione on partita.idpartita=partecipazione.idpartita inner join utente on partecipazione.email=utente.email where utente.admin='false' group by (utente.email,utente.nickname)) LIMIT 1");
			statistics3_exc = statistics3_stmt.executeQuery();
			if(statistics3_exc.isBeforeFirst()){
				statistics3_exc.next();
				returned=new QueryReturn(statistics3_exc.getFloat(1),statistics3_exc.getString(2),statistics3_exc.getString(3));
			}else{
				returned=new QueryReturn(0,"","");;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(statistics3_exc!=null){
				try {
					statistics3_exc.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(statistics3_stmt!=null){
				try {
					statistics3_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}

			disconnect();
		}
		return returned;
	}

	/**
     * metodo usato per ottenere il giocatore con la media più alta di punti acquisiti per manche
     * @return indica il risultato della query
     */
	public QueryReturn statistics4(){
		QueryReturn returned=null;
		PreparedStatement statistics4_stmt=null;
		ResultSet statistics4_exc=null;
		try {
			statistics4_stmt = conn.prepareStatement("SELECT ROUND(AVG(punteggio),2) AS score,manche.vincitore,utente.nickname FROM manche inner join utente on manche.vincitore=utente.email	group by (manche.vincitore,utente.nickname) having ROUND(AVG(punteggio),2)>= all( SELECT ROUND(AVG(punteggio),2) FROM manche inner join utente on manche.vincitore=utente.email	group by (manche.vincitore,utente.nickname)) limit 1");
			statistics4_exc = statistics4_stmt.executeQuery();
			if(statistics4_exc.isBeforeFirst()){
				statistics4_exc.next();
				returned=new QueryReturn(statistics4_exc.getFloat(1),statistics4_exc.getString(2),statistics4_exc.getString(3));
			}else{
				returned=new QueryReturn(0,"","");;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(statistics4_exc!=null){
				try {
					statistics4_exc.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(statistics4_stmt!=null){
				try {
					statistics4_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}

			disconnect();
		}
		return returned;
	}

	/**
     * metodo usato per ottenere il giocatore che ha dovuto cedere più volte il turno di gioco in seguito ad errori
     * @return indica il risultato della query
     */
	public QueryReturn statistics5(){
		QueryReturn returned=null;
		PreparedStatement statistics5_stmt=null;
		ResultSet statistics5_exc=null;
		try {
			statistics5_stmt = conn.prepareStatement("select count(*) as ntime,mossa.idutente,utente.nickname from mossa inner join utente on mossa.idutente=utente.email where mossa.nome='CT' group by (mossa.idutente,utente.nickname) having count(*) >= all( select count(*) as ntime from mossa inner join utente on mossa.idutente=utente.email where mossa.nome='CT' group by (mossa.idutente,utente.nickname)) limit 1");
			statistics5_exc = statistics5_stmt.executeQuery();
			if(statistics5_exc.isBeforeFirst()){
				statistics5_exc.next();
				returned=new QueryReturn(statistics5_exc.getFloat(1),statistics5_exc.getString(2),statistics5_exc.getString(3));
			}else{
				returned=new QueryReturn(0,"","");;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(statistics5_exc!=null){
				try {
					statistics5_exc.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(statistics5_stmt!=null){
				try {
					statistics5_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			disconnect();
		}
		return returned;
	}

	/**
     * metodo usato per ottenere il giocatore che ha perso tutto in seguito ad un giro di ruota per il maggior numero di volte
     * @return indica il risultato della query
     */
	public QueryReturn statistics6(){
		QueryReturn returned=null;
		PreparedStatement statistics6_stmt=null;
		ResultSet statistics6_exc=null;
		try {
			statistics6_stmt = conn.prepareStatement("select count(*) as ntime,mossa.idutente,utente.nickname from mossa inner join utente on mossa.idutente=utente.email where mossa.nome='Perde' group by (mossa.idutente,utente.nickname) having count(*) >= all( select count(*) as ntime from mossa inner join utente on mossa.idutente=utente.email where mossa.nome='Perde' group by (mossa.idutente,utente.nickname)) limit 1");
			statistics6_exc = statistics6_stmt.executeQuery();
			if(statistics6_exc.isBeforeFirst()){
				statistics6_exc.next();
				returned=new QueryReturn(statistics6_exc.getFloat(1),statistics6_exc.getString(2),statistics6_exc.getString(3));
			}else{
				returned=new QueryReturn(0,"","");;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(statistics6_exc!=null){
				try {
					statistics6_exc.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(statistics6_stmt!=null){
				try {
					statistics6_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			disconnect();
		}
		return returned;
	}

	/**
     * metodo usato per ottenere il numero medio mosse per manche con le quali viene indovinata una frase
     * @return indica il risultato della query
     */
	public float statistics7(){
		float returned=0;
		PreparedStatement statistics7_stmt=null;
		ResultSet statistics7_exc=null;
		try {
			statistics7_stmt = conn.prepareStatement("select round(avg(movecount),2) from (select count(*) as movecount,manche.idmanche from mossa natural join manche where mossa.nome <> 'CT' group by(manche.idmanche)) as count_move");
			statistics7_exc = statistics7_stmt.executeQuery();
			if(statistics7_exc.isBeforeFirst()){
				statistics7_exc.next();
				returned=statistics7_exc.getFloat(1);
			}else{
				returned=0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(statistics7_exc!=null){
				try {
					statistics7_exc.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(statistics7_stmt!=null){
				try {
					statistics7_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			disconnect();
		}
		return returned;
	}


	/**
     * metodo usato per mostrare la chiamata di consonante, riferita ad una manche già giocata, 
     * che ha portato all’acquisizione della maggior quantità punti, la frase misteriosa associata, 
     * e l’utente che ha fatto la mossa.
     * @return indica il risultato della query
     */
	public QueryReturn statistics8(){
		QueryReturn returned=null;
		PreparedStatement statistics8_stmt=null;
		ResultSet statistics8_exc=null;
		try {
			statistics8_stmt = conn.prepareStatement("select mossa.nome,mossa.idutente,frase.corpo from mossa inner join utente on mossa.idutente=utente.email inner join manche on mossa.idmanche=manche.idmanche and mossa.idpartita=manche.idpartita inner join frase on manche.idfrase=frase.idfrase where mossa.nome LIKE '%cns' and mossa.punti >= all( select mossa.punti from mossa inner join manche on mossa.idmanche=manche.idmanche and mossa.idpartita=manche.idpartita where mossa.nome LIKE '%cns' and manche.idfrase is not null) limit 1");
			statistics8_exc = statistics8_stmt.executeQuery();
			if(statistics8_exc.isBeforeFirst()){
				statistics8_exc.next();
				returned=new QueryReturn(statistics8_exc.getString(1),statistics8_exc.getString(2),statistics8_exc.getString(3));
			}else{
				returned=new QueryReturn("","","");	
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(statistics8_exc!=null){
				try {
					statistics8_exc.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(statistics8_stmt!=null){
				try {
					statistics8_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			disconnect();
		}
		return returned;
	}

	/**
     * metodo usato per mostrare il numero di manche giocate
     * @param email indica l'email dell'utente
     * @return indica il risultato della query
     */
	public int statistics9(String email){
		int returned=0;
		PreparedStatement statistics9_stmt=null;
		ResultSet statistics9_exc=null;
		try {
			statistics9_stmt = conn.prepareStatement(" select count(*) from manche natural join partecipazione where partecipazione.email=? and tipo=0");
			statistics9_stmt.setString(1,email);
			statistics9_exc = statistics9_stmt.executeQuery();
			if(statistics9_exc.isBeforeFirst()){
				statistics9_exc.next();
				returned=statistics9_exc.getInt(1);
			}else{
				returned=0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(statistics9_exc!=null){
				try {
					statistics9_exc.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(statistics9_stmt!=null){
				try {
					statistics9_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			disconnect();
		}
		return returned;
	}

	/**
     * metodo usato per mostrare il numero di partite giocate
     * @param email indica l'email dell'utente
     * @return indica il risultato della query
     */
	public int statistics10(String email){
		int returned=0;
		PreparedStatement statistics10_stmt=null;
		ResultSet statistics10_exc=null;
		try {
			statistics10_stmt = conn.prepareStatement("select count(*) from partita inner join partecipazione on partita.idpartita=partecipazione.idpartita where partecipazione.email=? and tipo=0");
			statistics10_stmt.setString(1,email);
			statistics10_exc = statistics10_stmt.executeQuery();
			if(statistics10_exc.isBeforeFirst()){
				statistics10_exc.next();
				returned=statistics10_exc.getInt(1);
			}else{
				returned=0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(statistics10_exc!=null){
				try {
					statistics10_exc.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(statistics10_stmt!=null){
				try {
					statistics10_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			disconnect();
		}
		return returned;
	}

	/**
     * metodo usato per mostrare il numero di manche osservate
     * @param email indica l'email dell'utente
     * @return indica il risultato della query
     */
	public int statistics11(String email){
		int returned=0;
		PreparedStatement statistics11_stmt=null;
		ResultSet statistics11_exc=null;
		try {
			statistics11_stmt = conn.prepareStatement("select count(*) from manche natural join partecipazione where partecipazione.email=? and (tipo=1 or tipo=2)");
			statistics11_stmt.setString(1,email);
			statistics11_exc = statistics11_stmt.executeQuery();
			if(statistics11_exc.isBeforeFirst()){
				statistics11_exc.next();
				returned=statistics11_exc.getInt(1);
			}else{
				returned=0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(statistics11_exc!=null){
				try {
					statistics11_exc.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(statistics11_stmt!=null){
				try {
					statistics11_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			disconnect();
		}
		return returned;
	}

	/**
     * metodo usato per mostrare il numero di partite osservate
     * @param email indica l'email dell'utente
     * @return indica il risultato della query
     */
	public int statistics12(String email){
		int returned=0;
		PreparedStatement statistics12_stmt=null;
		ResultSet statistics12_exc=null;
		try {
			statistics12_stmt = conn.prepareStatement("select count(*) from partita inner join partecipazione on partita.idpartita=partecipazione.idpartita where partecipazione.email=? and (tipo=1 or tipo=2)");
			statistics12_stmt.setString(1,email);
			statistics12_exc = statistics12_stmt.executeQuery();
			if(statistics12_exc.isBeforeFirst()){
				statistics12_exc.next();
				returned=statistics12_exc.getInt(1);
			}else{
				returned=0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(statistics12_exc!=null){
				try {
					statistics12_exc.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(statistics12_stmt!=null){
				try {
					statistics12_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			disconnect();
		}
		return returned;
	}

	/**
     * metodo usato per mostrare il numero di manche vinte
     * @param email indica l'email dell'utente
     * @return indica il risultato della query
     */
	public int statistics13(String email){
		int returned=0;
		PreparedStatement statistics13_stmt=null;
		ResultSet statistics13_exc=null;
		try {
			statistics13_stmt = conn.prepareStatement("select count(*) from manche where manche.vincitore=?");
			statistics13_stmt.setString(1,email);
			statistics13_exc = statistics13_stmt.executeQuery();
			if(statistics13_exc.isBeforeFirst()){
				statistics13_exc.next();
				returned=statistics13_exc.getInt(1);
			}else{
				returned=0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(statistics13_exc!=null){
				try {
					statistics13_exc.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(statistics13_stmt!=null){
				try {
					statistics13_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			disconnect();
		}
		return returned;
	}

	/**
     * metodo usato per mostrare il numero di partite vinte
     * @param email indica l'email dell'utente
     * @return indica il risultato della query
     */
	public int statistics14(String email){
		int returned=0;
		PreparedStatement statistics14_stmt=null;
		ResultSet statistics14_exc=null;
		try {
			statistics14_stmt = conn.prepareStatement("select count(*) from partita where partita.winner=?");
			statistics14_stmt.setString(1,email);
			statistics14_exc = statistics14_stmt.executeQuery();
			if(statistics14_exc.isBeforeFirst()){
				statistics14_exc.next();
				returned=statistics14_exc.getInt(1);
			}else{
				returned=0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(statistics14_exc!=null){
				try {
					statistics14_exc.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(statistics14_stmt!=null){
				try {
					statistics14_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			disconnect();
		}
		return returned;
	}

	/**
     * metodo usato per mostrare il numero di manche giocate
     * @param email indica l'email dell'utente
     * @return indica il risultato della query
     */
	public int statistics15(String email){
		int returned=0;
		PreparedStatement statistics15_stmt=null;
		ResultSet statistics15_exc=null;
		try {
			statistics15_stmt = conn.prepareStatement("select round(avg(score),2) from (select sum(punteggio) as score,idpartita,vincitore,nome from manche inner join utente on manche.vincitore=utente.email	where vincitore=? group by(idpartita,vincitore,nome)) as sum_score");
			statistics15_stmt.setString(1,email);
			statistics15_exc = statistics15_stmt.executeQuery();
			if(statistics15_exc.isBeforeFirst()){
				statistics15_exc.next();
				returned=statistics15_exc.getInt(1);
			}else{
				returned=0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(statistics15_exc!=null){
				try {
					statistics15_exc.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(statistics15_stmt!=null){
				try {
					statistics15_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			disconnect();
		}
		return returned;
	}

	 /**
     * metodo usato per mostrare il numero medio di passaggio turno per manche
     * @param email indica l'email dell'utente
	 * @return indica il numero medio di passaggio di turno per manche
     */
	public int statistics16(String email){
		int returned=0;
		PreparedStatement statistics16_stmt=null;
		ResultSet statistics16_exc=null;
		try {
			statistics16_stmt = conn.prepareStatement("select round(avg(ntime),2) from (select count(*) as ntime,idpartita,idmanche,idutente from mossa where nome='CT' and idutente=? group by (idpartita,idmanche,idutente)) as count_ct");
			statistics16_stmt.setString(1,email);
			statistics16_exc = statistics16_stmt.executeQuery();
			if(statistics16_exc.isBeforeFirst()){
				statistics16_exc.next();
				returned=statistics16_exc.getInt(1);
			}else{
				returned=0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(statistics16_exc!=null){
				try {
					statistics16_exc.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(statistics16_stmt!=null){
				try {
					statistics16_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			disconnect();
		}
		return returned;
	}

	/**
     * metodo usato per mostrare il numero medio di volte, per manche e partita, che ha dovuto cedere il turno di gioco
     * @param email indica l'email dell'utente
     * @return indica il risultato della query
     */
	public int statistics17(String email){
		int returned=0;
		PreparedStatement statistics17_stmt=null;
		ResultSet statistics17_exc=null;
		try {
			statistics17_stmt = conn.prepareStatement("select round(avg(ntime),2) from (select count(*) as ntime,idpartita,idutente from mossa where nome='CT' and idutente=? group by (idpartita,idutente)) as count_ct");
			statistics17_stmt.setString(1,email);
			statistics17_exc = statistics17_stmt.executeQuery();
			if(statistics17_exc.isBeforeFirst()){
				statistics17_exc.next();
				returned=statistics17_exc.getInt(1);
			}else{
				returned=0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(statistics17_exc!=null){
				try {
					statistics17_exc.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}

			if(statistics17_stmt!=null){
				try {
					statistics17_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			disconnect();
		}
		return returned;
	}

	/**
     * metodo usato per mostrare il numero medio di volte, per manche che perso tutto
     * @param email indica l'email dell'utente
     * @return indica il risultato della query
     */
	public int statistics18(String email){
		int returned=0;
		PreparedStatement statistics18_stmt=null;
		ResultSet statistics18_exc=null;
		try {
			statistics18_stmt = conn.prepareStatement("select round(avg(ntime),2) from (select count(*) as ntime,idpartita,idmanche,idutente from mossa where nome='Perde' and idutente=? group by (idpartita,idmanche,idutente)) as count_lose");
			statistics18_stmt.setString(1,email);
			statistics18_exc = statistics18_stmt.executeQuery();
			if(statistics18_exc.isBeforeFirst()){
				statistics18_exc.next();
				returned=statistics18_exc.getInt(1);
			}else{
				returned=0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(statistics18_exc!=null){
				try {
					statistics18_exc.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			if(statistics18_stmt!=null){
				try {
					statistics18_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			disconnect();
		}
		return returned;
	}

	/**
     * metodo usato per mostrare il numero medio di volte, per partita che perso tutto
     * @param email indica l'email dell'utente
     * @return indica il risultato della query
     */
	public int statistics19(String email){
		int returned=0;
		PreparedStatement statistics19_stmt=null;
		ResultSet statistics19_exc=null;
		try {
			statistics19_stmt = conn.prepareStatement("select round(avg(ntime),2) from (select count(*) as ntime,idpartita,idutente from mossa where nome='Perde' and idutente=? group by (idpartita,idutente)) as count_lose");
			statistics19_stmt.setString(1,email);
			statistics19_exc = statistics19_stmt.executeQuery();
			if(statistics19_exc.isBeforeFirst()){
				statistics19_exc.next();
				returned=statistics19_exc.getInt(1);
			}else{
				returned=0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(statistics19_exc!=null){
				try {
					statistics19_exc.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}

			if(statistics19_stmt!=null){
				try {
					statistics19_stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}}
			disconnect();
		}
		return returned;
	}

}
