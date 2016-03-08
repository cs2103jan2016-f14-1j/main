package storage;

import java.util.Collections;

import shared.Keywords;

public class FreeIDs {

	protected static int getNextAvailableID() {
		if (isListEmpty()) {
			generateID();
		}
		return Storage.getFreeIDs().poll();
	}

	protected static void sortIDs() {
		Collections.sort(Storage.getFreeIDs());
	}

	protected static void addIDs(int id) {
		Storage.getFreeIDs().offerFirst(id);
	}

	protected static void setCurrentID(int id) {
		Storage.currentTaskId = id;
	}
	
	protected static String formatIDToString(){
		String stringID = Keywords.EMPTY_STRING;
		for(int id:Storage.getFreeIDs()){
			stringID+=id+Keywords.EMPTY_STRING;
		}
		return stringID;
	}

	private static boolean isListEmpty() {
		return Storage.getFreeIDs().isEmpty();
	}

	private static void generateID() {
		Storage.getFreeIDs().offer(Storage.currentTaskId++);
	}

}
