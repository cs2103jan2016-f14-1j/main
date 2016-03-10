package parser;

import java.util.ArrayList;

import logic.Logic;
import shared.Keywords;

public class ParseDo {
	
	public static boolean doTask(String rawInput) {
		ArrayList<Integer> ids = Formatter.breakToIds(rawInput);
		
		Logic.doTask(ids);

		return true;
	}
}
