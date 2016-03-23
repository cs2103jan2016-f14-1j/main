package parser;

import shared.*;
import logic.*;
import java.util.ArrayList;

public class ParseMark {
	public static Notification prioritise(String rawInput) {
		ArrayList<Integer> taskIDs = Formatter.breakToIds(rawInput);
		return Logic.prioritise(taskIDs);
	}
}
