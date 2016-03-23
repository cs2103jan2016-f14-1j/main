import static org.junit.Assert.*;

import java.util.ArrayList;

import logic.*;
import shared.*;
public class Test extends Logger {

	@org.junit.Test
	public void test() {
		Logic logic = Logic.getInstance();
		
		ArrayList<Integer> ids = null;
		logf("junit test","trying to delete null task");
		assert(ids == null);
		try{
			logic.deleteTask(ids, null); // one is null (assertion fail)
			//logic.deleteTask(new ArrayList<Integer>(), new ArrayList<String>());
		}catch(Exception e){
			logf("EXCEPTION CAUGHT","delete task exception");
			System.exit(0);
		}
	}

}
