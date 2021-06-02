package application.net.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import org.springframework.security.crypto.bcrypt.BCrypt;

import application.net.common.User;

public class DatabaseHandler 
{
	
	private static DatabaseHandler instance=null;
	private Connection con=null;
	
	private DatabaseHandler()
	{
		try {
			con= DriverManager.getConnection("jdbc:sqlite:ClassManager_database.db");
		} catch (SQLException e) {
			//usare un file di log che è meglio
			e.printStackTrace();
		}
		
	}
	
	public static DatabaseHandler getInstance()
	{
		if(instance==null)
			instance= new DatabaseHandler();
		
		return instance;
	}
	public synchronized boolean insertUser(User user) throws SQLException
	{
		if(con==null || con.isClosed()|| user==null)
			return false;
		
		if(existsUser(user))
			return false;
		
	
		
		PreparedStatement p= con.prepareStatement("INSERT INTO User VALUES(?,?,?,?,?,?,?,?,?);");
		p.setString(1, null);
		p.setString(2, user.getUsername());
		p.setString(3, user.getNome());
		p.setString(4, user.getCognome());
		p.setString(5, user.getUsername());
		
		
		
		//devo  mettermi la password criptata, nel db devo salvarmi anche il sale
		p.setString(6, BCrypt.hashpw(user.getPassword(),  BCrypt.gensalt(12)));	
		p.setString(7, user.getData());
		p.setString(8, user.getClasse());
		p.setString(9, user.getType());
		p.executeUpdate();
		p.close();
		
		return true;
		
		
		
	}
	
	public synchronized boolean existsUser(User user) throws SQLException
	{
		if(con==null || con.isClosed()|| user==null)
			return false;
		
		String query= "SELECT * FROM User WHERE username=?;";
		PreparedStatement p= con.prepareStatement(query);
		p.setString(1, user.getUsername());
		
		ResultSet rs= p.executeQuery();
		
		boolean result= rs.next();
		p.close();
		
		return result;
		
	}
	
	public synchronized boolean checkUser(User user) throws SQLException
	{
		if(con==null || con.isClosed()|| user==null)
			return false;
		
		String query= "SELECT password FROM User WHERE username=?;";
		PreparedStatement p= con.prepareStatement(query);
		p.setString(1, user.getUsername());
		
		ResultSet rs= p.executeQuery();
		boolean result= false;
		if( rs.next())
		{
			String password= rs.getString("password");
			result=BCrypt.checkpw(user.getPassword(), password);
			
		}
		p.close();
		
		return result;
	}

	public synchronized String getType(String username) throws SQLException 
	{
		if(con==null || con.isClosed()|| username.equals(""))
			return "";
		
		String query= "SELECT tipo FROM User WHERE username=?;";
		PreparedStatement p= con.prepareStatement(query);
		p.setString(1, username);
		
		ResultSet rs= p.executeQuery();
		String result="";
	
		if( rs.next())
		{
			result= rs.getString("tipo");
			
		}
		p.close();
		
		return result;
	}

}
