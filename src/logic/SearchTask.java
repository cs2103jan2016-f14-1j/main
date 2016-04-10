//@@author A0076520L

package logic;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.ocpsoft.prettytime.shade.edu.emory.mathcs.backport.java.util.Collections;
import org.ocpsoft.prettytime.shade.org.apache.commons.lang.WordUtils;

import parser.Formatter;
import shared.Keywords;
import shared.Task;
import storage.Storage;

public class SearchTask extends Functionality {

	private ArrayList<String> replace;
	private HashMap<Integer, Integer> busiest;

	/**
	 * Search the tasks accordingly to the attributes given
	 * 
	 * @param words
	 *            the words to be filtered
	 * @param isPriortise
	 *            1 for priortise, 0 for not priortise
	 * @param month
	 *            the month to be searched
	 * @param date
	 *            the specific date to be searched
	 * @param categories
	 *            the categories to be filtered
	 * @return the results of the filtering
	 */
	public HashMap<String, Object> searchTask(String words, int isPriortise, String month, int date,
			ArrayList<String> categories, int isBusiest, String conflict) {
		replace = new ArrayList<String>();
		busiest = new HashMap<Integer, Integer>();
		replace.add("Do you mean:");
		HashMap<String, Object> results = new HashMap<String, Object>();
		ArrayList<Task> result = new ArrayList<Task>();
		int isCheckPerformed = 0;

		// search for conflicts
		if (!conflict.equals(Keywords.EMPTY_STRING)) {
			result = FreeSlots.findConflict();
			isCheckPerformed++;
		} else {
			result = Storage.getListOfUncompletedTasks();
		}

		// Check if user wants to search for prioritise, no prioritise or get
		// all uncompleted
		if (isPriortise == 1 || isPriortise == 0) {
			result = filterPriority(result, isPriortise);
			isCheckPerformed++;
		}

		// Compare the dates
		if (date != -1) {
			// search <result> comparing dates
			result = filterDate(result, date);
			// get free time slots
			ArrayList<String> freeSlots = FreeSlots.getFreeSlots(date);
			if (freeSlots.isEmpty()) {
				freeSlots.add("Whole day is free");
			}
			results.put("free", freeSlots);
			isCheckPerformed++;
		}

		// search by month
		if (!month.equals(Keywords.EMPTY_STRING)) {
			result = filterByMonth(result, month);
			isCheckPerformed++;
			if (isBusiest == Keywords.YES) {// check if user wants to search for
											// busiest
				int max = (int) Collections.max(busiest.values());// find the
																	// highest
																	// value
				ArrayList<String> busyDays = new ArrayList<String>();
				busyDays.add(" in " + WordUtils.capitalizeFully(month) + " with " + max + " task(s) in the day(s).");
				for (Map.Entry<Integer, Integer> e : busiest.entrySet()) {
					if (max == e.getValue()) {// find the days with the highest
												// value
						if (e.getKey() == 1) {
							busyDays.add("On the " + e.getKey() + "st");
						} else if (e.getKey() == 2) {
							busyDays.add("On the " + e.getKey() + "nd");
						} else if (e.getKey() == 3) {
							busyDays.add("On the " + e.getKey() + "rd");
						} else {
							busyDays.add("On the " + e.getKey() + "th");
						}
					}
				}
				results.put("busiest", busyDays);
			}
		}

		// search by categories
		if (!categories.isEmpty()) {
			result = filterCategories(result, categories);
			isCheckPerformed++;
		}

		// Lastly, after all the filtering, search for words containing if any
		if (!words.equals("")) {
			result = filterWords(result, words);
			isCheckPerformed++;
		}
		if (result.size() == 0) {
			setNTitle("Search Success!");
			setNMessage("No results found!");
		} else if (isCheckPerformed == 0) {// isCheckPerformed = 0 would mean
											// user's input is wrong
			result.clear();
			setNTitle("Search Error!");
			setNMessage("Your input format is wrong!");
		} else {
			setNTitle("Search Success!");
			setNMessage("Results found: " + result.size());
		}

		// combines all of it into results and return to caller
		ArrayList<Object> combined = new ArrayList<Object>();
		combined.add(getNotification());
		results.put("Tasks", result);
		results.put("notification", getNotification());
		results.put("replace", replace);
		combined.add(result);
		return results;
	}

