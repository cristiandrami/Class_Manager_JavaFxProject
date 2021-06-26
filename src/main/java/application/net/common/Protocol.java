package application.net.common;

public class Protocol {
	public final static String LOGIN= "login";
	public final static String REGISTRATION= "registration";
	public static final String GETTYPEFROMCODE = "get type from code";
	public static final String GETCLASSFROMCODE = "get class from code";
	public static final String LOGOUT = "loggout";
	
	//************************************ PROFESSOR *******************************//
	public final static String GETSTUDENTSFORPROF = "get students for professor";
	public static final String SENDASSIGNMENT = "send assignment";
	public static final String UPDATESTUDENTVOTE = "update student vote";
	public static final String INSERTNOTE = "insert note";
	public static final String GETCLASS = "get class";
	public static final String GETPROFESSOROBJECT = "get object";
	public static final String GET_PROFESSOR = "get professor info";
	public final static String SUBJECT_ERROR= "La materia inserita non è presente nel database della scuola, riprova";
	public static final String PROFESSOR_SUBJECT_EXISTS = "Sembra ci sia già un professore registrato per la stessa classe con la stessa materia di insegnamento, assicurati di aver inserito correttamente tutti i dati...";
	
	
	//*************************************STUDENT**********************************//
	public static final String GETVOTES = "get votes";
	public static final String GETASSIGNMENTS = "get assignments";
	public static final String GETNOTES = "get notes";
	public static final String GETSTUDENTINFO = "get student info";
	
	
	public final static String OK= "ok";
	
	public final static String ERROR= "Errore durante la connessione al server... riprova più tardi";
	public final static String AUTHENTICATION_ERROR= "L'username o la password sono errate, assicurati di averli inseriti correttamente";
	public final static String USER_LOGGED_ERROR= "L'utente è già loggato";
	public final static String USER_EXISTS_ERROR= "L'username è già stato scelto, riprova con uno nuovo";
	
	
	

	
	

	
	


	
	

	
	
	
	
	
	


}