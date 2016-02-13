package dotdotdot;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.awt.ComponentOrientation;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Label;

public class GUI {
	private static Text Input;
	private static Table Category;
	private static Table Main;
	private static TableItem tableItem;
	private static TableItem Events;
	private static TableItem tableItem_1;

	private static void init() {
		
	}
	
	private static void displayHelp() {
		
	}
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = Display.getDefault();
		Shell shell = new Shell();
		shell.setSize(640, 700);
		shell.setText("Dotdotdot");
		
		Input = new Text(shell, SWT.BORDER);
		Input.setText("Insert Input");
		Input.setBounds(10, 603, 594, 31);
		Input.addKeyListener(new KeyAdapter() {
		      public void keyPressed(KeyEvent event) {
		        switch (event.keyCode) {
		        // This is case happens after "enter" is pressed
		        case SWT.CR:
		          Parser parser = new Parser();
		          break;
		        case SWT.ESC:
		          System.out.println(SWT.ESC);
		          break;
		        }
		      }
		    });
		
		Category = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		Category.setBounds(10, 10, 155, 578);
		
		Events = new TableItem(Category, SWT.NONE);
		Events.setText("Events");
		
		tableItem = new TableItem(Category, SWT.NONE);
		tableItem.setText(new String[] {});
		tableItem.setText("Meeting");
		
		Main = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		Main.setBounds(179, 10, 425, 578);
		
		tableItem_1 = new TableItem(Main, SWT.NONE);
		tableItem_1.setText("Beat Sam");
		
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}
