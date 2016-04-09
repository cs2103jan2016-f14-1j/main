//@@author A0135778N
/**
 * This is the Notification object, updated by Logic component
 * and used by GUI component for displaying whether command
 * entered by user was successfully carried out or did an error
 * occur.
 */

package logic;

import shared.Keywords;

public class Notification {
	private  String title = "";
	private  String message = "";
	
	public String getTitle() {
		return title;
	}
	
	public String getMessage() {
		return message;
	}	
	
	public void setTitle(String toUpdate) {
		this.title = toUpdate;
	}
	
	public void setMessage(String toUpdate) {
		this.message = toUpdate;
	}

	public void clear() {
		this.title = "";
		this.message = "";
	}

	public void setInvalidMsg() {
		title = Keywords.INVALID_COMMAND;
	}
}
