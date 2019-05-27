/**
 * 
 * The main class executes the simulation of model
 */
public class Main {

	private static World world;
	
	public static void main(String[] args) {
		//The sum of densities of agents and cops should not be more than one.
		if (Setting.INITIAL_AGENT_DENSITY+
				Setting.INITIAL_COP_DENSITY<=1){
			//initialize a new world 
			world = new World();
			//starts the world
			world.setUp();
			//runs the world
			world.running();
			System.out.println("Rebellion Simulation is done.");
		}else {
			System.out.println("The sum of densities of agents"
					+ " and cops should not be more than one.");
		}
	}
		
}

