package dotdotdot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Logic {

	private Storage store = null;
	private final String EMPTY_LIST_MSG = "The list is empty";
	private final String TASK_NOT_FOUND_MSG = "The task is not found";
	private final String PREP_BY_PREPEND = " - by ";
	private final String PREP_ON_PREPEND = " - on ";

	private final int TASK_ID = 0;
	private final int TASK_DESC = 1;
	private final int TASK_DATE = 2;
	private final int TASK_CATEGORIES = 3;
	private final int TASK_ISCOMPLETE = 4;
	private final int TASK_NOT_FOUND = -1;
	private final String COMPLETED = "1";
	private final String NOT_COMPLETED = "0";
	private final String DELIMITER = "\\|";
	private final String WRITE_DELIMITER = "|";
	private final String EMPTY_STRING = "";
	private final String SPACE_STRING = " ";
	private final String PREP_BY = "by";

	enum COMMAND {
		ADD, DELETE, EDIT, COMPLETE
	}

	public Logic() {
		store = new Storage();
	}

	/**
	 * add task with taskName only
	 */
	public boolean addTask(String task) {
		String formatted = formatToDo(task, EMPTY_STRING, new ArrayList<String>(), 0); 
		commitToStore(formatted);
		return true;
	}
	private String formatToDo(	String taskName, String date, 
								ArrayList<String> cats, int completeStatus) {
		int forNewTaskId = store.getNextTaskId();
		String allCats = concatCats(cats);
		return 	forNewTaskId + WRITE_DELIMITER + taskName + WRITE_DELIMITER + 
		date + WRITE_DELIMITER + allCats + WRITE_DELIMITER + completeStatus + WRITE_DELIMITER;
	}
	private String concatCats(ArrayList<String> cats) {
		String out = EMPTY_STRING;
		for(String s : cats) {
			out += s + SPACE_STRING;
		}
		return out.trim();
	}

	/**
	 * add task with taskName and categories only
	 */
	public boolean addTask(String task, ArrayList<String> categories) {
		//String fullTask = addCategoriesToTask(task, categories);
		String formatted = formatToDo(task, EMPTY_STRING, categories, 0);
		commitToStore(formatted);
		return true;
	}

	/**
	 * used by addTask(String, ArrayList<String)
	 * 
	 * @param task
	 * @param cats
	 * @return task + cats
	 */
	private String addCategoriesToTask(String task, ArrayList<String> cats) {
		String output = task;
		for (String s : cats) {
			output += s + SPACE_STRING;
		}
		return output.trim();
	}

	/**
	 * add task with taskName and date
	 */
	public boolean addTask(String task, String preposition, String date) {
		String formatted = formatToDo(task, date, new ArrayList<String>(), 0);
		commitToStore(formatted);
		return true;
	}

	/**
	 * helper methods used by addTask(String,String,String[,ArrayList<String>]):
	 * isBy(String), concatDateToTask(By|On)(String,String)
	 */
	private boolean isBy(String p) {
		return p.equals(PREP_BY);
	}

	private String concatDateToTaskBy(String task, String date) {
		return task + PREP_BY_PREPEND + date;
	}

	private String concatDateToTaskOn(String task, String date) {
		return task + PREP_ON_PREPEND + date;
	}

	/**
	 * add task with taskName, date, and categories
	 */
	public boolean addTask(String task, String preposition, String date, ArrayList<String> categories) {
		String formatted = formatToDo(task, date, categories, 0);
		commitToStore(formatted);
		return true;
	}
	private void commitToStore(String formatted) {
		store.addStoreFormattedToDo(formatted);
		store.writeToFile();
	}

	/**
	 * This method allows the user to edit a task
	 * 
	 * @param taskID
	 *            the taskID is used to search for the task in the storage
	 * @param date
	 *            changes to be made to the task's date
	 * @return it will return successful when a task is edited, else otherwise.
	 */
	public boolean editTask(int taskID, String date) {
		ArrayList<String> list = store.getStoreFormattedToDos();
		// TODO: currently this checks not by TaskID but by order in ArrayList
		if (!isTaskFound(taskID, list)) {
			return false;
		}
		// TODO: edit task using date
		syncTaskToList(date, 0, taskID, COMMAND.EDIT);
		store.writeToFile();
		return true;
	}

	/**
	 * The following deleteTask() methods allow the user to delete task(s)
	 * 
	 * @param int
	 *            taskID or a list of integers(taskIDs) the taskID is used to
	 *            search for the task in the storage
	 * @return it will return successful when a task is deleted, else otherwise.
	 */

	public boolean deleteTask(int taskId) {
		int taskIndex = searchForTask(taskId);
		if (taskIndex == TASK_NOT_FOUND) {
			System.out.println(TASK_NOT_FOUND_MSG);
			return false;
		}

		syncTaskToList(EMPTY_STRING, taskId, taskIndex, COMMAND.DELETE);
		writeToFile();
		return true;
	}

	// TODO: don't bother doing delete of multiple task until
	// deleting single task via taskID is OK
	// ideally, this should just call deleteTask(int TaskID) multiple times
	//
	// need to do the taskID generator first
	public boolean deleteTask(ArrayList<Integer> taskIDs) {
		int numTasks = taskIDs.size();
		boolean value = false;
		for (int i = 0; i < numTasks; i++) {
			if (deleteTask(taskIDs.get(i))) {
				value = true;
			}
		}
		return value;
	}
	
	// Dummy method for JUnit testing
	public static boolean delete(int taskID) {
		Logic lg = new Logic();
		return lg.deleteTask(taskID);
	}
	// Dummy method for JUnit testing
	public static boolean delete(ArrayList<Integer> taskIDs) {
		Logic lg = new Logic();
		return lg.deleteTask(taskIDs);
	}

	private boolean isTaskFound(int taskID, ArrayList<String> list) {
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
		ArrayList<String> taskInformation = formatTaskforDisplay(task);
		taskInformation.set(TASK_ISCOMPLETE,COMPLETED);
		task = formatTaskForStorage(taskInformation);
		syncTaskToList(task, 0, taskIndex, COMMAND.COMPLETE);
		return true;
	}

	public ArrayList<String> viewIsCompletedTasks(int completed) {
		ArrayList<String> filterList = (ArrayList<String>) store.getStoreFormattedToDos().clone();
		for (int index = 0; index < filterList.size(); index++) {
			if (Integer.parseInt(formatTaskforDisplay(filterList.get(index)).get(TASK_ISCOMPLETE)) != completed) {
				filterList.remove(index);
				index--;
			}
		}
		return filterList;
	}

	/**
	 * This method is used to search for the task in the list, after which if
	 * the task is found, return the task index to the user
	 * 
	 * @param taskID
	 *            the task ID to be searched
	 * @return the task if it is found, otherwise -1 to represent not found
	 */
	private int searchForTask(int taskID) {
		if (store.isListEmpty()) {
			systemPrint(EMPTY_LIST_MSG);
			return TASK_NOT_FOUND;
		}
		ArrayList<String> toDoList = store.getStoreFormattedToDos();
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
	 * 
	 * @param task
	 *            takes in the unformatted task
	 * @return returns the ID of the unformatted task
	 */
	private String getTaskID(String task) {
		return task.split(DELIMITER)[TASK_ID];
	}

	/**
	 * This method is used to format the task information, which is in String[],
	 * for storing into the storage by concatenating the information into a
	 * string with delimiters
	 * 
	 * @param taskInformation
	 *            the block of information to be concatenate
	 * @return a string of concatenated line with delimiters
	 */
	private String formatTaskForStorage(ArrayList<String> taskInformation) {
		String concatTaskInfo = EMPTY_STRING;
		for (String info : taskInformation) {
			concatTaskInfo += info + WRITE_DELIMITER;
		}
		return concatTaskInfo;
	}

	/**
	 * This method is used to split the concatenated task into blocks of
	 * information stored using String[]. The String[] is then returned to the
	 * caller for display or other purposes.
	 * 
	 * @param task
	 *            the concatenated task to be split
	 * @return return the task in blocks of information stored in String[]
	 */
	private ArrayList<String> formatTaskforDisplay(String task) {
		return new ArrayList<String>(Arrays.asList(task.split(DELIMITER)));
	}

	/**
	 * This method is used to synchronize between the arrayList and the data in
	 * the file It will differentiate and run different type of code depends on
	 * the command it receives
	 * 
	 * @param taskToSync
	 *            the task in concatenated form
	 * @param taskIndex
	 *            the index that is currently refer to the task in the arrayList
	 * @param command
	 *            the action to be performed
	 * @return return the result, true being successful, false is fail
	 */
	private boolean syncTaskToList(String taskToSync, int taskId, int taskIndex, COMMAND command) {
		switch(command){
		case ADD:
			break;
		case DELETE:
			store.removeStoreFormattedToDo(taskId, taskIndex);
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
	 * 
	 * @return
	 */
	private boolean writeToFile() {
		store.writeToFile();
		return true;
	}
	
	public ArrayList<String> getUserFormattedToDos() {
		// TODO: format todos into nice nice for display
		ArrayList<String> out = new ArrayList<String>();
		for (String s : store.getStoreFormattedToDos()) {
			out.add(formatToUserFormat(s));
		}
		return out;
	}
	private String formatToUserFormat(String s) {
		// TODO: format it into user-viewable format (i.e. split by delimiters etc)
		return s;
	}
	/**
	 * This method is to link GUI class to the storage class through logic class
	 * 
	 * @return return storage created in this class
	 */
	public Storage getStorage() {
		return store;
	}

	/**
	 * A general method to print out to the console
	 * 
	 * @param toPrint
	 *            the string to be printed out
	 */
	private void systemPrint(String toPrint) {
		System.out.println(toPrint);
	}
}
