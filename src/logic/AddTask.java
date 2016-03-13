package logic;

import shared.*;
import storage.Storage;

public class AddTask extends Functionality {
	
	public boolean addTask(Task task, int isItUndoFunc) {
		if (task.getTask().isEmpty()) {
			Notification.setTitle(Keywords.MESSAGE_ERROR);
			return false;
		}
		Notification.setTitle(Keywords.MESSAGE_ADD_SUCCESS);
		Notification.setMessage(task.getTask() + " has been added!");
		Storage.addTaskToList(task);
		
		//Add to history the action to be done
		if(isItUndoFunc==0){
		String undoAction = "delete "+task.getId();
		Storage.addToHistory(undoAction);
		}
		
		super.synchronization();
		return true;
	}

}
