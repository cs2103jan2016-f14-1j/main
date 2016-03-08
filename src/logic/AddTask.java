package logic;

import java.util.ArrayList;

public class AddTask {
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
}
