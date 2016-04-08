//@@author A0076520L

package storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import shared.Keywords;
import shared.Task;

public class Categories {

	private static HashMap<String, Integer> noOfTasksPerCat;

	/**
	 * Initialize the variables
	 */
	protected static void init() {
		noOfTasksPerCat = new HashMap<>();
	}

	/**
	 * Add a count to the category name and store in the HashMap
	 * 
	 * @param category
	 *            the category to be incremented
	 */
	protected static void addACountToCat(ArrayList<String> category) {
		for (String cat : category) {
			if (noOfTasksPerCat.get(cat) == null) {
				noOfTasksPerCat.put(cat, 1);
			} else {
				noOfTasksPerCat.put(cat, noOfTasksPerCat.get(cat) + 1);
			}
		}
	}

	/**
	 * Remove a count from the category name and remove the category if it
	 * reaches 0
	 * 
	 * @param category
	 *            the category to be decremented
	 */
	protected static void removeACountFromCat(ArrayList<String> category) {
		for (String cat : category) {
			if (noOfTasksPerCat.get(cat) == null) {
				continue;
			}
			if (noOfTasksPerCat.get(cat) <= 0) {
				noOfTasksPerCat.remove(cat);
			} else {
				noOfTasksPerCat.put(cat, noOfTasksPerCat.get(cat) - 1);
			}
		}
	}

	/**
	 * Get a list of categories with the total number of tasks
	 * 
	 * @param tasks
	 *            the tasks to be accounted for
	 * @return the list of categories with its count
	 */
	protected static ArrayList<String> getListOfCategoriesWithCount(ArrayList<Task> tasks) {
		int uncompletedTasks = 0;
		for (Task t : tasks) {
			if (t.getIsCompleted() == Keywords.TASK_NOT_COMPLETED) {
				uncompletedTasks++;
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
		Iterator<Map.Entry<String, Integer>> it = noOfTasksPerCat.entrySet().iterator();
		ArrayList<String> temp = new ArrayList<>();
		while (it.hasNext()) {
			Map.Entry<String, Integer> pair = it.next();
			temp.add(pair.getKey() + Keywords.SPACE_STRING + "(" + pair.getValue() + ")");
		}
		temp.add("Uncompleted (" + uncompletedTasks + ")");
		// clear off the old list of categories
		noOfTasksPerCat.clear();
		return temp;
	}

	/**
	 * Filter the list of tasks by the categories
	 * 
	 * @param categories
	 *            the categories to be matched
	 * @param tasks
	 *            the tasks to be filtered
	 * @return the list of tasks filtered or empty list if none matched
	 */
	protected static ArrayList<Task> getTasksByCat(ArrayList<String> categories, ArrayList<Task> tasks) {
		ArrayList<Task> taskList = new ArrayList<Task>();
		for (Task t : tasks) {
			ArrayList<String> taskCats = t.getCategories();
			for (String cat : categories) {
				if (taskCats.contains(cat)) {
					taskList.add(t);
					break;
				}
			}
		}
		return taskList;
	}
}
