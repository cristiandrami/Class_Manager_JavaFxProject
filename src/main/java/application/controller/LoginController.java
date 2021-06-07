package application.controller;


import application.SceneHandler;
import application.net.client.Client;
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
    	System.exit(0);

    }
    
    
    private void authentication()
    {
    	String result= Client.getInstance().authentication(userField.getText(), passwordField.getText());
    	if(result.equals(Protocol.OK))
    	{
    		try 
    		{
				String type= DatabaseHandler.getInstance().getType(userField.getText());
				System.out.println(type);
				if(type.equals(PROFTYPE))
				{
					System.out.println("accesso prof");
					SceneHandler.getInstance().setProfHomePage();
				}
				else if(type.equals(STUDENTTYPE))
				{
					System.out.println("accesso studente");
					//SceneHandler.setStudentHomePage();
				}
				else
					System.exit(0);
				
			} 
    		catch (Exception e) 
    		{
				
				SceneHandler.getInstance().showError("Error during loading dashboard");
			}
    		
    	}
    	else
    	{
    		SceneHandler.getInstance().showError(result);
    		Client.getInstance().reset();
    	}
			
    
    }
    
    
    


}