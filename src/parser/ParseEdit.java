package parser;

import java.util.ArrayList;
import java.util.Date;

import logic.Logic;
import logic.Notification;
import shared.Keywords;
import shared.Task;

public class ParseEdit {
	public static Notification editTask(String rawInput) {
		
		String tempStr = Formatter.getAfterFirstPrep(rawInput);
		String taskName = Keywords.EMPTY_STRING, 
				prep = Keywords.EMPTY_STRING;
		ArrayList<String> inputParts = Formatter.breakString(tempStr);
		
		ArrayList<Integer> ids = Formatter.breakToIds(rawInput);
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
	
		return Logic.editTask(ids, datetimes, taskName, categories);
	}
}
