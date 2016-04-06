//@@author A0135778N

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
		ids = filterOutIds(ids);
		cats = filterOutCats(cats);
		if (ids.isEmpty() && cats.isEmpty()) {
			n.setTitle(Keywords.MESSAGE_ERROR);
		} else if (ids.size() + cats.size() > 1) {
			n.setTitle(Keywords.MESSAGE_DELETE_SUCCESS);
			if (cats.isEmpty()) {

				n.setMessage(ids.toString());
			} else if (ids.isEmpty()) {

				n.setMessage("Tasks under " + cats.toString() + " categories have been deleted!");
			} else {
				n.setMessage("Tasks under " + cats.toString() + " categories have been deleted!");
			}
		} else {
			n.setTitle(Keywords.MESSAGE_DELETE_SUCCESS);
			if (cats.isEmpty()) {
				Task t = Storage.getTask(ids.get(Keywords.FIRST_ELEMENT));
				n.setMessage(t.getUserFormat());
			} else {
				n.setMessage("Tasks under " + cats.toString() + " categories have been deleted!");
			}
		}
		deleteByIds(ids);
		deleteByCats(cats);
		return n;
	}

	private ArrayList<String> filterOutCats(ArrayList<String> cats) {
		ArrayList<String> newList = new ArrayList<String>();
		for (String cat : cats) {
			if (Storage.containsCat(cat)) {
				newList.add(cat);
			}
		}
		return newList;
	}

	private ArrayList<Integer> filterOutIds(ArrayList<Integer> ids) {
		ArrayList<Integer> validIds = new ArrayList<Integer>();
		for (int id : ids) { // fliters out non-existent ids
			if (Storage.getTask(id) != null) {
				validIds.add(id);
			}
		}
		return validIds;
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
