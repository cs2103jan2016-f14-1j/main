package logic;

import java.util.ArrayList;

public class ViewTask {
	
	public boolean sortTask(String sortType) {
		viewTasks(sortType);
		viewList.sort(null);
		return true;
	}
	
	public boolean viewTasks(String input) {
		defaultList.clear();
		viewList.clear();
		for (String task : store.getStoreFormattedToDos()) {
			defaultList.add(formatToUserFormat(task));
		}
		if (input.equalsIgnoreCase(DONE)) {
			viewList = view(Integer.parseInt(COMPLETED));
			return true;
		} else if (input.equalsIgnoreCase(NOT_DONE) || input.equalsIgnoreCase(SORT)) {
			viewList = view(Integer.parseInt(NOT_COMPLETED));
			return true;
		} else if (input.contains(CATEGORIES)) {
			viewList = viewByCat(input);
			if (viewList.isEmpty()) {
				return false;
			}
			return true;
		} else {
			return false;
		}
	}
	
	private ArrayList<String> view(int status) {
		ArrayList<String> tempList = new ArrayList<String>();
		for (String task : store.getStoreFormattedToDos()) {
			int taskStatus = Integer.parseInt(task.split(DELIMITER)[TASK_ISCOMPLETE]);
			if (taskStatus == status) {
				tempList.add(formatToUserFormat(task));
			}
		}
		return tempList;
	}
	
	private ArrayList<String> viewByCat(String input) {
		return getTasksByCat(input);
	}
}
