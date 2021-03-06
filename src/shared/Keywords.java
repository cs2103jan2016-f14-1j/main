//@@author A0125347H

package shared;

public class Keywords {
	// class to store global final variables
    
	protected static final String REGEX_HELP = "(h|H|help|HELP|\\?)";
	protected static final String REGEX_VIEW = "(view|v|V|VIEW)(.*)";
	
	public static final String REGEX_PREPOSITIONS = "^(at|by|on|to|from)$";
	public static final String REGEX_PREPOSITIONS_WITH_SPACE = "\\s(at|by|on|to|from)";
	public static final String REGEX_CATEGORIES = "(#)(.*)";
	public static final String REGEX_DATE = 
			"^(([1-9])|([0][1-9])|([1-2][0-9])|([3][0-1]))"
			+ "(jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec)$";
	public static final String REGEX_MONTH_EXIST = 
			".*(jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec).*";
	public static final String REGEX_CONFLICT = "\\s?conflict(s)?\\s?";
	public static final String EMPTY_STRING = "";
	public static final String SPACE_STRING = " ";
	public static final String DELIMITER = "\\|";
	public static final String STORE_DELIMITER = "|";
	public static final String NEW_FILE_DELIMITER = "\\";
	public static final String OLD_FILE_DELIMITER = "/";
	
	public static final String MESSAGE_ERROR = "Error!";
	public static final String MESSAGE_ADD_SUCCESS = "Add Successful!";
	public static final String MESSAGE_ADD_BODY = " has been added!";
	public static final String MESSAGE_CONFLICT = "Conflicting time slots! Tasks: ";
	public static final String MESSAGE_COMPLETED_SUCCESS = "Task Completed!";
	public static final String MESSAGE_DELETE_SUCCESS = "Task(s) Deleted!";
	public static final String MESSAGE_DELETE_CAT = "Tasks under %s categories have been deleted!";
	public static final String MESSAGE_EDIT_SUCCESS = "Edit Successful!";
	public static final String MESSAGE_HELP_SUCCESS = "Help Displayed!";
	public static final String MESSAGE_MARK_SUCCESS = "Prioritised Successful!";
	public static final String MESSAGE_MARK_BODY = "Prioritised: ";
	public static final String MESSAGE_VIEW_SUCCESS = "View By %s";
	public static final String MESSAGE_VIEW_CONFLICTS = "View All Conflict(s)!";
	public static final String INVALID_COMMAND = "Invalid Command entered!";
	public static final String INVALID_ID = "Invalid ID entered!";
	public static final String INVALID_CAT = "No such category!";
	public static final String WORD_NOT_DONE = "Not done";
	public static final String WORD_DONE = "Done";
	public static final String WORD_DEFAULT = "Default";
	
	public static final String TASKID_PREPEND = "-";
	public static final String CATEGORY_PREPEND = "#";
	public static final String CATEGORY_DEFAULT = "Uncompleted";
	
	public static final String PREPOSITION_ON = " on ";
	public static final String PREPOSITION_TO = " to ";
	public static final String PREPOSITION_FROM = " from ";
	public static final String PREPOSITION_AT = " at ";
	public static final String PREPOSITION_BY = " by ";
	
	public static final int FIRST_ELEMENT = 0;
	public static final int SECOND_ELEMENT = 1;
	public static final int MAX_DATES = 4;
	public static final int YES = 1;
	public static final int NO = 0;
	
	public static final int NO_DATE = 9999;
	public static final int CONSTANT_YEAR = 2016;
	public static final int CONSTANT_DATE_BREAK = 32;
	public static final int CONSTANT_HOURS = 24;
	public static final int CONSTANT_MIN = 60;
	public static final int CONSTANT_LAST_MIN = 59;
	public static final int CONSTANT_LAST_HOUR = 23;
	
	
	public static String settingsPath =  "settings.ini";
	public static final String TASK_FILENAME = "task.txt";
	public static String currLocation = EMPTY_STRING;
	public static String filePath = currLocation + Keywords.TASK_FILENAME;
	public static final String LOG_FILEPATH = "log.txt";
	
	public static final int TASK_ID = 0;
	public static final int TASK_DESC = 1;
	public static final int TASK_STARTDATE = 2;
	public static final int TASK_ENDDATE = 3;
	public static final int TASK_STARTTIME = 4;
	public static final int TASK_ENDTIME = 5;
	public static final int TASK_CATEGORIES = 6;
	public static final int TASK_ISCOMPLETE = 7;
	public static final int TASK_PRIORITY= 8;
	public static final int TASK_NOT_FOUND = -1;
	public static final int TASK_BOTH = -2;
	public static final int TASK_NOT_COMPLETED = 0;
	public static final int TASK_COMPLETED = 1;
	public static final int INDEX_STARTDATE = 0;
	public static final int INDEX_ENDDATE = 1;
	public static final int INDEX_STARTTIME = 2;
	public static final int INDEX_ENDTIME = 3;
	public static final int TOTAL_DATE = 2;
	public static final int AUTO_LENGTH = 4;
	
	public static final String FORMAT_DATE = "dd MMM yyyy";
	public static final String FORMAT_TIME = "hh:mm a";
	public static final String FORMAT_DAY = "EEEEEEE";
	public static final String FORMAT_HEADER = "dd MMM";
	public static final String FORMAT_YEAR = "yyyy";
	public static final String DDMMM = "ddMMM";
	public static final String DMMM = "dMMM";
	
	public static final String USER_FORMAT = "(#%s) %s %s %s";
	// STORAGE_FORMAT	: 	[taskID]|[task]|[sdate]|
	//						[stime]|[etime]|[categories]|[isComplete]|[priority]
	public static final String STORAGE_FORMAT = "%d|%s|%s|%s|%s|%s|%s|%d|%d|";
	// DATE FORMAT		:	[startDate][endDate] [startTime][endTime]
	public static final String DATE_FORMAT = "%s";
	public static final int DATE_FORMAT_MULTIPLIER = 100;
}
