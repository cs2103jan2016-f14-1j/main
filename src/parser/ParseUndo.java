package parser;

import logic.Logic;

public class ParseUndo {

	public static boolean undoTask(){
		Logic.undoTask();
		return true;
	}
}
