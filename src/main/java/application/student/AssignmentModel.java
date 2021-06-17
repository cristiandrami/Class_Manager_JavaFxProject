package application.student;

import java.io.Serializable;

public class AssignmentModel implements Serializable, Comparable<AssignmentModel>
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

	@Override
	public int compareTo(AssignmentModel o) {
		String year="";
		String month="";
		String day="";
		String[] divideDate= o.getDate().split("/");
		day=divideDate[0];
		month=divideDate[1];
		year=divideDate[2];
		String thisYear="";
		String thisMonth="";
		String thisDay="";
		String[] thisDivideDate= this.date.split("/");
		thisDay=thisDivideDate[0];
		thisMonth=thisDivideDate[1];
		thisYear=thisDivideDate[2];
		
		
		
		if(thisYear.equals(year))
		{
			if(month.equals(thisMonth))
			{
				return day.compareTo(thisDay);
			}
			if(!month.equals(thisMonth))
			{
				
				return month.compareTo(thisMonth);
			}
			
			
		}
		else
			return year.compareTo(thisYear);
		
		return 0;
		
	
	}
	

}
