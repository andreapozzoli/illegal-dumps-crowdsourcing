package DAO;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import beans.Immagine;

public class ImmagineDAO {

	private Connection con;

	public ImmagineDAO(Connection con) {
		this.con=con;
	}

	public void creaImmagine (InputStream foto,String provenienza, String data, String risoluzione, int idlocalita) throws SQLException {
		if (provenienza.equals("") || data.equals("") || risoluzione.equals("") || provenienza==null || data==null || risoluzione==null) {

		}else {
			String query = "INSERT into immagine (foto,provenienza, datarecupero, risoluzione, idlocalita) VALUES(?,?, ?, ?, ?)";
			int code = 0;
			PreparedStatement pstatement = null;

			pstatement = con.prepareStatement(query);
			pstatement.setBlob(1, foto);
			pstatement.setString(2, provenienza);
			if (data!=null && !(data.equals(""))) {
				Calendar c = Calendar.getInstance();
		        c.setTime(java.sql.Date.valueOf(data));
		        c.add(Calendar.DATE, 1);
		        java.sql.Date datasql = new java.sql.Date(c.getTimeInMillis());
				pstatement.setDate(3, datasql);
			}
			else {
				pstatement.setDate(3, null);
			}
			pstatement.setString(4, risoluzione);
			pstatement.setInt(5, idlocalita);
			code = pstatement.executeUpdate();

			pstatement.close();
		}

	}

	public Date creaData(String s) {
		int anno=Integer.parseInt(s.substring(0,3));
		int mese=Integer.parseInt(s.substring(5,6));
		int giorno=Integer.parseInt(s.substring(8,9));
		Calendar calendar=Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"),Locale.ITALY);
		calendar.set(anno, mese, giorno);
		Date data = calendar.getTime();
		return data;
	}

	public int ultimoId() throws SQLException {
		String query="SELECT DISTINCT LAST_INSERT_ID() FROM immagine";
		PreparedStatement pstatement=null;
		ResultSet result=null;
		pstatement=con.prepareStatement(query);
		result=pstatement.executeQuery();
		int id=0;
		if(result.next())
			id=result.getInt("idimmagine");
		result.close();
		pstatement.close();
		return id;
	}

	public Immagine getImmagine(int id) throws SQLException {
		Immagine img=new Immagine();
		String query="SELECT * FROM immagine WHERE idimmagine=?";
		PreparedStatement pstat=null;
		ResultSet result=null;

		pstat=con.prepareStatement(query);
		pstat.setInt(1, id);
		result=pstat.executeQuery();
		if(result.next()) {
			byte[] imgData = result.getBytes("foto");
			String encodedImg=Base64.getEncoder().encodeToString(imgData);
			img.setFoto(encodedImg);			
			img.setId(result.getInt("idimmagine"));
			img.setIdlocalita(result.getInt("idlocalita"));
			img.setProvenienza(result.getString("provenienza"));
			img.setData(result.getDate("datarecupero"));
			img.setRisoluzione(result.getString("risoluzione"));
		}
		else {
			img=null;
		}

		result.close();
		pstat.close();

		return img;

	}

	public ArrayList<Immagine> getConflitti(int idCampagna) throws SQLException {
		ArrayList<Immagine> confs=new ArrayList<Immagine>();
		String query="SELECT * FROM immagine WHERE idimmagine IN(SELECT A1.idimmagine FROM annotazione AS A1 RIGHT JOIN annotazione AS A2 ON A1.idimmagine=A2.idimmagine and A1.validita<>A2.validita WHERE A1.idimmagine IN (SELECT d.idimmagine FROM localitacampagna AS c JOIN immagine AS d ON d.idlocalita=c.idlocalita WHERE c.idcampagna=?))";
		PreparedStatement pstat=null;
		ResultSet result=null;
		pstat=con.prepareStatement(query);
		pstat.setInt(1, idCampagna);
		result=pstat.executeQuery();
		while(result.next()) {
			Immagine conf=new Immagine();
			conf.setId(result.getInt("idimmagine"));
			byte[] imgData = result.getBytes("foto");
			String encodedImg=Base64.getEncoder().encodeToString(imgData);
			conf.setFoto(encodedImg);	
			conf.setProvenienza(result.getString("provenienza"));
			conf.setData(result.getDate("datarecupero"));
			conf.setRisoluzione(result.getString("risoluzione"));
			conf.setIdlocalita(result.getInt("idlocalita"));
			confs.add(conf);
		}

		result.close();
		pstat.close();

		return confs;

	}

	public ArrayList<Immagine> getElencoImmagini(int id) throws SQLException{
		ArrayList<Immagine> imgs=new ArrayList<Immagine>();
		String query ="SELECT * FROM immagine WHERE idlocalita=?";
		PreparedStatement pstatement=null;
		ResultSet result=null;
		pstatement=con.prepareStatement(query);
		pstatement.setInt(1, id);
		result=pstatement.executeQuery();
		while(result.next()) {
			Immagine img=new Immagine();
			img.setId(result.getInt("idimmagine"));
			byte[] imgData = result.getBytes("foto");
			if (imgData!=null) {
				String encodedImg=Base64.getEncoder().encodeToString(imgData);
				img.setFoto(encodedImg);
			}
			img.setProvenienza(result.getString("provenienza"));
			img.setData(result.getDate("datarecupero"));
			img.setRisoluzione(result.getString("risoluzione"));
			img.setIdlocalita(result.getInt("idlocalita"));
			imgs.add(img);
		}

		result.close();
		pstatement.close();

		return imgs;

	}


}
