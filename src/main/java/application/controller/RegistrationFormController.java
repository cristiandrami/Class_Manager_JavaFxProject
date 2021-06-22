package application.controller;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import application.CommonUtil;
import application.SceneHandler;
import application.net.client.CommonClient;
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
import javafx.scene.control.Label;
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
	Pattern namePat;
	
	@FXML
    private Button registerButton;
	
    @FXML
    private Label materiaLabel;
    
    @FXML
    private Button backButton;

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
    	
		String type=CommonClient.getInstance().getTypeFromCode(codeField.getText());
		String classe= CommonClient.getInstance().getClassFromCode(codeField.getText());
		
		
		//String classe="1A";
		//String type="professore";
		if(type.equals("") || classe.equals(""))
		{
			SceneHandler.getInstance().showWarning(CommonUtil.CODEERROR);
			return;
		}
		
		if(!checkRepeatPass(passwordField.getText(), repeatPasswordField.getText()))
		{
			SceneHandler.getInstance().showWarning(CommonUtil.PASSWORDNOTMATCH);
			return;
		}
		
		else if(!validatePassword(passwordField.getText()))
		{
			SceneHandler.getInstance().showWarning(CommonUtil.PASSWORDNOTVALID);
			return;
		}
		else if(!validateUsername(username.getText()))
		{
			SceneHandler.getInstance().showWarning(CommonUtil.USERNAMENOTVALID);
			return;
		}
		else if(!validateName(nameField.getText()))
		{
			SceneHandler.getInstance().showWarning(CommonUtil.NAMENOTVALID);
			return;
		}
		else if(!validateSurname(surnameField.getText()))
		{
			SceneHandler.getInstance().showWarning(CommonUtil.SURNAMENOTVALID);
			return;
		}
			
		else	
		registration(type, classe);
			
		
    }
	@FXML
    void backButtonClicked(ActionEvent event) 
    {
    	try 
    	{
			SceneHandler.getInstance().setLogin();
			ProfessorClient.getInstance().reset();
			StudentClient.getInstance().reset();
			CommonClient.getInstance().reset();
		} 
    	catch (Exception e) 
    	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
                		CommonClient.getInstance().reset();
                	}
    				
    			}
    			else if(type.equals(STUDENTTYPE))
    			{
    				String result= StudentClient.getInstance().registration(usern, name, surname, password, date, classe, type, code, materia);
        			
                	if(result.equals(Protocol.OK))
                	{
                		try 
                		{

          					SceneHandler.getInstance().setStudentHomePage();

            			} 
                		catch (Exception e) 
                		{
            				
            				SceneHandler.getInstance().showError("Error during loading dashboard");
            			}
                		
                	}
                	else
                	{
                		SceneHandler.getInstance().showError(result);
                		StudentClient.getInstance().reset();
                		CommonClient.getInstance().reset();
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
					repeatPasswordField.styleProperty().set(CommonUtil.CHECKOKCOLOR);
				else
					repeatPasswordField.styleProperty().set(CommonUtil.CHECKNOTOKCOLOR);
					
				
			}
		});
    	
    	username.textProperty().addListener(new ChangeListener<String>() 
    	{

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
			{
				
				if(validateUsername(newValue))
					username.styleProperty().set(CommonUtil.CHECKOKCOLOR);
				else
					username.styleProperty().set(CommonUtil.CHECKNOTOKCOLOR);
					
				
			}
		});
    	passwordField.textProperty().addListener(new ChangeListener<String>() 
    	{

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
			{
				
				if(checkRepeatPass(newValue, repeatPasswordField.getText()))
					repeatPasswordField.styleProperty().set(CommonUtil.CHECKOKCOLOR);
				else
					repeatPasswordField.styleProperty().set(CommonUtil.CHECKNOTOKCOLOR);
					
				
			}
		});
    	codeField.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
			{
				
				if(CommonClient.getInstance().getTypeFromCode(newValue).equals(PROFTYPE))
				{
					materiaField.setVisible(true);
	    			materiaLabel.setVisible(true);
				}
				else
				{
					materiaField.setVisible(false);
					materiaLabel.setVisible(false);
					
				}
				
				
			}
		});
    }

    
    @FXML
    void initialize()
    {
    	
    	logoView.imageProperty().set(new Image(getClass().getResourceAsStream("/registrationResources/registrationLogo.jpg"))); 
    	usernamePat= Pattern.compile(CommonUtil.USERNAMEPATTERN);
    	passPat=Pattern.compile(CommonUtil.PASSWORDPATTERN);
    	namePat=Pattern.compile(CommonUtil.NAMEPATTERN);
    	addListeners();
    	datePicker.getStyleClass().add("myData");
    	materiaField.setVisible(false);
    	materiaLabel.setVisible(false);
    	
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
    
    private boolean validateName(String name) 
    {
    	Matcher match= namePat.matcher(name);
    	return match.matches();
  	}

	private boolean validateSurname(String surname) 
	{
		Matcher match= namePat.matcher(surname);
    	return match.matches();
	}

   


}


