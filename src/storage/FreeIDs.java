package storage;

import java.util.Collections;
import java.util.LinkedList;

public class FreeIDs {
	protected static LinkedList<Integer> freeIDs;
	private static int currentTaskId = 0;

	public FreeIDs() {
		freeIDs = new LinkedList<Integer>();
	}

	public int getNextAvailableID() {
		if (isListEmpty()) {
			generateID();
		}
		return freeIDs.poll();
	}

	protected static void sortIDs() {
		Collections.sort(freeIDs);
	}

	protected static void addIDs(int id) {
		freeIDs.offerFirst(id);
	}

	protected static void setCurrentID(int id) {
		currentTaskId = id;
	}

	private boolean isListEmpty() {
		return freeIDs.isEmpty();
	}

	private void generateID() {
		freeIDs.offer(currentTaskId++);
	}

}
