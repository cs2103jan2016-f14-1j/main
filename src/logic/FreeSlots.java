//@@author A0135778N

package logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import shared.*;
import storage.Storage;

public class FreeSlots {
	private static ArrayList<Task> tasks = new ArrayList<Task>();
	private static ArrayList<Task> tasksOnDate = new ArrayList<Task>();
	private static ArrayList<Task> taskSlots = new ArrayList<Task>();
	private static ArrayList<IntegerPair> freeSlots = new ArrayList<IntegerPair>();
	
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
	
	public static ArrayList<IntegerPair> getFreeSlotsInt(int input) {
		tasks = Storage.getListOfUncompletedTasks();
		return compileFreeSlots(input);
	}
	// assume input is displayDate format e.g. 27Feb, 02Mar
	public static ArrayList<String> getFreeSlots(int input) {
		tasks = Storage.getListOfUncompletedTasks();
		return convertToArrayListString(compileFreeSlots(input));
	}
	private static ArrayList<String> convertToArrayListString(ArrayList<IntegerPair> aip) {
		//System.out.println(aip.isEmpty()==true);
		ArrayList<String> as = new ArrayList<String>();
		for (IntegerPair ip : aip) {
			as.add(toTimeString(ip.getInt1(), ip.getInt2()));
		}
		return as;
	}
	
	private static ArrayList<IntegerPair> compileFreeSlots(int input) {
		initTimeSlot();
		freeSlots.clear();
		filterTaskSlots(tasks, input);
		if (taskSlots.isEmpty()) {
			return freeSlots; // null list means all time slots available
		} else {
			for (Task t : taskSlots) {
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
					}
				}
			}
			int startTRange = 0;
			int endTRange = 0;
			boolean started = false;
			for (int key = 0; key < 24; key++) {
				int totalMinSize = timeSlots.get(key).size();
				if (totalMinSize == 1){
					if (started) {
						endTRange = key * 100;
						freeSlots.add(new IntegerPair(startTRange, endTRange));
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
						if (timeSlots.get(key).get(0) != 0) {
							freeSlots.add(new IntegerPair(startTRange, endTRange));
							startTRange = (key * 100) + timeSlots.get(key).get(0);
						}
						for (int i = 0; i < totalMinSize-1; i++) {
							if (timeSlots.get(key).get(i)+1 != timeSlots.get(key).get(i+1)) {
								freeSlots.add(new IntegerPair(startTRange, endTRange+1));
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
								freeSlots.add(new IntegerPair(startTRange, endTRange));
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
			freeSlots.add(new IntegerPair(startTRange, endTRange));
			System.out.printf("%d - %d\n", startTRange, endTRange);
		}
		return freeSlots;
	}
	
	private static String toTimeString(int startTRange, int endTRange) {
		String sString = "";
		String eString = "";
		int sHour = startTRange / 100 == 0 ? 12 : startTRange / 100;
		if (startTRange < 1200) {
			sString = String.format("%d:%02dam", sHour, startTRange % 100);
		} else {
			sString = String.format("%d:%02dpm", sHour - 12 == 0 ? 12 : sHour - 12, startTRange % 100);
		}
		int eHour = endTRange / 100 == 0 ? 12 : endTRange / 100;
		if (endTRange < 1200) {
			eString = String.format("%d:%02dam", eHour, endTRange % 100);
		} else {
			eString = String.format("%d:%02dpm", eHour - 12 == 0 ? 12 : eHour - 12, endTRange % 100);
		}
		return sString + " to " + eString;
	}
	
	private static void filterTaskSlots(ArrayList<Task> tasks, int input) {
		taskSlots.clear();
		filterDateTask(tasks, input);
		for (Task t : tasksOnDate) {
			if (t.getDatetimes().get(3) == null) { // no time range ignore
				continue;
			}
			taskSlots.add(t);
		}
	}

	private static void filterDateTask(ArrayList<Task> tasks, int input) {
		tasksOnDate.clear();
		for (Task t : tasks) {
			if (t.getDatetimes().get(2) == null) { // no time at all ignore
				continue;
			} else if (t.getIntDateEnd() != 9999) { // have range of dates
				if (input > t.getIntDate() && input < t.getIntDateEnd()) {
					tasksOnDate.add(t);
				} else {
					continue;
				}
			} else { // start date only
				if (t.getIntDate() == input && t.getIsCompleted() != Keywords.TASK_COMPLETED) {
					tasksOnDate.add(t);
				}
			}
		}
	}

	public static ArrayList<Integer> getConflict(Task task) {
		ArrayList<Integer> taskIDs = new ArrayList<Integer>();
		if (task.getDatetimes().get(2) == null) {
			taskIDs.add(task.getId());
			return taskIDs;
		}
		filterDateTask(tasks, task.getIntDate());
		
		if (task.getDatetimes().get(3) == null) { //task only has start time
			for (Task t : tasksOnDate) {
				if (t.getDatetimes().get(2) == null) {
					continue;
				} else if (t.getDatetimes().get(3) != null) { // t got time range
					IntegerPair tTimeRange = new IntegerPair(t.getIntStartTime(),t.getIntEndTime());
					if (tTimeRange.inBetween(task.getIntStartTime())) {
						taskIDs.add(t.getId());
					}
				} else { // t only got start time;
					if (t.getIntStartTime() == task.getIntStartTime()){
						taskIDs.add(t.getId());
					}
				}
			}
		} else { // task has a time range
			IntegerPair taskTimeRange = new IntegerPair(task.getIntStartTime(),task.getIntEndTime());
			for (Task t : tasksOnDate) {
				if (t.getDatetimes().get(2) == null) {
					continue;
				} else if (t.getDatetimes().get(3) != null) { // t got time range
					if (taskTimeRange.inBetween(t.getIntStartTime())
							|| taskTimeRange.inBetween(t.getIntEndTime())) {
						taskIDs.add(t.getId());
					}
				} else { // t only got start time
					if (taskTimeRange.inBetween(t.getIntStartTime())){
						taskIDs.add(t.getId());
					}
				}
			}
		}
		taskIDs.add(task.getId());
		return taskIDs;
	}
}
