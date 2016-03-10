package logic;

import java.util.ArrayList;

import shared.*;
import storage.Storage;

public class Logic {
	
	public Logic(){
	}
	
	public static boolean addTask(Task task) {
		AddTask.addTask(task);
		return true;
	}

	private boolean deleteTask(Task task) {
		// DeleteTask.addTask(task);
		return true;
	}
	
	private boolean doTask(ArrayList<Integer> taskIDs) {
		return new DoTask().doTask(taskIDs);
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
