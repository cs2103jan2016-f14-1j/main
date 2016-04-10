//@@author A0076520L

package parser;

import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import shared.Keywords;

public class ParseSearch {

	private static final String NO_PRIORITY = "no priority";
	private static final String PRIORITY = "priority";
	private static final String REGEX_CHECKMTH = "(?:from|in)\\s(?i)"
			+ "(jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec|" 
			+ "january|february|march|april|may|june|july|august|"
			+ "september|october|november|december)";
	private static final String REGEX_DESC = "\"([^\"]*)\"";
	private static final String REGEX_CAT = "(\\#[^\\s]+)";

	/**
	 * Interpret the user's input
	 * 
	 * @param rawInput
	 * @return
	 */
	public static ArrayList<Object> filterInput(String rawInput) {
		ArrayList<Object> output = new ArrayList<Object>();
		// filter for words
		Pattern p = Pattern.compile(REGEX_DESC);
		Matcher m = p.matcher(rawInput);
		if (!m.find()) {
			// if it cannot find anything with "", add an empty string
			output.add("");
		} else {
			output.add(m.group(1));
		}
		// remove the words to be searched
		rawInput = removeString(REGEX_DESC, rawInput);

		// check for user asking for priority
		if (rawInput.contains(NO_PRIORITY)) {
			rawInput = removeString(NO_PRIORITY, rawInput);
			output.add(0);
		} else if (rawInput.contains(PRIORITY)) {
			rawInput = removeString(PRIORITY, rawInput);
			output.add(1);
		} else {
			// user wants both priority and no priority
			output.add(-1);
		}

		// filter for dates
		// check if it is by month only
		p = Pattern.compile(REGEX_CHECKMTH);
		m = p.matcher(rawInput);
		if (m.find()) {
			output.add(m.group(1));
			rawInput = removeString(REGEX_CHECKMTH, rawInput);
		} else {
			output.add(Keywords.EMPTY_STRING);
		}

		// check if there is any dates
		Date date = Formatter.getDateFromString(rawInput);
		int dateStart = -1;
		if (date != null) {
			dateStart = Formatter.fromDateToInt(date);
			System.out.println(date.toString() + Keywords.SPACE_STRING + dateStart);
		}
		output.add(dateStart);
		// filter for categories
		p = Pattern.compile(REGEX_CAT);
		m = p.matcher(rawInput);
		boolean hasCategory = m.find();
		if (!hasCategory) {
			output.add(new ArrayList<String>());
		} else {
			ArrayList<String> list = new ArrayList<String>();
			list.add(m.group(1).replace((Keywords.CATEGORY_PREPEND), Keywords.EMPTY_STRING));
			while (m.find()) {
				list.add(m.group(1).replace((Keywords.CATEGORY_PREPEND), Keywords.EMPTY_STRING));
			}
			output.add(list);
		}

		return output;
	}

	/**
	 * Remove matched regex from String
	 * 
	 * @param toR
	 *            Regex to be matched
	 * @param input
	 *            Input to be remove
	 * @return the input which the regex has been removed
	 */
	private static String removeString(String toR, String input) {
		return input.replaceAll(toR, Keywords.EMPTY_STRING);
	}
}
