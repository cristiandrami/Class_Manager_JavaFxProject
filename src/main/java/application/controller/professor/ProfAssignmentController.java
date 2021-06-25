package application.controller.professor;

import java.io.IOException;

import application.SceneHandler;
import application.net.client.ProfessorClient;
import application.professor.ProfessorUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ProfAssignmentController 
{
	@FXML
    private TextArea assignmentTextArea;

    @FXML
    private Button closeButton;

    @FXML
    private ImageView logoView;

    @FXML
    private Button sendAssignment;
    @FXML
    private Button backButton;
    @FXML
    private Label classLabel;


    

    @FXML
    void sendAssignmentClicked(ActionEvent event) 
    {
    	String assignment=assignmentTextArea.getText();
    	if(assignment.equals(""))
    	{
    		SceneHandler.getInstance().showWarning(ProfessorUtil.ASSIGNMENT_PROBLEM);
    		return;
    	}
    	
    	if(ProfessorClient.getInstance().sendAssigment(assignment))
    	{
    		SceneHandler.getInstance().showInformation(ProfessorUtil.ASSIGNMENT_SENT);
    		assignmentTextArea.setText("");
    		
    	}
    		
    		
    	

    }


    @FXML
    void closeClicked(ActionEvent event) 
    {
    	ProfessorClient.getInstance().reset();
    	System.exit(0);

    }
    @FXML
    void backClicked(ActionEvent event) 
    {
    	assignmentTextArea.setPromptText(ProfessorUtil.PROMT_TEXT_INSERT_ASSIGNMENT);
    	try 
    	{
			SceneHandler.getInstance().setProfHomePage();
		} 
    	catch (IOException e) 
    	{
			System.out.println(ProfessorUtil.BACK_TO_HOME_PROBLEM);
		}

    }
    
    @FXML
    void initialize()
    {
    	logoView.imageProperty().set(new Image(getClass().getResourceAsStream("/images/genericLogo.png"))); 
    	classLabel.setText(ProfessorClient.getInstance().getClasse());
    	//sendAssignment.getStyleClass().add("blueButton");
    	
    }

}
