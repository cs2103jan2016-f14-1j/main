package test.integrated;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.ocpsoft.prettytime.shade.edu.emory.mathcs.backport.java.util.Arrays;

import logic.Notification;
import parser.ParseAdd;
import parser.Parser;
import shared.Task;
import storage.Storage;

public class DeletingTasksTest {

	ArrayList<Task> tasks = new ArrayList<Task>();
	Parser parse = Parser.getInstance();
	Storage S = Storage.getInstance();
	@Before
	public void populateList(){
		Storage.getInstance();
		Task t = new Task();
		t.setTask("add add add addadd");
		t.setId(1);
		tasks.add(t);
		S.addTaskToList(t);
		t = new Task();
		t.setId(2);
		t.setTask("undo complete delete do view search");
		tasks.add(t);
		S.addTaskToList(t);
		t = new Task();
		t.setId(3);
		t.setTask("\\/>#,|<.@~$%^&*!)(+_-|}{\";:");
		tasks.add(t);
		S.addTaskToList(t);
		t = new Task();
		t.setId(4);
		t.setTask("test~1");
		String[] s = { ")", ")(!#", "test(#@#", "####", "cat" };
		ArrayList<String> cat = new ArrayList<String>(Arrays.asList(s));
		t.setCategories(cat);
		tasks.add(t);
		S.addTaskToList(t);
		t = new Task();
		t.setId(5);
		t.setTask("thistime#withnospace");
		t.setIntDate(87);
		tasks.add(t);
		S.addTaskToList(t);
		t = new Task();
		t.setId(6);
		t.setTask("random test1");
		String[] ss = { ")", ")(!#", "test(#@#", "####", "test"};
		cat = new ArrayList<String>(Arrays.asList(ss));
		t.setCategories(cat);
		t.setIntDate(87);
		tasks.add(t);
		S.addTaskToList(t);
		t = new Task();
		t.setId(7);
		t.setTask("random test2");
		t.setIntDate(87);
		tasks.add(t);
		S.addTaskToList(t);
	}
	
	@Test
	public void deleteTasksTest() {
		//Normal deletions
		Notification n = (Notification) parse.parse("delete 1");
		assertEquals("(#1) add add add addadd  ",n.getMessage());
		n = (Notification) parse.parse("delete 1 2");
		assertEquals("(#2) undo complete delete do view search  ",n.getMessage());
		n = (Notification) parse.parse("delete 3 5");
		assertEquals("[3, 5]",n.getMessage());
		
		//delete non existing tasks
		n = (Notification) parse.parse("delete 999");
		assertEquals("Error!",n.getTitle());
		
		//deleting category by specifying # E.g. delete #cat
		n = (Notification) parse.parse("delete #cat");
		assertEquals("Error!",n.getTitle());
		n = (Notification) parse.parse("delete %cat");
		assertEquals("Error!",n.getTitle());
		
		//deleting with special characters
		n = (Notification) parse.parse("delete @$%&");
		assertEquals("Error!",n.getTitle());
		
		//delete normal category delete cat
		n = (Notification) parse.parse("delete cat");
		assertEquals("Tasks under [cat] categories have been deleted!",n.getMessage());
		
		//delete 2 categories
		n = (Notification) parse.parse("delete #cat test  CS2103 CS2101");
		assertEquals("Tasks under [test] categories have been deleted!",n.getMessage());

		//delete by dates is not possible unless it is name of category
		n = (Notification) parse.parse("delete 29Mar");
		assertEquals("Error!",n.getTitle());
		
		//delete delete should do nothing unless delete is a category
		n = (Notification) parse.parse("delete delete");
		assertEquals("Error!",n.getTitle());
		
		//delete by time is not possible unless it is name of category
		n = (Notification) parse.parse("delete 11pm 10:00am");
		assertEquals("Error!",n.getTitle());
		
		//delete nothing
		n = (Notification) parse.parse("delete ");
		assertEquals("Error!",n.getTitle());
		
		//delete all (to erase everything) is not possible unless all is a category
		n = (Notification) parse.parse("delete all");
		assertEquals("Error!",n.getTitle());
		
		//delete by description is not possible
		n = (Notification) parse.parse("delete <description of task>");
		assertEquals("Error!",n.getTitle());
		
		//delete by +
		n = (Notification) parse.parse("delete +");
		assertEquals("Error!",n.getTitle());
		
		//delete categories within double quotes is not possible
		n = (Notification) parse.parse("delete \"jm\" \'mj\'");
		assertEquals("Error!",n.getTitle());
		
	}

}
