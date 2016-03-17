package parser;

import shared.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.ocpsoft.prettytime.nlp.PrettyTimeParser;

public class Formatter extends Logger {
	
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
	 * converts date object <date> to int representing a unique date 
	 * date object might be null
	 */
	public static int fromDateToInt(Date date) {
		if (date == null) {
			return Keywords.NO_DATE;
		}
		return date.getDate() + date.getMonth() * Keywords.CONSTANT_DATE_BREAK;
	}
	
	/**
	 * converts date <int> to date object <Date>
	 */
	public static Date fromIntToDate(String date) {
		if (!isDate(date)) {
			return null;
		}
		int intDate = convertStringDateToInt(date);
		int month = intDate / Keywords.CONSTANT_DATE_BREAK;
		int day = intDate - (month * Keywords.CONSTANT_DATE_BREAK);
		return new Date(Keywords.CONSTANT_YEAR, month, day);
	}
	private static int convertStringDateToInt(String date) {
		// TOOD: need to change after introducing preposition
		return Integer.parseInt(date);
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
			if (s.equals(Keywords.EMPTY_STRING)) {
				continue;
			} else {
				out += String.format("%s%s ", Keywords.CATEGORY_PREPEND, s);
			}
		}
		return out.trim();
	}
	
	public static String toCatsForStore(ArrayList<String> as) {
		String out = "";
		for (String s : as) {
			out += String.format("%s ", s);
		}
		return out.trim();
	}
	
	/**
	 * retrieve all Integer parseable IDs from String
	 * @param rawInput
	 * @return
	 */
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
	
	public static ArrayList<String> breakToCats(String rawInput) {
		ArrayList<String> as = breakString(rawInput);
		ArrayList<String> res = new ArrayList<String>();
		for (String s : as) {
			try {
				Integer.parseInt(s);
				continue;
			} catch (Exception e) {
				if (!s.equals(Keywords.EMPTY_STRING)) {
					res.add(s);
				}
			}
		}
		return res;
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
		
		if (as.isEmpty()) {
			return out;
		}
		
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
	
	public static boolean hasPrepositionOn(String s) {
		return s.contains(Keywords.PREPOSITION_ON);
	}
	
	public static String getTaskNameWithPrepositionOn(String s) {
		return s.split(Keywords.PREPOSITION_ON, 2)[Keywords.FIRST_ELEMENT];
	}
	
	public static ArrayList<Date> getDateTimes(String s) {
		ArrayList<Date> as = new ArrayList<Date>();
		populateDatetimesArrayList(as);
		for (String t : breakBySpacedPreposition(s)) {
			logf("getDateTimes", t);
			Date d = getDateFromString(t);
			if (d != null) { // either date or time
				if (isDateString(t)) {
					logf("getDateTimes, isDateString", t);
					if (as.get(Keywords.INDEX_STARTDATE) == null) {
						as.set(Keywords.INDEX_STARTDATE, d);
					} else {
						as.set(Keywords.INDEX_ENDDATE, d);
					}
				} else { // if (isTimeString(t)) 
					logf("getDateTimes, isTimeString", t);
					if (as.get(Keywords.INDEX_STARTTIME) == null) {
						as.set(Keywords.INDEX_STARTTIME, d);
						if (as.get(Keywords.INDEX_STARTDATE) == null) {
							as.set(Keywords.INDEX_STARTDATE, new Date()); // add today if time exist
						}
					} else {
						as.set(Keywords.INDEX_ENDTIME, d);
					}
				}
			}			
		}		
		return as;
	}
	private static void populateDatetimesArrayList(ArrayList<Date> as) {
		for (int i = 0; i < 4; i++) {
			as.add(null);
		}
	}
	private static boolean isDateString(String s) {
		return s.toLowerCase().matches(Keywords.REGEX_MONTH_EXIST);
	}
	
	private static String[] breakBySpacedPreposition(String s) {
		return s.split(Keywords.REGEX_PREPOSITIONS_WITH_SPACE);
	}
	public static Date getDateFromString(String s) {
		List<Date> parse = new PrettyTimeParser().parse(s);
		if (!parse.isEmpty()) {
			return parse.get(Keywords.FIRST_ELEMENT);
		} else if (s.toLowerCase().trim().matches(Keywords.REGEX_DATE)) {	
			return convertToDate(s.trim());
		}
		logf("getDateFromString", String.format("%s (failed)", s));
		return null;
	}
	/**
	 * assumption: String d is valid date string of (dMMM | ddMMM)
	 */
	private static Date convertToDate(String d) {
		DateFormat df = new SimpleDateFormat(Keywords.DDMMM);
		if (d.length() == 4) {
			df = new SimpleDateFormat(Keywords.DMMM);
		}
		try {
			return df.parse(d);  
		} catch (Exception e) {
		}
		return null; // will never reach this statement
	}

}
