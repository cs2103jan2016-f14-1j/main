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

	public boolean deleteTask(Task task) {
		// DeleteTask.addTask(task);
		return true;
	}
	
	public static boolean doTask(ArrayList<Integer> taskIDs) {
		return new DoTask().doTask(taskIDs);
	}
	
	public boolean editTask(Task task) {
		// EditTask.addTask(task);
		return true;
	}
	
	public boolean viewTask(Task task) {
		// ViewTask.addTask(task);
		return true;
	}

}
