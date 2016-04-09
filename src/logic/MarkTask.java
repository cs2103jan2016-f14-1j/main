//@@author A0135778N
/**
 * This Class handles all operations related to
 * marking or unmarking a task with priority.
 */

package logic;

import shared.Keywords;
import shared.Task;
import storage.Storage;
import java.util.ArrayList;

public class MarkTask extends Functionality {
	
// ========================= Main Prioritise Method =========================
	public Notification prioritise(ArrayList<Integer> taskIDs) {
		Notification  n = new Notification();
		ArrayList<Integer> validIDs = removeInvalidIDs(taskIDs);
		setNotifHistory(n, validIDs);
		return n;
	}

// ========================= Other Operations =========================
	
	private ArrayList<Integer> removeInvalidIDs(ArrayList<Integer> taskIDs) {
		ArrayList<Integer> validIDs = new ArrayList<Integer>();
		for (int id : taskIDs) {
			if (Storage.getTask(id) != null) {
				validIDs.add(id);
			}
		}
		return validIDs;
	}
	
	/**
	 * This method sets the notification title and
	 * message to be displayed back to user, and
	 * also adds the action done to History for use
	 * by the undo method.
	 * 
	 * @param n				Notification Object.
	 * @param validIDs		list of validIDs input by user.
	 */
	private void setNotifHistory(Notification n, ArrayList<Integer> validIDs) {
		if (validIDs.isEmpty()) {
			n.setTitle(Keywords.MESSAGE_ERROR);
			n.setMessage(Keywords.INVALID_ID);
		} else {
			for (int id : validIDs) {
				super.addToFuncTasks(Storage.getTask(id));
				togglePriority(id);
			}
			super.addToHistory("mark");
			n.setTitle(Keywords.MESSAGE_MARK_SUCCESS);
			n.setMessage(Keywords.MESSAGE_MARK_BODY + validIDs.toString());
			super.synchronization();
		}
	}

	private boolean togglePriority(int id) {
		Task t = Storage.getTask(id);
		if (doesTaskExist(t)) {
			return false;
		}
		t.togglePriority();
		return true;
	}
	
	private boolean doesTaskExist(Task t) {
		return t == null;
	}
}
