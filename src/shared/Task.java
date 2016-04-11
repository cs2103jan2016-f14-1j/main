//@@author A0125347H

package shared;

import parser.*;
import storage.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Task extends Logger {

	private final SimpleDateFormat DATE_DISPLAY_FORMAT = new SimpleDateFormat(Keywords.DDMMM);

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
		initDateTimes(); // add null Dates into datetimes
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
		initIntDateEnd();
	}

	/**
	 * @return task <String> to display to user
	 */
	public String getUserFormatNoDate() {
		return String
				.format(Keywords.USER_FORMAT, id, task, Formatter.toCatsForDisplay(categories), Keywords.EMPTY_STRING)
				.trim();
	}

	/**
	 * @return task <String> to display to user
	 */
	public String getUserFormat() {
		return String.format(Keywords.USER_FORMAT, id, task, Formatter.toCatsForDisplay(categories), getDisplayDate());
	}

	/**
	 * @return task <String> to be stored
	 */
	public String getStorageFormat() {
		return String.format(Keywords.STORAGE_FORMAT, id, task, intDate, intDateEnd, getIntStartTime(), getIntEndTime(),
				Formatter.toCatsForStore(categories), isCompleted, priority);
	}

	public String getDisplayDate() {
		String sdate = datetimes.get(Keywords.INDEX_STARTDATE) == null ? Keywords.EMPTY_STRING
				: DATE_DISPLAY_FORMAT.format(datetimes.get(Keywords.INDEX_STARTDATE));
		String edate = datetimes.get(Keywords.INDEX_ENDDATE) == null ? Keywords.EMPTY_STRING
				: DATE_DISPLAY_FORMAT.format(datetimes.get(Keywords.INDEX_ENDDATE));
		String timeFormat = getDisplayTimeRange();
		String dateFormat = getDisplayDateRange(sdate, edate);
		return String.format(Keywords.DATE_FORMAT, formatBothDateAndTime(dateFormat, timeFormat));
	}

	public void callInitDate() {
		initIntDate();
	}

	public void setIntDate() {
		intDate = Keywords.NO_DATE;
	}

	private void initIntDate() {
		this.intDate = Formatter.fromDateToInt(datetimes.get(Keywords.INDEX_STARTDATE));
	}

	private void initIntDateEnd() {
		this.intDateEnd = Formatter.fromDateToInt(datetimes.get(Keywords.INDEX_ENDDATE));
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

	/**
	 * sets intDate and datetimes[STARTDATE]
	 * 
	 * @param intDate
	 */
	public void setIntDate(int intDate) {
		this.intDate = intDate;
		datetimes.set(Keywords.INDEX_STARTDATE, Formatter.fromIntToDate(String.valueOf(intDate)));
	}

	private void setIntDateEnd(int intDate) {
		this.intDateEnd = intDate;
		datetimes.set(Keywords.INDEX_ENDDATE, Formatter.fromIntToDate(String.valueOf(intDate)));
	}

	private void setIntStartTime(String intTime) {
		if (Integer.parseInt(intTime) == Keywords.NO_DATE) {
			datetimes.set(Keywords.INDEX_STARTTIME, null);
		} else {
			datetimes.set(Keywords.INDEX_STARTTIME, Formatter.getDateFromString(intTime));
		}
	}

	private void setIntEndTime(String intTime) {
		if (Integer.parseInt(intTime) == Keywords.NO_DATE) {
			datetimes.set(Keywords.INDEX_ENDTIME, null);
		} else {
			datetimes.set(Keywords.INDEX_ENDTIME, Formatter.getDateFromString(intTime));
		}
	}

	public int getPriority() {
		return this.priority;
	}

	public void setPriority(int p) {
		this.priority = p;
	}
	
	public void togglePriority(){
		this.priority = (this.priority == 0) ? 1 : 0;
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
	 * 
	 * @param task
	 * @return
	 */
	public static String formatObjectToString(Task task) {
		String cats = convertToCategoriesString(task.getCategories());
		String stime = convertToTimeStringWithPrependedZeroes(task.getIntStartTime());
		String etime = convertToTimeStringWithPrependedZeroes(task.getIntEndTime());
		String toString = String.format(Keywords.STORAGE_FORMAT, task.getId(), task.getTask(), task.getIntDate(),
				task.getIntDateEnd(), stime, etime, cats, task.getIsCompleted(), task.getPriority());
		return toString;
	}

	private static String convertToCategoriesString(ArrayList<String> as) {
		String cats = Keywords.EMPTY_STRING;
		for (String s : as) {
			cats += s + Keywords.SPACE_STRING;
		}
		return cats;
	}

	private static String convertToTimeStringWithPrependedZeroes(int time) {
		return String.format("%04d", time);
	}

	public void setStartTime(Date d) {
		datetimes.set(Keywords.INDEX_STARTTIME, d);
	}

	public void setEndTime(Date d) {
		datetimes.set(Keywords.INDEX_ENDTIME, d);
	}

	public int getIntStartTime() {
		if (datetimes.get(Keywords.INDEX_STARTTIME) == null) {
			return Keywords.NO_DATE;
		}
		return datetimes.get(Keywords.INDEX_STARTTIME).getHours() * 100
				+ datetimes.get(Keywords.INDEX_STARTTIME).getMinutes();
	}

	public int getIntEndTime() {
		if (datetimes.get(Keywords.INDEX_ENDTIME) == null) {
			return Keywords.NO_DATE;
		}
		return datetimes.get(Keywords.INDEX_ENDTIME).getHours() * 100
				+ datetimes.get(Keywords.INDEX_ENDTIME).getMinutes();
	}

	public ArrayList<Date> getDateTimes() {
		return datetimes;
	}

	public void setDateEmpty() {
		datetimes.set(Keywords.INDEX_STARTDATE, null);
		datetimes.set(Keywords.INDEX_ENDDATE, null);
	}

	public void setTimeEmpty() {
		datetimes.set(Keywords.INDEX_STARTTIME, null);
		datetimes.set(Keywords.INDEX_ENDTIME, null);

	}

	public void setDateTimes(ArrayList<Date> datetimes) {
		if (datetimes.get(Keywords.INDEX_STARTTIME) != null || 
				datetimes.get(Keywords.INDEX_ENDTIME) != null) {
			this.datetimes.set(Keywords.INDEX_STARTTIME, datetimes.get(Keywords.INDEX_STARTTIME));
			this.datetimes.set(Keywords.INDEX_ENDTIME, datetimes.get(Keywords.INDEX_ENDTIME));
		}
		if (datetimes.get(Keywords.INDEX_STARTDATE) != null || 
				datetimes.get(Keywords.INDEX_ENDDATE) != null) {
			this.datetimes.set(Keywords.INDEX_STARTDATE, datetimes.get(Keywords.INDEX_STARTDATE));
			this.datetimes.set(Keywords.INDEX_ENDDATE, datetimes.get(Keywords.INDEX_ENDDATE));
		}
		initIntDate();
		initIntDateEnd();
	}

	public void initDateTimes() {
		datetimes = new ArrayList<Date>();
		for (int i = 0; i < Keywords.MAX_DATES; i++) {
			datetimes.add(null);
		}
	}

	public String getDisplayTimeRange() {
		int startTime = getIntStartTime(), endTime = getIntEndTime();
		if (startTime == Keywords.NO_DATE && endTime == Keywords.NO_DATE) {
			return Keywords.EMPTY_STRING;
		} else if (endTime == Keywords.NO_DATE) {
			return String.format("(%s)", getHumanReadableTimeFromIntTime(startTime));
		} else {
			return String.format("(%s - %s)", getHumanReadableTimeFromIntTime(startTime),
					getHumanReadableTimeFromIntTime(endTime));
		}
	}

	private String getDisplayDateRange(String sdate, String edate) {
		if (sdate.equals(Keywords.EMPTY_STRING)) {
			return Keywords.EMPTY_STRING;
		} else if (edate.equals(Keywords.EMPTY_STRING)) {
			return String.format("%s", sdate);
		} else {
			return String.format("%s to %s", sdate, edate);
		}
	}

	private String formatBothDateAndTime(String date, String time) {
		assert (date != null && time != null);
		logf("formatBothDateAndTime", String.format("%s (%s)", date, time));
		if (date.equals(Keywords.EMPTY_STRING) && time.equals(Keywords.EMPTY_STRING)) {
			return Keywords.EMPTY_STRING;
		} else if (time.equals(Keywords.EMPTY_STRING)) { // date != null
			return String.format("- %s", date);
		} else if (date.equals(Keywords.EMPTY_STRING)) { // time != null
			return String.format("- %s", time);
		} else {
			return String.format("- %s %s", date, time);
		}

	}

	private String getHumanReadableTimeFromIntTime(int time) {
		int hour = (time / 100 == 0) ? 12 : (time / 100);
		if (time < 1200) {
			return String.format("%d:%02dam", hour, time % 100);
		} else {
			return String.format("%d:%02dpm", (hour - 12 == 0) ? 12 : (hour - 12), (time % 100));
		}
		
	}
	
	//For JUnit Testing Purposes
	public boolean like(Task b){
		System.out.printf("like(): %d %d\n", intDate, b.getIntDate());
		for (String cat : b.getCategories()){
			if (!categories.contains(cat)){
				return false;
			}
		}
		for (Date d : b.getDateTimes()) {
			if (!datetimes.contains(d)) {
				return false;
			}
		}
		return (this.task.equals(b.getTask())
				&& this.intDate == b.getIntDate()) ? true : false; 
	}
}
