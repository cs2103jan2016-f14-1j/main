package logic;

import java.util.ArrayList;

import shared.*;
import storage.Storage;

public class Logic {
	
	public Logic(){
	}
	
	public static boolean addTask(Task task, int isItUndoFunc) {
		return new AddTask().addTask(task, isItUndoFunc);
	}

	public static boolean deleteTask(ArrayList<Integer> taskIDs, ArrayList<String> cats) {
		return new DeleteTask().deleteTask(taskIDs, cats);
	}
	
	public static boolean doTask(ArrayList<Integer> taskIDs, int completeOrNot) {
		return new DoTask().doTask(taskIDs, completeOrNot);
	}
	
	public static boolean editTask(int taskId, ArrayList<String> properties) {
		return new EditTask().editTask(taskId, properties);
	}
	
	public static ArrayList<Task> viewTask(String input) {
		return new ViewTask().viewTasks(input);
	}
	
	public static boolean undoTask(){
		return new UndoTask().undoTask();
	}

}
