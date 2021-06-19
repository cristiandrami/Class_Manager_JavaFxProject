package application.student;

import application.net.client.StudentClient;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

public class ScheduledGetVotes extends ScheduledService<ObservableList<VotesTableModel>> {
    @Override
    protected Task<ObservableList<VotesTableModel>> createTask() {
        return new Task<ObservableList<VotesTableModel>>() {
            @Override
            protected ObservableList<VotesTableModel> call() 
            {
            	ObservableList<VotesTableModel> votes=StudentClient.getInstance().getVotes();
            	
            	return votes;
            	
            	
            }
        };
    }
}