package dotdotdot;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolTip;

import java.util.ArrayList;

import org.apache.commons.lang3.text.WordUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

public class GUI {
	private static Text input;
	private static Table categoryTable;
	private static Table mainTable;
	private static TableItem categoryItem;
	private static TableItem mainItem;
	private static Parser parser = new Parser();
	private static Logic logic = new Logic();
	private static ArrayList<String> list;
	private static int borderSize;

	private static final String GUI_TITLE = "Dotdotdot";
	private static final String GUI_HINT = "< Input ? or help to show available commands >";
	private static final String HELP_REGEX = "(h|H|help|HELP|\\?)";
	private static final String VIEW_REGEX = "(view|v|V|VIEW)(.*)";
	private static final String EMPTY_STRING = "";
	private static final String SPACE_STRING = " ";
	private static final String SUCCESS_CONTENT_MESSAGE = "(%1$s) %2$s";
	private static final String SUCCESS_TITLE_MESSAGE = "%1$s Successful";
	private static final String FAIL_MESSAGE = "Invalid command";
	private static final String UNRECOGNISED_MESSAGE = "Unknown command";
	private static final String ERROR_MESSAGE = "An error has occured.";
	private static final int SCROLL_AMOUNT = 5;
	private static final int WRAP_AROUND = 45;
	private static final int NOT_DONE = 0;
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
			mainItem = new TableItem(categoryTable, SWT.NONE);
			mainItem.setText(categories.get(i));
		}
	}

	private static void displayList() {

		mainTable.removeAll();
		for (int i = 0; i < list.size(); i++) {
	
			String [] taskIDandDesc = getTaskIdAndDesc(list.get(i));
			String formattedOutput = WordUtils.wrap(taskIDandDesc[TASK_DESC], WRAP_AROUND);
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
		shell.setSize(685, 605);
		shell.setText(GUI_TITLE);
			
		hintColor = display.getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND);
		normalColor = display.getSystemColor(SWT.COLOR_BLACK);

		categoryTable = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		categoryTable.setBounds(10, 10, 155, 500);

		mainTable = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		mainTable.setBounds(179, 10, 490, 500);

		input = new Text(shell, SWT.BORDER);
		inputToHint();
		input.setBounds(10, 522, 659, 31);
		input.setFocus();

		final ToolTip tip = new ToolTip(shell, SWT.TOOL | SWT.ICON_INFORMATION | SWT.RIGHT);
	
		input.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent event) {

				switch (event.keyCode) {

				case SWT.CR:
					// SWT.CR : when "ENTER" key is pressed
					String tempInput = input.getText();
					inputToHint();
					tip.setVisible(false);

					if (isHelp(tempInput)) {
						displayHelp();
					} else {
						int returnCode = parser.input(tempInput);
						tip.setMessage(EMPTY_STRING);
						String longestString = EMPTY_STRING;
						
						if (returnCode == Parser.COMMAND_SUCCESS) {
							if (isView(tempInput)) {
								list = parser.getLogic().viewTasks(parser.isCompleted(tempInput));
								System.out.println("Size of list for view is: " + list.size());
							} else {
								list = parser.getLogic().viewTasks(NOT_DONE);
								System.out.println("It is not a view command, get all not done to do list");
							}
							
							tip.setText(String.format(SUCCESS_TITLE_MESSAGE, setFirstCharToUpper(parser.getLastCommand())));
							
							if(parser.getLastCommand().equals(Parser.CMD_DELETE)){
								ArrayList <Integer> deletedIDS = parser.getLogic().getCurrTaskIDs();
								String outputStatus = EMPTY_STRING;
								for(int i = 0; i < deletedIDS.size(); i++){
									String temp = parser.getLogic().getCurrTaskDescs().get(i);
									outputStatus += String.format(SUCCESS_CONTENT_MESSAGE,deletedIDS.get(i), temp) + "\n";
									if(temp.length() > longestString.length()){
										longestString = temp;
									}
								}
								tip.setMessage(outputStatus);
								parser.getLogic().clearCurrTasks();
							} else if (parser.getLastCommand().equals("sort")) {
								// Call logic to get sorted list
								// list = parser.getLogic().getSortedList
							}
								
						} else if (returnCode == Parser.COMMAND_FAIL) {
							tip.setText(FAIL_MESSAGE);
						} else if (returnCode == Parser.COMMAND_UNRECOGNISED) {
							tip.setText(UNRECOGNISED_MESSAGE);
						} else {
							tip.setText(ERROR_MESSAGE);
						}
					    
						if (tip.getText().length() > longestString.length()) {
							longestString = tip.getText();
						} 
						tip.setLocation(new Point(shell.getLocation().x + shell.getSize().x - longestString.length() * 15 ,
								shell.getLocation().y + borderSize));
						tip.setVisible(true);
						displayList();
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
		
		list = logic.viewTasks(NOT_DONE);
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

	private static boolean isHelp(String s) {
		return s.matches(HELP_REGEX);
	}

	private static boolean isView(String s) {
		return s.matches(VIEW_REGEX);
	}

	private static boolean isTextEmpty(Text t) {
		return t.getText().length() == 1;
	}
	
	private static String setFirstCharToUpper(String s){
		return Character.toUpperCase(s.charAt(0)) + s.substring(1);
	}
	
	private static void initBorderSize() {
		Rectangle outer = Display.getCurrent().getActiveShell().getBounds();
        Rectangle inner = Display.getCurrent().getActiveShell().getClientArea();
        borderSize = outer.height - inner.height - BORDER_WIDTH;
	}
	
	private static String [] getTaskIdAndDesc(String rawInput) {
		return rawInput.split(SPACE_STRING, 2);
	}
}
