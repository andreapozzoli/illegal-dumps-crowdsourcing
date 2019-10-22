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

import DAO.AnnotazioneDAO;
import DAO.CampagnaDAO;
import DAO.ImmagineDAO;
import DAO.LocalitaDAO;
import beans.Annotazione;
import beans.Immagine;
import beans.Localita;
import beans.Utente;

@WebServlet("/ApriMappa")
public class ApriMappa extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection con=null;       

	public ApriMappa() {
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
		HttpSession session = request.getSession(false);
		boolean violazione=false;
		Utente user=(Utente) session.getAttribute("currentUser");
		if (user == null ) {
			String path = getServletContext().getContextPath();
			response.sendRedirect(path);
		}
		else {
			request.setAttribute("user", user);
			if(user.getTipo().equals("lavoratore")) {
				CampagnaDAO cDAO=new CampagnaDAO(this.con);
				int idCampagna=Integer.parseInt(request.getParameter("idcampagna"));
				try {
					if(cDAO.controlloOpzione(idCampagna)) {
						violazione=false;


						try {
							if(!(cDAO.getIscrizione(idCampagna, user.getId()))) {
								cDAO.creaIscrizione(idCampagna, user.getId());
							}
						} catch (SQLException e) {
							String path1 = "/WEB-INF/ERRORE.jsp";
							request.setAttribute("user", "lavoratore");
							RequestDispatcher dispatcher = request.getRequestDispatcher(path1);
							dispatcher.forward(request, response);
							return;
						}
					}
					else {
						violazione=true;
					}
				} catch (SQLException e) {
					String path1 = "/WEB-INF/ERRORE.jsp";
					request.setAttribute("user", "lavoratore");
					RequestDispatcher dispatcher = request.getRequestDispatcher(path1);
					dispatcher.forward(request, response);
					return;
				}
			}
			else{
				CampagnaDAO cDAO=new CampagnaDAO(this.con);
				int idCampagna=Integer.parseInt(request.getParameter("idcampagna"));
				try {
					if(cDAO.controlloAppartenenza(idCampagna, user.getId())) {
						violazione=false;
					}
					else {
						violazione=true;
					}
				} catch (SQLException e) {
					String path1 = "/WEB-INF/ERRORE.jsp";
					request.setAttribute("user", "manager");
					RequestDispatcher dispatcher = request.getRequestDispatcher(path1);
					dispatcher.forward(request, response);
					return;
				}
			}
			if (!(violazione)) {
				String path="/WEB-INF/MAPPA.jsp";
				RequestDispatcher ds=request.getRequestDispatcher(path);
				ds.forward(request, response);

			}
			else if (violazione && user.getTipo().equals("lavoratore")) {
				String path = getServletContext().getContextPath()+"/ApriHomeLavoratore";
				response.sendRedirect(path);
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
	public void destroy() {
		try {
			if (con != null) {
				con.close();
			}
		} catch (SQLException sqle) {
		}
	}

}
