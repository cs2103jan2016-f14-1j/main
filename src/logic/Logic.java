package logic;

import shared.*;
import storage.Storage;

public class Logic {
	
	public Logic(){
	}
	
	private boolean addTask(Task task) {
		AddTask.addTask(task);
		return true;
	}

	private boolean deleteTask(Task task) {
		// DeleteTask.addTask(task);
		return true;
	}
	
	private boolean doTask(Task task) {
		// DoTask.addTask(task);
		return true;
	}
	
	private boolean editTask(Task task) {
		// EditTask.addTask(task);
		return true;
	}
	
	private boolean viewTask(Task task) {
		// ViewTask.addTask(task);
		return true;
	}

}
