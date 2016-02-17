package dotdotdot;

import java.util.ArrayList;
import java.util.Iterator;

public class Logic {

	private Storage store = null;
	private final String EMPTY_LIST_MSG = "The list is empty";
	private final String TASK_NOT_FOUND_MSG = "The task is not found";
	private final int TASK_ID = 0;
	private final int TASK_DESC = 1;
	private final int TASK_DATE = 2;
	private final int TASK_CATEGORIES = 3;
	private final int TASK_ISCOMPLETE = 4;
	private final int TASK_NOT_FOUND = -1;
	private final String COMPLETED = "1";
	private final String NOT_COMPLETED = "0";
	private final String DELIMITER = "|";
	private final String EMPTY_STRING = "";
	
	enum COMMAND{
		ADD, DELETE, EDIT, COMPLETE
	}

	public Logic() {
		// Do case based on pass in command
		store = new Storage();
	}

	public boolean addTask(String task) {
		return false;
	}

	public boolean addTask(String task, ArrayList<String> categories) {
		return false;
	}

	public boolean addTask(String task, String preposition, String date) {
		return false;
	}

	public boolean addTask(String task, String preposition, String date, ArrayList<String> categories) {
		return false;
	}

	public boolean editTask(int taskID, String date) {
		return false;
	}

	public boolean deleteTask(int taskID) {
		return false;
	}

	public boolean deleteTask(ArrayList<Integer> taskIDs) {
		return false;
	}

	/**
	 * This method allows the user to mark task as completed
	 * 
	 * @param taskID
	 *            the taskID is used to search for the task in the storage
	 * @return it will return successful when a task is marked as completed,
	 *         else otherwise.
	 */
	public boolean doTask(int taskID) {
		int taskIndex = searchForTask(taskID);
		if (taskIndex == TASK_NOT_FOUND) {
			systemPrint(TASK_NOT_FOUND_MSG);
			return false;
		}
		String task = store.getTaskByIndex(taskIndex);
		String[] taskInformation = formatTaskforDisplay(task);
		taskInformation[TASK_ISCOMPLETE] = COMPLETED;
		task = formatTaskForStorage(taskInformation);
		syncTaskToList(task, taskIndex, COMMAND.COMPLETE);
		return true;
	}

	/**
	 * This method is used to search for the task in the list,
	 * after which if the task is found, return the task index to the user
	 * @param taskID
	 * 		the task ID to be searched
	 * @return
	 * 		the task if it is found, otherwise -1 to represent not found
	 */
	private int searchForTask(int taskID) {
		if (store.isListEmpty()) {
			systemPrint(EMPTY_LIST_MSG);
			return TASK_NOT_FOUND;
		}
		ArrayList<String> toDoList = store.getUnformattedToDos();
		Iterator<String> looper = toDoList.iterator();
		String currentLine = EMPTY_STRING;
		int indexCounter = 0;
		while (looper.hasNext()) {
			currentLine = looper.next();
			int currentTaskID = Integer.parseInt(getTaskID(currentLine));
			if (currentTaskID == taskID) {
				return indexCounter;
			}
			indexCounter++;
		}
		return TASK_NOT_FOUND;
	}

	/**
	 * This method is used to retrieve the task ID of the unformatted task
	 * @param task
	 * 		takes in the unformatted task
	 * @return
	 * 		returns the ID of the unformatted task
	 */
	public String getTaskID(String task) {
		return task.split(DELIMITER)[TASK_ID];
	}

	/**
	 * This method is used to format the task information, which is in String[], for storing
	 * into the storage by concatenating the information into a string with delimiters
	 * @param taskInformation
	 * 		the block of information to be concatenate
	 * @return
	 * 		a string of concatenated line with delimiters
	 */
	public String formatTaskForStorage(String[] taskInformation) {
		String concatTaskInfo = EMPTY_STRING;
		for (String info : taskInformation) {
			concatTaskInfo += info + DELIMITER;
		}
		return concatTaskInfo;
	}

	/**
	 * This method is used to split the concatenated task into blocks of information stored
	 * using String[]. The String[] is then returned to the caller for display or other purposes.
	 * @param task
	 * 		the concatenated task to be split
	 * @return
	 * 		return the task in blocks of information stored in String[]
	 */
	public String[] formatTaskforDisplay(String task) {
		return task.split(DELIMITER);
	}

	/**
	 * This method is used to synchronize between the arrayList and the data in the file
	 * It will differentiate and run different type of code depends on the command it receives
	 * 
	 * @param taskToSync
	 * 		the task in concatenated form
	 * @param taskIndex
	 * 		the index that is currently refer to the task in the arrayList
	 * @param command
	 * 		the action to be performed
	 * @return
	 * 		return the result, true being successful, false is fail
	 */
	public boolean syncTaskToList(String taskToSync, int taskIndex, COMMAND command) {
		switch(command){
		case ADD:
			break;
		case DELETE:
			break;
		case EDIT:
			break;
		case COMPLETE:
			store.setTaskByIndex(taskIndex, taskToSync);
			writeToFile();
		}
		return true;
	}

	/**
	 * This method will initiate the writeToFile method in the Storage class
	 * @return
	 */
	public boolean writeToFile() {
		store.writeToFile();
		return true;
	}

	/**
	 * A general method to print out to the console
	 * @param toPrint
	 * 		the string to be printed out
	 */
	public void systemPrint(String toPrint) {
		System.out.println(toPrint);
	}
}
