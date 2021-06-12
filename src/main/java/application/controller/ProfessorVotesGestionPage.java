package application.controller;

import application.StudentsTableModel;
import application.net.client.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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

public class ProfessorVotesGestionPage {
	//*****************************************MAIN PAGE************************************//
	
	
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
    
    

   

    
    //bottone per passare al form di modifica voto
    @FXML
    void updateSelectedClicked(ActionEvent event) 
    {
    	mainPane.setOpacity(0.2);
    	updatePane.setOpacity(1);
    	

    }


    //aggiornamento nuovo voto su database
    @FXML
    void updateVoteClicked(ActionEvent event) 
    {

    }
    
    @FXML
    void initialize()
    {
    	tableList= Client.getInstance().getStudentsList();
    	nameColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
    	surnameColumn.setCellValueFactory(new PropertyValueFactory<>("cognome"));
    	bornDateColumn.setCellValueFactory(new PropertyValueFactory<>("dataNascita"));
    	currentVoteColumn.setCellValueFactory(new PropertyValueFactory<>("voto"));
    	studentsTable.setItems(tableList);
    	updatePane.setStyle("-fx-border-color: #00adb5");
    	logoView.imageProperty().set(new Image(getClass().getResourceAsStream("/loginResources/logoLogin.jpg"))); 
    }



}

