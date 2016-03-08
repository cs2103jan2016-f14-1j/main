package parser;

import shared.*;
import java.util.ArrayList;

public class ParseAdd {

	public static boolean addTask(String rawInput) {
		String taskName = Formatter.getTaskName(rawInput), 
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
		
		Task task = new Task(date, taskName, categories); 
		
		// TODO: AddTask.addTask(task);
		
		/*
		if (!hasCategory && !hasPreposition) {
			logic.addTask(taskName);
		} else if (hasCategory && !hasPreposition) {
			taskName = getTaskNameWithCategories(taskName);
			logic.addTask(taskName, categories);
		} else { // hasPreposition
			date = getDateFromRaw(taskName);
			taskName = getTaskNameWithPreposition(taskName);
			prep = getFirstElementInArrayList(preposition);
			if (!hasCategory) { // && hasPreposition
				logic.addTask(taskName, prep, date);
			} else { // hasCategory && hasPreposition
				logic.addTask(taskName, prep, date, categories);
			}
		}
		*/	

		return true;
	}

}
