package logic;

import java.util.ArrayList;

import parser.Parser;
import shared.Keywords;
import shared.Task;
import storage.Storage;

public class UndoTask extends Functionality {

	public Notification undoTask() {
		ArrayList<Task> t = Storage.getLastTasks();
		String action = Storage.getLastAction();
		Notification n = new Notification();
		switch(action){
		case "do":
			for(Task task : t){
				Storage.getTask(task.getId()).setIsCompleted(Keywords.TASK_NOT_COMPLETED);
			}
			n = printSuccessful("Complete command undone");
			break;
		case "add":
			for(Task task: t){
				Storage.recycleId(task.getId());
				Storage.removeTaskUsingTaskId(task.getId());
			}
			n = printSuccessful("Add command undone");
			break;
		case "edit":
			for(Task task: t){
				//when there are more attributes, we will add accordingly
				Storage.getTask(task.getId()).setDate(task.getDate());
				Storage.getTask(task.getId()).setIntDate(task.getIntDate());
				Storage.getTask(task.getId()).setCategories(task.getCategories());
				Storage.getTask(task.getId()).setDateTimes(task.getDatetimes());
				Storage.getTask(task.getId()).setIsCompleted(task.getIsCompleted());
				Storage.getTask(task.getId()).setPriority(task.getPriority());
				Storage.getTask(task.getId()).setTask(task.getTask());
			}
			n = printSuccessful("Edit command undone");
			break;
		case "delete":
			for(Task task:t){
				Storage.addTaskToList(task);
				Storage.removeSpecificId(task.getId());
			}
			n = printSuccessful("Delete command undone");
			break;
		case "mark":
			for(Task task : t){
				Storage.getTask(task.getId()).togglePriority();
			}
			n = printSuccessful("Mark command undone");
			break;
		}
		if(t == null){
			n.setTitle("Undo Failed.");
			n.setMessage("Nothing to undo!");
		}
		
		super.synchronization();
		return n;
	}
	
	private Notification printSuccessful(String toUpdate){
		Notification n = new Notification();
		n.setTitle("Undo Successful.");
		n.setMessage(toUpdate);
		return n;
	}
}
