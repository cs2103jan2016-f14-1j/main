package logic;

import shared.*;
import storage.Storage;

public class AddTask extends Functionality {

	public boolean addTask(Task task) {
		if (task.getTask().isEmpty()) {
			Notification.setTitle(Keywords.MESSAGE_ERROR);
			return false;
		}

		// Add to history the action to be done
		Notification.setTitle(Keywords.MESSAGE_ADD_SUCCESS);
		Notification.setMessage(task.getTask() + " has been added!");
		Storage.addTaskToList(task);
		super.addToFuncTasks(task);
		super.addToHistory("add");
		super.synchronization();
		return true;
	}

}
