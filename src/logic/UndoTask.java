//@@author A0076520L

package logic;

import java.util.ArrayList;

import parser.Formatter;
import shared.Keywords;
import shared.Task;
import storage.Storage;

public class UndoTask extends Functionality {

	/**
	 * Check the logic for undoing a task
	 * 
	 * @return the Notification object
	 */
	public Notification undoTask() {
		ArrayList<Task> t = Storage.getLastTasks();
		String action = Storage.getLastAction();
		Notification n = new Notification();
		switch (action) {
		case "do":// handles the reverse of do
			for (Task task : t) {
				Storage.getTask(task.getId()).setIsCompleted(Keywords.TASK_NOT_COMPLETED);
			}
			n = printSuccessful("Complete command undone");
			break;
		case "add":// handles the reverse of add
			for (Task task : t) {
				Storage.recycleId(task.getId());
				Storage.removeTaskUsingTaskId(task.getId());
			}
			n = printSuccessful("Add command undone");
			break;
		case "edit":// handles the reverse of edit
			for (Task task : t) {
				// change back to the original values
				Storage.getTask(task.getId()).setDate(task.getDate());
				Storage.getTask(task.getId()).setIntDate(task.getIntDate());
				Storage.getTask(task.getId()).setCategories(task.getCategories());
				Storage.getTask(task.getId()).setDateTimes(task.getDateTimes());
				Storage.getTask(task.getId()).setIsCompleted(task.getIsCompleted());
				Storage.getTask(task.getId()).setPriority(task.getPriority());
				Storage.getTask(task.getId()).setTask(task.getTask());
				Storage.getTask(task.getId()).setStartTime(Formatter.getDateTimes(Integer.toString(task.getIntStartTime())).get(Keywords.INDEX_STARTTIME));
				Storage.getTask(task.getId()).setEndTime(Formatter.getDateTimes(Integer.toString(task.getIntEndTime())).get(Keywords.INDEX_ENDTIME));
			}
			n = printSuccessful("Edit command undone");
			break;
		case "delete":// handles the reverse of delete
			for (Task task : t) {
				Storage.addTaskToList(task);
				Storage.removeSpecificId(task.getId());
			}
			n = printSuccessful("Delete command undone");
			break;
		case "mark":// handles the reverse of mark
			for (Task task : t) {
				Storage.getTask(task.getId()).togglePriority();
			}
			n = printSuccessful("Mark command undone");
			break;
		}
		// if t does not contain anything
		if (t == null) {
			n.setTitle("Undo Failed.");
			n.setMessage("Nothing to undo!");
		}

		super.synchronization();
		return n;
	}

	/**
	 * Set up the Notification Object
	 * 
	 * @param toUpdate
	 *            the String to be input
	 * @return the Notification object
	 */
	private Notification printSuccessful(String toUpdate) {
		Notification n = new Notification();
		n.setTitle("Undo Successful.");
		n.setMessage(toUpdate);
		return n;
	}
}
