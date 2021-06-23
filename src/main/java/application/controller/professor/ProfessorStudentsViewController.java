package application.controller.professor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import application.CommonUtil;
import application.SceneHandler;
import application.net.client.ProfessorClient;
import application.professor.ProfessorUtil;
import application.professor.ScheduledGetStudent;
import application.professor.StudentsTableModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.util.Duration;

public class ProfessorStudentsViewController 
{

	private ObservableList<StudentsTableModel> tableList= FXCollections.observableArrayList();
	private ScheduledGetStudent refreshStudents= new ScheduledGetStudent();
	private String studentName="";
	private String studentSurname="";
	private String studentUsername="";
	private String studentBornDate="";
	

    @FXML
    private Label classLabel;

    @FXML
    private Label studentLabelNotePane;

    @FXML
    private TextField insertNoteField;
	
	@FXML
    private BorderPane mainPane;
    @FXML
    private BorderPane notePane;

    @FXML
    private Button insertNote;
    @FXML
    private Button backNoteButton;
 	@FXML
    private Label totalStudentsLabel;
 	
    @FXML
    private Label insufficientLabel;
    @FXML
    private Label sufficientLabel;

    @FXML
    private ImageView logoView;

    
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
    private Button exportPdfBtn;
    
    

    @FXML
    private Button backButton;
    
    @FXML
    void onCloseClicked(ActionEvent event) 
    {
    	ProfessorClient.getInstance().reset();
    	System.exit(0);

    }
    @FXML
    void backClicked(ActionEvent event) 
    {
    	try 
    	{
			SceneHandler.getInstance().setProfHomePage();
		}
    	catch (IOException e) 
    	{
			System.out.println(ProfessorUtil.BACKTOHOMEPROBLEM);
		}
    	
    }
    
    @FXML
    void showNotePaneClicked(ActionEvent event) 
    {
    	StudentsTableModel student=tableView.getSelectionModel().getSelectedItem();
    	if(student==null)
    	{
    		SceneHandler.getInstance().showWarning(ProfessorUtil.STUDENTCHOOSEPROBLEM);
    		return;
    	}
    	else
    	{
    		notePane.setVisible(true);
    		studentName=student.getNome();
    		studentSurname=student.getCognome();
    		studentBornDate=student.getDataNascita();
    		studentUsername=student.getUsername();
    	    studentLabelNotePane.setText(studentName+" "+studentSurname+"\nNato il: "+studentBornDate);
        	//mainPane.setOpacity(0.2);
    		mainPane.setEffect(new GaussianBlur());
        	notePane.setOpacity(1);
        	
    	}

    }
  
    
    @FXML
    void insertNoteclicked(ActionEvent event) 
    {
    	if(insertNoteField.getText().equals(""))
    	{
    		SceneHandler.getInstance().showWarning(ProfessorUtil.NOTEPROBLEM);
    	}
    	else if(ProfessorClient.getInstance().insertStudentNote(studentUsername, insertNoteField.getText()))
		{
			
			SceneHandler.getInstance().showInformation("La nota disciplinare per "+studentName+" "+ studentSurname +" è stata inserita correttamente");
			studentName="";
			studentSurname="";
			studentUsername="";
			studentBornDate="";
			insertNoteField.setText("");
			insertNoteField.setPromptText(ProfessorUtil.PROMTTEXTNOTES);
			//mainPane.setOpacity(1);
        	notePane.setOpacity(0);
        	mainPane.effectProperty().set(null);
        	notePane.setVisible(false);
			
		}

    }
    

    @FXML
    void backNoteClicked(ActionEvent event) 
    {
    	insertNoteField.setText("");
    	insertNoteField.setPromptText(ProfessorUtil.PROMTTEXTNOTES);
    	mainPane.effectProperty().set(null);
    	notePane.setOpacity(0);
    }
    @FXML
    void exportPdfClicked(ActionEvent event) 
    {
    	
    	savePDF();
    	
    }
    private void setPdfImage() 
	{
	     ImageView view = new ImageView(new Image(getClass().getResourceAsStream("/images/pdf.png")));
	     view.setFitHeight(50);
	     view.setFitWidth(50);
	     exportPdfBtn.setGraphic(view);
		
	}
	private void refreshStudentsList() 
	{
		refreshStudents.setPeriod(Duration.seconds(30));
    	   
	 	refreshStudents.setDelay(Duration.seconds(0.1));

	 	refreshStudents.setOnSucceeded(new EventHandler<WorkerStateEvent>() 
	 	{
			
			@Override
			public void handle(WorkerStateEvent event) 
			{
				tableList= (ObservableList<StudentsTableModel>) event.getSource().getValue();
				tableView.setItems(tableList);
				Integer sufficient=0;
				Integer unsufficient=0;
				Integer total=0;
				if(tableList!=null)
				{	total=tableList.size();
					for(StudentsTableModel s: tableList)
					{
						
						try 
						 {
						    Integer vote = Integer.parseInt(s.getVoto());
						    if(vote>=6)
						    	sufficient++;
						    else
						    	unsufficient++;
							  
						 }
						 catch (NumberFormatException e) 
						 {
							    
							  
						 }
						
					}
					
				}
				
				
				sufficientLabel.setText(sufficient.toString());
				insufficientLabel.setText(unsufficient.toString());
				totalStudentsLabel.setText(total.toString());
			
				
			}
			
		});
	 	

	   
	refreshStudents.start();
		
	}
	
