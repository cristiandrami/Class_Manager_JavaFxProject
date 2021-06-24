package application.net.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import application.controller.RegistrationFormController;
import application.net.client.UserAccess;
import application.net.common.Protocol;
import application.net.common.User;
import application.professor.ProfessorModel;
import application.professor.StudentsTableModel;
import application.student.AssignmentModel;
import application.student.NotesModel;
import application.student.StudentModel;
import application.student.VotesTableModel;
import javafx.collections.ObservableList;

public class MessagesHandler extends Thread
{
	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private String username="";
	
	
	
	public MessagesHandler(Socket socket) throws IOException
	{
		this.socket=socket;
		
		//perchè altrimenti se lo creo anche nel server mi si blocca e c'è un deadlock
		this.out = new ObjectOutputStream(socket.getOutputStream());
		
	}
	public void closeStreams()
	{
		try {
			if(in!=null)
			in.close();
			if(out!=null)
			out.close();
			if(socket!=null)
				socket.close();
			
			in=null;
			out=null;
			socket=null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Override
	public void run() 
	{
		super.run();
		
		//il server deve gestire 3 operazioni
		//1) login
		//2) registrazione
		
		
		//prima faccio o login o registrazione 
		//solo dopo posso scambiare i messaggi
		//mi serve il PROTOCOLLO DI COMUNICAZIONE
		
		
		try {
				this.in= new ObjectInputStream(socket.getInputStream());
				String input;
				
					
				
				//se l'input è per fare il login allota provo a fare il check e se non va bene mando un messaggio di errore e ritorno
					input = (String) in.readObject();
					
					
					while(input.equals(Protocol.GETTYPEFROMCODE) || input.equals(Protocol.GETCLASSFROMCODE))
					{
						if(input.equals(Protocol.GETTYPEFROMCODE))
						{
							String code= (String) in.readObject();
							sendMessage(DatabaseHandler.getInstance().getTypeFromCode(code));
							input=(String) in.readObject();
						}
						else
						{
							String code= (String) in.readObject();
							sendMessage(DatabaseHandler.getInstance().getclassFromCode(code));
							input=(String) in.readObject();
							
						}
						
					}
					
					
					
					
					if(input.equals(Protocol.LOGIN))
					{
							
						
							UserAccess user=(UserAccess) in.readObject();
						
				
							if(!DatabaseHandler.getInstance().checkUser(user))
							{
								sendMessage(Protocol.AUTHENTICATION_ERROR);
								closeStreams();
								return;
								
							}
							//DB.controllaLogin(user);
							username=user.getUsername();
					}
					
					// se un prof prova a registrarsi  o uno studente provo a vedere se già esiste quell'user in caso ritorno
					
					else if(input.equals(Protocol.REGISTRATION))
					{
						User user=(User) in.readObject();
						if(DatabaseHandler.getInstance().existsUser(user))
						{
							sendMessage(Protocol.USER_EXISTS_ERROR);
							closeStreams();
							return;
							
						}
						
						else if(user.getType().equals(RegistrationFormController.PROFTYPE) && !DatabaseHandler.getInstance().existsSubject(user.getMateria()))
						{
							sendMessage(Protocol.SUBJECT_ERROR);
							closeStreams();
							return;
							
						}
						
						//qui cerco di aggiungerlo
						else 
						{
							if(!DatabaseHandler.getInstance().insertUser(user))
							{
								sendMessage(Protocol.ERROR);
								closeStreams();
								return;
							}
								
							
						}
						//se l'ho aggiunto mi salvo l'username
						
						username=user.getUsername();
					}
					

					
					// se sono arrivato qui c'è stato un errore
					
					else
					{
						//c'è stato un errore di protocollo
						sendMessage(Protocol.ERROR);
						
						closeStreams();
						return;
					}
					
					
				//vedo se non sono già loggato in caso ritorno
					if(!UsersHandler.insertUser(username, this))
					{
						sendMessage(Protocol.USER_LOGGED_ERROR);
						closeStreams();
						return;
					}
					
					
					
					//qui è tutto ok è andato tutto a buon fine
					sendMessage(Protocol.OK);
					
					
					//invio il messaggio 
					
					
					//STO IN ASCOLTO
					while(true)
					{
						String request= (String) in.readObject();
						//*************************************************** PROFESSOR ********************************//
						//System.out.println("server in ascolto");
						
						
						if(request.equals(Protocol.GETSTUDENTSFORPROF))
						{
							ArrayList<StudentsTableModel> list=DatabaseHandler.getInstance().getStudentsList(username);
							//System.out.println("list size "+ list.size());
							sendObject(list);
						}
						
						else if(request.equals(Protocol.SENDASSIGNMENT))
						{
							String assignment= (String)in.readObject();
							if(DatabaseHandler.getInstance().sendAssignment(username, assignment))
								sendMessage(Protocol.OK);
							else 
								sendMessage(Protocol.ERROR);
							
						}
						else if(request.equals(Protocol.UPDATESTUDENTVOTE))
						{
							
							String studentUsername= (String)in.readObject();
							
							Integer newVote= (Integer)in.readObject();
							
							if(DatabaseHandler.getInstance().updateVote(username, studentUsername, newVote))
								sendMessage(Protocol.OK);
							else 
								sendMessage(Protocol.ERROR);
							
						}
						else if (request.equals(Protocol.INSERTNOTE))
						{
							String student=(String) in.readObject();
							String note=(String) in.readObject();

							if(DatabaseHandler.getInstance().insertNote(username, student, note))
								sendMessage(Protocol.OK);
							else 
								sendMessage(Protocol.ERROR);
							
						}
						else if(request.equals(Protocol.GETCLASS))
						{
							sendMessage(DatabaseHandler.getInstance().getProfessorClass(username));
						}
						else if(request.equals(Protocol.GETPROFESSOROBJECT))
						{
							sendMessage(DatabaseHandler.getInstance().getProfessorObject(username));
						}
						else if(request.equals(Protocol.GET_PROFESSOR))
						{
							ProfessorModel professor= DatabaseHandler.getInstance().getProfessorInfo(username);
							sendObject(professor);
						}
						
						//*************************************************** STUDENT ********************************//
						else if(request.equals(Protocol.GETVOTES))
						{
							ArrayList<VotesTableModel> list=DatabaseHandler.getInstance().getVotes(username);
							//System.out.println("list size "+ list.size());
							sendObject(list);
						}
						else if(request.equals(Protocol.GETASSIGNMENTS))
						{
							//System.out.println("sto inviando il risultato della richiesta");
					
							ArrayList<AssignmentModel> list= DatabaseHandler.getInstance().getAssignmentsForStudent(username);
							sendObject(list);
							
						}
						else if(request.equals(Protocol.GETNOTES))
						{
							//System.out.println("sto inviando il risultato della richiesta");
					
							ArrayList<NotesModel> list= DatabaseHandler.getInstance().getNotesForStudent(username);
							sendObject(list);
							
						}
						
						else if(request.equals(Protocol.GETSTUDENTINFO))
						{
							StudentModel student= DatabaseHandler.getInstance().getStudentInfo(username);
							sendObject(student);
						}
						
						if(request.equals(Protocol.LOGOUT))
						{
							UsersHandler.removeUser(username);
							System.out.println("[SERVER] USER "+username+" DISCONNECTED");
							closeStreams();
							return;
						}
						
						
					}
			}
		catch(Exception e)
		{
			e.getStackTrace();
			// SE QUI ho avuto qualche problema di connessione tolgo l'utente da quelli online
			if(!username.equals(""))
			{
				//l'utente è stato disconnesso
				UsersHandler.removeUser(username);
				//System.out.println("[SERVER] USER "+username+" DISCONNECTED");
				
				
				
			}
			else
			{   //System.out.println("Errore qui");
				sendMessage(Protocol.ERROR);	
			}
			
			out=null;
			return;
				
		}
		
	}
	
	public void sendMessage(String message)
	{
		if(out==null)
			return;
		try 
		{
			out.writeObject(message);
			out.flush();
		} 
		catch (IOException e) 
		{
			if(!username.equals(""))
			{
				// SE QUI ho avuto qualche problema di connessione tolgo l'utente da quelli online
				//l'utente è stato disconnesso
				UsersHandler.removeUser(username);
			
				
				
			}
		}
		
	}
	
	public void sendObject(Object ob)
	{
		//System.out.println("sto spedendo la lista");
		if(out==null)
		{
			//System.out.println("oggetto nullo");
			return;
		}
			
		try 
		{
			out.writeObject(ob);
			out.flush();
			//System.out.println("lista spedita");
		} 
		catch (IOException e) 
		{
			if(!username.equals(""))
			{
				// SE QUI ho avuto qualche problema di connessione tolgo l'utente da quelli online
				//l'utente è stato disconnesso
				UsersHandler.removeUser(username);
				//System.out.println("problema con la spedizione");
			
				
				
			}
		}
		
	}
	
	

}
