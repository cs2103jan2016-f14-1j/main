package parser;

import java.util.ArrayList;

import logic.Logic;

public class ParseDelete {
	
	public static boolean deleteTask(String rawInput, int isItUndoFunc) {
		ArrayList<Integer> ids = Formatter.breakToIds(rawInput);
		ArrayList<String> cats = Formatter.breakToCats(rawInput);
		Logic.deleteTask(ids, cats, isItUndoFunc);
		return true;
	}
}
