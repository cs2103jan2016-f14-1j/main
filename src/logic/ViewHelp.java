package logic;

import java.util.LinkedList;

public class ViewHelp extends Functionality {

	public LinkedList<String> viewHelp() {
		LinkedList<String> help = new LinkedList<String>();
		help.add("Adding tasks");
		help.add("add <TODO>");
		help.add("add <TODO> (at | by | on | to) <date> [#category]");
		help.add("Eg. add do CS2103 tutorial by Sun");
		help.add("      add buy milk by 15Feb #shopping");
		help.add("Edit tasks");
		help.add("edit <task_ID#> to <date>");
		help.add("Eg. edit 1 to 15Feb");
		help.add("Delete tasks");
		help.add("delete <task_ID#>");
		help.add("Eg. delete 1 2");
		help.add("Set priority");
		help.add("mark <task_ID#>");
		help.add("Eg. mark 2 3");
		help.add("Complete Tasks");
		help.add("do <TODO | task_ID#>");
		help.add("Eg. do receive quest");
		help.add("      do 1");
		help.add("View tasks");
		help.add("view <category>");
		help.add("Eg. view shopping");
		help.add("Show help");
		help.add("[? | help | h]");
		help.add("Undo previous command");
		help.add("[u | undo]");
		help.add("Keyboard shortcuts");
		help.add("Launch application: CTRL + ALT + D");
		help.add("Choose file location: ALT + E");
		help.add("Exit program: ESC");
		return help;
	}
}
