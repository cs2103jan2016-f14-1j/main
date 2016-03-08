package logic;

import shared.*;
import storage.Storage;

public class AddTask {
	
	public static boolean addTask(Task task) {
		// TODO add task.....
		Storage.addTaskToList(task);
		return true;
	}

}
