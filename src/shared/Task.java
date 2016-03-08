package shared;

import parser.*;
import java.util.ArrayList;

public class Task {
	
	private final String USER_FORMAT = "%s (#%s) %s %s %s";
	private final String STORAGE_FORMAT = "%d|%s|%s|%s|%d|";

	private int id = 0;
	private String task;
	private String date; 
	private ArrayList<String> categories;
	private int isCompleted = 0;
	private int intDate = 999; // for sorting purposes

	public Task(String date, String taskName, ArrayList<String> cats){
		setDate(date);
		setTask(taskName);
		setCategories(cats);
		
		initIntDate(date);
	}
	
	public String getUserFormat() {
		return String.format(USER_FORMAT,
				date, id, task, categories, Formatter.fromIntToDDMMM(date));
	}
	public String getStorageFormat() {
		return String.format(STORAGE_FORMAT,
				id, task, date, categories, isCompleted);
	}
	
	private void initIntDate(String date) {
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

}
