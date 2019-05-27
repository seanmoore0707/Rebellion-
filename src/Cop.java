/**
 * 
 * This class represents the cops who keep finding 
 * active agents to arrest.
 * 
 */
import java.util.ArrayList;
import java.util.Random;


public class Cop extends Turtle {

	//index for getting x or y
	private final int X=0;
	private final int Y=1;
	
	/**
	 * initiate a cop
	 * @param world the world associated with cop
	 * @param xPosition x-coordinate position of cop
	 * @param yPosition y-coordinate position of cop
	 */
	public Cop(World world,int xPosition,int yPosition) {
		super(world, xPosition, yPosition);
	}
	
	/**
	 * get the type of the turtle.
	 * @return the type of turtle
	 */
	@Override
	public String getType() {
		return "Cop";
	}
	
	/**
	 * randomly move to an unoccupied patch
	 */
	@Override
	public void move() {
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
	/**
	 * add the valid location for cops to move
	 * if forMove is true, all occupied patches will be filtered out.
	 * if forMove is false, all patches without active agents will be filtered out.
	 * @param formove decide whether the cop can move
	 * @param observeX x-coordinate of the current patch
	 * @param observeY y-coordinate of the current patch
	 * @param result a list of locations that are valid for cops to move
	 */

	public void addLocation(boolean formove, int observeX, 
			int observeY, ArrayList<int[]> result ){
		
		int[] location;
		ArrayList<Turtle> people=this.getWorld().
				getPatchHolding(observeX,observeY);
		
		if(formove) {
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
		}else {
			if(people.size()!=0) {
				boolean flag=false;
				for (Turtle person : people) {
		//if there is at least one active agent, it should be considered.
					if (person.getType().equals("Agent")
							&&((Agent)person).getActive()) {
						flag=true;
						break;
					}
				} 
				if(flag) {
					location= new int[] {observeX,observeY};
					result.add(location);
				}
			}
		}
		
	}

	/**
	 * return all active agents in the given patches
	 * @param patches the list of patches to find targets
	 * @return the list of valid agent targets to catch
	 */
	public ArrayList<Turtle> potentialTargets(ArrayList<int[]> patches){
		ArrayList<Turtle> result = new ArrayList<Turtle>();
		int[] location;
		for(int i=0;i<patches.size();i++) {
			location=patches.get(i);
			ArrayList<Turtle> people=this.getWorld().
					getPatchHolding(location[X], location[Y]);
			for(Turtle t:people) {
				//add the active agents
				if(t.getType().equals("Agent")&&((Agent)t).getActive()) {
					result.add(t);
				}
			}
		}
		return result;
	}
	
	/**
	 * arrests an active agent
	 * @param agent an active agent
	 */
	public void arrest(Turtle agent) {
		//goes to the patch where there is the arrested agent
		this.getWorld().moveTurtle(this,agent.getX(),agent.getY());

		//arrest the active agent
		((Agent)agent).beArrested();

	}
	
	/**
	 * find an active agent to arrest
	 * executes in every tick
	 */
	public void findTarget() {
		//move first
		move();
		//get all visible patches containing at least one active agent
		ArrayList<int[]> patchIndex=observe(false);
		//get all active agents in the patches
		ArrayList<Turtle> targets=potentialTargets(patchIndex);
		//if a cop cannot see any active agent, it does nothing.
		if(targets.size()!=0) {
			Random r=new Random();
			//randomly choose a patch containing an active agent
			int index=r.nextInt(targets.size());
			/****************extension related*********************/
			//randomly decide whether kill or not
			if(r.nextFloat()<Setting.KILLING_FACTOR) {
				//Kill the active agent
				kill(targets.get(index));
			}else {
				//Arrest the active agent
				arrest(targets.get(index));
			}
			/*****************************************************/
		}
	}
	
	/*******************extension**************************/
	/**
	 * Kill an active agent instead of arresting.
	 * @param agent the agent selected by the cop
	 */
	public void kill(Turtle agent) {
		//goes to the patch where there is the arrested agent
		this.getWorld().moveTurtle(this,agent.getX(),agent.getY());
		//kill the active agent
		((Agent)agent).beKilled();
	}
	/******************************************************/

}
