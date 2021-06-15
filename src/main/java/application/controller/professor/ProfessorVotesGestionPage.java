package application.controller.professor;

import java.io.IOException;

import application.SceneHandler;
import application.net.client.ProfessorClient;
import application.professor.ScheduledGetStudent;
import application.professor.StudentsTableModel;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

public class ProfessorVotesGestionPage {
	//*****************************************MAIN PAGE************************************//
	private String studentName="";
	private String studentSurname="";
	private String studentUsername="";
	private String studentBornDate="";
	
	private ScheduledGetStudent refreshStudents= new ScheduledGetStudent();
	
   
	
	
	@FXML
    private BorderPane mainPane;
	
	@FXML
	private TableView<StudentsTableModel> studentsTable;
	@FXML
	private TableColumn<String, StudentsTableModel> surnameColumn;
	@FXML
    private TableColumn<String, StudentsTableModel> bornDateColumn;
	@FXML
    private TableColumn<String, StudentsTableModel> currentVoteColumn;
	@FXML
    private TableColumn<String, StudentsTableModel> nameColumn;
	@FXML
    private Button updateSelectedButton;
	@FXML
	private Button backButton;
	@FXML
    private ImageView logoView;
	
	private ObservableList<StudentsTableModel> tableList= FXCollections.observableArrayList();
	
    //************************************************UPDATE SECTION******************//

	@FXML
    private BorderPane updatePane;
	
	@FXML
    private Label studentLabelUpdate;
	@FXML
	private Label voteLabelUpdate;
	
    @FXML
    private TextField updateVoteField;

    @FXML
    private Button updateVote;
    
    @FXML
    private Button backUpdateButton;

    
    

   

    
    //bottone per passare al form di modifica voto
    @FXML
    void updateSelectedClicked(ActionEvent event) 
    {
    	
    	StudentsTableModel student=studentsTable.getSelectionModel().getSelectedItem();
    	if(student==null)
    	{
    		SceneHandler.getInstance().showWarning("C'è stato un problema con la scelta, assicurati di aver scelto uno studente della tabella");
    		return;
    	}
    	else
    	{
    		updatePane.setVisible(true);
    		studentName=student.getNome();
    		studentSurname=student.getCognome();
    		studentBornDate=student.getDataNascita();
    		studentUsername=student.getUsername();
    		studentLabelUpdate.setText(studentName+" "+studentSurname+"\nNato il: "+studentBornDate);
    		voteLabelUpdate.setText("Voto corrente: "+student.getVoto());
        	mainPane.setOpacity(0.2);
        	updatePane.setOpacity(1);
        	
    	}
    		
    	

    }


    //aggiornamento nuovo voto su database
    @FXML
    void updateVoteClicked(ActionEvent event) 
    {
    	try
    	{
    		Integer newVote= Integer.parseInt(updateVoteField.getText());
    		if(newVote<2 || newVote>10)
    		{
    			SceneHandler.getInstance().showWarning("Sembra esserci un problema con il voto inserito, assicurati che sia compreso tra 2 e 10");
    			return;
    			
    		}
    		
    		if(ProfessorClient.getInstance().updateStudentVote(studentUsername, newVote))
    		{
    			
    			SceneHandler.getInstance().showInformation("Il voto di "+studentName+" "+ studentSurname +" è stato aggiornato correttamente");
    			studentName="";
    			studentSurname="";
    			studentUsername="";
    			studentBornDate="";
    			tableList=ProfessorClient.getInstance().getStudentsList();
    			studentsTable.setItems(tableList);
    			updateVoteField.setText("");
    			updateVoteField.setPromptText("Nuovo voto...");
    			mainPane.setOpacity(1);
            	updatePane.setOpacity(0);
            	updatePane.setVisible(false);
    			
    		}
    			
    			
    			
    	}
    	catch (NumberFormatException e) 
    	{
    	    SceneHandler.getInstance().showWarning("Sembra esserci un problema con il voto inserito, assicurati che sia nel formato corretto");
    	    return;
    	}

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
			
    		System.out.println("Problema con il caricamento della home page del professore");
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
				studentsTable.setItems(tableList);
				
			}
		});
	 	
	
		   
		refreshStudents.start();
		
    	updatePane.setVisible(false);
    	tableList= ProfessorClient.getInstance().getStudentsList();
    	nameColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
    	surnameColumn.setCellValueFactory(new PropertyValueFactory<>("cognome"));
    	bornDateColumn.setCellValueFactory(new PropertyValueFactory<>("dataNascita"));
    	currentVoteColumn.setCellValueFactory(new PropertyValueFactory<>("voto"));
    	studentsTable.setItems(tableList);
    	updatePane.setStyle("-fx-border-color: #00adb5");
    	logoView.imageProperty().set(new Image(getClass().getResourceAsStream("/loginResources/logoLogin.jpg"))); 
    }
    
    @FXML
    void backUpdateClicked(ActionEvent event) 
    {
    	updateVoteField.setText("");
    	updateVoteField.setPromptText("Nuovo voto...");
    	mainPane.setOpacity(1);
    	updatePane.setOpacity(0);
    }



}
