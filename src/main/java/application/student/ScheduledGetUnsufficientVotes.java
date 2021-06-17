/*
package application.student;
import application.net.client.StudentClient;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

public class ScheduledGetUnsufficientVotes extends ScheduledService<String> 
{
    @Override
    protected Task<String> createTask() {
        return new Task<String>() {
            @Override
            protected String call() 
            {
            	String unsufficient=StudentClient.getInstance().getUnsufficientVotes();   
            	return unsufficient;
            }
        };
    }
}

*/
