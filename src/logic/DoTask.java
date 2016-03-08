package logic;

import java.util.ArrayList;

public class DoTask {
	
	/** This method allows the user to mark task as completed
	 * @param taskID
	 *            the taskID is used to search for the task in the storage
	 * @return it will return successful when a task is marked as completed,
	 *         else otherwise.
	 */
	private boolean doTask(int taskID) {
		int taskIndex = searchForTask(taskID);
		if (taskIndex == TASK_NOT_FOUND) {
			systemPrint(TASK_NOT_FOUND_MSG);
			return false;
		}
		String task = store.getTaskByIndex(taskIndex);
		ArrayList<String> taskInformation = formatTaskForDisplay(task);
		taskInformation.set(TASK_ISCOMPLETE, COMPLETED);
		task = formatTaskForStorage(taskInformation);
		currTaskDescs.add(getTaskDesc(store.getStoreFormattedToDos().get(taskIndex)));
		syncTaskToList(task, 0, taskIndex, COMMAND.COMPLETE);
		return true;
	}
	
	public boolean doTask(ArrayList<Integer> taskIds) {
		boolean value = false;
		for (int id : taskIds) {
			if (doTask(id)) {
				currTaskIDs.add(id);
				value = true;
			}
		}
		return value;
	}
}
