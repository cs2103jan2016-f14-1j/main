package dotdotdot;

import java.util.ArrayList;
import java.util.Arrays;

import TextBuddy.COMMAND_TYPE;

public class Parser {
	public final int FIRST_ELEMENT = 0;
	public final int SECOND_ELEMENT = 1;
	public final int AFTER_PREPOSITION = 3;
	public final int INVALID_ID = -1;
	public final int SINGLE_DIGIT_DAY = 4;
	
	enum COMMAND_TYPE {
		ADD, EDIT, DO, DELETE, INVALID
	};

	public Parser() {
		
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
		String 	taskName = getTaskName(rawInput);
		ArrayList<String> 	inputParts = breakString(rawInput),
							categories = new ArrayList<String>(),
							preposition = new ArrayList<String>();
		boolean hasCategory = getCategories(categories, inputParts),
				hasPreposition = getPreposition(preposition, inputParts);

		Logic logic = new Logic();
		
		if(!hasCategory && !hasPreposition) {
			logic.addTask(taskName); 
		} else if (hasCategory && !hasPreposition) {
			logic.addTask(taskName, categories); 
		} else if (!hasCategory && hasPreposition) {
			logic.addTask(taskName, preposition, date);
		} else { // hasCategory && hasPreposition
			logic.addTask(taskName, preposition, date, categories);
		}
		
		return true;
	}
	
	private boolean doTask(String rawInput) {
		String 	taskName = getTaskName(rawInput);
		int taskID = convertToInt(taskName);
		
		if(isInvalidID(taskID)) {
			return false;
		}
		
		Logic logic = new Logic();
		logic.doTask(taskID);
		return true;
	}
	
	private boolean editTask(String rawInput) {
		ArrayList<String> inputParts = breakString(rawInput);
		int taskID = convertToInt(getTaskID(inputParts));
		String date = getDateFromRaw(inputParts);
		
		if(isInvalidID(taskID)) {
			return false;
		}
		
		Logic logic = new Logic();
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
		String temp = date.replaceAll("\\s+","");
		return temp.length() == SINGLE_DIGIT_DAY ? "0" + temp : temp;
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
		return rawInput.split(" ",1)[FIRST_ELEMENT];
    }
	
	/*
	 * returns string without first token, delimited by spaces
	 */
	private String getTaskName(String rawInput) {
		return rawInput.split(" ",2)[SECOND_ELEMENT];
    }
	
	/*
	 * returns an ArrayList of a string, delimited by spaces
	 */
    private ArrayList<String> breakString(String rawInput){
    	return new ArrayList<String>(Arrays.asList(rawInput.split(" ")));
    }
	
	// returns true if there exist at least one category
	private boolean getCategories(ArrayList<String> cats, ArrayList<String> parts){
		boolean hasFound = false;
		for(String s : parts) {
			if(s.startsWith("@")) {
				cats.add(s);
				hasFound = true;
			}
		}

        return hasFound;
	}
	
	// returns true if preposition exists
	private boolean getPreposition(ArrayList<String> prep, ArrayList<String> parts){
		boolean hasFound = false;
		for(String s : parts) {
			if(s.matches("(at|by|to|on)")) {
				prep.add(s);
				hasFound = true;
			}
		}

        return hasFound;
	}
	
	private static COMMAND_TYPE determineCommandType(String commandTypeString) {
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
	
}
