package logic;

import shared.*;
import storage.Storage;
import java.util.ArrayList;

public class DeleteTask {
	/** The following deleteTask() methods allow the user to delete task(s)
	 * @param int
	 *            taskID or a list of integers(taskIDs) the taskID is used to
	 *            search for the task in the storage
	 * @return it will return successful when a task is deleted, else otherwise.
	 */
	public boolean deleteTask(ArrayList<Integer> taskIds) {
		boolean value = false;
		for (int id : taskIds) {
			if (deleteTask(id)) {
				value = true;
			}
		}
		return value;
	}
	
	/**
	 * This method allows user to delete all tasks under a category.
	 * Finds all taskIDs of tasks under category and call the 
	 * deleteTask method
	 */
	public boolean deleteByCat(ArrayList<String> categories) {
		ArrayList<Integer> iDs = new ArrayList<Integer>();
		if (iDs.isEmpty()) {
			return false;
		}
		
		for (int taskID : iDs) {
			deleteTask(taskID);
		}
		
		return true;
	}
	
	/**
	 * 
	 * @param taskId
	 * @return
	 */
	private boolean deleteTask(int taskId) {
		return true;
	}
	
}
