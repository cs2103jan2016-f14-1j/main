package dotdotdot;

import java.util.ArrayList;
import java.util.Arrays;

public class Parser {

	private Logic logic;
	private static String lastCommand = "";
	
	public static final int COMMAND_SUCCESS = 0;
	public static final int COMMAND_FAIL = 1;
	public static final int COMMAND_UNRECOGNISED = 2;
	public static final String CMD_ADD = "add";
	public static final String CMD_DO = "do";
	public static String CMD_DELETE = "delete";
	public static String CMD_EDIT = "edit";
	public static String CMD_INVALID = "invalid";
	public static String CMD_VIEW = "view";

	public final String NOT_DONE = "not done";
	public final String DONE = "done";
	public final String ALL = "all";
	private final int INT_NOT_DONE = 0;
	private final int INT_DONE = 1;

	private final String REGEX_PREPOSITIONS = "(at|by|on|to)";
	private final String REGEX_ALL_SPACES = "\\s+";
	private final String CATEGORIES = "@";
	private final String EMPTY_STRING = "";
	private final String SPACE_STRING = " ";

	private final int DEFAULT_VIEW = 1;
	private final int FIRST_ELEMENT = 0;
	private final int SECOND_ELEMENT = 1;
	private final int AFTER_PREPOSITION = 3;
	private final int INVALID_ID = -1; // taskID can only be +ve
	private final int SINGLE_DIGIT_DAY = 4; // 4 chars long; i.e. 9Jan
	private final int TASK_BOTH = -2;

	enum COMMAND_TYPE {
		ADD, EDIT, DO, DELETE, INVALID, VIEW
	};

	public Parser() {
		logic = new Logic();
	}

	/**
	 * DUMMY METHOD SOLELY USED FOR JUNIT TESTING
	 */
	public String inputTest(String rawInput) {
		String commandTypeString = getCommand(rawInput);

		COMMAND_TYPE commandType = determineCommandType(commandTypeString);

		switch (commandType) {
		case ADD:
			addTask(rawInput);
			break;
		case DO:
			doTask(rawInput);
			break;
		case EDIT:
			editTask(rawInput);
			break;
		case DELETE:
			deleteTask(rawInput);
			break;
		case VIEW:			
			break;
		default:
			// TODO: DOESN'T FIT INTO ANY OF THE ABOVE!!!
			return "f";
		}

		String t = logic.getStorage().getTaskByIndex(0);
		deleteTask("delete 1");
		return t;
	}

	/*
	 * @param String
	 * 
	 * @return int
	 * 
	 * true if user command is executed successfully false otherwise (or
	 * unrecognised command)
	 */
	public int input(String rawInput) {
		boolean result;
		
		String commandTypeString = getCommand(rawInput);
		
		COMMAND_TYPE commandType = determineCommandType(commandTypeString);

		switch (commandType) {
		case ADD:
			result = addTask(rawInput);
			break;
		case DO:
			result = doTask(rawInput);
			break;
		case EDIT:
			result = editTask(rawInput);
			break;
		case DELETE:
			result = deleteTask(rawInput);
			break;
		case VIEW:
			result = viewTask(rawInput);
			break;
		default:
			// TODO: DOESN'T FIT INTO ANY OF THE ABOVE!!!
			return COMMAND_UNRECOGNISED;
		}
		if (result == false) {
			return COMMAND_FAIL;
		}
		return COMMAND_SUCCESS;
	}

	public int isCompleted(String rawInput) {
		if (rawInput.contains(ALL)) {
			return TASK_BOTH;
		}
		return 	(rawInput.contains(NOT_DONE) || 
				(!rawInput.contains(DONE) && !rawInput.contains(NOT_DONE))) ? 
				INT_NOT_DONE : 
				INT_DONE;
	}

