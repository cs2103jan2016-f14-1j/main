//@@author A0125347H

package parser;

import java.util.ArrayList;

import logic.Logic;
import logic.Notification;

public class ParseDelete {
	
	public static Notification deleteTask(String rawInput) {
		ArrayList<Integer> ids = Formatter.breakToIds(rawInput);
		ArrayList<String> cats = Formatter.breakToCats(rawInput);
		
		return Logic.deleteTask(ids, cats);
	}
}
