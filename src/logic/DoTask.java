package logic;

import shared.*;
import storage.Storage;
import java.util.ArrayList;
import shared.Keywords;

public class DoTask extends Functionality {

	private final String TASK_NOT_FOUND_MSG = "The task is not found";

	/**
	 * This method allows the user to mark task as completed
	 * 
	 * @param taskID
	 *            the taskID is used to search for the task in the storage
	 * @return it will return successful when a task is marked as completed,
	 *         else otherwise.
	 */

	public boolean doTask(ArrayList<Integer> taskIds, int doneOrNotDone) {
		boolean value = false;
		if (taskIds.isEmpty()) {
			Notification.setTitle(Keywords.MESSAGE_ERROR);
		} else if (taskIds.size() > 1) {
			Notification.setTitle(Keywords.MESSAGE_COMPLETED_SUCCESS);
			Notification.setMessage(taskIds.toString());
		} else {
			Notification.setTitle(Keywords.MESSAGE_COMPLETED_SUCCESS);
			Notification.setMessage(Storage.getTask(taskIds.get(Keywords.FIRST_ELEMENT)).getUserFormat() + "done!");
		}
		if (doneOrNotDone == Keywords.TASK_COMPLETED) {
			String undoAction = "uncomplete ";
			for (int taskID : taskIds) {
				if (doTask(taskID)) {
					value = true;
					undoAction += taskID + Keywords.SPACE_STRING;
				}
			}
			//Add to history the action to be done
			super.addToHistory(undoAction);
		}else{
			for (int taskID : taskIds) {
				if (undoTask(taskID)) {
					value = true;
				}
			}
		}
		super.synchronization();
		return value;
	}

	private boolean undoTask(int taskID) {
		Task t = Storage.getTask(taskID);
		if (doesTaskExist(t)) {
			return false;
		}

		Storage.getTask(taskID).setIsCompleted(Keywords.TASK_NOT_COMPLETED);
		return true;
	}

	private boolean doTask(int taskID) {
		Task t = Storage.getTask(taskID);
		if (doesTaskExist(t)) {
			return false;
		}
		t.setIsCompleted(Keywords.TASK_COMPLETED);
		return true;
	}

	private boolean doesTaskExist(Task t) {
		return t == null;
	}
}
