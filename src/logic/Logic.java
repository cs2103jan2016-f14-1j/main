/**
 * This class acts as a facade for Parser and UI components to interact
 * with the Logic component.
 * 
 * @@author A0135778N
 */

package logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import shared.Task;
import storage.Storage;

public class Logic {

	private static Logic logic;

	/**
	 * Private Logic constructor
	 */
	private Logic() {
	}

	public static Logic getInstance() {
		if (logic == null) {
			logic = new Logic();
		}
		return logic;
	}
	
	public static Notification addTask(Task task) {
		Notification n = new AddTask().addTask(task);
		return n;
	}

	public static Notification deleteTask(ArrayList<Integer> taskIDs, ArrayList<String> cats) {
		return new DeleteTask().deleteTask(taskIDs, cats);	
	}

	public static Notification doTask(ArrayList<Integer> taskIDs) {
			return new DoTask().doTask(taskIDs);
	}

	public static Notification editTask(int taskID, ArrayList<Date> datetimes, String task,
			ArrayList<String> cats, int resetDate, int resetTime) {
		return new EditTask().editTask(taskID, datetimes, task, cats, resetDate, resetTime);
	}
	
	public static ArrayList<Object> viewTask(String input) {
		return new ViewTask().viewTasks(input);
	}

	public static Notification undoTask() {
		return new UndoTask().undoTask();
	}
	
	/**
	 * Used for marking a task. Toggles the task's priority status.
	 * 
	 * @param taskIDs
	 * @return     A notification object.
	 */
	public static Notification prioritise(ArrayList<Integer> taskIDs) {
		return new MarkTask().prioritise(taskIDs);
	}

	public static HashMap<String,Object> searchTask(String words, 
			int isPriortise, String month, int date, ArrayList<String> categories) {
		return new SearchTask().searchTask(words, isPriortise, date, categories, month);
	}
	
	/**
	 * If user inputs invalid command, this method will set the invalid
	 * message and returns it to GUI component for displaying to user.
	 * 
	 * @return n	A notification object.
	 */
	public static Notification invalidCommand(){
		Notification n = new Notification();
		n.setInvalidMsg();
		return n;
	}
	
	public static ArrayList<Task> getUncompletedTasks(){
		return Storage.getListOfUncompletedTasks();
	}
	
	/**
	 * Gets list of last modified/added task(s).
	 * This method removes the list from the history queue.
	 * 
	 * @return     list last modified/added task(s) or null if no history.
	 */
	public static ArrayList<Task> getLastTasks(){
		return Storage.getLastTasks();
	}
	
	/**
	 * Gets list of last modified/added task(s). However,
	 * unlike getLastTasks(), this method only peeks at
	 * the history queue.
	 * 
	 * @return     list last modified/added task(s) or null if no history.
	 */
	public static ArrayList<Task> getLastTasksNoRemove(){
		return Storage.getLastTasksNoRemove();
	}
	
	/**
	 * Gets the list of all categories with the total count of tasks
	 * that belongs to each category.
	 * 
	 * @return     list of categories with their task count.
	 * 			   e.g. in the format "Uncompleted(0)"
	 */
	public static ArrayList<String> getListOfCatWithCount(){
		return Storage.getListOfCategoriesWithCount();
	}
	
	public static LinkedList<String> viewHelp(){
		return new ViewHelp().viewHelp();
	}
	
	/**
	 * Used by ui.Controller class to update existing file,
	 * or if file does not exist, create the file and write
	 * to it.
	 * 
	 * @param fileExists	A boolean value.
	 */
	public static void updateFile(boolean fileExists){
		if(fileExists){
			Storage.readTasksFromFile();
		} else {
			Storage.writeTasksToFile();
		}
	}
	
	/**
	 * Gets all the categories' names.
	 * 
	 * @return     list of categories' names.
	 */
	public static ArrayList<String> getCatNames(){
		return Storage.getAllCategories();
	}
	
	/**
	 * Takes in user input with each char and looks
	 * for words that matches. Used for suggesting
	 * to user/auto-completing words.
	 * 
	 * @param word	User input.
	 * @return      list of possible words.
	 */
	public static ArrayList<String> findCompletions(String word){
		return Storage.findCompletions(word);
	}
}
