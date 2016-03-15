package logic;

import parser.Parser;
import shared.Keywords;
import storage.Storage;

public class UndoTask extends Functionality {

	public boolean undoTask() {
		String action = Storage.getLastAction();
		System.out.println(action);

		Parser p = new Parser();
		if (action.contains(Keywords.STORE_DELIMITER)) {
			String[] actions = action.split(Keywords.DELIMITER);
			for (String cmd : actions) {
				p.parse(cmd);
			}
		} else {
			p.parse(action);
		}
		super.synchronization();
		return true;
	}
}
