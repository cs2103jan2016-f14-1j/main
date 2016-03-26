package logic;

import shared.*;
import storage.Storage;
import java.util.ArrayList;
import java.util.Date;

public class EditTask extends Functionality {

	/**
	 * This method allows the user to edit a task
	 * 
	 * @param taskID
	 *            the taskID is used to search for the task in the storage
	 * @param date
	 *            changes to be made to the task's date
	 * @return it will return successful when a task is edited, else otherwise.
	 */
	public Notification editTask(int taskID, Task task) {
		Notification n = new Notification();
		if (!isValidId(taskID)) {
			n.setTitle(Keywords.MESSAGE_ERROR);
			n.setMessage(Keywords.INVALID_ID);
			return n;
		}
		if (Storage.getTask(taskID) != null) {
			//super.getTasks().add(Storage.getTask(taskID));
			super.addToFuncTasks(Storage.getTask(taskID));
			super.addToHistory("edit");
			
			if(!task.getTask().equals(Keywords.EMPTY_STRING)){
				Storage.getTask(taskID).setTask(task.getTask());
			}
			if(!task.getCategories().isEmpty()){
				Storage.getTask(taskID).setCategories(task.getCategories());
			}
			
			boolean dateExists = false;
			for(int i =0 ; i< task.getDatetimes().size(); i++){
				if(task.getDatetimes().get(i)!=null){
					dateExists = true;
					break;
				}
			}
			
			if(dateExists){
				Storage.getTask(taskID).setDateTimes(task.getDatetimes());
			}
			
			n.setTitle(Keywords.MESSAGE_EDIT_SUCCESS);
			n.setMessage(Storage.getTask(taskID).getUserFormat() + " has been edited!");
		}
		
		super.synchronization();
		return n;
	}

	private boolean isValidId(int taskID) {
		if (Storage.getTask(taskID) == null) {
			return false;
		}
		return true;
	}
}
