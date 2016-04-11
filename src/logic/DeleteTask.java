//@@author A0135778N
/**
 * This Class handles all Deleting-related operations.
 */

package logic;

import java.util.ArrayList;
import shared.Keywords;
import shared.Task;
import storage.Storage;

public class DeleteTask extends Functionality {

// ========================= Main Delete Method =========================
	/**
	 * The following deleteTask() methods allow the user to delete task(s)
	 * 
	 * @param int	taskID or a list of integers(taskIDs) the taskID is used to
	 * 				search for the task in the storage String categories to delete
	 * @return 		It will return successful when a task is deleted, else otherwise.
	 */
	public Notification deleteTask(ArrayList<Integer> ids, ArrayList<String> cats) {
		Notification n = new Notification();
		ids = filterOutInvalidIDs(ids);
		cats = filterOutInvalidCats(cats);
		setNotification(n, cats, ids);
		deleteByIds(ids);
		deleteByCats(cats);
		super.addToHistory("delete");
		return n;
	}

// ========================= Supporting Delete Methods =========================
	private void setNotification(Notification n, ArrayList<String> cats, ArrayList<Integer> ids) {
		if (isBothEmpty(ids, cats)) {
			n.setTitle(Keywords.MESSAGE_ERROR);
		} else if (isBothNonEmpty(ids, cats)) {
			n.setTitle(Keywords.MESSAGE_DELETE_SUCCESS);
			n.setMessage(String.format(Keywords.MESSAGE_DELETE_CAT, cats.toString())
						+ ids.toString());
		} else {
			n.setTitle(Keywords.MESSAGE_DELETE_SUCCESS);
			if (cats.isEmpty()) {
				if (ids.size() == 1) {
					Task t = Storage.getTask(ids.get(Keywords.FIRST_ELEMENT));
					n.setMessage(t.getUserFormat());
				} else {
					n.setMessage(ids.toString());
				}
			} else {
				n.setMessage(String.format(Keywords.MESSAGE_DELETE_CAT, cats.toString()));
			}
		}
	}

	/**
	 * Finds all tasks through the list of taskIDs and calls
	 * deleteTask(taskID) method to remove each task.
	 * 
	 * @param ids	List of valid taskIDs to be removed.
	 */
	private void deleteByIds(ArrayList<Integer> ids) {
		for (int id : ids) {
			deleteTask(id);
		}
	}
	
	/**
	 * This method allows user to delete all tasks under a category. Finds all
	 * tasks under the category/categories removes them through the
	 * deleteTask(taskID) method.
	 * 
	 * @param categories	List of category's/categories' tasks to remove.
	 */
	private void deleteByCats(ArrayList<String> categories) {
		ArrayList<Task> taskList = Storage.getTasksByCat(categories);
		for (Task task : taskList) {
			deleteTask(task.getId());
		}
	}

	/**
	 * This method removes a task using its taskID
	 * 
	 * @param taskId
	 */
	private void deleteTask(int taskId) {
		super.addToFuncTasks(Storage.getTask(taskId));
		Storage.removeTaskUsingTaskId(taskId);
		Storage.recycleId(taskId);
		super.synchronization();
	}
	
// ========================= Other Methods =========================
	private boolean isBothNonEmpty(ArrayList<Integer> ids, ArrayList<String> cats) {
		return (!ids.isEmpty() && !cats.isEmpty());
	}

	private boolean isBothEmpty(ArrayList<Integer> ids, ArrayList<String> cats) {
		return (ids.isEmpty() && cats.isEmpty());
	}
	
	private ArrayList<String> filterOutInvalidCats(ArrayList<String> cats) {
		ArrayList<String> newList = new ArrayList<String>();
		for (String cat : cats) {
			if (Storage.containsCat(cat)) {
				newList.add(cat);
			}
		}
		return newList;
	}

	private ArrayList<Integer> filterOutInvalidIDs(ArrayList<Integer> ids) {
		ArrayList<Integer> validIDs = new ArrayList<Integer>();
		for (int id : ids) {
			if (Storage.getTask(id) != null) {
				validIDs.add(id);
			}
		}
		return validIDs;
	}
}
