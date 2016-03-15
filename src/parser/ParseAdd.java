package parser;

import shared.*;
import logic.*;
import java.util.ArrayList;
import org.ocpsoft.prettytime.nlp.PrettyTimeParser;

public class ParseAdd {

	public static boolean addTask(String rawInput) {
		/* TODO: 	
		 * add <t> on <date> at <stime>
		 * add <t> on <date> from <stime> to <etime>
		 * add <t> from <sdate> to <edate>
		 * add <t> from <sdate> to <edate> at <stime>
		 * add <t> from <sdate> to <edate> from <stime> to <etime>
		 * add <t> from <stime> to <etime>
		 * 
		 * time can only be preceded by <at|from>
		 * date can only be preceded by <at|by|on|from>
		*/
		String 	taskName = rawInput, 
				date = Keywords.EMPTY_STRING, 
				prep = Keywords.EMPTY_STRING,
				sdate = Keywords.EMPTY_STRING,
				edate = Keywords.EMPTY_STRING,
				stime = Keywords.EMPTY_STRING,
				etime = Keywords.EMPTY_STRING;
		ArrayList<String> inputParts = Formatter.breakString(rawInput), 
				categories = new ArrayList<String>(),
				preposition = new ArrayList<String>(),
				dates = Formatter.extractDates(rawInput),
				times = Formatter.extractTimes(rawInput);
		boolean hasCategory = Formatter.getCategories(categories, inputParts),
				hasPreposition = Formatter.getPreposition(preposition, inputParts);

		//if (taskName.equals(Keywords.EMPTY_STRING)) {
		//	return false;
		//}
		// From Jx: if have the above line, Error notification wont work, cause the flow wont go into logic :(
		// 			unless you want shift notification under parser
		
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
