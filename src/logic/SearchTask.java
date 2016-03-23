package logic;

import java.util.ArrayList;

import shared.Task;
import storage.Storage;

public class SearchTask extends Functionality{
	
	public ArrayList<Object> searchTask(String words){
		ArrayList<Task> result = new ArrayList<Task>();
		for(Task t: Storage.getListOfTasks()){
			if(t.getTask().contains(words)){
				result.add(t);
			}else if(!t.getCategories().isEmpty()){
				for(String cat:t.getCategories()){
					if(cat.contains(words)){
						result.add(t);
						break;
					}
				}
			}
		}
		ArrayList<Object> combined = new ArrayList<Object>();
		combined.add(getNotification());
		combined.add(result);
		return combined;
	}

}