	private boolean addTask(String rawInput) {
		String taskName = getTaskName(rawInput), date = EMPTY_STRING, prep = EMPTY_STRING;
		ArrayList<String> inputParts = breakString(rawInput), categories = new ArrayList<String>(),
				preposition = new ArrayList<String>();
		boolean hasCategory = getCategories(categories, inputParts),
				hasPreposition = getPreposition(preposition, inputParts);

		if (taskName.equals(EMPTY_STRING)) {
			return false;
		}
		
		if (!hasCategory && !hasPreposition) {
			logic.addTask(taskName);
		} else if (hasCategory && !hasPreposition) {
			taskName = getTaskNameWithCategories(taskName);
			logic.addTask(taskName, categories);
		} else { // hasPreposition
			date = getDateFromRaw(taskName);
			taskName = getTaskNameWithPreposition(taskName);
			prep = getFirstElementInArrayList(preposition);
			if (!hasCategory) { // && hasPreposition
				logic.addTask(taskName, prep, date);
			} else { // hasCategory && hasPreposition
				logic.addTask(taskName, prep, date, categories);
			}
		}

		return true;
	}

	/**
	 * @param rawInput:
	 *            the taskID as a String
	 * @return true if taskID exists, false otherwise
	 */
	private boolean doTask(String rawInput) {
		int taskID = convertToInt(getTaskName(rawInput));
		if (isInvalidID(taskID)) {
			return false;
		}
		return logic.doTask(taskID);
	}

	/**
	 * @param rawInput:
	 *            the taskID as a String
	 * @return true if taskID exists, false otherwise
	 */
	private boolean deleteTask(String rawInput) {
		String taskName = getTaskName(rawInput);
		ArrayList<Integer> ids = convertToIds(taskName);

		return logic.deleteTask(ids);
	}
	
	private boolean viewTask(String rawInput) {
		ArrayList<String> inputParts = breakString(rawInput);
		if (isDefaultView(inputParts)) {
			// TODO: show todos that are not completed
		} else {
			// TODO: show by category/all
		}
		
		return true;
	}
	private boolean isDefaultView(ArrayList<String> ip) {
		return ip.size() == DEFAULT_VIEW;
	}

	/**
	 * @param rawInput:
	 *            taskID as a String
	 * @return true if taskID exists and date is valid, false otherwise
	 */
	private boolean editTask(String rawInput) {
		ArrayList<String> inputParts = breakString(rawInput);
		int taskID = convertToInt(getTaskID(inputParts));
		String date = getDateFromRaw(inputParts);

		if (isInvalidID(taskID) || date.isEmpty()) {
			return false;
		}
		return logic.editTask(taskID, date);
	}
	/** returns rawDate if found, otherwise returns EMPTY_STRING
	 */
	private String getDateFromRaw(ArrayList<String> inputParts) {
		String date = EMPTY_STRING;

		for (int i = AFTER_PREPOSITION; i < inputParts.size(); i++) {
			date += inputParts.get(i);
		}

		return formatDate(date);
	}

	private String formatDate(String date) {
		String formattedDate = removeAllSpaces(date);
		return isSingleDigitDay(formattedDate) ? appendZero(formattedDate) : formattedDate;
	}

	private String getTaskID(ArrayList<String> inputParts) {
		try {
			return inputParts.get(SECOND_ELEMENT);
		} catch (IndexOutOfBoundsException e) {
			return EMPTY_STRING;
		}
	}
	
	private int convertToInt(String taskName) {
		int id = INVALID_ID;
		try {
			id = Integer.parseInt(taskName);
		} catch (NumberFormatException nfe) {
		}
		
		return id;
	}
	
	private ArrayList<Integer> convertToIds(String taskName) {
		ArrayList<Integer> ids = new ArrayList<Integer>();
		
		if (taskName.equals(EMPTY_STRING)) {
			ids.add(INVALID_ID);
			return ids;
		}
		
		for (String s : taskName.split(" ")) {
			try {
				ids.add(Integer.parseInt(s));
			} catch (NumberFormatException nfe) {
				ids.add(INVALID_ID);
				return ids;
			}
		}
		return ids;
	}

	private boolean isInvalidID(int taskID) {
		return (taskID == INVALID_ID);
	}

	/**
	 * @param rawInput
	 * @return first token of rawInput, delimited by spaces
	 */
	private String getCommand(String rawInput) {
		return rawInput.split(SPACE_STRING, 2)[FIRST_ELEMENT];
	}

	/*
	 * returns string without first token, delimited by spaces
	 */
	private String getTaskName(String rawInput) {
		String taskName;
		try {
			taskName = rawInput.split(SPACE_STRING, 2)[SECOND_ELEMENT].trim();
		} catch (ArrayIndexOutOfBoundsException e) {
			return EMPTY_STRING;
		}
		return taskName;
	}

