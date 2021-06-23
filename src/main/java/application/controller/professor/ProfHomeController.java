package application.controller.professor;


import java.io.IOException;

import application.SceneHandler;
import application.net.client.CommonClient;
import application.net.client.ProfessorClient;
import application.professor.ProfessorModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ProfHomeController {

    @FXML
    private Button studentButton;

    @FXML
    private Button closeButton;

    @FXML
    private ImageView logoView;

    @FXML
    private Button assignmentButton;
    @FXML
    private Button votesButton;
    @FXML
    private Button logoutButton;

    @FXML
    private Label profInfo;

    @FXML
    private Label materiaProf;
    
    

    @FXML
    void studentsClicked(ActionEvent event) 
    {
    	try 
    	{
			SceneHandler.getInstance().setStudentViewProfessor();
		} 
    	catch (IOException e)
    	{
			
			e.printStackTrace();
		}

    }

 

    @FXML
    void assgimentClicked(ActionEvent event) 
    {
    	try 
    	{
			SceneHandler.getInstance().setAssignmnetProfPage();
		} catch (IOException e) 
    	{
			
			e.printStackTrace();
		}

    }



    @FXML
    void votesClicked(ActionEvent event) 
    {
    	try 
    	{
			SceneHandler.getInstance().setProfessorVotesGestionPage();
		} catch (IOException e) 
    	{
			
			e.printStackTrace();
		}
    	
    }



    @FXML
    void logoutClicked(ActionEvent event) 
    {
    	ProfessorClient.getInstance().reset();
    	try 
    	{
			SceneHandler.getInstance().setLogin();
			ProfessorClient.getInstance().reset();
			CommonClient.getInstance().reset();
		} 
    	catch (Exception e) 
    	{
			
		}

    }

    
    @FXML 
    void initialize()
    {
    	logoView.imageProperty().set(new Image(getClass().getResourceAsStream("/images/genericLogo.png"))); 
        materiaProf.setText(ProfessorClient.getInstance().getMateria().toUpperCase());
    	ProfessorModel professor=ProfessorClient.getInstance().getProfessor();
    	profInfo.setText(professor.getName()+ " "+ professor.getSurname());
    }
    

    
}
