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
			refreshNotes.cancel();
		} catch (IOException e) 
    	{
			System.out.println(StudentUtil.BACK_TO_HOME_PROBLEM);
    	}
			
    }
    
    @FXML
    void initialize()
    {
    	startNotesRefresh();
    	logoView.imageProperty().set(new Image(getClass().getResourceAsStream(StudentUtil.IMAGE_PATH))); 
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
						newBorderPane.getStyleClass().add(StudentUtil.RED_BORDER_PANE);
						
						newBorderPane.getTop().getStyleClass().add(StudentUtil.TOP_BORDER_PANE_STYLE);
						newBorderPane.getCenter().getStyleClass().add(StudentUtil.CENTER_BORDER_PANE_STYLE);
						newBorderPane.getBottom().getStyleClass().add(StudentUtil.BOTTOM_BORDER_PANE_STYLE);
						
						vBoxContainer.getChildren().add(newBorderPane);
					}
					
				}
				else
				{
					vBoxContainer.getChildren().clear();
					BorderPane newBorderPane= new BorderPane();
					Label note= new Label();
					note.setText(StudentUtil.NOTES_ABSENT);
					newBorderPane.setCenter(note);
					newBorderPane.setAlignment(newBorderPane.getCenter(), Pos.CENTER);
					newBorderPane.getStyleClass().add(StudentUtil.GREEN_BORDER_PANE);
					vBoxContainer.getChildren().add(newBorderPane);
					
				}
				
			}
		
    		
		});

	   
    	refreshNotes.start();
	}
}
    
