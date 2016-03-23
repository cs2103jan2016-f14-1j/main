package parser;

import java.util.ArrayList;

import logic.Logic;

public class ParseDo {
	
	public static boolean doTask(String rawInput) {
		ArrayList<Integer> ids = Formatter.breakToIds(rawInput);
		
		return Logic.doTask(ids);
	}
}
