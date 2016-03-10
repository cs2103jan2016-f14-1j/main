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

public class ReadWrite {

	protected static String readTasksFromFile(ArrayList<Task> at) {
		BufferedReader bufferReader = null;
		try {
			bufferReader = new BufferedReader(new FileReader(Keywords.FILENAME_FILEPATH));
			String currentLine = Keywords.EMPTY_STRING;
			while ((currentLine = bufferReader.readLine()) != null) {
				if (currentLine.contains(Keywords.STORE_DELIMITER)) {
					at.add(Task.formatStringToObject(currentLine));
				} else {
					return currentLine;//the list of FreeIDs
				}
			}
		} catch (FileNotFoundException ex) {
			// systemPrint(FILE_NOT_FOUND_ERROR_MSG);
		} catch (IOException ex) {
			// systemPrint(IO_ERROR_MSG);
		} finally {
			try {
				if (bufferReader != null) {
					bufferReader.close();
				}
			} catch (IOException ex) {
				// systemPrint(IO_ERROR_MSG);
			}
		}
		return Keywords.EMPTY_STRING;
	}

	protected static void writeTasksToFile(ArrayList<Task> at, FreeIDs freeIDs) {
		try {
			BufferedWriter bufferWriter = new BufferedWriter(new FileWriter(Keywords.FILENAME_FILEPATH));
			for (int index = 0; index < at.size(); index++) {
				bufferWriter.write(Task.formatObjectToString(at.get(index)));
				bufferWriter.newLine();
			}
			bufferWriter.write(freeIDs.convertIDListToString());
			bufferWriter.close();
		} catch (IOException ex) {
			//systemPrint(IO_ERROR_MSG);
		}
	}

}
