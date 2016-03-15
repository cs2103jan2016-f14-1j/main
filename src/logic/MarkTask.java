package logic;

import shared.Keywords;
import shared.Task;
import storage.Storage;
import java.util.ArrayList;

public class MarkTask extends Functionality {
	
	public boolean prioritise(ArrayList<Integer> taskIDs) {
		if (taskIDs.isEmpty()) {
			Notification.setTitle(Keywords.MESSAGE_ERROR);
		} else {
			for (int id : taskIDs) {
				prioritise(id);
			}
			Notification.setTitle("Prioritised Successful!");
			Notification.setMessage("Prioritised: " + taskIDs.toString());
		}
		super.synchronization();
		return true;
	}
	
	private boolean prioritise(int id) {
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
