package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
import beans.Utente;

@WebServlet("/AggiornaStatoCampagna")
public class AggiornaStatoCampagna extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public Connection con=null;


	public AggiornaStatoCampagna() {
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

		Utente user=(Utente) session.getAttribute("currentUser");
		if (user == null ) {
			String path = getServletContext().getContextPath();
			response.sendRedirect(path);
		}
		else {
			if (user.getTipo().equals("manager")) {

				String stato=request.getParameter("stato");
				int id=Integer.parseInt(request.getParameter("id"));
				String path;
				CampagnaDAO cDAO=new CampagnaDAO(this.con);

				path=getServletContext().getContextPath()+"/ApriDettaglioCampagna?idcampagna="+id;

				try {
					cDAO.aggiornaCampagna(id, stato);
				} catch (SQLException e) {
					String path1 = "/WEB-INF/ERRORE.jsp";
					request.setAttribute("user", "manager");
					RequestDispatcher dispatcher = request.getRequestDispatcher(path1);
					dispatcher.forward(request, response);
					return;
				}


				
					response.sendRedirect(path);
				
			}
			else {
				String path = getServletContext().getContextPath()+"/ApriHomeLavoratore";
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
