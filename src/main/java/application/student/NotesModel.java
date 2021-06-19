package application.student;

import java.io.Serializable;

public class NotesModel implements Serializable, Comparable<NotesModel>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6702619845365432506L;
	String object, date, note;

	public NotesModel(String object, String date, String note) {
		super();
		this.object = object;
		this.date = date;
		this.note = note;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public int compareTo(NotesModel o) {
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
