package test;

import logic.*;
import parser.*;
import java.util.Date;
import java.util.ArrayList;
import shared.*;
import storage.Storage;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestFile {
	Parser parser = Parser.getInstance();
	Logic logic = Logic.getInstance();
	Storage storage = Storage.getInstance();
	@Test
	public void test() {
		
/**
 * All add tests here
 * ====================================================================
 */
		// This is a test case for testing if the add function will add the following Strings
	    parser.parse("add addadd add");
		assertEquals(true, Parser.returnValue);
	    assertEquals("Add Successful! addadd add has been added!", Notification.getTitle() + " "+Notification.getMessage());
	    
		parser.parse("add task with date by 20Feb");
		assertEquals(true, Parser.returnValue);
		assertEquals("Add Successful! task with date has been added!", Notification.getTitle() + " "+Notification.getMessage());
	    
		parser.parse("add task with date and category #test by 20Feb");
		assertEquals(true, Parser.returnValue);
		assertEquals("Add Successful! task with date and category has been added!", Notification.getTitle() + " "+Notification.getMessage());
	    
		parser.parse("add task with date and category #test by 20Feb from 8pm");
		assertEquals(true, Parser.returnValue);
		assertEquals("Add Successful! task with date and category has been added!", Notification.getTitle() + " "+Notification.getMessage());
		
		parser.parse("add task with date and category #test by 20Feb from 8pm to 10pm");
		assertEquals(true, Parser.returnValue);
		assertEquals("Add Successful! task with date and category has been added!", Notification.getTitle() + " "+Notification.getMessage());
		
		parser.parse("add task with date and category #test by 20Feb to 25Feb");
		assertEquals(true, Parser.returnValue);
		assertEquals("Add Successful! task with date and category has been added!", Notification.getTitle() + " "+Notification.getMessage());
		
/**
 * All delete tests here
 * ====================================================================
 */
		parser.parse("delete 1");
		assertEquals(true, Parser.returnValue);
		assertEquals("Task(s) Deleted! (#1) addadd add  ", Notification.getTitle() + " "+Notification.getMessage());
		
		parser.parse("delete 2");
		assertEquals(true, Parser.returnValue);
		assertEquals("Task(s) Deleted! (#2) task with date - 20Feb", Notification.getTitle() + " "+Notification.getMessage());
		
		parser.parse("delete 3");
		assertEquals(true, Parser.returnValue);
		
/**
 * All undo tests here
 * ====================================================================
 */
		parser.parse("undo");
		assertEquals(true, Parser.returnValue);
		
/**
 * All complete tests here
 * ====================================================================
 */
		parser.parse("do 4");
		assertEquals(true, Parser.returnValue);
		
		parser.parse("do 3 5");
		assertEquals(true, Parser.returnValue);
		
/**
 * All search tests here
 * ====================================================================
 */
		parser.parse("search task");
		assertEquals(true, Parser.returnValue);
		
		parser.parse("view");
/**
 * All View tests here
 * ====================================================================
 */
		parser.parse("view not done");
		assertEquals(true, Parser.returnValue);
	
		parser.parse("view done");
		assertEquals(true, Parser.returnValue);
		
	    parser.parse("view test");
	    assertEquals(true, Parser.returnValue);
	    
/**
 * All edit tests here
 * ====================================================================
 */
	    // task description
	    parser.parse("edit 3 to beat sam");
	    assertEquals(true, Parser.returnValue);
	    
	    // task time
	    parser.parse("edit 3 to 8pm");
	    assertEquals(true, Parser.returnValue);
	
	    // task date
	    parser.parse("edit 3 to 11Jan");
	    assertEquals(true, Parser.returnValue);
	
	}

}
