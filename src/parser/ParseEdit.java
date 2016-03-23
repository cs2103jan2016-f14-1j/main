package parser;

import java.util.ArrayList;

import logic.Logic;
import shared.Keywords;

public class ParseEdit {
	public static boolean editTask(String rawInput) {
		ArrayList<String> inputParts = Formatter.breakString(rawInput);
		
		// TODO: determine if date edit or description edit
		// (1) edit <id(s)> to <date>
		ArrayList<String> tempProp = new ArrayList<>();
		tempProp.add(Formatter.getDateFromRaw(rawInput));
		String s = Formatter.getAfterPreposition(rawInput);
		return Logic.editTask(Integer.parseInt(inputParts.get(Keywords.TASK_ID)),
				Formatter.getDateFromString(s));
		// (2) edit <id> to <name>
		// (3) edit <cat> to <date>
		//Logic.editTask();
	}
}
