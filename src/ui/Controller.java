package ui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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

	private static final int WRAP_AROUND = 40;

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
	private final static int WHITESPACES = 1;

	private View view;
	private Parser parser;
	private Logic logic;
	private Storage storage;

	public Controller() throws Exception {
		view = new View();
		starImage = new Image(Display.getCurrent(),
				Thread.currentThread().getContextClassLoader().getResourceAsStream(MARK_FILE_PATH));
		warningImage = new Image(Display.getCurrent(),
				Thread.currentThread().getContextClassLoader().getResourceAsStream(WARNING_FILE_PATH));

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

		for (int i = STARTING_INDEX; i < days.length; i++) {
			days[i] = DEFAULT_DAYS[(index + i) % (NUMBER_OF_DAYS)];
		}

		timer();
		inputToHint();
		addKeyListener();
		view.getDateLabel().setText(getCurrentDate());
		view.getDayLabel().setText(getCurrentDay());
		view.getTimeLabel().setText(getCurrentTime());

		readFileLocation();
		if (Keywords.FILENAME_FILEPATH.equals(Keywords.EMPTY_STRING)) {
			writePathToFile();
		}

		parser = parser.getInstance();
		logic = logic.getInstance();
		storage = storage.getInstance();

		displayCategory();
		displayList(Logic.getUncompletedTasks());
	}

	private void displayNotification(Notification notify) {

		Label tip = view.getNotification();
		tip.setText(notify.getTitle() + " " + notify.getMessage());
		//Notification.clear();
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

		for (Task task : list) {

			/*
			 * String formattedOutput = WordUtils.wrap(taskIDandDesc,
			 * WRAP_AROUND, "\n", true); String outputArray[] =
			 * formattedOutput.split("\n"); for (int j = 0; j <
			 * outputArray.length; j++) {
			 * 
			 * mainItem = new TableItem(view.getMainTable(), SWT.NONE);
			 * 
			 * if (j == 0) { mainItem.setText(outputArray[j]); } else { // Add
			 * necessary white space to align tasks mainItem.setText(" " +
			 * outputArray[j]); } }
			 */

			// TODO change this shit to more than 1
			Date taskDate = task.getDatetimes().get(0);

			if (taskDate == null) {
				insertToHashMap(OTHERS, task);
			} else {

				Calendar compareCalendar = Calendar.getInstance();
				compareCalendar.set(getCurrentYear(), taskDate.getMonth(), taskDate.getDate());

				int diffInDay = calculateDiffInDay(compareCalendar);
				compareCalendar.set(getCurrentYear(), taskDate.getMonth(), taskDate.getDate());

				if (diffInDay == -1) {
					insertToHashMap(OVERDUE, task);
				} else if (diffInDay < 7) {
					if (diffInDay == 0) {
						insertToHashMap(TODAY, task);
					} else if (diffInDay == 1) {
						insertToHashMap(TOMORROW, task);
					} else {
						insertToHashMap(DEFAULT_DAYS[taskDate.getDay()], task);
					}
				} else {
					insertToHashMap(OTHERS, task);
				}
			}
		}

		if (!insertIntoTable(OVERDUE, false)) {
			firstItem.setForeground(View.missingColor);
			view.getMainTable().removeListener(SWT.MeasureItem, paintListener);
			view.getMainTable().removeListener(SWT.PaintItem, paintListener);
		}
		mainItem = new TableItem(view.getMainTable(), SWT.NONE);
		boolean thirdItem = false;

		for (String day : days) {

			if (day.equals(TODAY)) {

				mainItem = new TableItem(view.getMainTable(), SWT.NONE);
				mainItem.setText(day);
				mainItem.setFont(View.headingFont);
				mainItem.setForeground(View.orangeColor);

				if (!insertIntoTable(day, false)) {
					mainItem.setForeground(View.missingColor);
				}
				mainItem = new TableItem(view.getMainTable(), SWT.NONE);

			} else if (day.equals(TOMORROW)) {

				mainItem = new TableItem(view.getMainTable(), SWT.NONE);
				mainItem.setText(day);
				mainItem.setFont(View.headingFont);
				mainItem.setForeground(View.orangeColor);
				thirdItem = true;
				if (!insertIntoTable(day, false)) {
					mainItem.setForeground(View.missingColor);
				}
				mainItem = new TableItem(view.getMainTable(), SWT.NONE);

			} else if (thirdItem) {

				mainItem = new TableItem(view.getMainTable(), SWT.NONE);
				mainItem.setText(WEEK);
				mainItem.setFont(View.headingFont);
				mainItem.setForeground(View.missingColor);
				thirdItem = false;
				if (insertIntoTable(day, true)) {
					mainItem.setForeground(View.orangeColor);
				}

			} else {

				if (insertIntoTable(day, true)) {
					mainItem.setForeground(View.orangeColor);
				}
			}

		}

		mainItem = new TableItem(view.getMainTable(), SWT.NONE);
		mainItem = new TableItem(view.getMainTable(), SWT.NONE);
		mainItem.setText(OTHERS);
		mainItem.setFont(View.headingFont);
		mainItem.setForeground(View.orangeColor);
		if (!insertIntoTable(OTHERS, false)) {
			mainItem.setForeground(View.missingColor);
		}

	}

	private void displayHelp() {

		Table mainTable = view.getMainTable();
		mainTable.removeAll();

		TableItem mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(View.headingFont);
		mainItem.setForeground(View.orangeColor);
		mainItem.setText("Adding tasks");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setText("add <TODO>");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setText("add <TODO> (at | by | on | to) <date> [#category]");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(View.italicFont);
		mainItem.setText("Eg. add do CS2103 tutorial by Sun");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(View.italicFont);
		mainItem.setText("      add buy milk by 15Feb #shopping");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(View.headingFont);
		mainItem.setForeground(View.orangeColor);
		mainItem.setText("Edit tasks");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setText("edit <task_ID#> to <date>");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(View.italicFont);
		mainItem.setText("Eg. edit 1 to 15Feb");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(View.headingFont);
		mainItem.setForeground(View.orangeColor);
		mainItem.setText("Set priority");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setText("mark <task_ID#>");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(View.italicFont);
		mainItem.setText("Eg. set 2 to 10");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(View.headingFont);
		mainItem.setForeground(View.orangeColor);
		mainItem.setText("Complete Tasks");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setText("do <TODO | task_ID#>");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(View.italicFont);
		mainItem.setText("Eg. do receive quest");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(View.italicFont);
		mainItem.setText("      do 1");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(View.headingFont);
		mainItem.setForeground(View.orangeColor);
		mainItem.setText("View tasks");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setText("view <category>");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(View.italicFont);
		mainItem.setText("Eg. view shopping");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(View.headingFont);
		mainItem.setForeground(View.orangeColor);
		mainItem.setText("Show help");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setText("[? | help | h]");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(View.headingFont);
		mainItem.setForeground(View.orangeColor);
		mainItem.setText("Undo previous command");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setText("[u | undo]");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(View.headingFont);
		mainItem.setForeground(View.orangeColor);
		mainItem.setText("Keyboard shortcuts");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setText("Undo previous command: Ctrl+Z");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setText("Scroll through command history: \u2191 or \u2193");

	}

	private void addKeyListener() {

		StyledText input = view.getInput();
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

				switch (event.keyCode) {

				case SWT.KEYPAD_CR:
				case SWT.CR:
					// SWT.CR : when "ENTER" key is pressed
					String tempInput = input.getText();
					inputToHint();

					Object result = parser.parse(tempInput);
					displayCategory();

					try {
						if (result instanceof String) {
							displayHelp();
						} else if (result instanceof ArrayList<?>) {
							// here might need handle is empty arraylist
							ArrayList<Object> re = (ArrayList<Object>) result;
							displayNotification((Notification) re.get(0));
							displayList((ArrayList<Task>) re.get(1));
						} else {
							displayList(Logic.getUncompletedTasks());
							displayNotification((Notification) result);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}


					break;
				case SWT.ARROW_UP:
					view.getMainTable().setTopIndex(view.getMainTable().getTopIndex() - View.SCROLL_AMOUNT);
					event.doit = false;
					break;
				case SWT.ARROW_DOWN:
					view.getMainTable().setTopIndex(view.getMainTable().getTopIndex() + View.SCROLL_AMOUNT);
					event.doit = false;
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

	private int calculateDiffInDay(Calendar endDate) {
		Calendar currDate = Calendar.getInstance();
		int daysBetween = -1;
		endDate.add(Calendar.DAY_OF_MONTH, 1);
		while (currDate.before(endDate)) {
			currDate.add(Calendar.DAY_OF_MONTH, 1);
			daysBetween++;
		}
		return daysBetween;
	}

	private boolean insertIntoTable(String key, boolean week) {

		TableItem mainItem;
		if (putIntoDays.containsKey(key)) {
			if (week) {
				mainItem = new TableItem(view.getMainTable(), SWT.NONE);
				DateFormat dayFormat = new SimpleDateFormat(Keywords.FORMAT_HEADER);
				mainItem.setText(key + " - " + dayFormat.format(putIntoDays.get(key).get(0).getDatetimes().get(0)));
				mainItem.setFont(View.normalFont);
				mainItem.setForeground(View.orangeColor);
			}
			ArrayList<Task> tempArrList = putIntoDays.get(key);
			for (int i = 0; i < tempArrList.size(); i++) {
				mainItem = new TableItem(view.getMainTable(), SWT.NONE);
				String whiteSpaces = "";
				if (tempArrList.get(i).getPriority() == 1) {
					mainItem.setData(object);
					whiteSpaces = "     ";
				}

				if (week) {
					mainItem.setText(whiteSpaces + removeDate(tempArrList.get(i).getUserFormat()));
				} else {
					mainItem.setText(whiteSpaces + tempArrList.get(i).getUserFormat());
				}
			}
			return true;
		}
		return false;
	}

	private void insertToHashMap(String key, Task value) {
		ArrayList<Task> toAddList = new ArrayList<Task>();

		if (putIntoDays.containsKey(key)) {
			toAddList = putIntoDays.get(key);
		}

		toAddList.add(value);
		putIntoDays.put(key, toAddList);
	}

	private String removeDate(String removeDate) {
		return removeDate.substring(0, removeDate.lastIndexOf("-") - WHITESPACES);
	}

	private void readFileLocation() {
		BufferedReader bufferReader = null;
		try {
			bufferReader = new BufferedReader(new FileReader(Keywords.SETTINGS_FILEPATH));
			String currentLine = Keywords.EMPTY_STRING;
			while ((currentLine = bufferReader.readLine()) != null) {
				// Read first line as file location is there
				Keywords.FILENAME_FILEPATH = currentLine;
				break;
			}

		} catch (FileNotFoundException ex) {
			// systemPrint(FILE_NOT_FOUND_ERROR_MSG);
		} catch (IOException ex) {
			// systemPrint(IO_ERROR_MSG);
		} finally {
			try {
				if (bufferReader != null) {
					bufferReader.close();
				}
			} catch (IOException ex) {
				// systemPrint(IO_ERROR_MSG);
			}
		}
	}

	public void writePathToFile() {
		DirectoryDialog dialog = new DirectoryDialog(view.getShell());
		dialog.setFilterPath("c:\\"); // Windows specific
		String path = dialog.open();
		if (path == null) {
			Keywords.FILENAME_FILEPATH = Keywords.TASK_FILENAME;
		} else {
			Keywords.FILENAME_FILEPATH = path + "\\" + Keywords.TASK_FILENAME;
		}

		try {
			BufferedWriter bufferWriter = new BufferedWriter(new FileWriter(Keywords.SETTINGS_FILEPATH));
			bufferWriter.write(Keywords.FILENAME_FILEPATH);
			// bufferWriter.newLine();
			bufferWriter.close();
		} catch (IOException ex) {
			// systemPrint(IO_ERROR_MSG);
		}
	}

	public View getView() {
		return view;
	}
}
