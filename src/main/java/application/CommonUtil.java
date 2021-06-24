package application;

public class CommonUtil 
{
	//******************************* REGISTRATION **************************//
		
		public static final String CODE_ERROR = "Il codice non risulta presente, assicurati di averlo inserito correttamente...";
		public static final String PASSWORD_NOT_MATCH = "Le password inserite non coincidono, assicurati che siano uguali...";
		public static final String PASSWORD_NOT_VALID = "La password scelta non rispetta le condizioni di sicurezza, scegliene una più sicura...";
		public static final String USERNAME_NOT_VALID = "L'username scelto può contenere solo lettere e numeri...";
		public static final String USER_NOT_EXISTS = "Username non valido, perfavore assicurati di averlo inserito correttamente";
		public static final String NAME_NOT_VALID = "Il nome può contenere solo lettere e spazi, trattini o virgole per il secondo nome...";
		public static final String SURNAME_NOT_VALID = "Il cognome può contenere solo lettere e spazi, trattini o virgole per il secondo cognome...";;
		
		public static final String CHECK_OK_COLOR = "-fx-background-color: #39ff39";
		public static final String CHECK_NOT_OK_COLOR = "-fx-background-color: #ff4040";
		public static final String USERNAME_PATTERN = "^[a-zA-Z]+[a-zA-Z0-9]*$";
		public static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$";
		public static final String NAME_PATTERN = "^[a-zA-Z-,]+(\\s{0,1}[a-zA-Z-, ])*$";
		public static final String LOGOUT_YES_NO = "Sei sicuro di voler effettuare il logout?";
		
		
		//******************************* LOGIN **************************//
		
		public static final String PROF_TYPE = "professore";
		public static final String STUDENT_TYPE = "studente";
		
		
		//************************************************ COMMON ****************************************//
		public static final String PDF_CREATED = "PDF correttamente creato e salvato!";
		
		
		
		
}
