package application.net.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import application.SceneHandler;
import application.net.common.Protocol;
import application.net.common.User;
import application.net.server.UsersHandler;
import application.professor.StudentsTableModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


//meglio averla come singleton
public class ProfessorClient 
{
	private static ProfessorClient instance=null;
	private Socket socket;
	
	//per comunicare col server
	private ObjectOutputStream out;
	//in non devo crearlo qui perchè 
	private ObjectInputStream in;
	
	
	//allocaree sempre l'out altrimenti se faccio prima in lui resta in attesa dal server e nel server ancora non ho creato l'out vado in blocco
	
	private ProfessorClient()
	{
		try 
		{
			socket= new Socket("localhost",8000);
			out= new ObjectOutputStream(socket.getOutputStream());
			
			in=new ObjectInputStream(socket.getInputStream());
			
		}
		catch(IOException e)
		{
			out=null;
			SceneHandler.getInstance().showError("Cannot connect to server");
			
		}
	}
	
	public static ProfessorClient getInstance()
	{
		if(instance==null)
			instance= new ProfessorClient();
		return instance;
	}

	
	//se login è true voglio entrare, se è false l'utente vuole registrarsi
	//se sulla regitrazione c'è un errore il server chiude la comunicazione
	//creo un metodo di reset della comunicazione
	public String authentication(String username, String password)
	{
		sendMessage(Protocol.LOGIN);
		UserAccess user= new UserAccess(username, password);
		sendMessage(user,true);
			

		try 
		{
			String result= (String) in.readObject();
			return result;
	
		} 
		catch (Exception e) 
		{
			out=null;
			System.out.println("Errore qui");
			return Protocol.ERROR;
			
		}
		
		
	}
	
	//ma se sbaglio e nella chat mando un oggetto che non è una stringa?
	private boolean sendMessage(Object message, boolean empty)
	{
		//non possso inviare il messaggio se non sono connesso al server
		if(out==null)
			return false;
		
		try 
		{
			out.writeObject(message);
			out.flush();
		} 
		catch (IOException e) 
		{
			out=null;
			return false;
			
		}
		
		
		return true;
	}
	
	public String registration(String username,String nome,String cognome,String password, String nascita, String classe, String tipo, String code,String materia)
	{
		sendMessage(Protocol.REGISTRATION);
		sendMessage(new User(username,nome, cognome,  password, nascita, classe, tipo, code, materia),true);
			

		try 
		{
			
			String result= (String) in.readObject();
			return result;
	
		} 
		catch (Exception e) 
		{
			out=null;
			return Protocol.ERROR;
		}
		
		
	}
	public ObservableList<StudentsTableModel> getStudentsList() 
	{
		sendMessage(Protocol.GETSTUDENTSFORPROF);
		
		try 
		{
			ArrayList<StudentsTableModel> tableList;
			tableList = (ArrayList<StudentsTableModel>) in.readObject();
			ObservableList<StudentsTableModel> studentList= FXCollections.observableArrayList();
			for(StudentsTableModel student: tableList)
				studentList.add(student);
			//System.out.println(tableList.size());
			return studentList;
		} 
		catch (Exception e) 
		{
			out=null;
			return null;
		}

	}
	
	public String getSufficientStudents()
	{
		System.out.println("invio la richiesta");
		sendMessage(Protocol.GETSUFFICIENTSTUDENS);
		
		try 
		{
			
			String result= (String) in.readObject();
			return result;
	
		} 
		catch (Exception e) 
		{
			out=null;
			return Protocol.ERROR;
		}
		
	}
	
	public String getUnsufficientStudents() 
	{
		sendMessage(Protocol.GETUNSUFFICIENTSTUDENS);
		
			
		try 
		{
			
			String result= (String) in.readObject();
			return result;
	
		} 
		catch (Exception e) 
		{
			out=null;
			return Protocol.ERROR;
		}
	}
	
	public String getTotalStudents() 
	{
		sendMessage(Protocol.GETTOTALSTUDENTS);
		
		try 
		{
			
			String result= (String) in.readObject();
			return result;
	
		} 
		catch (Exception e) 
		{
			out=null;
			return Protocol.ERROR;
		}
		
	}
	
	public boolean sendAssigment(String assignment) 
	{
		sendMessage(Protocol.SENDASSIGNMENT);
		sendMessage(assignment);
		
		try 
		{
			
			String result= (String) in.readObject();
			if(result.equals(Protocol.OK))
				return true;
			else return false;
	
		} 
		catch (Exception e) 
		{
			out=null;
			return false;
		}
	}
	
	public boolean updateStudentVote(String studentUsername, int newVote) 
	{
		sendMessage(Protocol.UPDATESTUDENTVOTE);
		sendMessage(studentUsername);
		sendMessage(newVote, true);
		
		try 
		{
			
			String result= (String) in.readObject();
			if(result.equals(Protocol.OK))
				return true;
			else return false;
	
		} 
		catch (Exception e) 
		{
			out=null;
			return false;
		}
		
	}
	
	
	
	//questo lo posso usare con la chat senza avere problemi
	
	public boolean sendMessage(String message)
	{
		//così capiamo che sto usando quello di sopra e non ho problemi tra i due
		return sendMessage(message,true);
	}
	
	public void reset()
	{
		instance=null;
		out=null;
		in=null;
		socket=null;
	}



	

	

	

	

	
	
}
