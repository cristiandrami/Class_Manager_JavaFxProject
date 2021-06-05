package application.net.common;

import java.io.Serializable;

public class User implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6302972329167979183L;
	
	private  String username;
	private String password;
	private String type;
	private String code;
	private String nome;
	private String cognome;
	private String data;
	private String classe;
	
	
	public User(String user,String nome, String cognome, String password,String data, String classe, String tipo, String code)
	{
		this.username=user;
		this.password=password;
		this.type=tipo;
		this.code= code;
		this.nome=nome;
		this.cognome=cognome;
		this.data=data;
		this.classe=classe;
		
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
