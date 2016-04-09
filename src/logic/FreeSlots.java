//@@author A0135778N
/**
 * This class computes all the free time slots for each day as well as
 * checks for which tasks have conflicting time slots.
 */

package logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import shared.IntegerPair;
import shared.Keywords;
import shared.Task;
import storage.Storage;

public class FreeSlots {
	
// ============== Main Methods For Retrieving Free Time Slots =================
	public static ArrayList<IntegerPair> getFreeSlotsInt(int input) {
		return compileFreeSlots(input);
	}

	public static ArrayList<String> getFreeSlots(int input) {
		return convertToArrayListString(compileFreeSlots(input));
	}
	
//============== Main Methods For Retrieving Conflicting Tasks ===============
	public static ArrayList<Task> findConflict() {
		ArrayList<Task> tasks = Storage.getListOfUncompletedTasks();
		ArrayList<Integer> dates = getAllDatesWithTime(tasks);
		ArrayList<Task> totalConflict = new ArrayList<Task>();
		for (int date : dates) {
			ArrayList<Task> tasksOnDate = filterTasksByDate(tasks, date);
			ArrayList<Task> conflictByDate = new ArrayList<Task>();
			for (int i = 0; i < tasksOnDate.size(); i++) {
				Task task = tasksOnDate.get(i);
				ArrayList<Task> taskList = findAllConflict(tasksOnDate, task);
				for (Task t : taskList) {
					if (!conflictByDate.contains(t)) {
						conflictByDate.add(t);
					}
				}
			}
			for (Task t : conflictByDate) {
				if (!totalConflict.contains(t)) {
					totalConflict.add(t);
				}
			}
		}
		return totalConflict;
	}

