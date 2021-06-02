package application.net.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import application.net.common.Protocol;
import application.net.common.User;

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
		//3) scambio di messaggi con gli altri utenti 
		
		//prima faccio o login o registrazione 
		//solo dopo posso scambiare i messaggi
		//mi serve il PROTOCOLLO DI COMUNICAZIONE
		
		try {
				this.in= new ObjectInputStream(socket.getInputStream());
				String input;
				
					
				
				//se l'input è per fare il login allota provo a fare il check e se non va bene mando un messaggio di errore e ritorno
					input = (String) in.readObject();
					if(input.equals(Protocol.LOGIN))
					{
						
							User user=(User) in.readObject();
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
						
					}
			}
		catch(Exception e)
		{
			// SE QUI ho avuto qualche problema di connessione tolgo l'utente da quelli online
			if(!username.equals(""))
			{
				//l'utente è stato disconnesso
				UsersHandler.removeUser(username);
				
				
			}
			else
			{
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

}