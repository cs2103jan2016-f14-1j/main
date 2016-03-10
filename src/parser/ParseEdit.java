package parser;

import java.util.ArrayList;

import logic.Logic;

public class ParseEdit {
	public static boolean doTask(String rawInput) {
		ArrayList<String> inputParts = Formatter.breakString(rawInput);
		
		// TODO: determine if date edit or description edit
		// (1) edit <id> to <date>
		// (2) edit <id> to <name>
		// (3) edit <cat> to <date>
		//Logic.editTask();

		return true;
	}
}
