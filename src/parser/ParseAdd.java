package parser;

import shared.*;
import logic.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ocpsoft.prettytime.nlp.PrettyTimeParser;
import org.ocpsoft.prettytime.nlp.parse.DateGroup;

public class ParseAdd {

	public static boolean addTask(String rawInput) {
		String 	taskName = rawInput, 
				date = Keywords.EMPTY_STRING, 
				prep = Keywords.EMPTY_STRING;
		ArrayList<String> inputParts = Formatter.breakString(rawInput), 
				categories = new ArrayList<String>(),
				preposition = new ArrayList<String>();
		ArrayList<Date> datetimes = Formatter.getDateTimes(rawInput);;
		boolean hasCategory = Formatter.getCategories(categories, inputParts),
				hasPreposition = Formatter.getPreposition(preposition, inputParts);	
		
		if (hasCategory && !hasPreposition) {
			taskName = Formatter.getTaskNameWithCategories(rawInput);
		} else if (hasPreposition) {
			taskName = Formatter.getTaskNameWithPreposition(rawInput);
			//prep = getFirstElementInArrayList(preposition);
		}
		
		for(Date d:datetimes) {
			System.out.println(d);
		}
		
		Task task = new Task(datetimes, taskName, categories); 
		
		Logic.addTask(task);
		
		return true;
	}

}
