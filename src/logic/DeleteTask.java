package logic;

import shared.*;
import storage.Storage;
import java.util.ArrayList;

public class DeleteTask extends Functionality {

	/** The following deleteTask() methods allow the user to delete task(s)
	 * @param int
	 *            taskID or a list of integers(taskIDs) the taskID is used to
	 *            search for the task in the storage
	 *        String
	 *            categories to delete
	 * @return it will return successful when a task is deleted, else otherwise.
	 */
	public boolean deleteTask(ArrayList<Integer> ids, ArrayList<String> cats) {
		if (ids.isEmpty() && cats.isEmpty()) {
			Notification.setTitle(Keywords.MESSAGE_ERROR);
		} else if (ids.size() + cats.size() > 1) {
			Notification.setTitle(Keywords.MESSAGE_DELETE_SUCCESS);
			if (cats.isEmpty()) {
				Notification.setMessage(ids.toString());
			} else if (ids.isEmpty()) {
				Notification.setMessage("All tasks under the following categories"
										+ " have been deleted: " + cats.toString());
			} else {
				Notification.setMessage(ids.toString() + "\nAll tasks under the following"
										+ " categories have been deleted: " + cats.toString());
			}
		} else {
			Notification.setTitle(Keywords.MESSAGE_DELETE_SUCCESS);
			if (cats.isEmpty()) {
				Task t = Storage.getTask(ids.get(Keywords.FIRST_ELEMENT));
				Notification.setMessage(t.getUserFormat());
			} else {
				Notification.setMessage("All tasks under the following categories"
										+ " have been deleted: " + cats.toString());
			}
		}
		return deleteByIds(ids) | deleteByCats(cats);
	}
	
	/**
	 * delete by ids
	 * @param ids
	 * @return
	 */
	private boolean deleteByIds(ArrayList<Integer> ids) {
		boolean value = false;
		for (int id : ids) {
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
	private boolean deleteByCats(ArrayList<String> categories) {
		ArrayList<Task> taskList = Storage.getTasksByCat(categories);
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
