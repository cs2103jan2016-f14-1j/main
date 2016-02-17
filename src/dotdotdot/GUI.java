package dotdotdot;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.awt.ComponentOrientation;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Label;

public class GUI {
	private static Text Input;
	private static Table Category;
	private static Table Main;
	private static TableItem categoryItem;
	private static TableItem mainItem;
	private static Parser parser = new Parser();
	private static Logic logic = new Logic();
	private static ArrayList<String> list;
	private static final String GUI_TITLE = "Dotdotdot";
	private static final String GUI_HINT = "< add <TODO> (at | by | on | to) <date> [@category] >";
	
	private static void displayList() {
		for(int i=0; i<list.size(); i++){
			// System.out.println((i+1) + ". " + list.get(i));
			mainItem = new TableItem(Main, SWT.NONE);
			mainItem.setText((i+1) + ". " + list.get(i));
		}
	}
	
	private static void displayHelp() {
		
	}
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		// Initialize the array list from the text file
		list = logic.init();
		Display display = Display.getDefault();
		Shell shell = new Shell();
		shell.setSize(640, 620);
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
			
		Color hintColor = display.getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND);
		Color blackColor = display.getSystemColor(SWT.COLOR_BLACK);
		
		Input = new Text(shell, SWT.BORDER);
		Input.setText(GUI_HINT);
		Input.setForeground(hintColor);
		Input.setBounds(10, 522, 594, 31);
		
		Input.addKeyListener(new KeyAdapter() {
		      public void keyPressed(KeyEvent event) {
		        switch (event.keyCode) {
		        case SWT.CR:
		        	// This case happens after "enter" is pressed
				    ArrayList<String> tempList = parser.input();
				    if(list.equals(tempList)){
				    	// Command Failed Because No Change
				    } else {
				        // Command Success
				    	list = tempList;
				    	displayList();
				    }
		            break;
		        case SWT.ESC:
		            System.out.println(SWT.ESC);
		            break;
		        case SWT.BS:
		        	// 1 Character left before backspace
		        	if(Input.getText().length() == 1){
		        		Input.setText(GUI_HINT);
		        		Input.setForeground(hintColor);
		        	} 
		        	break;
		        default:
		        	// Input is still a hint
		        	if(Input.getForeground().equals(hintColor)){
		        		Input.setText("");
		        		Input.setForeground(blackColor);
		        	}       	
		        	break;
		        }
		      }
		    });
		
		Category = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		Category.setBounds(10, 10, 155, 500);
		
		/*
		Events = new TableItem(Category, SWT.NONE);
		Events.setText("Events");
		*/
		
		Main = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		Main.setBounds(179, 10, 425, 500);
		
		displayList();
		
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}
