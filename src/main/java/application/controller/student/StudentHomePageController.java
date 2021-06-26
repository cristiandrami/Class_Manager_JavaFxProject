package application.controller.student;

import java.io.IOException;
import java.util.Optional;

import application.CommonUtil;
import application.SceneHandler;
import application.net.client.CommonClient;
import application.net.client.ProfessorClient;
import application.net.client.StudentClient;
import application.net.common.Protocol;
import application.student.StudentModel;
import application.student.StudentUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class StudentHomePageController {

    @FXML
    private ImageView logoView;
    @FXML
    private Label studentInfo;

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
    	Optional<ButtonType> result= SceneHandler.getInstance().showYesNoDialog(CommonUtil.LOGOUT_YES_NO, CommonUtil.LOGOUT_YES_NO_TITLE);
    	
    	if(result.get()== ButtonType.YES)
    	{
	    	try 
	    	{
				SceneHandler.getInstance().setLogin();
				StudentClient.getInstance().logout();
				ProfessorClient.getInstance().reset();
				StudentClient.getInstance().reset();
				CommonClient.getInstance().reset();
			} 
	    	catch (Exception e) 
	    	{
				
			}
    	}
    	else return;
    }
    @FXML
    void initialize()
    {
    	logoView.imageProperty().set(new Image(getClass().getResourceAsStream(StudentUtil.IMAGE_PATH)));
    	StudentModel student= StudentClient.getInstance().getStudent();
    	studentInfo.setText(student.getName()+" "+student.getSurname());
    }



}
