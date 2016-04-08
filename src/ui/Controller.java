//@@author A0125387Y

package ui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.SwingUtilities;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import logic.*;
import parser.*;
import shared.*;
import storage.*;

public class Controller {

	private final static int NUMBER_OF_DAYS = 7;
	private HashMap<String, ArrayList<Task>> putIntoDays = new HashMap<>();
	private static String[] days = new String[NUMBER_OF_DAYS];
	private final static String[] DEFAULT_DAYS = new String[] { "SATURDAY", "SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY",
			"THURSDAY", "FRIDAY" };
	
	private final Object object = new Object();

	private final static String WARNING_FILE_PATH = "images/warning-icon.png";
	private final static String MARK_FILE_PATH = "images/star-icon.png";
	private Image warningImage;
	private Image starImage;

	private final static String OVERDUE = "OVERDUE";
	private final static String TODAY = "TODAY";
	private final static String TOMORROW = "TOMORROW";
	private final static String WEEK = "WEEK";
	private final static String OTHERS = "OTHERS";
	private final static int STARTING_INDEX = 2;

	private final static String TASK_HEADING = "Task(s)";
	private final static String FREE_HEADING = "Free";
	
	private View view;
	private Parser parser;
	private Logic logic;
	private Storage storage;

	private int lastIndex = -1;
	private int setIndex = -1;
	
	public Controller() throws Exception {
		view = new View();
		initImages();
		initDayArr();

		timer();
		inputToHint();
		addKeyListener();
		setGUITiming();
		
		readFileLocation();

		initClasses();

		displayList(Logic.getUncompletedTasks());
		displayCategory();
	}

	private void initClasses(){
		parser = parser.getInstance();
		logic = logic.getInstance();
		storage = storage.getInstance();
	}
	
	private void initDayArr(){
		days[0] = TODAY;
		days[1] = TOMORROW;

		String compareDay = getCurrentDay().toUpperCase();
		int index = -1;
		for (int i = 0; i < DEFAULT_DAYS.length; i++) {
			if (compareDay.equals(DEFAULT_DAYS[i])) {
				index = i;
				break;
			}
		}

		Calendar today = Calendar.getInstance();
		today.add(Calendar.DAY_OF_MONTH, 1);

		for (int i = STARTING_INDEX; i < days.length; i++) {
			today.add(Calendar.DAY_OF_MONTH, 1);
			days[i] = DEFAULT_DAYS[(index + i) % (NUMBER_OF_DAYS)] + " - " + getHeaderFormat(today.getTime());
		}
	}
	
	private void setGUITiming(){
		view.getDateLabel().setText(getCurrentDate());
		view.getDayLabel().setText(getCurrentDay());
		view.getTimeLabel().setText(getCurrentTime());
	}
	
	private void initImages(){
		starImage = new Image(Display.getCurrent(),
				Thread.currentThread().getContextClassLoader().getResourceAsStream(MARK_FILE_PATH));
		warningImage = new Image(Display.getCurrent(),
				Thread.currentThread().getContextClassLoader().getResourceAsStream(WARNING_FILE_PATH));
	}
	
	private void displayNotification(Notification notify) {
		Label tip = view.getNotification();
		tip.setText(notify.getTitle() + " " + notify.getMessage());
	}

	private void displayCategory() {
		view.getCategoryTable().removeAll();
		TableItem categoryItem;

		ArrayList<String> categories = Logic.getListOfCatWithCount();

		for (int i = 0; i < categories.size(); i++) {
			categoryItem = new TableItem(view.getCategoryTable(), SWT.NONE);
			categoryItem.setText(categories.get(i));
			for (int z = 0; z < ViewTask.getCategories().size(); z++) {
				String compareCategory = categories.get(i);
				compareCategory = compareCategory.substring(0, compareCategory.indexOf(Keywords.SPACE_STRING));

				if (ViewTask.getCategories().get(z).equals(compareCategory)) {
					categoryItem.setBackground(View.newColor);
					break;
				}
			}
		}
		setCategoryText();
	}

