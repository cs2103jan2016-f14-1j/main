package logic;

import shared.*;
import storage.Storage;
import java.util.ArrayList;
import java.util.Date;

public class EditTask extends Functionality {

	/**
	 * This method allows the user to edit a task
	 * 
	 * @param taskID
	 *            the taskID is used to search for the task in the storage
	 * @param date
	 *            changes to be made to the task's date
	 * @return it will return successful when a task is edited, else otherwise.
	 */
	public boolean editTask(int taskID, Date date) {
		if (Storage.getTask(taskID) != null) {
			//super.getTasks().add(Storage.getTask(taskID));
			super.addToFuncTasks(Storage.getTask(taskID));
			super.addToHistory("edit");
			ArrayList<Date> dt = new ArrayList<Date>();
			dt.add(date); dt.add(null); dt.add(null); dt.add(null);
			Storage.getTask(taskID).setDateTimes(dt);
			Storage.getTask(taskID).callInitDate();
			Notification.setTitle(Keywords.MESSAGE_EDIT_SUCCESS);
			Notification.setMessage(Storage.getTask(taskID).getUserFormat() + " has been edited!");
			// Storage.getTask(taskID).setTask(properties.get(Keywords.TASK_DESC));
		}
		// Storage.getTask(taskID).setCategories((properties.get(Keywords.TASK_CATEGORIES));
		/*
		 * int taskIndex = searchForTask(taskID); if (taskIndex ==
		 * TASK_NOT_FOUND) { System.out.println(TASK_NOT_FOUND_MSG); return
		 * false; } else if (date.isEmpty()) { return false; }
		 * 
		 * String task = Storage.getTaskByIndex(taskIndex); ArrayList<String>
		 * taskInformation = formatTaskForDisplay(task);
		 * taskInformation.set(TASK_DATE,date); task =
		 * formatTaskForStorage(taskInformation); syncTaskToList(task, 0,
		 * taskIndex, COMMAND.EDIT); return true;
		 */
		super.synchronization();
		return true;
	}
}
