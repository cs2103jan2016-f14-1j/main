package shared;

public class Keywords {
	// class to store global final variables
    
	protected static final String REGEX_HELP = "(h|H|help|HELP|\\?)";
	protected static final String REGEX_VIEW = "(view|v|V|VIEW)(.*)";
	
	public static final String REGEX_PREPOSITIONS = "(at|by|on|to)";
	public static final String REGEX_CATEGORIES = "(#)(.*)";
	public static final String EMPTY_STRING = "";
	public static final String SPACE_STRING = " ";
	public static final String DELIMITER = "\\|";
	public static final String STORE_DELIMITER = "|";
	
	public static final String MESSAGE_DELETE_SUCCESS = "Task(s) Deleted!";
	public static final String MESSAGE_ADD_SUCCESS = "Add Successful!";
	public static final String MESSAGE_EDIT_SUCCESS = "Edit Successful!";
	public static final String MESSAGE_COMPLETED_SUCCESS = "Task Completed!";
	public static final String MESSAGE_VIEW_SUCCESS = "View By %s";
	public static final String MESSAGE_ERROR = "Error!";
	public static final String TASKID_PREPEND = "-";
	public static final String CATEGORY_PREPEND = "#";
	
	public static final int FIRST_ELEMENT = 0;
	public static final int SECOND_ELEMENT = 1;
	public static final int YES = 1;
	public static final int NO = 0;
	
	public static final int NO_DATE = 999;
	
	public static final String FILENAME_FILEPATH = "test.txt";
	
	public static final int TASK_ID = 0;
	public static final int TASK_DESC = 1;
	public static final int TASK_DATE = 2;
	public static final int TASK_CATEGORIES = 3;
	public static final int TASK_ISCOMPLETE = 4;
	public static final int TASK_NOT_FOUND = -1;
	public static final int TASK_BOTH = -2;
	public static final int TASK_NOT_COMPLETED = 0;
	public static final int TASK_COMPLETED = 1;
	
	public static final String FORMAT_DATE = "dd/MM/yyyy";
	public static final String FORMAT_TIME = "hh:mm a";
	public static final String FORMAT_DAY = "EEEEEEE";
}
