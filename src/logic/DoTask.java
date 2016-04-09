//@@author A0076520L

package logic;

import shared.*;
import storage.Storage;
import java.util.ArrayList;
import shared.Keywords;

public class DoTask extends Functionality {

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
		// check if the validIds is empty
		if (validIds.isEmpty()) {
			n.setTitle(Keywords.MESSAGE_ERROR);
		} else if (validIds.size() > 1) { // shows that there will be definitely
											// more than 1 task been completed
			n.setTitle(Keywords.MESSAGE_COMPLETED_SUCCESS);
			n.setMessage(validIds.toString());
		} else {// there is only 1 task to be completed
			n.setTitle(Keywords.MESSAGE_COMPLETED_SUCCESS);
			n.setMessage(Storage.getTask(validIds.get(Keywords.FIRST_ELEMENT)).getUserFormat() + "done!");
		}
		for (int taskID : validIds) {
			doTask(taskID);
		}
		// Add to history the action to be done
		super.addToHistory("do");
		super.synchronization();
		return n;
	}

	/**
	 * Set the task to be completed
	 * 
	 * @param taskID
	 *            the ID to retrieve the task
	 * @return true if task is set as completed or false if it is not found
	 */
	private boolean doTask(int taskID) {
		Task t = Storage.getTask(taskID);
		if (doesTaskExist(t)) {
			return false;
		}
		super.addToFuncTasks(t);
		t.setIsCompleted(Keywords.TASK_COMPLETED);
		return true;
	}

	/**
	 * Check if the task is null
	 * 
	 * @param t
	 *            the task to be checked
	 * @return the truth value of t == null;
	 */
	private boolean doesTaskExist(Task t) {
		return t == null;
	}
}
