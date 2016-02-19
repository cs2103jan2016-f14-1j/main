package dotdotdot;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Color;
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
	private static final String HELP_REGEX = "(help|\\?|HELP)";
	private static final String EMPTY_STRING = "";
	private static final String SUCCESS_MESSAGE = "Your command has been executed successfully!";
	private static final String FAIL_MESSAGE = "Your command has failed to execute.";
	private static final String UNRECOGNISED_MESSAGE = "Your command is not recognised.";
	private static final String ERROR_MESSAGE = "An error has occured.";
	
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
    	
		for(int i = 0; i < list.size(); i++){
			mainItem = new TableItem(mainTable, SWT.NONE);
			mainItem.setText((i + 1) + ". " + list.get(i));
		}
		
		if(!outputStatus.isEmpty()){
			mainItem = new TableItem(mainTable, SWT.NONE);
			mainItem = new TableItem(mainTable, SWT.NONE);
			mainItem.setText(outputStatus);
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
		mainItem.setText("[? | help]");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		mainItem.setText("Undo previous command");
		mainItem = new TableItem(mainTable, SWT.NONE);
		mainItem.setText("[u | undo]");
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
					    	list = parser.getLogic().getStorage().getStoreFormattedToDos();
					    	outputStatus = SUCCESS_MESSAGE;
					    } else if (returnCode == Parser.COMMAND_FAIL){
					    	outputStatus = FAIL_MESSAGE;
					    } else if (returnCode == Parser.COMMAND_UNRECOGNISED){
					    	outputStatus = UNRECOGNISED_MESSAGE;
					    } else {
					    	outputStatus = ERROR_MESSAGE;
					    }
					    displayList();
		        	}		     				  
		            break;
		        case SWT.ESC:
		            System.out.println(SWT.ESC);
		            break;
		        case SWT.BS:
		        	if(isTextEmpty(input)) {
		        		inputToHint();
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
		
		list = logic.getStorage().getStoreFormattedToDos();
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
	
	private static boolean isTextEmpty(Text t){
		return t.getText().length() == 1;
	}
	
}
