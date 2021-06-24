package application.controller.student;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import application.CommonUtil;
import application.SceneHandler;
import application.net.client.ProfessorClient;
import application.net.client.StudentClient;
import application.professor.StudentsTableModel;
//import application.student.ScheduledGetSufficientVotes;
//import application.student.ScheduledGetUnsufficientVotes;
import application.student.ScheduledGetVotes;
import application.student.StudentModel;
//import application.student.ScheduledGetWaitingVotes;
import application.student.StudentUtil;
import application.student.VotesTableModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.Duration;

public class PerformanceStudentPageController 
{
	private ScheduledGetVotes refreshVotes= new ScheduledGetVotes();
	private ScheduledGetVotes refreshGraphic= new ScheduledGetVotes();
	private boolean firstRefreshGraphic=true;
	
	
	
	@FXML
    private ImageView logoView;
    @FXML
    private Label votesAverange;
    @FXML
    private BorderPane averangeBorderPane;

    @FXML
    private VBox vBoxContainer;
	
    @FXML
    private Label unsufficientLabel;

    @FXML
    private Label waitingVotesLabel;

    @FXML
    private Label sufficientLabel;
    @FXML
    private TableView<VotesTableModel> tableView;

    @FXML
    private TableColumn<VotesTableModel, String> nameColumn;

    @FXML
    private TableColumn<VotesTableModel, Integer> voteColumn;
    
    private ObservableList<VotesTableModel> votes=FXCollections.observableArrayList();

    @FXML
    private Button backButton;

    @FXML
    private Button exportToPdf;



   
    @FXML
    void backClicked(ActionEvent event) 
    {
    	try 
    	{
			SceneHandler.getInstance().setStudentHomePage();
		} 
    	catch (IOException e) 
    	{
			System.out.println(StudentUtil.BACK_TO_HOME_PROBLEM);
		}

    	
    }
    @FXML
    void exportPdfClicked(ActionEvent event) 
    {
    	savePDF();
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
				
					StudentModel student= StudentClient.getInstance().getStudent();
					//creo il paragrafo che mi dice quale classe gestendo
					Paragraph paragraph= new Paragraph("Studente  "+ student.getName()+ " "+ student.getSurname()+ "\nNato il " + student.getBornDate()+"\n\n", classFont);
					paragraph.setAlignment(Element.ALIGN_CENTER);
					document.add(paragraph);
					paragraph= new Paragraph("Classe: "+ student.getsClass()+"\n\n", classFont);
					paragraph.setAlignment(Element.ALIGN_CENTER);
					document.add(paragraph);
					
					// creo un array di float con delle grandezze che mi servono per la lunghezza delle celle
					float[] tableSizes={200f, 200f};
					// creo le 4 celle che mi servono e le aggiungo alla tabella
					PdfPTable table= new PdfPTable(tableSizes);
					PdfPCell nameCol= new PdfPCell(new Phrase("Materia", boldFont));
					PdfPCell voteCol= new PdfPCell(new Phrase("Voto", boldFont));
					table.addCell(nameCol);
					table.addCell(voteCol);
					
					
					table.setHeaderRows(1);
					// mi prendo la lista degli studenti della table view
					List<VotesTableModel> votes= tableView.getItems();
					
					// con un for mi scorro questa lista e popolo la tabella del pdf
					for(VotesTableModel vote : votes)
					{
						
						 nameCol= new PdfPCell(new Phrase(vote.getName(), rowsFont));
						
						 voteCol= new PdfPCell(new Phrase(vote.getVote(), rowsFont));
						 table.addCell(nameCol);
		
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
					
					SceneHandler.getInstance().showInformation(CommonUtil.PDF_CREATED);
		   }
		   catch (FileNotFoundException e) 
		   {
			
		   } 
		   catch (Exception e) 
		   {
		
		   }
					
					
		
	}
	@FXML
    void initialize()
    {

    	startTableRefresh();	
    	startGrapichRefresh();
    	logoView.imageProperty().set(new Image(getClass().getResourceAsStream(StudentUtil.IMAGE_PATH))); 
    	nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    	voteColumn.setCellValueFactory(new PropertyValueFactory<>("vote"));
    	setPdfButtonImage();
    	
    }
    