	/**
	 * Filter by key words
	 * 
	 * @param list
	 *            the list of tasks to be filtered
	 * @param words
	 *            the words to filter
	 * @return the list of filtered tasks
	 */
	private ArrayList<Task> filterWords(ArrayList<Task> list, String words) {
		ArrayList<Task> temp = new ArrayList<Task>();
		InputStream is = getClass().getResourceAsStream("/storage/dictionary");
		SymSpell.CreateDictionary(is, Keywords.EMPTY_STRING);
		for (Task t : list) {
			for (String word : words.split(Keywords.SPACE_STRING)) {
				ArrayList<String> result = SymSpell.Correct(word, Keywords.EMPTY_STRING);
				if (t.getTask().contains(word)) {// check if task contains the
													// word itself
					temp = checkTask(temp, t);
					break;
				}
				if (t.getCategories().contains(word)) {// check if task's
														// categories contains
														// the word itself
					temp = checkTask(temp, t);
					break;
				}
				for (String replacement : result) {// check if any replacement
													// words found
					if (t.getTask().contains(replacement)) {// check if it
															// contains the
															// replacement word
						addWordToReplace(word, replacement);
						temp = checkTask(temp, t);
						break;
					} else if (!t.getCategories().isEmpty()) {// check if its
																// categories
																// contains the
																// replacement
																// word
						if (t.getCategories().contains(replacement)) {
							addWordToReplace(word, replacement);
							temp = checkTask(temp, t);
							break;
						}
					}
				}
			}
		}
		return temp;
	}

	/**
	 * Check if task is exists in list
	 * 
	 * @param tasks
	 *            list of tasks
	 * @param task
	 *            task to check
	 * @return the list of tasks
	 */
	private ArrayList<Task> checkTask(ArrayList<Task> tasks, Task task) {
		if (!tasks.contains(task)) {
			tasks.add(task);
		}
		return tasks;
	}

	/**
	 * Check if word in dictionary match and if it already exists in the
	 * ArrayList
	 * 
	 * @param toReplace
	 * @param toCheck
	 */
	private void addWordToReplace(String toReplace, String toCheck) {
		if (!toCheck.equals(toReplace) && !replace.contains(toCheck)) {
			// if it is not in ArrayList, add it in, else ignore
			replace.add(toCheck);
		}
	}

	/**
	 * Filter the list of tasks by date
	 * 
	 * @param list
	 *            the list of tasks to be filtered
	 * @param date
	 *            the date to filter
	 * @return the filtered list
	 */
	private ArrayList<Task> filterDate(ArrayList<Task> list, int date) {
		ArrayList<Task> temp = new ArrayList<Task>();
		for (Task t : list) {
			boolean isTaskCompleted = t.getIsCompleted() == Keywords.TASK_NOT_COMPLETED;
			if (t.getIntDate() == date && isTaskCompleted) {
				// checks the start date
				temp.add(t);
			} else if (t.getIntDateEnd() == date && isTaskCompleted) {
				// checks end date
				temp.add(t);
			} else {
				// check date range
				int start = t.getIntDate();
				int end = t.getIntDateEnd();
				while (start <= end && end != 9999) {
					if (start == date && isTaskCompleted) {
						temp.add(t);
						break;
					}
					start++;
				}
			}
		}
		return temp;
	}

	/**
	 * Filter the list of tasks by priority
	 * 
	 * @param list
	 *            the list of tasks to be filtered
	 * @param isPriortise
	 *            the priority
	 * @return the list of filtered tasks
	 */
	private ArrayList<Task> filterPriority(ArrayList<Task> list, int isPriortise) {
		ArrayList<Task> temp = new ArrayList<Task>();
		for (Task t : list) {
			if (t.getPriority() == isPriortise && t.getIsCompleted() == Keywords.TASK_NOT_COMPLETED) {
				// checks for priority based on the isPrioritise variable
				temp.add(t);
			}
		}
		return temp;
	}

	/**
	 * Filter the list of tasks by categories
	 * 
	 * @param list
	 *            the list of tasks to be filtered
	 * @param catToFilter
	 *            the categories to filter
	 * @return the list of filtered tasks
	 */
	private ArrayList<Task> filterCategories(ArrayList<Task> list, ArrayList<String> catToFilter) {

		ArrayList<Task> temp = new ArrayList<Task>();
		for (Task t : list) {
			for (String cat : catToFilter) {
				if (t.getCategories().contains(cat)) {
					temp.add(t);
					break;
				}
			}
		}
		return temp;
	}

