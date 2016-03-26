package logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

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

	public static Notification addTask(Task task) {
		return new AddTask().addTask(task);
	}

	public static Notification deleteTask(ArrayList<Integer> taskIDs, ArrayList<String> cats) {
		return new DeleteTask().deleteTask(taskIDs, cats);	
	}

	public static Notification doTask(ArrayList<Integer> taskIDs) {
			return new DoTask().doTask(taskIDs);
	}

	public static Notification editTask(int taskId, ArrayList<Date> datetimes,String task, ArrayList<String> cats) {
		return new EditTask().editTask(taskId, datetimes, task, cats);
	}
	
	public static ArrayList<Object> viewTask(String input) {
		return new ViewTask().viewTasks(input);
	}

	public static Notification undoTask() {
		return new UndoTask().undoTask();
	}

	public static Notification prioritise(ArrayList<Integer> taskIDs) {
		return new MarkTask().prioritise(taskIDs);
	}

	public static Notification invalidCommand(){
		Notification n = new Notification();
		n.setInvalidMsg();
		return n;
	}

	public static ArrayList<Object> searchTask(String words, boolean isPriortise, String date, ArrayList<String> categories) {
		return new SearchTask().searchTask(words, isPriortise, date, categories);
	}
	
	public static ArrayList<Task> getUncompletedTasks(){
		return Storage.getListOfUncompletedTasks();
	}
	
	public static ArrayList<String> getListOfCatWithCount(){
		return Storage.getListOfCategoriesWithCount();
	}
	
	public static LinkedList<String> viewHelp(){
		return new ViewHelp().viewHelp();
	}
	
	public static void updateFile(){
		Storage.writeTasksToFile();
	}
}
