package logic;

import shared.*;
import storage.Storage;
import java.util.ArrayList;
import java.util.Arrays;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class ViewTask extends Functionality {
	public boolean sortTask(String sortType) {
		return true;
	}

	public ArrayList<Object> viewTasks(String input) {
		ArrayList<Object> combined = new ArrayList<Object>();
		if (input.equals(Keywords.EMPTY_STRING) || input.equalsIgnoreCase("not done")) {
			setNTitle(String.format(Keywords.MESSAGE_VIEW_SUCCESS, "Default"));
			combined.add(getNotification());
			combined.add(Storage.getListOfUncompletedTasks());
			return combined;
		}
		setNTitle(String.format(Keywords.MESSAGE_VIEW_SUCCESS, input));
		if (input.equalsIgnoreCase("done")) {
			combined.add(getNotification());
			combined.add(Storage.getListOfCompletedTasks());
			return combined;
		} else {
			ArrayList<String> categories = new ArrayList<String>(Arrays.asList(input.split(Keywords.SPACE_STRING)));
			combined.add(getNotification());
			combined.add(viewByCat(categories));
			return combined;
		}
	}

	private ArrayList<Task> viewByCat(ArrayList<String> categories) {
		ArrayList<Task> taskCats = Storage.getTasksByCat(categories);
		if (taskCats.isEmpty()) {
			setNTitle(Keywords.MESSAGE_ERROR);
			setNMessage("No such category!");
			// TODO: here might need to return same view
		}
		return taskCats;
	}
}