    private void setPdfButtonImage() 
    {
    	 ImageView view = new ImageView(new Image(getClass().getResourceAsStream(StudentUtil.PDF_IMAGE_PATH)));
	     view.setFitHeight(40);
	     view.setFitWidth(40);
	     exportToPdf.setGraphic(view);
	     exportToPdf.getStyleClass().add("pdfButton");
		
	}
	private void startGrapichRefresh()
    {
    	refreshGraphic.setPeriod(Duration.seconds(15));
    	   
    	refreshGraphic.setDelay(Duration.seconds(0.1));

    	refreshGraphic.setOnSucceeded(new EventHandler<WorkerStateEvent>() 
    	{
    		

			@Override
			public void handle(WorkerStateEvent event) 
			{
				CategoryAxis xAxis= new CategoryAxis();
				NumberAxis yAxis= new NumberAxis();
				BarChart<String, Number> votesGraphic= new BarChart<>(xAxis, yAxis);
				XYChart.Series<String,Number> graphicData=new XYChart.Series<String,Number>();
				graphicData.setName("VOTI");
				votesGraphic.setTitle("Andamento dei voti");
				xAxis.setLabel(StudentUtil.OBJECT_PERFORMANCE);
				yAxis.setLabel(StudentUtil.VOTE_PERFORMANCE);
				ObservableList<VotesTableModel> votes= (ObservableList<VotesTableModel>) event.getSource().getValue();
				Float averange=(float) 0.0;
				int size=0;
				//ora mi riempio le barre del grafico 
				if(!(votes==null))
				{	
					for(VotesTableModel v: votes)
					{
							if(v.getVote().equals(StudentUtil.VOTE_ABSENT))
							{
								graphicData.getData().add(new XYChart.Data<String, Number>(v.getName(), 0));
							}
							else 
							{
								try 
								 {
								    Integer voto = Integer.parseInt(v.getVote());
								    graphicData.getData().add(new XYChart.Data<String, Number>(v.getName(), voto));
								    averange+=voto;
								    size++;
									  
								 }
								 catch (NumberFormatException e) 
								 {
									    
									  
								 }
							}
		
					}
					if(size!=0)
					{
						averange/=size;
						votesAverange.setText(averange.toString());
						if(averange>=6)
						{
							averangeBorderPane.setStyle(StudentUtil.SUFFICIENT_AVERAGE_PANE_STYLE);
						}
						else
						{
							averangeBorderPane.setStyle(StudentUtil.INSUFFICIENT_AVERAGE_PANESTYLE);
					
						}
							
					}
					else
					{
						votesAverange.setText("0.0");
						averangeBorderPane.setStyle(StudentUtil.NULL_AVERAGE_PANE_STYLE);
					}
					
					votesGraphic.getData().add(graphicData);
					if(firstRefreshGraphic)
					{
						vBoxContainer.getChildren().add(2, votesGraphic);
						firstRefreshGraphic=false;
						
					}
					else
					{
						vBoxContainer.getChildren().remove(2);
						vBoxContainer.getChildren().add(2, votesGraphic);
					}
					
				}
					
				
				
				
			}
    		
		});

	   
    	refreshGraphic.start();
    	
    }
    
    
    private void startTableRefresh()
    {
    	refreshVotes.setPeriod(Duration.seconds(15));
  	   
    	refreshVotes.setDelay(Duration.seconds(0.2));

    	refreshVotes.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) 
			{
				Integer unsufficient=0;
				Integer sufficient=0;
				Integer waiting=0;
				votes= (ObservableList<VotesTableModel>) event.getSource().getValue();
				tableView.setItems(votes);
				if(!(votes==null))
				{
					for(VotesTableModel v: votes)
					{
						if(v.getVote().equals(StudentUtil.VOTE_ABSENT))
						{
							waiting++;
						}
						else 
						{
							try 
							 {
							    Integer vote = Integer.parseInt(v.getVote());
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
				}
				
				unsufficientLabel.setText(unsufficient.toString());
				waitingVotesLabel.setText(waiting.toString());
				sufficientLabel.setText(sufficient.toString());
				
			}
    		
		});

	   
    	refreshVotes.start();
    }
    
   
   
    
   
    


}
