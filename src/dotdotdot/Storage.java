package dotdotdot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Storage {

	//FORMAT OF EACH TASK: [taskID]|[task]|[date]|[categories]|[isComplete]|
	private ArrayList<String> toDos = new ArrayList<String>();
	
	private final String FILENAME = "./test.txt";
	private final String GENERAL_ERROR_MSG = "Error has occured: %1$s.";
	private final String FILE_NOT_FOUND_ERROR_MSG = "The file is not found. Check the path of file";
	private final String IO_ERROR_MSG = "Input/Output error.";

	/**
	 *  Constructor method to initialize the values of the variables
	 */
	public Storage() {
		readFromFile();
	}

	/**
	 * Get all the unformatted list of to do
	 *  
	 * @return 
	 * 		returns an arrayList of unformatted tasks to the caller
	 */
	public ArrayList<String> getStoreFormattedToDos() {
		return toDos;
	}

	/**
	 * Add an unformatted task to the arrayList
	 * @param line
	 * 		the task to be added to the arrayList
	 */
	public void addStoreFormattedToDo(String line) {
		toDos.add(line);
	}
	
	/**
	 * Remove the task from the arrayList
	 * @param line
	 * 		the task to be removed from the arrayList
	 */
	public void removeStoreFormattedToDo(int taskID){
		// TODO: currently this removes by the order in which it is in ArrayList
		// TODO: we want it to remove by the taskID
		toDos.remove(taskID - 1);
	}
	
	/**
	 * Returns the concatenated task by using index
	 * @param index
	 * 		the index of the task to be obtained
	 * @return
	 * 		return the concatenated task to the caller
	 */
	public String getTaskByIndex(int index){
		return toDos.get(index);
	}
	
	public void setTaskByIndex(int index, String task){
		toDos.set(index, task);
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
				// TODO: NEED TO UNFORMAT!! (in tandem with writeToFile's BufferedWriter.write)
				addStoreFormattedToDo(currentLine);
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
	 * A method to check if the current list is empty
	 * @return
	 * 		returns the truth value of the list
	 */
	public boolean isListEmpty(){
		return toDos.isEmpty();
	}
	
	/**
	 * Overwrite the file directly with the data in the arrayList 
	 */
	public void writeToFile(){
		try {
		BufferedWriter bufferWriter = new BufferedWriter(new FileWriter(FILENAME));
		for (int index = 0; index < toDos.size(); index++) {
			// TODO: NEED TO FORMAT!!
			bufferWriter.write(toDos.get(index));
			bufferWriter.newLine();
		}
		bufferWriter.close();
		} catch (IOException ex) {
			systemPrint(IO_ERROR_MSG);
		}
	}

	/**
	 * A general method to print the output to the console
	 * 
	 * @param toPrint
	 */
	private void systemPrint(String toPrint) {
		System.out.println(toPrint);
	}

}