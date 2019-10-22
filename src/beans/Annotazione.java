package beans;

import java.util.Date;

public class Annotazione {
	
	private int id;
	private Date data;
	private String validita;
	private String fiducia;
	private String note;
	private int idimmagine;
	private int idlavoratore;
	
	public Annotazione() {
		
	}

	public String isValidita() {
		return validita;
	}

	public void setValidita(String validita) {
		this.validita = validita;
	}
	public String getValidita() {
		return this.validita;
	}

	public String getFiducia() {
		return fiducia;
	}

	public void setFiducia(String fiducia) {
		this.fiducia = fiducia;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public int getIdimmagine() {
		return idimmagine;
	}

	public void setIdimmagine(int idimmagine) {
		this.idimmagine = idimmagine;
	}

	public int getIdlavoratore() {
		return idlavoratore;
	}

	public void setIdlavoratore(int idlavoratore) {
		this.idlavoratore = idlavoratore;
	}
	
	

}