	private void setCategoryText(){
		final TextLayout textLayout = new TextLayout(Display.getCurrent());

		view.getCategoryTable().addListener(SWT.PaintItem, new Listener() {
			public void handleEvent(Event event) {
				TableItem item = (TableItem) event.item;
				String text = item.getText();
				textLayout.setText(text);

				TextStyle styleForCatCount = new TextStyle(View.boldFont, View.orangeColor, null);
				textLayout.setStyle(styleForCatCount, text.lastIndexOf(" ") + 1, text.length() + 1);
				textLayout.draw(event.gc, event.x, event.y);
			}
		});

		view.getCategoryTable().addListener(SWT.EraseItem, new Listener() {
			public void handleEvent(Event event) {
				event.detail &= ~SWT.FOREGROUND;
				// MouseOver:
				event.detail &= ~SWT.HOT;
			}
		});
	}
	
	private int getLengthOfDays(Date startTaskDate, Date endTaskDate){
		int lengthOfDays = 0;
		if (startTaskDate != null && endTaskDate != null) {
			Calendar convertStart = Calendar.getInstance();
			convertStart.set(getCurrentYear(), startTaskDate.getMonth(), startTaskDate.getDate());

			Calendar convertEnd = Calendar.getInstance();
			convertEnd.set(getCurrentYear(), endTaskDate.getMonth(), endTaskDate.getDate());

			lengthOfDays = calculateDiffInDay(convertStart, convertEnd);
		}
		return lengthOfDays;
	}
	
	private void displayList(ArrayList<Task> list) throws Exception {

		view.getMainTable().removeAll();
		list = Sorter.sortByDate(list);
		TableItem mainItem;
		putIntoDays.clear();
		lastIndex = -1;
		setIndex = -1;

		final TableItem firstItem = new TableItem(view.getMainTable(), SWT.NONE);
		setTableHeading(firstItem, OVERDUE);
		Listener paintListener = paintOverdueIcon(firstItem);
		paintPriorityIcon();
		
		for (Task task : list) {
			insertKeysToHashMap(task);
		}

		if (!insertKeyIntoTable(OVERDUE, false)) {
			firstItem.setForeground(View.missingColor);
			unpaintOverdueIcon(paintListener);
		}
		
		mainItem = new TableItem(view.getMainTable(), SWT.NONE);
		mainItem.setText("                                                                                                  ");
		lastIndex++;
		
		insertKeysIntoTable();
		view.getMainTable().setTopIndex(setIndex);
	}
	
	private void insertKeysToHashMap(Task task){
		
		Date startTaskDate = task.getDatetimes().get(Keywords.INDEX_STARTDATE);
		Date endTaskDate = task.getDatetimes().get(Keywords.INDEX_ENDDATE);
		
		boolean added = false;
		int lengthOfDays = getLengthOfDays(startTaskDate, endTaskDate);
		
		if (startTaskDate == null) {
			insertToHashMap(OTHERS, task);
		} else {
			for (int i = 0; i < lengthOfDays + 1; i++) {

				Calendar compareCalendar = Calendar.getInstance();
				compareCalendar.set(getCurrentYear(), startTaskDate.getMonth(), startTaskDate.getDate());
				compareCalendar.add(Calendar.DAY_OF_MONTH, i);

				int diffInDay = calculateDiffInDay(Calendar.getInstance(), compareCalendar);
				compareCalendar.set(getCurrentYear(), startTaskDate.getMonth(), startTaskDate.getDate());
				compareCalendar.add(Calendar.DAY_OF_MONTH, i);

				if (diffInDay == -1 && !added) {
					insertToHashMap(OVERDUE, task);
					added = true;
				} else if (diffInDay < 7) {
					if (diffInDay == 0) {
						insertToHashMap(TODAY, task);
					} else if (diffInDay == 1) {
						insertToHashMap(TOMORROW, task);
					} else {
						Date tempDate = compareCalendar.getTime();
						insertToHashMap(DEFAULT_DAYS[(tempDate.getDay() + 1) % 7] + " - " + getHeaderFormat(tempDate), task);
					}
				} else {
					insertToHashMap(OTHERS, task);
					break;
				}
			}
		}
	}
	
