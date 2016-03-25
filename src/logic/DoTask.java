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

	public Notification doTask(ArrayList<Integer> taskIds) {
		Notification n = new Notification();
		ArrayList<Integer> validIds = new ArrayList<Integer>();
		for (int id : taskIds) { // fliters out non-existent ids
			if (Storage.getTask(id) != null) {
				validIds.add(id);
			}
		}
		if (validIds.isEmpty()) {
			n.setTitle(Keywords.MESSAGE_ERROR);
		} else if (validIds.size() > 1) {
			n.setTitle(Keywords.MESSAGE_COMPLETED_SUCCESS);
			n.setMessage(validIds.toString());
		} else {
			n.setTitle(Keywords.MESSAGE_COMPLETED_SUCCESS);
			n.setMessage(Storage.getTask(validIds.get(Keywords.FIRST_ELEMENT)).getUserFormat() + "done!");
		}
		for (int taskID : validIds) {
			if (doTask(taskID)) {
				//changing in progress
			}
		}
		// Add to history the action to be done
		super.addToHistory("do");
		super.synchronization();
		return n;
	}

	private boolean doTask(int taskID) {
		Task t = Storage.getTask(taskID);
		if (doesTaskExist(t)) {
			return false;
		}
		super.addToFuncTasks(t);
		t.setIsCompleted(Keywords.TASK_COMPLETED);
		return true;
	}

	private boolean doesTaskExist(Task t) {
		return t == null;
	}
}
