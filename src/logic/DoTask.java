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
	public boolean doTask(ArrayList<Integer> taskIds) {
		boolean value = false;
		for (int taskID : taskIds) {
			if (doTask(taskID)) {
				value = true;
			}
		}
		super.synchronization();
		return value;
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
