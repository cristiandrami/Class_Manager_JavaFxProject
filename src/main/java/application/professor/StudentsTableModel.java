package application.professor;

import java.io.Serializable;

public class StudentsTableModel implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2387294425459424451L;
	private String nome, cognome, dataNascita, voto, username;
	
	public StudentsTableModel(String username, String nome, String cognome, String dataNascita, String voto) 
	{
		super();
		this.username=username;
		this.nome = nome;
		this.cognome = cognome;
		this.dataNascita = dataNascita;
		this.voto = voto;
	}
	public String getNome() 
	{
		return nome;
	}
	public void setNome(String nome) 
	{
		this.nome = nome;
	}
	public String getCognome() 
	{
		return cognome;
	}
	public void setCognome(String cognome) 
	{
		this.cognome = cognome;
	}
	public String getDataNascita() {
		return dataNascita;
	}
	public void setDataNascita(String dataNascita) 
	{
		this.dataNascita = dataNascita;
	}
	public String getVoto() 
	{
		return voto;
	}
	public void setVoto(String voto) 
	{
		this.voto = voto;
	}
	public String getUsername() 
	{
		return username;
	}
	

}
