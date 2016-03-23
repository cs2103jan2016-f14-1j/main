package logic;

import shared.*;
import storage.Storage;
import java.util.ArrayList;

public class DeleteTask extends Functionality {

	/**
	 * The following deleteTask() methods allow the user to delete task(s)
	 * 
	 * @param int
	 *            taskID or a list of integers(taskIDs) the taskID is used to
	 *            search for the task in the storage String categories to delete
	 * @return it will return successful when a task is deleted, else otherwise.
	 */
	public Notification deleteTask(ArrayList<Integer> ids, ArrayList<String> cats) {
		Notification n = new Notification();
		if (ids.isEmpty() && cats.isEmpty()) {
			n.setTitle(Keywords.MESSAGE_ERROR);
		} else if (ids.size() + cats.size() > 1) {
			n.setTitle(Keywords.MESSAGE_DELETE_SUCCESS);
			if (cats.isEmpty()) {

				n.setMessage(ids.toString());
			} else if (ids.isEmpty()) {

				n.setMessage("All tasks under the following categories" + " have been deleted: " + cats.toString());
			} else {
				n.setMessage(ids.toString() + "\nAll tasks under the following" + " categories have been deleted: "
						+ cats.toString());
			}
		} else {
			n.setTitle(Keywords.MESSAGE_DELETE_SUCCESS);
			if (cats.isEmpty()) {
				Task t = Storage.getTask(ids.get(Keywords.FIRST_ELEMENT));
				n.setMessage(t.getUserFormat());
			} else {
				n.setMessage("All tasks under the following categories" + " have been deleted: " + cats.toString());
			}
		}
		deleteByIds(ids);
		deleteByCats(cats);
		return n;
	}

	/**
	 * delete by ids
	 * 
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
		super.addToHistory("delete");
		return value;
	}

	/**
	 * This method allows user to delete all tasks under a category. Finds all
	 * taskIDs of tasks under category and call the deleteTask method
	 */
	private boolean deleteByCats(ArrayList<String> categories) {
		ArrayList<Task> taskList = Storage.getTasksByCat(categories);
		if (taskList.isEmpty()) {
			return false;
		}

		for (Task task : taskList) {
			deleteTask(task.getId());
		}

		super.addToHistory("delete");
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
		super.addToFuncTasks(Storage.getTask(taskId));
		Storage.removeTaskFromList(Storage.getTaskIndex(taskId));
		Storage.recycleId(taskId);
		super.synchronization();
		return true;
	}

}