	private void insertKeysIntoTable(){
		boolean thirdItem = false;
		TableItem mainItem = null;
		
		for (String day : days) {

			if (day.equals(TODAY)) {

				mainItem = new TableItem(view.getMainTable(), SWT.NONE);
				setTableHeading(mainItem, day);

				if (!insertKeyIntoTable(day, false)) {
					mainItem.setForeground(View.missingColor);
				}

				setSpacing();

			} else if (day.equals(TOMORROW)) {

				mainItem = new TableItem(view.getMainTable(), SWT.NONE);
				setTableHeading(mainItem, day);
				thirdItem = true;
				
				if (!insertKeyIntoTable(day, false)) {
					mainItem.setForeground(View.missingColor);
				}
				
				setSpacing();

			} else if (thirdItem) {

				mainItem = new TableItem(view.getMainTable(), SWT.NONE);
				setTableHeading(mainItem, WEEK);
				thirdItem = false;
				if (!insertKeyIntoTable(day, true)) {
					mainItem.setForeground(View.missingColor);
				}

			} else {

				if (insertKeyIntoTable(day, true)) {
					mainItem.setForeground(View.orangeColor);
				}
			}

		}

		setSpacing();
		mainItem = new TableItem(view.getMainTable(), SWT.NONE);
		setTableHeading(mainItem, OTHERS);
		
		if (!insertKeyIntoTable(OTHERS, false)) {
			mainItem.setForeground(View.missingColor);
		}	
	}
	
	private void paintPriorityIcon(){
		Listener paintStarListener = new Listener() {
			public void handleEvent(Event event) {

				TableItem item = (TableItem) event.item;
				if (item.getData() == object) {
					switch (event.type) {
						case SWT.MeasureItem: {
							Rectangle rect = starImage.getBounds();
							event.width += rect.width;
							event.height = Math.max(event.height, rect.height + 2);
							break;
						}
						case SWT.PaintItem: {
							int x = 7;
							Rectangle rect = starImage.getBounds();
							int offset = Math.max(0, (event.height - rect.height) / 2);
							event.gc.drawImage(starImage, x, event.y + offset + 1);
							break;
						}
					}
				}
			}
		};
		
		view.getMainTable().addListener(SWT.MeasureItem, paintStarListener);
		view.getMainTable().addListener(SWT.PaintItem, paintStarListener);
	}

	private Listener paintOverdueIcon(TableItem firstItem){
		Listener paintListener = new Listener() {
			public void handleEvent(Event event) {

				TableItem item = (TableItem) event.item;
				if (item.equals(firstItem)) {
					switch (event.type) {
					case SWT.MeasureItem: {
						Rectangle rect = warningImage.getBounds();
						event.width += rect.width;
						event.height = Math.max(event.height, rect.height + 2);
						break;
					}
					case SWT.PaintItem: {
						int x = event.width;
						Rectangle rect = warningImage.getBounds();
						int offset = Math.max(0, (event.height - rect.height) / 2);
						event.gc.drawImage(warningImage, x, event.y + offset);
						break;
					}
					}

				}
			}

		};

		view.getMainTable().addListener(SWT.MeasureItem, paintListener);
		view.getMainTable().addListener(SWT.PaintItem, paintListener);
		return paintListener;
	}
	
	private void unpaintOverdueIcon(Listener paintListener){
		view.getMainTable().removeListener(SWT.MeasureItem, paintListener);
		view.getMainTable().removeListener(SWT.PaintItem, paintListener);
	}

	private void displayHelp(LinkedList<String> items) {

		Table mainTable = view.getMainTable();
		mainTable.removeAll();

		TableItem mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(View.headingFont);
		mainItem.setForeground(View.orangeColor);
		mainItem.setText(items.poll());
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setText(items.poll());
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setText(items.poll());
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(View.italicFont);
		mainItem.setText(items.poll());
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(View.italicFont);
		mainItem.setText(items.poll());
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(View.headingFont);
		mainItem.setForeground(View.orangeColor);
		mainItem.setText(items.poll());
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setText(items.poll());
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(View.italicFont);
		mainItem.setText(items.poll());
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(View.headingFont);
		mainItem.setForeground(View.orangeColor);
		mainItem.setText(items.poll());
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setText(items.poll());
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(View.italicFont);
		mainItem.setText(items.poll());
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(View.headingFont);
		mainItem.setForeground(View.orangeColor);
		mainItem.setText(items.poll());
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setText(items.poll());
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(View.italicFont);
		mainItem.setText(items.poll());
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(View.italicFont);
		mainItem.setText(items.poll());
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(View.headingFont);
		mainItem.setForeground(View.orangeColor);
		mainItem.setText(items.poll());
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setText(items.poll());
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(View.italicFont);
		mainItem.setText(items.poll());
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(View.headingFont);
		mainItem.setForeground(View.orangeColor);
		mainItem.setText(items.poll());
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setText(items.poll());
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(View.headingFont);
		mainItem.setForeground(View.orangeColor);
		mainItem.setText(items.poll());
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setText(items.poll());
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(View.headingFont);
		mainItem.setForeground(View.orangeColor);
		mainItem.setText(items.poll());
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setText(items.poll());
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setText(items.poll());

	}

