package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import beans.Campagna;



public class CampagnaDAO {
	private Connection con;

	public CampagnaDAO(Connection con) {
		this.con=con;
	}

	public void creaCampagna(String nome, String committente, int idmanager) throws SQLException {
		String query="INSERT into campagna (nome,committente,idmanager) VALUES(?,?,?)";
		int code = 0;
		PreparedStatement pstatement = null;
		try {
			pstatement = con.prepareStatement(query);
			pstatement.setString(1, nome);
			pstatement.setString(2, committente);
			pstatement.setInt(3, idmanager);
			code = pstatement.executeUpdate();

		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			try {
				pstatement.close();
			} catch (Exception e1) {

			}
		}
	}

	public boolean controlloAppartenenza(int idcampagna, int idutente) throws SQLException {
		String query="SELECT * FROM campagna WHERE idcampagna=?";
		PreparedStatement pstatement=null;
		ResultSet result=null;

		
			pstatement=con.prepareStatement(query);
			pstatement.setInt(1, idcampagna);
			result=pstatement.executeQuery();
			if (result.next() && result.getInt("idmanager")==idutente) {
				return true;
			}
		
		
		return false;
	}
	
	public boolean controlloOpzione(int idcampagna) throws SQLException {
		String query="SELECT * FROM campagna WHERE stato=\"avviato\" AND idcampagna=?";
		PreparedStatement pstatement=null;
		ResultSet result=null;

		
			pstatement=con.prepareStatement(query);
			pstatement.setInt(1, idcampagna);
			result=pstatement.executeQuery();
			if (result.next()) {
				return true;
			}
		
		
		return false;
	}
	
	
	
	public void aggiornaCampagna(int idCampagna, String stato) throws SQLException {
		String query="UPDATE campagna SET stato=? WHERE idcampagna=?";
		int code = 0;
		PreparedStatement pstatement = null;
		try {
			pstatement = con.prepareStatement(query);
			pstatement.setString(1, stato);
			pstatement.setInt(2, idCampagna);		
			code = pstatement.executeUpdate();
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			try {
				pstatement.close();
			} catch (Exception e1) {

			}
		}
	}

	public int ultimoId() throws SQLException {
		String query="SELECT DISTINCT LAST_INSERT_ID() AS idcampagna FROM campagna";
		PreparedStatement pstatement=null;
		ResultSet result=null;
		pstatement=con.prepareStatement(query);
		result=pstatement.executeQuery();
		int id=0;
		if(result.next())
			id=result.getInt("idcampagna");
		result.close();
		pstatement.close();
		return id;

	}

	public void creaIscrizione(int idcamp, int idlav) throws SQLException {
		String query="INSERT into iscrizione (idcampagna, idlavoratore) VALUES(?,?)";
		int code = 0;
		PreparedStatement pstatement = null;

		pstatement = con.prepareStatement(query);
		pstatement.setInt(1, idcamp);
		pstatement.setInt(2, idlav);
		code = pstatement.executeUpdate();


		pstatement.close();
	}
	
	public boolean getIscrizione(int idcamp, int idlav) throws SQLException {
		String query="SELECT * FROM iscrizione WHERE idcampagna=? AND idlavoratore=?";
		ResultSet result=null;
		PreparedStatement pstatement = null;
		boolean presente=false;

		pstatement = con.prepareStatement(query);
		pstatement.setInt(1, idcamp);
		pstatement.setInt(2, idlav);
		result=pstatement.executeQuery();

		if(result.next()) {
			presente=true;
		}
		pstatement.close();
		return presente;
	}
	
	

