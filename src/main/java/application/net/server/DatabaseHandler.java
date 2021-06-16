package application.net.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCrypt;

import application.controller.RegistrationFormController;
import application.net.client.UserAccess;
import application.net.common.User;
import application.professor.StudentsTableModel;
import application.student.AssignmentModel;
import application.student.VotesTableModel;
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
		
		else if(user.getType().equals(RegistrationFormController.STUDENTTYPE))
		{
			initStudentVotes(user.getUsername());
		}
		
		
		return true;
		
		
		
	}
	
	private void initStudentVotes(String username) throws SQLException 
	{
		
		if(con==null || con.isClosed())
			return;
		
		List<String> materie = new ArrayList<String>();
		
		String query= "SELECT nomeMateria FROM Materie;";
		PreparedStatement p= con.prepareStatement(query);
		ResultSet rs= p.executeQuery();
		
		while(rs.next())
		{
			materie.add(rs.getString("nomeMateria"));
		}
		p.close();
		
		for(String materia: materie)
		{
			p= con.prepareStatement("INSERT INTO studentiVoti VALUES(?,?,?);");
			p.setString(1, username);
			p.setString(2, materia);
			p.setInt(3, 0);
			p.executeUpdate();
			p.close();
		}
		
		
		
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
	//******************************************************************** PROFESSOR ***************************************************//
	public synchronized boolean existsSubject(String materia) throws SQLException 
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
	
	public synchronized ArrayList<StudentsTableModel> getStudentsList(String profUsername) throws SQLException 
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
	
		
		String query2= "SELECT user.username, user.nome, user.cognome, user.dataNascita, studentiVoti.voto FROM user, studentiVoti "
				+ "WHERE user.classeAppartenenza=? and user.username=studentiVoti.studente and studentiVoti.materia=?;";
		PreparedStatement p2= con.prepareStatement(query2);
		p2.setString(1, classe);
		p2.setString(2, materia);
		
		ResultSet rs2= p2.executeQuery();
		ArrayList<StudentsTableModel> studenti=new ArrayList<StudentsTableModel>();
		
	
		while( rs2.next())
		{
				String username=rs2.getString("username");
	           	String voto = rs2.getString("voto");
	            String nome= rs2.getString("nome");
				String cognome= rs2.getString("cognome");
				String data= rs2.getString("dataNascita");
				
				if(voto.equals("0"))
					voto="Non ancora scrutinato";
				
				
				
				studenti.add(new StudentsTableModel(username, nome, cognome, data, voto));

		}
		p2.close();
		
		
		
		return studenti;
		
	}

	public synchronized String getSufficientStudents(String profUsername) throws SQLException 
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
		while( rs1.next())
		{
			classe=rs1.getString("classeAppartenenza");
			materia=rs1.getString("materia");
			
		
		}
		
		
		
		
		p.close();
		
		Integer count=0;
	
		
		String query2="SELECT studentiVoti.voto FROM user, studentiVoti "
				+ "WHERE user.classeAppartenenza=? and user.username=studentiVoti.studente and studentiVoti.materia=?;";
		PreparedStatement p2= con.prepareStatement(query2);
		p2.setString(1, classe);
		p2.setString(2, materia);
		
		ResultSet rs2= p2.executeQuery();
		
		while( rs2.next())
		{
	            int voto = Integer.parseInt(rs2.getString("voto"));
	            if(voto>=6)
	            	count++;
		}
		p2.close();
		//System.out.println(count.toString());
		
		return count.toString();
		
		
	}
	
	public synchronized String getUnsufficientStudents(String profUsername) throws SQLException 
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
		while( rs1.next())
		{
			classe=rs1.getString("classeAppartenenza");
			materia=rs1.getString("materia");
		
		}
		
		
		
		
		p.close();
		
		Integer count=0;
	
		
		String query2="SELECT studentiVoti.voto FROM user, studentiVoti "
				+ "WHERE user.classeAppartenenza=? and user.username=studentiVoti.studente and studentiVoti.materia=?;";
		PreparedStatement p2= con.prepareStatement(query2);
		p2.setString(1, classe);
		p2.setString(2, materia);
		
		ResultSet rs2= p2.executeQuery();
		
		while( rs2.next())
		{
	            int voto = Integer.parseInt(rs2.getString("voto"));
	            //System.out.println(voto);
	            if(voto<6 && voto>=2)
	            	count++;
  
	        
			
		}
		p2.close();
		//System.out.println(count);
		
		return count.toString();
		
		
	}

	public synchronized String getTotalStudents(String profUsername) throws SQLException
	{
		if(con==null || con.isClosed()|| profUsername.equals(""))
			return null;
		
		String classe="";
		String query= "SELECT user.classeAppartenenza FROM user "
				+ "WHERE user.username=?;";
		PreparedStatement p= con.prepareStatement(query);
		p.setString(1, profUsername);
		
		ResultSet rs1= p.executeQuery();
		while( rs1.next())
		{
			classe=rs1.getString("classeAppartenenza");
		
		}
		
		
		
		p.close();
		
		String total="0";
	
		
		String query2="SELECT count(*) as tot FROM user "
				+ "WHERE user.classeAppartenenza=? and user.tipo=?;";
		PreparedStatement p2= con.prepareStatement(query2);
		p2.setString(1, classe);
		p2.setString(2, RegistrationFormController.STUDENTTYPE);
	
		
		ResultSet rs2= p2.executeQuery();
		
		while( rs2.next())
		{
	             total = rs2.getString("tot");
			
		}
		p2.close();

		
		
		
		return total;
		
	}

	public synchronized boolean sendAssignment(String profUsername, String assignment) throws SQLException 
	{
		if(con==null || con.isClosed()|| profUsername.equals(""))
			return false;
		
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
		
		Date date = new Date();  
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
		String data=formatter.format(date);
		
		p= con.prepareStatement("INSERT INTO compitiAssegnati VALUES(?,?,?,?,?);");
		p.setString(1, null);
		p.setString(2, materia);
		p.setString(3,  classe);
		p.setString(4, assignment);
		p.setString(5,  data);
		p.executeUpdate();
		p.close();
		
		
		return true;
		
		
	
	
		
		
	}
	
	public synchronized boolean updateVote(String profUsername, String studentUsername, int newVote) throws SQLException 
	{
		if(con==null || con.isClosed()|| profUsername.equals(""))
			return false;
		
		
		
		
		String materia="";
		String query= "SELECT ProfessoreMateria.materia FROM ProfessoreMateria "
				+ "WHERE ProfessoreMateria.prof=?;";
		PreparedStatement p= con.prepareStatement(query);
		p.setString(1, profUsername);
		
		
		ResultSet rs1= p.executeQuery();
		if( rs1.next())
		{
			materia=rs1.getString("materia");
		
		}
		
		
		
		
		p.close();
		
	
		
		
		p=con.prepareStatement("UPDATE studentiVoti SET voto=? WHERE materia=? and studente=?;") ;
		p.setInt(1, newVote);
		p.setString(2, materia);
		p.setString(3, studentUsername);
		
		p.executeUpdate();
		p.close();
		
		return true;
		
		
	}
	
	//******************************************************************** STUDENT ***************************************************//
	
	public synchronized ArrayList<VotesTableModel> getVotes(String studentUsername) throws SQLException 
	{
		if(con==null || con.isClosed()|| studentUsername.equals(""))
			return null;
		
		
	
		
		String query= "SELECT studentiVoti.materia, studentiVoti.voto FROM studentiVoti "
				+ "WHERE studentiVoti.studente=?;";
		PreparedStatement p2= con.prepareStatement(query);
		p2.setString(1, studentUsername);
	
		
		ResultSet rs2= p2.executeQuery();
		ArrayList<VotesTableModel> voti=new ArrayList<VotesTableModel>();
		
	
		while( rs2.next())
		{
			
	           	String voto = rs2.getString("voto");
	            String nome= rs2.getString("materia");
	            
	            //System.out.println(nome+" "+voto);
				
				
				if(voto.equals("0"))
					voto="Non ancora scrutinato";
				
				
				
				voti.add(new VotesTableModel(nome,voto));

		}
		p2.close();
		
		
		
		return voti;
		
	}
	
	public synchronized String getSufficientVotes(String studentUsername) throws SQLException 
	{
		if(con==null || con.isClosed()|| studentUsername.equals(""))
			return null;
		
		
	
		
		String query= "SELECT studentiVoti.voto FROM studentiVoti "
				+ "WHERE studentiVoti.studente=?;";
		PreparedStatement p2= con.prepareStatement(query);
		p2.setString(1, studentUsername);
	
		
		ResultSet rs2= p2.executeQuery();
		Integer sufficient=0;
	
		while( rs2.next())
		{
			
	           	String voto = rs2.getString("voto");
	           	try
	           	{
	           		Integer v=Integer.parseInt(voto);
	           		if(v>=6)
	           			sufficient++;
	           	}
	           	catch (NumberFormatException e) 
	           	{
	           	    
	           	}
	            
	            //System.out.println(nome+" "+voto);

				

		}
		p2.close();
		
		
		
		return sufficient.toString();
		
	}

	public synchronized String getUnsufficientVotes(String studentUsername) throws SQLException 
	{
		if(con==null || con.isClosed()|| studentUsername.equals(""))
			return null;
		
		
	
		
		String query= "SELECT studentiVoti.voto FROM studentiVoti "
				+ "WHERE studentiVoti.studente=?;";
		PreparedStatement p2= con.prepareStatement(query);
		p2.setString(1, studentUsername);
	
		
		ResultSet rs2= p2.executeQuery();
		Integer unsufficient=0;
	
		while( rs2.next())
		{
			
	           	String voto = rs2.getString("voto");
	           	try
	           	{
	           		Integer v=Integer.parseInt(voto);
	           		if(v<6 && v!=0)
	           			unsufficient++;
	           	}
	           	catch (NumberFormatException e) 
	           	{
	           	    
	           	}
	            
	            //System.out.println(nome+" "+voto);
				

		}
		p2.close();
		
		
		
		return unsufficient.toString();
		
	}

	public synchronized String getWaitingVotes(String studentUsername) throws SQLException 
	{
		if(con==null || con.isClosed()|| studentUsername.equals(""))
			return null;
		
		
	
		
		String query= "SELECT studentiVoti.voto FROM studentiVoti "
				+ "WHERE studentiVoti.studente=?;";
		PreparedStatement p2= con.prepareStatement(query);
		p2.setString(1, studentUsername);
	
		
		ResultSet rs2= p2.executeQuery();
		Integer waiting=0;
	
		while( rs2.next())
		{
			
	           	String voto = rs2.getString("voto");
	           	try
	           	{
	           		Integer v=Integer.parseInt(voto);
	           		if(v==0)
	           			waiting++;
	           	}
	           	catch (NumberFormatException e) 
	           	{
	           	    
	           	}
	            
	            //System.out.println(nome+" "+voto);
				

		}
		p2.close();
		
		
		
		return waiting.toString();
	}

	public synchronized ArrayList<AssignmentModel> getAssignmentsForStudent(String studentUsername) throws SQLException 
	{
		if(con==null || con.isClosed()|| studentUsername.equals(""))
			return null;
		
		String classe="";
		String query= "SELECT user.classeAppartenenza FROM user "
				+ "WHERE user.username=?;";
		PreparedStatement p= con.prepareStatement(query);
		p.setString(1, studentUsername);
		ResultSet rs1= p.executeQuery();
		if( rs1.next())
		{
			classe=rs1.getString("classeAppartenenza");
		
		}
		
		
		p.close();
		
		String query2= "SELECT compitiAssegnati.materia, compitiAssegnati.compito, compitiAssegnati.data FROM compitiAssegnati "
				+ "WHERE compitiAssegnati.classe=?;";
		PreparedStatement p2= con.prepareStatement(query2);
		p2.setString(1, classe);
		
	
		
		ResultSet rs2= p2.executeQuery();
		
		ArrayList<AssignmentModel> assignments=new ArrayList<AssignmentModel>();
		
	
		while( rs2.next())
		{
			
	           	String materia = rs2.getString("materia");
	            String compito= rs2.getString("compito");
	            String data=rs2.getString("data");
	            System.out.println(materia);
	            assignments.add(new AssignmentModel(materia, compito, data));
		}
		p2.close();
		
		
		
		return assignments;
		
	}


	

}

