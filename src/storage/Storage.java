package storage;

import java.util.ArrayList;
import shared.Keywords;
import shared.Task;

public class Storage {

	// FORMAT OF EACH TASK: [taskID]|[task]|[date]|[categories]|[isComplete]|
	private static ArrayList<Task> tasks;
	private static Categories categories;
	private static FreeIDs freeIDs;

	public Storage() {
		tasks = new ArrayList<Task>();
		categories = new Categories();
		freeIDs = new FreeIDs();
		ReadWrite.readTasksFromFile(tasks);
	}

	public static ArrayList<Task> getListOfTasks() {
		return tasks;
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
	
	public static int getNextAvailableID(){
		return freeIDs.getNextAvailableID();
	}

	public static void addTaskToList(Task task) {
		tasks.add(task);
		categories.addACountToCat(task.getCategories());
	}

	public static void removeTaskFromList(int taskIndex) {
		Task t = tasks.remove(taskIndex);
		categories.removeACountFromCat(t.getCategories());
	}
	
	public static void recycleId(int id) {
		freeIDs.addToFreeId(id);
	}

	public static void writeTasksToFile() {
		ReadWrite.writeTasksToFile(tasks, freeIDs);
	}

	public static void readTasksFromFile() {
		String stringOfIDs = ReadWrite.readTasksFromFile(tasks);
		freeIDs.convertIDStringToList(stringOfIDs);
	}

	public static ArrayList<String> getListOfCategoriesWithCount() {
		return categories.getListOfCategoriesWithCount(tasks);
	}

	public static ArrayList<Task> getTasksByCat(ArrayList<String> categoriesList) {
		return categories.getTasksByCat(categoriesList, tasks);
	}


}
