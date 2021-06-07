package application.controller;



import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ProfessorHomePageController  
{

    @FXML
    private Button studentButton;

    @FXML
    private ImageView logoView;

    @FXML
    private Button assignmentButton;
    

    @FXML
    private Button closeButton;

    @FXML
    void studentsClicked(ActionEvent event) {

    }

   
    @FXML
    void assignementClicked(ActionEvent event) {

    }

    @FXML
    void votesClicked(ActionEvent event) {

    }
 
    @FXML
    void logoutClicked(ActionEvent event) 
    {

    }
    @FXML
    void closeClicked(ActionEvent event) 
    {
    	System.exit(0);

    }

    
    @FXML
    void initialize()
    {
    	logoView.imageProperty().set(new Image(getClass().getResourceAsStream("/loginResources/logoLogin.jpg"))); 
    }


}
