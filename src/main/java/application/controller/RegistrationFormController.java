package application.controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.javafx.scene.control.DatePickerContent;

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
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;


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
			SceneHandler.getInstance().showWarning(CommonUtil.CODE_ERROR);
			return;
		}
		
		if(!checkRepeatPass(passwordField.getText(), repeatPasswordField.getText()))
		{
			SceneHandler.getInstance().showWarning(CommonUtil.PASSWORD_NOT_MATCH);
			return;
		}
		
		else if(!validatePassword(passwordField.getText()))
		{
			SceneHandler.getInstance().showWarning(CommonUtil.PASSWORD_NOT_VALID);
			return;
		}
		else if(!validateUsername(username.getText()))
		{
			SceneHandler.getInstance().showWarning(CommonUtil.USERNAME_NOT_VALID);
			return;
		}
		else if(!validateName(nameField.getText()))
		{
			SceneHandler.getInstance().showWarning(CommonUtil.NAME_NOT_VALID);
			return;
		}
		else if(!validateSurname(surnameField.getText()))
		{
			SceneHandler.getInstance().showWarning(CommonUtil.SURNAME_NOT_VALID);
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
					repeatPasswordField.styleProperty().set(CommonUtil.CHECK_OK_COLOR);
				else
					repeatPasswordField.styleProperty().set(CommonUtil.CHECK_NOT_OK_COLOR);
					
				
			}
		});
    	
    	username.textProperty().addListener(new ChangeListener<String>() 
    	{

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
			{
				
				if(validateUsername(newValue))
					username.styleProperty().set(CommonUtil.CHECK_OK_COLOR);
				else
					username.styleProperty().set(CommonUtil.CHECK_NOT_OK_COLOR);
					
				
			}
		});
    	passwordField.textProperty().addListener(new ChangeListener<String>() 
    	{

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
			{
				
				if(checkRepeatPass(newValue, repeatPasswordField.getText()))
					repeatPasswordField.styleProperty().set(CommonUtil.CHECK_OK_COLOR);
				else
					repeatPasswordField.styleProperty().set(CommonUtil.CHECK_NOT_OK_COLOR);
					
				
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
    	usernamePat= Pattern.compile(CommonUtil.USERNAME_PATTERN);
    	passPat=Pattern.compile(CommonUtil.PASSWORD_PATTERN);
    	namePat=Pattern.compile(CommonUtil.NAME_PATTERN);
    	addListeners();
    	datePicker.getStyleClass().add("myData");
    	
    	materiaField.setVisible(false);
    	materiaLabel.setVisible(false);
    	
    	setDateMaximumValue();
    	
    }
    
    private void setDateMaximumValue() 
    {
    	//ora mi setto la data massima da scegliere nel date picker perchè voglio che parta dal 2004
    	//uso un oggetto date picker per impostargli il valore di 1 gennaio 2004
    	DatePicker maxDate = new DatePicker(); 
    	maxDate.setValue(LocalDate.of(2009, Month.JANUARY, 1)); 
    	final Callback<DatePicker, DateCell> dayCellFactory;
    	
    	//mi setto le celle del data picker che usero in modo che possa scegliere solo quelle prima del 2008
    	dayCellFactory = (final DatePicker datePicker) -> new DateCell() {
    	    @Override
    	    public void updateItem(LocalDate item, boolean empty) {
    	        super.updateItem(item, empty);
    	        //disabilito tutte le date dopo il 2008 e gli associo un colore rosso chiaro
    	        if(item.isAfter(maxDate.getValue())) 
    	        {
    	            setDisable(true);
    	            setStyle("-fx-background-color: #ffc0cb;"); 
    	        }
    	    }
    	};
    	//qui gli sto dicendo che deve seguire le condizioni gestite precedentemente con il cell factory
    	datePicker.setDayCellFactory(dayCellFactory);
    	
    	// a questo punto quando lo apro voglio che mi venga mostrata una data del 2008 
    	datePicker.setOnShown(e -> {
    	    if (datePicker.getValue() == null && datePicker.getSkin() instanceof DatePickerSkin) {
    	        DatePickerSkin skin = (DatePickerSkin) datePicker.getSkin();
    	        DatePickerContent popupContent = (DatePickerContent) skin.getPopupContent();
    	        popupContent.goToDate(LocalDate.now().minusYears(13), true);
    	    }
    	});
		
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


