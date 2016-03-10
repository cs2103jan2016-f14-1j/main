package logic;

import shared.*;
import storage.Storage;
import java.util.ArrayList;
import shared.Keywords;


public class DoTask extends Functionality {

	public boolean doTask(ArrayList<Integer> taskIds) {
		for (int taskID : taskIds) {
			doTask(taskID);
		}
		super.synchronization();
		return true;
	}

	private boolean undoTask(int taskID) {
		if (Storage.getTask(taskID) == null) {
			return false;
		}
		Storage.getTask(taskID).setIsCompleted(Keywords.TASK_NOT_COMPLETED);
		return true;
	}

	/**
	 * This method allows the user to mark task as completed
	 * 
	 * @param taskID
	 *            the taskID is used to search for the task in the storage
	 * @return it will return successful when a task is marked as completed,
	 *         else otherwise.
	 */
	private boolean doTask(int taskID) {
		if (Storage.getTask(taskID) == null) {
			return false;
		}
		Storage.getTask(taskID).setIsCompleted(Keywords.TASK_COMPLETED);
		return true;
	}
}
