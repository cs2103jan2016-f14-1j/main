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
 * This test class approaches exploratory and white box testing.
 * Exploratory - design the test cases on the go. Do while testing
 * White Box - knowing what the code is going to do. Test after that. 
 * */		
		
/**
 * All add tests here
 * ====================================================================
 */
		// This is a test case for testing if the add function will add the following Strings
	    parser.parse("add addadd add");
	    assertEquals("Add Successful! addadd add has been added!", Parser.returnValue);
	    
	    // task with date
	    // date is parsed and removed from task name
		parser.parse("add task with date by 20Feb");
		assertEquals("Add Successful! task with date has been added!", Parser.returnValue);
	    
		// task with date and category
		// date and categories are parsed and removed from task name
		parser.parse("add task with date and category #test by 20Feb");
		assertEquals("Add Successful! task with date and category has been added!", Parser.returnValue);
	    
		// task with date, time, and category
		// date, time and categories are parsed and removed from task name
		parser.parse("add task with date and category #test by 20Feb from 8pm");
		assertEquals("Add Successful! task with date and category has been added!", Parser.returnValue);
		
		// task with date, time range, and category
		// date, time range and categories are parsed and removed from task name
		parser.parse("add task with date and category #test by 20Feb from 8pm to 10pm");
		assertEquals("Add Successful! task with date and category has been added!", Parser.returnValue);
		
		// task with date range and category
		// date range and categories are parsed and removed from task namme
		parser.parse("add task with date and category #test by 20Feb to 25Feb");
		assertEquals("Add Successful! task with date and category has been added!", Parser.returnValue);
		
/**
 * All delete tests here
 * ====================================================================
 */
		parser.parse("delete 1");
		assertEquals("Task(s) Deleted! (#1) addadd add  ", Parser.returnValue);
		
		parser.parse("delete 2");
		assertEquals("Task(s) Deleted! (#2) task with date  - 20Feb", Parser.returnValue);
		
		parser.parse("delete 3");
		assertEquals("Task(s) Deleted! (#3) task with date and category #test - 20Feb",Parser.returnValue);
		
/**
 * All undo tests here
 * ====================================================================
 */
		parser.parse("undo");
		assertEquals(true, Parser.returnValue);
		
/**
 * All complete tests here
 * ====================================================================
 *  
 */
		//mark task 4 as done, action: move task 4 to done list.
		//input:
		//action:
		//result:
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
