//@@author A0135778N
/**
 * This class computes all the free time slots for each day as well as
 * checks for which tasks have conflicting time slots.
 */

package logic;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import shared.IntegerPair;
import shared.Keywords;
import shared.Task;
import storage.Storage;

public class FreeSlots {
	
// ============== Main Methods For Retrieving Free Time Slots =================
	/**
	 * This method is used for getting a list of free slots in the
	 * IntegerPair format.
	 * 
	 * @param input
	 */
	public static ArrayList<IntegerPair> getFreeSlotsInt(int input) {
		return compileFreeSlots(input);
	}

	/**
	 * This method is used for getting a list of free slots in the
	 * String format. E.g. "12:00pm to 4:00pm". Used by searchTask
	 * to display the free time slots to user.
	 * 
	 * @param input
	 */
	public static ArrayList<String> getFreeSlots(int input) {
		return convertToArrayListString(compileFreeSlots(input));
	}
	
//============== Main Methods For Retrieving Conflicting Tasks ===============
	/**
	 * This method is only used by the GUI component, SearchTask and
	 * ViewTask classes, for highlighting and viewing conflicting tasks
	 * respectively.
	 * 
	 * Other methods such as AddTask, EditTask, will use
	 * the getConflictIDs() method below for setting Notification messages.
	 * 
	 * @return totalConflict	A list of all tasks that have conflicting
	 * 							time slots.
	 */
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

