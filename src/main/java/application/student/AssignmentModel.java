package application.student;

import java.io.Serializable;

public class AssignmentModel implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1502844110655147440L;
	
	String object, message, date;

	public AssignmentModel(String object, String message, String date) 
	{
		super();
		this.object = object;
		this.message = message;
		this.date = date;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	

}
