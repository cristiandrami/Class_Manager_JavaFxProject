package application.controller.professor;

import java.io.IOException;


import application.SceneHandler;
import application.net.client.ProfessorClient;
import application.professor.ScheduledGetStudent;
import application.professor.StudentsTableModel;
import javafx.application.Platform;
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

public class StudentViewTableController  
{
		private ObservableList<StudentsTableModel> tableList= FXCollections.observableArrayList();
		private ScheduledGetStudent refreshStudents= new ScheduledGetStudent();
	 	@FXML
	    private Label totalStudentsLabel;
	 	
	    @FXML
	    private Label insufficientLabel;

	    @FXML
	    private ImageView logoView;

	    @FXML
	    private Label sufficientLabel;

	    @FXML
	    private TableView<StudentsTableModel> tableView;
	    
	    @FXML
	    private TableColumn<StudentsTableModel, Integer> voteColumn;

	    @FXML
	    private TableColumn<StudentsTableModel, String> bornDateColumn;

	    @FXML
	    private TableColumn<StudentsTableModel, String> surnameColumn;

	    @FXML
	    private TableColumn<StudentsTableModel, String> nameColumn;

	    @FXML
	    private Button backButton;
	    
	    @FXML
	    void onCloseClicked(ActionEvent event) 
	    {
	    	ProfessorClient.getInstance().reset();
	    	System.exit(0);

	    }
	    @FXML
	    void backClicked(ActionEvent event) 
	    {
	    	try 
	    	{
				SceneHandler.getInstance().setProfHomePage();
			}
	    	catch (IOException e) 
	    	{
				System.out.println("Problema caricamento home page professore");
			}
	    	
	    }

	    
	    
	    



	    
	    @FXML
	    void initialize()
	    {
	    	 	refreshStudents.setPeriod(Duration.seconds(30));
	    	   
	    	 	refreshStudents.setDelay(Duration.seconds(0.1));

	    	 	refreshStudents.setOnSucceeded(new EventHandler<WorkerStateEvent>() 
	    	 	{
					
					@Override
					public void handle(WorkerStateEvent event) 
					{
						tableList= (ObservableList<StudentsTableModel>) event.getSource().getValue();
						tableView.setItems(tableList);
						
					}
				});
	    	 	

	    	   
	    	refreshStudents.start();
	    	
	    	
	    	
	    	sufficientLabel.setText(ProfessorClient.getInstance().getSufficientStudents());
	    	insufficientLabel.setText(ProfessorClient.getInstance().getUnsufficientStudents());
	    	totalStudentsLabel.setText(ProfessorClient.getInstance().getTotalStudents());
	    	
	    	 //ProfessorClient.getInstance().getStudentsList();
	    	

	    	logoView.imageProperty().set(new Image(getClass().getResourceAsStream("/loginResources/logoLogin.jpg"))); 
	    	nameColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
	    	surnameColumn.setCellValueFactory(new PropertyValueFactory<>("cognome"));
	    	bornDateColumn.setCellValueFactory(new PropertyValueFactory<>("dataNascita"));
	    	voteColumn.setCellValueFactory(new PropertyValueFactory<>("voto"));
	    	//tableView.setItems(tableList);
	    }


}
