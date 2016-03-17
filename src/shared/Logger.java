package shared;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class Logger {
	protected static void log(String s) {
		System.out.println(String.format("%s %s", new Date(), s));
	}
	
	protected static void logf(String s) {
		try {
			BufferedWriter bufferWriter = 
					new BufferedWriter(new FileWriter(Keywords.LOG_FILEPATH));
			bufferWriter.write(String.format("%s %s\n", new Date(), s));
			bufferWriter.close();
		} catch (IOException ex) {
		}
	}
}
