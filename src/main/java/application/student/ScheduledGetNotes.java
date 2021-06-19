package application.student;

import java.util.ArrayList;

import application.net.client.StudentClient;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

public class ScheduledGetNotes extends ScheduledService<ArrayList<NotesModel>> {
    @Override
    protected Task<ArrayList<NotesModel>> createTask() {
        return new Task<ArrayList<NotesModel>>() {
            @Override
            protected ArrayList<NotesModel> call() 
            {
            	ArrayList<NotesModel> notes=StudentClient.getInstance().getNotes();      	
            	return notes;
            	
            	
            }
        };
    }
}