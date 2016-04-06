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
	public Notification editTask(int taskID, ArrayList<Date> datetimes , String task, ArrayList<String> categories, int resetDate, int resetTime) {
		Notification n = new Notification();
		System.out.println(taskID+"   " +datetimes.isEmpty()+"  :"+task+":"+categories.isEmpty());
			if (!isValidId(taskID)) {
				n.setTitle(Keywords.MESSAGE_ERROR);
				n.setMessage(Keywords.INVALID_ID);
				return n;
			}
			if (Storage.getTask(taskID) != null) {
				//super.getTasks().add(Storage.getTask(taskID));
				super.addToFuncTasks(Storage.getTask(taskID));
				super.addToHistory("edit");
				
				if(!task.equals(Keywords.EMPTY_STRING)){
					Storage.getTask(taskID).setTask(task);
				}
				if(!categories.isEmpty()){
					Storage.getTask(taskID).setCategories(categories);
				}
				
				boolean dateExists = false;
				for(int i =0 ; i< datetimes.size(); i++){
					if(datetimes.get(i)!=null){
						dateExists = true;
						System.out.println(i+" was ran");
						break;
					}
				}
				
				if(resetTime==1){
					Storage.getTask(taskID).setTimeEmpty();
				}
				if(resetDate==1){
					Storage.getTask(taskID).setDateEmpty();
				}
				
				if(dateExists){
					Storage.getTask(taskID).setDateTimes(datetimes);
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
