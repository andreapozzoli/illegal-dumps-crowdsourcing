package beans;

public class Utente {

		protected int id;
		protected String nomeUtente;
		protected String password;
		protected String email;
		protected String tipo;
		
		public Utente() {
			
		}
		public Utente(int id, String nome, String password, String email,String tipo) {
			this.email=email;
			this.id=id;
			this.nomeUtente=nome;
			this.password=password;
			this.tipo=tipo;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getNomeUtente() {
			return nomeUtente;
		}
		public void setNomeUtente(String nomeUtente) {
			this.nomeUtente = nomeUtente;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String mail) {
			this.email = mail;
		}
		public String getTipo() {
			return tipo;
		}
		public void setTipo(String tipo) {
			this.tipo = tipo;
		}
		
		
}
