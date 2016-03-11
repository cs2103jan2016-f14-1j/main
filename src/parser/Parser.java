package parser;

import java.util.ArrayList;

import logic.Logic;
import shared.*;

public class Parser {

	public enum COMMAND_TYPE {
		ADD, DO, DELETE, EDIT, DISPLAY, CLEAR, UNDO, HELP, INVALID,
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

	public ArrayList<Task> parse(String userInput) {
		String commandType = getFirstWord(userInput).toLowerCase();
		String inputWithoutCommandType = removeFirstWord(userInput);

		switch (commandType) {
		case COMMAND_ADD:
			ParseAdd.addTask(inputWithoutCommandType);
			break;
		case COMMAND_DO:
			ParseDo.doTask(inputWithoutCommandType);
			break;
		case COMMAND_DELETE:
			ParseDelete.deleteTask(inputWithoutCommandType);
			break;
		case COMMAND_EDIT:
			ParseEdit.editTask(inputWithoutCommandType);
			break;
		// TODO
		case COMMAND_DISPLAY:
			// TODO
		case COMMAND_UNDO:
			// TODO
		case COMMAND_CLEAR:
			// TODO
		case COMMAND_HELP:
			// TODO
		case COMMAND_VIEW:
			return Logic.viewTask(inputWithoutCommandType);
		default:
			// TODO
		}

		return null;
	}

	private String removeFirstWord(String userInput) {
		System.out.println(userInput);
		return (!userInput.contains(Keywords.SPACE_STRING)) ? Keywords.EMPTY_STRING
				: userInput.split(" ", 2)[Keywords.SECOND_ELEMENT].trim();
	}

	private String getFirstWord(String userInput) {
		return userInput.trim().split("\\s+")[0];
	}

}
