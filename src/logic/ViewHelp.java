//@@author A0076520L

package logic;

import java.util.LinkedList;

public class ViewHelp extends Functionality {

	/**
	 * Set up the help sheet for user
	 * 
	 * @return the list of Strings to display as help
	 */
	public LinkedList<String> viewHelp() {
		LinkedList<String> help = new LinkedList<String>();
		help.add("Adding tasks");
		help.add("add <TODO>");
		help.add("add <TODO> (at | by | on | to) <date> [#category]");
		help.add("Eg. add do CS2103 tutorial by 10 May");
		help.add("      add buy milk by 15Feb #shopping");
		
		help.add("Edit tasks");
		help.add("edit <task_ID#> to <date>");
		help.add("Eg. edit 1 to 15Feb");
		
		help.add("Delete tasks");
		help.add("delete <task_ID#>");
		help.add("Eg. delete 1 2");
		
		help.add("Searching tasks");
		help.add("search \"<TODO>\"");
		help.add("search \"<TODO>\" <date>");
		help.add("search <date>");
		help.add("search (busiest | priority) (from | in) <date>");
		help.add("Eg. search \"networking\"");
		help.add("      search \"networking\" 14 apr");
		help.add("      search May");
		help.add("      search busiest from Apr");
		
		help.add("Set priority");
		help.add("mark <task_ID#>");
		help.add("Eg. mark 2 3");
		
		help.add("Complete Tasks");
		help.add("do <TODO | task_ID#>");
		help.add("Eg. do receive quest");
		help.add("      do 1");
		
		help.add("View tasks");
		help.add("view [category]");
		help.add("Eg. view shopping");
		help.add("      view");
		
		help.add("Show help");
		help.add("[help | h | ?]");
		
		help.add("Undo previous command");
		help.add("[undo | u]");
		
		help.add("Keyboard shortcuts");
		help.add("Launch application: CTRL + ALT + D");
		help.add("Choose file location: ALT + E");
		help.add("Exit program: ESC");
		
		return help;
	}
}
