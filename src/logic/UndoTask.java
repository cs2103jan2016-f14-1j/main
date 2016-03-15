package logic;

import java.util.ArrayList;

import parser.Parser;
import shared.Keywords;
import shared.Task;
import storage.Storage;

public class UndoTask extends Functionality {

	public boolean undoTask() {
		ArrayList<Task> t = Storage.getLastTasks();
		String action = Storage.getLastAction();
		switch(action){
		case "do":
			for(Task task : t){
				Storage.getTask(task.getId()).setIsCompleted(Keywords.TASK_NOT_COMPLETED);
			}
			printSuccessful("Complete command undone");
			break;
		case "add":
			for(Task task: t){
				Storage.recycleId(task.getId());
				Storage.removeTaskUsingTaskId(task.getId());
			}
			printSuccessful("Add command undone");
			break;
		case "edit":
			for(Task task: t){
				//when there are more attributes, we will add accordingly
				Storage.getTask(task.getId()).setDate(task.getDate());
				Storage.getTask(task.getId()).setIntDate(task.getIntDate());
			}
			printSuccessful("Edit command undone");
			break;
		case "delete":
			for(Task task:t){
				Storage.addTaskToList(task);
				Storage.removeSpecificId(task.getId());
			}
			printSuccessful("Delete command undone");
			break;
		}
		if(t == null){
			Notification.setTitle("Undo Failed.");
			Notification.setMessage("Nothing to undo!");
		}
		super.synchronization();
		return true;
	}
	
	private void printSuccessful(String toUpdate){
		Notification.setTitle("Undo Successful.");
		Notification.setMessage(toUpdate);
	}
}
