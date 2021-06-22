package application.student;

import java.io.Serializable;

public class StudentModel  implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7493618750588988645L;
	

	private String name, surname, bornDate, sClass;
	public StudentModel() {
		name="";
		surname="";
		bornDate="";
		sClass="";
		
		
	}

	public StudentModel(String name, String surname, String bornDate, String sClass) {
		super();
		this.name = name;
		this.surname = surname;
		this.bornDate = bornDate;
		this.sClass = sClass;
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

	public String getBornDate() {
		return bornDate;
	}

	public void setBornDate(String bornDate) {
		this.bornDate = bornDate;
	}

	public String getsClass() {
		return sClass;
	}

	public void setsClass(String sClass) {
		this.sClass = sClass;
	}

	
}
