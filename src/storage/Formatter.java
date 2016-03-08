package storage;

import java.util.ArrayList;
import java.util.Arrays;
import shared.Task;

public class Formatter {
	/**
	 * This method is used to split the concatenated task into blocks of
	 * information stored using ArrayList<String>
	 * 
	 * @param task
	 *            the concatenated task to be split
	 * @return return the task in blocks of information stored in ArrayList
	 */
	protected static Task formatTaskToObject(String task) {
		ArrayList<String> properties = new ArrayList<String>(Arrays.asList(task.split(DELIMITER)));
		Task temp = new Task();
		temp.setID(Integer.parseInt(properties.get(TASK_ID)));
		temp.setDate(properties.get(TASK_DATE));
		temp.setIntDate(0);//have not set what position is this intDate supposed to be
		temp.setIsCompleted(Integer.parseInt(properties.get(TASK_ISCOMPLETE)));
		temp.setTask(properties.get(TASK_DESC));
		return temp;
	}
}
