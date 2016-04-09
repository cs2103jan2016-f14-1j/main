//@@author A0135778N
/**
 * This Class handles all Adding-related operations.
 */

package logic;

import java.util.ArrayList;

import shared.Keywords;
import shared.Task;
import storage.Storage;

public class AddTask extends Functionality {
	
// ========================= Main Add Operation =========================
	public Notification addTask(Task task) {
		if (task.getTask().isEmpty()) {
			setNTitle(Keywords.MESSAGE_ERROR);
			return getNotification();
		}
		
		Storage.addTaskToList(task);
		setNotification(task);
		addActionToHistory(task);
		return getNotification();
	}

// ========================= Other Operations =========================
	/**
	 * Add to history the action to be done, which is stored
	 * for the undo operation.
	 * 
	 * @param task
	 */
	private void addActionToHistory(Task task) {
		super.addToFuncTasks(task);
		super.addToHistory("add");
		super.synchronization();
	}
	
	/**
	 * This method takes in the task object and checks for the appropriate
	 * notification title and message to be set for display back to user.
	 * 
	 * @param task
	 */
	private void setNotification(Task task) {
		
		ArrayList<Integer> conflictTaskIDs = FreeSlots.getConflictIDs(task);
		
		if (conflictTaskIDs.isEmpty()) {
			setNTitle(Keywords.MESSAGE_ADD_SUCCESS);
			setNMessage(task.getTask() + Keywords.MESSAGE_ADD_BODY);
		} else {
			setNTitle(Keywords.MESSAGE_ADD_SUCCESS);
			setNMessage(Keywords.MESSAGE_ADD_CONFLICT + conflictTaskIDs.toString());
		}
	}
}
