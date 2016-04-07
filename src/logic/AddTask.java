package logic;

import java.util.ArrayList;

import shared.*;
import storage.Storage;

public class AddTask extends Functionality {
	
	/**
	 * Adds a task, also checks if task's time slot clashes with another
	 * @param task
	 * @return Notification object
	 */
	public Notification addTask(Task task) {
		if (task.getTask().isEmpty()) {
			setNTitle(Keywords.MESSAGE_ERROR);
			return getNotification();
		}
		ArrayList<IntegerPair> freeS = FreeSlots.getFreeSlotsInt(task.getIntDate());
		// TODO: get warning notification for clashing tasks

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
