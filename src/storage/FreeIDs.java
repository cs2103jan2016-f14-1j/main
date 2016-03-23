package storage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

import shared.Keywords;

public class FreeIDs {

	private static LinkedList<Integer> freeIDs;
	private static int currentTaskId = 0;
	
	protected static void init(){
		freeIDs = new LinkedList<Integer>();
	}
	
	public int getNextAvailID(){
		return getNextAvailableID();
	}
	
	protected static int getNextAvailableID() {
		sortIDs();
		if (freeIDs.isEmpty()) {
			generateID();
		}
		return freeIDs.poll();
	}

	protected static void addToFreeId(int id) {
		freeIDs.offerFirst(id);
	}
	
	protected static void removeSpecificId(int id){
		boolean foundID = false;
		for(int i : freeIDs){
			if(i==id){
				foundID = true;
			}
		}
		if(foundID==true){
			freeIDs.remove(new Integer(id));
		}
	}
	
	protected static String convertIDListToString() {
		String stringID = Keywords.EMPTY_STRING;
		if (freeIDs.isEmpty()) {
			generateID();
		}
		for(int id : freeIDs){
			stringID += id + Keywords.SPACE_STRING;
		}
		return stringID.trim();
	}
	
	protected static void convertIDStringToList(String s) {
		ArrayList<String> stringOfIds = new ArrayList<String>(Arrays.asList(s.split(Keywords.SPACE_STRING)));
		for (String id : stringOfIds) {
			freeIDs.offerFirst(Integer.parseInt(id));
		}
		setCurrentID(freeIDs.peek());
		sortIDs();
	}

	private static void sortIDs() {
		Collections.sort(freeIDs);
	}

	private static void setCurrentID(int id) {
		currentTaskId = id;
	}

	private static void generateID() {
		freeIDs.offer(++currentTaskId);
	}

}
