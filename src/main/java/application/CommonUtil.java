package application;

public class CommonUtil 
{
	//******************************* REGISTRATION **************************//
		
		public static final String CODEERROR = "Il codice non risulta presente, assicurati di averlo inserito correttamente...";
		public static final String PASSWORDNOTMATCH = "Le password inserite non coincidono, assicurati che siano uguali...";
		public static final String PASSWORDNOTVALID = "La password scelta non rispetta le condizioni di sicurezza, scegliene una più sicura...";
		public static final String USERNAMENOTVALID = "L'username scelto può contenere solo lettere e numeri...";
		public static final String USERNOTEXISTS = "Username non valido, perfavore assicurati di averlo inserito correttamente";
		public static final String NAMENOTVALID = "Il nome può contenere solo lettere e spazi, trattini o virgole per il secondo nome...";
		public static final String SURNAMENOTVALID = "Il cognome può contenere solo lettere e spazi, trattini o virgole per il secondo cognome...";;
		
		public static final String CHECKOKCOLOR = "-fx-background-color: #39ff39";
		public static final String CHECKNOTOKCOLOR = "-fx-background-color: #ff4040";
		public static final String USERNAMEPATTERN = "^[a-zA-Z]+[a-zA-Z0-9]*$";
		public static final String PASSWORDPATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$";
		public static final String NAMEPATTERN = "^[a-zA-Z-,]+(\\s{0,1}[a-zA-Z-, ])*$";
		
		
		//******************************* LOGIN **************************//
		
		public static final String PROFTYPE = "professore";
		public static final String STUDENTTYPE = "studente";
		
		
		//************************************************ COMMON ****************************************//
		public static final String PDFCREATED = "PDF correttamente creato e salvato!";
		
		
		
}
