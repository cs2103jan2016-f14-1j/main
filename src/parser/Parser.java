//@@author A0125347H

package parser;

import java.util.ArrayList;
import java.util.Arrays;

import logic.Logic;
import logic.Notification;
import shared.*;

public class Parser {

	public enum COMMAND_TYPE {
		ADD, DO, DELETE, EDIT, DISPLAY, CLEAR, UNDO, HELP, INVALID, SEARCH, MARK
	}

	private static final String COMMAND_ADD = "add";
	private static final String COMMAND_DO = "do";
	private static final String COMMAND_DELETE = "delete";
	private static final String COMMAND_EDIT = "edit";
	private static final String COMMAND_VIEW = "view";
	private static final String COMMAND_UNDO = "undo";
	private static final String COMMAND_HELP = "help";
	private static final String COMMAND_SEARCH = "search";
	private static final String COMMAND_MARK = "mark";
	private static Parser parser;
	// For JUnit testing
	public static boolean returnValue;

	// private constructor
	private Parser() {
	}

	public static Parser getInstance() {
		if (parser == null) {
			parser = new Parser();
		}
		return parser;
	}
	
	@SuppressWarnings("unchecked")
	public Object parse(String userInput) {
		String commandType = getFirstWord(userInput).toLowerCase();
		String inputWithoutCommandType = removeFirstWord(userInput);
		inputWithoutCommandType = filterDelimiters(inputWithoutCommandType);
		Notification returnValue = new Notification();

		switch (commandType) {
		case COMMAND_ADD:
			return returnValue = ParseAdd.addTask(inputWithoutCommandType);
		case COMMAND_DO:
			return returnValue = ParseDo.doTask(inputWithoutCommandType);
		case COMMAND_DELETE:
			return returnValue = ParseDelete.deleteTask(inputWithoutCommandType);
		case COMMAND_EDIT:
			return returnValue = ParseEdit.editTask(inputWithoutCommandType);
		case COMMAND_UNDO:
			return returnValue = ParseUndo.undoTask();
		case COMMAND_HELP:
			return Logic.viewHelp();
		case COMMAND_VIEW:
			return Logic.viewTask(inputWithoutCommandType);
		case COMMAND_SEARCH:
			ArrayList<Object> output = ParseSearch.filterInput(inputWithoutCommandType);
			return Logic.searchTask((String)output.get(0), //words to be searched
					(int) output.get(1), //user's priority option
					(String) output.get(2), //get month
					(int )output.get(3),//get date by int
					(ArrayList<String>) output.get(4),//category
					(int) output.get(5));//does user want the busiest day of month
		case COMMAND_MARK:
			return returnValue = ParseMark.prioritise(inputWithoutCommandType);
		default:
			return returnValue = ParseInvalid.invalidCommand();
		}
	}

	public String removeFirstWord(String userInput) {
		return (!userInput.contains(Keywords.SPACE_STRING)) ? Keywords.EMPTY_STRING
				: userInput.split(Keywords.SPACE_STRING, 2)[Keywords.SECOND_ELEMENT].trim();
	}

	private String getFirstWord(String userInput) {
		return userInput.trim().split("\\s+")[Keywords.FIRST_ELEMENT];
	}
	
	private String filterDelimiters(String userInput) {
		return userInput.replaceAll("\\|", Keywords.EMPTY_STRING);
	}

}
