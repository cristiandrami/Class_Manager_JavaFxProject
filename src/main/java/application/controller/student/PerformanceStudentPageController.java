package application.controller.student;

import java.io.IOException;
import application.SceneHandler;
import application.professor.StudentsTableModel;
import application.student.ScheduledGetSufficientVotes;
import application.student.ScheduledGetUnsufficientVotes;
import application.student.ScheduledGetVotes;
import application.student.ScheduledGetWaitingVotes;
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
import javafx.util.Duration;

public class PerformanceStudentPageController 
{
	private ScheduledGetVotes refreshVotes= new ScheduledGetVotes();
	private ScheduledGetVotes refreshGraphic= new ScheduledGetVotes();
	private ScheduledGetUnsufficientVotes refreshUnsufficient= new ScheduledGetUnsufficientVotes();
	private ScheduledGetSufficientVotes refreshSufficient= new ScheduledGetSufficientVotes();
	private ScheduledGetWaitingVotes refreshWaiting= new ScheduledGetWaitingVotes();

	@FXML
    private ImageView logoView;
	
	
	private CategoryAxis xAxis= new CategoryAxis();
	private NumberAxis yAxis= new NumberAxis();

	@FXML
	private BarChart<String, Number> votesGraphic= new BarChart<>(xAxis, yAxis);
	
	private XYChart.Series<String,Number> graphicData=new XYChart.Series<String,Number>();
	
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
    void backClicked(ActionEvent event) 
    {
    	try 
    	{
			SceneHandler.getInstance().setStudentHomePage();
		} 
    	catch (IOException e) 
    	{
			System.out.println("Problema con il caricamento della home page studente");
		}

    }
    
    @FXML
    void initialize()
    {
    	votesGraphic.setTitle("Andamento dei voti");
    	xAxis.setLabel("Materia");
		yAxis.setLabel("Voto");
    	votesGraphic.getData().add(graphicData);
    	
    	startTableRefresh();
    	startUnsufficientVotesRefresh();
    	startSufficientVotesRefresh();
    	startWaitingVotesRefresh();
    	startGrapichRefresh();
    	
    	
    	
    	logoView.imageProperty().set(new Image(getClass().getResourceAsStream("/loginResources/logoLogin.jpg"))); 
    	nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    	voteColumn.setCellValueFactory(new PropertyValueFactory<>("vote"));
    	//tableView.setItems(tableList);
    }
    
    private void startTableRefresh()
    {
    	refreshVotes.setPeriod(Duration.seconds(20));
  	   
    	refreshVotes.setDelay(Duration.seconds(0.1));

    	refreshVotes.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) 
			{
				votes= (ObservableList<VotesTableModel>) event.getSource().getValue();
				tableView.setItems(votes);
				
			}
    		
		});

	   
    	refreshVotes.start();
    }
    
    private void startUnsufficientVotesRefresh()
    {
    	refreshUnsufficient.setPeriod(Duration.seconds(20));
   	   
    	refreshUnsufficient.setDelay(Duration.seconds(0.2));

    	refreshUnsufficient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) 
			{
				String unsufficient= (String) event.getSource().getValue();
				unsufficientLabel.setText(unsufficient);
				
			}
    		
		});

	   
    	refreshUnsufficient.start();
    }
    private void startSufficientVotesRefresh()
    {
    	refreshSufficient.setPeriod(Duration.seconds(20));
   	   
    	refreshSufficient.setDelay(Duration.seconds(0.3));

    	refreshSufficient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) 
			{
				String sufficient= (String) event.getSource().getValue();
				sufficientLabel.setText(sufficient);
			}
    		
		});

	   
    	refreshSufficient.start();
    }
    
    private void startWaitingVotesRefresh()
    {
    	refreshWaiting.setPeriod(Duration.seconds(20));
   	   
    	refreshWaiting.setDelay(Duration.seconds(0.4));

    	refreshWaiting.setOnSucceeded(new EventHandler<WorkerStateEvent>() 
    	{

			@Override
			public void handle(WorkerStateEvent event) 
			{
				String waiting= (String) event.getSource().getValue();
				waitingVotesLabel.setText(waiting);
				
			}
    		
		});

	   
    	refreshWaiting.start();
    }
    
    private void startGrapichRefresh()
    {
    	refreshGraphic.setPeriod(Duration.seconds(20));
    	   
    	refreshGraphic.setDelay(Duration.seconds(0.5));

    	refreshGraphic.setOnSucceeded(new EventHandler<WorkerStateEvent>() 
    	{
    		

			@Override
			public void handle(WorkerStateEvent event) 
			{
				ObservableList<VotesTableModel> votes= (ObservableList<VotesTableModel>) event.getSource().getValue();
				graphicData.getData().clear();
				//ora mi riempio le barre del grafico 
				for(VotesTableModel v: votes)
				{
					if(v.getVote().equals("Non ancora scrutinato"))
					{
						graphicData.getData().add(new XYChart.Data<String, Number>(v.getName(), 0));
					}
					else 
					{
						try 
						 {
						    Integer voto = Integer.parseInt(v.getVote());
						    graphicData.getData().add(new XYChart.Data<String, Number>(v.getName(), voto));
							  
						 }
						 catch (NumberFormatException e) 
						 {
							    
							  
						 }
					}
					
					
					 
						
						
				}
				
				
				
			}
    		
		});

	   
    	refreshGraphic.start();
    	
    	
    	
		
		

		
    	
    }
    
    


}
