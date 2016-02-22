package dotdotdot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class Storage {

	// FORMAT OF EACH TASK: [taskID]|[task]|[date]|[categories]|[isComplete]|
	private ArrayList<String> toDos = new ArrayList<String>();
	private LinkedList<Integer> freedIds = new LinkedList<Integer>();
	private int newTaskId = 0;

	private final String STORE_DELIMITER = "|";
	private final String FILENAME_FILEPATH = "./test.txt";
	private final String EMPTY_STRING = "";
	private final String SPACE_STRING = " ";

	private final String GENERAL_ERROR_MSG = "Error has occured: %1$s.";
	private final String FILE_NOT_FOUND_ERROR_MSG = "The file is not found. Check the path of file";
	private final String IO_ERROR_MSG = "Input/Output error.";

	/**
	 * Constructor method to initialize the values of the variables
	 */
	public Storage() {
		readFromFile();
		readQueueFromFile();
	}

	/**
	 * Get all the unformatted list of to do
	 * 
	 * @return returns an arrayList of unformatted tasks to the caller
	 */
	public ArrayList<String> getStoreFormattedToDos() {
		return toDos;
	}

	/**
	 * Add StoreFormatted todo
	 */
	public void addStoreFormattedToDo(String line) {
		toDos.add(line);
	}

	/**
	 * Remove the task from the arrayList
	 * 
	 * @param line
	 *            the task to be removed from the arrayList
	 */
	public void removeStoreFormattedToDo(int taskId, int taskIndex) {
		freedIds.offer(taskId);
		toDos.remove(taskIndex);
	}

	/**
	 * Returns the concatenated task by using index
	 * 
	 * @param index
	 *            the index of the task to be obtained
	 * @return return the concatenated task to the caller
	 */
	public String getTaskByIndex(int index) {
		return toDos.get(index);
	}

	public void setTaskByIndex(int index, String task) {
		toDos.set(index, task);
	}

	/**
	 * This method directly read the contents of the file
	 * 
	 */
	public void readFromFile() {
		BufferedReader bufferReader = null;
		try {
			bufferReader = new BufferedReader(new FileReader(FILENAME_FILEPATH));
			String currentLine = EMPTY_STRING;
			while ((currentLine = bufferReader.readLine()) != null) {
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

	private void readQueueFromFile() {
		convertIDStringToQueue(removeLastElement(toDos));
	}
	private String removeLastElement(ArrayList<String> as){
		return as.remove(as.size() - 1);
	}
	private void convertIDStringToQueue(String s) {
		String[] stringOfIds = s.split(SPACE_STRING);
		for (String id : stringOfIds) {
			freedIds.offerFirst(Integer.parseInt(id));
		}
		setNextTaskId(freedIds.poll());
		Collections.sort(freedIds);
	}

	private void setNextTaskId(int a) {
		this.newTaskId = a;
	}
	public int getNextTaskId() {
		isFreeIdListEmpty();
		return freedIds.poll();
	}

	/**
	 * A method to check if the current list is empty
	 * 
	 * @return returns the truth value of the list
	 */
	public boolean isListEmpty() {
		return toDos.isEmpty();
	}

	/**
	 * Overwrite the file directly with the data in the arrayList
	 */
	public void writeToFile() {
		try {
			BufferedWriter bufferWriter = new BufferedWriter(new FileWriter(FILENAME_FILEPATH));
			for (int index = 0; index < toDos.size(); index++) {
				bufferWriter.write(toDos.get(index));
				bufferWriter.newLine();
			}
			bufferWriter.write(getTaskIdString());
			bufferWriter.close();
		} catch (IOException ex) {
			systemPrint(IO_ERROR_MSG);
		}
	}

	private String getTaskIdString() {
		Collections.sort(freedIds);
		String out = EMPTY_STRING;
		isFreeIdListEmpty();
		for (int id : freedIds) {
			out += id + SPACE_STRING;
		}
		return out;
	}
	
	/**
	 * This checks if the list of id to allocate to the task is empty
	 * and will generate the next id if it is empty
	 */
	private void isFreeIdListEmpty(){
		if (freedIds.isEmpty()) {
			freedIds.offer(newTaskId++);
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