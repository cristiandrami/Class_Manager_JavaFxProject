
	package application.net.client;

	import java.io.IOException;
	import java.io.ObjectInputStream;
	import java.io.ObjectOutputStream;
	import java.net.Socket;
import java.util.ArrayList;

import application.SceneHandler;

import application.net.common.Protocol;
import application.net.common.User;
import application.professor.StudentsTableModel;
import application.student.AssignmentModel;
import application.student.NotesModel;
import application.student.StudentModel;
import application.student.VotesTableModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;



	//meglio averla come singleton
	public class StudentClient 
	{
		private static StudentClient instance=null;
		private Socket socket;
		
		//per comunicare col server
		private ObjectOutputStream out;
		//in non devo crearlo qui perchè 
		private ObjectInputStream in;
		
		
		//allocaree sempre l'out altrimenti se faccio prima in lui resta in attesa dal server e nel server ancora non ho creato l'out vado in blocco
		
		private StudentClient()
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
		
		public static StudentClient getInstance()
		{
			if(instance==null)
				instance= new StudentClient();
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
		
		public ObservableList<VotesTableModel> getVotes() 
		{
			sendMessage(Protocol.GETVOTES);
			
			try 
			{
				ArrayList<VotesTableModel> tableList;
				tableList = (ArrayList<VotesTableModel>) in.readObject();
				ObservableList<VotesTableModel> votesList= FXCollections.observableArrayList();
				for(VotesTableModel votes: tableList)
					votesList.add(votes);
				//System.out.println(tableList.size());
				return votesList;
			} 
			catch (Exception e) 
			{
				out=null;
				return null;
				
			}
		}
		public void logout() 
		{
			sendMessage(Protocol.LOGOUT);
			try 
			{
				
				String result= (String) in.readObject();
				
		
			} 
			catch (Exception e) 
			{
				out=null;
			}
			
		}
		public ArrayList<AssignmentModel> getAssignments() 
		{
				sendMessage(Protocol.GETASSIGNMENTS);
				
				try 
				{
					ArrayList<AssignmentModel> assignments=(ArrayList<AssignmentModel>) in.readObject();
					return assignments;
				} 
				catch (Exception e) 
				{
					out=null;
					return null;
				}
		}
		public ArrayList<NotesModel> getNotes() 
		{
			sendMessage(Protocol.GETNOTES);
			try 
			{
				ArrayList<NotesModel> notes=(ArrayList<NotesModel>) in.readObject();
				return notes;
			} 
			catch (Exception e) 
			{
				out=null;
				return null;
			}
		}
		
		public StudentModel getStudent() {
			sendMessage(Protocol.GETSTUDENTINFO);
			try 
			{
				StudentModel student=(StudentModel) in.readObject();
				return student;
			} 
			catch (Exception e) 
			{
				out=null;
				return null;
			}
		}

		




	

		public boolean sendMessage(String message)
		{
			
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


