package dotdotdot;

import java.util.ArrayList;
import java.util.Arrays;

public class Parser {

	private Logic logic;
	private static String lastCommand = "";
	
	public static final int COMMAND_SUCCESS = 0;
	public static final int COMMAND_FAIL = 1;
	public static final int COMMAND_UNRECOGNISED = 2;
	public static final String CMD_ADD = "Add";
	public static final String CMD_DO = "Do";
	public static final String CMD_DELETE = "Delete";
	public static final String CMD_EDIT = "Edit";
	public static final String CMD_INVALID = "Invalid";
	public static final String CMD_SORT = "Sort";
	public static final String CMD_VIEW = "View";
	private static final String VIEW_REGEX = "(view|v|V|VIEW)(.*)";
	
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
	private final int MSG_SIZE = 13;
	
	private int currCommandStatus = -1;
	private int msgSize = -1;
	private boolean isView = false;
	private boolean isSort = false;
	
	private static final String SUCCESS_CONTENT_MESSAGE = "(%1$s) %2$s";
	private static final String SUCCESS_TITLE_MESSAGE = "%1$s Successful";
	private static final String FAIL_MESSAGE = "Invalid command";
	private static final String UNRECOGNISED_MESSAGE = "Unknown command";
	private static final String ERROR_MESSAGE = "An error has occured.";
	
	enum COMMAND_TYPE {
		ADD, DO, DELETE, EDIT, INVALID, SORT, VIEW
	};

	public Parser() {
		logic = new Logic();
	}

	/*
	 * @param String
	 * 
	 * @return int
	 * 
	 * true if user command is executed successfully false otherwise (or
	 * unrecognised command)
	 */
	public void input(String rawInput) {
		boolean result = false;
		isView = false;
		isSort = false;
		
		if(isViewMethod(rawInput)){
			isView = true;
		} else if (isSortMethod(rawInput)) {
			isSort = true;
		}
		
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
		case SORT:
			result = sortTask(rawInput);
			break;
		case VIEW:
			result = viewTask(rawInput);
			break;
		default:
			// TODO: DOESN'T FIT INTO ANY OF THE ABOVE!!!
			currCommandStatus = COMMAND_UNRECOGNISED;
		}
		if (result == false) {
			currCommandStatus = COMMAND_FAIL;
		} else {
			currCommandStatus = COMMAND_SUCCESS;
		}
	}

	public boolean isCompleted(String rawInput) {
		if (rawInput.contains(ALL)) {
			return false;
		}
		return 	(rawInput.contains(NOT_DONE) || 
				(!rawInput.contains(DONE) && !rawInput.contains(NOT_DONE))) ? 
				false : 
				true;
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
		String taskName = getTaskName(rawInput);
		ArrayList<Integer> ids = new ArrayList<Integer>();
		ids = convertToIds(taskName);
		return logic.doTask(ids);
	}

	/**
	 * @param rawInput:
	 *            the taskID as a String
	 * @return true if taskID exists, false otherwise
	 */
	private boolean deleteTask(String rawInput) {
		String taskName = getTaskName(rawInput);
		ArrayList<String> inputParts = breakString(rawInput);
		ArrayList<String> categories = new ArrayList<String>();
		ArrayList<Integer> ids = new ArrayList<Integer>();
		if (getCategories(categories, inputParts)) {
			return logic.deleteByCat(categories);
		}
		ids = convertToIds(taskName);
		return logic.deleteTask(ids);
	}
	
	private boolean sortTask(String rawInput) {
		return logic.sortTask(rawInput);
	}
	
	private boolean viewTask(String rawInput) {
		String viewType = removeCommand(rawInput);
		if (isDefaultView(viewType)) {
			return logic.viewTasks(NOT_DONE);
		} else if (isCompleted(viewType)){
			return logic.viewTasks(DONE);
		} else if (isCategory(viewType)) {
			return logic.viewTasks(viewType);
		} else {
			return false;
		}
	}
	
	private boolean isDefaultView(String viewType) {
		if (viewType.isEmpty()) {
			return true;
		} else if (viewType.equalsIgnoreCase(NOT_DONE)) {
			return true;
		} else {
			return false;
		}
	}
	
	private String removeCommand(String rawInput) {
		return rawInput.replace(getCommand(rawInput), EMPTY_STRING).trim();
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
		} else if (commandTypeString.equalsIgnoreCase("sort")) {
			lastCommand = CMD_SORT;
			return COMMAND_TYPE.SORT;
		} else if (commandTypeString.equalsIgnoreCase("view")) {
			lastCommand = CMD_VIEW;
			return COMMAND_TYPE.VIEW;
		} else {
			lastCommand = CMD_INVALID;
			return COMMAND_TYPE.INVALID;
		}
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
	
	public ArrayList<String> getList() {
		if(isView || isSort){
			return logic.getViewList();
		} else {
			return logic.getDefaultList();
		}
	}
	
	public String getNotifyTitle() {
		String toReturn = "";

		if(currCommandStatus == COMMAND_SUCCESS){
			toReturn = String.format(SUCCESS_TITLE_MESSAGE, lastCommand);
		} else if (currCommandStatus == COMMAND_FAIL) {
			toReturn = FAIL_MESSAGE;
		} else if (currCommandStatus == COMMAND_UNRECOGNISED) {
			toReturn = UNRECOGNISED_MESSAGE;
		} else {
			toReturn = ERROR_MESSAGE;
		}
		currCommandStatus = -1;
		msgSize = toReturn.length();
		return toReturn;
	}
	
	public String getNotifyMsg(){
		
			String longestString = EMPTY_STRING;
			String outputStatus = EMPTY_STRING;
			
			if(lastCommand.equals(CMD_DELETE)){
				ArrayList <Integer> deletedIDS = logic.getCurrTaskIDs();
			
				for(int i = 0; i < deletedIDS.size(); i++){
					String temp = logic.getCurrTaskDescs().get(i);
					outputStatus += String.format(SUCCESS_CONTENT_MESSAGE, deletedIDS.get(i), temp) + "\n";
					if(temp.length() > longestString.length()){
						longestString = temp;
					}
				}
				logic.clearCurrTasks();
			} 
		    
			if (msgSize < longestString.length()) {
				msgSize = longestString.length();
			} 
			
			return outputStatus;

	}
	
	public int getMsgSize(){
		return msgSize*MSG_SIZE;
	}

	private static boolean isViewMethod(String s) {
		return s.matches(VIEW_REGEX);
	}
	
	private boolean isSortMethod(String s) {
		return getCommand(s).equalsIgnoreCase(CMD_SORT);
	}
	
}
