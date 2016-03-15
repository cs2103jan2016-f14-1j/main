package ui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class MainGUI {

	public static void main(String[] args) throws Exception {

		Display display = Display.getDefault();
		Controller controller = new Controller();
		Shell shell = controller.getView().getShell();
	    
		shell.open();
		shell.layout();
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}	
}
