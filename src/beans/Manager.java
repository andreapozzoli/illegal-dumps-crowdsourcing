package beans;

public class Manager extends Utente {

	public Manager() {
		super();
	}
	
	public Manager(int id,String nome,String password,String email) {
		super(id,nome,password,email,"manager");
	}
	

}
