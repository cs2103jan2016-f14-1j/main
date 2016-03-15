package logic;

import shared.*;
import storage.Storage;
import java.util.ArrayList;
import java.util.Arrays;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class ViewTask {

	public boolean sortTask(String sortType) {
		return true;
	}

	public ArrayList<Task> viewTasks(String input) {
		if (input.equals(Keywords.EMPTY_STRING)) {
			Notification.setTitle(String.format(Keywords.MESSAGE_VIEW_SUCCESS, "Default"));
			return Storage.getListOfUncompletedTasks();
		}
		Notification.setTitle(String.format(Keywords.MESSAGE_VIEW_SUCCESS, input));
		if (input.equalsIgnoreCase("done")) {
			return Storage.getListOfCompletedTasks();
		} else {
			ArrayList<String> categories = new ArrayList<String>(Arrays.asList(input.split(Keywords.SPACE_STRING)));
			return viewByCat(categories);
		}
	}

	private ArrayList<Task> viewByCat(ArrayList<String> categories) {
		ArrayList<Task> taskCats = Storage.getTasksByCat(categories);
		if (taskCats.isEmpty()) {
			Notification.setTitle(Keywords.MESSAGE_ERROR);
			Notification.setMessage("No such category!");
			// TODO: here might need to return same view
		}
		return taskCats;
	}
}
