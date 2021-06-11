package application.controller;

import java.io.IOException;

import application.SceneHandler;
import application.net.client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
    	System.out.println("hai cliccato");
    }



    @FXML
    void logoutClicked(ActionEvent event) {
    	Client.getInstance().reset();
    	try 
    	{
			SceneHandler.getInstance().setLogin();
		} 
    	catch (Exception e) 
    	{
			
		}

    }



    @FXML
    void closeClicked(ActionEvent event) 
    {
    	Client.getInstance().reset();
    	System.exit(0);
    	
    }
    
    @FXML 
    void initialize()
    {
    	logoView.imageProperty().set(new Image(getClass().getResourceAsStream("/loginResources/logoLogin.jpg"))); 
    	
    }
    

    
}
