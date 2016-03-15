package parser;

import shared.*;
import logic.*;
import java.util.ArrayList;

public class ParseMark {
	public static boolean prioritize(String rawInput) {
		ArrayList<Integer> taskIDs = Formatter.breakToIds(rawInput);
		Logic.prioritize(taskIDs);
		return true;
	}
}
