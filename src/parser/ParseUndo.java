//@@author A0076520L

package parser;

import logic.Logic;
import logic.Notification;

public class ParseUndo {

	/**
	 * Call the Logic's undoTask method
	 * @return the Notification Object
	 */
	public static Notification undoTask(){
		return Logic.undoTask();
	}
}
