package shared;

public class Keywords {
	// class to store global final variables
    
	protected static final String REGEX_HELP = "(h|H|help|HELP|\\?)";
	protected static final String REGEX_VIEW = "(view|v|V|VIEW)(.*)";
	
	public static final String REGEX_PREPOSITIONS = "(at|by|on|to)";
	public static final String REGEX_CATEGORIES = "(#.*)";
	public static final String EMPTY_STRING = "";
	public static final String SPACE_STRING = " ";
	public static final String DELIMITER = "\\|";
	public static final String STORE_DELIMITER = "|";
	
	public static final String MESSAGE_DELETE_SUCCESS = "success delete";
	public static final String TASKID_PREPEND = "-";
	public static final String CATEGORY_PREPEND = "#";
	
	public static final int FIRST_ELEMENT = 0;
	
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
}
