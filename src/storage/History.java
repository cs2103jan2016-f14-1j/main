package storage;

import java.util.LinkedList;

public class History {
	private static LinkedList<String> historyList;
	private static final String EMPTY_HISTORY = "History is empty";
	
	protected static void initHistory(){
		historyList = new LinkedList<String>();
	}
	
	protected static String getLastAction(){
		if(historyList.isEmpty()){
			return EMPTY_HISTORY;
		}
		return historyList.poll();
	}
	
	protected static void addActionToHistory(String action){
		historyList.addFirst(action);
	}
	
}
