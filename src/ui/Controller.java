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
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.graphics.TextStyle;
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
	private final static int IMAGE_PADDING = 8;
	private final static int TEXT_PADDING = 1;
	
	private final static String TASK_HEADING = "Task(s)";
	private final static String FREE_HEADING = "Free";
	private final static String BUSY_HEADING = "Busiest Day(s)";

	private final static String DELIMITER = " - ";
	
	private final static String PRIORITY_WHITESPACES = "      ";
	
	private View view;
	private Parser parser;
	private Logic logic;
	private Storage storage;
	
	// To set the scroll index when new task is added, edited or marked
	private int lastIndex = -1;
	private int setIndex = -1;
	
	private int imageWidth = 0;
	
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
	
	/**
	 * Initializes the days array based on current day
	 */
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
		
		TableItem forStarSize = new TableItem(view.getMainTable(), SWT.NONE);
		forStarSize.setFont(View.headingFont);
		TableItem forWarningSize = new TableItem(view.getMainTable(), SWT.NONE);
		
		starImage = new Image(Display.getCurrent(),
				Thread.currentThread().getContextClassLoader().getResourceAsStream(MARK_FILE_PATH));
		warningImage = new Image(Display.getCurrent(),
				Thread.currentThread().getContextClassLoader().getResourceAsStream(WARNING_FILE_PATH));
		
		starImage = resize(starImage, forStarSize.getBounds().height - IMAGE_PADDING, forStarSize.getBounds().height - IMAGE_PADDING);
		warningImage = resize(warningImage, forWarningSize.getBounds().height- IMAGE_PADDING, forWarningSize.getBounds().height- IMAGE_PADDING);
		imageWidth = forStarSize.getBounds().height;
		
		view.getMainTable().removeAll();
	}
	
	private Image resize(Image image, int width, int height) {
		Image scaled = new Image(Display.getDefault(), width, height);
		GC gc = new GC(scaled);
		gc.setAntialias(SWT.ON);
		gc.setInterpolation(SWT.HIGH);
		gc.drawImage(image, 0, 0, 
		image.getBounds().width, image.getBounds().height, 
		0, 0, width, height);
		gc.dispose();
		image.dispose(); 
		return scaled;
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
			for (int z = 0; z < ViewTask.getCurrCat().size(); z++) {
				String compareCategory = categories.get(i);
				compareCategory = compareCategory.substring(0, compareCategory.indexOf(Keywords.SPACE_STRING));

				if (ViewTask.getCurrCat().get(z).equals(compareCategory)) {
					categoryItem.setBackground(View.newColor);
					break;
				}
			}
		}
		setCategoryText();
	}

	private void setCategoryText(){
		final TextLayout textLayout = new TextLayout(Display.getCurrent());

		// Drawing the style (for colors)
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

		// Need this as we are drawing the style
		view.getCategoryTable().addListener(SWT.EraseItem, new Listener() {
			public void handleEvent(Event event) {
				event.detail &= ~SWT.FOREGROUND;
				// MouseOver:
				event.detail &= ~SWT.HOT;
			}
		});
	}
	
	/**
	 * Get the length of days from startTaskDate to endTaskDate after
	 * converting them to Calendar type.
	 * 
	 * @param startTaskDate
	 *            the starting task date
	 * @param endTaskDate
	 *            the ending task date
	 * @return the length of days
	 */
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

		if (!insertKeyToTable(OVERDUE, false)) {
			firstItem.setForeground(View.missingColor);
			unpaintOverdueIcon(paintListener);
		}
		
		mainItem = new TableItem(view.getMainTable(), SWT.NONE);
		mainItem.setText("                                                                                                  ");
		lastIndex++;
		
		insertKeysToTable();
		view.getMainTable().setTopIndex(setIndex);
	}
	
	/**
	 * Insert task to hashmap based on their day (key) for viewing purpose
	 * 
	 * @param task
	 *            the task to be inserted
	 */
	private void insertKeysToHashMap(Task task){
		
		Date startTaskDate = task.getDateTimes().get(Keywords.INDEX_STARTDATE);
		Date endTaskDate = task.getDateTimes().get(Keywords.INDEX_ENDDATE);
		
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
	
	private void insertKeysToTable(){
		boolean thirdItem = false;
		TableItem mainItem = null;
		
		for (String day : days) {

			if (day.equals(TODAY)) {

				mainItem = new TableItem(view.getMainTable(), SWT.NONE);
				setTableHeading(mainItem, day);

				if (!insertKeyToTable(day, false)) {
					mainItem.setForeground(View.missingColor);
				}

				addSpacing();

			} else if (day.equals(TOMORROW)) {

				mainItem = new TableItem(view.getMainTable(), SWT.NONE);
				setTableHeading(mainItem, day);
				thirdItem = true;
				
				if (!insertKeyToTable(day, false)) {
					mainItem.setForeground(View.missingColor);
				}
				
				addSpacing();

			} else if (thirdItem) {

				mainItem = new TableItem(view.getMainTable(), SWT.NONE);
				setTableHeading(mainItem, WEEK);
				thirdItem = false;
				if (!insertKeyToTable(day, true)) {
					mainItem.setForeground(View.missingColor);
				}

			} else {

				if (insertKeyToTable(day, true)) {
					mainItem.setForeground(View.orangeColor);
				}
			}

		}

		addSpacing();
		mainItem = new TableItem(view.getMainTable(), SWT.NONE);
		setTableHeading(mainItem, OTHERS);
		
		if (!insertKeyToTable(OTHERS, false)) {
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
							event.height = Math.max(event.height, rect.height);
							break;
						}
						case SWT.PaintItem: {
							int x = 7;
							Rectangle rect = starImage.getBounds();
							int offset = Math.max(0, (event.height - rect.height) / 2);
							event.gc.drawImage(starImage, x, event.y + offset);
							break;
						}
					}
				}
			}
		};
		
		view.getMainTable().addListener(SWT.MeasureItem, paintStarListener);
		view.getMainTable().addListener(SWT.PaintItem, paintStarListener);
	}

	/**
	 * Paint the overdue icon
	 * 
	 * @param firstItem
	 *            the first item in the table
	 * @return the paint listener of the icon
	 */
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
	
	/**
	 * Unpaint overdue icon when table refresh
	 * 
	 * @param paintListener
	 *            the paintListener to be removed
	 */
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
	
	/**
	 * Add key listener to the input box.
	 */
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
			@SuppressWarnings("unchecked")//all instanceof checks are valid
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
	
	/**
	 * Create a timer to update the time every second
	 */
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

	public int getCurrentYear() {
		Date date = new Date();
		DateFormat dayFormat = new SimpleDateFormat(Keywords.FORMAT_YEAR);
		return Integer.parseInt(dayFormat.format(date));
	}
	
	/**
	 * Get the header format (dd MMM) from the date parameter
	 * 
	 * @param date
	 *            the date to be formatted
	 * @return the header format date
	 */
	public String getHeaderFormat(Date date) {
		DateFormat dayFormat = new SimpleDateFormat(Keywords.FORMAT_HEADER);
		return dayFormat.format(date);
	}

	private void inputToHint() {
		view.getInput().setText(View.GUI_HINT);
		view.getInput().setForeground(View.hintColor);
	}

	private void inputToNormal() {
		view.getInput().setText(View.EMPTY_STRING);
		view.getInput().setForeground(View.normalColor);
	}

	/**
	 * Get the length of days from startDate to endDate 
	 * 
	 * @param startDate
	 *            the starting date
	 * @param endDate
	 *            the ending date
	 * @return the length of days, -1 if endDate is earlier
	 */
	private int calculateDiffInDay(Calendar startDate, Calendar endDate) {
		int daysBetween = -1;
		endDate.add(Calendar.DAY_OF_MONTH, 1);
		while (startDate.before(endDate)) {
			startDate.add(Calendar.DAY_OF_MONTH, 1);
			daysBetween++;
		}
		return daysBetween;
	}

	private void setTableHeading(TableItem headerItem, String heading){
		headerItem.setText(heading);
		headerItem.setFont(View.headingFont);
		headerItem.setForeground(View.orangeColor);
		lastIndex++;
	}
	
	private void setTableSubHeading(String subHeading){
		TableItem headerItem = new TableItem(view.getMainTable(), SWT.NONE);
		headerItem.setText(subHeading);
		headerItem.setFont(View.normalFont);
		headerItem.setForeground(View.orangeColor);
		lastIndex++;
	}
	
	private void addSpacing(){
		new TableItem(view.getMainTable(), SWT.NONE);
		lastIndex++;
	}
	
	/**
	 * Inserts a single key with their values from the hashmap to the table. 
	 * Check if it is under "Week" for interface.
	 * 
	 * @param key
	 *            the key in the hashmap
	 * @param week
	 *            true if the task is within the week else false
	 * @return true if the key is successfully added else false
	 */
	private boolean insertKeyToTable(String key, boolean week) {
		if (putIntoDays.containsKey(key)) {
			
			if (week) {
				setTableSubHeading(key);
			}

			ArrayList<Task> tempArrList = putIntoDays.get(key);

			ArrayList<Task> lastTasks = Logic.peekLastTask();

			initTextLayout(tempArrList, lastTasks, week);
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Initialize the text layout for colors and formatting of text
	 *
	 * @param tempArrList
	 *            the arraylist of tasks
	 * @param lastTasks
	 *            the last few tasks that are affected by the last command
	 * @param week
	 *            true if the task is within the week else false
	 */
	private void initTextLayout(ArrayList<Task> tempArrList, ArrayList<Task> lastTasks, boolean week){
		
		for (int i = 0; i < tempArrList.size(); i++) {
			Task task = tempArrList.get(i);
			final TableItem mainItem = new TableItem(view.getMainTable(), SWT.NONE);
			lastIndex++;
			String whiteSpaces = setWhiteSpaces(task, mainItem);

			// To highlight added or edited tasks
			highlightTasks(lastTasks, mainItem, task.getId(), i);

			String text = task.getUserFormat();

			if (week) {

				text = task.getUserFormatNoDate() + Keywords.SPACE_STRING
						+ task.getDisplayTimeRange();
			}

			final TextLayout textLayout = new TextLayout(Display.getCurrent());

			textLayout.setText(text);
			setLayoutStyle(task, textLayout, text, whiteSpaces, week);
			
			drawTextLayout(mainItem, textLayout, task.getPriority(), week);
		}
		
	}
	
	/**
	 * Takes in an arraylist of task and highlight it 
	 * if it is newly added or conflicts with other tasks.
	 *
	 * @param lastTasks
	 *            the arraylist of tasks
	 * @param mainItem
	 *            the table item to be highlighted
	 * @param taskId
	 *            compare with this task id
	 * @param index
	 *            index of the task         
	 */
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
		
		for(int i = 0; i<Logic.getConflicting().size(); i++ ){
			if(Logic.getConflicting().get(i).getId()==taskId){
				mainItem.setBackground(View.redColor);
				mainItem.setFont(View.boldFont);
			}
		}
	}
	
	/**
	 * Set the text layout style 
	 *
	 * @param task
	 *            task to be styled
	 * @param textLayout
	 *            textlayout of the task
	 * @param text
	 *            text of the task
	 * @param whiteSpaces
	 *            whitespaces infront of task
	 * @param week
	 *            true if the task is within the week else false       
	 */
	private void setLayoutStyle(Task task, TextLayout textLayout, String text, String whiteSpaces, boolean week){
		TextStyle styleDescription = new TextStyle(View.normalFont, null, null);
		TextStyle styleDate = new TextStyle(View.boldFont, View.dateColor, null);
		TextStyle styleCategory = new TextStyle(View.normalFont, View.greenColor, null);

		if (task.getDateTimes().get(0) != null) {
			int seperatingIndex = whiteSpaces.length() + task.getUserFormatNoDate().length();
			textLayout.setStyle(styleDescription, 0, seperatingIndex);
			if (week) {
				textLayout.setStyle(styleDate, seperatingIndex, text.length());
			} else {
				textLayout.setStyle(styleDate, seperatingIndex + DELIMITER.length(), text.length());
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
	
	/**
	 * Set the whitespaces infront of prioritized task for icon.
	 * 
	 * @param task
	 *            the task to be edited
	 * @param mainItem
	 *            the task's table item
	 * @return whitespaces if the task is prioritized     
	 */
    private String setWhiteSpaces(Task task, TableItem mainItem){
    	if (task.getPriority() == 1) {
			mainItem.setData(object);
			return PRIORITY_WHITESPACES;
		}
    	return Keywords.EMPTY_STRING;
    }
	
    
	private void drawTextLayout(TableItem mainItem, TextLayout textLayout, int priority, boolean week){
		view.getMainTable().addListener(SWT.PaintItem, new Listener() {
			public void handleEvent(Event event) {
				if (event.item.equals(mainItem)) {
					int padding = 0;
					
					if(week){
						padding = TEXT_PADDING;
					}
					
					if (priority == 1){
						textLayout.draw(event.gc, event.x + imageWidth, event.y + padding);
					} else {
						textLayout.draw(event.gc, event.x, event.y + padding);
					}
				}
			}
		});

		final Rectangle textLayoutBounds = textLayout.getBounds();
		view.getMainTable().addListener(SWT.MeasureItem, new Listener() {
			public void handleEvent(Event e) {
				if (e.item.equals(mainItem)) {
					e.width = textLayoutBounds.width;
					e.height = textLayoutBounds.height;
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
			// Path of current tasks' file is stored in the settings file
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

	/**
	 * Alt + E will prompt this method to save the tasks' file into a new location.
	 */
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
	
	/**
	 * Display search results.
	 * 
	 * @param items
	 *            the tasks, replace words and freeslots found
	 * @param tempInput
	 *            user's current input     
	 */
	@SuppressWarnings("unchecked")
	//it is safe as the HashMap tag with "Tasks" is an ArrayList<Task>
	//it is safe as the HashMap tag with "replace" is an ArrayList<String>
	//it is safe as the HashMap tag with "busiest" is an ArrayList<String>
	//it is safe as the HashMap tag with "free" is an ArrayList<String>
	private <T extends ArrayList<?>> void  displaySearch(HashMap<String,Object> items, String tempInput){
		view.getMainTable().removeAll();
		TableItem mainItem;
		ArrayList<Task> tasks = (ArrayList<Task>)items.get("Tasks");
		ArrayList<String> freeSlots = new ArrayList<String>();
		ArrayList<String> replace = (ArrayList<String>)items.get("replace");
		ArrayList<String> busiest = new ArrayList<String>();
		if(items.get("free")!=null){
			freeSlots = (ArrayList<String>) items.get("free");
		}
		tasks = Sorter.sortByDate(tasks);
		
		setSearchHeading(tempInput);
		
		if(suggestionWords(replace)){
			mainItem = new TableItem(view.getMainTable(),SWT.NONE);
		}

		if(items.get("busiest")!=null){
			busiest = (ArrayList<String>) items.get("busiest");
			setBusiestDays(busiest);
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
	
	/**
	 * Set suggested words
	 * 
	 * @param replace
	 *         an arraylist of replace words.
	 * @return true if there are any suggested words.  
	 */
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
	
	private void setBusiestDays(ArrayList<String> busiestDays){

		TableItem mainItem = new TableItem(view.getMainTable(),SWT.NONE);
		mainItem.setText(BUSY_HEADING+busiestDays.get(0));
		mainItem.setFont(View.headingFont);
		
		if(busiestDays.isEmpty()){
			mainItem.setForeground(View.missingColor);
		} else {
			mainItem.setForeground(View.orangeColor);
		}
		
		for(int i = 1 ; i < busiestDays.size() ; i++){
			mainItem = new TableItem(view.getMainTable(),SWT.NONE);
			mainItem.setText(busiestDays.get(i));
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
	
	public View getView() {
		return view;
	}
}
