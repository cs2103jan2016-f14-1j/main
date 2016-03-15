package storage;

import java.util.ArrayList;
import shared.Keywords;
import shared.Task;

public class Storage {

	// FORMAT OF EACH TASK: [taskID]|[task]|[sdate]|[edate]|
	//						[stime]|[etime]|[categories]|[isComplete]|[priority]
	private static ArrayList<Task> tasks;
	//private static Categories categories;
	//private static FreeIDs freeIDs;

	public Storage() {
		tasks = new ArrayList<Task>();
		//categories = new Categories();
		//freeIDs = new FreeIDs();
		Categories.init();
		FreeIDs.init();
		History.initHistory();
		ReadWrite.readTasksFromFile(tasks);
	}

	public static ArrayList<Task> getListOfTasks() {
		return tasks;
	}

	public static ArrayList<Task> getListOfUncompletedTasks() {
		ArrayList<Task> temp = new ArrayList<>();
		for (Task t : tasks) {
			if (t.getIsCompleted() == Keywords.TASK_NOT_COMPLETED) {
				temp.add(t);
			}
		}
		return temp;
	}

	public static ArrayList<Task> getListOfCompletedTasks() {
		ArrayList<Task> temp = new ArrayList<>();
		for (Task t : tasks) {
			if (t.getIsCompleted() == Keywords.TASK_COMPLETED) {
				temp.add(t);
			}
		}
		return temp;
	}

	public static Task getTask(int taskID) {
		for (Task t : tasks) {
			if (t.getId() == taskID) {
				return t;
			}
		}
		return null;
	}

	public static int getTaskIndex(int taskID) {
		for (int i = 0; i < tasks.size(); i++) {
			if (tasks.get(i).getId() == taskID) {
				return i;
			}
		}
		return Keywords.TASK_NOT_FOUND;
	}

	public static int getNextAvailableID() {
		return FreeIDs.getNextAvailableID();
	}

	public static void addTaskToList(Task task) {
		tasks.add(task);
		//Categories.addACountToCat(task.getCategories());
	}

	public static void removeTaskFromList(int taskIndex) {
		Task t = tasks.remove(taskIndex);
		Categories.removeACountFromCat(t.getCategories());
	}

	public static void recycleId(int id) {
		FreeIDs.addToFreeId(id);
	}

	public static void writeTasksToFile() {
		ReadWrite.writeTasksToFile(tasks);
	}

	public static void readTasksFromFile() {
		ReadWrite.readTasksFromFile(tasks);
	}

	public static ArrayList<String> getListOfCategoriesWithCount() {
		return Categories.getListOfCategoriesWithCount(tasks);
	}

	public static ArrayList<Task> getTasksByCat(ArrayList<String> categoriesList) {
		ArrayList<Task> temp = new ArrayList<Task>();
		for (Task t : Categories.getTasksByCat(categoriesList, tasks)) {
			if (t.getIsCompleted() == Keywords.TASK_NOT_COMPLETED) {
				temp.add(t);
			}
		}
		return temp;
	}
	
	public static void addToHistory(String action){
		History.addActionToHistory(action);
	}
	
	public static String getLastAction(){
		return History.getLastAction();
	}

}
