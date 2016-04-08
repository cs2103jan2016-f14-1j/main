//@@author A0076520L

package logic;

import java.util.ArrayList;

import shared.Task;
import storage.Storage;
import shared.*;

public class Functionality extends Logger {

	private ArrayList<Task> tasks = new ArrayList<Task>();
	private Notification notification = new Notification();

	/**
	 * Synchronize the file with the active list
	 */
	protected void synchronization() {
		Storage.writeTasksToFile();
	}

	/**
	 * Add the action and set of tasks to History
	 * @param action
	 *            the action to be added
	 */
	protected void addToHistory(String action) {
		Storage.addToHistory(tasks, action);
	}

	/**
	 * Obtain a list of tasks this Function is currently using
	 * @return the list of tasks
	 */
	protected ArrayList<Task> getTasks() {
		return tasks;
	}

	/**
	 * Create a new task object and add to this list
	 * @param task
	 * 			the task to be added
	 */
	protected void addToFuncTasks(Task task) {
		Task newt = new Task();
		newt.setId(task.getId());
		newt.setCategories(task.getCategories());
		newt.setDate(task.getDate());
		newt.setDateTimes(task.getDateTimes());
		newt.setTask(task.getTask());
		newt.setIsCompleted(task.getIsCompleted());
		newt.setIntDate(task.getIntDate());
		tasks.add(newt);
	}

	/**
	 * Set the title for Notification
	 * @param title
	 * 			the String to be input
	 */
	protected void setNTitle(String title) {
		notification.setTitle(title);
	}

	/**
	 * Set the message for Notification
	 * @param msg
	 * 			the String to be input
	 */
	protected void setNMessage(String msg) {
		notification.setMessage(msg);
	}

	/**
	 * Obtain the Notification of this Function
	 * @return the Notification object
	 */
	protected Notification getNotification() {
		return notification;
	}
}