	public ArrayList<Campagna> getElencoCampagne(int idManager) throws SQLException{
		ArrayList<Campagna> campagne=new ArrayList<Campagna>();
		String query="SELECT * FROM campagna WHERE idmanager=?";
		PreparedStatement pstatement=null;
		ResultSet result=null;

		try {
			pstatement=con.prepareStatement(query);
			pstatement.setInt(1, idManager);
			result=pstatement.executeQuery();
			while (result.next()) {
				Campagna campagna = new Campagna();
				campagna.setId(result.getInt("idcampagna"));
				campagna.setNome(result.getString("nome"));
				campagna.setCommittente(result.getString("committente"));
				campagna.setStato(result.getString("stato"));
				campagne.add(campagna);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		result.close();

		pstatement.close();
		return campagne;
	}

	public Campagna getCampagna(int idCampagna) throws SQLException{
		Campagna campagna = null;
		String query="SELECT * FROM campagna WHERE idcampagna=?";
		PreparedStatement pstatement=null;
		ResultSet result=null;

		try {
			pstatement=con.prepareStatement(query);
			pstatement.setInt(1, idCampagna);
			result=pstatement.executeQuery();



			if (result.next()) {
				campagna = new Campagna();
				campagna.setId(result.getInt("idcampagna"));
				campagna.setNome(result.getString("nome"));
				campagna.setCommittente(result.getString("committente"));
				campagna.setStato(result.getString("stato"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		result.close();

		pstatement.close();

		return campagna;
	}

	public ArrayList<Campagna> getCampagneNonIscritto(int idLavoratore) throws SQLException{
		ArrayList<Campagna> campagne=new ArrayList<Campagna>();
		String query="SELECT * FROM campagna WHERE stato=\"avviato\" and idcampagna NOT IN (SELECT campagna.idcampagna FROM campagna join iscrizione WHERE campagna.idcampagna=iscrizione.idcampagna and iscrizione.idlavoratore=?)";
		PreparedStatement pstatement=null;
		ResultSet result=null;

		try {
			pstatement=con.prepareStatement(query);
			pstatement.setInt(1, idLavoratore);
			result=pstatement.executeQuery();
			while (result.next()) {
				Campagna campagna = new Campagna();
				campagna.setId(result.getInt("idcampagna"));
				campagna.setNome(result.getString("nome"));
				campagna.setCommittente(result.getString("committente"));
				campagna.setStato(result.getString("stato"));
				campagne.add(campagna);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		result.close();

		pstatement.close();
		return campagne;
	}

	public ArrayList<Campagna> getCampagneIscritto(int idLavoratore) throws SQLException{
		ArrayList<Campagna> campagne=new ArrayList<Campagna>();
		String query="SELECT * FROM campagna WHERE stato=\"avviato\" and idcampagna IN (SELECT campagna.idcampagna FROM campagna join iscrizione WHERE campagna.idcampagna=iscrizione.idcampagna and iscrizione.idlavoratore=?)";
		PreparedStatement pstatement=null;
		ResultSet result=null;

		try {
			pstatement=con.prepareStatement(query);
			pstatement.setInt(1, idLavoratore);
			result=pstatement.executeQuery();
			while (result.next()) {
				Campagna campagna = new Campagna();
				campagna.setId(result.getInt("idcampagna"));
				campagna.setNome(result.getString("nome"));
				campagna.setCommittente(result.getString("committente"));
				campagna.setStato(result.getString("stato"));
				campagne.add(campagna);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		result.close();

		pstatement.close();
		return campagne;
	}

	private int getStatistica(int idCampagna,String query) throws SQLException {
		PreparedStatement pstatement=null;
		ResultSet result=null;
		int statistica=0;
		try {
			pstatement=con.prepareStatement(query);
			pstatement.setInt(1, idCampagna);
			result=pstatement.executeQuery();
			if (result.next()) {
				statistica=result.getInt("num");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		result.close();

		pstatement.close();
		return statistica;

	}

	public int[] getStatistiche(int idCampagna) throws SQLException {
		int[] statistiche=new int[4];
		String[] query=new String[4];
		query[0]="SELECT COUNT(*) AS num FROM localitacampagna WHERE idcampagna=?";
		query[1]="SELECT COUNT(*) AS num FROM localitacampagna AS L JOIN immagine AS I WHERE L.idlocalita=I.idlocalita and L.idcampagna=?";
		query[2]="SELECT COUNT(*) AS num FROM (localitacampagna AS L JOIN immagine AS I) JOIN annotazione AS A WHERE L.idlocalita=I.idlocalita and I.idimmagine=A.idimmagine and L.idcampagna=?";
		query[3]="SELECT COUNT(*) AS num FROM immagine WHERE idimmagine IN(SELECT a.idimmagine FROM annotazione AS a RIGHT JOIN annotazione AS b ON b.idimmagine=a.idimmagine AND b.validita<>a.validita WHERE a.idimmagine IN (SELECT d.idimmagine FROM localitacampagna As c JOIN immagine AS d ON c.idlocalita=d.idlocalita WHERE c.idcampagna=?))";
		for (int i=0;i<4;i++) {
			statistiche[i]=this.getStatistica(idCampagna, query[i]);
		}
		return statistiche;
	}



}
