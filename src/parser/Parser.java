package parser;

import java.util.ArrayList;

import logic.Logic;
import shared.*;

public class Parser {

	public enum COMMAND_TYPE {
		ADD, DO, DELETE, EDIT, DISPLAY, CLEAR, UNDO, HELP, INVALID, SEARCH, MARK
	}

	private static final String COMMAND_ADD = "add";
	private static final String COMMAND_DO = "do";
	private static final String COMMAND_DELETE = "delete";
	private static final String COMMAND_EDIT = "edit";
	private static final String COMMAND_DISPLAY = "display";
	private static final String COMMAND_VIEW = "view";
	private static final String COMMAND_CLEAR = "clear";
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
	
	public ArrayList<Task> parse(String userInput) {
		String commandType = getFirstWord(userInput).toLowerCase();
		String inputWithoutCommandType = removeFirstWord(userInput);
		returnValue = false;
		
		switch (commandType) {
		case COMMAND_ADD:
			returnValue = ParseAdd.addTask(inputWithoutCommandType);
			break;
		case COMMAND_DO:
			returnValue = ParseDo.doTask(inputWithoutCommandType);
			break;
		case COMMAND_DELETE:
			returnValue = ParseDelete.deleteTask(inputWithoutCommandType);
			break;
		case COMMAND_EDIT:
			returnValue = ParseEdit.editTask(inputWithoutCommandType);
			break;
		case COMMAND_DISPLAY:
			// TODO
		case COMMAND_UNDO:
			returnValue = ParseUndo.undoTask();
			break;
		// TODO
		case COMMAND_CLEAR:
			// TODO
		case COMMAND_HELP:
			// TODO
		case COMMAND_VIEW:
			returnValue = true;
			return Logic.viewTask(inputWithoutCommandType);
		case COMMAND_SEARCH:
			returnValue = true;
			return Logic.searchTask(inputWithoutCommandType);
		case COMMAND_MARK:
			returnValue = ParseMark.prioritise(inputWithoutCommandType);
			break;
		default:
			// TODO
		}

		return null;
	}

	private String removeFirstWord(String userInput) {
		return (!userInput.contains(Keywords.SPACE_STRING)) ? Keywords.EMPTY_STRING
				: userInput.split(" ", 2)[Keywords.SECOND_ELEMENT].trim();
	}

	private String getFirstWord(String userInput) {
		return userInput.trim().split("\\s+")[Keywords.FIRST_ELEMENT];
	}

}
