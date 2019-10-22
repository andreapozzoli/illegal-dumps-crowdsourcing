package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import beans.Manager;
import beans.Utente;

public class ManagerDAO {
private Connection con;
	
	public ManagerDAO(Connection con) {
		this.con=con;
	}
	
	public void creaManager(String nome, String password, String email) throws SQLException {
		String query="INSERT into manager (nomeUtente,password,email) VALUES(?,?,?)";
		int code = 0;
		PreparedStatement pstatement = null;
		try {
			pstatement = con.prepareStatement(query);
			pstatement.setString(1, nome);
			pstatement.setString(2, password);
			pstatement.setString(3, email);
			code = pstatement.executeUpdate();
		} catch (SQLException e) {
			throw new SQLException(e);
		} 		
		pstatement.close();
	}
	
	public Manager getManager(int idManager) throws SQLException {
		String query="SELECT * FROM manager WHERE idmanager=?";
		ResultSet result = null;
		PreparedStatement pstatement = null;
		Manager user=null;
		try {
			pstatement = con.prepareStatement(query);
			pstatement.setInt(1, idManager);
			result = pstatement.executeQuery();
			if (result.next()) {
				user=new Manager(idManager,result.getString("nomeUtente"),result.getString("password"),result.getString("email"));
			}

		} catch (SQLException e) {
			throw new SQLException(e);
		} 

		result.close();
		
		pstatement.close();
		return user;
	}
	
	public void aggiornaManager(int id,String nome, String password, String email) throws SQLException {
		String query="UPDATE manager SET nomeUtente=?,password=?,email=? WHERE idmanager=?";
		int code = 0;
		PreparedStatement pstatement = null;
		try {
			pstatement = con.prepareStatement(query);
			pstatement.setString(1, nome);
			pstatement.setString(2, password);
			pstatement.setString(3, email);
			pstatement.setInt(4, id);			
			code = pstatement.executeUpdate();
		} catch (SQLException e) {
			throw new SQLException(e);
		} 
		
		
		pstatement.close();
	}

}
