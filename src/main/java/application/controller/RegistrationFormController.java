package application.controller;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import application.SceneHandler;
import application.net.client.ProfessorClient;
import application.net.client.StudentClient;
import application.net.common.Protocol;
import application.net.server.DatabaseHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class RegistrationFormController 
{
	//String username,String nome,String cognome,String password, String nascita, String classe, String tipo, String code
	
	
	
	public static final String PROFTYPE="professore";
	public static final String STUDENTTYPE="studente";
	Pattern usernamePat;
	Pattern passPat;
	
	@FXML
    private Button registerButton;
    

    @FXML
    private Button closeButton;


    @FXML
    private TextField codeField;


    @FXML
    private TextField surnameField;

    @FXML
    private PasswordField repeatPasswordField;

    @FXML
    private TextField nameField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private PasswordField passwordField;
    
    @FXML
    private TextField materiaField;

    @FXML
    private TextField username;
    
    @FXML
    private ImageView logoView;

    @FXML
    void registerClicked(ActionEvent event) 
    {
    	try 
    	{
			String type= DatabaseHandler.getInstance().getTypeFromCode(codeField.getText());
			String classe= DatabaseHandler.getInstance().getclassFromCode(codeField.getText());
			
			//String classe="1A";
			//String type="professore";
			if(type.equals("") || classe.equals(""))
			{
				SceneHandler.getInstance().showWarning("Il codice di registrazione inserito non è valido, per favore riprova");
				return;
			}
			
			if(!checkRepeatPass(passwordField.getText(), repeatPasswordField.getText()))
			{
				SceneHandler.getInstance().showWarning("Le password inserite non coincidono");
				return;
			}
			
			if(!validatePassword(passwordField.getText()))
			{
				SceneHandler.getInstance().showWarning("La PASSWORD scelta non rispetta le condizioni sotto elencate");
				return;
			}
			if(!validateUsername(username.getText()))
			{
				SceneHandler.getInstance().showWarning("L'USERNAME scelto non rispetta le condizioni sotto elencate");
				return;
			}
				
				
			registration(type, classe);
			
		} 
    	catch (SQLException e) 
    	{
			SceneHandler.getInstance().showError("Errore con il db per la registrazione");
		}
    }
    
    @FXML
    void onCloseClicked(ActionEvent event) 
    {
    	ProfessorClient.getInstance().reset();
    	System.exit(0);
    
    }

    
    private void registration(String type, String classe)
    {
    	
    		//String username,String nome,String cognome,String password, String nascita, String classe, String tipo, String code
    	
    			String materia=materiaField.getText().toLowerCase();
    			String usern= username.getText();
    			String name= nameField.getText();
    			String surname= surnameField.getText();
    			String password=passwordField.getText();
    			String date = datePicker.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    			String code=codeField.getText();
    			if(type.equals(PROFTYPE))
    			{
    				String result= ProfessorClient.getInstance().registration(usern, name, surname, password, date, classe, type, code, materia);
        			
                	if(result.equals(Protocol.OK))
                	{
                		try 
                		{

          					SceneHandler.getInstance().setProfHomePage();

            			} 
                		catch (Exception e) 
                		{
            				
            				SceneHandler.getInstance().showError("Error during loading dashboard");
            			}
                		
                	}
                	else
                	{
                		SceneHandler.getInstance().showError(result);
                		ProfessorClient.getInstance().reset();
                	}
    				
    			}
    			else if(type.equals(STUDENTTYPE))
    			{
    				String result= StudentClient.getInstance().registration(usern, name, surname, password, date, classe, type, code, materia);
        			
                	if(result.equals(Protocol.OK))
                	{
                		try 
                		{

          					//SceneHandler.getInstance().setProfHomePage();

            			} 
                		catch (Exception e) 
                		{
            				
            				SceneHandler.getInstance().showError("Error during loading dashboard");
            			}
                		
                	}
                	else
                	{
                		SceneHandler.getInstance().showError(result);
                		ProfessorClient.getInstance().reset();
                	}
    				
    			}
    			
	
    	
    }
    
    private void addListeners()
    {
    	repeatPasswordField.textProperty().addListener(new ChangeListener<String>() 
    	{

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
			{
				
				if(checkRepeatPass(newValue, passwordField.getText()))
					repeatPasswordField.styleProperty().set("-fx-background-color: #39ff39");
				else
					repeatPasswordField.styleProperty().set("-fx-background-color: #ff4040");
					
				
			}
		});
    	
    	username.textProperty().addListener(new ChangeListener<String>() 
    	{

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
			{
				
				if(validateUsername(newValue))
					username.styleProperty().set("-fx-background-color: #39ff39");
				else
					username.styleProperty().set("-fx-background-color: #ff4040");
					
				
			}
		});
    	passwordField.textProperty().addListener(new ChangeListener<String>() 
    	{

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
			{
				
				if(checkRepeatPass(newValue, repeatPasswordField.getText()))
					repeatPasswordField.styleProperty().set("-fx-background-color: #39ff39");
				else
					repeatPasswordField.styleProperty().set("-fx-background-color: #ff4040");
					
				
			}
		});
    }

    
    @FXML
    void initialize()
    {
    	
    	logoView.imageProperty().set(new Image(getClass().getResourceAsStream("/loginResources/logoLogin.jpg"))); 
    	usernamePat= Pattern.compile("^[a-zA-Z]+[a-zA-Z0-9]*$");
    	passPat=Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$");
    	addListeners();
    	
    }
    
    private boolean checkRepeatPass(String pass, String repeat)
    {
    	return pass.equals(repeat);
    }
    
    private boolean validateUsername(String username)
    {
    	Matcher match= usernamePat.matcher(username);
    	return match.matches();
    }
    private boolean validatePassword(String pass)
    {
    	Matcher match= passPat.matcher(pass);
    	
    	return match.matches();
    }

}


