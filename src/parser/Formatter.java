package parser;

import shared.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Formatter {
	
	public static ArrayList<String> breakString(String rawInput) {
		try {
			return new ArrayList<String>(Arrays.asList(rawInput.split(Keywords.SPACE_STRING)));
		} catch (IndexOutOfBoundsException e) {
			ArrayList<String> empty = new ArrayList<String>();
			empty.add(Keywords.EMPTY_STRING);
			return empty;
		}
	}
	
	/**
	 * @param cats:
	 *            will add category(s) to this ArrayList if they exist
	 * @param parts
	 * @return true if >= 1 category exist, false otherwise
	 */
	public static boolean getCategories(ArrayList<String> cats, ArrayList<String> parts) {
		boolean hasFound = false;
		for (String s : parts) {
			if (isCategory(s)) {
				cats.add(s.substring(1));
				hasFound = true;
			}
		}

		return hasFound;
	}

	/**
	 * @param prep:
	 *            will add preposition to this ArrayList if it exists
	 * @param parts
	 * @return true if preposition exists, false otherwise
	 */
	public static boolean getPreposition(ArrayList<String> prep, ArrayList<String> parts) {
		for (String s : parts) {
			if (isPreposition(s)) {
				prep.add(s);
				return true;
			}
		}
		return false;
	}
	
	public static String getTaskName(String rawInput) {
		try {
			return rawInput.split(Keywords.SPACE_STRING, 2)[Keywords.FIRST_ELEMENT].trim();
		} catch (ArrayIndexOutOfBoundsException e) {
			return Keywords.EMPTY_STRING;
		}
	}
	
	private static boolean isCategory(String s) {
		return s.startsWith(Keywords.CATEGORY_PREPEND);
	}

	private static boolean isPreposition(String s) {
		return s.matches(Keywords.REGEX_PREPOSITIONS);
	}
	
	/**
	 * converts date <DDMMM | DMMM> to int representing a unique date 
	 * assumption: String is valid format [d]dmmm where mmm is legit month and [d]d is legit day
	 * if empty string, return NO_DATE
	 */
	public static int fromDDMMMToInt(String date) {
		if (date.equals(Keywords.EMPTY_STRING)) {
			return Keywords.NO_DATE;
		}
		int len = date.length();
		String day = date.substring(0, len - 3);
		String month = date.substring(len - 3, len);
		int result = Integer.parseInt(day) + (convertMonthToInt(month) * 32);
		return result;
	}
	private static int convertMonthToInt(String month) {
		if (month.equalsIgnoreCase("jan")) {
			return 1;
		} else if (month.equalsIgnoreCase("feb")) {
			return 2;
		} else if (month.equalsIgnoreCase("mar")) {
			return 3;
		} else if (month.equalsIgnoreCase("apr")) {
			return 4;
		} else if (month.equalsIgnoreCase("may")) {
			return 5;
		} else if (month.equalsIgnoreCase("jun")) {
			return 6;
		} else if (month.equalsIgnoreCase("jul")) {
			return 7;
		} else if (month.equalsIgnoreCase("aug")) {
			return 8;
		} else if (month.equalsIgnoreCase("sep")) {
			return 9;
		} else if (month.equalsIgnoreCase("oct")) {
			return 10;
		} else if (month.equalsIgnoreCase("nov")) {
			return 11;
		} else { // month.equalsIgnoreCase("dec")
			return 12;
		}
	}
	
	/**
	 * converts date <int> to human-readable String <DDMMM>
	 */
	public static String fromIntToDDMMM(String date) {
		if (!isDate(date)) {
			return Keywords.EMPTY_STRING;
		}
		int intDate = convertStringDateToInt(date);
		int month = intDate / 32;
		int day = intDate - (month * 32);
		String result = String.format("%d%s", day, convertIntToMonth(month));
		return result;
	}
	private static int convertStringDateToInt(String date) {
		// TOOD: need to change after introducing preposition
		return Integer.parseInt(date);
	}
	private static String convertIntToMonth(int month) {
		switch (month) {
		case 1: return "Jan";
		case 2: return "Feb";
		case 3: return "Mar";
		case 4: return "Apr";
		case 5: return "May";
		case 6: return "Jun";
		case 7: return "Jul";
		case 8: return "Aug";
		case 9: return "Sep";
		case 10: return "Oct";
		case 11: return "Nov";
		case 12: return "Dec";
		}
		return ""; // will never reach this statement
	}
	private String removePreposition(String date) {
		if (date.equals(Keywords.EMPTY_STRING)) {
			return date;
		}
		return date.split(" ", 2)[1];
	}
	private static boolean isDate(String date) {
		try {
			return Integer.parseInt(date) != Keywords.NO_DATE;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}
	
	public static String toCatsForDisplay(ArrayList<String> as) {
		String out = "";
		for (String s : as) {
			out += Keywords.CATEGORY_PREPEND + s + Keywords.SPACE_STRING;
		}
		return out.trim();
	}
	
	public static String toCatsForStore(ArrayList<String> as) {
		String out = "";
		for (String s : as) {
			out += s + Keywords.SPACE_STRING;
		}
		return out.trim();
	}
	
	public static ArrayList<Integer> breakToIds(String rawInput) {
		ArrayList<String> as = breakString(rawInput);
		ArrayList<Integer> ai = new ArrayList<Integer>();
		for (String s : as) {
			try {
				ai.add(Integer.parseInt(s));
			} catch (Exception e) {
				// if s is not Integer parsable
			}
		}
		return ai;
	}
	
	public static String getTaskNameWithCategories(String taskName) {
		String out = Keywords.EMPTY_STRING;
		ArrayList<String> as = breakString(taskName);
		for (String s : as) {
			if (!isCategory(s)) {
				out += s + Keywords.SPACE_STRING;
			}
		}

		return out.trim();
	}
	
	public static String getDateFromRaw(String taskName) {
		String out = Keywords.EMPTY_STRING, s = Keywords.EMPTY_STRING;
		ArrayList<String> as = breakString(taskName);
		for (int i = lastIndexOf(as); !isPreposition(s); i--) {
			s = as.get(i);
			if (!isPreposition(s) && !isCategory(s)) {
				out = s + out;
			} else if (isPreposition(s)) {
				break;
			}
		}

		return out;
	}
	private static int lastIndexOf(ArrayList<String> as) {
		return as.size() - 1;
	}
	
	/**
	 * @param rawInput:
	 * 				without commandType (i.e. add/delete)
	 * @return String without commandType and preposition/date
	 */
	public static String getTaskNameWithPreposition(String taskName) {
		String out = Keywords.EMPTY_STRING;
		ArrayList<String> as = breakString(taskName);
		for (String s : as) {
			if (!isPreposition(s) && !isCategory(s)) {
				out += s + Keywords.SPACE_STRING;
			} else if (isPreposition(s)) {
				break;
			}
		}

		return out.trim();
	}

}
