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

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.corba.se.impl.orbutil.ObjectWriter;

import DAO.AnnotazioneDAO;
import DAO.ImmagineDAO;
import DAO.LocalitaDAO;
import beans.Localita;
import beans.Utente;
import beans.Immagine;

@WebServlet("/setLocalita")
public class SetLocalita extends HttpServlet {
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
String risultato="{ \"name\": \"ok\"}";
		Utente user=(Utente) session.getAttribute("currentUser");
		if (user == null ) {
			String path = getServletContext().getContextPath();
			response.sendRedirect(path);
		}
		else {

		String tipoUtente=request.getParameter("tipoUtente");
		LocalitaDAO lDAO=new LocalitaDAO(this.con);
		ImmagineDAO iDAO=new ImmagineDAO(this.con);
		AnnotazioneDAO aDAO=new AnnotazioneDAO(this.con);
		int idCampagna=Integer.parseInt(request.getParameter("idcampagna"));
		ArrayList<Localita> localita=null;
		try {

			localita=lDAO.getElencoLocalita(idCampagna);
				for(int i=0;i<localita.size();i++) {
					localita.get(i).setImmagini(iDAO.getElencoImmagini(localita.get(i).getId()));
				}
				if (tipoUtente.equals("manager")) {
				for(int i=0;i<localita.size();i++) {
					for(int j=0;j<localita.get(i).getImmagini().size();j++) {
						localita.get(i).getImmagini().get(j).setAnnotazioni(aDAO.getElencoAnnotazioni(localita.get(i).getImmagini().get(j).getId()));
					}
				}
			}
		} catch (SQLException e) {
			risultato="{ \"name\": \"ko\"}";
		}
		writeResponse(response, localita, risultato);
		}
	}

	private void writeResponse(HttpServletResponse response,ArrayList<Localita> loc, String risultato) throws IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");


		JsonObject jo = new JsonObject();
		JsonObject risultatoconv = new JsonParser().parse(risultato).getAsJsonObject();

		// ArrayList to JsonArray
		JsonArray jaB = new Gson().toJsonTree(loc).getAsJsonArray();
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
