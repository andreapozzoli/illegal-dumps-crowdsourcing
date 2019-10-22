package DAO;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Base64;

import beans.Lavoratore;
import beans.Manager;

public class LavoratoreDAO {
	private Connection con;

	public LavoratoreDAO(Connection con) {
		this.con=con;
	}

	public void creaLavoratore(String nome, String password, String email, String esperienza, InputStream foto) throws SQLException {
		String query="INSERT into lavoratore (nomeUtente,password,email,esperienza,foto) VALUES(?,?,?,?,?)";
		int code = 0;
		PreparedStatement pstatement = null;
		try {
			pstatement = con.prepareStatement(query);
			pstatement.setString(1, nome);
			pstatement.setString(2, password);
			pstatement.setString(3, email);
			pstatement.setString(4, esperienza);
			pstatement.setBlob(5, foto);
			code = pstatement.executeUpdate();
		} catch (SQLException e) {
			throw new SQLException(e);
		} 

		pstatement.close();
	}

	public Lavoratore getLavoratore(int idLavoratore) throws SQLException {
		String query="SELECT * FROM lavoratore WHERE idlavoratore=?";
		ResultSet result = null;
		PreparedStatement pstatement = null;
		Lavoratore user=null;
		try {
			pstatement = con.prepareStatement(query);
			pstatement.setInt(1, idLavoratore);
			result = pstatement.executeQuery();
			if (result.next()) {
				byte[] imgData = result.getBytes("foto");
				String fotostr=null;
				if (imgData!=null)
				fotostr=Base64.getEncoder().encodeToString(imgData);
				
				user=new Lavoratore(idLavoratore,result.getString("nomeUtente"),result.getString("password"),result.getString("email"),result.getString("esperienza"),fotostr);
			}

		} catch (SQLException e) {
			throw new SQLException(e);
		} 

		result.close();

		pstatement.close();
		return user;
	}

	public void aggiornaLavoratore(int id,String nome, String password, String email, String esperienza, String foto) throws SQLException, IOException {
		String query="UPDATE lavoratore SET nomeUtente=?,password=?,email=?, esperienza=?,foto=? WHERE idlavoratore=?";
		int code = 0;
		PreparedStatement pstatement = null;
		try {
			pstatement = con.prepareStatement(query);
			pstatement.setString(1, nome);
			pstatement.setString(2, password);
			pstatement.setString(3, email);
			pstatement.setString(4, esperienza);

			InputStream fotoblob = null;
				File fi = new File(foto);
				byte[] fileContent ;

				
					try {

						fileContent = Files.readAllBytes(fi.toPath());
					}
					catch(java.nio.file.NoSuchFileException e) {
						String per = "C:/Users/Matteo/Desktop/immagini progetto web/"+fi.getName(); // C:/Users/Matteo/Desktop/immagini progetto web/
						Path p = Paths.get(per);                                                   // C:/Users/semmo/Desktop/immagini progetto web/
						fileContent = Files.readAllBytes(p);
					}
				
				fotoblob = new ByteArrayInputStream(fileContent);
			
			pstatement.setBlob(5, fotoblob);
			pstatement.setInt(6, id);			
			code = pstatement.executeUpdate();
		} catch (SQLException e) {
			throw new SQLException(e);
		} 

		pstatement.close();

	}
	
	
	public void aggiornaLavoratoreNoFoto(int id,String nome, String password, String email, String esperienza) throws SQLException, IOException {
		String query="UPDATE lavoratore SET nomeUtente=?,password=?,email=?, esperienza=? WHERE idlavoratore=?";
		int code = 0;
		PreparedStatement pstatement = null;
		try {
			pstatement = con.prepareStatement(query);
			pstatement.setString(1, nome);
			pstatement.setString(2, password);
			pstatement.setString(3, email);
			pstatement.setString(4, esperienza);

			pstatement.setInt(5, id);			
			code = pstatement.executeUpdate();
		} catch (SQLException e) {
			throw new SQLException(e);
		} 

		pstatement.close();

	}
	
	
	

}