	/**
	 * This method is used by AddTask, EditTask classes 
	 * for setting Notification messages.
	 * 
	 * @param task		The task object being added or edited.
	 * @return taskIDs	A list of taskIDs of conflicting tasks.
	 */
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
			updateTimeSlots(taskSlots, timeSlots);
			freeSlots = findFreeSlots(timeSlots);
			return freeSlots;
		}
	}

	private static ArrayList<IntegerPair> findFreeSlots(HashMap<Integer, ArrayList<Integer>> timeSlots) {
		ArrayList<IntegerPair> freeSlots = new ArrayList<IntegerPair>();
		int startTRange = 0;
		int endTRange = 0;
		boolean started = false;
		for (int key = 0; key < Keywords.CONSTANT_HOURS; key++) {
			int totalMinSize = timeSlots.get(key).size();
			if (totalMinSize == 1){
				if (started) {
					endTRange = key * Keywords.DATE_FORMAT_MULTIPLIER;
					freeSlots.add(new IntegerPair(startTRange, endTRange));
					startTRange = (key + 1) * Keywords.DATE_FORMAT_MULTIPLIER;
					endTRange = (key + 1) * Keywords.DATE_FORMAT_MULTIPLIER;
					started = false;
				}
				continue;
			}
			if (started) {
				endTRange = key * Keywords.DATE_FORMAT_MULTIPLIER;
				if (totalMinSize == Keywords.CONSTANT_MIN) {
					if (key == Keywords.CONSTANT_LAST_HOUR) {
						endTRange = (key * Keywords.DATE_FORMAT_MULTIPLIER)
									+ Keywords.CONSTANT_LAST_MIN;
						freeSlots.add(new IntegerPair(startTRange, endTRange));
					}
					continue;
				} else {
					if (timeSlots.get(key).get(Keywords.FIRST_ELEMENT) != 0) {
						freeSlots.add(new IntegerPair(startTRange, endTRange));
						startTRange = (key * Keywords.DATE_FORMAT_MULTIPLIER) 
								+ timeSlots.get(key).get(Keywords.FIRST_ELEMENT);
					}
					for (int i = 0; i < totalMinSize-1; i++) {
						if (timeSlots.get(key).get(i)+1 != timeSlots.get(key).get(i+1)) {
							endTRange = (key * Keywords.DATE_FORMAT_MULTIPLIER)
										+ timeSlots.get(key).get(i);
							freeSlots.add(new IntegerPair(startTRange, endTRange+1));
							startTRange = (key * Keywords.DATE_FORMAT_MULTIPLIER) 
										  + timeSlots.get(key).get(i+1);
							endTRange = (key * Keywords.DATE_FORMAT_MULTIPLIER) 
										+ timeSlots.get(key).get(i+1);
							started = false;
							continue;
						}
					}
					if (started) {
						endTRange = (key * Keywords.DATE_FORMAT_MULTIPLIER)
									+ timeSlots.get(key).get(totalMinSize-1);
						freeSlots.add(new IntegerPair(startTRange, endTRange+1));
						started = false;
					} else {
						started = true;
					}
				}
			} else {
				if (totalMinSize == Keywords.CONSTANT_MIN) {
					started = true;
					startTRange = key * Keywords.DATE_FORMAT_MULTIPLIER;
					endTRange = key * Keywords.DATE_FORMAT_MULTIPLIER;
					continue;
				} else {
					startTRange = (key * Keywords.DATE_FORMAT_MULTIPLIER)
								  + timeSlots.get(key).get(Keywords.FIRST_ELEMENT);
					endTRange = (key * Keywords.DATE_FORMAT_MULTIPLIER)
								+ timeSlots.get(key).get(Keywords.FIRST_ELEMENT);
					for (int i = 0; i < totalMinSize-1; i++) {
						if (timeSlots.get(key).get(i)+1 != timeSlots.get(key).get(i+1)) {
							freeSlots.add(new IntegerPair(startTRange, endTRange));
							startTRange = (key * Keywords.DATE_FORMAT_MULTIPLIER)
										  + timeSlots.get(key).get(i+1);
							endTRange = (key * Keywords.DATE_FORMAT_MULTIPLIER)
										+ timeSlots.get(key).get(i+1);
							continue;
						}
						endTRange = (key * Keywords.DATE_FORMAT_MULTIPLIER)
									+ timeSlots.get(key).get(i+1);
					}
					started = true;
				}
			}
		}
		return freeSlots;
	}
	
	/**
	 * This method will update the time slots by removing those time ranges
	 * that have been occupied by a tasks.
	 * 
	 * @param taskSlots		A list of tasks on a particular date that has a
	 * 						time range.
	 * @param timeSlots
	 */
	private static void updateTimeSlots(ArrayList<Task> taskSlots,
			HashMap<Integer, ArrayList<Integer>> timeSlots) {
		for (Task t : taskSlots) {
			ArrayList<Date> dateTimes = t.getDateTimes();
			Date startT = dateTimes.get(Keywords.INDEX_STARTTIME);
			Date endT = dateTimes.get(Keywords.INDEX_ENDTIME);
			update(timeSlots, startT, endT);
		}
	}

	private static void update(HashMap<Integer, ArrayList<Integer>> timeSlots, Date startT, Date endT) {
		Calendar calStart = Calendar.getInstance();
		Calendar calEnd = Calendar.getInstance();
		calStart.setTime(startT); 
		calEnd.setTime(endT);
		int sHour = calStart.get(Calendar.HOUR_OF_DAY);
		int sMin = calStart.get(Calendar.MINUTE);
		int eHour = calEnd.get(Calendar.HOUR_OF_DAY);
		int eMin = calEnd.get(Calendar.MINUTE);
		
		for (int i = sHour; i <= eHour; i++) {
			ArrayList<Integer> mins = timeSlots.get(i);
			if (sHour == eHour) {
				// if the time blocked is 4.30pm-4.45pm
				ArrayList<Integer> temp = new ArrayList<Integer>(mins);
				removeMins(temp, sMin, eMin);
				timeSlots.replace(i, temp);
				break;
			}
			if (i == sHour){
				if (sMin == 0) {
					mins.clear();
					// if list only got 0 then whole hour blocked, but
					// can still stop at that hour. E.g. 4pm-5pm blocked,
					// but can still have a task ending at 4pm or starting
					// at 5pm.
					mins.add(0);
					timeSlots.replace(i, mins);
				} else {
					mins = new ArrayList<Integer>(mins.subList(0, sMin));
					timeSlots.replace(i, mins);
				}
			} else if (i == eHour) {
				if (eMin == 0) {
					break;
				}
				mins = new ArrayList<Integer>(mins.subList(eMin, mins.size()));
				timeSlots.replace(i, mins);
			} else {
				mins.clear();
				mins.add(0);
				timeSlots.replace(i, mins);
			}
		}
	}
	
	private static void removeMins(ArrayList<Integer> temp, int sMin, int eMin) {
		for (int i = sMin; i < eMin; i++) {
			temp.remove(new Integer(i));
		}
	}

	// ========================= Conflict Compilation Operation =========================
	/**
	 * This method finds all the conflicting tasks with the given task,
	 * on a particular date.
	 * 
	 * @param tasksOnDate	List of tasks with at least a start time.
	 * @param task
	 * @return				List of conflicting tasks if any.
	 */
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
	
	/**
	 * This method takes in a task and finds for other tasks that conflicts with
	 * its time slot.
	 * 
	 * @param task
	 * @return totalConflict	List of tasks that conflicts with the given task.
	 */
	private static ArrayList<Task> findConflictByTask(Task task) {
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
	/**
	 * This method creates a HashMap with keys representing the hour values
	 * in the 24-hour format. Each key is assigned an ArrayList of Integers
	 * which represents the minutes in an hour.
	 */
	private static HashMap<Integer, ArrayList<Integer>> initTimeSlot() {
		HashMap<Integer, ArrayList<Integer>> timeSlots = 
				new HashMap<Integer, ArrayList<Integer>>(24);
		for (int i = 0; i < Keywords.CONSTANT_HOURS; i++) {
			ArrayList<Integer> mins = new ArrayList<Integer>();
			for (int j = 0; j < Keywords.CONSTANT_MIN; j++){ 
				mins.add(j);
			}
			timeSlots.put(i, mins);
		}
		return timeSlots;
	}
	
// ========================= Format Conversion Methods =========================
	private static ArrayList<String> convertToArrayListString(ArrayList<IntegerPair> aip) {
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
	/**
	 * This method filters out all the tasks with a time range
	 * in the given date(input).
	 * 
	 * @param tasks		List of uncompleted tasks.
	 * @param input		Date in integer format.
	 * @return			List of tasks on given date with a time range.
	 */
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

	/**
	 * This method gets all the dates the task is allocated to.
	 */
	private static ArrayList<Integer> getTaskDates(Task task) {
		ArrayList<Integer> dates = new ArrayList<Integer>();
		int startD = task.getIntDate();
		int endD = task.getIntDateEnd();
		for (int i = startD; i <= endD; i++) {
			dates.add(i);
		}
		return dates;
	}
	
	/**
	 * This method finds all the dates with tasks that have a time range.
	 * 
	 * @param tasks		List of uncompleted tasks.
	 * @return dates	List of dates with tasks that have a time range.
	 */
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
