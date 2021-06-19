package application.professor;

import java.util.ArrayList;

import application.net.client.ProfessorClient;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

public class ScheduledGetStudent extends ScheduledService<ObservableList<StudentsTableModel>> {
    @Override
    protected Task<ObservableList<StudentsTableModel>> createTask() {
        return new Task<ObservableList<StudentsTableModel>>() {
            @Override
            protected ObservableList<StudentsTableModel> call() 
            {
            	ObservableList<StudentsTableModel> students=ProfessorClient.getInstance().getStudentsList();
            	return students;
            }
        };
    }
}