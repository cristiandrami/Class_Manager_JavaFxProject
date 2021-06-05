package application;

import java.sql.SQLException;
import java.util.Scanner;

import application.net.client.UserAccess;
import application.net.common.User;
import application.net.server.DatabaseHandler;

public class TestAccessMain {
	public static void main(String[] args) 
	{
		
		
		
		
		//User u= new User("pepe", "ssass", "sssaas", "ciaociao", "esdds", "1A", "prof", "000");
		
		try {
			//DatabaseHandler.getInstance().insertUser(u);
			
			if(DatabaseHandler.getInstance().checkUser(new UserAccess("pepe","ciaociao")))
				System.out.println("login effettuatos");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
	}

}
