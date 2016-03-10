package parser;

import java.util.ArrayList;

import logic.Logic;

public class ParseDelete {
	
	public static boolean deleteTask(String rawInput) {
		ArrayList<Integer> ids = Formatter.breakToIds(rawInput);
		
		Logic.deleteTask(ids);

		return true;
	}
}
