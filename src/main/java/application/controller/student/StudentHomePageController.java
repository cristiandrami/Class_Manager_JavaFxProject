package application.controller.student;

import java.io.IOException;

import application.SceneHandler;
import application.net.client.StudentClient;
import application.student.StudentUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
			System.out.println(StudentUtil.PERFORMANCESETPAGEPROBLEM);
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
			System.out.println(StudentUtil.NOTESSETPAGEPROBLEM);
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
			System.out.println(StudentUtil.ASSIGNMENTSETPAGEPROBLEM);
		}
    }



    @FXML
    void logoutClicked(ActionEvent event) 
    {
    	StudentClient.getInstance().reset();
    	try 
    	{
			SceneHandler.getInstance().setLogin();
		} 
    	catch (Exception e) 
    	{
			
		}
    }



}
