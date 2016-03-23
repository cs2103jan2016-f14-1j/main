package logic;

import shared.Keywords;
import shared.Task;
import storage.Storage;
import java.util.ArrayList;

public class MarkTask extends Functionality {
	
	public Notification prioritise(ArrayList<Integer> taskIDs) {
		Notification  n = new Notification();
		if (taskIDs.isEmpty()) {
			n.setTitle(Keywords.MESSAGE_ERROR);
		} else {
			for (int id : taskIDs) {
				prioritise(id);
			}
			n.setTitle("Prioritised Successful!");
			n.setMessage("Prioritised: " + taskIDs.toString());
		}
		super.synchronization();
		return n;
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
