package dotdotdot;

import java.util.ArrayList;
import java.util.Arrays;

public class Parser {
	
	private Logic logic; 
	public final int FIRST_ELEMENT = 0;
	public final int SECOND_ELEMENT = 1;
	public final int AFTER_PREPOSITION = 3;
	public final int INVALID_ID = -1; 		// taskID can only be +ve
	public final int SINGLE_DIGIT_DAY = 4; 	// 4 chars long; i.e. 9Jan
	
	enum COMMAND_TYPE {
		ADD, EDIT, DO, DELETE, INVALID
	};

	public Parser() {	
		logic = new Logic();
	}
	
	/*
	 * @param String
	 * @return boolean
	 * 
	 * true if user command is executed successfully
	 * false otherwise
	 */
	public boolean input(String rawInput) {
		String commandTypeString = getCommand(rawInput);	

		COMMAND_TYPE commandType = determineCommandType(commandTypeString);
		
		switch(commandType) {
			case ADD:
				addTask(rawInput);
				break;
			case DO:
				doTask(rawInput);
				break;
			case EDIT:
				editTask(rawInput);
				break;
			default:
				// TODO: DOESN'T FIT INTO ANY OF THE ABOVE!!!
				return false;
		}
		
		return true;
	}
	
	private boolean addTask(String rawInput) {
		String 	taskName = getTaskName(rawInput),
				date = "",
				prep = "";
		ArrayList<String> 	inputParts = breakString(rawInput),
							categories = new ArrayList<String>(),
							preposition = new ArrayList<String>();
		boolean hasCategory = getCategories(categories, inputParts),
				hasPreposition = getPreposition(preposition, inputParts);
		
		if(!hasCategory && !hasPreposition) {
			logic.addTask(taskName); 
		} else if (hasCategory && !hasPreposition) {
			taskName = getTaskNameWithCategories(taskName);
			logic.addTask(taskName, categories); 
		} else { // hasPreposition
			taskName = getTaskNameWithPreposition(taskName);
			prep = getFirstElementInArrayList(preposition);
			date = getDateFromRaw(taskName);
			if (!hasCategory) { // && hasPreposition
				logic.addTask(taskName, prep, date);
			} else { // hasCategory && hasPreposition
				logic.addTask(taskName, prep, date, categories);
			}
		} 
		
		return true;
	}
	
	/**
	 * @param rawInput: the taskID as a String
	 * @return true if taskID exists, false otherwise
	 */
	private boolean doTask(String rawInput) {
		String 	taskName = getTaskName(rawInput);
		int taskID = convertToInt(taskName);
		
		if(isInvalidID(taskID)) {
			return false;
		}
		
		logic.doTask(taskID);
		return true;
	}
	
	/**
	 * @param rawInput: taskID as a String
	 * @return true if taskID exists and date is valid, false otherwise
	 */
	private boolean editTask(String rawInput) {
		ArrayList<String> inputParts = breakString(rawInput);
		int taskID = convertToInt(getTaskID(inputParts));
		String date = getDateFromRaw(inputParts);
		
		if(isInvalidID(taskID)) {
			return false;
		}
		
		logic.editTask(taskID, date);
		
		return true;
	}
	
	private String getDateFromRaw(ArrayList<String> inputParts) {
		String date = "";
		
		for(int i = AFTER_PREPOSITION; i < inputParts.size(); i++) {
			date += inputParts.get(i);
		}
		
		return formatDate(date);
	}

	private String formatDate(String date) {
		String formattedDate = date.replaceAll("\\s+","");
		return (formattedDate.length() == SINGLE_DIGIT_DAY) ? appendZero(formattedDate) : formattedDate;
	}

	private String getTaskID(ArrayList<String> inputParts) {
		return inputParts.get(SECOND_ELEMENT);
	}

	private int convertToInt(String taskName) {
		int taskID = INVALID_ID;
		try {
			taskID = Integer.parseInt(taskName);
		} catch (NumberFormatException nfe) {
		}
		return taskID;
	}

	private boolean isInvalidID(int taskID) {
		return (taskID == INVALID_ID);
	}

