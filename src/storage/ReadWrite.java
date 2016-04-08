//@@author A0076520L

package storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import shared.Task;
import shared.Keywords;
import shared.Logger;

public class ReadWrite {
	
	private static final String ERROR_MSG = "ReadWrite Error Msg";
	
	/**
	 * Method to read the data from file and store it in the list
	 * @param list 
	 * 			Obtain an arraylist to add tasks to
	 */
	protected static void readTasksFromFile(ArrayList<Task> list) {
		BufferedReader bufferReader = null;
		try {
			bufferReader = new BufferedReader(new FileReader(Keywords.filePath));

			String currentLine = Keywords.EMPTY_STRING;
			while ((currentLine = bufferReader.readLine()) != null) {
				if (currentLine.contains(Keywords.STORE_DELIMITER)) {
					list.add(Task.formatStringToObject(currentLine));
				} else {
					FreeIDs.convertIDStringToList(currentLine);
				}
			}
		} catch (FileNotFoundException ex) {
			Logger.logf(ERROR_MSG, "File was not found.");
		} catch (IOException ex) {
			Logger.logf(ERROR_MSG, "Usage of BufferReader IOException caught.");
		} finally {
			try {
				if (bufferReader != null) {
					bufferReader.close();
				}
			} catch (IOException ex) {
				Logger.logf(ERROR_MSG, " Closing of BufferReader IOException caught.");
			}
		}
	}

	/**
	 * Method to write tasks to file
	 * @param list 
	 * 			Obtain a list of tasks to write to file
	 */
	protected static void writeTasksToFile(ArrayList<Task> list) {
		try {
			BufferedWriter bufferWriter = new BufferedWriter(new FileWriter(Keywords.filePath));
			for (int index = 0; index < list.size(); index++) {
				bufferWriter.write(Task.formatObjectToString(list.get(index)));
				bufferWriter.newLine();
			}
			bufferWriter.write(FreeIDs.convertIDListToString());
			bufferWriter.close();
		} catch (IOException ex) {
			Logger.logf(ERROR_MSG, "Usage of BufferWriter IOException caught.");
		}
	}

}
