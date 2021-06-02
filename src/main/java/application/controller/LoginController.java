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

public class LoginController {
	private String PROFTYPE="prof";
	private String STUDENTTYPE="student";
	
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
    void loginClicked(ActionEvent event) {

    }

    @FXML
    void forgotPasswordClicked(ActionEvent event) {

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
    
    
    private void authentication(boolean login)
    {
    	String result= Client.getInstance().authentication(userField.getText(), passwordField.getText(), login);
    	if(result.equals(Protocol.OK))
    	{
    		try 
    		{
				String type= DatabaseHandler.getInstance().getType(userField.getText());
				if(type.equals(PROFTYPE));
				{
					SceneHandler.setProfHomePage();
				}
				else if(type.equals(STUDENTTYPE))
				{
					SceneHandler.setStudentHomePage();
				}
				else
					System.exit(0);
				
			} 
    		catch (Exception e) 
    		{
				
				SceneHandler.getInstance().showError("error during loading chat");
			}
    		
    	}
    	else
    	{
    		SceneHandler.getInstance().showError(result);
    		Client.getInstance().reset();
    	}
			
    
    }
    
    


}