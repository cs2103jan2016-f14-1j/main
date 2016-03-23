package logic;

import shared.*;
import storage.Storage;

public class AddTask extends Functionality {

	public Notification addTask(Task task) {
		if (task.getTask().isEmpty()) {
			setNTitle(Keywords.MESSAGE_ERROR);
			return getNotification();
		}

		// Add to history the action to be done
		setNTitle(Keywords.MESSAGE_ADD_SUCCESS);
		setNMessage(task.getTask() + " has been added!");
		Storage.addTaskToList(task);
		super.addToFuncTasks(task);
		super.addToHistory("add");
		super.synchronization();
		return getNotification();
	}

}
