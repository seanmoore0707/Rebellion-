/**
 * 
 * This class represents the agents who can be 
 * active, quiet and jailed because of being active.
 * 
 */
import java.util.ArrayList;
import java.util.Random;

public class Agent extends Turtle implements Comparable<Agent>{
	
	//index for getting numbers of cops and active agents around 
	private final int COPS_AROUND=0;
	private final int ACTIVE_AROUND=1;
	
	//index for getting x or y
	private final int X=0;
	private final int Y=1;
	
	//the parameters used in NetLogo
	private float riskAversion;
	private float perceivedHardship;
	private float grievance;
	private boolean active;
	private int jailTerm;
	
	/**
	 * initiate an agent
	 * @param world the world associated with agent
	 * @param xPosition x-coordinate position of agent
	 * @param yPosition y-coordinate position of agent
	 */
	public Agent(World world,int xPosition,int yPosition) {
		super(world, xPosition, yPosition);
		riskAversion=(float)Math.random();
		perceivedHardship=(float)Math.random();
		grievance=perceivedHardship * 
				(1 - Setting.GOVERNMENT_LEGITIMACY);
		active=false;
		jailTerm=0;
	}
	
	/**
	 * get the type of the turtle
	 * @return 	type of turtle
	 */
	@Override
	public String getType() {
		return "Agent";
	}

	/**
	 * agent tries to move to another patch
	 */
	@Override
	public void move() {
		//Whether an agent can move depends on whether it is allowed by setting 
		//and whether the agent is jailed.
		if(Setting.MOVEMENT && jailTerm==0) {
			ArrayList<int[]> patchIndex=observe(true);
			if(patchIndex.size()!=0) {
				Random r=new Random();
				//randomly choose an unoccupied patch to move
				int index= r.nextInt(patchIndex.size());
				int newX=(patchIndex.get(index))[X];
				int newY=(patchIndex.get(index))[Y];
				this.getWorld().moveTurtle(this, newX,newY);

			}
		}

	}
	
	/**
	 * add the valid location for agents to move
	 * if forMove is true, those occupied patches will be filtered out.	
	 * if forMove is false, no patch will be filtered out.
	 * @param formove decide whether the agent can move
	 * @param observeX x-coordinate of the current patch
	 * @param observeY y-coordinate of the current patch
	 * @param result a list of locations that are valid for agents to move
	 */
	public void addLocation(boolean formove, int observeX, 
			int observeY, ArrayList<int[]> result ){
		
		int[] location;
		if(formove) {
			//only return unoccupied patches
			ArrayList<Turtle> people=this.getWorld().
					getPatchHolding(observeX,observeY);
			
			
			if(people.size()==0) {
				//No turtle is in this patch, so it is unoccupied.
				location= new int[] {observeX,observeY};
				result.add(location);
			}
			else{
				boolean flag=true;
				//Free agents and cops can occupy a patch.
				for (Turtle person : people) {
					if (person.getType().equals("Cop")) {
						flag=false;
						break;
					}else if(((Agent)person).getJailTerm()==0){
						flag=false;
						break;
					}
				} 
				if(flag) {
					location= new int[] {observeX,observeY};
					result.add(location);
				}
			}
		}
		
		else {
			location= new int[] {observeX,observeY};
			result.add(location);
		}
		
	}

	/**
	 * an agent becomes active in rebelling
	 */
	public void rebel() {
		active=true;
	}
	
	/**
	 * an agent becomes quiet and stops rebelling
	 */
	public void calm() {
		active=false;
	}
	
	/**
	 * see how many cops and active agents around
	 * @return the array recording the number of cops and active agents around
	 */
	public int[] observeOthers(){
		ArrayList<int[]> patchIndex=observe(false);
		ArrayList<Turtle> people;

		int[] index;
		int[] result= new int[2];
		//number of cops around
		result[COPS_AROUND]=0;
		//number of active agents around
		result[ACTIVE_AROUND]=0;
		
		for(int i=0;i<patchIndex.size();i++) {
			index=patchIndex.get(i);
			people=this.getWorld().getPatchHolding(index[X],index[Y]);
			
			if(people.size()!=0) {
				for(Turtle person : people) {
					if(person.getType().equals("Cop")) {
						//newly find a cop around
						result[COPS_AROUND]++;
						
					}else if(person.getType().equals("Agent")) {
						if(((Agent)person).getActive()) {
							//newly find an active agent around
							result[ACTIVE_AROUND]++;
						}
					}
				}
				
			}			
		}
		//the active agents found by an active agent 
		//should exclude itself. 
		if (this.getActive()) result[ACTIVE_AROUND]--;
		return result;
		
	}
	
	/**
	 * an agent tries to decide what to do in every tick.
	 */
	public void determineBehavior() {

		if(jailTerm>0) {
			//If the agent is jailed, he can do nothing
			//but the term will minus 1.
			jailTerm--;

		}else {
			//free agent goes to a new place
			move();
			int[] situation=observeOthers();
			float estimatedArrestProbability=(float)
					(1 - Math.exp(- Setting.ARREST_PROBABILITY_FACTOR * 
					(situation[COPS_AROUND] /(1+ situation[ACTIVE_AROUND]))));
			//decides whether rebels or not
			if(grievance - riskAversion * estimatedArrestProbability > 
				Setting.THRESHOLD) {
				rebel();
			}else {
				calm();
			}
		}
	}
	
	
	/**
	 * get the active state of an agent
	 * @return whether the agent is active
	 */
	public boolean getActive() {
		return active;
	}
	
	/**
	 * an agent gets arrested because of rebelling
	 */
	public void beArrested() {
		active=false;
		jailTerm=new Random().nextInt(Setting.MAX_JAIL_TERM)+1;
	}
	
	/**************extension***************************/
	/**
	 * an agent gets killed because of rebelling
	 */
	public void beKilled() {
		this.getWorld().killTurtle(this);
	}
	/**************************************************/
	
	/**
	 * get the jail terms left, 0 means free.
	 * @return the number of jail term left
	 */
	public Integer getJailTerm() {
		return jailTerm;
	}

	//used to compare the jail terms of agents
	@Override
	public int compareTo(Agent o) {
		return this.getJailTerm().compareTo(o.getJailTerm());
	}
	
	
	
}
