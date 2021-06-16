package application.controller.student;

import java.io.IOException;
import java.util.ArrayList;

import application.SceneHandler;
import application.student.AssignmentModel;
import application.student.ScheduledGetAssignment;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;


public class AssignmentsStudentPageController 
{
	private ScheduledGetAssignment refreshAssignments= new ScheduledGetAssignment();

    @FXML
    private VBox vBoxContainer;

    @FXML
    private Button backButton;

    @FXML
    private ImageView logoView;



    @FXML
    void backClicked(ActionEvent event) 
    {
    	try 
    	{
			SceneHandler.getInstance().setStudentHomePage();
		} catch (IOException e) 
    	{
			System.out.println("Problema con il caricamento della home page studente");
    	}

    }
    @FXML
    void initialize()
    {
    	
    	startAssignmentsRefresh();
    	logoView.imageProperty().set(new Image(getClass().getResourceAsStream("/loginResources/logoLogin.jpg"))); 
    }
    
    private void startAssignmentsRefresh()
    {
    	refreshAssignments.setPeriod(Duration.seconds(20));
   	   
    	refreshAssignments.setDelay(Duration.seconds(1));

    	refreshAssignments.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) 
			{
				ArrayList<AssignmentModel> assignments = (ArrayList<AssignmentModel>) event.getSource().getValue();
				
				vBoxContainer.getChildren().clear();
				for(AssignmentModel a: assignments)
				{
					System.out.println(a.getObject()+"ciao"); 
					BorderPane newBorderPane= new BorderPane();
					Label object= new Label();
					object.setText(a.getObject());
					Label message= new Label();
					message.setText(a.getMessage());
					Label date= new Label();
					date.setText("Data caricamento: "+a.getDate());
					
					newBorderPane.setTop(object);
					newBorderPane.setCenter(message);
					newBorderPane.setBottom(date);
					
					vBoxContainer.getChildren().add(newBorderPane);
					
					System.out.println(a.getDate());
				}
				
				
				
			}
    		
		});

	   
    	refreshAssignments.start();
    }

}
