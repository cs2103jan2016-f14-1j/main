package parser;

import java.util.ArrayList;

import logic.Logic;

public class ParseDo {
	
	public static boolean doTask(String rawInput, int completeOrNot) {
		ArrayList<Integer> ids = Formatter.breakToIds(rawInput);
		
		Logic.doTask(ids, completeOrNot);

		return true;
	}
}
