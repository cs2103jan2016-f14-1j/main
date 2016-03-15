package ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.sun.jna.platform.win32.Kernel32;

import shared.Keywords;

public class LockFile {
	
	   private static final String LOCK_FILEPATH = System.getProperty("java.io.tmpdir") +"/dotdotdot.lock";
	   public static final File lock = new File(LOCK_FILEPATH);
	  // private static int pid = Kernel32.INSTANCE.GetCurrentProcessId();
	   
	   public static boolean lock() throws IOException {
		   
	        if(lock.exists()) {
	        	
	        	BufferedReader bufferReader = new BufferedReader(new FileReader(lock));
	        	String pidStr = Keywords.EMPTY_STRING;
	        	if((pidStr = bufferReader.readLine()) != null){
					/*if(){
	        		// if pid in task manager, let the second instance run
	        		return true;
					}*/
	        	}
	        	
	        	return false;
	        }

	        lock.createNewFile();
	        lock.deleteOnExit();
	        
	       /* PrintWriter pw = new PrintWriter(lock);
            pw.println(pid);
            pw.close();
            */
	        return true;
	    }
}
