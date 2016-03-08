package storage;

import java.util.ArrayList;
import java.util.LinkedList;

import shared.Task;

public class Storage {

	private static ArrayList<Task> tasks; //still in development: transitioning to OOP
	private static LinkedList<Integer> freeIDs;
	protected static int currentTaskId = 0;
	private ReadWrite rw;
	
	public Storage(){
		tasks = new ArrayList<Task>();
		freeIDs = new LinkedList<Integer>();
		rw = new ReadWrite();
		rw.readTasksFromFile();
	}
	
	public static ArrayList<Task> getTasks(){
		return tasks;
	}
	
	public static void addTaskToList(Task task){
		tasks.add(task);
	}
	
	public void removeTasksFromList(int taskIndex){
		tasks.remove(taskIndex);
	}
	
	public Task getTask(int index){
		return tasks.get(index);
	}
	
	public void writeTasksToFile(){
		rw.writeTasksToFile();
	}
	
	private boolean isListEmpty(){
		return tasks.isEmpty();
	}
	
	protected static LinkedList<Integer> getFreeIDs(){
		return freeIDs;
	}
	
}
