package logic;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.ocpsoft.prettytime.shade.edu.emory.mathcs.backport.java.util.Collections;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import shared.*;
import storage.Storage;
import parser.Formatter;

public class FreeSlots {
	private ArrayList<Task> tasks = Storage.getListOfUncompletedTasks();
	private ArrayList<Task> tasksOnDate = new ArrayList<Task>();
	private ArrayList<Integer> availSlots = new ArrayList<Integer>();
	//FORMAT: ["when to when", ... ]
	private ArrayList<String> freeSlots = new ArrayList<String>();
	
	private HashMap<Integer, Boolean> timeSlots = initTimeSlot();
	
	private HashMap<Integer, Boolean> initTimeSlot() {
		HashMap<Integer, Boolean> hm = new HashMap<Integer, Boolean>(24);
		for (int i = 0; i < hm.size(); i++) {
			hm.put(i, false);
		}
		return hm;
	}
	// assume input is displayDate format e.g. 27Feb, 02Mar
	public ArrayList<String> getFreeSlots(String input) {
		return compileFreeSlots(input);
	}
	
	private ArrayList<String> compileFreeSlots(String input) {
		for (Task t : tasks) {
			if (t.getDisplayDate().equalsIgnoreCase(input)) {
				tasksOnDate.add(t);
			}
		}
		if (tasksOnDate.isEmpty()) {
			return freeSlots; // null list means all time slots available
		} else {
			for (Task t : tasksOnDate) {
				ArrayList<Date> dateTimes = t.getDatetimes();
				Date startT = dateTimes.get(Keywords.INDEX_STARTTIME);
				if (startT != null){ //either only one timing or range timing
					Date endT = dateTimes.get(Keywords.INDEX_ENDTIME);
					if (endT != null) {
						for (int i = startT.getHours(); i < endT.getHours(); i++) {
							timeSlots.put(i, true);
						}
					} else {
						timeSlots.put(startT.getHours(), true);
					}
				}
			}
			for (int key : timeSlots.keySet()){
				if (!timeSlots.get(key)) {
					availSlots.add(key);
				}
			}
			if (availSlots.isEmpty()) {
				freeSlots.add(Keywords.EMPTY_STRING); //empty string means no free slots already
				return freeSlots;
			}
			Collections.sort(availSlots);
			int start = availSlots.get(Keywords.FIRST_ELEMENT);
			int curr = start;
			for (int i : availSlots) {
				if (i == curr) {
					continue;
				} else if (curr == i-1){
					curr = i;
				} else {
					if (curr == start) {
						if (curr > 9) {
							freeSlots.add(String.format("%d00H", curr));
						}
						freeSlots.add(String.format("0%d00H", curr));
					} else {
						if (start > 9) {
							freeSlots.add(String.format("%d00H to %d00H", start, curr));
						} else if (curr > 9) {
							freeSlots.add(String.format("0%d00H to %d00H", start, curr));
						}
						freeSlots.add(String.format("0%d00H to 0%d00H", start, curr));
					}
				}
			}
		}
		return freeSlots;
	}
}
