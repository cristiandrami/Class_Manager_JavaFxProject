package application.net.common;

public class User 
{

	private  String username;
	private String password;
	private String type;
	private String code;
	private String nome;
	private String cognome;
	private String data;
	private String classe;
	
	
	public User(String user, String password, String type, String code)
	{
		this.username=user;
		this.password=password;
		this.type=type;
		this.code= code;
		
	}
	
	public String getPassword() {
		return password;
	}
	public String getUsername() {
		return username;
	}

	public String getType() {
		return type;
	}

	public String getCode() {
		return code;
	}

	public String getNome() {
		return nome;
	}

	public String getData() {
		return data;
	}

	public String getClasse() {
		return classe;
	}

	public String getCognome() {
		return cognome;
	}

}