	/**
	 * 
	 * @param rawInput
	 *            without commandType (i.e. add/delete)
	 * @return String without commandType and @categories
	 */
	private String getTaskNameWithCategories(String taskName) {
		String out = EMPTY_STRING;
		ArrayList<String> as = breakString(taskName);
		for (String s : as) {
			if (!isCategory(s)) {
				out += s + SPACE_STRING;
			}
		}

		return out.trim();
	}

	/**
	 * @param rawInput:
	 * 				without commandType (i.e. add/delete)
	 * @return String without commandType and preposition/date
	 */
	private String getTaskNameWithPreposition(String taskName) {
		String out = EMPTY_STRING;
		ArrayList<String> as = breakString(taskName);
		for (String s : as) {
			if (!isPreposition(s) && !isCategory(s)) {
				out += s + SPACE_STRING;
			} else if (isPreposition(s)) {
				break;
			}
		}

		return out.trim();
	}

	/**
	 * Get Date from raw input
	 */
	private String getDateFromRaw(String taskName) {
		String out = EMPTY_STRING, s = EMPTY_STRING;
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

	/**
	 * Returns an ArrayList<String> of rawInput, separated by spaces
	 */
	private ArrayList<String> breakString(String rawInput) {
		try {
			return new ArrayList<String>(Arrays.asList(rawInput.split(SPACE_STRING)));
		} catch (IndexOutOfBoundsException e) {
			ArrayList<String> empty = new ArrayList<String>();
			empty.add(EMPTY_STRING);
			return empty;
		}
		
	}

	/**
	 * @param cats:
	 *            will add category(s) to this ArrayList if they exist
	 * @param parts
	 * @return true if >= 1 category exist, false otherwise
	 */
	private boolean getCategories(ArrayList<String> cats, ArrayList<String> parts) {
		boolean hasFound = false;
		for (String s : parts) {
			if (isCategory(s)) {
				cats.add(s);
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
	private boolean getPreposition(ArrayList<String> prep, ArrayList<String> parts) {
		boolean hasFound = false;
		for (String s : parts) {
			if (isPreposition(s)) {
				prep.add(s);
				hasFound = true;
			}
		}

		return hasFound;
	}

	/**
	 * @param as:
	 *            ArrayList<String>
	 * @return first element as a String
	 */
	private String getFirstElementInArrayList(ArrayList<String> as) {
		return as.get(FIRST_ELEMENT);
	}

	private COMMAND_TYPE determineCommandType(String commandTypeString) {
		if (commandTypeString == null)
			throw new Error("command type string cannot be null!");

		if (commandTypeString.equalsIgnoreCase("add")) {
			lastCommand = CMD_ADD;
			return COMMAND_TYPE.ADD;
		} else if (commandTypeString.equalsIgnoreCase("do")) {
			lastCommand = CMD_DO;
			return COMMAND_TYPE.DO;
		} else if (commandTypeString.equalsIgnoreCase("delete")) {
			lastCommand = CMD_DELETE;
			return COMMAND_TYPE.DELETE;
		} else if (commandTypeString.equalsIgnoreCase("edit")) {
			lastCommand = CMD_EDIT;
			return COMMAND_TYPE.EDIT;
		} else if (commandTypeString.equalsIgnoreCase("view")) {
			lastCommand = CMD_VIEW;
			return COMMAND_TYPE.VIEW;
		} else {
			lastCommand = CMD_INVALID;
			return COMMAND_TYPE.INVALID;
		}
	}

	public String getLastCommand(){
		return lastCommand;
	}
	
	private boolean isCategory(String s) {
		return s.startsWith(CATEGORIES);
	}

	private boolean isPreposition(String s) {
		return s.matches(REGEX_PREPOSITIONS);
	}

	private String appendZero(String s) {
		return "0" + s;
	}

	private int lastIndexOf(ArrayList<String> as) {
		return as.size() - 1;
	}

	private String removeAllSpaces(String date) {
		return date.replaceAll(REGEX_ALL_SPACES, EMPTY_STRING);
	}

	private boolean isSingleDigitDay(String date) {
		return date.length() == SINGLE_DIGIT_DAY;
	}

	/**
	 * This method is to link GUI class to the logic class through parser class
	 * 
	 * @return return logic created in this class
	 */
	public Logic getLogic() {
		return logic;
	}

}
