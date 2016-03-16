package ui;

import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.SwingUtilities;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

import logic.*;
import parser.*;
import shared.*;
import storage.*;

public class Controller {

	private static final int WRAP_AROUND = 40;

	private final static int NUMBER_OF_DAYS = 7;
	private HashMap<String, ArrayList<String>> putIntoDays = new HashMap<>();
	private static String [] days = new String[NUMBER_OF_DAYS];
	private final static String [] DEFAULT_DAYS = new String[]{"","SUNDAY","MONDAY", "TUESDAY", "WEDNESDAY","THURSDAY", "FRIDAY", "SATURDAY"};

	
	private final static String FILE_PATH = "images/warning-icon.png";
	private final static String OVERDUE = "OVERDUE";
	private final static String TODAY = "TODAY";
	private final static String TOMORROW = "TOMORROW";
	private final static String WEEK = "WEEK";
	private final static String OTHERS = "OTHERS";
	private final static int STARTING_INDEX = 2;
	
	private View view;
	private Parser parser = new Parser();
	private Logic logic = new Logic();
	private Storage storage = new Storage();

	public Controller() throws Exception {
		view = new View();
		
		days[0] = TODAY;
		days[1] = TOMORROW;
		
		String compareDay = getCurrentDay().toUpperCase();
		int index = -1;
		for(int i=0;i<DEFAULT_DAYS.length;i++){
			if(compareDay.equals(DEFAULT_DAYS[i])){
				index = i;
				break;
			}
		}
		
		for(int i = STARTING_INDEX; i < days.length ;i++){
			int z = (index+i)/(NUMBER_OF_DAYS+1);
			if(z == 0){
				days[i] = DEFAULT_DAYS[index+i];
			} else {
				days[i] = DEFAULT_DAYS[(index+i+1)%(NUMBER_OF_DAYS+1)];
			}
		}
		
		timer();
		inputToHint();
		addKeyListener();
		view.getDateLabel().setText(getCurrentDate());
		view.getDayLabel().setText(getCurrentDay());
		view.getTimeLabel().setText(getCurrentTime());
		displayCategory();
		displayList(Storage.getListOfUncompletedTasks());
	}

	private void displayNotification() {

		Label tip = view.getNotification();
		tip.setText(Notification.getTitle() + " " + Notification.getMessage());
		Notification.clear();
	}

	
	private void displayCategory() {
		view.getCategoryTable().removeAll();
		TableItem categoryItem;

		ArrayList<String> categories = Storage.getListOfCategoriesWithCount();
		for (int i = 0; i < categories.size(); i++) {
			categoryItem = new TableItem(view.getCategoryTable(), SWT.NONE);
			categoryItem.setText(categories.get(i));
		}
		
		final TextLayout textLayout = new TextLayout(Display.getCurrent());
		
		view.getCategoryTable().addListener(SWT.PaintItem, new Listener() {
			public void handleEvent(Event event) {
				TableItem item = (TableItem)event.item;
				String text = item.getText();
				textLayout.setText(text);
				
				TextStyle styleForCatCount = new TextStyle(View.boldFont, View.orangeColor, null);
				textLayout.setStyle(styleForCatCount, text.lastIndexOf(" ") + 1, text.length()+1);
				textLayout.draw(event.gc, event.x, event.y);
			}
		});
		
		view.getCategoryTable().addListener(SWT.EraseItem, new Listener() {
			public void handleEvent(Event event) {
			/* indicate that we'll be drawing the foreground in the
			PaintItem listener */
			event.detail &= ~SWT.FOREGROUND;
			}
			});
		}

