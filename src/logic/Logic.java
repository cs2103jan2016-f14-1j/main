package logic;

import java.util.ArrayList;

import shared.*;
import storage.Storage;

public class Logic {
	
	public Logic(){
	}
	
	public static boolean addTask(Task task) {
		new AddTask().addTask(task);
		return true;
	}

	public static boolean deleteTask(ArrayList<Integer> taskIDs, ArrayList<String> cats) {
		new DeleteTask().deleteTask(taskIDs, cats);
		return true;
	}
	
	public static boolean doTask(ArrayList<Integer> taskIDs) {
		return new DoTask().doTask(taskIDs);
	}
	
	public boolean editTask(Task task) {
		// EditTask.addTask(task);
		return true;
	}
	
	public static ArrayList<Task> viewTask(String input) {
		return new ViewTask().viewTasks(input);
	}

}
