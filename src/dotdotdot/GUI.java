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
	private static final String HELP_COMMAND_1 = "?";
	private static final String HELP_COMMAND_2 = "help";
	private static final String EMPTY_STRING = "";
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
		for(int i=0; i<list.size(); i++){
			mainItem = new TableItem(mainTable, SWT.NONE);
			mainItem.setText((i+1) + ". " + list.get(i));
		}
	}
	
	private static void displayHelp() {
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
		// Initialize the array list from the text file
		list = null;
		//list = logic.init();
		Display display = Display.getDefault();
		Shell shell = new Shell();
		shell.setSize(685, 619);
		shell.setText(GUI_TITLE);

		/*
		shell.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent event) {
				switch (event.keyCode) {
		        // This case happens after "I" is pressed
		        case LETTER_I_CODE:
		        	Input.setText("");
		        	Input.setFocus();
		        	break;
				default:
					
					break;
				}	
			}
		});
		*/
			
		hintColor = display.getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND);
		normalColor = display.getSystemColor(SWT.COLOR_BLACK);
		
		input = new Text(shell, SWT.BORDER);
		inputToHint();
		input.setBounds(10, 522, 643, 31);
		
		input.addKeyListener(new KeyAdapter() {
		      public void keyPressed(KeyEvent event) {
		        switch (event.keyCode) {
		        case SWT.CR:
		        	// This case happens after "enter" is pressed
		        	String tempInput = input.getText();		
		        	inputToHint();
	        		
		        	if(tempInput.equals(HELP_COMMAND_1) || tempInput.equals(HELP_COMMAND_2)){
		        		mainTable.removeAll();
		        		displayHelp();
		        	} else {
		        		// ArrayList<String> tempList = parser.input(tempInput);
					    ArrayList<String> tempList = null;
					    if(list.equals(tempList)){
					    	// Command Failed Because No Change
					    } else {
					        // Command Success
					    	mainTable.clearAll();
					    	list = tempList;
					    	displayList();
					    }
		        	}		     				  
		            break;
		        case SWT.ESC:
		            System.out.println(SWT.ESC);
		            break;
		        case SWT.BS:
		        	// 1 Character left before backspace
		        	if(input.getText().length() == 1){
		        		inputToHint();
		        	} 
		        	break;
		        default:
		        	// Input is still a hint
		        	if(input.getForeground().equals(hintColor)){
		        		inputToNormal();
		        	}       	
		        	break;
		        }
		      }
		    });
		
		categoryTable = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		categoryTable.setBounds(10, 10, 155, 500);
		
		mainTable = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		mainTable.setBounds(179, 10, 474, 500);
		
		//displayList();
		
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}
