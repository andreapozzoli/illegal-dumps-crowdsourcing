package controllers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import DAO.LavoratoreDAO;
import DAO.ManagerDAO;
import DAO.UtenteDAO;
import beans.Utente;


@WebServlet("/CreaUtente")
public class CreaUtente extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection con=null;


	public CreaUtente() {
		super();
		// TODO Auto-generated constructor stub
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

		String nome = request.getParameter("username");
		String password = request.getParameter("password");
		String email=request.getParameter("email");
		String risultato="ko";
String risultato2="";
		if (nome == null || password == null || email==null) {
			String path = getServletContext().getContextPath()+"/ApriRegistrazione";
			response.sendRedirect(path);
		}else {

		String esiste="true";

		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
				"[a-zA-Z0-9_+&*-]+)*@" + 
				"(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
				"A-Z]{2,7}$"; 

		Pattern pat = Pattern.compile(emailRegex); 
		boolean valida=  pat.matcher(email).matches(); 



		if (valida) {

			UtenteDAO userDAO = new UtenteDAO(con);


			try {
				esiste = userDAO.checkNome(nome, email);
			} catch (SQLException e1) {
				risultato2="nonvabene";
			}
			if (esiste.equals("")) {
				String tipo=request.getParameter("tipo");


				if (tipo.equals("manager")) {
					ManagerDAO mDAO=new ManagerDAO(this.con);
					risultato="manager";
					try {
						mDAO.creaManager(nome, password, email);
					} catch (SQLException e) {
						risultato2="nonvabene";					}
				}
				else {
					LavoratoreDAO lDAO=new LavoratoreDAO(this.con);
					String esperienza=request.getParameter("livelloEsperienza");
					risultato="lavoratore";
					InputStream foto = null;
					String fotostr=request.getParameter("foto");
					if (fotostr!=null && fotostr!="") {
						File fi = new File(fotostr);
						byte[] fileContent ;

							try {

								fileContent = Files.readAllBytes(fi.toPath());
							}
							catch(java.nio.file.NoSuchFileException e) {
								String per = "C:/Users/Matteo/Desktop/immagini progetto web/"+fi.getName(); // C:/Users/Matteo/Desktop/immagini progetto web/
								Path p = Paths.get(per);                                                   // C:/Users/semmo/Desktop/immagini progetto web/
								fileContent = Files.readAllBytes(p);
							}
						foto = new ByteArrayInputStream(fileContent);
					}

					try {
						lDAO.creaLavoratore(nome, password, email, esperienza, foto);
					} catch (SQLException e) {
						risultato2="nonvabene";
					}
				}
				Utente user;
				try {
					user = userDAO.getUtente(nome, password);
					HttpSession session = request.getSession();
					session.setAttribute("currentUser", user);
				} catch (SQLException e) {
					risultato2="nonvabene";
				}
			}
			else if (esiste.equals("nomeemail")){
				risultato="ko2";
			}
			else if (esiste.equals("nome")) {
				risultato="ko";
			}
			else if (esiste.equals("email")) {
				risultato="ko1";
			}


		}
		else {
			risultato="ko3";

		}
if (risultato2.equals("")) {
		writeResponse(response, risultato);
}
else {
	writeResponse(response, risultato2);
}
		}
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
