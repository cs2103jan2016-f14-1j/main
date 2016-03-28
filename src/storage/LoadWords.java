package storage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import shared.Keywords;

public class LoadWords {

	private static TreeSet<String> lines;

	public static void init() {
	    lines = new TreeSet<String>();
	    BufferedReader bufferReader = null;
		try {
			bufferReader = new BufferedReader(new FileReader("dictionary"));
			String currentLine = Keywords.EMPTY_STRING;
			while ((currentLine = bufferReader.readLine()) != null) {
					load(currentLine);

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

	private static void load(String line) {
		lines.add(line);
	}

	public boolean matchPrefix(String prefix) {
		Set<String> tailSet = lines.tailSet(prefix);
		for (String tail : tailSet) {
			if (tail.startsWith(prefix)) {
				return true;
			}
		}
		return false;
	}

	public static ArrayList<String> findCompletions(String prefix) {
		ArrayList<String> completions = new ArrayList<String>();
		Set<String> tailSet = lines.tailSet(prefix);
		for (String tail : tailSet) {
			if (tail.startsWith(prefix)) {
				completions.add(tail);
			} else {
				break;
			}
		}
		return completions;
	}
}