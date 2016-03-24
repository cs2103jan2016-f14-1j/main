package logic;

import java.util.ArrayList;

import shared.Keywords;
import shared.Task;
import storage.Storage;

public class SearchTask extends Functionality{
	
	public ArrayList<Object> searchTask(String words, boolean isPriortise, String date, ArrayList<String> categories){
		ArrayList<Task> result = new ArrayList<Task>();
		if(isPriortise){
			result = searchPriority(Storage.getListOfUncompletedTasks());
		}else{
			result = Storage.getListOfUncompletedTasks();
		}
		
		if(!date.isEmpty()){
			//search <result> comparing dates
		}
		
		if(!categories.isEmpty()){
			for(String cat: categories){
				//search <result> with categories
			}
		}
		//Lastly, after all the filtering, search for words containing if any
		result = searchWords(result, words);
	
		ArrayList<Object> combined = new ArrayList<Object>();
		combined.add(getNotification());
		combined.add(result);
		return combined;
	}
	
	private ArrayList<Task> searchWords(ArrayList<Task> list, String words){
		ArrayList<Task> temp = new ArrayList<Task>();
		for(Task t: list){
			if(t.getTask().contains(words)){
				temp.add(t);
			}else if(!t.getCategories().isEmpty()){
				for(String cat:t.getCategories()){
					if(cat.contains(words)){
						temp.add(t);
						break;
					}
				}
			}
		}
		return temp;
	}
	
	//filter out task with priority
	private ArrayList<Task> searchPriority(ArrayList<Task> list){
		ArrayList<Task> temp = new ArrayList<Task>();
		for(Task t: list){
			if(t.getPriority()==1 && t.getIsCompleted()==Keywords.TASK_NOT_COMPLETED){
				temp.add(t);
			}
		}
		return temp;
	}

}
