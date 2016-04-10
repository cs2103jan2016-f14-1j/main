//@@author A0135778N
/**
 * This Class handles all operations related to
 * the view command.
 */

package logic;

import java.util.ArrayList;
import java.util.Arrays;
import shared.Keywords;
import shared.Task;
import storage.Storage;

public class ViewTask extends Functionality {
	
	// Variable for storing current view Category/Categories.
	private static ArrayList<String> currCat = defaultCat();
	
// ========================= Main View Method =========================
	public ArrayList<Object> viewTasks(String input) {
		if (input.equals(Keywords.EMPTY_STRING) 
				|| input.equalsIgnoreCase(Keywords.WORD_NOT_DONE)) {
			return defaultView();
		}
		setNTitle(String.format(Keywords.MESSAGE_VIEW_SUCCESS, toCat(input)));
		if (input.equalsIgnoreCase(Keywords.WORD_DONE)) {
			return doneView();
		} else if (input.matches(Keywords.REGEX_CONFLICT)) {
			setNTitle(Keywords.MESSAGE_VIEW_CONFLICTS);
			return conflictView();
		} else {
			return otherView(input);
		}
	}

// ========================= Supporting View Method =========================

	private ArrayList<Object> defaultView() {
		ArrayList<Object> combined = new ArrayList<Object>();
		setNTitle(String.format(Keywords.MESSAGE_VIEW_SUCCESS, Keywords.WORD_DEFAULT));
		combined.add(getNotification());
		combined.add(Storage.getListOfUncompletedTasks());
		currCat = defaultCat();
		return combined;
	}

	private ArrayList<Object> doneView() {
		ArrayList<Object> combined = new ArrayList<Object>();
		currCat.clear();
		combined.add(getNotification());
		combined.add(Storage.getListOfCompletedTasks());
		return combined;
	}
	
	private ArrayList<Object> conflictView() {
		ArrayList<Object> combined = new ArrayList<Object>();
		combined.add(getNotification());
		combined.add(FreeSlots.findConflict());
		return combined;
	}
	
	/**
	 * This method returns a list of Objects under the user-specificied
	 * input view type.
	 * 
	 * @param input
	 * @return
	 */
	private ArrayList<Object> otherView(String input) {
		ArrayList<Object> combined = new ArrayList<Object>();
		currCat = new ArrayList<String>(Arrays.asList(input.split(Keywords.SPACE_STRING)));
		combined.add(getNotification());
		combined.add(viewByCat(currCat));
		return combined;
	}
	
	private ArrayList<Task> viewByCat(ArrayList<String> categories) {
		ArrayList<Task> taskCats = Storage.getTasksByCat(categories);
		if (taskCats.isEmpty()) {
			setNTitle(Keywords.MESSAGE_ERROR);
			setNMessage(Keywords.INVALID_CAT);
		}
		return taskCats;
	}

// ========================= Other Methods =========================
	/**
	 * This method initialises the currCat variable
	 * with the "Uncompleted" category. Which is the
	 * default view category.
	 */
	private static ArrayList<String> defaultCat() {
		ArrayList<String> cat = new ArrayList<String>();
		cat.add(Keywords.CATEGORY_DEFAULT);
		return cat;
	}
	
	/**
	 * Formats user input for display in Notification.
	 * @param input
	 * @return
	 */
	private String toCat(String input) {
		if (input.equalsIgnoreCase("conflict")){
			return input;
		}
		String res = Keywords.EMPTY_STRING;
		String[] cats = input.split(Keywords.SPACE_STRING);
		for (String str : cats) {
			res += Keywords.CATEGORY_PREPEND + str + Keywords.SPACE_STRING;
		}
		res.trim();
		return res;
	}
	
	public static ArrayList<String> getCurrCat(){
		return currCat;
	}
}
