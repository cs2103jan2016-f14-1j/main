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
		assertEquals(true, parser.returnValue);
		
		//assertEquals();
		
	}

}
