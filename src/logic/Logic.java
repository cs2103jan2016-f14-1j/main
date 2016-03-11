package logic;

import java.util.ArrayList;

import shared.*;
import storage.Storage;

public class Logic {
	
	public Logic(){
	}
	
	public static boolean addTask(Task task) {
		if (new AddTask().addTask(task)) {
			Notification.setTitle(Keywords.MESSAGE_ADD_SUCCESS);
			Notification.setMessage(task.getTask() + " has been added!");
			return true;
		} else {
			return false;
		}
	}

	public static boolean deleteTask(ArrayList<Integer> taskIDs, ArrayList<String> cats) {
		new DeleteTask().deleteTask(taskIDs, cats);
		return true;
	}
	
	public static boolean doTask(ArrayList<Integer> taskIDs) {
		return new DoTask().doTask(taskIDs);
	}
	
	public static boolean editTask(int taskId, ArrayList<String> properties) {
		return new EditTask().editTask(taskId, properties);
	}
	
	public static ArrayList<Task> viewTask(String input) {
		if (input.isEmpty()) {
			Notification.setTitle(String.format(Keywords.MESSAGE_VIEW_SUCCESS, "Default"));
		} else {
			Notification.setTitle(String.format(Keywords.MESSAGE_VIEW_SUCCESS, input));
		}
		return new ViewTask().viewTasks(input);
	}

}