	   private void savePDF() 
	   {
		  // creo il nome del pdf;
		   FileChooser chooser= new FileChooser();
		   FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF Files", "*.pdf");
		   chooser.getExtensionFilters().add(extFilter);
		   File fileToSave = chooser.showSaveDialog(null);
		   //System.out.println(fileToSave.getAbsolutePath());
		   try 
		   {
			   		//qui mi creo lo stream sul file in modo da modificarlo
					FileOutputStream streamFile= new FileOutputStream(fileToSave);
					//mi creo un document (oggetto della libreria itext) che mi servirà per modificare il file pdf che vado a creare
					Document document= new Document();
					//con pdfwriter ho la possibilità di settare il documento come pdf modificabile
					PdfWriter.getInstance(document, streamFile);
					//qui do la possibilità di accedere ai dati del documento
					document.open();
					//mi creo una immagine che inserirò al centro pagin aprima della tabella(esce scritto in questo modo poichè ho importato
					// in questa classe anche Image di
					com.itextpdf.text.Image img= com.itextpdf.text.Image.getInstance("src/main/resources/images/logoScuola.jpg");
					img.scaleToFit(200, 300);
					img.setAlignment(com.itextpdf.text.Image.MIDDLE);
					document.add(img);
					
					
					
					//qui mi creo i font che mi servono per differenziare la riga header della tabella, le righe normali e il paragrafo iniziale
					Font classFont =  new Font(FontFamily.COURIER, 20, Font.BOLD);
					Font boldFont =  new Font(FontFamily.COURIER, 12, Font.BOLD);
					Font rowsFont=new Font(FontFamily.COURIER, 12, Font.NORMAL);
					
					//creo il paragrafo che mi dice quale classe gestendo
					Paragraph paragraph= new Paragraph("Studenti della classe "+ ProfessorClient.getInstance().getClasse()+"\n\n", classFont);
					paragraph.setAlignment(Element.ALIGN_CENTER);
					document.add(paragraph);
					paragraph= new Paragraph("Materia: "+ ProfessorClient.getInstance().getMateria()+"\n\n", classFont);
					paragraph.setAlignment(Element.ALIGN_CENTER);
					document.add(paragraph);
					
					// creo un array di float con delle grandezze che mi servono per la lunghezza delle celle
					float[] tableSizes={120f, 120f, 200f, 150f};
					// creo le 4 celle che mi servono e le aggiungo alla tabella
					PdfPTable table= new PdfPTable(tableSizes);
					PdfPCell nameCol= new PdfPCell(new Phrase("Nome", boldFont));
					PdfPCell surnameCol= new PdfPCell(new Phrase("Cognome", boldFont));
					PdfPCell bornDateCol= new PdfPCell(new Phrase("Data di nascita", boldFont));
					PdfPCell voteCol= new PdfPCell(new Phrase("Voto", boldFont));
					table.addCell(nameCol);
					table.addCell(surnameCol);
					table.addCell(bornDateCol);
					table.addCell(voteCol);
					
					table.setHeaderRows(1);
					// mi prendo la lista degli studenti della table view
					List<StudentsTableModel> students= tableView.getItems();
					
					// con un for mi scorro questa lista e popolo la tabella del pdf
					for(StudentsTableModel student : students)
					{
						
						 nameCol= new PdfPCell(new Phrase(student.getNome(), rowsFont));
						 surnameCol= new PdfPCell(new Phrase(student.getCognome(), rowsFont));
						 bornDateCol= new PdfPCell(new Phrase(student.getDataNascita(), rowsFont));
						 voteCol= new PdfPCell(new Phrase(student.getVoto(), rowsFont));
						 table.addCell(nameCol);
						 table.addCell(surnameCol);
						 table.addCell(bornDateCol);
						 table.addCell(voteCol);
						
					}
					
					//aggiungo la tabella al documento
					document.add(table);
					//aggiungo il logo del gestore
					img= com.itextpdf.text.Image.getInstance("src/main/resources/images/logoInPDF.jpg");
					img.scaleToFit(200, 300);
					document.add(img);
					
					//chiudo il documento e il file stream
					document.close();
					streamFile.close();
					SceneHandler.getInstance().showInformation(CommonUtil.PDFCREATED);
		   } 
		   catch (FileNotFoundException e) 
		   {
			// TODO Auto-generated catch block
			e.printStackTrace();
		   } 
		   catch (Exception e) 
		   {
			// TODO Auto-generated catch block
			e.printStackTrace();
		   }
		   	
			
	   }
	

   
 
	@FXML
    void initialize()
    {
    	refreshStudentsList();
    	logoView.imageProperty().set(new Image(getClass().getResourceAsStream("/images/genericLogo.png"))); 
    	nameColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
    	surnameColumn.setCellValueFactory(new PropertyValueFactory<>("cognome"));
    	bornDateColumn.setCellValueFactory(new PropertyValueFactory<>("dataNascita"));
    	voteColumn.setCellValueFactory(new PropertyValueFactory<>("voto"));
    	notePane.setVisible(false);
    	notePane.setEffect(new DropShadow());
    	classLabel.setText(ProfessorClient.getInstance().getClasse());
    	setPdfImage();
    
    }
    
	

}