	private void displayList(ArrayList<Task> list) throws Exception {

		view.getMainTable().removeAll();
		list = Sorter.sortByDate(list);
		TableItem mainItem;
		putIntoDays.clear();
		
		final TableItem firstItem = new TableItem(view.getMainTable(), SWT.NONE);
		firstItem.setText(OVERDUE);
		firstItem.setFont(View.headingFont);
		firstItem.setForeground(View.orangeColor);
		
		Image image  = new Image(Display.getCurrent(), Thread.currentThread().getContextClassLoader().getResourceAsStream(FILE_PATH));
		
		Listener paintListener = new Listener() {
		      public void handleEvent(Event event) {
		    	  
		    	Point pt = new Point(event.x, event.y);
		        TableItem item = view.getMainTable().getItem(pt);
		    	
		        if(item.equals(firstItem)){
		        switch (event.type) {
		        
		        case SWT.MeasureItem: {
		          Rectangle rect = image.getBounds();
		          event.width += rect.width;
		          event.height = Math.max(event.height, rect.height + 2);
		          break;
		        }
		        case SWT.PaintItem: {
		          int x = event.width;
		          Rectangle rect = image.getBounds();
		          int offset = Math.max(0, (event.height - rect.height) / 2);
		          event.gc.drawImage(image, x, event.y + offset);
		          break;
		        }
		        }
		        }
		      }
		    };
		    view.getMainTable().addListener(SWT.MeasureItem, paintListener);
		    view.getMainTable().addListener(SWT.PaintItem, paintListener);
		    	
		for(Task task : list){
			String taskIDandDesc = task.getUserFormat();
			/*
			String formattedOutput = WordUtils.wrap(taskIDandDesc, WRAP_AROUND, "\n", true);
			String outputArray[] = formattedOutput.split("\n");
			for (int j = 0; j < outputArray.length; j++) {

				mainItem = new TableItem(view.getMainTable(), SWT.NONE);

				if (j == 0) {
					mainItem.setText(outputArray[j]);
				} else {
					// Add necessary white space to align tasks
					mainItem.setText(" " + outputArray[j]);
				}
			}
			*/
			
			// TODO change this shit to more than 1
			Date taskDate = task.getDatetimes().get(0);
			
			if(taskDate == null){
				insertToHashMap(OTHERS, taskIDandDesc);
			} else {
			
				System.out.println("here " + taskDate.getDay());
				Calendar compareCalendar = Calendar.getInstance();
			    compareCalendar.set(getCurrentYear(), taskDate.getMonth(), taskDate.getDate());
			 
			    int diffInDay = calculateDiffInDay(compareCalendar);
			    compareCalendar.set(getCurrentYear(), taskDate.getMonth(), taskDate.getDate());
			    
				if(diffInDay == -1){
					insertToHashMap(OVERDUE, taskIDandDesc);
				} else if(diffInDay < 7){
				if (diffInDay == 0){
		    		insertToHashMap(TODAY, taskIDandDesc);
		    	} else if (diffInDay == 1){
		    		insertToHashMap(TOMORROW, taskIDandDesc);
				} else {		
					System.out.println("asdasd " + taskDate.getDay());
					insertToHashMap(DEFAULT_DAYS[taskDate.getDay()], taskIDandDesc);
				}
				
			    } else {
			    	insertToHashMap(OTHERS, taskIDandDesc);
			    }
			}
		}
		
		insertIntoTable(OVERDUE);
		boolean thirdItem = false;
		
		for (String day : days) {
			
			mainItem = new TableItem(view.getMainTable(), SWT.NONE);
			
			if(day.equals(TODAY)){

				mainItem = new TableItem(view.getMainTable(), SWT.NONE);
				mainItem.setText(day);
				mainItem.setFont(View.headingFont);
				mainItem.setForeground(View.orangeColor);
				insertIntoTable(day);
				mainItem = new TableItem(view.getMainTable(), SWT.NONE);
				
			} else if (day.equals(TOMORROW)){

				mainItem.setText(day);
				mainItem.setFont(View.headingFont);
				mainItem.setForeground(View.orangeColor);
				thirdItem = true;
				insertIntoTable(day);
				mainItem = new TableItem(view.getMainTable(), SWT.NONE);
				
			} else if (thirdItem){
				
				mainItem.setText(WEEK);
				mainItem.setFont(View.headingFont);
				mainItem.setForeground(View.orangeColor);
				thirdItem = false;
				mainItem = new TableItem(view.getMainTable(), SWT.NONE);
				mainItem.setText(day);
				mainItem.setFont(View.normalFont);
				mainItem.setForeground(View.orangeColor);
				insertIntoTable(day);
				
			} else {
				mainItem.setText(day);
				mainItem.setFont(View.normalFont);
				mainItem.setForeground(View.orangeColor);
				insertIntoTable(day);
			}
			
			
		}
	    
		mainItem = new TableItem(view.getMainTable(), SWT.NONE);
		mainItem = new TableItem(view.getMainTable(), SWT.NONE);
		mainItem.setText(OTHERS);
		mainItem.setFont(View.headingFont);
		mainItem.setForeground(View.orangeColor);
		insertIntoTable(OTHERS);

	}

