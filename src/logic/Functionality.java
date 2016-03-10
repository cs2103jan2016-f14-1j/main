package logic;

import storage.Storage;

public class Functionality {

	protected static void synchronization(){
		//Storage storage = new Storage();
		Storage.writeTasksToFile();
	}
}
