package application.student;



import java.util.ArrayList;

import application.net.client.ProfessorClient;
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
            	ObservableList<VotesTableModel> students=StudentClient.getInstance().getVotes();
            	System.out.println(students.get(0).getName());
            	//System.out.println(students);
            	return students;
            }
        };
    }
}