package storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import shared.Keywords;
import shared.Task;

public class Storage {

	// FORMAT OF EACH TASK: [taskID]|[task]|[date]|[categories]|[isComplete]|
	private static ArrayList<Task> tasks;
	private static HashMap<String, Integer> noOfTasksPerCat;
	private static LinkedList<Integer> freeIDs;
	protected static int currentTaskId = 0;
	private ReadWrite rw;

	public Storage() {
		tasks = new ArrayList<Task>();
		noOfTasksPerCat = new HashMap<>();
		freeIDs = new LinkedList<Integer>();
		rw = new ReadWrite();
	}

	public static ArrayList<Task> getTasks() {
		return tasks;
	}

	public static HashMap<String, Integer> getNoOfTasksPerCat() {
		return noOfTasksPerCat;
	}

	public static void addTaskToList(Task task) {
		tasks.add(task);
	}

	public void removeTasksFromList(int taskIndex) {
		tasks.remove(taskIndex);
	}

	public static Task getTask(int taskID) {
		for (Task t : tasks) {
			if (t.getId() == taskID) {
				return t;
			}
		}
		return null;
	}

	public void writeTasksToFile() {
		rw.writeTasksToFile();
	}

	public void readTasksFromFile() {
		rw.readTasksFromFile();
	}

	protected static LinkedList<Integer> getFreeIDs() {
		return freeIDs;
	}

	public ArrayList<String> getListOfCategoriesWithCount() {

		for (Task t : tasks) {
			if (t.getIsCompleted() == Keywords.TASK_NOT_COMPLETED) {
				for (String cat : t.getCategories()) {
					if (!cat.equals(Keywords.EMPTY_STRING)) {
						int currentCount = (noOfTasksPerCat.get(cat) == null) ? 0 : noOfTasksPerCat.get(cat);
						currentCount++;
						noOfTasksPerCat.put(cat, currentCount);
					}
				}
			}
		}
		// format the list to be displayed
		Iterator it = noOfTasksPerCat.entrySet().iterator();
		ArrayList<String> temp = new ArrayList<>();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			temp.add(pair.getKey() + Keywords.SPACE_STRING + "(" + pair.getValue() + ")");
		}
		temp.add("@Uncompleted (" + Categories.getNoOfUncompletedTasks() + ")");
		noOfTasksPerCat.clear();
		return temp;
	}

}
