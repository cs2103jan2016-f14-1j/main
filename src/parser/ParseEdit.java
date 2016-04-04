package parser;

import java.util.ArrayList;
import java.util.Date;

import logic.Logic;
import logic.Notification;
import shared.Keywords;
import shared.Task;

public class ParseEdit {
	
	private static int id;
	
	public static Notification editTask(String rawInput) {
		String tempStr = Formatter.getAfterFirstPrep(rawInput);
		String taskName = Keywords.EMPTY_STRING;
		ArrayList<String> inputParts = Formatter.breakString(tempStr);
		try{
			id = Integer.parseInt(Formatter.breakString(rawInput).get(Keywords.FIRST_ELEMENT));
		}catch(Exception e){
			id = -1;
		}
		ArrayList<String> categories = new ArrayList<String>();
		ArrayList<String> preposition = new ArrayList<String>();
		ArrayList<Date> datetimes = Formatter.getDateTimes(rawInput);

		boolean hasCategory = Formatter.getCategories(categories, inputParts),
				hasPreposition = Formatter.getPreposition(preposition, inputParts);
				
		if (hasCategory && !hasPreposition) {
			taskName = Formatter.getTaskNameWithCategories(tempStr);
		} else if (Formatter.getDateFromString(tempStr) == null){
			taskName = tempStr;
		} else if (hasPreposition) {
			taskName = Formatter.getTaskNameWithPreposition(tempStr);
		}

		return Logic.editTask(id, datetimes, taskName, categories);
	}
	
	public static int returnId(){
		return id;
	}
}
