package parser;

import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import logic.Logic;
import logic.Notification;
import shared.Keywords;
import shared.Task;

public class ParseEdit {

	private static int id;

	private static final String timeRegex = "(((1[012]|[1-9])(:[0-5][0-9])?\\s?(?i)(am|pm))|([01]?[0-9]|2[0-3]):[0-5][0-9])";
	private static final String dateRegex = "(0?[1-9]|[12][0-9]|3[01])\\s?"
			+ "(?i)(January|February|March|April|May|June|July|" + "August|September|October|November|December|"
			+ "Jan|Feb|Mar|Apr|Jun|Jul|Aug|Sep|Oct|Nov|Dec)";
	private static final String catRegex = "(\\#[a-zA-Z0-9]+\\s*)+";

	public static Notification editTask(String rawInput) {

		Pattern p = Pattern.compile(catRegex);
		Matcher m = p.matcher(rawInput);
		String taskName = Keywords.EMPTY_STRING;
		ArrayList<Date> datetimes = new ArrayList<Date>();
		ArrayList<String> categories = new ArrayList<String>();
		for (int i = 0; i < 4; i++) {
			datetimes.add(null);
		}
		int resetDate=0,resetTime=0;
		if(rawInput.contains("NO DATE")){
			resetDate=1;
			rawInput = rawInput.replace("NO DATE", "");
		}
		if(rawInput.contains("NO TIME")){
			resetTime=1;
			rawInput = rawInput.replace("NO TIME", "");
		}
		// find categories
		if (m.find()) {
			categories = Formatter.breakString(m.group(0).replace("#", ""));
			rawInput = rawInput.replaceAll(catRegex, "");
			System.out.println(rawInput);
		}
		String findID = "^\\d+";
		String result = find(p, m, findID, rawInput);
		if(result==null){
			id=-1;
		}else{
			id=Integer.parseInt(result);
			rawInput = rawInput.replaceAll(findID+"\\sto\\s", "");
			System.out.println(rawInput);
		}
		String startDate = "(?<=from|on|by|at)\\s" + dateRegex;
		result = find(p,m,startDate,rawInput);
		if(result!=null){//may have multiple dates
			datetimes.set(Keywords.INDEX_STARTDATE, Formatter.getDateFromString(result));
			rawInput = rawInput.replaceAll("(from|on|by|at)\\s"+dateRegex+"\\s?", "");
			System.out.println(rawInput+" lolol"+result);
			String endDate="(?<=to)\\s"+dateRegex;
			result = find(p,m,endDate,rawInput);
			if(result!=null){//may have end date
				datetimes.set(Keywords.INDEX_ENDDATE, Formatter.getDateFromString(result));
				rawInput = rawInput.replaceAll("to\\s"+dateRegex+"\\s?", "");
			}
		}else{//single date or no date
			startDate = "^"+dateRegex;
			result = find(p,m,startDate,rawInput);
			if(result!=null){
				datetimes.set(Keywords.INDEX_STARTDATE, Formatter.getDateFromString(result));
				rawInput = rawInput.replaceAll(dateRegex+"\\s?", "");
				System.out.println(rawInput+" aasds "+result);
			}
		}
		String startTime = "(?<=from|on|by|at)\\s"+timeRegex;
		result = find(p,m,startTime,rawInput);
		if(result!=null){//may have multiple times
			datetimes.set(Keywords.INDEX_STARTTIME, Formatter.getDateFromString(result));
			rawInput = rawInput.replaceAll("(from|on|by|at)\\s"+timeRegex+"\\s?", "");
			String endTime = "(?<=to)\\s"+timeRegex;
			result = find(p,m,endTime,rawInput);
			if(result!=null){// may have a end time
				datetimes.set(Keywords.INDEX_ENDTIME, Formatter.getDateFromString(result));
				rawInput = rawInput.replaceAll("to\\s"+timeRegex+"\\s?", "");
			}
		}else{//may have a single start time
			startTime = "^"+timeRegex;
			result = find(p,m,startTime,rawInput);
			if(result!=null){
				datetimes.set(Keywords.INDEX_STARTTIME, Formatter.getDateFromString(result));
				rawInput = rawInput.replaceAll(startTime+"\\s?", "");
			}
		}
		//find task description
		taskName = rawInput;
		
		return Logic.editTask(id, datetimes, taskName, categories, resetDate, resetTime);
	}

	private static String find(Pattern p, Matcher m, String regex, String input) {
		p = Pattern.compile(regex);
		m = p.matcher(input);
		if (m.find()) {
			return m.group();
		} else {
			return null;
		}
	}

	public static int returnId() {
		return id;
	}
}
