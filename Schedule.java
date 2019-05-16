import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Schedule {

	ArrayList<Job> jobs; 
	Job end; 
	boolean freshlySorted; 

	public Schedule()
	{
		jobs = new ArrayList<Job>(); 
		end = new Job(0);
		jobs.add(end); 
		end.mySchedule = this; 
	}

	public Job insert(int time){

		Job newJob = new Job(time);
		newJob.mySchedule = this; 
		jobs.add(newJob);
		end.requires(newJob); 
//		freshlySorted = false; 

		return newJob; 
	}
	

	public Job get(int index) {

		return jobs.get(index + 1); 
	}

	//return the earliest possible completion time for the entire schedule 
	public int finish() {

		if (!freshlySorted) 
			topSort(); 
		if (end.inDeg == 0)
			return end.startTime; 
		else
			return -1; 
	}	

	//Returns an Topologically ordered ArrayList of jobs 
	public ArrayList<Job> topSort() {
		for (Job j : jobs) {
			j.inDeg = j.requiredJobs.size(); //constant time
		}
		ArrayList<Job> topOrderedJobs = new ArrayList<>();  
		Queue<Job> q = new LinkedList<Job>(); 
		for (Job j : jobs) //j jobs in schedule
			if (j.inDeg == 0) 
				q.add(j);

		while (q.size() > 0) {
			Job j = q.remove(); 
			topOrderedJobs.add(j); 
			for (Job k : j.requiresMe) { 
				relax(j, k); 
					if (k.inDeg - 1 >= 0) //End's inDeg needs to be addressed... start time too? 
						k.inDeg--; 
				if (k.inDeg == 0)
					q.add(k);
			}
		}
		
		freshlySorted = true; 
		return topOrderedJobs; 
	}

	public static boolean relax(Job j, Job nextJ)
	{
		if (j.startTime + j.time > nextJ.startTime) {
			nextJ.startTime = j.startTime + j.time; 
			nextJ.predecessor = j; 
		}
		return true;
	}

	class Job{

		Schedule mySchedule; 
		int time;
		int startTime;//finish time of preceding required
		ArrayList<Job> requiredJobs; //comes before IN adjacency list
		ArrayList<Job> requiresMe; //outgoing
		Job predecessor; 
		//		boolean added; 

		int inDeg; 

		private Job(int theTime) {

			this.time = theTime; 
			requiredJobs = new ArrayList<Job>(); 
			requiresMe = new ArrayList<Job>(); 
			startTime = 0; 

		}

		public void requires(Job j){
			requiredJobs.add(j); 
			j.requiresMe.add(this);
			this.mySchedule.freshlySorted = false; 
		}

		//returns the earliest possible time for the job 
		public int start() {

			if (!this.mySchedule.freshlySorted)
				this.mySchedule.topSort();
			
			if (this.inDeg != 0)
				return -1; 
			else
				return this.startTime; 
		}
	}
}