package storage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import shared.Keywords;

public class FreeIDs {

	public static int getNextAvailableID() {
		if (isListEmpty()) {
			generateID();
		}
		return Storage.getFreeIDs().poll();
	}
	
	/**
	 * only called when task is deleted (recycle the id)
	 * @param id
	 */
	public static void addToFreeId(int id) {
		Storage.getFreeIDs().offerFirst(id);
	}

	protected static void addIDs(int id) {
		Storage.getFreeIDs().offerFirst(id);
	}

	protected static void setCurrentID(int id) {
		Storage.currentTaskId = id;
	}

	private static boolean isListEmpty() {
		return Storage.getFreeIDs().isEmpty();
	}

	private static void generateID() {
		Storage.getFreeIDs().offer(++Storage.currentTaskId);
	}
	
	protected static String convertIDListToString() {
		String stringID = Keywords.EMPTY_STRING;
		if (isListEmpty()) {
			generateID();
		}
		for(int id : Storage.getFreeIDs()){
			stringID += id + Keywords.SPACE_STRING;
		}
		return stringID;
	}
	
	protected static void convertIDStringToList(String s) {
		ArrayList<String> stringOfIds = new ArrayList<String>(Arrays.asList(s.split(Keywords.SPACE_STRING)));
		for (String id : stringOfIds) {
			Storage.getFreeIDs().offerFirst(Integer.parseInt(id));
		}
		setCurrentID(Storage.getFreeIDs().peek());
		sortIDs();
	}


	private static void sortIDs() {
		Collections.sort(Storage.getFreeIDs());
	}

}
