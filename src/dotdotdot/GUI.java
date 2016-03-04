package dotdotdot;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolTip;

import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.SwingUtilities;

import org.apache.commons.lang3.text.WordUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Label;

public class GUI {
	private static Text input;
	private static Table categoryTable;
	private static Table mainTable;
	private static TableItem categoryItem;
	private static TableItem mainItem;
	private static Label timeLabel;
	private static Parser parser = new Parser();
	private static ArrayList<String> list;
	private static int borderSize;

	private static final String GUI_TITLE = "Dotdotdot";
	private static final String GUI_HINT = "< Input ? or help to show available commands >";
	private static final String EMPTY_STRING = "";
	private static final String SPACE_STRING = " ";
	private static final int SCROLL_AMOUNT = 5;
	private static final int WRAP_AROUND = 40;
	private static final int BORDER_WIDTH = 2;
	private static final int TASK_ID = 0;
	private static final int TASK_DESC = 1;
	private static final int DEFAULT_WHITESPACES = 3;
	
	private static Color hintColor;
	private static Color normalColor;

	private static void inputToHint() {
		input.setText(GUI_HINT);
		input.setForeground(hintColor);
	}

	private static void inputToNormal() {
		input.setText(EMPTY_STRING);
		input.setForeground(normalColor);
	}
	
	private static void displayCategory(){
		categoryTable.removeAll();
		// Call logic for list
		ArrayList<String> categories = parser.getLogic().getListOfCategoriesWithCount();
		for(int i =0 ; i < categories.size(); i++){
			categoryItem = new TableItem(categoryTable, SWT.NONE);
			categoryItem.setText(categories.get(i));
		}
	}

	private static void displayList() {

		mainTable.removeAll();
		for (int i = 0; i < list.size(); i++) {
	
			String[] taskIDandDesc = getTaskIdAndDesc(list.get(i));
			String formattedOutput = WordUtils.wrap(taskIDandDesc[TASK_DESC], WRAP_AROUND, "\n", true);
			String outputArray[] = formattedOutput.split("\n");
			for (int j = 0; j < outputArray.length; j++) {

				mainItem = new TableItem(mainTable, SWT.NONE);

				if (j == 0) {
					mainItem.setText(taskIDandDesc[TASK_ID] + " " +outputArray[j]);
				} else {
					String whiteSpaces = "";
					for(int z = 0 ; z < taskIDandDesc[TASK_ID].length() + DEFAULT_WHITESPACES; z++){
						whiteSpaces += " ";
					}
					mainItem.setText(whiteSpaces + outputArray[j]);
				}
			}
		}

	}

	private static void displayHelp() {

		mainTable.removeAll();

		mainItem = new TableItem(mainTable, SWT.NONE);
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
		mainItem.setText("Add priority");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setText("set <task_ID#> to <priority#>");
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

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		Display display = Display.getDefault();
		Shell shell = new Shell(SWT.CLOSE | SWT.TITLE | SWT.MIN);
		shell.setSize(725, 605);
		shell.setText(GUI_TITLE);
			
		hintColor = display.getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND);
		normalColor = display.getSystemColor(SWT.COLOR_BLACK);

		categoryTable = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		categoryTable.setBounds(10, 102, 180, 408);

		mainTable = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		mainTable.setBounds(203, 10, 506, 500);

		input = new Text(shell, SWT.BORDER);
		inputToHint();
		input.setBounds(10, 522, 699, 31);
		input.setFocus();

		final ToolTip tip = new ToolTip(shell, SWT.TOOL | SWT.ICON_INFORMATION | SWT.RIGHT);
		
		Date date = new Date();
		
		DateFormat dateFormat = new SimpleDateFormat("EEEEEEE");
		
		Label dayLabel = new Label(shell, SWT.NONE);
		dayLabel.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.BOLD));
		dayLabel.setAlignment(SWT.CENTER);
		dayLabel.setBounds(10, 10, 180, 31);
		dayLabel.setText(dateFormat.format(date));
		
		dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		Label dateLabel = new Label(shell, SWT.NONE);
		dateLabel.setAlignment(SWT.CENTER);
		dateLabel.setBounds(10, 41, 180, 31);
		dateLabel.setText(dateFormat.format(date));
		
		timeLabel = new Label(shell, SWT.NONE);
		timeLabel.setAlignment(SWT.CENTER);
		timeLabel.setBounds(10, 67, 180, 29);
		updateTime();
		timer();
			
		input.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent event) {

				switch (event.keyCode) {

				case SWT.CR:
					// SWT.CR : when "ENTER" key is pressed
					String tempInput = input.getText();
					inputToHint();
					tip.setVisible(false);
					tip.setMessage(EMPTY_STRING);
					parser.input(tempInput);
				
					if(parser.getIsViewOrHelp()==Parser.HELP_VIEW){
						displayHelp();
					} else {
						list = parser.getList();
						tip.setText(parser.getNotifyTitle());
							
						String notifyMsg = parser.getNotifyMsg();
	
						if(!notifyMsg.equals(EMPTY_STRING)){
							tip.setMessage(notifyMsg);
						}
						tip.setLocation(new Point(shell.getLocation().x + shell.getSize().x - parser.getMsgSize() ,
									shell.getLocation().y + borderSize));
						tip.setVisible(true);
						displayList();
						displayCategory();
					}
					break;
				case SWT.ARROW_UP:
					mainTable.setTopIndex(mainTable.getTopIndex() - SCROLL_AMOUNT);
					event.doit = false;
					break;
				case SWT.ARROW_DOWN:
					mainTable.setTopIndex(mainTable.getTopIndex() + SCROLL_AMOUNT);
					event.doit = false;
					break;
				case SWT.ESC:
					System.out.println(SWT.ESC);
					break;
				case SWT.BS:
					if (isTextEmpty(input)) {
						inputToHint();
					} else if (input.getForeground().equals(hintColor)) {
						inputToNormal();
					}
					break;
				default:
					// removes hint and changes input back to normal
					if (input.getForeground().equals(hintColor)) {
						inputToNormal();
					}
					break;
				}
			}
		});
		
		list = parser.getList();
		displayCategory();
		displayList();

		shell.open();
		shell.layout();
		initBorderSize();
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	private static boolean isTextEmpty(Text t) {
		return t.getText().length() == 1;
	}
	
	private static void initBorderSize() {
		Rectangle outer = Display.getCurrent().getActiveShell().getBounds();
        Rectangle inner = Display.getCurrent().getActiveShell().getClientArea();
        borderSize = outer.height - inner.height - BORDER_WIDTH;
	}
	
	private static void timer(){
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
		                	    	updateTime();
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
	
	private static void updateTime(){
    	Date date = new Date();
        DateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        timeLabel.setText(timeFormat.format(date));
	}
	
	private static String [] getTaskIdAndDesc(String rawInput) {
		return rawInput.split(SPACE_STRING, 2);
	}
}
