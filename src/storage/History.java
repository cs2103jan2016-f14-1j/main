//@@author A0076520L

package storage;

import java.util.ArrayList;
import java.util.LinkedList;

import shared.Keywords;
import shared.Task;

public class History {
	private static LinkedList<ArrayList<Task>> historyList;
	private static LinkedList<String> actionList;
	
	/**
	 * Initialize the variables
	 */
	protected static void initHistory(){
		historyList = new LinkedList<ArrayList<Task>>();
		actionList = new LinkedList<String>();
	}
	
	/**
	 * Obtain the last set of tasks from the list
	 * @return 
	 * 		the last set of tasks or null if there are no tasks
	 */
	protected static ArrayList<Task> getLastTasks(){
		if(historyList.isEmpty()){
			return null;
		}
		return historyList.poll();
	}
	
	/**
	 * Obtain the last action performed by the user
	 * @return 
	 * 		the last action or empty string if there are no action
	 */
	protected static String getLastAction(){
		if(actionList.isEmpty()){
			return Keywords.EMPTY_STRING;
		}
		return actionList.poll();
	}
	
	/**
	 * Peek the list to check the last set of tasks stored in the history
	 * @return 
	 * 		the peeked set of tasks or null if there are no tasks
	 */
	protected static ArrayList<Task> peekLastTask(){
		if(historyList.isEmpty()){
			return null;
		}
		return historyList.peek();
	}
	
	/**
	 * Store the set of tasks and action to the variables
	 * @param tasks 
	 * 			the set of tasks to store
	 * @param action 
	 * 			the action to store
	 */
	protected static void addActionToHistory(ArrayList<Task> tasks, String action){
		historyList.addFirst(tasks);
		actionList.addFirst(action);
	}
	
}
