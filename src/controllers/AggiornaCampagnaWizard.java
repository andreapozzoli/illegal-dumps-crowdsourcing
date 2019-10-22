package controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.codec.binary.Base64;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import DAO.CampagnaDAO;
import DAO.ImmagineDAO;
import DAO.LocalitaDAO;
import beans.Campagna;
import beans.Localita;
import beans.Utente;
import sun.misc.BASE64Decoder;

@WebServlet("/AggiornaCampagnaWizard")
public class AggiornaCampagnaWizard extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public Connection con=null;

	public AggiornaCampagnaWizard() {
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


		Utente user = (Utente) session.getAttribute("currentUser");
		if (user == null ) {
			String path = getServletContext().getContextPath();
			response.sendRedirect(path);
		}
		else {
			if(user.getTipo().equals("manager")) {
				boolean numero=true;
				int idcampagna=Integer.parseInt(request.getParameter("idcampagna"));
				Campagna campagna=new Campagna();
				String risultato="{ \"name\": \"vabene\"}";
				String data=request.getParameter("data");
				DateFormat format = new SimpleDateFormat("yyyy-mm-dd");
				format.setLenient(false);
				String erroredata="ok";
				try {
					format.parse(data);
				} catch (java.text.ParseException e) {
					erroredata="ko";
				}
				if (erroredata.equals("ok")) {

					String path;
					LocalitaDAO lDAO=new LocalitaDAO(this.con);
					ImmagineDAO iDAO=new ImmagineDAO(this.con);
					CampagnaDAO cDAO=new CampagnaDAO(this.con);
					int idlocalita=0;
					String opzione=request.getParameter("opzione"); //puo essere o crea o scegli localita

					if (opzione.equals("scegli")) {
						idlocalita=Integer.parseInt(request.getParameter("idlocalita"));			
					}
					else if (opzione.equals("crea")) {
						Float lat= null;
						Float lon = null;

						try {
							lat=Float.valueOf((request.getParameter("latitudine")));
							lon=Float.valueOf((request.getParameter("longitudine")));
							if (lat<-90 || lat>90 || lon<-180 ||lon>180) {
								risultato="{ \"name\": \"nonvabenenumero\"}";
								numero=false;
							}
						}
						catch(java.lang.NumberFormatException e) {
							risultato="{ \"name\": \"nonvabenenumero\"}";
							numero=false;
						}
						if (numero) {
							String nome=request.getParameter("nome");
							String comune=request.getParameter("comune");
							String regione=request.getParameter("regione");
							if (nome!=null && comune!=null && regione!=null) {
							try {
								lDAO.creaLocalita(lat, lon, nome, comune, regione, idcampagna);
								idlocalita=lDAO.ultimoId();
								lDAO.aggiungiloccamp(idlocalita, idcampagna);

							} catch (SQLException e) {
								risultato="{ \"name\": \"errpage\"}";

							}
						}
							else {
								risultato="{ \"name\": \"nullita\"}";
							}
						}

					}

					InputStream foto = null;
					String fotostr=request.getParameter("foto");
					byte[] fileContent ;

					if (fotostr!=null) {
						File fi = new File(fotostr);

						try {

							fileContent = Files.readAllBytes(fi.toPath());
						}
						catch(java.nio.file.NoSuchFileException e) {
							String per = "C:/Users/Matteo/Desktop/immagini progetto web/"+fi.getName(); // C:/Users/Matteo/Desktop/immagini progetto web/
							Path p = Paths.get(per);                                                   // C:/Users/semmo/Desktop/immagini progetto web/
							fileContent = Files.readAllBytes(p);
						}
						//BASE64Decoder decoder = new BASE64Decoder();
						//byte[] dataBytes = decoder.decodeBuffer(fotostr.replace(" ","+").split("^data:image/(png|jpg);base64,")[0]);
						foto = new ByteArrayInputStream(fileContent);
					}
					else {
						risultato="{ \"name\": \"nullita\"}";
					}

					String provenienza=request.getParameter("provenienza");
					String risoluzione=request.getParameter("risoluzione");

					if(provenienza!=null && data!=null && risoluzione!=null && numero) {

						try {
							/*DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			    DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			    String parsedDate=LocalDate.parse(data, formatter).format(formatter2);*/
							iDAO.creaImmagine(foto,provenienza, data, risoluzione, idlocalita);

							campagna=cDAO.getCampagna(idcampagna);
							if(cDAO.controlloAppartenenza(idcampagna, user.getId())) {
								campagna.setLocalita(lDAO.getElencoLocalita(idcampagna));
								for(int i=0;i<campagna.getLocalita().size();i++) {
									campagna.getLocalita().get(i).setImmagini(iDAO.getElencoImmagini(campagna.getLocalita().get(i).getId()));
								}
							}
						} catch (SQLException e) {
							risultato="{ \"name\": \"errpage\"}";

						}
					}
					else {
						risultato="{ \"name\": \"nullita\"}";
					}



				}

				else if (erroredata.equals("ko")) {
					risultato="{ \"name\": \"nonvabene\"}";
				}
				writeResponse(response,campagna.getLocalita(),risultato);
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

	private void writeResponse(HttpServletResponse response,ArrayList<Localita> loc,String risultato) throws IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");


		JsonObject jo = new JsonObject();
		JsonObject risultatoconv = new JsonParser().parse(risultato).getAsJsonObject();
		// ArrayList to JsonArray
		if(loc!=null && loc.size()!=0) {
			JsonArray jaB = new Gson().toJsonTree(loc).getAsJsonArray();
			jo.add("jaB", jaB);
		}
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
