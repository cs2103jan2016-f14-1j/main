package parser;

import java.util.ArrayList;
import java.util.Arrays;

import logic.Logic;
import logic.Notification;
import shared.*;
import storage.LoadWords;

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

	public ArrayList<String> parseAuto(String userInput){
		ArrayList<String> toReturn = new ArrayList<String>();
		ArrayList<String> findLastWord = new ArrayList<String>(Arrays.asList(userInput.split(Keywords.SPACE_STRING)));
		toReturn = Logic.findCompletions(findLastWord.get(findLastWord.size()-1));
		String sentence = "";
		for(int i =0; i <findLastWord.size()-1;i++){
			sentence+=findLastWord.get(i)+" ";
		}
		ArrayList<String> newSentences = new ArrayList<String>();
		for(String word :toReturn){
			newSentences.add(sentence+word);
		}
		return newSentences;
	}
	
	public Object parse(String userInput) {
		String commandType = getFirstWord(userInput).toLowerCase();
		String inputWithoutCommandType = removeFirstWord(userInput);
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
		case COMMAND_DISPLAY:
			// TODO
		case COMMAND_UNDO:
			return returnValue = ParseUndo.undoTask();
			// TODO
		case COMMAND_CLEAR:
			// TODO
		case COMMAND_HELP:
			return Logic.viewHelp();
			// TODO
		case COMMAND_VIEW:
			// returnValue = true;
			return Logic.viewTask(inputWithoutCommandType);
		case COMMAND_SEARCH:
			// returnValue = true;
			ArrayList<Object> output = ParseSearch.filterInput(inputWithoutCommandType);
			return Logic.searchTask((String)output.get(0), //words to be searched
					(int) output.get(1), //user's priority option
					(int) output.get(2), //date in integer form
					(ArrayList<String>)output.get(3));//categories
		case COMMAND_MARK:
			return returnValue = ParseMark.prioritise(inputWithoutCommandType);
		default:
			return returnValue = ParseInvalid.invalidCommand();
		}
	}

	private String removeFirstWord(String userInput) {
		return (!userInput.contains(Keywords.SPACE_STRING)) ? Keywords.EMPTY_STRING
				: userInput.split(" ", 2)[Keywords.SECOND_ELEMENT].trim();
	}

	private String getFirstWord(String userInput) {
		return userInput.trim().split("\\s+")[Keywords.FIRST_ELEMENT];
	}

}
