package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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

import DAO.AnnotazioneDAO;
import DAO.CampagnaDAO;
import beans.Localita;
import beans.Utente;

@WebServlet("/CreaAnnotazione")
public class CreaAnnotazione extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection con=null;

	public CreaAnnotazione() {
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
		String ris="ok";
		HttpSession session = request.getSession(false);
		Utente user=(Utente) session.getAttribute("currentUser");
		if (user == null ) {
			String path = getServletContext().getContextPath();
			response.sendRedirect(path);
		}
		else {
		if (user.getTipo().contentEquals("lavoratore")) {

		int idLavoratore = ((Utente) session.getAttribute("currentUser")).getId();

		String val= request.getParameter("validita");
		String fiducia=request.getParameter("fiducia");
		String note=request.getParameter("note");
		int idImmagine=Integer.parseInt(request.getParameter("idimmagine"));
		if (val == null || fiducia == null || note==null) {
			String path = getServletContext().getContextPath()+"/ApriHomeLavoratore";
			response.sendRedirect(path);
		}
		else {

		AnnotazioneDAO aDAO=new AnnotazioneDAO(this.con);		

		try {
			aDAO.creaAnnotazione(val, fiducia, note, idLavoratore,idImmagine);
		} catch (SQLException e) {
			ris="ko";
		}

		
		writeResponse(response, ris);
		
	}
		}
	else {

		String path = getServletContext().getContextPath()+"/ApriHomeManager";
		response.sendRedirect(path);
	}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void writeResponse(HttpServletResponse response, String ris) throws IOException {
		response.getWriter().write(ris);
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
