package application.controller.professor;

import java.io.IOException;
import java.util.Optional;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.BoundsAccessor;

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
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
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

    @FXML
    private Label classLabel;
	
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
    		SceneHandler.getInstance().showWarning(ProfessorUtil.STUDENT_CHOOSE_PROBLEM);
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
        	//mainPane.setOpacity(0.2);
    		mainPane.setEffect(new GaussianBlur());
        	updatePane.setOpacity(1);
        	mainPane.setDisable(true);
    	}
    		
    	

    }


    //aggiornamento nuovo voto su database
    @FXML
    void updateVoteClicked(ActionEvent event) 
    {
    	Optional<ButtonType> result= SceneHandler.getInstance().showYesNoDialog(ProfessorUtil.VOTE_YES_NO+ studentName+ " "+studentSurname);
    	
    	if(result.get()== ButtonType.YES)
    	{

	    	try
	    	{
	    		Integer newVote= Integer.parseInt(updateVoteField.getText());
	    		if(newVote<2 || newVote>10)
	    		{
	    			SceneHandler.getInstance().showWarning(ProfessorUtil.VOTE_PROBLEM);
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
	    			updateVoteField.setPromptText(ProfessorUtil.NEW_VOTE_PROMPT_TEXT);
	    			//mainPane.setOpacity(1);
	            	updatePane.setOpacity(0);
	            	mainPane.effectProperty().set(null);
	            	updatePane.setVisible(false);
	            	mainPane.setDisable(false);
	    			
	    		}
	    			
	    			
	    			
	    	}
	    	catch (NumberFormatException e) 
	    	{
	    	    SceneHandler.getInstance().showWarning(ProfessorUtil.VOTE_FORMAT_PROBLEM);
	    	    return;
	    	}
    	}
    	else
    		return;

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
			
    		System.out.println(ProfessorUtil.BACK_TO_HOME_PROBLEM);
		}
    }
    
    @FXML
    void initialize()
    {
    	
		refreshStudentsTable();
		
    	tableList= ProfessorClient.getInstance().getStudentsList();
    	nameColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
    	surnameColumn.setCellValueFactory(new PropertyValueFactory<>("cognome"));
    	bornDateColumn.setCellValueFactory(new PropertyValueFactory<>("dataNascita"));
    	currentVoteColumn.setCellValueFactory(new PropertyValueFactory<>("voto"));
    	studentsTable.setItems(tableList);

    	updatePane.setVisible(false);
    	updatePane.setEffect(new DropShadow());
    	logoView.imageProperty().set(new Image(getClass().getResourceAsStream("/images/genericLogo.png"))); 
    	classLabel.setText(ProfessorClient.getInstance().getClasse());


    }
    
    private void refreshStudentsTable() 
    {
    	refreshStudents.setPeriod(Duration.seconds(30));
  	   
	 	refreshStudents.setDelay(Duration.seconds(0.1));

	 	refreshStudents.setOnSucceeded(new EventHandler<WorkerStateEvent>() 
	 	{
			
			@Override
			public void handle(WorkerStateEvent event) 
			{
				tableList= (ObservableList<StudentsTableModel>) event.getSource().getValue();
				if(tableList!=null)
				studentsTable.setItems(tableList);
				
			}
		});
	 	
	
		   
		refreshStudents.start();
	}


	@FXML
    void backUpdateClicked(ActionEvent event) 
    {
    	updateVoteField.setText("");
    	updateVoteField.setPromptText(ProfessorUtil.NEW_VOTE_PROMPT_TEXT);
    	//mainPane.setOpacity(1);
    	mainPane.effectProperty().set(null);
    	updatePane.setOpacity(0);
    	updatePane.setVisible(false);
    	mainPane.setDisable(false);
    }

	


}

