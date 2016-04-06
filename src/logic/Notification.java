//@@author A0135778N

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
		title = toUpdate;
	}
	
	public void setMessage(String toUpdate) {
		message = toUpdate;
	}

	public void clear() {
		title = "";
		message = "";
	}

	public void setInvalidMsg() {
		title = Keywords.INVALID_COMMAND;
	}
}
