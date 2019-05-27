/**
 * Team members:
 * Yifan Fan 962284
 * Chen(Christina) Xu 945756
 * Haonan Chen 930614
 * 
 * This class constructs the simulation of rebellion model
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class World {
	private Result result;
	private Patch[][] patches;
	private ArrayList<Turtle> turtles;
	private int numPatch = 0;
	private int numCop = 0;
	private int numAgent = 0;
	
	/****************extension***************/
	//used to store the original index of the killed agent.
	//-1 means no killed agent needs handling
	int killIndex=-1;
	/****************************************/

	
	//Initialize the World
	public World() {
		
		turtles = new ArrayList<Turtle>();
		result = new Result();
	    patches = new Patch[Setting.WORLD_WIDTH][Setting.WORLD_LENGTH];
	   
	    for (int i =0; i<Setting.WORLD_WIDTH; i++)
	    	for(int j =0; j< Setting.WORLD_LENGTH; j++) {
	    		patches[i][j] = new Patch();
	    	}
	    // Initialize the number of patch, cop and agent
	    numPatch = Setting.WORLD_WIDTH*Setting.WORLD_LENGTH;
	    numCop =(int)(Setting.INITIAL_COP_DENSITY *  numPatch);
	    numAgent =(int)(Setting.INITIAL_AGENT_DENSITY *  numPatch);	
	}
	
	/**
	 * Update the result for each tick of time
	 * */
	public void resultUpdate() {
		result.resetActive();
		result.resetJailed();
		for (Turtle t : turtles) {
			if(t.getType().equals("Agent")){
				if(((Agent)t).getJailTerm() > 0)
					result.addJailed();
				else if (((Agent)t).getActive())
					result.addActive();
				else 
					continue;	
			} 
		}
		result.resetQuiet();
	
	}
	
	/**
	 * Set up the initialization process
	 * Distribute cops and agents to patches randomly
	 */
	public void setUp() {

		int numTurtle = this.numCop + this.numAgent;
		Random r = new Random();

		int[] nums = r.ints(0, numPatch-1).distinct().limit(numTurtle).toArray();	
		
		for(int i = 0; i<=numTurtle-1; i++) {
			int row = nums[i] / Setting.WORLD_LENGTH;
			int remain = (nums[i] % Setting.WORLD_LENGTH);
			int col = 0;
			
		if(nums[i]!=0) {
			if (remain == 0) {
				col = Setting.WORLD_LENGTH-1;
				row -=1;
			}
			else {
				col = remain - 1;
			}
		}
			
		else {
				row = 0;
				col = 0;
				
			}
			
		if(i<this.numCop) {
				Cop cop = new Cop(this, row, col);
				this.moveTurtle(cop, row, col);
				turtles.add(cop);

				
		}
		else
			{
				Agent agent = new Agent(this, row, col);
				this.moveTurtle(agent, row, col);	

				turtles.add(agent);


			}			
	  }		
	}
	
	
	/**
	 * The primary method of the World, responsible for running the simulation in the loop
	 * After each simulation, write the data to the output file
	 */
	public void running() {
		for(int i =1;i<=Setting.MAX_TICK;i++) {
			result.addTick();
			this.render();
			 this.resultUpdate();
			 this.result.add();

		}
		this.result.writeFile();
	}
	
	
	/**
	 * Let all turtles act once in each tick of time
	 * */
	public void render() {
		//get the initial size of the turtles. If killing occurs, the size will change.
		int size=turtles.size();
		int index=0;
		Turtle t;
		//the loop continues when not all turtles have acted
		while(index<size) {
			t=turtles.get(index);
			if(t.getType().equals("Cop")) {
				((Cop)t).findTarget(); 

			}else {	
				((Agent)t).determineBehavior();
			}
			/*********************extension*********************/
			//if a cop has acted, it may kill an active agent.
			//the killed agent has been removed from the turtles list
			//and its original index is stored in killIndex
			if (killIndex!=-1) {
				//since an agent has been killed, the size of 
				//turtle list will become less.
				size--;
		 //if the index of killed agent is less than that of the cop, 
		// the index should keep the same because the index of the cop has become 1 less.
				if(killIndex>index) {
					index++;
				}
				//reset the killIndex
				killIndex=-1;
			}else {
				index++;	
			}
			/***************************************************/
		}
		
	}
		
	/**
	 * Get the list of turtles the patch holds
	 * If there is a cop, return the cop. 
	 * Otherwise, sort the list of agents according to their jail time 
	 * and return list of agents
	 * @param observeX the x-coordinate of patch
	 * @param observeY the y-coordinate of patch
	 * @return the list of sorted agents or cop
	 * */
	public ArrayList<Turtle> getPatchHolding(int observeX, int observeY) {
		Patch patch = patches[observeX][observeY];
		ArrayList<Turtle> turtles = patch.getHolding();
		ArrayList<Turtle> cops = new ArrayList<Turtle>();
		ArrayList<Turtle> agents = new ArrayList<Turtle>();
		for(Turtle t : turtles) {
			if (t.getType().equals("Cop")) 
				cops.add((Cop)t);
			else 
				agents.add((Agent)t);
		}
		
		Collections.sort(agents, Collections.reverseOrder());
		
		if (cops.size()!= 0)
			return cops;
		else 
			return agents;
	}

   /**
    * Move a turtle to its destination patch
    * @param turtle the turtle to be moved
    * @param newX the x-coordinate of the destination
    * @param newY the y-coordinate of the destination
    */
	public void moveTurtle(Turtle turtle, int newX, int newY) {

		Patch patch = patches[newX][newY];
		// solve the case when we need to move turtle to a patch which is 
		// different from the current patch 
		if(turtle.getX()!=newX || turtle.getY()!=newY) {

		Patch origin = patches[turtle.getX()][turtle.getY()];
		origin.free(turtle);
		turtle.setX(newX);
		turtle.setY(newY);
		patch.hold(turtle);
		}
		// solve the initial setting up of turtle on the patch, 
		// condition: turtle.getX()==newX && turtle.getY()==newY
		else {

			if(!patch.getHolding().contains(turtle))
				patch.hold(turtle);
				
		}
		
	}

	/*********************extension*********************/
	/**
	 * Kill a turtle by removing it from the world.
	 * @param turtle the turtle to be killed
	 */
	public void killTurtle(Turtle turtle) {
		Patch origin = patches[turtle.getX()][turtle.getY()];
		origin.free(turtle);
		//store the original index of this killed agent
		killIndex=turtles.indexOf(turtle);
		turtles.remove(turtle);
		this.result.addKill();
	}
	/***************************************************/

}
