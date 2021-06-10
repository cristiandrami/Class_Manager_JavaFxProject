package application.controller;

import application.StudentsTableModel;
import application.net.client.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class StudentViewTableController  
{
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
	    void onCloseClicked(ActionEvent event) {
	    	Client.getInstance().reset();
	    	System.exit(0);

	    }
	    
	    
	    private ObservableList<StudentsTableModel> tableList= FXCollections.observableArrayList();



	    
	    @FXML
	    void initialize()
	    {
	    	
	    	sufficientLabel.setText(Client.getInstance().getSufficientStudents());
	    	insufficientLabel.setText(Client.getInstance().getUnsufficientStudents());
	    	totalStudentsLabel.setText(Client.getInstance().getTotalStudents());
	    	
	    	tableList= Client.getInstance().getStudentsList();
	    	

	    	logoView.imageProperty().set(new Image(getClass().getResourceAsStream("/loginResources/logoLogin.jpg"))); 
	    	nameColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
	    	surnameColumn.setCellValueFactory(new PropertyValueFactory<>("cognome"));
	    	bornDateColumn.setCellValueFactory(new PropertyValueFactory<>("dataNascita"));
	    	voteColumn.setCellValueFactory(new PropertyValueFactory<>("voto"));
	    	tableView.setItems(tableList);
	    }


}
