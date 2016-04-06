//@@author A0125347H

package parser;

import shared.*;
import logic.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ocpsoft.prettytime.nlp.PrettyTimeParser;
import org.ocpsoft.prettytime.nlp.parse.DateGroup;

public class ParseAdd extends Logger {

	private static Task task;
	public static Notification addTask(String rawInput) {
	
		String 	taskName = rawInput;
		ArrayList<String> inputParts = Formatter.breakString(rawInput), 
				categories = new ArrayList<String>(),
				preposition = new ArrayList<String>();
		ArrayList<Date> datetimes = Formatter.getDateTimes(rawInput);
		boolean hasCategory = Formatter.getCategories(categories, inputParts),
				hasPreposition = Formatter.getPreposition(preposition, inputParts);	
		
		if (hasCategory && !hasPreposition) {
			taskName = Formatter.getTaskNameWithCategories(rawInput);
		} else if (hasPreposition) {
			taskName = Formatter.getTaskNameWithPreposition(rawInput);
		}
	
		task = new Task(datetimes, taskName, categories); 
		
		return Logic.addTask(task);
	}
	
	//for JUnit Test
	public static Task getTask(){
		return task;
	}
}
