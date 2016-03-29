package ui;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import shared.Keywords;

import java.awt.FileDialog;
import java.util.concurrent.Executor;

import javax.swing.JFrame;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class MainGUI implements NativeKeyListener {
	
	private final static String JAR_NAME = "dotdotdot.jar"; 
	private final static String KEY_NAME = "dotdotdot autorun key"; 

	private boolean keyd = false;
	private boolean keyctrl = false;
	private static boolean keyalt = false;
	private static boolean keyE = false;
	private static Shell shell;

	public static void main(String[] args) throws Exception {
	    
		Keywords.currLocation = MainGUI.class.getProtectionDomain().getCodeSource().getLocation().toURI().toString();
		Keywords.currLocation = Keywords.currLocation.replace(Keywords.OLD_FILE_DELIMITER, Keywords.NEW_FILE_DELIMITER);
		Keywords.currLocation = Keywords.currLocation.substring(Keywords.currLocation.indexOf(Keywords.NEW_FILE_DELIMITER) + 1, Keywords.currLocation.lastIndexOf(Keywords.NEW_FILE_DELIMITER)+1);
		// Run startup.reg to add preference
		String value = "\"" + Keywords.currLocation + JAR_NAME +"\"";
		WinRegistry.writeStringValue(WinRegistry.HKEY_CURRENT_USER, "Software\\Microsoft\\Windows\\CurrentVersion\\Run", KEY_NAME, value);
		//WinRegistry.deleteValue(WinRegistry.HKEY_CURRENT_USER, "Software\\Microsoft\\Windows\\CurrentVersion\\Run", "dotdotdot autorun key");
		
		Keywords.settingsPath = Keywords.currLocation + Keywords.settingsPath;
	    
		Display display = new Display();
		Controller controller = new Controller();

		display.addFilter(SWT.KeyDown, new Listener() {
			
			@Override
            public void handleEvent(Event e) {
            	if(e.keyCode==SWT.ESC){
            		System.exit(0);
            	}
            	
            	if(e.keyCode==SWT.ALT){
            		keyalt = true;
            	}
            	
            	if(e.keyCode=='e'){
            		keyE = true;
            	}
            	
            	if(keyalt && keyE){
            		 controller.writePathToFile();
            		 keyalt = false;
            		 keyE = false;
                }
            	
            	
            }
        });
		display.addFilter(SWT.KeyUp, new Listener(){

			@Override
			public void handleEvent(Event e) {
				if(e.keyCode=='e'){
					keyE = false;
				}
				
				if(e.keyCode==SWT.ALT){
					keyalt = false;
				}
			}
			
		});
		shell = controller.getView().getShell();
		shell.open();
		shell.layout();
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == NativeKeyEvent.VC_D) {
            keyd = true;
            if (keyd && keyctrl && keyalt) {
    	        
               	Display.getDefault().asyncExec(
               			new Runnable(){
              			       public void run(){
              			    	   shell.setVisible(true);  
              			    	   shell.forceActive();
               			   } 
               			});
              	    	   
               	try {
    					GlobalScreen.unregisterNativeHook();
    				} catch (NativeHookException e1) {
    					e1.printStackTrace();
    				}
            }
            keyd = false;
            keyctrl = false;
            keyalt = false;
        } else if (e.getKeyCode() == NativeKeyEvent.VC_CONTROL_L || e.getKeyCode() == NativeKeyEvent.VC_CONTROL_R) {
        	keyctrl = true;
        } else if (e.getKeyCode() == NativeKeyEvent.VC_ALT_L || e.getKeyCode() == NativeKeyEvent.VC_ALT_R) {
        	keyalt = true;
        }
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == NativeKeyEvent.VC_D) {
            keyd = false;
        } else if (e.getKeyCode() == NativeKeyEvent.VC_CONTROL_L || e.getKeyCode() == NativeKeyEvent.VC_CONTROL_R) {
        	keyctrl = false;
        } else if (e.getKeyCode() == NativeKeyEvent.VC_ALT_L || e.getKeyCode() == NativeKeyEvent.VC_ALT_R) {
        	keyalt = false;
        }
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent e) {
		// TODO Auto-generated method stub
		
	}	
	
}