	private void displayHelp() {

		Table mainTable = view.getMainTable();
		mainTable.removeAll();

		TableItem mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		mainItem.setText("Adding tasks");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setText("add <TODO>");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setText("add <TODO> (at | by | on | to) <date> [@category]");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.ITALIC));
		mainItem.setText("Eg. add do CS2103 tutorial by Sun");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.ITALIC));
		mainItem.setText("      add buy milk by 15Feb @shopping");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		mainItem.setText("Edit tasks");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setText("edit <task_ID#> to <date>");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.ITALIC));
		mainItem.setText("Eg. edit 1 to 15Feb");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		mainItem.setText("Set priority");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setText("mark <task_ID#>");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.ITALIC));
		mainItem.setText("Eg. set 2 to 10");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		mainItem.setText("Complete Tasks");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setText("do <TODO | task_ID#>");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.ITALIC));
		mainItem.setText("Eg. do receive quest");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.ITALIC));
		mainItem.setText("      do 1");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		mainItem.setText("View tasks");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setText("view <category>");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.ITALIC));
		mainItem.setText("Eg. view shopping");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		mainItem.setText("Show help");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setText("[? | help | h]");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		mainItem.setText("Undo previous command");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setText("[u | undo]");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		mainItem.setText("Keyboard shortcuts");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setText("Undo previous command: Ctrl+Z");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setText("Scroll through command history: \u2191 or \u2193");

	}

	private void addKeyListener()  {

		StyledText input = view.getInput();
		input.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent event) {

				switch (event.keyCode) {

				case SWT.CR:
					// SWT.CR : when "ENTER" key is pressed
					String tempInput = input.getText();
					inputToHint();

					Object result = parser.parse(tempInput);
					displayCategory();
					
					try{
					if (result instanceof ArrayList<?>) {
						// here might need handle is empty arraylist
						displayList((ArrayList<Task>) result);
					}else{
						displayList(Storage.getListOfUncompletedTasks());
					}
					}catch(Exception e){
						e.printStackTrace();
					}
					
					displayNotification();

					break;
				case SWT.ARROW_UP:
					view.getMainTable().setTopIndex(view.getMainTable().getTopIndex() - View.SCROLL_AMOUNT);
					event.doit = false;
					break;
				case SWT.ARROW_DOWN:
					view.getMainTable().setTopIndex(view.getMainTable().getTopIndex() + View.SCROLL_AMOUNT);
					event.doit = false;
					break;
				case SWT.ESC:
					System.exit(0);
					break;
				case SWT.BS:
					if (isTextEmpty(input)) {
						inputToHint();
					} 
					break;
				default:
					// removes hint and changes input back to normal
					if (input.getForeground().equals(View.hintColor)) {
						inputToNormal();
						if(Character.isLetterOrDigit((char)event.keyCode)){
						input.setText((char)event.keyCode + "");
						input.setSelection(1);
						}
					}
					break;
				}
			}
		});

	}

	private void timer() {
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				// Task to be executed every second
				try {
					SwingUtilities.invokeAndWait(new Runnable() {

						@Override
						public void run() {
							Display.getDefault().asyncExec(new Runnable() {
								public void run() {
									view.getTimeLabel().setText(getCurrentTime());
								}
							});
						}
					});
				} catch (InterruptedException e) {

				} catch (InvocationTargetException e) {

				}

			}
		};

		// This will invoke the timer every second
		timer.scheduleAtFixedRate(task, 1000, 1000);
	}

	public String getCurrentDate() {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat(Keywords.FORMAT_DATE);
		return dateFormat.format(date);
	}

	public String getCurrentTime() {
		Date date = new Date();
		DateFormat timeFormat = new SimpleDateFormat(Keywords.FORMAT_TIME);
		return timeFormat.format(date);
	}

	public String getCurrentDay() {
		Date date = new Date();
		DateFormat dayFormat = new SimpleDateFormat(Keywords.FORMAT_DAY);
		return dayFormat.format(date);
	}
	
	public int getCurrentYear() {
		Date date = new Date();
		DateFormat dayFormat = new SimpleDateFormat(Keywords.FORMAT_YEAR);
		return Integer.parseInt(dayFormat.format(date));
	}

	private void inputToHint() {
		view.getInput().setText(View.GUI_HINT);
		view.getInput().setForeground(View.hintColor);
	}

	private void inputToNormal() {
		view.getInput().setText(View.EMPTY_STRING);
		view.getInput().setForeground(View.normalColor);
	}

	private boolean isTextEmpty(StyledText t) {
		return t.getText().length() == 0;
	}
	
	private int calculateDiffInDay(Calendar endDate){
		Calendar currDate = Calendar.getInstance();
	    int daysBetween = -1;  
	    endDate.add(Calendar.DAY_OF_MONTH, 1);
	    while (currDate.before(endDate)) {  
	        currDate.add(Calendar.DAY_OF_MONTH, 1);  
	        daysBetween++;  
	    } 
	    return daysBetween;
	}
    
	private void insertIntoTable(String key){
		
		TableItem mainItem;
		if(putIntoDays.containsKey(key)){
			ArrayList<String> tempArrList = putIntoDays.get(key);
			for(int i = 0; i < tempArrList.size(); i++){
				mainItem = new TableItem(view.getMainTable(), SWT.NONE);
				mainItem.setText(tempArrList.get(i));
			}
		}
	}
	
	private void insertToHashMap(String key, String value){
		ArrayList<String> toAddList= new ArrayList<String>();
		
		if(putIntoDays.containsKey(key)){
			toAddList = putIntoDays.get(key);
		} 
		
		toAddList.add(value);
		putIntoDays.put(key, toAddList);
	}
	
	public View getView() {
		return view;
	}
}
