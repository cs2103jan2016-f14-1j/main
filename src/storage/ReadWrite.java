package storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import shared.Task;
import shared.Keywords;

public class ReadWrite {

	protected void readTasksFromFile() {
		BufferedReader bufferReader = null;
		try {
			bufferReader = new BufferedReader(new FileReader(Keywords.FILENAME_FILEPATH));
			String currentLine = Keywords.EMPTY_STRING;
			while ((currentLine = bufferReader.readLine()) != null) {
				if (currentLine.contains("|")) {
					Storage.addTaskToList(Task.formatStringToObject(currentLine));
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
		ArrayList<String> stringOfIds = (ArrayList<String>) Arrays.asList(s.split(Keywords.SPACE_STRING));
		for (String id : stringOfIds) {
			Storage.getFreeIDs().offerFirst(Integer.parseInt(id));
		}
		FreeIDs.setCurrentID(Storage.getFreeIDs().poll());
		FreeIDs.sortIDs();
	}

	protected void writeTasksToFile() {
		try {
			BufferedWriter bufferWriter = new BufferedWriter(new FileWriter(Keywords.FILENAME_FILEPATH));
			for (int index = 0; index < Storage.getTasks().size(); index++) {
				bufferWriter.write(Task.formatObjectToString(Storage.getTasks().get(index)));
				bufferWriter.newLine();
			}
			bufferWriter.write(FreeIDs.formatIDToString());
			bufferWriter.close();
		} catch (IOException ex) {
			//systemPrint(IO_ERROR_MSG);
		}
	}

}
