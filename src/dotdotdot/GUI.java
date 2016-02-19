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
	
	private static final String GUI_TITLE = "Dotdotdot";
	private static final String GUI_HINT = "< Input ? or help to show available commands >";
<<<<<<< HEAD
	private static final String HELP_REGEX = "(h|H|help|HELP|\\?)";
	private static final String VIEW_REGEX = "view";
=======
	private static final String HELP_REGEX = "(help|\\?|HELP)";
	private static final String VIEW_REGEX = "(view|VIEW)";
>>>>>>> ab6871de452cfd588c0cc383c6be68aa9447e254
	private static final String EMPTY_STRING = "";
	private static final String SUCCESS_MESSAGE = "Your command has been executed successfully!";
	private static final String FAIL_MESSAGE = "Your command has failed to execute.";
	private static final String UNRECOGNISED_MESSAGE = "Your command is not recognised.";
	private static final String ERROR_MESSAGE = "An error has occured.";
	private static final int SCROLL_AMOUNT = 5;
	private static final int WRAP_AROUND = 40;
	
	private static String outputStatus = EMPTY_STRING;
	private static Color hintColor;
	private static Color normalColor;
	
	private static void inputToHint(){
		input.setText(GUI_HINT);
		input.setForeground(hintColor);
	}
	
	private static void inputToNormal(){
		input.setText(EMPTY_STRING);
		input.setForeground(normalColor);
	}
	
	private static void displayList() {
		
		mainTable.removeAll();
<<<<<<< HEAD

		for (String entry : list) {
			mainItem = new TableItem(mainTable, SWT.NONE);
			mainItem.setText(entry);
		}

		if (!outputStatus.isEmpty()) {
			mainItem = new TableItem(mainTable, SWT.NONE);
			mainItem = new TableItem(mainTable, SWT.NONE);
			mainItem.setText(outputStatus);
=======
    	
		for(int i = 0; i < list.size(); i++){
			String formattedOutput = WordUtils.wrap(list.get(i), WRAP_AROUND);
			String outputArray[] = formattedOutput.split("\n");
			for(int z=0; z<outputArray.length; z++){
				
				mainItem = new TableItem(mainTable, SWT.NONE);
				
				if(z==0){
					mainItem.setText(outputArray[z]);
				} else {
					mainItem.setText("      " + outputArray[z]);
				}
			}
>>>>>>> ab6871de452cfd588c0cc383c6be68aa9447e254
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
	 * @param args
	 */
	public static void main(String[] args) {
		
		Display display = Display.getDefault();
		Shell shell = new Shell();
		shell.setSize(685, 619);
		shell.setText(GUI_TITLE);
		 
		hintColor = display.getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND);
		normalColor = display.getSystemColor(SWT.COLOR_BLACK);
		
		categoryTable = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		categoryTable.setBounds(10, 10, 155, 500);
		
		mainTable = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		mainTable.setBounds(179, 10, 474, 500);
		
		input = new Text(shell, SWT.BORDER);
		inputToHint();
		input.setBounds(10, 522, 643, 31);
		input.setFocus();
		
		final ToolTip tip = new ToolTip(shell, SWT.TOOL | SWT.ICON_INFORMATION);
		
		input.addKeyListener(new KeyAdapter() {
		      public void keyPressed(KeyEvent event) {
		    	  
		    	switch (event.keyCode) {
		        
		        case SWT.CR:
		        	// SWT.CR : when "ENTER" key is pressed
		        	String tempInput = input.getText();		
		        	inputToHint();
	        		
		        	if(isHelp(tempInput)) {
		        		displayHelp();
		        	} else {
		        		int returnCode = parser.input(tempInput);
		        	
		        		if (returnCode == Parser.COMMAND_SUCCESS) {
		        			if (isView(tempInput)) {
		        				list = parser.getLogic().viewTasks(parser.isCompleted(tempInput));
							} else {
								list = parser.getLogic().getUserFormattedToDos();
							}
					    } else if (returnCode == Parser.COMMAND_FAIL){
					    	outputStatus = FAIL_MESSAGE;
					    } else if (returnCode == Parser.COMMAND_UNRECOGNISED){
					    	outputStatus = UNRECOGNISED_MESSAGE;
					    } else {
					    	outputStatus = ERROR_MESSAGE;
					    }
		        		
		        		tip.setLocation(new Point(shell.getLocation().x + mainTable.getSize().x / 4, shell.getLocation().y + mainTable.getSize().y ));	
		        		tip.setText(outputStatus);
		        		tip.setVisible(true);
				        
					    displayList();
		        	}		     				  
		            break;
		        case SWT.ARROW_UP:
		        	mainTable.setTopIndex(mainTable.getTopIndex() -  SCROLL_AMOUNT);
		        	event.doit = false;
		        	break;
		        case SWT.ARROW_DOWN:
		        	mainTable.setTopIndex(mainTable.getTopIndex() +  SCROLL_AMOUNT);
		        	event.doit = false;
		        	break;
		        case SWT.ESC:
		            System.out.println(SWT.ESC);
		            break;
		        case SWT.BS:
		        	if(isTextEmpty(input)) {
		        		inputToHint();
		        	} else if (input.getForeground().equals(hintColor)) {
		        		inputToNormal();
		        	}
		        	break;
		        default:
		        	// removes hint and changes input back to normal
		        	if(input.getForeground().equals(hintColor)) {
		        		inputToNormal();
		        	}       	
		        	break;
		        }
		      }
		    });
		list = logic.getUserFormattedToDos();
		displayList();
		
		shell.open();
		shell.layout();
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
	
	private static boolean isTextEmpty(Text t){
		return t.getText().length() == 1;
	}
	
}
