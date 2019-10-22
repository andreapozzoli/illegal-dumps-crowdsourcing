package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


import beans.Localita;


public class LocalitaDAO {

	private Connection con;

	public LocalitaDAO(Connection con) {
		this.con=con;
	}

	public void creaLocalita (Float latitudine, Float longitudine, String nome, String comune, String regione, int idcampagna) throws SQLException {
		String query = "INSERT into localita (latitudine, longitudine, nome, comune, regione) VALUES(?, ?, ?, ?, ?)" ;
		int code = 0;
		PreparedStatement pstatement = null;

		pstatement = con.prepareStatement(query);
		pstatement.setFloat(1, latitudine);
		pstatement.setFloat(2, longitudine);
		pstatement.setString(3, nome);
		pstatement.setString(4, comune);
		pstatement.setString(5, regione);
		code = pstatement.executeUpdate();


		pstatement.close();

	}

	public void aggiungiloccamp(int idloc, int idcamp) throws SQLException {
		String query = "INSERT into localitacampagna (idcampagna,idlocalita) VALUES(?,?)";
		int code = 0;
		PreparedStatement pstatement = null;


		pstatement = con.prepareStatement(query);

		pstatement.setInt(1, idcamp);
		pstatement.setInt(2, idloc);

		code = pstatement.executeUpdate();

		pstatement.close();

	}

	public int ultimoId() throws SQLException {
		String query="SELECT DISTINCT LAST_INSERT_ID() AS idlocalita FROM localita";
		PreparedStatement pstatement=null;
		ResultSet result=null;
		pstatement=con.prepareStatement(query);
		result=pstatement.executeQuery();
		int id=0;
		if(result.next())
			id=result.getInt("idlocalita");
		result.close();
		pstatement.close();
		return id;
	}

	public Localita getLocalita(int id) throws SQLException {
		Localita loc=new Localita();
		String query="SELECT * FROM localita WHERE idlocalita=?";
		PreparedStatement pstat=null;
		ResultSet result=null;

		pstat=con.prepareStatement(query);
		pstat.setInt(1, id);
		result=pstat.executeQuery();
		if(result.next()) {
			loc.setId(result.getInt("idlocalita"));
			loc.setLatitudine(result.getFloat("latitudine"));
			loc.setLongitudine(result.getFloat("longitudine"));
			loc.setNome(result.getString("nome"));
			loc.setComune(result.getString("comune"));
			loc.setRegione(result.getString("regione"));
		}
		else {
			loc=null;
		}

		result.close();
		pstat.close();

		return loc;

	}


	public ArrayList<Localita> getElencoLocalita(int id) throws SQLException{
		ArrayList<Localita> locs=new ArrayList<Localita>();
		String query ="SELECT * FROM localita JOIN localitacampagna ON localita.idlocalita=localitacampagna.idlocalita WHERE idcampagna=?";
		PreparedStatement pstatement=null;
		ResultSet result=null;
		pstatement=con.prepareStatement(query);
		pstatement.setInt(1, id);
		result=pstatement.executeQuery();
		while(result.next()) {
			Localita loc=new Localita();
			loc.setId(result.getInt("idlocalita"));
			loc.setLatitudine(result.getFloat("latitudine"));
			loc.setLongitudine(result.getFloat("longitudine"));
			loc.setNome(result.getString("nome"));
			loc.setComune(result.getString("comune"));
			loc.setRegione(result.getString("regione"));
			locs.add(loc);
		}

		result.close();
		pstatement.close();

		return locs;

	}

}
