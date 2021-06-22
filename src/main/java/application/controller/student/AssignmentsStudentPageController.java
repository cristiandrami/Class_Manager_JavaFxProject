package application.controller.student;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import application.SceneHandler;
import application.net.client.ProfessorClient;
import application.student.AssignmentModel;
import application.student.ScheduledGetAssignment;
import application.student.StudentUtil;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
			System.out.println(StudentUtil.BACKTOHOMEPROBLEM);
    	}
    }
    @FXML
    void initialize()
    {
    	
    	startAssignmentsRefresh();
    	logoView.imageProperty().set(new Image(getClass().getResourceAsStream("/images/genericLogo.png"))); 
    	
    }
    
    private void startAssignmentsRefresh()
    {
    	refreshAssignments.setPeriod(Duration.seconds(20));
   	   
    	refreshAssignments.setDelay(Duration.seconds(0.1));

    	refreshAssignments.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) 
			{
				
				ArrayList<AssignmentModel> assignments = (ArrayList<AssignmentModel>) event.getSource().getValue();
				if(!(assignments==null))
				{
					Collections.sort(assignments);
					
					vBoxContainer.getChildren().clear();
					for(AssignmentModel a: assignments)
					{
						
						
						BorderPane newBorderPane= new BorderPane();
						Label object= new Label();
						object.setText(StudentUtil.OBJECT+ a.getObject());
						Label message= new Label();
						message.setText(a.getMessage());
						Label date= new Label();
						date.setText(StudentUtil.DATE+a.getDate());
						newBorderPane.setTop(object);
						newBorderPane.setCenter(message);
						newBorderPane.setBottom(date);
						newBorderPane.setAlignment(newBorderPane.getTop(), Pos.CENTER);
						newBorderPane.setAlignment(newBorderPane.getBottom(), Pos.CENTER);
						newBorderPane.setAlignment(newBorderPane.getCenter(), Pos.CENTER);
						newBorderPane.getStyleClass().add(StudentUtil.BORDERPANESTYLE);
						newBorderPane.getTop().getStyleClass().add(StudentUtil.TOPBORDERPANESTYLE);
						newBorderPane.getCenter().getStyleClass().add(StudentUtil.CENTERBORDERPANESTYLE);
						newBorderPane.getBottom().getStyleClass().add(StudentUtil.BOTTOMBORDERPANESTYLE);
						
						vBoxContainer.getChildren().add(newBorderPane);
						
						
					}
				}
				
				
				
				
			}
    		
		});

	   
    	refreshAssignments.start();
    }

}
