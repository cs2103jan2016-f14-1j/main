//@@author A0125347H

package shared;

import java.util.ArrayList;
import java.util.Collections;

public class Sorter {

	/**
	 * 
	 * @param ArrayList<Task>
	 * @return sorted by intStartTime then intDate, in ascending order
	 */
	public static ArrayList<Task> sortByDate(ArrayList<Task> at) {
		Collections.sort(at, (t1, t2) -> t1.getIntStartTime() - t2.getIntStartTime());
		Collections.sort(at, (t1, t2) -> t1.getIntDate() - t2.getIntDate());
		return at;
	}
	
}
