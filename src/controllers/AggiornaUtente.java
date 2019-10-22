package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
import javax.swing.JOptionPane;

import DAO.LavoratoreDAO;
import DAO.ManagerDAO;
import DAO.UtenteDAO;
import beans.Utente;


@WebServlet("/AggiornaUtente")
public class AggiornaUtente extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public Connection con=null;


	public AggiornaUtente() {
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		Utente user=(Utente) session.getAttribute("currentUser");
		String risultato="ko";
		String risultato2="";
		if (user == null ) {
			String path = getServletContext().getContextPath();
			response.sendRedirect(path);
		}
		else {
			
		String tipo=user.getTipo();
		int id=user.getId();
		UtenteDAO userDAO=new UtenteDAO(this.con);


		if (tipo.equals("manager")) {
			ManagerDAO mDAO=new ManagerDAO(this.con);
			String nome=request.getParameter("nome");

			String password=request.getParameter("password");
			String email=request.getParameter("email");

			if (nome==null || password==null || email==null) {
				risultato="ko1";

			}
			else {
				String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
						"[a-zA-Z0-9_+&*-]+)*@" + 
						"(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
						"A-Z]{2,7}$"; 

				Pattern pat = Pattern.compile(emailRegex); 
				boolean valida=  pat.matcher(email).matches(); 

				if(valida) {



					boolean esiste=true;
					try {
						esiste = userDAO.checkNome(id, nome, email);
					} catch (SQLException e1) {
						risultato2="rotto";
					}if(!esiste) {
						risultato="manager";
						try {
							mDAO.aggiornaManager(id, nome, password, email);
						} catch (SQLException e) {
							risultato2="rotto";

						}
					}
					else {
						risultato="ko";
					}
				}
				else {
					risultato="ko2";
				}
			}
		}
		else {
			LavoratoreDAO lDAO=new LavoratoreDAO(this.con);
			String nome=request.getParameter("nome");
			String password=request.getParameter("password");
			String email=request.getParameter("email");



			String esperienza=request.getParameter("livelloEsperienza");
			String foto=request.getParameter("foto");

			if (nome==null || password==null || email==null || esperienza==null) {
				risultato="ko1";

			}
			else {
				String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
						"[a-zA-Z0-9_+&*-]+)*@" + 
						"(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
						"A-Z]{2,7}$"; 

				Pattern pat = Pattern.compile(emailRegex); 
				boolean valida=  pat.matcher(email).matches(); 

				if(valida) {

					boolean esiste=true;
					try {
						esiste = userDAO.checkNome(id, nome, email);
					} catch (SQLException e1) {
						risultato2="rotto";

					}if(!esiste) {
						risultato="lavoratore";
						try {
							if(foto.equals("") || foto==null) {
								lDAO.aggiornaLavoratoreNoFoto(id, nome, password, email, esperienza);

							}
							else {
							lDAO.aggiornaLavoratore(id, nome, password, email, esperienza, foto);
							}
						} catch (SQLException e) {
							risultato2="rotto";

						}
					}
					else {
						risultato="ko";					}
				}
				else {
					risultato="ko2";
				}
			}
		}

		if(risultato2.equals("")) {
			writeResponse(response, risultato);

		}
		
		else {
		writeResponse(response, risultato2);
		}
		}
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
