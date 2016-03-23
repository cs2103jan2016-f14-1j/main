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
		
		// This is a test case for testing if the add function will add the following Strings
	    parser.parse("add addadd add");
	    // True to pass
		assertEquals(true, Parser.returnValue);
	    
		parser.parse("add task with date by 20Feb");
	    // True to pass
		assertEquals(true, Parser.returnValue);
		
		parser.parse("add task with date and category #test by 20Feb");
	    // True to pass
		assertEquals(true, Parser.returnValue);
		
		parser.parse("add task with date and category #test by 20Feb from 8pm");
		assertEquals(true, Parser.returnValue);
		
		parser.parse("add task with date and category #test by 20Feb from 8pm to 10pm");
		assertEquals(true, Parser.returnValue);
		
		parser.parse("add task with date and category #test by 20Feb to 25Feb");
		assertEquals(true, Parser.returnValue);
		
		parser.parse("delete 1");
		assertEquals(true, Parser.returnValue);
		
		parser.parse("delete 2");
		assertEquals(true, Parser.returnValue);
		
		parser.parse("delete 3");
		assertEquals(true, Parser.returnValue);
		
		parser.parse("undo");
		assertEquals(true, Parser.returnValue);
		
		parser.parse("do 4");
		assertEquals(true, Parser.returnValue);
		
		parser.parse("do 3 5");
		assertEquals(true, Parser.returnValue);
		
		parser.parse("search task");
		assertEquals(true, Parser.returnValue);
		
		parser.parse("view not done");
		assertEquals(true, Parser.returnValue);
	
		parser.parse("view done");
		assertEquals(true, Parser.returnValue);
		
	    parser.parse("view test");
	    assertEquals(true, Parser.returnValue);
		
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