	/**
	 * Filter the list of tasks by month
	 * 
	 * @param list
	 *            the list of tasks to be filtered
	 * @param month
	 *            the month to filter
	 * @return the list of filtered tasks
	 */
	private ArrayList<Task> filterByMonth(ArrayList<Task> list, String month) {
		ArrayList<Task> temp = new ArrayList<Task>();
		busiest.clear();
		for (Task t : list) {
			// initialise all variables needed
			String intStartDate = Integer.toString(t.getIntDate());
			String intEndDate = Integer.toString(t.getIntDateEnd());
			Calendar startDateOfTask = null;
			Calendar endDateOfTask = null;
			Calendar dateMth = Calendar.getInstance();
			if (Formatter.fromIntToDate(intStartDate) != null) {// if start date
																// is found
				startDateOfTask = Calendar.getInstance();
				startDateOfTask.setTime(Formatter.fromIntToDate(intStartDate));
			}
			if (Formatter.fromIntToDate(intEndDate) != null) {// if end date is
																// found
				endDateOfTask = Calendar.getInstance();
				endDateOfTask.setTime(Formatter.fromIntToDate(intEndDate));
			}
			Date userMth = getUserMth(month);// check if the format of month is
												// correct
			if (userMth != null) {
				dateMth.setTime(userMth);
			}
			boolean isStart = (startDateOfTask != null && dateMth != null)
					? startDateOfTask.get(Calendar.MONTH) == dateMth.get(Calendar.MONTH) : false;
			boolean isEnd = (endDateOfTask != null && dateMth != null)
					? endDateOfTask.get(Calendar.MONTH) == dateMth.get(Calendar.MONTH) : false;
			if (isStart) {
				temp = checkTask(temp, t);
			}
			if (isEnd) {
				temp = checkTask(temp, t);
			}
			// filter busiest just in case if user needs it
			filterBusiest(startDateOfTask, endDateOfTask, dateMth.get(Calendar.MONTH));
		}
		return temp;
	}

	/**
	 * Get Date of the month of what user wants
	 * 
	 * @param month
	 *            the month to get
	 * @return the Date object
	 */
	private Date getUserMth(String month) {
		Date mth = null;
		try {
			mth = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(month);
		} catch (Exception e) {
			setNMessage("Wrong date format. Use Feb, may, Jan.");
		}
		return mth;
	}

	/**
	 * Tabulate the busiest HashMap for finding busiest day
	 * 
	 * @param start
	 *            the starting Calendar
	 * @param end
	 *            the ending Calendar
	 * @param userMth
	 *            the month user wants
	 */
	private void filterBusiest(Calendar start, Calendar end, int userMth) {
		boolean isStart = (start != null) ? start.get(Calendar.MONTH) == userMth : false;
		boolean isEnd = (end != null) ? end.get(Calendar.MONTH) == userMth : false;

		if (start != null && end != null) {
			if (start.get(Calendar.MONTH) == end.get(Calendar.MONTH) && isStart) {
				int day = start.get(Calendar.DAY_OF_MONTH);
				while (day <= end.get(Calendar.DAY_OF_MONTH)) {
					addToBusiest(day);
					start.add(Calendar.DAY_OF_MONTH, 1);
					day = start.get(Calendar.DAY_OF_MONTH);
				}

			} else if (isStart || isEnd) {
				Calendar toUse = Calendar.getInstance();
				toUse.setTime((isEnd) ? end.getTime() : start.getTime());
				boolean toSameEnd = toUse.get(Calendar.MONTH) == end.get(Calendar.MONTH);
				boolean toSameStart = toUse.get(Calendar.MONTH) == start.get(Calendar.MONTH);
				if (toSameEnd) {
					int day = toUse.get(Calendar.DAY_OF_MONTH);

					while (toSameEnd) {
						addToBusiest(day);
						toUse.add(Calendar.DAY_OF_MONTH, -1);
						day = toUse.get(Calendar.DAY_OF_MONTH);
						toSameEnd = toUse.get(Calendar.MONTH) == end.get(Calendar.MONTH);
					}
				} else {
					int day = toUse.get(Calendar.DAY_OF_MONTH);

					while (toSameStart) {
						addToBusiest(day);
						toUse.add(Calendar.DAY_OF_MONTH, 1);
						day = toUse.get(Calendar.DAY_OF_MONTH);
						toSameStart = toUse.get(Calendar.MONTH) == start.get(Calendar.MONTH);
					}
				}
			}
		} else if (start != null) {
			if (isStart) {
				int day = start.get(Calendar.DAY_OF_MONTH);
				addToBusiest(day);
			}
		}
	}

	/**
	 * Add to the busiest HashMap
	 * 
	 * @param day
	 *            the day to add to
	 */
	private void addToBusiest(int day) {
		if (busiest.get(day) != null) {
			busiest.put(day, busiest.get(day) + 1);
		} else {
			busiest.put(day, 1);
		}
	}

}
