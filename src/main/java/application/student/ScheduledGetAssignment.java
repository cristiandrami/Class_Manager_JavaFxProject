package application.student;

import java.util.ArrayList;
import application.net.client.StudentClient;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

public class ScheduledGetAssignment extends ScheduledService<ArrayList<AssignmentModel>> {
    @Override
    protected Task<ArrayList<AssignmentModel>> createTask() {
        return new Task<ArrayList<AssignmentModel>>() {
            @Override
            protected ArrayList<AssignmentModel> call() 
            {
            	ArrayList<AssignmentModel> assignments=StudentClient.getInstance().getAssignments();      	
            	return assignments;
            	
            	
            }
        };
    }
}