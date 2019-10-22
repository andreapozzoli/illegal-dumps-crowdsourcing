package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import beans.Utente;

public class UtenteDAO {
	private Connection con;

	public UtenteDAO(Connection con) {
		this.con=con;
	}

	public Utente checkUtente(String nome,String password) throws SQLException {
		String query="(SELECT idlavoratore AS id,nomeUtente,password,email,tipo FROM lavoratore WHERE nomeUtente=? and password=?) UNION (SELECT idmanager AS id,nomeUtente,password,email,tipo FROM manager WHERE nomeUtente=? and password=?)";
		ResultSet result = null;
		PreparedStatement pstatement = null;
		Utente utente=null;
		try {
			pstatement = con.prepareStatement(query);
			pstatement.setString(1, nome);
			pstatement.setString(2, password);
			pstatement.setString(3, nome);
			pstatement.setString(4, password);
			result = pstatement.executeQuery();
			if (result.next()) {
				utente=new Utente(result.getInt("id"),result.getString("nomeUtente"),result.getString("password"),result.getString("email"),result.getString("tipo"));
			}

		} catch (SQLException e) {
			throw new SQLException(e);
		} 

		
		pstatement.close();
		return utente;

	}

	public String checkNome(String nome, String email) throws SQLException {
		String query="(SELECT nomeUtente FROM lavoratore WHERE nomeUtente=?) UNION (SELECT nomeUtente FROM manager WHERE nomeUtente=?)";
		ResultSet result = null;
		PreparedStatement pstatement = null;
		String presente="";
		try {
			pstatement = con.prepareStatement(query);
			pstatement.setString(1, nome);
			pstatement.setString(2, nome);
			result = pstatement.executeQuery();
			if (result.next()) {
				presente="nome";
			}

		} catch (SQLException e) {
			throw new SQLException(e);
		} 

		String query2="(SELECT email FROM lavoratore WHERE email=?) UNION (SELECT email FROM manager WHERE email=?)";
		try {
			pstatement = con.prepareStatement(query2);
			pstatement.setString(1, email);
			pstatement.setString(2, email);
			result = pstatement.executeQuery();
			if (result.next()) {
				presente=presente+"email";
			}

		} catch (SQLException e) {
			throw new SQLException(e);
		} 
		
		
		
		result.close();
		
		pstatement.close();
		return presente;
	}
	
	public boolean checkNome(int id, String nome, String email) throws SQLException {
		String query="(SELECT idlavoratore as id, nomeUtente, email FROM lavoratore WHERE nomeUtente=? OR email=?) UNION (SELECT idmanager as id, nomeUtente, email FROM manager WHERE nomeUtente=? OR email=?)";
		ResultSet result = null;
		PreparedStatement pstatement = null;
		boolean presente=false;
		try {
			pstatement = con.prepareStatement(query);
			pstatement.setString(1, nome);
			pstatement.setString(2, email);
			pstatement.setString(3, nome);
			pstatement.setString(4, email);
			result = pstatement.executeQuery();
			if (result.next()) {
				if (!(id==result.getInt("id"))) {
				presente=true;
				}
				else if (result.next()) {
					presente=true;
				}
			}

		} catch (SQLException e) {
			throw new SQLException(e);
		} 

		result.close();
		
		pstatement.close();
		return presente;
	}
	
	public Utente getUtente(String nome,String password) throws SQLException {
		String query="(SELECT idlavoratore AS id,email,tipo FROM lavoratore WHERE nomeUtente=? and password=?) UNION (SELECT idmanager AS id,email,tipo FROM manager WHERE nomeUtente=? and password=?)";
		ResultSet result = null;
		PreparedStatement pstatement = null;
		Utente user=null;
		try {
			pstatement = con.prepareStatement(query);
			pstatement.setString(1, nome);
			pstatement.setString(2, password);
			pstatement.setString(3, nome);
			pstatement.setString(4, password);
			result = pstatement.executeQuery();
			if (result.next()) {
				user=new Utente(result.getInt("id"),nome,password,result.getString("email"),result.getString("tipo"));
			}

		} catch (SQLException e) {
			throw new SQLException(e);
		} 

		result.close();
		
		pstatement.close();
		return user;
	}

}
