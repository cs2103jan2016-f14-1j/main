package logic;

import java.util.ArrayList;

import dotdotdot.Logic.COMMAND;

public class EditTask {
	
	/** This method allows the user to edit a task
	 * @param taskID
	 *            the taskID is used to search for the task in the storage
	 * @param date
	 *            changes to be made to the task's date
	 * @return it will return successful when a task is edited, else otherwise.
	 */
	public boolean editTask(int taskID, String date) {
		/*
		int taskIndex = searchForTask(taskID);
		if (taskIndex == TASK_NOT_FOUND) {
			System.out.println(TASK_NOT_FOUND_MSG);
			return false;
		} else if (date.isEmpty()) {
			return false;
		}

		String task = Storage.getTaskByIndex(taskIndex);
		ArrayList<String> taskInformation = formatTaskForDisplay(task);
		taskInformation.set(TASK_DATE,date);
		task = formatTaskForStorage(taskInformation);
		syncTaskToList(task, 0, taskIndex, COMMAND.EDIT);
		return true;
	*/
	}
}