	/**
	 * @param rawInput
	 * @return first token of rawInput, delimited by spaces
	 */
	private String getCommand(String rawInput) {
		return rawInput.split(" ",2)[FIRST_ELEMENT];
    }
	
	/*
	 * returns string without first token, delimited by spaces
	 */
	private String getTaskName(String rawInput) {
		return rawInput.split(" ",2)[SECOND_ELEMENT];
    }
	
	/**
	 * 
	 * @param rawInput without commandType (i.e. add/delete)
	 * @return String without commandType and @categories
	 */
	private String getTaskNameWithCategories(String taskName) {
		String out = "";
		ArrayList<String> as = breakString(taskName);
		for(String s : as) {
			if(!isCategory(s)) {
				out += s + " ";
			}
		}
		
		return out;
    }
	
	/**
	 * 
	 * @param rawInput without commandType (i.e. add/delete)
	 * @return String without commandType and preposition/date
	 */
	private String getTaskNameWithPreposition(String taskName) {
		String out = "";
		ArrayList<String> as = breakString(taskName);
		for(String s : as) {
			if(!isPreposition(s) && !isCategory(s)) {
				out += s + " ";
			} else {
				break;
			}
		}
		
		return out;
    }
	
	private String getDateFromRaw(String taskName) {
		String 	out = "",
				s = "";
		ArrayList<String> as = breakString(taskName);
		for(int i = lastIndexOf(as); !isPreposition(s); i--) {
			s = as.get(i);
			if(!isPreposition(s) && !isCategory(s)) {
				out = s + out;
			} else {
				break;
			}
		}
		
		return out;
    }
	
	/**
	 * @param rawInput
	 * @return
	 */
    private ArrayList<String> breakString(String rawInput){
    	return new ArrayList<String>(Arrays.asList(rawInput.split(" ")));
    }
	
    /**
     * @param cats: will add category(s) to this ArrayList if they exist
     * @param parts
     * @return true if >= 1 category exist, false otherwise
     */
	private boolean getCategories(ArrayList<String> cats, ArrayList<String> parts){
		boolean hasFound = false;
		for(String s : parts) {
			if(isCategory(s)) {
				cats.add(s);
				hasFound = true;
			}
		}

        return hasFound;
	}
	
	/**
	 * @param prep: will add preposition to this ArrayList if it exists
	 * @param parts
	 * @return true if preposition exists, false otherwise
	 */
	private boolean getPreposition(ArrayList<String> prep, ArrayList<String> parts){
		boolean hasFound = false;
		for(String s : parts) {
			if(isPreposition(s)) {
				prep.add(s);
				hasFound = true;
			}
		}

        return hasFound;
	}
	
	/**
	 * @param as: ArrayList<String>
	 * @return first element as a String
	 */
	private String getFirstElementInArrayList(ArrayList<String> as) {
		return as.get(FIRST_ELEMENT);
	}
	
	private COMMAND_TYPE determineCommandType(String commandTypeString) {
		if (commandTypeString == null)
			throw new Error("command type string cannot be null!");

		if (commandTypeString.equalsIgnoreCase("add")) {
			return COMMAND_TYPE.ADD;
		} else if (commandTypeString.equalsIgnoreCase("do")) {
			return COMMAND_TYPE.DO;
		} else if (commandTypeString.equalsIgnoreCase("delete")) {
			return COMMAND_TYPE.DELETE;
		} else if (commandTypeString.equalsIgnoreCase("edit")) {
			return COMMAND_TYPE.EDIT;
		} else {
			return COMMAND_TYPE.INVALID;
		}
	}
	
	private boolean isCategory(String s) {
		return s.startsWith("@");
	}
	
	private boolean isPreposition(String s) {
		return s.matches("(at|by|to|on)");
	}
	
	private String appendZero(String s) {
		return "0" + s;
	}
	private int lastIndexOf(ArrayList<String> as) {
		return as.size() - 1;
	}
	
	/**
	 * This method is to link GUI class to the logic class through parser class
	 * 
	 * @return
	 * 		return logic created in this class
	 */
	public Logic getLogic(){
		return logic;
	}
	
}
