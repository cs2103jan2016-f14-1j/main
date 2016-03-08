package shared;

import java.util.ArrayList;

public class Task {

	private int ID = 0;
	private String task = "";
	private String date = "";
	private ArrayList<String> categories;
	private int isCompleted = 0;
	private int intDate = 0;
	
	public Task(){
		categories = new ArrayList<String>();
	}

	public int getID() {
		return ID;
	}

	public void setID(int ID) {
		this.ID = ID;
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
