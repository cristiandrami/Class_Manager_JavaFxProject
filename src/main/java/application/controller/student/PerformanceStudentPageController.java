package application.controller.student;

import java.io.IOException;

import application.SceneHandler;
import application.professor.StudentsTableModel;
import application.student.ScheduledGetVotes;
import application.student.VotesTableModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class PerformanceStudentPageController 
{
	private ScheduledGetVotes refreshVotes= new ScheduledGetVotes();

	@FXML
    private ImageView logoView;

    @FXML
    private Label insufficientLabel;

    @FXML
    private Label totalStudentsLabel;

    @FXML
    private Label sufficientLabel;
    @FXML
    private TableView<VotesTableModel> tableView;

    @FXML
    private TableColumn<VotesTableModel, String> nameColumn;

    @FXML
    private TableColumn<VotesTableModel, Integer> voteColumn;
    
    private ObservableList<VotesTableModel> votes=FXCollections.observableArrayList();

    @FXML
    private Button backButton;



   
    @FXML
    void backClicked(ActionEvent event) 
    {
    	try 
    	{
			SceneHandler.getInstance().setStudentHomePage();
		} 
    	catch (IOException e) 
    	{
			System.out.println("Problema con il caricamento della home page studente");
		}

    }
    
    @FXML
    void initialize()
    {
    	
    	refreshVotes.setPeriod(Duration.seconds(30));
 	   
    	refreshVotes.setDelay(Duration.seconds(0.1));

    	refreshVotes.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) 
			{
				votes= (ObservableList<VotesTableModel>) event.getSource().getValue();
				tableView.setItems(votes);
				
			}
    		
		});

	   
    	refreshVotes.start();
    	
    	
    	logoView.imageProperty().set(new Image(getClass().getResourceAsStream("/loginResources/logoLogin.jpg"))); 
    	nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    	voteColumn.setCellValueFactory(new PropertyValueFactory<>("vote"));
    	//tableView.setItems(tableList);
    }
    


}
