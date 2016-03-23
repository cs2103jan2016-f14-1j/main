package logic;

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
}
