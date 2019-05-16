import static org.junit.Assert.*;

import org.junit.Test;

public class ScheduleTests {

	@Test
	public void ProfTest() {
	
		Schedule schedule = new Schedule();
		schedule.insert(8); //adds job 0 with time 8
		Schedule.Job j1 = schedule.insert(3); //adds job 1 with time 3
		schedule.insert(5); //adds job 2 with time 5
		assertEquals(8, schedule.finish()); //should return 8, since job 0 takes time 8 to complete.
		/* Note it is not the earliest completion time of any job, but the earliest the entire set can complete. */
		schedule.get(0).requires(schedule.get(2)); //job 2 must precede job 0
		assertEquals(13, schedule.finish()); //should return 13 (job 0 cannot start until time 5)
		schedule.get(0).requires(j1); //job 1 must precede job 0
		assertEquals(13, schedule.finish()); //should return 13
		schedule.get(0).start(); //should return 5
		assertEquals(j1.start(), 0); //should return 0
		assertEquals(0, schedule.get(2).start()); //should return 0
		j1.requires(schedule.get(2)); //job 2 must precede job 1
		assertEquals(16, schedule.finish()); //should return 16
		assertEquals(8, schedule.get(0).start()); //should return 8
		assertEquals(5, schedule.get(1).start()); //should return 5
		assertEquals(0, schedule.get(2).start()); //should return 0
		schedule.get(1).requires(schedule.get(0)); //job 0 must precede job 1 (creates loop)
		assertEquals(-1, schedule.finish()); //should return -1
		assertEquals(-1, schedule.get(0).start()); //should return -1
		assertEquals(-1, schedule.get(1).start()); //should return -1
		assertEquals(0, schedule.get(2).start()); //should return 0 (no loops in prerequisites)
	}

	@Test
	public void medTest() {
		
		Schedule schedule = new Schedule(); 
		schedule.insert(1); //Job 0
		schedule.insert(1); //Job 1
		schedule.insert(2); //Job 2
		schedule.insert(8); //Job 3
		
		assertEquals(0, schedule.get(0).start()); //should be 0 
		assertEquals(0, schedule.get(1).start()); //should be 0 
		assertEquals(0, schedule.get(2).start()); //should be 0 
		assertEquals(0, schedule.get(3).start()); //should be 0
		
		assertEquals(8, schedule.finish()); //should be 8 since Job 3 requires 8
		
		schedule.get(2).requires(schedule.get(1)); 
		assertEquals(8, schedule.finish()); //Should still be 8
		assertEquals(1, schedule.get(2).start());
		
		
		
		schedule.get(1).requires(schedule.get(0));
		assertEquals(2, schedule.get(2).start()); //chains of 3 jobs, job 2 req Job 1  which req Job 0 1 + 1 = 2
		assertEquals(1, schedule.get(1).start()); //should be 1 since Job 1 requires Job 0 with time of 1
		
		schedule.get(3).requires(schedule.get(2)); 
		assertEquals(4, schedule.get(3).start()); 
		assertEquals(12, schedule.finish()); 
		
		schedule.get(3).requires(schedule.get(0));
		schedule.get(3).requires(schedule.get(1));
		assertEquals(4, schedule.get(3).start()); 
		assertEquals(12, schedule.finish());

		schedule.get(1).requires(schedule.get(2)); 
		assertEquals(-1, schedule.get(2).start()); 
		assertEquals(0, schedule.get(0).start()); 
		assertEquals(-1, schedule.finish());
		assertEquals(-1, schedule.get(3).start()); 
	}
	
	@Test
	public void lrgTest() {
		
		Schedule schedule = new Schedule();
		Schedule.Job j0 = schedule.insert(3); //Job 0
		Schedule.Job j1 = schedule.insert(5); //Job 1
		Schedule.Job j2 = schedule.insert(4); //Job 2
		Schedule.Job j3 = schedule.insert(6); //Job 3
		Schedule.Job j4 = schedule.insert(2); //Job 4
		Schedule.Job j5 = schedule.insert(13); //Job 5
		Schedule.Job j6 = schedule.insert(4); //Job 6
		assertEquals(13, schedule.finish()); 
		
		j6.requires(j5); 
		assertEquals(17, schedule.finish()); 
		assertEquals(13, j6.start()); 
		
		j5.requires(j0); //j5 start should be 3 now, finish should be 20
		assertEquals(20, schedule.finish()); //3 added to previous finish time of 17... 
		assertEquals(3, j5.start()); 
		assertEquals(16, j6.start()); //now j5 start is 3, j6 start should be 16
		
		j0.requires(j3); 
		assertEquals(0, j3.start());
		assertEquals(6, j0.start()); 
		assertEquals(26, schedule.finish());
		assertEquals(9, j5.start()); 
		assertEquals(22, j6.start()); 
		
		j0.requires(j4); 
		//should be no change since j3 T = 6 and j4 T = 2, j0 still hast to wait for j3(6) since it's longer
		assertEquals(0, j3.start());
		assertEquals(6, j0.start()); 
		assertEquals(26, schedule.finish());
		assertEquals(9, j5.start()); 
		assertEquals(22, j6.start()); 
		
		j4.requires(j1);
		assertEquals(0, j1.start()); 
		assertEquals(5, j4.start());
		assertEquals(7, j0.start());
		assertEquals(10, j5.start()); 
		assertEquals(23, j6.start());
		assertEquals(27, schedule.finish());
		
		j4.requires(j2);
		//should produce no change since j1 time > j2
		assertEquals(0, j1.start()); 
		assertEquals(5, j4.start());
		assertEquals(7, j0.start());
		assertEquals(10, j5.start()); 
		assertEquals(23, j6.start());
		assertEquals(27, schedule.finish());
		
		j1.requires(j2); 
		assertEquals(4, j1.start());
		assertEquals(0, j2.start()); 
		assertEquals(9, j4.start());
		assertEquals(11, j0.start());
		assertEquals(14, j5.start()); 
		assertEquals(27, j6.start());
		assertEquals(31, schedule.finish());
		
		Schedule.Job j7 = schedule.insert(39); //Job 7
		assertEquals(39, schedule.finish());
		
		j7.requires(j0);
		assertEquals(53, schedule.finish());
		assertEquals(14, j7.start()); 
		
		Schedule.Job j8 = schedule.insert(15);
		Schedule.Job j9 = schedule.insert(100);
		Schedule.Job j10 = schedule.insert(40);
		
		assertEquals(100, schedule.finish()); 
		j9.requires(j8); 
		j8.requires(j9); 
		assertEquals(14, j7.start()); //nodes still in cycle .start() not affected
		assertEquals(-1, j8.start()); //start of node in cycle won't work
		assertEquals(-1, schedule.finish()); //entire schedule won't work 
		
	}
	
	
	
}
