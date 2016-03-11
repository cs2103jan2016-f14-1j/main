package storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import shared.Keywords;
import shared.Task;

public class Categories {

	private static HashMap<String, Integer> noOfTasksPerCat;

	protected static void init() {
		noOfTasksPerCat = new HashMap<>();
	}

	protected static void addACountToCat(ArrayList<String> category) {
		for (String cat : category) {
			if (noOfTasksPerCat.get(cat) == null) {
				noOfTasksPerCat.put(cat, 1);
			} else {
				noOfTasksPerCat.put(cat, noOfTasksPerCat.get(cat) + 1);
			}
		}
	}

	protected static void removeACountFromCat(ArrayList<String> category) {
		for (String cat : category) {
			if (noOfTasksPerCat.get(cat) == null) {
				noOfTasksPerCat.put(cat, 0);
			} else {
				noOfTasksPerCat.put(cat, noOfTasksPerCat.get(cat) - 1);
			}
		}
	}

	protected static ArrayList<String> getListOfCategoriesWithCount(ArrayList<Task> tasks) {
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
		temp.add(getUncompletedCatWithCount(tasks));
		noOfTasksPerCat.clear();
		return temp;
	}

	protected static ArrayList<Task> getTasksByCat(ArrayList<String> categories, ArrayList<Task> tasks) {
		ArrayList<Task> taskList = new ArrayList<Task>();
		for (Task t : tasks) {
			ArrayList<String> cats = t.getCategories();
			for (String cat : cats) {
				if (categories.contains(cat)) {
					taskList.add(t);
				}
			}
		}
		return taskList;
	}

	private static String getUncompletedCatWithCount(ArrayList<Task> tasks) {
		for (Task t : tasks) {
			if (t.getIsCompleted() == Keywords.TASK_NOT_COMPLETED) {
				int currentCount = (noOfTasksPerCat.get("Uncompleted") == null) ? 0
						: noOfTasksPerCat.get("Uncompleted");
				currentCount++;
				noOfTasksPerCat.put("Uncompleted", currentCount);
			}
		}
		int count = (noOfTasksPerCat.get("Uncompleted") == null) ? 0
				: noOfTasksPerCat.get("Uncompleted");
		return new String("Uncompleted (" + count + ")");
	}
}
