package logic;

import shared.*;
import storage.Storage;
import java.util.ArrayList;
import java.util.Date;

public class EditTask extends Functionality {

	/**
	 * This method allows the user to edit a task
	 * 
	 * @param taskIDs
	 *            the taskID is used to search for the task in the storage
	 * @param date
	 *            changes to be made to the task's date
	 * @return it will return successful when a task is edited, else otherwise.
	 */
	public Notification editTask(ArrayList<Integer> taskIDs, ArrayList<Date> datetimes , String task, ArrayList<String> categories) {
		Notification n = new Notification();
		for(int z = 0 ; z < taskIDs.size(); z++){
			if (!isValidId(taskIDs.get(z))) {
				n.setTitle(Keywords.MESSAGE_ERROR);
				n.setMessage(Keywords.INVALID_ID);
				return n;
			}
			if (Storage.getTask(taskIDs.get(z)) != null) {
				//super.getTasks().add(Storage.getTask(taskID));
				super.addToFuncTasks(Storage.getTask(taskIDs.get(z)));
				super.addToHistory("edit");
				
				if(!task.equals(Keywords.EMPTY_STRING)){
					Storage.getTask(taskIDs.get(z)).setTask(task);
				}
				if(!categories.isEmpty()){
					Storage.getTask(taskIDs.get(z)).setCategories(categories);
				}
				
				boolean dateExists = false;
				for(int i =0 ; i< datetimes.size(); i++){
					if(datetimes.get(i)!=null){
						
						dateExists = true;
						break;
					}
				}
				
				if(dateExists){
					Storage.getTask(taskIDs.get(z)).setDateTimes(datetimes);
	         	}
				
				n.setTitle(Keywords.MESSAGE_EDIT_SUCCESS);
				n.setMessage(Storage.getTask(taskIDs.get(z)).getUserFormat() + " has been edited!");
			}
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
