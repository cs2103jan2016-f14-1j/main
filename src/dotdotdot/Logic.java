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
	private final String SPACE_STRING = " ";
	
	enum COMMAND{
		ADD, DELETE, EDIT, COMPLETE
	}

	public Logic() {
		store = new Storage();
	}

	public boolean addTask(String task) {
		store.addUnformattedToDo(task);
		store.writeToFile();
		return true;
	}

	public boolean addTask(String task, ArrayList<String> categories) {
		String fullTask = addCategoriesToTask(task, categories);
		store.addUnformattedToDo(fullTask);
		store.writeToFile();
		return true;
	}
	
	/**
	 * used by addTask(String, ArrayList<String)
	 * @param task
	 * @param cats
	 * @return task + cats
	 */
	private String addCategoriesToTask(String task, ArrayList<String> cats) {
		String output = task;
		for(String s : cats) {
			output += s + SPACE_STRING;
		}
		return output;
	}

	public boolean addTask(String task, String preposition, String date) {
		// TODO: differentiate between different prepositions (i.e. by VS at/to/on)
		// TODO: add date
		return false;
	}

	public boolean addTask(String task, String preposition, String date, ArrayList<String> categories) {
		String fullTask = addCategoriesToTask(task, categories);
		store.addUnformattedToDo(fullTask);
		store.writeToFile();
		// TODO: differentiate between different prepositions (i.e. by VS at/to/on)
		// TODO: add date
		return true;
	}

	/**
	 * This method allows the user to edit a task
	 * 
	 * @param taskID
	 *            the taskID is used to search for the task in the storage
	 * @param date
	 *            changes to be made to the task's date
	 * @return 
	 * 			  it will return successful when a task is edited, else otherwise.
	 */
	public boolean editTask(int taskID, String date) {
		ArrayList<String> list = store.getUnformattedToDos();
		// TODO: currently this checks not by TaskID but by order in ArrayList
		if (!isTaskFound(taskID, list)) { 
			return false;
		}
		// TODO: edit task using date
		syncTaskToList(date, taskID, COMMAND.EDIT);
		store.writeToFile();
		return true;
	}
	
	/**
	 * The following deleteTask() methods allow the user to delete task(s)
	 * 
	 * @param int taskID or a list of integers(taskIDs)
	 *            the taskID is used to search for the task in the storage
	 * @return it will return successful when a task is deleted,
	 *         else otherwise.
	 */
	public boolean deleteTask(int taskID) {
		ArrayList<String> list = store.getUnformattedToDos();
		// TODO: currently this checks not by TaskID but by order in ArrayList
		if (!isTaskFound(taskID, list)) { 
			return false;
		}
		//String toDelete = store.getTaskByIndex(taskID);
		syncTaskToList(EMPTY_STRING, taskID, COMMAND.DELETE);
		writeToFile();
		return true;
	}

	// TODO:	don't bother doing delete of multiple task until
	// 			deleting single task via taskID is OK
	//			ideally, this should just call deleteTask(int TaskID) multiple times
	//
	// 			need to do the taskID generator first
	public boolean deleteTask(ArrayList<Integer> taskIDs) {
		ArrayList<String> list = store.getUnformattedToDos();
		for (int taskID: taskIDs) {
			if (!isTaskFound(taskID, list)) {
				return false;
			}
			String toDelete = store.getTaskByIndex(taskID);
			syncTaskToList(toDelete, taskID, COMMAND.DELETE);
			writeToFile();
			return true;
		}
		return false; //this part might have problems since the deletion does nothing
	}

	public boolean isTaskFound(int taskID, ArrayList<String> list) {
		return list.size() >= taskID;
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
			store.removeUnformattedToDos(taskIndex);
			break;
		case EDIT:
			store.setTaskByIndex(taskIndex, taskToSync);
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
	 * This method is to link GUI class to the storage class through logic class
	 * 
	 * @return
	 * 		return storage created in this class
	 */
	public Storage getStorage(){
		return store;
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
