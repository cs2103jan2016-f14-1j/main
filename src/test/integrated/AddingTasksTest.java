//@@author A0076520L

package test.integrated;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;
import org.ocpsoft.prettytime.shade.edu.emory.mathcs.backport.java.util.Arrays;

import logic.Notification;
import parser.ParseAdd;
import parser.Parser;
import shared.Task;
import storage.Storage;

public class AddingTasksTest {


	ArrayList<Task> tasks = new ArrayList<Task>();
	
	@Test
	public void addTasksTest() {
		Parser parser = Parser.getInstance();
		Storage storage = Storage.getInstance();
		//ArrayList<Task> tasks = new ArrayList<Task>();

		// Floating tasks with commands
		Notification n = (Notification) parser.parse("add add add add addadd");
		Task t = new Task();
		t.setTask("add add add addadd");
		tasks.add(t);
		assertTrue(t.like(ParseAdd.getTask()));

		n = (Notification) parser.parse("add undo complete delete do view search");
		t = new Task();
		t.setTask("undo complete delete do view search");
		tasks.add(t);
		assertTrue(t.like(ParseAdd.getTask()));
		
		//add task with #11pm as category
		n = (Notification) parser.parse("add this task #11pm");
		t = new Task();
		t.setTask("this task");
		String[] ss = { "11pm" };
		ArrayList<String> cats = new ArrayList<String>(Arrays.asList(ss));
		t.setCategories(cats);
		tasks.add(t);
		assertTrue(t.like(ParseAdd.getTask()));

		// Floating tasks with special characters and categories
		n = (Notification) parser.parse("add \\/>#,|<.@~$%^&*!)(+_-|}{\";:");
		t = new Task();
		t.setTask("\\/>#,|<.@~$%^&*!)(+_-|}{\";:");
		tasks.add(t);
		assertTrue(t.like(ParseAdd.getTask()));

		n = (Notification) parser.parse("add test~1 #)(!# #test(#@# #####");
		t = new Task();
		t.setTask("test~1");
		String[] s = { ")", ")(!#", "test(#@#", "####" };
		ArrayList<String> cat = new ArrayList<String>(Arrays.asList(s));
		t.setCategories(cat);
		tasks.add(t);
		assertTrue(t.like(ParseAdd.getTask()));

		// Tasks with date, special characters, categories

		n = (Notification) parser.parse("add thistime#withnospace by 23 Mar");
		t = new Task();
		t.setTask("thistime#withnospace");
		t.setIntDate(87);
		System.out.println(ParseAdd.getTask().getDate() + " asd " + ParseAdd.getTask().getIntDate() + " haha "
				+ ParseAdd.getTask().getDisplayDate() + " d " + ParseAdd.getTask().getIntDateEnd() + " d "
				+ ParseAdd.getTask().getIntEndTime() + " a" + ParseAdd.getTask().getIntStartTime() + " end");
		tasks.add(t);
		assertTrue(t.like(ParseAdd.getTask()));

		/*
		// Task with date time, require discussion to test on these
		n = (Notification) parser.parse("add thistime#withnospace by 23 Mar 3pm to 5pm");
		assertTrue(t.like(ParseAdd.getTask()));
		
		n = (Notification) parser.parse("add thistime#withnospace by 23 Mar on 3pm");
		t = new Task();
		t.setTask("thistime#withnospace");
		t.setIntDate(87);
		ArrayList<Date> ad = Formatter.getDateTimes("add thistime#withnospace by 23 Mar on 3pm");
		t.setDateTimes(ad);
		for (Date d : ad) {
			System.out.println(d + " aweeeeee");
		}
		for (Date d : ParseAdd.getTask().getDatetimes()) {
			System.out.println(d);
		}
		System.out.println(ParseAdd.getTask().getDate() + " asd " + ParseAdd.getTask().getIntDate() + " haha "
				+ ParseAdd.getTask().getDisplayDate() + " d " + ParseAdd.getTask().getIntDateEnd() + " d "
				+ ParseAdd.getTask().getIntEndTime() + " a" + ParseAdd.getTask().getIntStartTime() + " end");
		tasks.add(t);
		assertTrue(t.like(ParseAdd.getTask()));
 		*/
		//Check if each task in temp storage and actual storage are the same
		for(Task sTask: Storage.getListOfTasks()){
			boolean test = false;
			for(Task te: tasks){
				if(te.like(sTask)){
					test = true;
					break;
				}
			}
			assertTrue(test);
		}
		
		
		// Inputs that would definitely result in errors
		// Floating task with empty string
		n = (Notification) parser.parse("add ");
		assertEquals("Error!", n.getTitle());
		/*
		n = (Notification) parser.parse("add #withnospace by 23 Mar 3pm to 5pm");
		assertEquals("Error!", n.getTitle());
		n = (Notification) parser.parse("add ##### by 23 Mar");
		assertEquals("Error!", n.getTitle());
		
		//descriptions carried at the back
		n = (Notification) parser
				.parse("add tesing this task ##### by 23 Mar #### #category1 @!(#)&!)(@*$)(!@*#98)@#*()@#)}{[]");
		assertEquals("Error!", n.getTitle());
		
		//lack of prepositions at time
		n = (Notification) parser.parse("add testing \\/>#,|<.@~$%^&*!)(+_-|}{\";: this task by 23 Mar 3pm");
		assertEquals("Error!", n.getTitle());
		*/
	}

}
