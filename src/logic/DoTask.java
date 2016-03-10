package logic;

import shared.*;
import storage.Storage;
import java.util.ArrayList;
import shared.Keywords;
import storage.Storage;

//import dotdotdot.Logic.COMMAND;

public class DoTask extends Functionality {

	private final String TASK_NOT_FOUND_MSG = "The task is not found";
	private final int COMPLETED = 1;

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
		Storage.getTask(taskID).setIsCompleted(COMPLETED);
		return true;
	}

	public boolean doTask(ArrayList<Integer> taskIds) {
		boolean value = false;
		for (int taskID : taskIds) {
			value = doTask(taskID);
		}
		super.synchronization();
		return value;
	}
}
