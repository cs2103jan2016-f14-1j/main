//@@author A0076520L

package storage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

import shared.Keywords;

public class FreeIDs {

	private static LinkedList<Integer> freeIDs;
	private static int currentTaskId;
	
	/**
	 * Initialize the variables
	 */
	protected static void init(){
		freeIDs = new LinkedList<Integer>();
		currentTaskId = 0;
	}
	
	/**
	 * A clear public method for callers
	 * @return
	 * 		the next available ID
	 */
	public int getNextAvailID(){
		return getNextAvailableID();
	}
	
	/**
	 * Check if the list is empty and generate ID using generateID()
	 * else poll ID from the list
	 * @return
	 * 		the next ID
	 */
	protected static int getNextAvailableID() {
		sortIDs();
		if (freeIDs.isEmpty()) {
			generateID();
		}
		return freeIDs.poll();
	}

	/**
	 * Add the ID to the list
	 * @param id
	 * 			the ID to be added
	 */
	protected static void addToFreeId(int id) {
		//detects if there are any repeated IDs
		if(!freeIDs.contains(id)){
			freeIDs.offerFirst(id);
		}
	}
	
	/**
	 * Remove the specific ID from the list
	 * @param id
	 * 			the ID to be removed
	 */
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
	
	/**
	 * Convert the list of ID to String for storing in file
	 * @return
	 * 		the string form of IDs
	 */
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
	
	/**
	 * Convert the String to an Integer object and store it in the list
	 * @param s
	 * 		the String to be converted
	 */
	protected static void convertIDStringToList(String s) {
		ArrayList<String> stringOfIds = new ArrayList<String>(Arrays.asList(s.split(Keywords.SPACE_STRING)));
		for (String id : stringOfIds) {
			freeIDs.offerFirst(Integer.parseInt(id));
		}
		setCurrentID(freeIDs.peek());
		sortIDs();
	}

	/**
	 * sort the IDs in ascending order
	 */
	private static void sortIDs() {
		Collections.sort(freeIDs);
	}

	/**
	 * set the currentID to be the ID next
	 * @param id
	 * 			the ID to be set
	 */
	private static void setCurrentID(int id) {
		currentTaskId = id;
	}

	/**
	 * Generate the next ID and store it in list
	 */
	private static void generateID() {
		freeIDs.offer(++currentTaskId);
	}

}
