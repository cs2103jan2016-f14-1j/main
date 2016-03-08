package storage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import shared.Task;

public class ReadWrite {

	protected void readTasksFromFile() {
		BufferedReader bufferReader = null;
		try {
			bufferReader = new BufferedReader(new FileReader(FILENAME_FILEPATH));
			String currentLine = EMPTY_STRING;
			while ((currentLine = bufferReader.readLine()) != null) {
				if (currentLine.contains("|")) {
					Storage.addTaskToList(Formatter.formatTaskToObject(currentLine));
				}else{
					convertIDStringToList(currentLine);
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
	}

	private void convertIDStringToList(String s) {
		ArrayList<String> stringOfIds = s.split(SPACE_STRING);
		for (String id : stringOfIds) {
			FreeIDs.freeIDs.offerFirst(Integer.parseInt(id));
		}
		FreeIDs.setCurrentID(FreeIDs.freeIDs.poll());
		FreeIDs.sortIDs();
	}

	protected void writeTasksToFile() {

	}

	protected void writeIDsToFile() {

	}

}
