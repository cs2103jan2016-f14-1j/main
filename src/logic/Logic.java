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
			// need set notification to error message
			return false;
		}
	}

	public static boolean deleteTask(ArrayList<Integer> taskIDs, ArrayList<String> cats) {
		if (new DeleteTask().deleteTask(taskIDs, cats)) {
			if (taskIDs.size() > 1) {
				Notification.setTitle(Keywords.MESSAGE_DELETE_SUCCESS);
				Notification.setMessage(taskIDs.toString());
				return true;
			} else if (cats.size() > 1) {
				Notification.setTitle(Keywords.MESSAGE_DELETE_SUCCESS);
				Notification.setMessage(cats.toString());
				return true;
			} else {
				Notification.setTitle(Keywords.MESSAGE_DELETE_SUCCESS);
				Notification.setMessage(taskIDs.toString() + "\n" + cats.toString());
				// need to format the message output later
			}
			return true;
		} else {
			// need set notification to error message
			return false;
		}
	}
	
	public static boolean doTask(ArrayList<Integer> taskIDs) {
		if (new DoTask().doTask(taskIDs)) {
			if (taskIDs.size() > 1) {
				Notification.setTitle(Keywords.MESSAGE_COMPLETED_SUCCESS);
				Notification.setMessage(taskIDs.toString());
				return true;
			} else {
				Notification.setTitle(Keywords.MESSAGE_COMPLETED_SUCCESS);
				Notification.setMessage(taskIDs.toString());
				// need to format the message output later
			}
			return true;
		} else {
			// need set notification to error message
			return false;
		}
	}
	
	public static boolean editTask(int taskId, ArrayList<String> properties) {
		if (new EditTask().editTask(taskId, properties)) {
			Notification.setTitle(Keywords.MESSAGE_EDIT_SUCCESS);
			Notification.setMessage(Integer.toString(taskId) + " to " + properties.toString());
			return true;
		} else {
			return false;
		}
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
