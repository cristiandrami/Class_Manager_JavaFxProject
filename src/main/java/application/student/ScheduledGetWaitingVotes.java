/*package application.student;

import application.net.client.StudentClient;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

public class ScheduledGetWaitingVotes extends ScheduledService<String> 
{
	 @Override
	    protected Task<String> createTask() {
	        return new Task<String>() {
	            @Override
	            protected String call() 
	            {
	            	String waitingVotes=StudentClient.getInstance().getWaitingVotes();
	            
	            	return waitingVotes;
	            }
	        };
	    }
}
*/
