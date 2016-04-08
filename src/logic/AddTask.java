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
		ArrayList<IntegerPair> freeS = FreeSlots.getFreeSlotsInt(task.getIntDate()); //only get free slots
		ArrayList<Integer> taskIDs = FreeSlots.getConflict(task); //should handle single time tasks
		if (freeS.isEmpty()){ // if empty means free whole day
			if (taskIDs.size() == 1) {
				setNTitle(Keywords.MESSAGE_ADD_SUCCESS);
				setNMessage(task.getTask() + " has been added!");
			} else {
				setNTitle(Keywords.MESSAGE_ADD_SUCCESS);
				setNMessage("Conflicting time slots! Tasks: " + taskIDs.toString());
			}
		} else { // not free whole day, so need check if valid slot chosen
			if (validSlot(freeS, task)) {
				setNTitle(Keywords.MESSAGE_ADD_SUCCESS);
				setNMessage(task.getTask() + " has been added!");
			} else {
				setNTitle(Keywords.MESSAGE_ADD_SUCCESS);
				setNMessage("Conflicting time slots! Tasks: " + taskIDs.toString());
			}
		}

		// Add to history the action to be done
		Storage.addTaskToList(task);
		super.addToFuncTasks(task);
		super.addToHistory("add");
		super.synchronization();
		return getNotification();
	}

	private boolean validSlot(ArrayList<IntegerPair> freeS, Task task) {
		for (IntegerPair slots : freeS){
			if (task.getDatetimes().get(3) != null) {
				if (slots.inBetween(task.getIntStartTime()) &&
						slots.inBetween(task.getIntEndTime())) {
					return true;
				}
			}
		}
		return false;
	}

}
