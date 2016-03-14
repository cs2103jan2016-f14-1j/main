package shared;

import parser.*;
import storage.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Task {
	
	private final String USER_FORMAT = "(#%s) %s %s %s";
	private final String STORAGE_FORMAT = "%d|%s|%s|%s|%d|";

	private int id = 0;
	private String task;
	private String date; 
	private ArrayList<String> categories;
	private int isCompleted = 0;
	private int intDate = Keywords.NO_DATE; // for sorting purposes
	private boolean priority = false;

	public Task() {
		id = 0;
		task = "";
		date = "";
		categories = new ArrayList<String>();
		isCompleted = 0;
		intDate = Keywords.NO_DATE;
		priority = false;
	}

	public Task(String date, String taskName, ArrayList<String> cats) {
		this();
		setId(Storage.getNextAvailableID());
		setDate(date);
		setTask(taskName);
		setCategories(cats);
		initIntDate(date);
	}
	/**
	 * @return task <String> to display to user
	 */
	public String getUserFormat() {		
		return String.format(USER_FORMAT,
				id, task, Formatter.toCatsForDisplay(categories), getDisplayDate(date));
	}
	/**
	 * @return task <String> to be stored
	 */
	public String getStorageFormat() {
		return String.format(STORAGE_FORMAT,
				id, task, intDate, Formatter.toCatsForStore(categories), isCompleted);
	}
	
	private String getDisplayDate(String date) {
		return date.equals(Keywords.EMPTY_STRING) ? date : "- " + date;
	}
	
	public void callInitDate(){
		initIntDate(this.date);
	}
	
	private void initIntDate(String date) {
		if (date.equals(Keywords.EMPTY_STRING)) {
			this.intDate = Keywords.NO_DATE;
		}
		this.intDate = Formatter.fromDDMMMToInt(date);
	}

	public int getId() {
		return id;
	}

	public void setId(int ID) {
		this.id = ID;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public ArrayList<String> getCategories() {
		return categories;
	}

	public void setCategories(ArrayList<String> categories) {
		if (categories.size() == 1 && categories.get(0).equals(Keywords.EMPTY_STRING)) {
			this.categories = new ArrayList<String>();
		} else {
			this.categories = categories;
		}
	}

	public int getIsCompleted() {
		return isCompleted;
	}

	public void setIsCompleted(int isCompleted) {
		this.isCompleted = isCompleted;
	}

	public int getIntDate() {
		return intDate;
	}

	private void setIntDate(int intDate) {
		this.intDate = intDate;
		this.date = Formatter.fromIntToDDMMM(String.valueOf(intDate));
	}
	
	public boolean getPriority(){
		return this.priority;
	}
	
	public void setPriority(){
		if (this.priority == true) {
			this.priority = false;
		} else {
			this.priority = true;
		}
	}
	
	/**
	 * This method is used to split the concatenated task into blocks of
	 * information stored using ArrayList<String>
	 * 
	 * @param task
	 *            the concatenated task to be split
	 * @return return the task in blocks of information stored in ArrayList
	 */
	public static Task formatStringToObject(String task) {
		ArrayList<String> properties = new ArrayList<String>(Arrays.asList(task.split(Keywords.DELIMITER)));
		Task temp = new Task();
		temp.setId(Integer.parseInt(properties.get(Keywords.TASK_ID)));
		temp.setIntDate(Integer.parseInt(properties.get(Keywords.TASK_DATE)));
		temp.setCategories(new ArrayList<String>(
				Arrays.asList(properties.get(Keywords.TASK_CATEGORIES).split(Keywords.SPACE_STRING))));
		temp.setIsCompleted(Integer.parseInt(properties.get(Keywords.TASK_ISCOMPLETE)));
		temp.setTask(properties.get(Keywords.TASK_DESC));
		return temp;
	}

	public static String formatObjectToString(Task task) {
		String toString = task.getId() + Keywords.STORE_DELIMITER + task.getTask() + Keywords.STORE_DELIMITER
				+ task.getIntDate() + Keywords.STORE_DELIMITER;
		for (String cat : task.getCategories()) {
			toString += cat + Keywords.SPACE_STRING;
		}
		toString += Keywords.STORE_DELIMITER + task.getIsCompleted() + Keywords.STORE_DELIMITER 
					+ task.getPriority() + Keywords.STORE_DELIMITER;
		return toString;

	}
}
