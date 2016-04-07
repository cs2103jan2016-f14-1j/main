//@@author A0125387Y

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

	private boolean keyD = false;
	private boolean keyCtrl = false;
	private static boolean keyAlt = false;
	private static boolean keyE = false;
	private static Shell shell;

	public static void main(String[] args) throws Exception {
		
		Keywords.currLocation = MainGUI.class.getProtectionDomain().getCodeSource().getLocation().toURI().toString();
		Keywords.currLocation = Keywords.currLocation.replace(Keywords.OLD_FILE_DELIMITER, Keywords.NEW_FILE_DELIMITER);
		Keywords.currLocation = Keywords.currLocation.substring(Keywords.currLocation.indexOf(Keywords.NEW_FILE_DELIMITER) + 1, Keywords.currLocation.lastIndexOf(Keywords.NEW_FILE_DELIMITER)+1);
		
		// Run startup.reg to add preference
		String value = "\"" + Keywords.currLocation + JAR_NAME +"\"";
		WinRegistry.writeStringValue(WinRegistry.HKEY_CURRENT_USER, "Software\\Microsoft\\Windows\\CurrentVersion\\Run", KEY_NAME, value);
		
		Keywords.settingsPath = Keywords.currLocation + Keywords.settingsPath;
	    
		Display display = new Display();
		Controller controller = new Controller();
		
		// For alt tab bug
		display.addFilter(SWT.FocusOut, new Listener(){

			@Override
			public void handleEvent(Event e) {
				keyAlt = false;
				keyE = false;
			}
			
		});

		display.addFilter(SWT.KeyDown, new Listener() {
			
			@Override
            public void handleEvent(Event e) {
            	if(e.keyCode==SWT.ESC){
            		System.exit(0);
            	}
            	
            	if(e.keyCode==SWT.ALT){
            		keyAlt = true;
            	}
            	
            	if(e.keyCode=='e'){
            		keyE = true;
            	}
            	
            	if(keyAlt && keyE){
            		 controller.writePathToFile();
            		 keyAlt = false;
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
					keyAlt = false;
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
		if (e.getKeyCode() == NativeKeyEvent.VC_D) {
            keyD = true;
            if (keyD && keyCtrl && keyAlt) {
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
    					System.exit(0);
    				}
            }
            keyD = false;
            keyCtrl = false;
            keyAlt = false;
        } else if (e.getKeyCode() == NativeKeyEvent.VC_CONTROL_L || e.getKeyCode() == NativeKeyEvent.VC_CONTROL_R) {
        	keyCtrl = true;
        } else if (e.getKeyCode() == NativeKeyEvent.VC_ALT_L || e.getKeyCode() == NativeKeyEvent.VC_ALT_R) {
        	keyAlt = true;
        }
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent e) {
		if (e.getKeyCode() == NativeKeyEvent.VC_D) {
            keyD = false;
        } else if (e.getKeyCode() == NativeKeyEvent.VC_CONTROL_L || e.getKeyCode() == NativeKeyEvent.VC_CONTROL_R) {
        	keyCtrl = false;
        } else if (e.getKeyCode() == NativeKeyEvent.VC_ALT_L || e.getKeyCode() == NativeKeyEvent.VC_ALT_R) {
        	keyAlt = false;
        }
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent e) {
		// We do not need this method
	}	
	
}
