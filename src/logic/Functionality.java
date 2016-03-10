package logic;

import storage.Storage;

public class Functionality {

	protected static void synchronization(){
		Storage.writeTasksToFile();
	}
}
