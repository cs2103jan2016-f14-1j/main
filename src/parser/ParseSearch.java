package parser;

import java.util.ArrayList;
import java.util.Date;

import logic.Logic;
import logic.Notification;
import shared.Keywords;
import shared.Task;

public class ParseSearch {
	public static ArrayList<Object> filterInput(String rawInput) {
		ArrayList<Object> output = new ArrayList<Object>();
		//filter for words
		if(rawInput.contains("priority")){
			output.add("");
		}else{
			output.add(rawInput);
		}
		//filter is user getting prioritised tasks
		if(rawInput.contains("priority")){
			output.add(true);
		}else{
			output.add(false);
		}
		//filter for dates
		output.add("");
		//filter for categories
		output.add(new ArrayList<String>());
		return output;
	}
}
