package logic;

import shared.*;
import storage.Storage;
import java.util.ArrayList;

public class AddTask {
	
	public String taskDesc;
	public String taskDate;
	public ArrayList<String> taskCats;
	public int taskStatus;
	
	public static boolean addTask(Task task) {
		Storage.addTaskToList(task);
		return true;
	}

}
