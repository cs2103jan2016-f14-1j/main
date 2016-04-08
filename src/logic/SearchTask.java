//@@author A0076520L

package logic;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.eclipse.swt.widgets.Display;

import parser.Formatter;
import shared.Logger;
import shared.Keywords;
import shared.Task;
import storage.Storage;

public class SearchTask extends Functionality {

	ArrayList<String>replace;
	public HashMap<String, Object> searchTask(String words, int isPriortise, int date, ArrayList<String> categories, String month){
		replace =new ArrayList<String>();
		replace.add("Do you mean:");
		HashMap<String, Object> results = new HashMap<String, Object>();
		ArrayList<Task> result = new ArrayList<Task>();
		if (isPriortise == 1 || isPriortise == 0) {
			result = filterPriority(Storage.getListOfUncompletedTasks(), isPriortise);
		} else {
			result = Storage.getListOfUncompletedTasks();
		}
		
		if (date != -1) {
			// search <result> comparing dates
			result = filterDate(result, date);
			// get free time slots
			ArrayList<String> freeSlots = FreeSlots.getFreeSlots(date);
			if (freeSlots.isEmpty()) {
				freeSlots.add("Whole day is free");
			}
			results.put("free", freeSlots);
		}
		
		//search by month
		if(!month.equals(Keywords.EMPTY_STRING)){
			result = filterByMonth(result, month);
		}

		if (!categories.isEmpty()) {
			result = filterCategories(result, categories);
		}
		// Lastly, after all the filtering, search for words containing if any
		if(!words.equals("")){
			result = filterWords(result, words);
		}
		if (result.size() == 0) {
			setNTitle("Search Success!");
			setNMessage("No results found!");
		} else {
			setNTitle("Search Success!");
			setNMessage("Results found: " + result.size());
		}
		ArrayList<Object> combined = new ArrayList<Object>();
		combined.add(getNotification());
		results.put("Tasks", result);
		results.put("notification", getNotification());
		results.put("replace", replace);
		combined.add(result);
		return results;
	}
	private ArrayList<Task> filterWords(ArrayList<Task> list, String words) {
		ArrayList<Task> temp = new ArrayList<Task>();
		File f=null;
		try {
			f = new File(new String(Files.readAllBytes(Paths.get(getClass().getResource("/storage/ditionary").toURI()))));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logf(Keywords.currLocation," current Location");
		logf("is file found?", (f.exists())?"true":"false");
		System.out.print(Keywords.currLocation);
		SymSpell.CreateDictionary(f, "");
		for (Task t : list) {
			for (String word : words.split(Keywords.SPACE_STRING)) {
				ArrayList<String> result = SymSpell.Correct(word, "");
				for (String wor : result) {
					if (t.getTask().contains(wor) || t.getTask().contains(word)) {
						if(wor!=word && !replace.contains(wor)){
							replace.add(wor);
						}
						if(!temp.contains(t)){
							temp.add(t);
						}
						break;
					} else if (!t.getCategories().isEmpty()) {
						for (String cat : t.getCategories()) {
							if (cat.contains(wor)||cat.contains(word)) {
								if(!temp.contains(t)){
									temp.add(t);
								}
								break;
							}
						}
					}
				}
			}
		}
		return temp;
	}

	private ArrayList<Task> filterDate(ArrayList<Task> list, int date) {
		ArrayList<Task> temp = new ArrayList<Task>();
		for (Task t : list) {
			if (t.getIntDate() == date && t.getIsCompleted() == Keywords.TASK_NOT_COMPLETED) {
				temp.add(t);
			}
		}
		return temp;
	}

	// filter out task with priority
	private ArrayList<Task> filterPriority(ArrayList<Task> list, int isPriortise) {
		ArrayList<Task> temp = new ArrayList<Task>();
		for (Task t : list) {
			if (t.getPriority() == isPriortise && t.getIsCompleted() == Keywords.TASK_NOT_COMPLETED) {
				temp.add(t);
			}
		}
		return temp;
	}

	private ArrayList<Task> filterCategories(ArrayList<Task> list, ArrayList<String> catToFilter) {

		ArrayList<Task> temp = new ArrayList<Task>();
		for (Task t : list) {
			for (String cat : catToFilter) {
				if (t.getCategories().contains(cat)) {
					temp.add(t);
					break;
				}
			}
		}
		return temp;
	}
	
	private ArrayList<Task> filterByMonth(ArrayList<Task> list, String month){
		ArrayList<Task> temp = new ArrayList<Task>();
		for(Task t: list){
			String intDate = Integer.toString(t.getIntDate());
			Date dateMth =null;
			try{
				dateMth = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(month);
			}catch(Exception e){
				setNMessage("Wrong date format. Use Feb, may, Jan.");
			}
			Date date = Formatter.fromIntToDate(intDate);
			if(date!=null){
				if(date.getMonth()==dateMth.getMonth()){
					temp.add(t);
				}
			}
		}
		return temp;
	}

}
