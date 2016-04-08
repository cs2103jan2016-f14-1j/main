//@@author A0125347H

package shared;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class Logger {
	final static String STOUT_FORMAT = "%s %s";
	final static String LOG_FORMAT = "%s (%s): %s\n";
	
	protected static void log(String s) {
		System.out.println(String.format(STOUT_FORMAT, new Date(), s));
	}
	
	public static void logf(String className, String s) {
		try {
			BufferedWriter bufferWriter = 
					new BufferedWriter(new FileWriter(Keywords.LOG_FILEPATH, true));
			bufferWriter.write(String.format(LOG_FORMAT, className, new Date(), s));
			bufferWriter.close();
		} catch (IOException ex) {
		}
	}
}
