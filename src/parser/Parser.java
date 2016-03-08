package parser;

import shared.*;

public class Parser {
	
	public enum COMMAND_TYPE {
		ADD,
		DO,
		DELETE,
		EDIT,
		DISPLAY,
		CLEAR,
		UNDO,
		INVALID
	}
	
	private static final String COMMAND_ADD = "add";
	private static final String COMMAND_DO = "do";
	private static final String COMMAND_DELETE = "delete";
	private static final String COMMAND_EDIT = "edit";
	private static final String COMMAND_DISPLAY = "display";
	private static final String COMMAND_CLEAR = "clear";
	private static final String COMMAND_UNDO = "undo";
	
	public Task parse(String userInput) {
		String commandType = getFirstWord(userInput).toLowerCase();
		String inputWithoutCommandType = removeFirstWord(userInput);
	
		switch (commandType) {
			case COMMAND_ADD:
				ParseAdd.addTask(inputWithoutCommandType);
			case COMMAND_DO:
				// TODO
			case COMMAND_DELETE:
				
			case COMMAND_EDIT:
				// TODO
			case COMMAND_DISPLAY:
				// TODO
			case COMMAND_UNDO:
				// TODO
			case COMMAND_CLEAR:
				// TODO			
			default:
				// TODO
		}
		
		return null;
	}
	
	 private String removeFirstWord(String userInput) {
		 return userInput.replace(getFirstWord(userInput), "").trim();
	 }

	 private String getFirstWord(String userInput) {
		 return userInput.trim().split("\\s+")[0];
	 }
	 
}
