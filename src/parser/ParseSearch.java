package parser;

import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import logic.Logic;
import logic.Notification;
import shared.Keywords;
import shared.Task;

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

		System.out.println(rawInput);
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
		// need to check for invalid input
		Date date = Formatter.getDateFromString(rawInput);
		int dateStart = -1;
		if (date != null) {
			dateStart = Formatter.fromDateToInt(date);
		}
		// int dateEnd =
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
