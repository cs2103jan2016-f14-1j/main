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
		if(input.equals(Keywords.EMPTY_STRING)){
			return Storage.getListOfUncompletedTasks();
		}
		if(input.equalsIgnoreCase("done")){
			return Storage.getListOfCompletedTasks();
		}else if(input.contains("@")){
			ArrayList<String> categories = new ArrayList<String>(Arrays.asList(input.split(Keywords.SPACE_STRING)));
			return viewByCat(categories);
		}
		return null;
	}
	
	private ArrayList<Task> viewByCat(ArrayList<String> categories) {
		return Storage.getTasksByCat(categories);
	}
}
