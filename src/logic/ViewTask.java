package logic;

import shared.*;
import storage.Storage;
import java.util.ArrayList;
import java.util.Arrays;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class ViewTask extends Functionality {
	
	private static ArrayList<String> currCat = defaultCat();
	
	public boolean sortTask(String sortType) {
		return true;
	}

	private static ArrayList<String> defaultCat() {
		ArrayList<String> cat = new ArrayList<String>();
		cat.add(Keywords.CATEGORY_DEFAULT);
		return cat;
	}

	public ArrayList<Object> viewTasks(String input) {
		ArrayList<Object> combined = new ArrayList<Object>();
		if (input.equals(Keywords.EMPTY_STRING) || input.equalsIgnoreCase("not done")) {
			setNTitle(String.format(Keywords.MESSAGE_VIEW_SUCCESS, "Default"));
			combined.add(getNotification());
			combined.add(Storage.getListOfUncompletedTasks());
			currCat = defaultCat();
			return combined;
		}
		setNTitle(String.format(Keywords.MESSAGE_VIEW_SUCCESS, input));
		if (input.equalsIgnoreCase("done")) {
			currCat.clear();
			combined.add(getNotification());
			combined.add(Storage.getListOfCompletedTasks());
			return combined;
		} else {
			currCat = new ArrayList<String>(Arrays.asList(input.split(Keywords.SPACE_STRING)));
			combined.add(getNotification());
			combined.add(viewByCat(currCat));
			return combined;
		}
	}

	private ArrayList<Task> viewByCat(ArrayList<String> categories) {
		ArrayList<Task> taskCats = Storage.getTasksByCat(categories);
		if (taskCats.isEmpty()) {
			setNTitle(Keywords.MESSAGE_ERROR);
			setNMessage("No such category!");
		}
		return taskCats;
	}
	
	public static ArrayList<String> getCategories(){
		return currCat;
	}
}
