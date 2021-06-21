package application.controller.student;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import application.SceneHandler;
import application.student.AssignmentModel;
import application.student.NotesModel;
import application.student.ScheduledGetNotes;
import application.student.StudentUtil;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class NotesStudentPageController {

	private ScheduledGetNotes refreshNotes= new ScheduledGetNotes();
	
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
    	startNotesRefresh();
    	logoView.imageProperty().set(new Image(getClass().getResourceAsStream("/loginResources/logoLogin.jpg"))); 
    }

	private void startNotesRefresh() 
	{

    	refreshNotes.setPeriod(Duration.seconds(20));
   	   
    	refreshNotes.setDelay(Duration.seconds(0.1));

    	refreshNotes.setOnSucceeded(new EventHandler<WorkerStateEvent>() 
    	{

			@Override
			public void handle(WorkerStateEvent event) 
			{
				ArrayList<NotesModel> notes = (ArrayList<NotesModel>) event.getSource().getValue();
				if(notes!= null && notes.size()>0)
				{
					Collections.sort(notes);
					
					vBoxContainer.getChildren().clear();
					for(NotesModel n: notes)
					{
						
						
						BorderPane newBorderPane= new BorderPane();
						Label object= new Label();
						object.setText(StudentUtil.OBJECT+ n.getObject());
						Label note= new Label();
						note.setText(n.getNote());
						Label date= new Label();
						date.setText(StudentUtil.DATE+n.getDate());
						newBorderPane.setTop(object);
						newBorderPane.setCenter(note);
						newBorderPane.setBottom(date);
						newBorderPane.setAlignment(newBorderPane.getTop(), Pos.CENTER);
						newBorderPane.setAlignment(newBorderPane.getBottom(), Pos.CENTER);
						newBorderPane.setAlignment(newBorderPane.getCenter(), Pos.CENTER);
						newBorderPane.getStyleClass().add(StudentUtil.REDBORDERPANE);
						
						newBorderPane.getTop().getStyleClass().add(StudentUtil.TOPBORDERPANESTYLE);
						newBorderPane.getCenter().getStyleClass().add(StudentUtil.CENTERBORDERPANESTYLE);
						newBorderPane.getBottom().getStyleClass().add(StudentUtil.BOTTOMBORDERPANESTYLE);
						
						vBoxContainer.getChildren().add(newBorderPane);
					}
					
				}
				else
				{
					BorderPane newBorderPane= new BorderPane();
					Label note= new Label();
					note.setText(StudentUtil.NOTESABSENT);
					newBorderPane.setCenter(note);
					newBorderPane.setAlignment(newBorderPane.getCenter(), Pos.CENTER);
					newBorderPane.getStyleClass().add(StudentUtil.GREENBORDERPANE);
					vBoxContainer.getChildren().add(newBorderPane);
					
				}
				
			}
		
    		
		});

	   
    	refreshNotes.start();
	}
}
    
