package application.net.server;

import java.util.ArrayList;
import java.util.HashMap;

//ora devo gestire tutti gli utenti nella chat
public class UsersHandler 
{
	private static HashMap<String, MessagesHandler> users= new HashMap<String, MessagesHandler>();
	
	
	//due utenti con lo stesso user provano a fare il login o la registrazione in contemporaneo
	public synchronized static boolean insertUser(String username, MessagesHandler handler)
	{
		if(users.containsKey(username))
			return false;
		
		//così evito gli utenti che già si erano loggati
		
		users.put(username, handler);
		return true;
	}
	public synchronized static void removeUser(String username)
	{
		users.remove(username);
	}

}