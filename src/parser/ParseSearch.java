//@@author A0076520L

package parser;

import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import shared.Keywords;

public class ParseSearch {
	public static ArrayList<Object> filterInput(String rawInput) {
		ArrayList<Object> output = new ArrayList<Object>();
		// filter for words
		Pattern p = Pattern.compile("\"([^\"]*)\"");
		Matcher m = p.matcher(rawInput);
		if (!m.find()) {
			// if it cannot find anything with "", add an empty string
			output.add("");
		} else {
			output.add(m.group(1));
		}
		// remove the words to be searched
		rawInput = removeString("\"([^\"]*)\"", rawInput);

		// check for user asking for priority
		if (rawInput.contains("no priority")) {
			rawInput = removeString("no priority", rawInput);
			output.add(0);
		} else if (rawInput.contains("priority")) {
			rawInput = removeString("priority", rawInput);
			output.add(1);
		} else {
			// user wants both priority and no priority
			output.add(-1);
		}

		// filter for dates

		// check if it is by month only
		String checkMth = "(?:from|in)?\\s?(?i)(jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec|january|february|march|april|may|june|july|august|september|october|november|december)";
		p = Pattern.compile(checkMth);
		m = p.matcher(rawInput);
		if (m.find()) {
			output.add(m.group(1));
			rawInput = rawInput.replaceAll(checkMth, "");
		}else{
			output.add(Keywords.EMPTY_STRING);
		}
		
		// need to check for invalid input
		Date date = Formatter.getDateFromString(rawInput);
		int dateStart = -1;
		if (date != null) {
			dateStart = Formatter.fromDateToInt(date);
			System.out.println(date.toString()+" "+dateStart);
		}

		// Formatter.fromDateToInt(dates.get(Keywords.INDEX_ENDDATE));
		output.add(dateStart);
		// filter for categories
		p = Pattern.compile("(\\#[^\\s]+)");
		m = p.matcher(rawInput);
		boolean hasCategory = m.find();
		if (!hasCategory) {
			output.add(new ArrayList<String>());
		} else {
			ArrayList<String> list = new ArrayList<String>();
			list.add(m.group(1).replace((Keywords.CATEGORY_PREPEND), ""));
			while (m.find()) {
				list.add(m.group(1).replace((Keywords.CATEGORY_PREPEND), ""));
			}
			output.add(list);
		}
		
		return output;
	}

	private static String removeString(String toR, String input) {
		return input.replaceAll(toR, "");
	}
}
