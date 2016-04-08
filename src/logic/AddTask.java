//@@author A0135778N

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
		if (hasCollision(freeS, task)){ // not working yet! TODO
			setNTitle(Keywords.MESSAGE_ADD_SUCCESS);
			setNMessage("Conflicting time slots!");
		} else {
			setNTitle(Keywords.MESSAGE_ADD_SUCCESS);
			setNMessage(task.getTask() + " has been added!");
		}

		// Add to history the action to be done
		Storage.addTaskToList(task);
		super.addToFuncTasks(task);
		super.addToHistory("add");
		super.synchronization();
		return getNotification();
	}

	private boolean hasCollision(ArrayList<IntegerPair> freeS, Task task) {
		for (IntegerPair slots : freeS){
			if (slots.inBetween(task.getIntStartTime())) {
				return true;
			} else if (slots.inBetween(task.getIntEndTime())) {
				return true;
			}
		}
		return false;
	}

}
