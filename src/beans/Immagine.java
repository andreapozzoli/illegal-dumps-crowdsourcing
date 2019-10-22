package beans;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

public class Immagine {
	
	private int id;
	private String foto;
	private String provenienza;
	private Date data;
	private String risoluzione;
	private int idlocalita;
	private ArrayList<Annotazione> annotazioni=null;

	
	public Immagine() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProvenienza() {
		return provenienza;
	}

	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}

	public String getRisoluzione() {
		return risoluzione;
	}

	public void setRisoluzione(String risoluzione) {
		this.risoluzione = risoluzione;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public ArrayList<Annotazione> getAnnotazioni() {
		return annotazioni;
	}

	public void setAnnotazioni(ArrayList<Annotazione> annotazioni) {
		this.annotazioni = annotazioni;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public int getIdlocalita() {
		return idlocalita;
	}

	public void setIdlocalita(int idlocalita) {
		this.idlocalita = idlocalita;
	}
	
	

}
