package shared;

import parser.*;
import storage.*;
import shared.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Task {

	private int id = 0; 
	private String task; 
	private String date; 
	private String startDate; 
	private String endDate; 
	private String startTime; 
	private String endTime; 
	private ArrayList<String> categories; 
	private int isCompleted = 0; 
	private int intDate = Keywords.NO_DATE; // for sorting purposes 
	private int intDateEnd = Keywords.NO_DATE;

	public Task() {
		id = 0;
		task = date = startDate = endDate = startTime = endTime = "";
		categories = new ArrayList<String>();
		isCompleted = 0;
		intDate = Keywords.NO_DATE;
		intDateEnd = Keywords.NO_DATE;
	}

	public Task(String date, String taskName, ArrayList<String> cats) {
		this();
		setId(Storage.getNextAvailableID());
		setDate(date);
		setTask(taskName);
		setCategories(cats);
		initIntDate(date); // TODO: CHANGE TO START DATE
		initIntDateEnd(date); // TODO: CHANGE TO END DATE
	}
	/**
	 * @return task <String> to display to user
	 */
	public String getUserFormat() {		
		return String.format(Keywords.USER_FORMAT,
				id, task, Formatter.toCatsForDisplay(categories), getDisplayDate());
	}
	/**
	 * @return task <String> to be stored
	 */
	public String getStorageFormat() {
		return String.format(Keywords.STORAGE_FORMAT,
				id, task, intDate, intDateEnd, startTime, endTime, 
				Formatter.toCatsForStore(categories), isCompleted);
	}
	
	private String getDisplayDate() {
		return String.format(Keywords.DATE_FORMAT, 
				startDate, endDate, startTime, endTime);
	}
	
	private void initIntDate(String date) {
		if (date.equals(Keywords.EMPTY_STRING)) {
			this.intDate = Keywords.NO_DATE;
		}
		this.intDate = Formatter.fromDDMMMToInt(date);
	}
	private void initIntDateEnd(String date) {
		if (date.equals(Keywords.EMPTY_STRING)) {
			this.intDateEnd = Keywords.NO_DATE;
		}
		this.intDateEnd = Formatter.fromDDMMMToInt(date);
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
	
	public int getIntDateEnd() {
		return intDateEnd;
	}

	private void setIntDate(int intDate) {
		this.intDate = intDate;
		this.date = Formatter.fromIntToDDMMM(String.valueOf(intDate));
	}
	
	private void setIntDateEnd(int intDate) {
		this.intDateEnd = intDate;
		this.endDate = Formatter.fromIntToDDMMM(String.valueOf(intDate));
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
		temp.setIntDate(Integer.parseInt(properties.get(Keywords.TASK_STARTDATE)));
		temp.setIntDateEnd(Integer.parseInt(properties.get(Keywords.TASK_ENDDATE)));
		temp.setStartTime(properties.get(Keywords.TASK_STARTTIME));
		temp.setEndTime(properties.get(Keywords.TASK_ENDTIME));
		temp.setCategories(new ArrayList<String>(
				Arrays.asList(properties.get(Keywords.TASK_CATEGORIES).split(Keywords.SPACE_STRING))));
		temp.setIsCompleted(Integer.parseInt(properties.get(Keywords.TASK_ISCOMPLETE)));
		temp.setTask(properties.get(Keywords.TASK_DESC));
		return temp;
	}

	/**
	 * formatted String to be written to file
	 * @param task
	 * @return
	 */
	public static String formatObjectToString(Task task) {
		String toString = String.format("",task.getId(),task.getTask(),task.getIntDate(),
				task.getIntDateEnd(),task.getStartTime(),task.getEndTime());
		for (String cat : task.getCategories()) {
			toString += cat + Keywords.SPACE_STRING;
		}
		toString += Keywords.STORE_DELIMITER + task.getIsCompleted() + Keywords.STORE_DELIMITER;
		return toString;

	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	public boolean hasDateRange() {
		return endDate.equals(Keywords.EMPTY_STRING);
	}
	
	public boolean hasTimeRange() {
		return endTime.equals(Keywords.EMPTY_STRING);
	}
}
