package shared;

import parser.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Task {

	private int id = 0;
	private String task;
	private String date;
	private ArrayList<String> categories;
	private int isCompleted = 0;
	private int intDate = 999; // for sorting purposes

	public Task() {

	}

	public Task(String date, String taskName, ArrayList<String> cats) {
		setDate(date);
		setTask(taskName);
		setCategories(cats);

		initIntDate(date);
	}

	public void initIntDate(String date) {
		setIntDate(Formatter.fromDDMMMToInt(date));
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
		this.categories = categories;
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

	public void setIntDate(int intDate) {
		this.intDate = intDate;
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
		temp.setDate(properties.get(Keywords.TASK_DATE));
		temp.setIntDate(0);// have not set what position is this intDate
							// supposed to be
		temp.setIsCompleted(Integer.parseInt(properties.get(Keywords.TASK_ISCOMPLETE)));
		temp.setTask(properties.get(Keywords.TASK_DESC));
		return temp;
	}

	public static String formatObjectToString(Task task) {
		// [taskID]|[task]|[date]|[categories]|[isComplete]|
		String toString = task.getId() + Keywords.STORE_DELIMITER + task.getTask() + Keywords.STORE_DELIMITER
				+ task.getDate() + Keywords.STORE_DELIMITER;
		for (String cat : task.getCategories()) {
			toString += cat + Keywords.SPACE_STRING;
		}
		toString += Keywords.STORE_DELIMITER + task.getIsCompleted() + Keywords.STORE_DELIMITER;
		return toString;

	}

}
