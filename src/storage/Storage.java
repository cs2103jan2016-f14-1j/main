package storage;

import java.util.ArrayList;
import shared.Task;

public class Storage {

	private static ArrayList<Task> tasks;//still in development: transitioning to OOP
	private ReadWrite rw;
	private FreeIDs fID;
	
	public Storage(){
		tasks = new ArrayList<Task>();
		rw = new ReadWrite();
		fID = new FreeIDs();
		rw.readTasksFromFile();
	}
	
	public static ArrayList<Task> getTasks(){
		return tasks;
	}
	
	public static void addTaskToList(Task task){
		
	}
	
	public void removeTasksFromList(int taskID, int taskIndex){
		
	}
	
	public Task getTask(int index){
		return tasks.get(index);
	}
	
	public boolean isListEmpty(){
		return tasks.isEmpty();
	}
	
	public void writeTasksToFile(){
		rw.writeTasksToFile();
		rw.writeIDsToFile();
	}
}
