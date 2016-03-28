package logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.ocpsoft.prettytime.shade.edu.emory.mathcs.backport.java.util.Collections;

import shared.*;
import storage.Storage;

public class FreeSlots {
	private static ArrayList<Task> tasks = Storage.getListOfUncompletedTasks();
	private static ArrayList<Task> tasksOnDate = new ArrayList<Task>();
	//FORMAT: ["xxxxH to xxxxH", ... ]
	private static ArrayList<String> freeSlots = new ArrayList<String>();
	
	private static HashMap<Integer, ArrayList<Integer>> timeSlots = new HashMap<Integer, ArrayList<Integer>>(24);
	
	private static void initTimeSlot() {
		for (int i = 0; i < 24; i++) {
			ArrayList<Integer> mins = new ArrayList<Integer>();
			for (int j = 0; j < 60; j++){ 
				mins.add(j);
			}
			timeSlots.put(i, mins);
		}
	}
	
	// assume input is displayDate format e.g. 27Feb, 02Mar
	public static ArrayList<String> getFreeSlots(String input) {
		//initTimeSlot();
		return compileFreeSlots(input);
	}
	
	private static ArrayList<String> compileFreeSlots(String input) {
		initTimeSlot();
		freeSlots.clear();
		filterDateTask(tasks, input);
		if (tasksOnDate.isEmpty()) {
			return freeSlots; // null list means all time slots available
		} else {
			for (Task t : tasksOnDate) {
				ArrayList<Date> dateTimes = t.getDatetimes();
				Date startT = dateTimes.get(Keywords.INDEX_STARTTIME);
				if (startT != null){ //either only one timing or range timing
					Date endT = dateTimes.get(Keywords.INDEX_ENDTIME);
					if (endT != null) {
						for (int i = startT.getHours(); i < endT.getHours()+1; i++) {
							ArrayList<Integer> mins = timeSlots.get(i);
							if (i == startT.getHours() && i == endT.getHours()) {
								// if the time blocked is 4.30pm-4.45pm
								ArrayList<Integer> temp = new ArrayList<Integer>(mins.subList(0, startT.getMinutes()));
								temp.addAll(mins.subList(endT.getMinutes(), mins.size()));
								mins = temp;
								System.out.println(temp.toString());
								timeSlots.replace(i, mins);
								break;
							}
							if (i == startT.getHours()){
								if (startT.getMinutes() == 0) {
									mins.clear();
									mins.add(0); 
									// if list only got 0 then whole hour blocked, but can still stop at that hour
									// e.g. 4pm-5pm blocked but can still do 3pm-4pm
									timeSlots.replace(i, mins);
								} else {
									mins = new ArrayList<Integer>(mins.subList(0, startT.getMinutes()));
									timeSlots.replace(i, mins);
								}
							} else if (i == endT.getHours()) {
								if (endT.getMinutes() == 0) {
									break;
								}
								mins = new ArrayList<Integer>(mins.subList(endT.getMinutes(), mins.size()));
								timeSlots.replace(i, mins);
							} else {
								mins.clear();
								mins.add(0);
								timeSlots.replace(i, mins);
							}
						}
					} else {
						ArrayList<Integer> mins = timeSlots.get(startT.getHours());
						mins.clear();
						mins.add(0); 
						timeSlots.replace(startT.getHours(), mins);
					}
				}
			}
			int startTRange = 0; // will be in the format 0000
			int endTRange = 0;
			boolean started = false;
			for (int key = 0; key < 24; key++) {
				int totalMinSize = timeSlots.get(key).size();
				if (totalMinSize == 1){
					if (started) {
						endTRange = key * 100;
						freeSlots.add(toTimeString(startTRange, endTRange));
						startTRange = (key + 1) * 100;
						endTRange = (key + 1) * 100;
						started = false;
						continue;
					}
					startTRange = (key+1) * 100;
					continue;
				}
				if (started) {
					endTRange = key * 100;
					if (totalMinSize == 60) {
						if (key == 23) {
							endTRange = (key * 100) + 59;
						} else {
							endTRange = (key + 1) * 100;
						}
						continue;
					} else {
						for (int i = 0; i < totalMinSize-1; i++) {
							if (timeSlots.get(key).get(i)+1 != timeSlots.get(key).get(i+1)) {
								freeSlots.add(toTimeString(startTRange, endTRange+1));
								startTRange = (key * 100) + timeSlots.get(key).get(i+1);
								endTRange = (key * 100) + timeSlots.get(key).get(i+1);
								started = true;
								continue;
							}
							endTRange = (key * 100) + timeSlots.get(key).get(i+1);
						}
					}
				} else {
					if (totalMinSize == 60) {
						started = true;
						startTRange = key * 100;
						endTRange = key * 100;
						continue;
					} else {
						startTRange = timeSlots.get(key).get(0);
						endTRange = timeSlots.get(key).get(0);
						for (int i = 0; i < totalMinSize-1; i++) {
							if (timeSlots.get(key).get(i)+1 != timeSlots.get(key).get(i+1)) {
								freeSlots.add(toTimeString(startTRange, endTRange));
								startTRange = (key * 100) + timeSlots.get(key).get(i+1);
								endTRange = (key * 100) + timeSlots.get(key).get(i+1);
								continue;
							}
							endTRange = (key * 100) + timeSlots.get(key).get(i+1);
						}
						started = true;
					}
				}
			}
			freeSlots.add(toTimeString(startTRange, endTRange));
		}
		return freeSlots;
	}
	
	private static String toTimeString(int startTRange, int endTRange) {
		String sString = "";
		String eString = "";
		if (startTRange >= 1000) {
			sString = String.format("%dH", startTRange);
		} else {
			if (startTRange >= 100) {
				sString = String.format("0%dH", startTRange);
			} else if (startTRange >= 10) {
				sString = String.format("00%dH", startTRange);
			} else {
				sString = String.format("000%dH", startTRange);
			}
		}
		
		if (startTRange == endTRange) {
			return sString;
		}
		
		if (endTRange >= 1000) {
			eString = String.format("%dH", endTRange);
		} else {
			if (endTRange >= 100) {
				eString = String.format("0%dH", endTRange);
			} else if (endTRange >= 10) {
				eString = String.format("00%dH", endTRange);
			} else {
				eString = String.format("000%dH", endTRange);
			}
		}
		return sString + " to " + eString;
	}

	private static void filterDateTask(ArrayList<Task> tasks, String input) {
		for (Task t : tasks) {
			// get display date might be buggy/wrong format!
			if (t.getDate().equalsIgnoreCase(input) && t.getIsCompleted() != Keywords.TASK_COMPLETED) {
				tasksOnDate.add(t);
			}
		}
	}
	
	/** populate mins arraylist
	private static void createNewMinList(ArrayList<Integer> mins) {
		for (int i = 0; i < 60; i++){ 
			mins.add(i);
		}
	} */
}
