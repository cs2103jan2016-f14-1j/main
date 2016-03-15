package shared;

import parser.*;
import storage.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Task {
	private final int INDEX_STARTDATE = 0;
	//private final int INDEX_ENDDATE = 1;
	private final int INDEX_STARTTIME = 1;
	private final int INDEX_ENDTIME = 2;
	private final SimpleDateFormat DATE_DISPLAY_FORMAT = new SimpleDateFormat("ddMMM");

	private int id = 0; 
	private String task; 
	private String date; 
	private ArrayList<Date> datetimes;
	private ArrayList<String> categories; 
	private int isCompleted; 
	private int priority;
	private int intDate; // for sorting purposes 
	private int intDateEnd;

	public Task() {
		id = 0;
		task = date = Keywords.EMPTY_STRING;
		datetimes = new ArrayList<Date>();
		initDatetimes();
		categories = new ArrayList<String>();
		intDate = Keywords.NO_DATE;
		intDateEnd = Keywords.NO_DATE;
		isCompleted = 0;
		priority = 0;
	}

	public Task(ArrayList<Date> datetimes, String taskName, ArrayList<String> cats) {
		this();
		setId(Storage.getNextAvailableID());
		setDateTimes(datetimes);
		setTask(taskName);
		setCategories(cats);
		initIntDate();
		//initIntDateEnd();
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
				id, task, intDate, intDateEnd, getIntStartTime(), getIntEndTime(), 
				Formatter.toCatsForStore(categories), isCompleted, priority);
	}
	private String getDisplayDate() {
		String sdate = datetimes.get(INDEX_STARTDATE) == null ? 
				Keywords.EMPTY_STRING :
				String.format("- %s", 
						DATE_DISPLAY_FORMAT.format(datetimes.get(INDEX_STARTDATE)));
		String timeformat = getDisplayTime();
		/*
		String edate = datetimes.get(INDEX_ENDDATE) == null ? 
				Keywords.EMPTY_STRING :
				DATE_DISPLAY_FORMAT.format(datetimes.get(INDEX_ENDDATE));
		*/
		return String.format(Keywords.DATE_FORMAT, 
				sdate, 
				timeformat);
	}
	public void callInitDate(){
		initIntDate();
	}
	public void setIntDate(){
		intDate = Keywords.NO_DATE;
	}
	private void initIntDate() {
		this.intDate = Formatter.fromDateToInt(datetimes.get(INDEX_STARTDATE));
	}
	/*
	private void initIntDateEnd() {
		this.intDateEnd = Formatter.fromDateToInt(datetimes.get(INDEX_ENDDATE));
	}
	*/
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
	public void setIntDate(int intDate) {
		this.intDate = intDate;
		datetimes.set(INDEX_STARTDATE, Formatter.fromIntToDate(String.valueOf(intDate)));
	}
	/*
	private void setIntDateEnd(int intDate) {
		this.intDateEnd = intDate;
		datetimes.set(INDEX_ENDDATE, Formatter.fromIntToDate(String.valueOf(intDate)));
	}
	*/
	private void setIntStartTime(String intTime) {
		if (Integer.parseInt(intTime) == Keywords.NO_DATE) {
			datetimes.set(INDEX_STARTTIME, null);
		} else {
			datetimes.set(INDEX_STARTTIME, Formatter.getDateFromString(intTime));
		}
	}
	private void setIntEndTime(String intTime) {
		if (Integer.parseInt(intTime) == Keywords.NO_DATE) {
			datetimes.set(INDEX_ENDTIME, null);
		} else {
			datetimes.set(INDEX_ENDTIME, Formatter.getDateFromString(intTime));
		}
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
		//temp.setIntDateEnd(Integer.parseInt(properties.get(Keywords.TASK_ENDDATE)));
		temp.setIntStartTime(properties.get(Keywords.TASK_STARTTIME));
		temp.setIntEndTime(properties.get(Keywords.TASK_ENDTIME));
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
				 task.getIntStartTime(), task.getIntEndTime(),
				cats, task.getIsCompleted(), task.getPriority());
		return toString;
	}
	
	public void setStartTime(Date d) {
		datetimes.set(INDEX_STARTTIME, d);
	}
	public void setEndTime(Date d) {
		datetimes.set(INDEX_ENDTIME, d);
	}
	public int getIntStartTime() {
		if (datetimes.get(INDEX_STARTTIME) == null) {
			return Keywords.NO_DATE;
		}
		return datetimes.get(INDEX_STARTTIME).getHours() * 100
				+ datetimes.get(INDEX_STARTTIME).getMinutes();
	}
	public int getIntEndTime() {
		if (datetimes.get(INDEX_ENDTIME) == null) {
			return Keywords.NO_DATE;
		}
		return datetimes.get(INDEX_ENDTIME).getHours() * 100
				+ datetimes.get(INDEX_ENDTIME).getMinutes();
	}

	public ArrayList<Date> getDatetimes() {
		return datetimes;
	}

	public void setDateTimes(ArrayList<Date> datetimes) {
		this.datetimes = datetimes;
	}
	
	public void initDatetimes() {
		for (int i = 0; i < Keywords.MAX_DATES; i++) {
			datetimes.add(null);
		}
	}
	
	public String getDisplayTime() {
		int startTime = getIntStartTime(), 
			endTime = getIntEndTime(); 
		if (startTime == Keywords.NO_DATE && endTime == startTime) {
			return Keywords.EMPTY_STRING;
		} else if (endTime == Keywords.NO_DATE) {
			return String.format("(%s)", startTime);
		} else {
			return String.format("(%s - %s)", startTime, endTime);
		}
	}
}
