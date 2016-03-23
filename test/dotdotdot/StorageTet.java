package dotdotdot;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import shared.Task;
import storage.FreeIDs;
import storage.Storage;

public class StorageTet {

	//White-Box testing
	@Test
	public void testAddTask() {
		Storage s = Storage.getInstance();
		//try adding a task to the storage list
		Task task = new Task();
		s.addTaskToList(task);
		ArrayList<Task> ar = s.getListOfTasks();
		assertEquals(1, ar.size());
	}
	
	@Test
	public void testRemoveTask(){
		Storage s = Storage.getInstance();
		//try remove a task to the storage list
		Task task = new Task();
		s.removeTaskFromList(0);
		ArrayList<Task> ar = s.getListOfTasks();
		assertEquals(0, ar.size());
	}
	
	@Test
	public void testRemoveWhenEmpty(){
		Storage s = Storage.getInstance();
		//assertEquals("",s.removeTaskFromList(0));
		
	}
	
	@Test
	public void testFreeID(){
		//if there are no IDs, getNextAvailID will get the next ID
		FreeIDs f = new FreeIDs();
		assertEquals(1,f.getNextAvailID());
	}

}
