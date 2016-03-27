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

		Calendar today = Calendar.getInstance();
		today.add(Calendar.DAY_OF_MONTH, 1);
		
		for (int i = STARTING_INDEX; i < days.length; i++) {
			today.add(Calendar.DAY_OF_MONTH, 1);
			days[i] = DEFAULT_DAYS[(index + i) % (NUMBER_OF_DAYS)] + " - " +  getHeaderFormat(today.getTime());
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

		ArrayList<String> categories = Logic.getListOfCatWithCount();
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
				boolean added = false;
				Date startTaskDate = task.getDatetimes().get(Keywords.INDEX_STARTDATE);
				Date endTaskDate = task.getDatetimes().get(Keywords.INDEX_ENDDATE);
				
				int lengthOfDays = 0;
				
				if(startTaskDate !=null && endTaskDate!=null){
					Calendar convertStart = Calendar.getInstance();
					convertStart.set(getCurrentYear(), startTaskDate.getMonth(), startTaskDate.getDate());
					
					Calendar convertEnd = Calendar.getInstance();
					convertEnd.set(getCurrentYear(), endTaskDate.getMonth(), endTaskDate.getDate());
					
					lengthOfDays = calculateDiffInDay(convertStart, convertEnd);
				}
				
				if (startTaskDate == null) {
					insertToHashMap(OTHERS, task);
				} else {
	
					for(int i = 0; i < lengthOfDays+1; i++){
						
						Calendar compareCalendar = Calendar.getInstance();
						compareCalendar.set(getCurrentYear(), startTaskDate.getMonth(), startTaskDate.getDate());
						compareCalendar.add(Calendar.DAY_OF_MONTH, i);
						
						int diffInDay = calculateDiffInDay(Calendar.getInstance(),compareCalendar);
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
								insertToHashMap(DEFAULT_DAYS[(tempDate.getDay()+1) %7] + " - " +  getHeaderFormat(tempDate), task);
							}
						} else {
							insertToHashMap(OTHERS, task);
							break;
						}
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
		
		// http://git.eclipse.org/c/platform/eclipse.platform.swt.git/tree/examples/org.eclipse.swt.snippets/src/org/eclipse/swt/snippets/Snippet320.java
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
						if (result instanceof LinkedList<?>) {
							displayHelp((LinkedList<String>)result);
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
					
					if (view.getPopupShell().isVisible() && view.getPopupTable().getSelectionIndex() != -1) {
						input.setText(view.getPopupTable().getSelection()[0].getText());
						view.getPopupShell().setVisible(false);
					}

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
					if (isTextEmpty(input)) {
						inputToHint();
					}
					break;
				case SWT.ARROW_DOWN:
					int index = (view.getPopupTable().getSelectionIndex() + 1) % view.getPopupTable().getItemCount();
					view.getPopupTable().setSelection(index);
					event.doit = false;
					break;
				case SWT.ARROW_UP:
					index = view.getPopupTable().getSelectionIndex() - 1;
					if (index < 0) index = view.getPopupTable().getItemCount() - 1;
					view.getPopupTable().setSelection(index);
					event.doit = false;
					break;
				case SWT.ESC:
					view.getPopupShell().setVisible(false);
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

		
		setAutoComplete();
		
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

	public String getHeaderFormat(Date date){
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

	private boolean isTextEmpty(StyledText t) {
		return t.getText().length() == 0;
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

	private boolean insertIntoTable(String key, boolean week) {
		if (putIntoDays.containsKey(key)) {
			if (week) {
				TableItem headerItem = new TableItem(view.getMainTable(), SWT.NONE);
				headerItem.setText(key);
				headerItem.setFont(View.normalFont);
				headerItem.setForeground(View.orangeColor);
			}
			ArrayList<Task> tempArrList = putIntoDays.get(key);
			for (int i = 0; i < tempArrList.size(); i++) {
				final TableItem mainItem = new TableItem(view.getMainTable(), SWT.NONE);
				String whiteSpaces = "";
				if (tempArrList.get(i).getPriority() == 1) {
					mainItem.setData(object);
					whiteSpaces = "      ";
				}

				if (week) {
					mainItem.setText(whiteSpaces + tempArrList.get(i).getUserFormatNoDate());
				} else {
					
					final TextLayout textLayout = new TextLayout(Display.getCurrent());  
					String text = whiteSpaces + tempArrList.get(i).getUserFormat();
					textLayout.setText(text);
					
					TextStyle styleDescription = new TextStyle(View.normalFont, null, null);
					TextStyle styleDate = new TextStyle(View.normalFont, View.redColor, null);
					
					if(tempArrList.get(i).getDatetimes().get(0)!= null){
						int seperatingIndex = whiteSpaces.length() + tempArrList.get(i).getUserFormatNoDate().length();
						textLayout.setStyle(styleDescription, 0, seperatingIndex);
						textLayout.setStyle(styleDate, seperatingIndex + 3, text.length());
					} else {
						textLayout.setStyle(styleDescription, 0, text.length());
					}
					
					view.getMainTable().addListener(SWT.PaintItem, new Listener() {
					      public void handleEvent(Event event) {
					    	  if(event.item.equals(mainItem)){
					    		  textLayout.draw(event.gc, event.x, event.y);
					    	  }
					      }
					});
					
					final Rectangle textLayoutBounds = textLayout.getBounds();
					    view.getMainTable().addListener(SWT.MeasureItem, new Listener() {
					      public void handleEvent(Event e) {
					    	  if(e.item.equals(mainItem)){
						        e.width = textLayoutBounds.width + 2;
						        e.height = textLayoutBounds.height + 2;
					    	  }
					      }
					});
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
		Logic.updateFile();
	}
	
	// Keep the arraylist at size 4
	public void setAutoComplete(){
			
		view.getInput().addListener(SWT.Modify, event -> {
			String string = view.getInput().getText();
			if (string.length() == 0) {
				view.getPopupShell().setVisible(false);
			} else {
				// TODO : change to logic arraylist? keep at size 4, use variable string to pass stuff
				TableItem[] items = view.getPopupTable().getItems();
				// currently this is the way to get suggested categories
				ArrayList<String> logicArrList = Storage.getAllCategories();
				//logicArrList.add("something");
				for (int i = 0; i < logicArrList.size(); i++) {
					items[i].setText(logicArrList.get(i));
				}
				Rectangle textBounds = Display.getCurrent().map(view.getShell(), null, view.getInput().getBounds());
				view.getPopupShell().setBounds(textBounds.x, textBounds.y + textBounds.height, textBounds.width, 120);
				view.getPopupShell().setVisible(true);
			}
		});
		
		view.getPopupTable().addListener(SWT.DefaultSelection, event -> {
			view.getInput().setText(view.getPopupTable().getSelection()[0].getText());
			view.getPopupShell().setVisible(false);
		});
		
		view.getPopupTable().addListener(SWT.KeyDown, event -> {
			if (event.keyCode == SWT.ESC) {
				view.getPopupShell().setVisible(false);
			}
		});

		Listener focusOutListener = event -> Display.getCurrent().asyncExec(() -> {
			if (Display.getCurrent().isDisposed()) return;
			Control control = Display.getCurrent().getFocusControl();
			if (control == null || (control != view.getInput() && control != view.getPopupTable())) {
				view.getPopupShell().setVisible(false);
			}
		});
		
		view.getPopupTable().addListener(SWT.FocusOut, focusOutListener);
		view.getInput().addListener(SWT.FocusOut, focusOutListener);
		view.getShell().addListener(SWT.Move, event -> view.getPopupShell().setVisible(false));
	}

	public View getView() {
		return view;
	}
}
