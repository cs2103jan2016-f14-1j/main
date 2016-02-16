package dotdotdot;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Storage {

	private ArrayList<String> toDos;
	private final String FILENAME = "C:\\";
	private final String GENERAL_ERROR_MSG = "Error has occured: %1$s.";
	private final String FILE_NOT_FOUND_ERROR_MSG = "The file is not found. Check the path of file";
	private final String IO_ERROR_MSG = "Input/Output error.";

	/**
	 *  Constructor method to initialize the values of the variables
	 */
	public Storage() {
		toDos = new ArrayList<>();
	}

	/**
	 * Get all the unformatted list of to do
	 *  
	 * @return 
	 * 		returns an arrayList of unformatted tasks to the caller
	 */
	public ArrayList<String> getUnformattedToDos() {
		return toDos;
	}

	/**
	 * Add an unformatted task to the arrayList
	 * @param line
	 * 		the task to be added to the arrayList
	 */
	public void addUnformattedToDos(String line) {
		toDos.add(line);
	}

	/**
	 * This method directly read the contents of the file
	 * 
	 */
	public void readFromFile() {
		BufferedReader bufferReader = null;
		try {
			bufferReader = new BufferedReader(new FileReader(FILENAME));
			String currentLine = "";
			while ((currentLine = bufferReader.readLine()) != null) {
				addUnformattedToDos(currentLine);
			}
		} catch (FileNotFoundException ex) {
			systemPrint(FILE_NOT_FOUND_ERROR_MSG);
		} catch (IOException ex) {
			systemPrint(IO_ERROR_MSG);
		} finally {
			try {
				if (bufferReader != null) {
					bufferReader.close();
				}
			} catch (IOException ex) {
				systemPrint(IO_ERROR_MSG);
			}
		}
	}

	/**
	 * A general method to print the output to the console
	 * 
	 * @param toPrint
	 */
	public void systemPrint(String toPrint) {
		System.out.println(toPrint);
	}

}