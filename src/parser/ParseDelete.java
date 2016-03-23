package parser;

import java.util.ArrayList;

import logic.Logic;

public class ParseDelete {
	
	public static boolean deleteTask(String rawInput) {
		ArrayList<Integer> ids = Formatter.breakToIds(rawInput);
		ArrayList<String> cats = Formatter.breakToCats(rawInput);
		return Logic.deleteTask(ids, cats);
	}
}
