/**
 * Team members:
 * Yifan Fan 962284
 * Chen(Christina) Xu 945756
 * Haonan Chen 930614
 * 
 * The Settings of the model, users can manipulate the variables here
 * to change the Settings of model simulation
 */
public class Setting {
	//the Length of the worlds(x)
	public static int WORLD_LENGTH = 40;
	//the Width of the worlds(y)
	public static int WORLD_WIDTH = 40;
	
	//factor for determining arrest probability
	public static float ARREST_PROBABILITY_FACTOR = (float) 2.3;
	//by how much must G > N to make someone rebel?
	public static float THRESHOLD = (float) 0.1;
	
	//affect the number of cops
	public static float INITIAL_COP_DENSITY = (float) 0.04;
	//affect the number of agents
	public static float INITIAL_AGENT_DENSITY = (float) 0.7;
	
	//affect the patches that cops and agents can see
	public static float VISION = (float)7.0;
	//to which degree agents support the government
	public static float GOVERNMENT_LEGITIMACY = (float) 0.82;
	//how many ticks an agent should be kept once arrested
	public static int MAX_JAIL_TERM = 30;
	
	//whether agents are allowed to move
	public static boolean MOVEMENT = true;
	// the number of loops run in the system
    public static int MAX_TICK = 1000;
    
    // the number of elements add in results per iteration
    public static int NUM_ELE = 6;
    
    // the name of output file
    public static String fileName = "results.csv";
    /***************************extension*******************************/
    //The probability of the situation that a cop kills an active agent.
    public static float KILLING_FACTOR = (float)0.0;
    /*******************************************************************/
}
