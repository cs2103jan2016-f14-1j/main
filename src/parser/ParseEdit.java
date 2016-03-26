package parser;

import java.util.ArrayList;
import java.util.Date;

import logic.Logic;
import logic.Notification;
import shared.Keywords;
import shared.Task;

public class ParseEdit {
	public static Notification editTask(String rawInput) {
		
		String tempStr = Formatter.getAfterPreposition(rawInput);
		String taskName = Keywords.EMPTY_STRING, 
				prep = Keywords.EMPTY_STRING;
		ArrayList<String> inputParts = Formatter.breakString(tempStr);
		
		ArrayList<Integer> ids = Formatter.breakToIds(rawInput);
		ArrayList<String> categories = new ArrayList<String>();
		ArrayList<String> preposition = new ArrayList<String>();
		ArrayList<Date> datetimes = Formatter.getDateTimes(tempStr);
	
		System.out.println(datetimes);
		boolean hasCategory = Formatter.getCategories(categories, inputParts),
				hasPreposition = Formatter.getPreposition(preposition, inputParts);
		
		if (hasCategory && !hasPreposition) {
			taskName = Formatter.getTaskNameWithCategories(tempStr);
		} else if (hasPreposition) {
			taskName = Formatter.getTaskNameWithPreposition(tempStr);
			//prep = getFirstElementInArrayList(preposition);
		}
		System.out.println(taskName + " task");
		// TODO: determine if date edit or description edit
		// (1) edit <id(s)> to <date>
		//ArrayList<String> tempProp = new ArrayList<>();
		//tempProp.add(Formatter.getDateFromRaw(rawInput));
		return Logic.editTask(Integer.parseInt(Formatter.breakString(rawInput).get(Keywords.TASK_ID)), new Task(datetimes,taskName,categories));
	}
}