	public static ArrayList<Integer> getConflictIDs(Task task) {
		ArrayList<Task> taskList = findConflictByTask(task);
		ArrayList<Integer> taskIDs = new ArrayList<Integer>();
		for (Task t : taskList) {
			taskIDs.add(t.getId());
		}
		return taskIDs;
	}

// ========================= Free Slots Compilation Operation =========================
	private static ArrayList<IntegerPair> compileFreeSlots(int input) {
		HashMap<Integer, ArrayList<Integer>> timeSlots = initTimeSlot();
		ArrayList<Task> tasks = Storage.getListOfUncompletedTasks();
		ArrayList<IntegerPair> freeSlots = new ArrayList<IntegerPair>();
		ArrayList<Task> taskSlots = filterTaskSlots(tasks, input);
		if (taskSlots.isEmpty()) {
			// null list means all time slots available
			return freeSlots;
		} else {
			for (Task t : taskSlots) {
				ArrayList<Date> dateTimes = t.getDateTimes();
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

// ========================= Conflict Compilation Operation =========================
	private static ArrayList<Task> findAllConflict(ArrayList<Task> tasksOnDate, Task task) {
		ArrayList<Task> taskList = new ArrayList<Task>();
		ArrayList<Date> dateTimes = task.getDateTimes();
		if (dateTimes.get(3) == null) { //task only has start time
			for (Task t : tasksOnDate) {
				if (task.getId() == t.getId()) {
					continue;
				}
				if (t.getDateTimes().get(3) != null) { // t got time range
					if (task.getIntStartTime() == t.getIntEndTime()) {
						continue;
					}
					IntegerPair tTimeRange = new IntegerPair(t.getIntStartTime(),t.getIntEndTime());
					if (tTimeRange.inBetween(task.getIntStartTime())) {
						taskList.add(t);
					}
				} else { // t only got start time;
					if (t.getIntStartTime() == task.getIntStartTime()){
						taskList.add(t);
					}
				}
			}
		} else { // task has a time range
			IntegerPair taskTimeRange = new IntegerPair(task.getIntStartTime(),task.getIntEndTime());
			for (Task t : tasksOnDate) {
				if (task.getId() == t.getId()) {
					continue;
				}
				if (t.getDateTimes().get(3) != null) { // t got time range
					if (t.getIntStartTime() == task.getIntEndTime() 
							|| task.getIntStartTime() == t.getIntEndTime()) {
						continue;
					}
					if (taskTimeRange.inBetween(t.getIntStartTime())
							|| taskTimeRange.inBetween(t.getIntEndTime())) {
						taskList.add(t);
					}
				} else { // t only got start time
					if (t.getIntStartTime() == task.getIntEndTime()) {
						continue;
					}
					if (taskTimeRange.inBetween(t.getIntStartTime())){
						taskList.add(t);
					}
				}
			}
		}
		if (!taskList.isEmpty()) {
			if (!taskList.contains(task)) {
				taskList.add(task);
			}
		}
		return taskList;
	}
	
	private static ArrayList<Task> findConflictByTask(Task task) { //for commands
		ArrayList<Task> tasks = Storage.getListOfUncompletedTasks();
		ArrayList<Integer> dates = getTaskDates(task);
		ArrayList<Task> totalConflict = new ArrayList<Task>();
		for (int date : dates) {
			ArrayList<Task> tasksOnDate = filterTasksByDate(tasks, date);
			ArrayList<Task> taskList = findAllConflict(tasksOnDate, task);
			for (Task t : taskList) {
				if (!totalConflict.contains(t)) {
					totalConflict.add(t);
				}
			}
		}
		return totalConflict;
	}
	
// ========================= Variable Initialisation Methods =========================
	private static HashMap<Integer, ArrayList<Integer>> initTimeSlot() {
		HashMap<Integer, ArrayList<Integer>> timeSlots = 
				new HashMap<Integer, ArrayList<Integer>>(24);
		for (int i = 0; i < 24; i++) {
			ArrayList<Integer> mins = new ArrayList<Integer>();
			for (int j = 0; j < 60; j++){ 
				mins.add(j);
			}
			timeSlots.put(i, mins);
		}
		return timeSlots;
	}
	
// ========================= Format Conversion Methods =========================
	private static ArrayList<String> convertToArrayListString(ArrayList<IntegerPair> aip) {
		//System.out.println(aip.isEmpty()==true);
		ArrayList<String> as = new ArrayList<String>();
		for (IntegerPair ip : aip) {
			as.add(toTimeString(ip.getInt1(), ip.getInt2()));
		}
		return as;
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
	
//========================= Filter Methods =================================
	private static ArrayList<Task> filterTaskSlots(ArrayList<Task> tasks, int input) {
		ArrayList<Task> taskSlots = new ArrayList<Task>();
		ArrayList<Task> tasksDate = filterTasksByDate(tasks, input);
		for (Task t : tasksDate) {
			if (t.getDateTimes().get(3) != null) { // no time range ignore
				taskSlots.add(t);
			}
		}
		return taskSlots;
	}
	
	private static ArrayList<Task> filterTasksByDate(ArrayList<Task> tasks, int date) {
		ArrayList<Task> tasksOnDate = new ArrayList<Task>();
		for (Task t : tasks) {
			if (t.getDateTimes().get(2) == null) { // no time at all ignore
				continue;
			} else if (t.getIntDateEnd() != Keywords.NO_DATE) { // have range of dates
				if (date >= t.getIntDate() && date <= t.getIntDateEnd()) {
					tasksOnDate.add(t);
				} else {
					continue;
				}
			} else { // start date only
				if (t.getIntDate() == date && t.getIsCompleted() != Keywords.TASK_COMPLETED) {
					tasksOnDate.add(t);
				}
			}
		}
		return tasksOnDate;
	}

	private static ArrayList<Integer> getTaskDates(Task task) {
		ArrayList<Integer> dates = new ArrayList<Integer>();
		int startD = task.getIntDate();
		int endD = task.getIntDateEnd();
		for (int i = startD; i <= endD; i++) {
			dates.add(i);
		}
		return dates;
	}

	private static ArrayList<Integer> getAllDatesWithTime(ArrayList<Task> tasks) {
		ArrayList<Integer> dates = new ArrayList<Integer>();
		for (Task t : tasks) {
			ArrayList<Date> dateTimes = t.getDateTimes();
			if (dateTimes.get(2) != null) {
				if (dateTimes.get(1) != null) {
					int tStart = t.getIntDate();
					int tEnd = t.getIntDateEnd();
					for (int i = tStart; i <= tEnd; i++) {
						if (!dates.contains(i)) {
							dates.add(i);
						}
					}
				} else {
					int tStart = t.getIntDate();
					if (!dates.contains(tStart)) {
						dates.add(tStart);
					}
				}
			}
		}
		return dates;
	}
}
