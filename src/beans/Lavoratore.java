package beans;

import java.io.InputStream;
import java.util.Base64;

public class Lavoratore extends Utente {
	
	private String livelloEsperienza;
	private String foto;
	
	public Lavoratore() {
		super();
	}
	
	public Lavoratore(int id,String nome,String password,String email,String esperienza, String foto) {
		super(id,nome,password,email,"manager");
		this.livelloEsperienza=esperienza;
		this.foto=foto;
	}

	public String getLivelloEsperienza() {
		return livelloEsperienza;
	}

	public void setLivelloEsperienza(String livelloEsperienza) {
		this.livelloEsperienza = livelloEsperienza;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

}
