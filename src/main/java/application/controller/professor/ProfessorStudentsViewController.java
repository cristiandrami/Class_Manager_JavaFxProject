package application.controller.professor;

import java.io.IOException;

import application.SceneHandler;
import application.net.client.ProfessorClient;
import application.professor.ProfessorUtil;
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
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

public class ProfessorStudentsViewController 
{

	private ObservableList<StudentsTableModel> tableList= FXCollections.observableArrayList();
	private ScheduledGetStudent refreshStudents= new ScheduledGetStudent();
	private String studentName="";
	private String studentSurname="";
	private String studentUsername="";
	private String studentBornDate="";

    @FXML
    private Label studentLabelNotePane;

    @FXML
    private TextField insertNoteField;
	
	@FXML
    private BorderPane mainPane;
    @FXML
    private BorderPane notePane;

    @FXML
    private Button insertNote;
    @FXML
    private Button backNoteButton;
 	@FXML
    private Label totalStudentsLabel;
 	
    @FXML
    private Label insufficientLabel;
    @FXML
    private Label sufficientLabel;

    @FXML
    private ImageView logoView;

    
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
			System.out.println(ProfessorUtil.BACKTOHOMEPROBLEM);
		}
    	
    }
    
    @FXML
    void showNotePaneClicked(ActionEvent event) 
    {
    	StudentsTableModel student=tableView.getSelectionModel().getSelectedItem();
    	if(student==null)
    	{
    		SceneHandler.getInstance().showWarning(ProfessorUtil.STUDENTCHOOSEPROBLEM);
    		return;
    	}
    	else
    	{
    		notePane.setVisible(true);
    		studentName=student.getNome();
    		studentSurname=student.getCognome();
    		studentBornDate=student.getDataNascita();
    		studentUsername=student.getUsername();
    	    studentLabelNotePane.setText(studentName+" "+studentSurname+"\nNato il: "+studentBornDate);
        	//mainPane.setOpacity(0.2);
    		mainPane.setEffect(new GaussianBlur());
        	notePane.setOpacity(1);
        	
    	}

    }
    
    @FXML
    void insertNoteclicked(ActionEvent event) 
    {
    	if(insertNoteField.getText().equals(""))
    	{
    		SceneHandler.getInstance().showWarning(ProfessorUtil.NOTEPROBLEM);
    	}
    	else if(ProfessorClient.getInstance().insertStudentNote(studentUsername, insertNoteField.getText()))
		{
			
			SceneHandler.getInstance().showInformation("La nota disciplinare per "+studentName+" "+ studentSurname +" è stata inserita correttamente");
			studentName="";
			studentSurname="";
			studentUsername="";
			studentBornDate="";
			insertNoteField.setText("");
			insertNoteField.setPromptText(ProfessorUtil.PROMTTEXTNOTES);
			//mainPane.setOpacity(1);
        	notePane.setOpacity(0);
        	mainPane.effectProperty().set(null);
        	notePane.setVisible(false);
			
		}

    }
    

    @FXML
    void backNoteClicked(ActionEvent event) 
    {
    	insertNoteField.setText("");
    	insertNoteField.setPromptText(ProfessorUtil.PROMTTEXTNOTES);
    	mainPane.effectProperty().set(null);
    	notePane.setOpacity(0);
    }

    
    @FXML
    void initialize()
    {
    	refreshStudentsList();
    	logoView.imageProperty().set(new Image(getClass().getResourceAsStream("/loginResources/logoLogin.jpg"))); 
    	nameColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
    	surnameColumn.setCellValueFactory(new PropertyValueFactory<>("cognome"));
    	bornDateColumn.setCellValueFactory(new PropertyValueFactory<>("dataNascita"));
    	voteColumn.setCellValueFactory(new PropertyValueFactory<>("voto"));
    	notePane.setVisible(false);
    	notePane.setEffect(new DropShadow());
    
    }
    
	private void refreshStudentsList() 
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
				Integer sufficient=0;
				Integer unsufficient=0;
				Integer total=tableList.size();
				for(StudentsTableModel s: tableList)
				{
					
					try 
					 {
					    Integer vote = Integer.parseInt(s.getVoto());
					    if(vote>=6)
					    	sufficient++;
					    else
					    	unsufficient++;
						  
					 }
					 catch (NumberFormatException e) 
					 {
						    
						  
					 }
					
				}
				
				sufficientLabel.setText(sufficient.toString());
				insufficientLabel.setText(unsufficient.toString());
				totalStudentsLabel.setText(total.toString());
			
				
			}
			
		});
	 	

	   
	refreshStudents.start();
		
	}
	

}
