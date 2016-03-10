package logic;

import shared.*;
import storage.Storage;
import java.util.ArrayList;

public class AddTask extends Functionality {
	
	public String taskDesc;
	public String taskDate;
	public ArrayList<String> taskCats;
	public int taskStatus;
	
	public boolean addTask(Task task) {
		Storage.addTaskToList(task);
		super.synchronization();
		return true;
	}

}
