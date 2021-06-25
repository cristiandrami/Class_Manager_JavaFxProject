package application;

import java.awt.event.MouseAdapter;
import java.io.IOException;
import java.util.Optional;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SceneHandler 
{
	private Scene scene;
    private Stage stage;
    
    private double xPosition;
    private double yPosition;
	
	private static SceneHandler instance = null;
	
	private SceneHandler() {}
	
	public void init(Stage primaryStage) throws Exception 
	{		
		stage = primaryStage;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/view/common/Login.fxml"));
    	Parent root = (Parent) loader.load();
    	//addMouseDragging(root);
		scene = new Scene(root);
		stage.setScene(scene);
		stage.setMinHeight(450);
		stage.setMinWidth(700);
		stage.setTitle("Class Manager");
		scene.getStylesheets().clear();
		scene.getStylesheets().add(getClass().getResource("/css/commonFont.css").toExternalForm());
		stage.setResizable(false);
		stage.show();
	}
	
	public static SceneHandler getInstance() 
	{
		if(instance == null)
			instance = new SceneHandler();
		return instance;
	}        
    
    public void setRegistration() throws Exception 
    {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/view/common/registrationForm.fxml"));
    	Parent root = (Parent) loader.load();    	
    	//addMouseDragging(root);
		scene.setRoot(root);
		stage.setScene(scene);
		stage.setTitle("Class Manager");
		stage.setHeight(805);
		stage.setWidth(700);
		stage.setResizable(false);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(getClass().getResource("/css/registrationForm.css").toExternalForm());
		stage.show();
    }
    public void setLogin() throws Exception 
    {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/view/common/Login.fxml"));
    	Parent root = (Parent) loader.load();
    	//addMouseDragging(root);
		scene.setRoot(root);
		stage.setScene(scene);
		stage.setTitle("Class Manager");
		scene.getStylesheets().clear();
		scene.getStylesheets().add(getClass().getResource("/css/commonFont.css").toExternalForm());
		stage.setHeight(450);
		stage.setWidth(700);
		stage.setResizable(false);
		stage.show();
    }
    
    public void setProfHomePage() throws IOException 
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/view/professor/homePageProf.fxml"));

    	Parent root = (Parent) loader.load();    	
    	//addMouseDragging(root);
		scene.setRoot(root);
		stage.setScene(scene);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(getClass().getResource("/css/homePage.css").toExternalForm());
		stage.setHeight(760);
		stage.setWidth(1050);
		stage.setTitle("Class Manager");
		//stage.initStyle(StageStyle.UNDECORATED);
		stage.setResizable(true);
		stage.show();

		
	}
    public void setStudentViewProfessor() throws IOException
    {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/view/professor/studentsViewProf.fxml"));

    	Parent root = (Parent) loader.load();    	
    	//addMouseDragging(root);
		scene.setRoot(root);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(getClass().getResource("/css/tableView.css").toExternalForm());
		stage.setHeight(760);
		stage.setWidth(1050);
		stage.setTitle("Class Manager");
		
		stage.setResizable(true);
		stage.show();

    }
    
    public void setAssignmnetProfPage() throws IOException 
    {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/view/professor/assignmentProfessor.fxml"));

    	Parent root = (Parent) loader.load();    	
    	//addMouseDragging(root);
		scene.setRoot(root);
		stage.setHeight(760);
		stage.setWidth(1050);
		stage.setTitle("Class Manager");
		scene.getStylesheets().clear();
		scene.getStylesheets().add(getClass().getResource("/css/tableView.css").toExternalForm());
		stage.setResizable(true);
		stage.show();
		
		
	}
    
    public void setProfessorVotesGestionPage() throws IOException 
    {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/view/professor/professorVotesGestionPage.fxml"));

    	Parent root = (Parent) loader.load();    	
    	//addMouseDragging(root);
		scene.setRoot(root);
		stage.setHeight(760);
		stage.setWidth(1050);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(getClass().getResource("/css/tableView.css").toExternalForm());
		stage.setTitle("Class Manager");
		
		stage.setResizable(true);
		stage.show();
		
		
	}
    
	public void setStudentHomePage() throws IOException 
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/view/student/homePageStudent.fxml"));

    	Parent root = (Parent) loader.load();    	
    	//addMouseDragging(root);
		scene.setRoot(root);
		stage.setScene(scene);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(getClass().getResource("/css/homePage.css").toExternalForm());
		stage.setHeight(760);
		stage.setWidth(1050);
		stage.setTitle("Class Manager");
		//stage.initStyle(StageStyle.UNDECORATED);
		stage.setResizable(true);
		stage.show();
		
		
	}
	public void setPerformanceStudentPage() throws IOException 
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/view/student/studentPerformance.fxml"));

    	Parent root = (Parent) loader.load();    	
    	//addMouseDragging(root);
		scene.setRoot(root);
		stage.setScene(scene);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(getClass().getResource("/css/tableView.css").toExternalForm());
		stage.setHeight(760);
		stage.setWidth(1050);
		stage.setTitle("Class Manager");
		//stage.initStyle(StageStyle.UNDECORATED);
		stage.setResizable(true);
		stage.show();
		
	}

	public void setAssignmentStudentPage() throws IOException 
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/view/student/studentsAssignment.fxml"));

    	Parent root = (Parent) loader.load();    	
    	//addMouseDragging(root);
		scene.setRoot(root);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(getClass().getResource("/css/assignmentStudent.css").toExternalForm());
		stage.setScene(scene);
		stage.setHeight(760);
		stage.setWidth(1050);
		stage.setTitle("Class Manager");
		//stage.initStyle(StageStyle.UNDECORATED);
		stage.setResizable(true);
		stage.show();
		
	}
	public void setNotesStudentPage() throws IOException 
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/view/student/studentsNotes.fxml"));

    	Parent root = (Parent) loader.load();    	
    	//addMouseDragging(root);
		scene.setRoot(root);
		
		stage.setScene(scene);
		stage.setHeight(760);
		stage.setWidth(1050);
		stage.setTitle("Class Manager");
		scene.getStylesheets().clear();
		scene.getStylesheets().add(getClass().getResource("/css/notesStudent.css").toExternalForm());
		//stage.initStyle(StageStyle.UNDECORATED);
		stage.setResizable(true);
		stage.show();
		
		
	}

	
   
    
    public void showError(String message) 
    {
    	
    	Alert alert = new Alert(AlertType.ERROR);
    	alert.initOwner(stage);
    	alert.setTitle("Errore");
		alert.setHeaderText("");
		alert.setContentText(message);
		DialogPane dialog=alert.getDialogPane();
		dialog.getStylesheets().add(getClass().getResource("/css/alert.css").toExternalForm());
		alert.showAndWait();
		
    }
    public void showWarning(String message)
    {
    	Alert alert = new Alert(AlertType.WARNING);
    	alert.initOwner(stage);
		alert.setTitle("Attenzione");
		alert.setHeaderText("");
		alert.setContentText(message);
		DialogPane dialog=alert.getDialogPane();
		dialog.getStylesheets().add(getClass().getResource("/css/alert.css").toExternalForm());
		alert.showAndWait();
    	
    }
    
    public void showInformation(String message) 
    {
    	Alert alert = new Alert(AlertType.INFORMATION);
    	alert.initOwner(stage);
		alert.setTitle("Perfetto");
		alert.setHeaderText("");
		alert.setContentText(message);
		DialogPane dialog=alert.getDialogPane();
		dialog.getStylesheets().add(getClass().getResource("/css/alert.css").toExternalForm());
		alert.showAndWait();
		
		
    }

	public Optional<ButtonType> showYesNoDialog(String request, String title) 
	{
		Alert alert = new Alert(AlertType.CONFIRMATION, request, ButtonType.YES, ButtonType.NO);
		alert.initOwner(stage);
		alert.setTitle(title);
		alert.setHeaderText("");
		DialogPane dialog=alert.getDialogPane();
		dialog.getStylesheets().add(getClass().getResource("/css/alert.css").toExternalForm());

		return alert.showAndWait();
		
	}

	

	

    
  /*  private void addMouseDragging(Parent root)
    {
    	root.setOnMousePressed(new EventHandler<MouseEvent>() 
    	{

			@Override
			public void handle(MouseEvent event) 
			{
				xPosition=event.getSceneX();
				yPosition=event.getSceneY();
				
			}
    		
    		
		});
    	
    	root.setOnMouseDragged(new EventHandler<MouseEvent>() 
    	{

			@Override
			public void handle(MouseEvent event) 
			{
				stage.setX(event.getScreenX()-xPosition);
				stage.setY(event.getScreenY()-yPosition);
				
			}
		});
    	
    }
    */

	

	

	

}
