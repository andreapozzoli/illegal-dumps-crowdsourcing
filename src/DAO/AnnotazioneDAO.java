package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;

import beans.Annotazione;


public class AnnotazioneDAO {
	
	private Connection con;

	public AnnotazioneDAO(Connection con) {
		this.con=con;
	}

	public void creaAnnotazione (String validita, String fiducia, String note,int idLavoratore, int idImmagine) throws SQLException {
		String query = "INSERT into annotazione (validita, fiducia, note, datacreazione,idlavoratore,idimmagine) VALUES(?,?, ?, ?, ?,?)";
		int code = 0;
		PreparedStatement pstatement = null;
		pstatement = con.prepareStatement(query);
		pstatement.setString(1, validita);
		pstatement.setString(2, fiducia);
		pstatement.setString(3, note);
		
		Calendar c = Calendar.getInstance();
        c.setTime(java.sql.Date.valueOf(java.time.LocalDate.now()));
        c.add(Calendar.DATE, 1);
        java.sql.Date datasql = new java.sql.Date(c.getTimeInMillis());
        pstatement.setDate(4, datasql);
		
		
		pstatement.setInt(5, idLavoratore);
		pstatement.setInt(6, idImmagine);

		code = pstatement.executeUpdate();

		pstatement.close();

	}

	


	public ArrayList<Annotazione> getElencoAnnotazioni(int idImmagine) throws SQLException{
		ArrayList<Annotazione> anns=new ArrayList<Annotazione>();
		String query ="SELECT * FROM annotazione WHERE idimmagine=?";
		PreparedStatement pstatement=null;
		ResultSet result=null;
		pstatement=con.prepareStatement(query);
		pstatement.setInt(1, idImmagine);
		result=pstatement.executeQuery();
		while(result.next()) {
			Annotazione ann=new Annotazione();
			ann.setId(result.getInt("idannotazione"));
			ann.setValidita(result.getString("validita"));
			ann.setFiducia(result.getString("fiducia"));
			ann.setNote(result.getString("note"));
			ann.setData(result.getDate("datacreazione"));
			ann.setIdimmagine(result.getInt("idimmagine"));
			ann.setIdlavoratore(result.getInt("idlavoratore"));
			anns.add(ann);
		}
		result.close();
		pstatement.close();

		return anns;

	}

	public boolean presenzaAnnotazione(int idLavoratore,int idImmagine) throws SQLException{
		String query ="SELECT * FROM annotazione WHERE idimmagine=? AND idlavoratore=?";
		PreparedStatement pstatement=null;
		ResultSet result=null;
		boolean presente=false;
		pstatement=con.prepareStatement(query);
		pstatement.setInt(1, idImmagine);
		pstatement.setInt(2, idLavoratore);
		result=pstatement.executeQuery();
		if(result.next()) {
			presente = true;
		}

		result.close();
		pstatement.close();

		return presente;

	}
	

}
