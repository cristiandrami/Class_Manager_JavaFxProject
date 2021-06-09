package application.net.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.security.crypto.bcrypt.BCrypt;

import application.StudentsTableModel;
import application.controller.RegistrationFormController;
import application.net.client.UserAccess;
import application.net.common.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
		
	
		
		
	
		
		PreparedStatement p= con.prepareStatement("INSERT INTO User VALUES(?,?,?,?,?,?,?,?);");
		p.setString(1, null);
		p.setString(2, user.getUsername());
		p.setString(3, user.getNome());
		p.setString(4, user.getCognome());
		
		
		
		//devo  mettermi la password criptata, nel db devo salvarmi anche il sale
		p.setString(5, BCrypt.hashpw(user.getPassword(),  BCrypt.gensalt(12)));	
		p.setString(6, user.getData());
		p.setString(7, user.getClasse());
		p.setString(8, user.getType());
		p.executeUpdate();
		p.close();
		
		if(user.getType().equals(RegistrationFormController.PROFTYPE))
		{
			p= con.prepareStatement("INSERT INTO ProfessoreMateria VALUES(?,?);");
			p.setString(1, user.getUsername());
			p.setString(2, user.getMateria());
			p.executeUpdate();
			p.close();
			
		}
		
		
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
	
	public synchronized boolean checkUser(UserAccess user) throws SQLException
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
	
	public synchronized String getTypeFromCode(String code) throws SQLException 
	{
		if(con==null || con.isClosed()|| code.equals(""))
			return "";
		
		String query= "SELECT tipologia FROM CodiciClassi WHERE codiceAccesso=?;";
		PreparedStatement p= con.prepareStatement(query);
		p.setString(1, code);
		
		ResultSet rs= p.executeQuery();
		String result="";
	
		if( rs.next())
		{
			result= rs.getString("tipologia");
			
		}
		p.close();
		
		return result;
	}

	public synchronized String getclassFromCode(String code) throws SQLException 
	{
		if(con==null || con.isClosed()|| code.equals(""))
			return "";
		
		String query= "SELECT nomeClasse FROM CodiciClassi WHERE codiceAccesso=?;";
		PreparedStatement p= con.prepareStatement(query);
		p.setString(1, code);
		
		ResultSet rs= p.executeQuery();
		String result="";
	
		if( rs.next())
		{
			result= rs.getString("nomeClasse");
			
		}
		p.close();
		
		return result;
		
	}

	public boolean existsSubject(String materia) throws SQLException 
	{
		if(con==null || con.isClosed()|| materia==null)
			return false;
		
		String query= "SELECT * FROM Materie WHERE nomeMateria=?;";
		PreparedStatement p= con.prepareStatement(query);
		p.setString(1, materia);
		
		ResultSet rs= p.executeQuery();
		
		boolean result= rs.next();
		p.close();
		
		return result;
		
	}
	
	public synchronized ObservableList<StudentsTableModel> getStudentsList(String profUsername) throws SQLException 
	{
		if(con==null || con.isClosed()|| profUsername.equals(""))
			return null;
		
		String classe="";
		String materia="";
		String query= "SELECT user.classeAppartenenza, ProfessoreMateria.materia FROM user, ProfessoreMateria "
				+ "WHERE user.username=? and ProfessoreMateria.prof=?;";
		PreparedStatement p= con.prepareStatement(query);
		p.setString(1, profUsername);
		p.setString(2, profUsername);
		
		ResultSet rs1= p.executeQuery();
		if( rs1.next())
		{
			classe=rs1.getString("classeAppartenenza");
			materia=rs1.getString("materia");
		
		}
		
		p.close();
	
		
		String query2= "SELECT user.nome, user.cognome, user.dataNascita, studenti-voti.voto FROM user, studenti-voti "
				+ "WHERE user.classeAppartenenza=? and user.username=studenti-voti.studente and studenti-voti.materia=?;";
		PreparedStatement p2= con.prepareStatement(query);
		p2.setString(1, classe);
		p2.setString(2, materia);
		
		ResultSet rs2= p.executeQuery();
	
		
		ObservableList<StudentsTableModel> studenti= FXCollections.observableArrayList();
		
	
		if( rs2.next())
		{
			
			try
			{
	            int voto = Integer.parseInt(rs2.getString("voto"));
	            String nome= rs2.getString("nome");
				String cognome= rs2.getString("cognome");
				String data= rs2.getString("dataNascita");
				studenti.add(new StudentsTableModel(nome, cognome, data, voto));
				
				
	            
	        }
	        catch (NumberFormatException ex){
	            ex.printStackTrace();
	        }
			
			
			
		}
		p2.close();
		
		return studenti;
		
	}

}

