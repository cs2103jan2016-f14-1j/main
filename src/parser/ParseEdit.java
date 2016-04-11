//@@author A0076520L

package parser;

import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ocpsoft.prettytime.shade.edu.emory.mathcs.backport.java.util.Arrays;

import logic.Logic;
import logic.Notification;
import shared.Keywords;

public class ParseEdit {

	private static int id;

	private static final String REGEX_TIME = "(((1[012]|[1-9])(:[0-5][0-9])?\\s?(?i)(am|pm))|([01]?[0-9]|2[0-3]):[0-5][0-9])";
	private static final String REGEX_DATE = "(0?[1-9]|[12][0-9]|3[01])\\s?"
			+ "(?i)(January|February|March|April|May|June|July|" + "August|September|October|November|December|"
			+ "Jan|Feb|Mar|Apr|Jun|Jul|Aug|Sep|Oct|Nov|Dec)";
	private static final String REGEX_CAT = "(\\#+[a-zA-Z0-9.\\;]+\\s*)+";
	private static final String REGEX_ID = "^\\d+";
	private static final String NO_DATE = "NO DATE";
	private static final String NO_TIME = "NO TIME";
	private static Pattern p;
	private static Matcher m;

	private static ArrayList<Date> datetimes;
	private static ArrayList<String> categories;
	private static int resetDate, resetTime;
	private static String userInput;

	/**
	 * Interpret and dissect the user's input
	 * 
	 * @param userInput
	 *            user Input
	 * @return the Notification object
	 */
	public static Notification editTask(String rawInput) {
		datetimes = new ArrayList<Date>();
		categories = new ArrayList<String>();
		p = null;
		m = null;
		resetDate = 0;
		resetTime = 0;
		userInput = rawInput;
		String taskName = Keywords.EMPTY_STRING;
		for (int i = 0; i < 4; i++) {
			datetimes.add(null);
		}
		// check if user wants reset date or time
		checkResetDateOrTime();
		// find categories
		findCategory();
		// find the ID from the input
		findID();
		// find dates
		findDates();
		// find times
		findTimes();
		// find task description
		taskName = userInput;

		return Logic.editTask(id, datetimes, taskName, categories, resetDate, resetTime);
	}

	/**
	 * Check if user is resetting Date or Time to null
	 */
	private static void checkResetDateOrTime() {
		if (userInput.contains(NO_DATE)) {
			resetDate = 1;
			userInput = userInput.replace(NO_DATE, Keywords.EMPTY_STRING);
		}

		if (userInput.contains(NO_TIME)) {
			resetTime = 1;
			userInput = userInput.replace(NO_TIME, Keywords.EMPTY_STRING);
		}
	}

	/**
	 * Find the categories in the user input
	 */
	private static void findCategory() {
		p = Pattern.compile(REGEX_CAT);
		m = p.matcher(userInput);
		while (m.find()) {
			ArrayList<String> cats = new ArrayList<String>(Arrays.asList(m.group().split(Keywords.SPACE_STRING)));
			for (String cat : cats) {
				categories.add(cat.replace("#", Keywords.EMPTY_STRING));
			}
			userInput = userInput.replaceAll(REGEX_CAT, Keywords.EMPTY_STRING);
		}
	}

	/**
	 * Find the ID in the user input
	 */
	private static void findID() {
		String result = find(REGEX_ID, userInput);
		if (result == null) {
			id = -1;
		} else {
			id = Integer.parseInt(result);
			userInput = userInput.replaceAll(REGEX_ID + "(?:\\sto\\s)?", Keywords.EMPTY_STRING);
		}
	}

	/**
	 * Find the dates in the user input
	 */
	private static void findDates() {
		String startDate = "(?<=from|on|by|at)\\s" + REGEX_DATE;
		String result = find(startDate, userInput);
		if (result != null) {// may have multiple dates
			datetimes.set(Keywords.INDEX_STARTDATE, Formatter.getDateFromString(result));
			userInput = userInput.replaceAll("(from|on|by|at)\\s" + REGEX_DATE + "\\s?", Keywords.EMPTY_STRING);
			findEndDate();
		} else {// single date or no date
			startDate = "^" + REGEX_DATE;
			result = find(startDate, userInput);
			if (result != null) {
				datetimes.set(Keywords.INDEX_STARTDATE, Formatter.getDateFromString(result));
				userInput = userInput.replaceAll(startDate + "\\s?", Keywords.EMPTY_STRING);
				findEndDate();
			}
		}
	}

	/**
	 * Find the times in the user input
	 */
	private static void findTimes() {
		String startTime = "(?<=from|on|by|at)\\s" + REGEX_TIME;
		String result = find(startTime, userInput);
		if (result != null) {// may have multiple times
			datetimes.set(Keywords.INDEX_STARTTIME, Formatter.getDateFromString(result));
			userInput = userInput.replaceAll("(from|on|by|at)\\s" + REGEX_TIME + "\\s?", Keywords.EMPTY_STRING);
			findEndTime();
		} else {// may have a single start time
			startTime = "^" + REGEX_TIME;
			result = find(startTime, userInput);
			if (result != null) {
				datetimes.set(Keywords.INDEX_STARTTIME, Formatter.getDateFromString(result));
				userInput = userInput.replaceAll(startTime + "\\s?", Keywords.EMPTY_STRING);
				findEndTime();
			}
		}
	}

	/**
	 * Find the end date of the input
	 */
	private static void findEndDate() {
		String endDate = "(?<=to)\\s" + REGEX_DATE;
		String result = find(endDate, userInput);
		if (result != null) {// may have end date
			datetimes.set(Keywords.INDEX_ENDDATE, Formatter.getDateFromString(result));
			userInput = userInput.replaceAll("to\\s" + REGEX_DATE + "\\s?", Keywords.EMPTY_STRING);
		}
	}

	/**
	 * Find the end time of the input
	 */
	private static void findEndTime() {
		String endTime = "(?<=to)\\s" + REGEX_TIME;
		String result = find(endTime, userInput);
		if (result != null) {// may have a end time
			datetimes.set(Keywords.INDEX_ENDTIME, Formatter.getDateFromString(result));
			userInput = userInput.replaceAll("to\\s" + REGEX_TIME + "\\s?", Keywords.EMPTY_STRING);
		}
	}

	/**
	 * Find if it matches the regex of the input
	 * 
	 * @param regex
	 *            the regex to be matched
	 * @param input
	 *            the input to be matched with
	 * @return the finding of the result
	 */
	private static String find(String regex, String input) {
		p = Pattern.compile(regex);
		m = p.matcher(input);
		if (m.find()) {
			return m.group();
		} else {
			return null;
		}
	}
}
