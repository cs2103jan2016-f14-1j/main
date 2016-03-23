package parser;

import java.util.ArrayList;

import logic.Logic;
import logic.Notification;

public class ParseDo {
	
	public static Notification doTask(String rawInput) {
		ArrayList<Integer> ids = Formatter.breakToIds(rawInput);
		
		return Logic.doTask(ids);
	}
}
