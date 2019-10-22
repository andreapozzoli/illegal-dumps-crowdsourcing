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

import DAO.CampagnaDAO;
import DAO.LavoratoreDAO;
import DAO.ManagerDAO;
import beans.Campagna;
import beans.Lavoratore;
import beans.Manager;
import beans.Utente;


@WebServlet("/ApriModificaDati")
public class ApriModificaDati extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection con=null;

	public ApriModificaDati() {
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
		Utente current=(Utente) session.getAttribute("currentUser");
		if (current == null ) {
			String path = getServletContext().getContextPath();
			response.sendRedirect(path);
		}
		else {
			if (current.getTipo().equals("manager")) {
				ManagerDAO mDAO=new ManagerDAO(this.con);
				Manager manager;
				try {
					manager = mDAO.getManager(current.getId());
					session.setAttribute("utenteCompleto", manager);
				} catch (SQLException e) {
					String path1 = "/WEB-INF/ERRORE.jsp";
					request.setAttribute("user", current.getTipo());
					RequestDispatcher dispatcher = request.getRequestDispatcher(path1);
					dispatcher.forward(request, response);
					return;
				}
			}
			else {
				LavoratoreDAO lDAO=new LavoratoreDAO(this.con);
				try {
					Lavoratore lavoratore=lDAO.getLavoratore(current.getId());
					session.setAttribute("utenteCompleto", lavoratore);
				} catch (SQLException e) {
					String path1 = "/WEB-INF/ERRORE.jsp";
					request.setAttribute("user", current.getTipo());
					RequestDispatcher dispatcher = request.getRequestDispatcher(path1);
					dispatcher.forward(request, response);
					return;
				}

			}
			
				String path = "/WEB-INF/MODIFICADATI.jsp";
				RequestDispatcher dispatcher = request.getRequestDispatcher(path);
				dispatcher.forward(request, response);
			
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
