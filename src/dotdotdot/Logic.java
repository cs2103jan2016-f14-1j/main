package dotdotdot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class Logic {

	private Storage store = null;
	private ArrayList<Integer> currTaskIDs = new ArrayList<Integer>();
	private ArrayList<String> currTaskDescs = new ArrayList<String>();
	
	private ArrayList<String> tasksToDisplay = new ArrayList<String>();
	private static int VIEW_CURRENT = 1;
	private static String VIEW_CATEGORY = "";
	public static final int VIEW_DEFAULT = 1;
	public static final int VIEW_DONE = 2;
	public static final int VIEW_CAT = 3;
	
	private final String EMPTY_LIST_MSG = "The list is empty";
	private final String TASK_NOT_FOUND_MSG = "The task is not found";
	private final String PREP_BY_PREPEND = "by ";
	private final String PREP_ON_PREPEND = "on ";
	private final String TASKID_PREPEND = "#";
	private final String DATE_PREPEND = "- ";

	private final int TASK_ID = 0;
	private final int TASK_DESC = 1;
	private final int TASK_DATE = 2;
	private final int TASK_CATEGORIES = 3;
	private final int TASK_ISCOMPLETE = 4;
	private final int TASK_NOT_FOUND = -1;
	private final int TASK_BOTH = -2;
	private final String COMPLETED = "1";
	private final String NOT_COMPLETED = "0";
	private final String DELIMITER = "\\|";
	private final String WRITE_DELIMITER = "|";
	private final String EMPTY_STRING = "";
	private final String SPACE_STRING = " ";
	private final String PREP_BY = "by";
	
	private final ArrayList<String> EMPTY_ARRAYLIST = new ArrayList<String>();

	enum COMMAND {
		ADD, DELETE, EDIT, COMPLETE
	}

	public Logic() {
		store = new Storage();
	}
	
	//============================== START OF USER FUNCTIONS ==============================

	/**
	 * add task with taskName only
	 */
	public boolean addTask(String task) {
		String formatted = formatToDo(task, EMPTY_STRING, EMPTY_ARRAYLIST, NOT_COMPLETED); 
		commitToStore(formatted);
		return true;
	}
	
	/**
	 * add task with taskName and categories only
	 */
	public boolean addTask(String task, ArrayList<String> categories) {
		//String fullTask = addCategoriesToTask(task, categories);
		String formatted = formatToDo(task, EMPTY_STRING, categories, NOT_COMPLETED);
		commitToStore(formatted);
		return true;
	}

	/**
	 * add task with taskName and date
	 */
	public boolean addTask(String task, String preposition, String date) {
		if (isBy(preposition)) {
			date = concatBy(date);
		} else {
			date = concatOn(date);
		}
		String formatted = formatToDo(task, date, EMPTY_ARRAYLIST, NOT_COMPLETED);
		commitToStore(formatted);
		return true;
	}
	
	/**
	 * add task with taskName, date, and categories
	 */
	public boolean addTask(String task, String preposition, String date, ArrayList<String> categories) {
		String formatted = formatToDo(task, date, categories, NOT_COMPLETED);
		commitToStore(formatted);
		return true;
	}
	
	/** This method allows the user to edit a task
	 * @param taskID
	 *            the taskID is used to search for the task in the storage
	 * @param date
	 *            changes to be made to the task's date
	 * @return it will return successful when a task is edited, else otherwise.
	 */
	public boolean editTask(int taskID, String date) {
		int taskIndex = searchForTask(taskID);
		if (taskIndex == TASK_NOT_FOUND) {
			System.out.println(TASK_NOT_FOUND_MSG);
			return false;
		} else if (date.isEmpty()) {
			return false;
		}

		String task = store.getTaskByIndex(taskIndex);
		ArrayList<String> taskInformation = formatTaskForDisplay(task);
		taskInformation.set(TASK_DATE,date);
		task = formatTaskForStorage(taskInformation);
		syncTaskToList(task, 0, taskIndex, COMMAND.EDIT);
		return true;
	}

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
				currTaskIDs.add(id);
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
		iDs = getTaskIDsByCat(categories);
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
		int taskIndex = searchForTask(taskId);
		if (taskIndex == TASK_NOT_FOUND) {
			System.out.println(TASK_NOT_FOUND_MSG);
			return false;
		}
		currTaskDescs.add(getTaskDesc(store.getStoreFormattedToDos().get(taskIndex)));
		syncTaskToList(EMPTY_STRING, taskId, taskIndex, COMMAND.DELETE);
		writeToFile();
		return true;
	}
	
	/** This method allows the user to mark task as completed
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
		ArrayList<String> taskInformation = formatTaskForDisplay(task);
		taskInformation.set(TASK_ISCOMPLETE, COMPLETED);
		task = formatTaskForStorage(taskInformation);
		syncTaskToList(task, 0, taskIndex, COMMAND.COMPLETE);
		return true;
	}
	
	public ArrayList<String> viewTasks(int completed) {
		ArrayList<String> filterList = (ArrayList<String>) store.getStoreFormattedToDos().clone();
		for (int index = 0; index < filterList.size(); index++) {
			if (Integer.parseInt(formatTaskForDisplay(filterList.get(index)).get(TASK_ISCOMPLETE)) != completed && completed!=TASK_BOTH) {
				filterList.remove(index);
				index--;
			}else{
				filterList.set(index, formatToUserFormat(filterList.get(index)));
			}
		}
		return filterList;
	}
	
	//============================== END OF USER FUNCTIONS ==============================
	
	
	//============================== START OF SYNC FUNCTIONS ==============================
	/**
	 * 
	 * @param formatted
	 */
	private void commitToStore(String formatted) {
		store.addStoreFormattedToDo(formatted);
		store.writeToFile();
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
			writeToFile();
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
	
	//============================== END OF SYNC FUNCTIONS ==============================
	
	//============================== START OF GETTER FUNCTIONS ==============================

	/**
	 * This method is used to search for the task in the list, after which if
	 * the task is found, return the task index to the user
	 * 
	 * @param taskID
	 *            the task ID to be searched
	 * @return the task if it is found, otherwise -1 to represent not found
	 */
	public int searchForTask(int taskID) {
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
	 * This method is used to retrieve the task description of the unformatted task
	 * 
	 * @param task
	 *            takes in the unformatted task
	 * @return returns the description of the unformatted task
	 */
	private String getTaskDesc(String task) {
		return task.split(DELIMITER)[TASK_DESC];
	}
	
	/**
	 * Converts todos into user-readable format
	 * @return
	 */
	public ArrayList<String> getUserFormattedToDos() {
		ArrayList<String> out = new ArrayList<String>();
		for (String s : store.getStoreFormattedToDos()) {
			out.add(formatToUserFormat(s));
		}
		return out;
	}
	
	/**
	 * This method is to link GUI class to the storage class through logic class
	 * 
	 * @return return storage created in this class
	 */
	public Storage getStorage() {
		return store;
	}
	
	public ArrayList<Integer> getCurrTaskIDs(){
		return currTaskIDs;
	}
	public ArrayList<String> getCurrTaskDescs(){
		return currTaskDescs;
	}
	
	public void clearCurrTasks(){
	    currTaskIDs.clear();
	    currTaskDescs.clear();
	}
	
	public ArrayList<String> getCategoryOfToDo(String toDo){
		return separateCats(formatTaskForDisplay(toDo).get(TASK_CATEGORIES));
	}
	
	public ArrayList<String> getListOfCategoriesWithCount(){
		ArrayList<String> temp = store.getStoreFormattedToDos();
		
		for(String toDo : temp){
			if(Integer.parseInt(formatTaskForDisplay(toDo).get(TASK_ISCOMPLETE))==0){
				ArrayList<String> listOfCat = getCategoryOfToDo(toDo);
					for(String cat: listOfCat){
							if(!cat.equals(EMPTY_STRING)){
								int currentCount = store.getCountForEachCat(cat);
								currentCount++;
								store.addToHashMap(cat, currentCount);
						}
					}
			}
		}
		//format the list to be displayed
		HashMap<String, Integer> countCatTasksNo = store.getTasksCountPerCat();
		temp = new ArrayList<>();
		for(String category:countCatTasksNo.keySet()){
			int count = countCatTasksNo.get(category);
			temp.add(category+SPACE_STRING+"("+count+")");
		}
		return temp;
	}
	
	public ArrayList<Integer> getTaskIDsByCat(ArrayList<String> categories) {
		ArrayList<String> tasks = new ArrayList<String>();
		ArrayList<Integer> iDs = new ArrayList<Integer>();
		for (String cat : categories) {
			tasks.addAll(store.getIDByCat(cat));
		}
		for (String task : tasks) {
			iDs.add(Integer.parseInt(task.split(DELIMITER)[TASK_ID]));
		}
		return iDs;
	}
	//============================== END OF GETTER FUNCTIONS ==============================
	
	//============================== START OF FORMAT FUNCTIONS ==============================
	
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
	 * information stored using ArrayList<String>
	 * 
	 * @param task
	 *            the concatenated task to be split
	 * @return return the task in blocks of information stored in ArrayList
	 */
	private ArrayList<String> formatTaskForDisplay(String task) {
		return new ArrayList<String>(Arrays.asList(task.split(DELIMITER)));
	}
	
	/**
	 * 
	 * @param s
	 * @return
	 */
	private String formatToUserFormat(String s) {
		ArrayList<String> as = formatTaskForDisplay(s);
		String 	id = as.get(TASK_ID),
				name = as.get(TASK_DESC),
				cats = as.get(TASK_CATEGORIES),
				date = as.get(TASK_DATE);
		if (!date.equals(EMPTY_STRING)) {
			date = DATE_PREPEND + date;
		}
		String out = String.format("(%s%s) %s %s %s", TASKID_PREPEND, id, name, cats, date);
		
		return out;
	}
	
	/**
	 * 
	 * @param taskName
	 * @param date
	 * @param cats
	 * @param completeStatus
	 * @return
	 */
	private String formatToDo(	String taskName, String date, 
								ArrayList<String> cats, String completeStatus) {
		int forNewTaskId = store.getNextTaskId();
		String allCats = concatCats(cats);
		ArrayList<String> finalTodo = putStringsTogetherForStorage(forNewTaskId, taskName,
				date, allCats, completeStatus);
		return formatTaskForStorage(finalTodo);
	}
	
	/**
	 * 
	 * @param cats
	 * @return
	 */
	private String concatCats(ArrayList<String> cats) {
		String out = EMPTY_STRING;
		for(String s : cats) {
			out += s + SPACE_STRING;
		}
		return out.trim();
	}
	
	/** takes in int, Str, Str, Str, Str
	 * @return ArrayList<String> of size 5 of the above variables
	 */
	private ArrayList<String> putStringsTogetherForStorage(
			int a, String b, String c, String d, String e) {
		ArrayList<String> out = new ArrayList<String>();
		out.add(String.valueOf(a));
		out.add(b);
		out.add(c);
		out.add(d);
		out.add(e);
		return out;
	}

	/**
	 * helper methods used by addTask(String,String,String[,ArrayList<String>]):
	 * isBy(String), concatDateToTask(By|On)(String,String)
	 */
	private boolean isBy(String p) {
		return p.equals(PREP_BY);
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	private String concatBy(String date) {
		return PREP_BY_PREPEND + date;
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	private String concatOn(String date) {
		return PREP_ON_PREPEND + date;
	}
	
	private ArrayList<String> separateCats(String categories){
		return new ArrayList<String>(Arrays.asList(categories.split(SPACE_STRING)));
	}
	
	//============================== END OF FORMAT FUNCTIONS ==============================
	
	
	//============================== BEGIN OF TEST FUNCTIONS ==============================
	
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

	/**
	 * A general method to print out to the console
	 * 
	 * @param toPrint
	 *            the string to be printed out
	 */
	private void systemPrint(String toPrint) {
		System.out.println(toPrint);
	}
	
	//============================== START OF TEST FUNCTIONS ==============================
	
}
