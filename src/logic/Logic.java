package logic;

import java.util.ArrayList;
import java.util.Date;

import parser.Parser;
import shared.*;
import storage.Storage;

public class Logic {
	
	private static Logic logic;

	// private constructor
	private Logic() {
	}
	
	public static Logic getInstance() {
		if (logic == null) {
			logic = new Logic();
		}
		return logic;
	}
	
	public static boolean addTask(Task task) {
		return new AddTask().addTask(task);
	}

	public static boolean deleteTask(ArrayList<Integer> taskIDs, ArrayList<String> cats) {
		return new DeleteTask().deleteTask(taskIDs, cats);
		
	}
	
	public static boolean doTask(ArrayList<Integer> taskIDs) {
		return new DoTask().doTask(taskIDs);
	}
	
	public static boolean editTask(int taskId, Date date) {
		return new EditTask().editTask(taskId, date);
	}
	
	public static ArrayList<Task> viewTask(String input) {
		return new ViewTask().viewTasks(input);
	}
	
	public static boolean undoTask(){
		return new UndoTask().undoTask();
	}

	public static boolean prioritise(ArrayList<Integer> taskIDs) {
		return new MarkTask().prioritise(taskIDs);
	}
	
	public static ArrayList<Task> searchTask(String words){
		return new SearchTask().searchTask(words);
	}

}
