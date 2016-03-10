package storage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

import shared.Keywords;

public class FreeIDs {

	private LinkedList<Integer> freeIDs;
	private int currentTaskId = 0;
	
	protected FreeIDs(){
		freeIDs = new LinkedList<Integer>();
	}
	
	protected int getNextAvailableID() {
		if (freeIDs.isEmpty()) {
			generateID();
		}
		return freeIDs.poll();
	}

	protected void addToFreeId(int id) {
		freeIDs.offerFirst(id);
	}
	
	protected String convertIDListToString() {
		String stringID = Keywords.EMPTY_STRING;
		if (freeIDs.isEmpty()) {
			generateID();
		}
		for(int id : freeIDs){
			stringID += id + Keywords.SPACE_STRING;
		}
		return stringID;
	}
	
	protected void convertIDStringToList(String s) {
		ArrayList<String> stringOfIds = new ArrayList<String>(Arrays.asList(s.split(Keywords.SPACE_STRING)));
		for (String id : stringOfIds) {
			freeIDs.offerFirst(Integer.parseInt(id));
		}
		setCurrentID(freeIDs.peek());
		sortIDs();
	}

	private void sortIDs() {
		Collections.sort(freeIDs);
	}

	private void setCurrentID(int id) {
		currentTaskId = id;
	}

	private void generateID() {
		freeIDs.offer(++currentTaskId);
	}

}
