package logic;

import java.util.ArrayList;

import shared.Keywords;
import shared.Task;
import storage.Storage;

public class SearchTask extends Functionality{
	
	public ArrayList<Object> searchTask(String words, int isPriortise, int date, ArrayList<String> categories){
		ArrayList<Task> result = new ArrayList<Task>();
		if(isPriortise==1 || isPriortise==0){
			result = filterPriority(Storage.getListOfUncompletedTasks(), isPriortise);
		}else{
			result = Storage.getListOfUncompletedTasks();
		}
		
		if(date != -1){
			//search <result> comparing dates
			result = filterDate(result, date);
		}
		
		if(!categories.isEmpty()){
			result = filterCategories(result, categories);
		}
		//Lastly, after all the filtering, search for words containing if any
		result = filterWords(result, words);
	
		ArrayList<Object> combined = new ArrayList<Object>();
		combined.add(getNotification());
		combined.add(result);
		return combined;
	}
	
	private ArrayList<Task> filterWords(ArrayList<Task> list, String words){
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
	
	private ArrayList<Task> filterDate(ArrayList<Task> list, int date){
		ArrayList<Task> temp = new ArrayList<Task>();
		for(Task t: list){
			if(t.getIntDate()==date && t.getIsCompleted()==Keywords.TASK_NOT_COMPLETED){
				temp.add(t);
			}
		}
		return temp;
	}
	
	//filter out task with priority
	private ArrayList<Task> filterPriority(ArrayList<Task> list, int isPriortise){
		ArrayList<Task> temp = new ArrayList<Task>();
		for(Task t: list){
			if(t.getPriority()==isPriortise && t.getIsCompleted()==Keywords.TASK_NOT_COMPLETED){
				temp.add(t);
			}
		}
		return temp;
	}
	
	private ArrayList<Task> filterCategories(ArrayList<Task> list, ArrayList<String> catToFilter){
		
		ArrayList<Task> temp = new ArrayList<Task>();
		for(Task t: list){
			for(String cat : catToFilter){
				if(t.getCategories().contains(cat)){
					temp.add(t);
					break;
				}
			}
		}
		return temp;
	}

}
