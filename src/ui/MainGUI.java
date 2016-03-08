package ui;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import logic.AddTask;
import shared.Task;
import storage.Storage;

public class MainGUI {

	public static void main(String[] args) {

		Display display = Display.getDefault();
		Controller controller = new Controller();
		Shell shell = controller.getView().getShell();
	    
		shell.open();
		shell.layout();
		controller.initBorderSize();
		AddTask.addTask(new Task("", "test", null));
		System.out.println("HERE " + Storage.getTasks().size());
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}	
}
