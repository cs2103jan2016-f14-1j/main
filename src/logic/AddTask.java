package logic;

import shared.*;
import storage.Storage;

public class AddTask extends Functionality {
	
	public boolean addTask(Task task) {
		if (task.getTask().isEmpty()) {
			Notification.setTitle(Keywords.MESSAGE_ERROR);
			return false;
		}
		Notification.setTitle(Keywords.MESSAGE_ADD_SUCCESS);
		Notification.setMessage(task.getTask() + " has been added!");
		Storage.addTaskToList(task);
		super.synchronization();
		return true;
	}

}
