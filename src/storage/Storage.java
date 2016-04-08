//@@author A0076520L

package storage;

import java.util.ArrayList;
import shared.Keywords;
import shared.Task;

public class Storage {

	// FORMAT OF EACH TASK: [taskID]|[task]|[sdate]|[edate]|
	// [stime]|[etime]|[categories]|[isComplete]|[priority]
	private static ArrayList<Task> tasks;
	private static Storage storage;

	/**
	 * Initialize the variables
	 */
	private Storage() {
		tasks = new ArrayList<Task>();
		Categories.init();
		FreeIDs.init();
		History.initHistory();
		ReadWrite.readTasksFromFile(tasks);
	}

	/**
	 * Obtain the instance of the Storage
	 * 
	 * @return the instance of the storage
	 */
	public static Storage getInstance() {
		if (storage == null) {
			storage = new Storage();
		}
		return storage;
	}

	/**
	 * Obtain a list of tasks
	 * 
	 * @return a list of tasks
	 */
	public static ArrayList<Task> getListOfTasks() {
		return tasks;
	}

	/**
	 * Obtain a list of uncompleted tasks by calling getTasksBasedOnStatus(int);
	 * 
	 * @return a list of uncompleted tasks
	 */
	public static ArrayList<Task> getListOfUncompletedTasks() {
		return getTasksBasedOnStatus(Keywords.TASK_NOT_COMPLETED);
	}

	/**
	 * Obtain a list of completed tasks by calling getTasksBasedOnStatus(int);
	 * 
	 * @return a list of completed tasks
	 */
	public static ArrayList<Task> getListOfCompletedTasks() {
		return getTasksBasedOnStatus(Keywords.TASK_COMPLETED);
	}

	/**
	 * Retrieves the tasks based on the status
	 * 
	 * @param status
	 *            either completed or not completed
	 * @return the results obtained
	 */
	private static ArrayList<Task> getTasksBasedOnStatus(int status) {
		ArrayList<Task> temp = new ArrayList<>();
		for (Task t : tasks) {
			if (t.getIsCompleted() == status) {
				temp.add(t);
			}
		}
		return temp;
	}

	/**
	 * Get the task based on its ID
	 * 
	 * @param taskID
	 *            the ID of the task to get
	 * @return the task or null if cannot be found
	 */
	public static Task getTask(int taskID) {
		for (Task t : tasks) {
			if (t.getId() == taskID) {
				return t;
			}
		}
		return null;
	}

	/**
	 * Obtain the next availableID using FreeIDs class
	 * 
	 * @return the next ID to be given to the task
	 */
	public static int getNextAvailableID() {
		return FreeIDs.getNextAvailableID();
	}

	/**
	 * Add the task into the list
	 * 
	 * @param task
	 *            the task to be added
	 */
	public static void addTaskToList(Task task) {
		tasks.add(task);
	}

	/**
	 * Remove the task using the Task ID
	 * 
	 * @param taskId
	 *            the task to be removed
	 */
	public static void removeTaskUsingTaskId(int taskId) {
		Task task = getTask(taskId);
		if (task != null) {
			Categories.removeACountFromCat(getTask(taskId).getCategories());
			tasks.remove(getTask(taskId));
		}
	}

	/**
	 * Remove specific ID from the FreeIDs class
	 * 
	 * @param id
	 *            the ID to be removed
	 */
	public static void removeSpecificId(int id) {
		FreeIDs.removeSpecificId(id);
	}

	/**
	 * Add the ID to the FreeIDs class
	 * 
	 * @param id
	 *            the ID to be added
	 */
	public static void recycleId(int id) {
		FreeIDs.addToFreeId(id);
	}

	/**
	 * Write the tasks to the file
	 */
	public static void writeTasksToFile() {
		ReadWrite.writeTasksToFile(tasks);
	}

	/**
	 * Read the file to obtain the tasks
	 */
	public static void readTasksFromFile() {
		tasks.clear();// clear any existing data from the list
		ReadWrite.readTasksFromFile(tasks);
	}

	/**
	 * Obtain a list of categories by call the Categories class
	 * 
	 * @return a list of categories with its count
	 */
	public static ArrayList<String> getListOfCategoriesWithCount() {
		return Categories.getListOfCategoriesWithCount(tasks);
	}

	/**
	 * Get the list of tasks by categories
	 * 
	 * @param categoriesList
	 *            the list of categories to obtain
	 * @return the list of tasks based on categories or empty list
	 */
	public static ArrayList<Task> getTasksByCat(ArrayList<String> categoriesList) {
		ArrayList<Task> temp = new ArrayList<Task>();
		for (Task t : Categories.getTasksByCat(categoriesList, tasks)) {
			if (t.getIsCompleted() == Keywords.TASK_NOT_COMPLETED) {
				temp.add(t);
			}
		}
		return temp;
	}

	/**
	 * add the set of tasks and action to History class
	 * 
	 * @param tasks
	 *            the set of tasks to be added
	 * @param action
	 *            the action to be added
	 */
	public static void addToHistory(ArrayList<Task> tasks, String action) {
		History.addActionToHistory(tasks, action);
	}

	/**
	 * Poll the last set of tasks from the History class
	 * 
	 * @return the last set of tasks
	 */
	public static ArrayList<Task> getLastTasks() {
		return History.getLastTasks();
	}

	/**
	 * Peek the last set of tasks in the History class
	 * 
	 * @return the last set of tasks peeked
	 */
	public static ArrayList<Task> peekLastTask() {
		return History.peekLastTask();
	}

	/**
	 * Poll the last action performed through History class
	 * 
	 * @return the last action performed
	 */
	public static String getLastAction() {
		return History.getLastAction();
	}

	/**
	 * Check if the tasks contain the category
	 * 
	 * @param cat
	 *            the category to be matched
	 * @return the boolean result of the matching
	 */
	public static boolean containsCat(String cat) {
		boolean bool = false;
		for (Task t : tasks) {
			if (t.getCategories().contains(cat)) {
				bool = true;
			}
		}
		return bool;
	}

}
