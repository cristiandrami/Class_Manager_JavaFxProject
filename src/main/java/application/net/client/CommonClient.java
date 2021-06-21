package application.net.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import application.SceneHandler;
import application.net.common.Protocol;


//meglio averla come singleton
public class CommonClient 
{
	private static CommonClient instance=null;
	private Socket socket;
	
	//per comunicare col server
	private ObjectOutputStream out;
	//in non devo crearlo qui perchè 
	private ObjectInputStream in;
	
	
	//allocaree sempre l'out altrimenti se faccio prima in lui resta in attesa dal server e nel server ancora non ho creato l'out vado in blocco
	
	private CommonClient()
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
	
	public static CommonClient getInstance()
	{
		if(instance==null)
			instance= new CommonClient();
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
	
	public String getTypeFromCode(String newValue) 
	{
		sendMessage(Protocol.GETTYPEFROMCODE);
		sendMessage(newValue);
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
	
	public String getClassFromCode(String code) 
	{
		sendMessage(Protocol.GETCLASSFROMCODE);
		sendMessage(code);
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