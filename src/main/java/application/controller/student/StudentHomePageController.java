package application.controller.student;

import java.io.IOException;

import application.SceneHandler;
import application.net.client.CommonClient;
import application.net.client.StudentClient;
import application.student.StudentUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class StudentHomePageController {

    @FXML
    private ImageView logoView;

    @FXML
    private Button performanceButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button assignmentButton;
    
    @FXML
    private Button notesButton;

    @FXML
    void performanceClicked(ActionEvent event) 
    {
    	try 
    	{
			SceneHandler.getInstance().setPerformanceStudentPage();
		} 
    	catch (IOException e) 
    	{
			System.out.println(StudentUtil.PERFORMANCE_SET_PAGE_PROBLEM);
		}
    }
    
    @FXML
    void notesButtonClicked(ActionEvent event) 
    {
    	try 
    	{
			SceneHandler.getInstance().setNotesStudentPage();
		} 
    	catch (IOException e) 
    	{
			System.out.println(StudentUtil.NOTES_SET_PAGE_PROBLEM);
		}

    }


    


    @FXML
    void assignmentClicked(ActionEvent event) 
    {
    	try 
    	{
			SceneHandler.getInstance().setAssignmentStudentPage();
		} 
    	catch (IOException e) 
    	{
			System.out.println(StudentUtil.ASSIGNMENT_SET_PAGE_PROBLEM);
		}
    }



    @FXML
    void logoutClicked(ActionEvent event) 
    {
    	StudentClient.getInstance().reset();
    	try 
    	{
			SceneHandler.getInstance().setLogin();
			StudentClient.getInstance().reset();
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
    }



}
