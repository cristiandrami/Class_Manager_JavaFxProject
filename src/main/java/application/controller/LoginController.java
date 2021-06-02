package application.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LoginController {
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
    
    


}