package parser;

import shared.*;
import logic.*;
import java.util.ArrayList;

public class ParseAdd {

	public static boolean addTask(String rawInput) {
		String taskName = rawInput, 
				date = Keywords.EMPTY_STRING, 
				prep = Keywords.EMPTY_STRING;
		ArrayList<String> inputParts = Formatter.breakString(rawInput), 
				categories = new ArrayList<String>(),
				preposition = new ArrayList<String>();
		boolean hasCategory = Formatter.getCategories(categories, inputParts),
				hasPreposition = Formatter.getPreposition(preposition, inputParts);

		if (taskName.equals(Keywords.EMPTY_STRING)) {
			return false;
		}
		
		if (hasCategory && !hasPreposition) {
			taskName = Formatter.getTaskNameWithCategories(rawInput);
		} else if (hasPreposition) {
			date = Formatter.getDateFromRaw(rawInput);
			taskName = Formatter.getTaskNameWithPreposition(rawInput);
			//prep = getFirstElementInArrayList(preposition);
		}
		
		Task task = new Task(date, taskName, categories); 
		
		Logic.addTask(task);

		return true;
	}

}
