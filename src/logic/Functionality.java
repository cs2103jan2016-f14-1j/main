package logic;

import storage.Storage;

public class Functionality {

	protected void synchronization(){
		Storage storage = new Storage();
		storage.writeTasksToFile();
	}
}
