package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

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
import com.google.gson.JsonParser;

import DAO.AnnotazioneDAO;
import DAO.ImmagineDAO;
import DAO.LocalitaDAO;
import beans.Annotazione;
import beans.Localita;
import beans.Utente;


@WebServlet("/SetAnnotazioniConflittuali")
public class SetAnnotazioniConflittuali extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection con=null;       


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
       
    
    public SetAnnotazioniConflittuali() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
String risultato="{ \"name\": \"ok\"}";
		HttpSession session = request.getSession(true);

		Utente user=(Utente) session.getAttribute("currentUser");
		if (user == null ) {
			String path = getServletContext().getContextPath();
			response.sendRedirect(path);
		}
		else {
		
		AnnotazioneDAO aDAO=new AnnotazioneDAO(this.con);

		int idImmagine=Integer.parseInt(request.getParameter("idimmagine"));
		
		ArrayList<Annotazione> annotazioniConflittuali = new ArrayList<Annotazione>();
		
		try {

			annotazioniConflittuali = aDAO.getElencoAnnotazioni(idImmagine);
			
		} catch (SQLException e) {
risultato="{ \"name\": \"ko\"}";	
}
		writeResponse(response, annotazioniConflittuali, risultato);
		}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	
	private void writeResponse(HttpServletResponse response,ArrayList<Annotazione> annotazioniConflittuali, String risultato) throws IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");

		JsonObject jo = new JsonObject();
		JsonObject risultatoconv = new JsonParser().parse(risultato).getAsJsonObject();
		// ArrayList to JsonArray
		JsonArray jaB = new Gson().toJsonTree(annotazioniConflittuali).getAsJsonArray();
		jo.add("jaB", jaB);
		jo.add("ris", risultatoconv);
		response.getWriter().write(jo.toString());
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
