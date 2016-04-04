package storage;

import java.util.ArrayList;
import java.util.LinkedList;

import shared.Keywords;
import shared.Task;

public class History {
	private static LinkedList<ArrayList<Task>> historyList;
	private static LinkedList<String> actionList;
	
	protected static void initHistory(){
		historyList = new LinkedList<ArrayList<Task>>();
		actionList = new LinkedList<String>();
	}
	
	protected static ArrayList<Task> getLastTasks(){
		if(historyList.isEmpty()){
			System.out.print("ran");
			return null;
		}
		return historyList.poll();
	}
	
	protected static String getLastAction(){
		if(actionList.isEmpty()){
			return Keywords.EMPTY_STRING;
		}
		return actionList.poll();
	}
	
	protected static ArrayList<Task> getLastTasksNoRemove(){
		if(historyList.isEmpty()){
			return null;
		}
		return historyList.peek();
	}
	
	protected static void addActionToHistory(ArrayList<Task> t, String action){
		historyList.addFirst(t);
		actionList.addFirst(action);
	}
	
}
