package logic;

import storage.Storage;

public class Functionality {

	protected static void synchronization(){
		Storage.writeTasksToFile();
	}
	
	protected static void addToHistory(String action){
		Storage.addToHistory(action);
	}
	
}
