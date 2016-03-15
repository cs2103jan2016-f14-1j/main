package ui;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class MainGUI implements NativeKeyListener {
	
	private final static String JAR_NAME = "dotdotdot.jar"; 
	private final static String KEY_NAME = "dotdotdot autorun key"; 
	private boolean keyd = false;
	private boolean keyctrl = false;
	private boolean keyalt = false;
	private static Shell shell;

	public static void main(String[] args) throws Exception {
	
		String value = "\"javaw -jar " + System.getProperty("user.dir") + "\\"+ JAR_NAME +"\"";
		WinRegistry.writeStringValue(WinRegistry.HKEY_CURRENT_USER, "Software\\Microsoft\\Windows\\CurrentVersion\\Run", KEY_NAME, value);
		//WinRegistry.deleteValue(WinRegistry.HKEY_CURRENT_USER, "Software\\Microsoft\\Windows\\CurrentVersion\\Run", "dotdotdot autorun key");
	
		Display display = Display.getDefault();
		Controller controller = new Controller();
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
