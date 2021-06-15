package application.controller;


import java.io.IOException;
import java.sql.SQLException;

import application.SceneHandler;
import application.net.client.ProfessorClient;
import application.net.client.StudentClient;
import application.net.common.Protocol;
import application.net.server.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LoginController 
{
	private String PROFTYPE="professore";
	private String STUDENTTYPE="studente";
	
    @FXML
    private ImageView logoView;

	@FXML
    private Hyperlink forgotPasswordHypelink;

    @FXML
    private Button loginButton;

    @FXML
    private TextField userField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button registrationButton;




    @FXML
    void loginClicked(ActionEvent event) 
    {
    	authentication();
    }

    @FXML
    void forgotPasswordClicked(ActionEvent event) 
    {

    }
    
    @FXML
    void registrationClicked(ActionEvent event) throws Exception 
    {
    	SceneHandler.getInstance().setRegistration();

    }
    @FXML
    void initialize()
    {
    	logoView.imageProperty().set(new Image(getClass().getResourceAsStream("/loginResources/logoLogin.jpg"))); 
    	//logoView.setImage(new Image(getClass().getResourceAsStream("/loginResources/logoLogin.jpg")));
   
    }
    

    @FXML
    void exitClicked(ActionEvent event) 
    {
    	ProfessorClient.getInstance().reset();
    	System.exit(0);

    }
    
    
    private void authentication() 
    {
    	String type="";
    	String result="";
		try 
		{
			type = DatabaseHandler.getInstance().getType(userField.getText());
			//System.out.println(type);
			if(type.equals(PROFTYPE))
			{	
				result= ProfessorClient.getInstance().authentication(userField.getText(), passwordField.getText());
				if(result.equals(Protocol.OK))
				{
					try 
					{
							SceneHandler.getInstance().setProfHomePage();
						
					} 
					catch (IOException e) 
					{
							// TODO Auto-generated catch block
							e.printStackTrace();
						
					}
				}
				else
		    	{
		    		SceneHandler.getInstance().showError(result);
		    		ProfessorClient.getInstance().reset();
		    	}
			}
			else if(type.equals(STUDENTTYPE))
			{
				result= StudentClient.getInstance().authentication(userField.getText(), passwordField.getText());
				if(result.equals(Protocol.OK))
				{
					try 
					{
							SceneHandler.getInstance().setStudentHomePage();
						
					} 
					catch (IOException e) 
					{
							// TODO Auto-generated catch block
							e.printStackTrace();
						
					}
				}
				else
		    	{
		    		SceneHandler.getInstance().showError(result);
		    		ProfessorClient.getInstance().reset();
		    	}
			}
			
		}
		catch (SQLException e1) 
		{
			
		}
    	

    	
   
			
    
    }
    
    
    


}