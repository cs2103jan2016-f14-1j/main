package logic;

import shared.Keywords;
import shared.Task;
import storage.Storage;
import java.util.ArrayList;

public class MarkTask extends Functionality {
	
	public Notification prioritise(ArrayList<Integer> taskIDs) {
		Notification  n = new Notification();
		ArrayList<Integer> validIds = new ArrayList<Integer>();
		for (int id : taskIDs) { // fliters out non-existent ids
			if (Storage.getTask(id) != null) {
				validIds.add(id);
			}
		}
		if (validIds.isEmpty()) {
			n.setTitle(Keywords.MESSAGE_ERROR);
			n.setMessage(Keywords.INVALID_ID);
			return n;
		} else {
			for (int id : validIds) {
				prioritise(id);
			}
			n.setTitle("Prioritised Successful!");
			n.setMessage("Prioritised: " + validIds.toString());
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
