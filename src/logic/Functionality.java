package logic;

import java.util.ArrayList;

import shared.Task;
import storage.Storage;

public class Functionality {

	private ArrayList<Task> tasks = new ArrayList<Task>();
	
	protected void synchronization(){
		Storage.writeTasksToFile();
	}
	
	protected void addToHistory(String action){
		Storage.addToHistory(tasks, action);
	}
	
	protected ArrayList<Task> getTasks(){
		return tasks;
	}
	
	protected void addToFuncTasks(Task t){
		Task newt = new Task();
		newt.setId(t.getId());
		newt.setCategories(t.getCategories());
		newt.setDate(t.getDate());
		newt.setDateTimes(t.getDatetimes());
		newt.setTask(t.getTask());
		newt.setIsCompleted(t.getIsCompleted());
		newt.setIntDate(t.getIntDate());
		tasks.add(newt);
	}
	
}
