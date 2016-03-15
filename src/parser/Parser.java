package parser;

import java.util.ArrayList;

import logic.Logic;
import shared.*;

public class Parser {

	public enum COMMAND_TYPE {
		ADD, DO, DELETE, EDIT, DISPLAY, CLEAR, UNDO, HELP, INVALID, UNCOMPLETE, UNADD, MARK
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
	private static final String COMMAND_UNCOMPLETE = "uncomplete";
	private static final String COMMAND_UNADD = "unadd";
	private static final String COMMAND_MARK = "mark";
	private static final String COMMAND_UNDELETE = "undelete";

	public ArrayList<Task> parse(String userInput) {
		String commandType = getFirstWord(userInput).toLowerCase();
		String inputWithoutCommandType = removeFirstWord(userInput);

		switch (commandType) {
		case COMMAND_ADD:
			ParseAdd.addTask(inputWithoutCommandType, Keywords.NO);
			break;
		case COMMAND_DO:
			ParseDo.doTask(inputWithoutCommandType, Keywords.TASK_COMPLETED);
			break;
		case COMMAND_DELETE:
			ParseDelete.deleteTask(inputWithoutCommandType, Keywords.NO);
			break;
		case COMMAND_EDIT:
			ParseEdit.editTask(inputWithoutCommandType);
			break;
		// TODO
		case COMMAND_DISPLAY:
			// TODO
		case COMMAND_UNDO:
			ParseUndo.undoTask();
			// TODO
		case COMMAND_CLEAR:
			// TODO
		case COMMAND_HELP:
			// TODO
		case COMMAND_VIEW:
			return Logic.viewTask(inputWithoutCommandType);
		case COMMAND_UNCOMPLETE:
			ParseDo.doTask(inputWithoutCommandType, Keywords.TASK_NOT_COMPLETED);
			break;
		case COMMAND_UNADD:
			ParseAdd.addTask(inputWithoutCommandType, Keywords.YES);
			break;
		case COMMAND_MARK:
			ParseMark.prioritize(inputWithoutCommandType);
			break;
		case COMMAND_UNDELETE:
			ParseDelete.deleteTask(inputWithoutCommandType, Keywords.YES);
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
