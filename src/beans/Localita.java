package beans;

import java.util.ArrayList;

public class Localita {

	private int id;
	private Float latitudine;
	private Float longitudine;
	private String nome;
	private String comune;
	private String regione;
	private ArrayList<Immagine> immagini=null;

	
	public Localita() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Float getLatitudine() {
		return latitudine;
	}

	public void setLatitudine(Float latitudine) {
		this.latitudine = latitudine;
	}

	public Float getLongitudine() {
		return longitudine;
	}

	public void setLongitudine(Float longitudine) {
		this.longitudine = longitudine;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	public String getRegione() {
		return regione;
	}

	public void setRegione(String regione) {
		this.regione = regione;
	}

	public ArrayList<Immagine> getImmagini() {
		return immagini;
	}

	public void setImmagini(ArrayList<Immagine> immagini) {
		this.immagini = immagini;
	}
	
}
