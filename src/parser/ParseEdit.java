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
				
		ArrayList<Date> datetimes = new ArrayList<Date>();

		boolean hasCategory = Formatter.getCategories(categories, inputParts),
				hasPreposition = Formatter.getPreposition(preposition, inputParts);
				
		if (hasCategory && !hasPreposition ) {
			taskName = Formatter.getTaskNameWithCategories(tempStr);
		} else if (hasPreposition) {
			taskName = Formatter.getTaskNameWithPreposition(tempStr);
		}
		
		if (Formatter.getDateFromString(tempStr)==null){
			taskName = tempStr;
			datetimes.add(null);
			datetimes.add(null);
			datetimes.add(null);
			datetimes.add(null);
		} else {
			datetimes = Formatter.getDateTimes(tempStr);
		}
	
		return Logic.editTask(Integer.parseInt(Formatter.breakString(rawInput).get(Keywords.TASK_ID)), datetimes,taskName,categories);
	}
}
