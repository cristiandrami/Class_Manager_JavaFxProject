package application;

import java.awt.event.MouseAdapter;
import java.io.IOException;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
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
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/view/Login.fxml"));
    	Parent root = (Parent) loader.load();
    	addMouseDragging(root);
		scene = new Scene(root, 700, 500);
		stage.setScene(scene);
		stage.setTitle("Class Manager");
		stage.initStyle(StageStyle.UNDECORATED);
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
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/view/registrationForm.fxml"));
    	Parent root = (Parent) loader.load();    	
    	addMouseDragging(root);
		scene = new Scene(root, 600, 700);
		stage.setScene(scene);
		stage.setTitle("Class Manager");
		//stage.initStyle(StageStyle.UNDECORATED);
		stage.setResizable(false);
		stage.show();
    }
   
    
    public void showError(String message) 
    {
    	Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Errore");
		alert.setHeaderText("");
		alert.setContentText(message);
		alert.showAndWait();
    }
    public void showWarning(String message)
    {
    	Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Attenzione");
		alert.setHeaderText("");
		alert.setContentText(message);
		alert.showAndWait();
    	
    }
    
    private void addMouseDragging(Parent root)
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

}