	private void addKeyListener() {

		StyledText input = view.getInput();
		// To prevent extra line being entered
		input.addVerifyKeyListener(new VerifyKeyListener() {

			@Override
			public void verifyKey(VerifyEvent e) {
				if ((e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR)) {
					e.doit = false;
				}
			}
		});

		input.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent event) {

				String tempInput = input.getText();

				switch (event.keyCode) {

				case SWT.KEYPAD_CR:
				case SWT.CR:
					// SWT.CR : when "ENTER" key is pressed
					inputToHint();
					Object result = parser.parse(tempInput);

					try {
						if (result instanceof LinkedList<?>) {
							displayHelp((LinkedList<String>) result);
						} else if (result instanceof ArrayList<?>) {
							// here might need handle is empty arraylist
							ArrayList<Object> re = (ArrayList<Object>) result;
							displayNotification((Notification) re.get(0));
							displayList((ArrayList<Task>) re.get(1));
						} else if (result instanceof HashMap<?, ?>) {
							HashMap<String,Object> re = (HashMap<String,Object>) result;
							displaySearch(re, tempInput);
							displayNotification((Notification)re.get("notification"));
						} else {
							displayList(Logic.getUncompletedTasks());
							displayNotification((Notification) result);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

					displayCategory();
					
					break;

				case SWT.PAGE_UP:
					view.getMainTable().setTopIndex(view.getMainTable().getTopIndex() - View.SCROLL_AMOUNT);
					event.doit = false;
					break;
				case SWT.PAGE_DOWN:
					view.getMainTable().setTopIndex(view.getMainTable().getTopIndex() + View.SCROLL_AMOUNT);
					event.doit = false;
					break;
				case SWT.BS:
					if (tempInput.length() == 0) {
						inputToHint();
					}
					break;
				default:
					// removes hint and changes input back to normal
					if (input.getForeground().equals(View.hintColor)) {
						inputToNormal();
						if (Character.isLetterOrDigit((char) event.keyCode)) {
							input.setText((char) event.keyCode + "");
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
									view.getDateLabel().setText(getCurrentDate());
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

	public String getHeaderFormat(Date date) {
		DateFormat dayFormat = new SimpleDateFormat(Keywords.FORMAT_HEADER);
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

	private int calculateDiffInDay(Calendar startDate, Calendar endDate) {
		int daysBetween = -1;
		endDate.add(Calendar.DAY_OF_MONTH, 1);
		while (startDate.before(endDate)) {
			startDate.add(Calendar.DAY_OF_MONTH, 1);
			daysBetween++;
		}
		return daysBetween;
	}

	private void setTableSubHeading(String subHeading){
		TableItem headerItem = new TableItem(view.getMainTable(), SWT.NONE);
		headerItem.setText(subHeading);
		headerItem.setFont(View.normalFont);
		headerItem.setForeground(View.orangeColor);
		lastIndex++;
	}
	
	private void setSpacing(){
		new TableItem(view.getMainTable(), SWT.NONE);
		lastIndex++;
	}
	
	private void setTableHeading(TableItem headerItem, String heading){
		headerItem.setText(heading);
		headerItem.setFont(View.headingFont);
		headerItem.setForeground(View.orangeColor);
		lastIndex++;
	}
	
	private boolean insertKeyIntoTable(String key, boolean week) {
		if (putIntoDays.containsKey(key)) {
			
			if (week) {
				setTableSubHeading(key);
			}

			ArrayList<Task> tempArrList = putIntoDays.get(key);

			ArrayList<Task> lastTasks = Logic.getLastTasksNoRemove();

			initTextLayout(tempArrList, lastTasks, week);
			
			return true;
		}
		
		return false;
	}
	
	private void initTextLayout(ArrayList<Task> tempArrList, ArrayList<Task> lastTasks, boolean week){
		
		for (int i = 0; i < tempArrList.size(); i++) {
			Task task = tempArrList.get(i);
			final TableItem mainItem = new TableItem(view.getMainTable(), SWT.NONE);
			lastIndex++;
			String whiteSpaces = setWhiteSpaces(task, mainItem);

			// To highlight added or edited tasks
			highlightTasks(lastTasks, mainItem, task.getId(), i);

			String text = whiteSpaces + task.getUserFormat();

			if (week) {

				text = whiteSpaces + task.getUserFormatNoDate() + Keywords.SPACE_STRING
						+ task.getDisplayTimeRange();
			}

			final TextLayout textLayout = new TextLayout(Display.getCurrent());

			textLayout.setText(text);

			setLayoutStyle(task, textLayout, text, whiteSpaces, week);
			
			drawTextLayout(mainItem, textLayout);
		}
		
	}
	
	private void highlightTasks(ArrayList<Task> lastTasks, TableItem mainItem, int taskId, int index){
		if (lastTasks != null) {
			for (int k = 0; k < lastTasks.size(); k++) {
				if (lastTasks.get(k).getId() == taskId) {
					mainItem.setBackground(View.newColor);
					mainItem.setFont(View.boldFont);
					setIndex = lastIndex - index - 1;
				}
			}
		}
	}
	
	private void setLayoutStyle(Task task, TextLayout textLayout, String text, String whiteSpaces, boolean week){
		TextStyle styleDescription = new TextStyle(View.normalFont, null, null);
		TextStyle styleDate = new TextStyle(View.boldFont, View.dateColor, null);
		TextStyle styleCategory = new TextStyle(View.normalFont, View.redColor, null);

		if (task.getDatetimes().get(0) != null) {
			int seperatingIndex = whiteSpaces.length() + task.getUserFormatNoDate().length();
			textLayout.setStyle(styleDescription, 0, seperatingIndex);
			if (week) {
				textLayout.setStyle(styleDate, seperatingIndex, text.length());
			} else {
				textLayout.setStyle(styleDate, seperatingIndex + 3, text.length());
			}

		} else {
			textLayout.setStyle(styleDescription, 0, text.length());
		}

		for (int z = 0; z < task.getCategories().size(); z++) {
			String tempCat = task.getCategories().get(z);
			textLayout.setStyle(styleCategory, text.lastIndexOf(tempCat) - Keywords.CATEGORY_PREPEND.length(),
			text.lastIndexOf(tempCat) + tempCat.length());
		}
	}
	
    private String setWhiteSpaces(Task task, TableItem mainItem){
    	if (task.getPriority() == 1) {
			mainItem.setData(object);
			return "      ";
		}
    	return Keywords.EMPTY_STRING;
    }
	
	private void drawTextLayout(TableItem mainItem, TextLayout textLayout){
		view.getMainTable().addListener(SWT.PaintItem, new Listener() {
			public void handleEvent(Event event) {
				if (event.item.equals(mainItem)) {
					textLayout.draw(event.gc, event.x, event.y);
				}
			}
		});

		final Rectangle textLayoutBounds = textLayout.getBounds();
		view.getMainTable().addListener(SWT.MeasureItem, new Listener() {
			public void handleEvent(Event e) {
				if (e.item.equals(mainItem)) {
					e.width = textLayoutBounds.width + 2;
					e.height = textLayoutBounds.height + 2;
				}
			}
		});
	}
	
	private void insertToHashMap(String key, Task value) {

		ArrayList<Task> toAddList = new ArrayList<Task>();

		if (putIntoDays.containsKey(key)) {
			toAddList = putIntoDays.get(key);
		}

		toAddList.add(value);
		putIntoDays.put(key, toAddList);

	}

	private void readFileLocation() throws IOException {
		BufferedReader bufferReader = null;
		try {
			bufferReader = new BufferedReader(new FileReader(Keywords.settingsPath));
			String currentLine = Keywords.EMPTY_STRING;
			while ((currentLine = bufferReader.readLine()) != null) {
				// Read first line as file location is there
				Keywords.filePath = currentLine;
				break;
			}

		} catch (FileNotFoundException ex) {
			File f = new File(Keywords.settingsPath);
			f.createNewFile();
		} finally {
			try {
				if (bufferReader != null) {
					bufferReader.close();
				}
			} catch (IOException ex) {
				System.exit(0);
			}
		}
	}

	public void writePathToFile() {
		DirectoryDialog dialog = new DirectoryDialog(view.getShell());
		dialog.setFilterPath("c:\\"); // Windows specific
		String path = dialog.open();

		try {
			if (path != null) {

				Keywords.filePath = path + "\\" + Keywords.TASK_FILENAME;

				BufferedWriter bufferWriter = new BufferedWriter(new FileWriter(Keywords.settingsPath));
				bufferWriter.write(Keywords.filePath);
				bufferWriter.close();

				File currFile = new File(Keywords.filePath);

				Logic.updateFile(currFile.exists());

				displayList(Logic.getUncompletedTasks());
				displayCategory();

			}

		} catch (Exception ex) {
			System.exit(0);
		}
	}

	public View getView() {
		return view;
	}
	
	public void displaySearch(HashMap<String,Object> items, String tempInput){
		view.getMainTable().removeAll();
		TableItem mainItem;
		ArrayList<Task> tasks = (ArrayList<Task>)items.get("Tasks");
		ArrayList<String> freeSlots = new ArrayList<String>();
		ArrayList<String> replace = (ArrayList<String>)items.get("replace");
		if(items.get("free")!=null){
			freeSlots = (ArrayList<String>) items.get("free");
		}
		tasks = Sorter.sortByDate(tasks);
		
		setSearchHeading(tempInput);
		
		if(suggestionWords(replace)){
			mainItem = new TableItem(view.getMainTable(),SWT.NONE);
		}
		
		setSearchTasks(tasks);
		mainItem = new TableItem(view.getMainTable(),SWT.NONE);
		setFreeSlots(freeSlots);
	}
	
	private void setSearchHeading(String tempInput){
		TableItem mainItem = new TableItem(view.getMainTable(),SWT.NONE);
		mainItem.setText("Search Results : " + parser.removeFirstWord(tempInput));
		mainItem.setFont(View.headingFont);
		mainItem.setForeground(View.orangeColor);
	}
	
	private boolean suggestionWords(ArrayList<String> replace){
		TableItem mainItem = new TableItem(view.getMainTable(), SWT.NONE);
		boolean exist = false;
		String form = "";
		
		if(replace.size()>1){
			for(String s : replace){
				form+=s+" ";
			}
			mainItem.setText(form+"?");
			mainItem.setFont(View.italicFont);
			mainItem.setForeground(View.orangeColor);
			exist= true;
		}
		return exist;
	}
	
	private void setSearchTasks(ArrayList<Task> tasks){
		TableItem mainItem = new TableItem(view.getMainTable(),SWT.NONE);
		mainItem.setText(TASK_HEADING);
		mainItem.setFont(View.headingFont);
		
		if(tasks.isEmpty()){
			mainItem.setForeground(View.missingColor);
		} else {
			mainItem.setForeground(View.orangeColor);
		}
		
		for(int i = 0 ; i <tasks.size() ; i++){
			mainItem = new TableItem(view.getMainTable(),SWT.NONE);
			mainItem.setText(tasks.get(i).getUserFormat());
			mainItem.setFont(View.normalFont);
		}
	}
	
	private void setFreeSlots(ArrayList<String> freeSlots){
		TableItem mainItem = new TableItem(view.getMainTable(),SWT.NONE);
		mainItem.setText(FREE_HEADING);
		mainItem.setFont(View.headingFont);
		
		if(freeSlots.isEmpty()){
			mainItem.setForeground(View.missingColor);
		} else {
			mainItem.setForeground(View.orangeColor);
		}
		
		for(int i = 0 ; i < freeSlots.size() ; i++){
			mainItem = new TableItem(view.getMainTable(),SWT.NONE);
			mainItem.setText(freeSlots.get(i));
			mainItem.setFont(View.normalFont);
		}
	}
}
