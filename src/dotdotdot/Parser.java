package dotdotdot;

import java.util.ArrayList;
import java.util.Arrays;

public class Parser {

	public Parser() {
		
	}
	
	/*
	 * @param: String
	 * returns boolean
	 * 
	 * true if user command is executed successfully
	 * false otherwise
	 */
	public boolean input(String rawInput) {
		String commandType = getCommand(rawInput);	
		
		switch(commandType) {
			// TODO: ADD TASK
			case "add":
				// if (1.0), i.e. no preposition AND no @category
				addTask(rawInput);
			break;
			// TODO: DO/DELETE TASK
			case "do":
				doTask(rawInput);
			break;
			// TODO: EDIT TASK
			case "edit":
			
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
	
	private int convertToInt(String taskName) {
		int taskID = -1;
		try {
			taskID = Integer.parseInt(taskName);
		} catch (NumberFormatException nfe) {
		}
		return taskID;
	}

	private boolean isInvalidID(int taskID) {
		return taskID == -1 ? true : false;
	}

	/*
	 * returns the first token of a string, delimited by spaces
	 */
	private String getCommand(String rawInput) {
		return rawInput.split(" ",1)[0];
    }
	
	/*
	 * returns string without first token, delimited by spaces
	 */
	private String getTaskName(String rawInput) {
		return rawInput.split(" ",2)[1];
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
	
}
