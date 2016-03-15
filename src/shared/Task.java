package shared;

import parser.*;
import storage.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Task {

	private int id = 0; 
	private String task; 
	private String date; 
	private Date startDate; 
	private Date endDate; 
	private int startTime; 
	private int endTime; 
	private ArrayList<String> categories; 
	private int isCompleted; 
	private int priority;
	private int intDate; // for sorting purposes 
	private int intDateEnd;

	public Task() {
		id = 0;
		task = date = Keywords.EMPTY_STRING;
		startDate = null;
		endDate = null;
		startTime = Keywords.NO_DATE;
		endTime = Keywords.NO_DATE;
		categories = new ArrayList<String>();
		intDate = Keywords.NO_DATE;
		intDateEnd = Keywords.NO_DATE;
		isCompleted = 0;
		priority = 0;
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
				Formatter.toCatsForStore(categories), isCompleted, priority);
	}
	
	private String getDisplayDate() {
		return String.format(Keywords.DATE_FORMAT, 
				startDate, endDate, startTime, endTime);
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
		this.startDate = Formatter.fromIntToDate(String.valueOf(intDate));
	}
	
	private void setIntDateEnd(int intDate) {
		this.intDateEnd = intDate;
		this.endDate = Formatter.fromIntToDate(String.valueOf(intDate));
	}

	public int getPriority(){
		return this.priority;
	}
	
	public void setPriority(int p) {
		this.priority = p;
	}
	
	public void togglePriority(){
		this.priority = this.priority == 0 ? 1 : 0;
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
		temp.setStartTime(Integer.parseInt(properties.get(Keywords.TASK_STARTTIME)));
		temp.setEndTime(Integer.parseInt(properties.get(Keywords.TASK_ENDTIME)));
		temp.setCategories(new ArrayList<String>(
				Arrays.asList(properties.get(Keywords.TASK_CATEGORIES).split(Keywords.SPACE_STRING))));
		temp.setIsCompleted(Integer.parseInt(properties.get(Keywords.TASK_ISCOMPLETE)));
		temp.setPriority(Integer.parseInt(properties.get(Keywords.TASK_PRIORITY)));
		temp.setTask(properties.get(Keywords.TASK_DESC));
		return temp;
	}

	/**
	 * formatted String to be written to file
	 * @param task
	 * @return
	 */
	public static String formatObjectToString(Task task) {
		String cats = Keywords.EMPTY_STRING;
		for (String s : task.getCategories()) {
			cats += s + Keywords.SPACE_STRING;
		}
		String toString = String.format(Keywords.STORAGE_FORMAT, task.getId(), 
				task.getTask(), task.getIntDate(),
				task.getIntDateEnd(), task.getStartTime(), task.getEndTime(),
				cats, task.getIsCompleted(), task.getPriority());
		return toString;
	}

	public boolean hasDateRange() {
		return endDate != null;
	}
	
	public boolean hasTimeRange() {
		return endTime != Keywords.NO_DATE;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public int getStartTime() {
		return startTime;
	}
	public int getEndTime() {
		return endTime;
	}
	public void setStartDate(Date d) {
		this.startDate = d;
	}
	public void setEndDate(Date d) {
		this.endDate = d;
	}
	public void setStartTime(int d) {
		this.startTime = d;
	}
	public void setEndTime(int d) {
		this.endTime = d;
	}
}
