package logic;

import shared.*;
import storage.Storage;
import java.util.ArrayList;

public class DeleteTask extends Functionality {

	/** The following deleteTask() methods allow the user to delete task(s)
	 * @param int
	 *            taskID or a list of integers(taskIDs) the taskID is used to
	 *            search for the task in the storage
	 * @return it will return successful when a task is deleted, else otherwise.
	 */
	public boolean deleteTask(ArrayList<Integer> taskIds) {
		boolean value = false;
		if (taskIds.isEmpty()) {
			return value;
		} else {
			for (int id : taskIds) {
				if (deleteTask(id)) {
					value = true;
				}
			}
			return value;
		}
	}
	
	/**
	 * This method allows user to delete all tasks under a category.
	 * Finds all taskIDs of tasks under category and call the 
	 * deleteTask method
	 */
	public boolean deleteByCat(ArrayList<String> categories) {
		ArrayList<Task> taskList = Storage.getTasksByCat(categories); // TODO: Need to implement this in storage
		if (taskList.isEmpty()) {
			return false;
		}
		
		for (Task task : taskList) {
			deleteTask(task.getId());
		}
		
		return true;
	}
	
	/**
	 * 
	 * @param taskId
	 * @return
	 */
	private boolean deleteTask(int taskId) {
		if (Storage.getTask(taskId) == null) {
			return false;
		}
		Storage.removeTaskFromList(Storage.getTaskIndex(taskId));
		Storage.recycleId(taskId);
		super.synchronization();
		return true;
	}
	
}
