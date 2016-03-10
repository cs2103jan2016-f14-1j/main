package storage;

import java.util.Iterator;

import shared.Keywords;
import shared.Task;

public class Categories {

	protected static void addACountToCat(String category) {
		if (Storage.getNoOfTasksPerCat().get(category) == null) {
			Storage.getNoOfTasksPerCat().put(category, 1);
		} else {
			Storage.getNoOfTasksPerCat().put(category, Storage.getNoOfTasksPerCat().get(category) + 1);
		}
	}

	protected static String getNoOfUncompletedTasks() {
		for (Task t : Storage.getTasks()) {
			if (t.getIsCompleted() == Keywords.TASK_NOT_COMPLETED) {
				int currentCount = (Storage.getNoOfTasksPerCat().get("Uncompleted") == null) ? 0 : Storage.getNoOfTasksPerCat().get("Uncompleted");
				currentCount++;
				Storage.getNoOfTasksPerCat().put("Uncompleted", currentCount);
			}
		}
		return new String("Uncompleted ("+Storage.getNoOfTasksPerCat().get("Uncompleted")+")");
	}
}
