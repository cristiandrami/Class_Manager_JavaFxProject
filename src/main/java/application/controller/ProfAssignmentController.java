package application.controller;

import application.SceneHandler;
import application.net.client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
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
    void sendAssignmentClicked(ActionEvent event) 
    {
    	String assignment=assignmentTextArea.getText();
    	if(assignment.equals(""))
    	{
    		SceneHandler.getInstance().showWarning("Sembra ci sia un problema con i compiti assegnati... assicurati di aver riempito il campo");
    		return;
    	}
    	
    	if(Client.getInstance().sendAssigment(assignment))
    		SceneHandler.getInstance().showInformation("Compiti assegnati correttamente");
    		
    	

    }


    @FXML
    void closeClicked(ActionEvent event) 
    {
    	Client.getInstance().reset();
    	System.exit(0);

    }

}
