package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import DAO.UtenteDAO;
import beans.Localita;
import beans.Utente;

@WebServlet("/ControlloLogin")
public class ControlloLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection con=null;
	
    public ControlloLogin() {
        super();
    }
    
    public void init() throws ServletException {
		try {
			ServletContext context = getServletContext();
			String driver = context.getInitParameter("dbDriver");
			//String url = context.getInitParameter("dbUrl");
			final String DB_URL = "jdbc:mysql://localhost:3306/dbtest?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
			String user = context.getInitParameter("dbUser");
			String password = context.getInitParameter("dbPassword");
			Class.forName(driver);
			con = DriverManager.getConnection(DB_URL, user, password);

		} catch (ClassNotFoundException e) {
			throw new UnavailableException("Can't load database driver");
		} catch (SQLException e) {
			throw new UnavailableException("Couldn't get db connection");
		}
	}
	
	
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
	    String password = request.getParameter("password");
	    String risultato = "ko";
String risultato2="";
		if (username == null || password == null) {
			String path = getServletContext().getContextPath()+"/ApriLogin";
			response.sendRedirect(path);
		}else {
		UtenteDAO userDAO = new UtenteDAO(con);
		try {
			Utente user = userDAO.checkUtente(username, password);
			if (user != null) {
				HttpSession session = request.getSession();
				session.setAttribute("currentUser", user);
				risultato = user.getTipo();
			
			}
			
		} catch (SQLException e) {
		risultato2="errore";
		}
if (risultato2.equals("")) {
		writeResponse(response, risultato);
}
else {
	writeResponse(response, risultato2);

}}
	
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	
	
	private void writeResponse(HttpServletResponse response,String risultato) throws IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
	
		response.getWriter().write(risultato);
	}
	
	
	public void destroy() {
		try {
			if (con != null) {
				con.close();
			}
		} catch (SQLException sqle) {
		}
	}

}
