package beans;

import java.util.ArrayList;

public class Campagna {

	private int id;
	private String nome;
	private String committente;
	private String stato;
	private ArrayList<Localita> localita=null;
	
	public Campagna() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCommittente() {
		return committente;
	}

	public void setCommittente(String committente) {
		this.committente = committente;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public ArrayList<Localita> getLocalita() {
		return localita;
	}

	public void setLocalita(ArrayList<Localita> localita) {
		this.localita = localita;
	}
	
	
	
}
