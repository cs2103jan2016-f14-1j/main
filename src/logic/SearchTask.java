//@@author A0076520L

package logic;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import parser.Formatter;
import shared.Keywords;
import shared.Task;
import storage.Storage;

public class SearchTask extends Functionality {

	private ArrayList<String> replace;

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
			ArrayList<String> categories) {
		replace = new ArrayList<String>();
		replace.add("Do you mean:");
		HashMap<String, Object> results = new HashMap<String, Object>();
		ArrayList<Task> result = new ArrayList<Task>();

		// Check if user wants to search for prioritise, no prioritise or get
		// all uncompleted
		if (isPriortise == 1 || isPriortise == 0) {
			result = filterPriority(Storage.getListOfUncompletedTasks(), isPriortise);
		} else {
			result = Storage.getListOfUncompletedTasks();
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
		}

		// search by month
		if (!month.equals(Keywords.EMPTY_STRING)) {
			result = filterByMonth(result, month);
		}

		// search by categories
		if (!categories.isEmpty()) {
			result = filterCategories(result, categories);
		}
		// Lastly, after all the filtering, search for words containing if any
		if (!words.equals("")) {
			result = filterWords(result, words);
		}
		if (result.size() == 0) {
			setNTitle("Search Success!");
			setNMessage("No results found!");
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
				ArrayList<String> result = SymSpell.Correct(word, "");
				if (t.getTask().contains(word)) {
					temp = checkTask(temp, t);
					break;
				}
				if (t.getCategories().contains(word)) {
					temp = checkTask(temp, t);
					break;
				}
				for (String wor : result) {
					if (t.getTask().contains(wor)) {
						addWordToReplace(word, wor);
						temp = checkTask(temp, t);
						break;
					} else if (!t.getCategories().isEmpty()) {
						if (t.getCategories().contains(wor)) {
							addWordToReplace(word, wor);
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
	 * Check if word in dictionary match
	 * 
	 * @param toReplace
	 * @param toCheck
	 */
	private void addWordToReplace(String toReplace, String toCheck) {
		if (!toCheck.equals(toReplace) && !replace.contains(toCheck)) {
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
			if (t.getIntDate() == date && t.getIsCompleted() == Keywords.TASK_NOT_COMPLETED) {
				temp.add(t);
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
		for (Task t : list) {
			String intDate = Integer.toString(t.getIntDate());
			Calendar dateMth = Calendar.getInstance();
			Calendar dateOfTask = Calendar.getInstance();
			Date mth = null;
			try {
				mth = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(month);
				dateMth.setTime(mth);
				dateOfTask.setTime(Formatter.fromIntToDate(intDate));
			} catch (Exception e) {
				setNMessage("Wrong date format. Use Feb, may, Jan.");
			}
			if (dateOfTask != null && dateMth !=null) {
				if (dateOfTask.get(Calendar.MONTH) == dateMth.get(Calendar.MONTH)) {
					temp.add(t);
				}
			}
		}
		return temp;
	}

}
