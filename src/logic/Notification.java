package logic;

public class Notification {
	private static String title = "To be updated";
	private static String message = "";
	
	public static String getTitle() {
		return title;
	}
	
	public static String getMessage() {
		return message;
	}	
	
	public static void setTitle(String toUpdate) {
		title = toUpdate;
	}
	
	public static void setMessage(String toUpdate) {
		message = toUpdate;
	}

	public static void clear() {
		title = "";
		message = "";
	}
}
