package application.net.common;

public class Protocol {
	public final static String LOGIN= "login";
	public final static String REGISTRATION= "registration";
	public final static String OK= "ok";
	public final static String ERROR= "error during connection";
	public final static String AUTHENTICATION_ERROR= "invalid username/password";
	public final static String USER_LOGGED_ERROR= "user already logged in";
	public final static String USER_EXISTS_ERROR= "user already exist";
	
	//il client invia al server o "login" o "registration" 
	// se i client invia login allora il server si predispone al login 
	// il client manda user e password
	// il server verifica l'autenticazione con il dbms
	
	// se il client manda registration allroa il server si predispone alla registrazione
	//il server ottiene user e pass e li aggiunge al database
	
	//il server invece manda al client login ok oppure user pass sbagliata oppure è già stato effettuato il login
	
	//registrazione ok oppure username già esistente 
	


}