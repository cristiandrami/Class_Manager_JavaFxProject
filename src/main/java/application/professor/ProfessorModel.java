package application.professor;

import java.io.Serializable;

public class ProfessorModel implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 168535027632414566L;
	private String name, surname;

	public ProfessorModel(String name, String surname) {
		super();
		this.name = name;
		this.surname = surname;
	}

	public ProfessorModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}
	
}
