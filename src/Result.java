/**
 * Team members:
 * Yifan Fan 962284
 * Chen(Christina) Xu 945756
 * Haonan Chen 930614
 * 
 * This class executes the functionality of writing output into a csv file
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class Result {
	//number of iteration
	private int tick;
	//number of patches
	private int patches;
	//number of cops
	private int cops;
	//number of total agents
	private int agents;
	//number of quiet agents
	private int quiet;
	//number of agents in jail
	private int jailed;
	//number of active agents
	private int active;
	//number of killed agents
	private int kill;
	//array list of results
	private ArrayList<Integer> results;
	
	// Initialization of attributes in result  
	public Result () {
		this.tick = 0;
		this.patches = Setting.WORLD_LENGTH * Setting.WORLD_WIDTH;
		this.cops = (int) (Setting.INITIAL_COP_DENSITY * patches);
		this.agents = (int) (Setting.INITIAL_AGENT_DENSITY * patches);
		this.quiet = this.agents;
		this.jailed = 0;
		this.active = 0;
		this.kill = 0;

		this.results = new ArrayList<Integer>();
		// Add the initial values of attributes of result to the output
		results.add(tick);
		results.add(cops);
		results.add(quiet);
		results.add(jailed);
		results.add(active);
		results.add(kill);
	}
	
	/**
	 * initialize the output file
	 * @param bw the BufferedWriter of the output file
	 * @throws IOException 
	 */
	public void initial(BufferedWriter bw) throws IOException {
		//the header contains initial parameters
		String header = String.format("Initialisation parameters \n"+"patches,"
				+ "initial-cop-density," +"initial-agent-density, vision,"
				+ "government-legitimacy,"+"max-jail-term," + "killing-factor\n");
		
		String initialData = String.format("%d,%f,%f,%f,%f,%d,%f\n", 
							patches, Setting.INITIAL_COP_DENSITY, 
							Setting.INITIAL_AGENT_DENSITY,
							Setting.VISION, Setting.GOVERNMENT_LEGITIMACY, 
							Setting.MAX_JAIL_TERM, Setting.KILLING_FACTOR);
		
		String subHeader = String.format("Time,cops,quiet,jailed,active,"
				+ "killed\n");
		//write the header to the output file
		bw.append(header);
		bw.append(initialData);
		bw.append(subHeader);
	}
	
	
	/**
	 * add current numbers to the result list
	 */
	public void add() {
		results.add(tick);
		results.add(cops);
		results.add(quiet);
		results.add(jailed);
		results.add(active);
		/*********************extension*********************/
		results.add(kill);
		/***************************************************/
	}
	
	/**
	 * Write results to the file
	 */
	public void writeFile() {
		File outputFile = new File(Setting.fileName);
		BufferedWriter bw = null;
	        try {
	        	bw = new BufferedWriter(
	                    new OutputStreamWriter(new FileOutputStream(outputFile)));
	            initial(bw);
	            for (int i = 0; i < results.size()/Setting.NUM_ELE; i++) {
	            	for (int k=0; k<Setting.NUM_ELE; k++) {
	            String temp = String.format("%d,", results.get(Setting.NUM_ELE*i+k));
	            		bw.append(temp);
	            	}
	            	bw.append("\n");
	            }
	        } catch (FileNotFoundException e) {
	            System.err.println("File not found: " + e.getMessage());
	        } catch (IOException e) {
	            System.err.println("IO: " + e.getMessage());
	        } finally {
	            if (bw != null) {
	                try {
	                    bw.close();
	                } catch (IOException e) {
	                    System.out.println("IO: " + e.getMessage());
	                }
	            }
	        }
	}
	
	/**
	 * reset the number of quiet agent
	 */
	public void resetQuiet() {
		this.quiet = this.agents - (this.active + this.jailed+ this.kill);
	}
	
	/**
	 * reset the number of jailed agent
	 */
	public void resetJailed() {
		this.jailed = 0;
	}
	
	/**
	 * reset the number of jailed agent
	 */
	public void resetActive() {
		this.active = 0 ;
	}

	/**
	 * add the number of agent in jail by one
	 */
	public void addJailed() {
		jailed++;
	}
	
	/**
	 * add the number of active agent by one
	 */
	public void addActive() {
		active++;
	}

	/**
	 * add the number of tick by one
	 */
	public void addTick() {
		tick+=1;
		
	}
	
	/*********************extension*********************/
	
	/**
	 * add the number of killed agent by one
	 */
	public void addKill() {
		kill+=1;
		
	}
	/***************************************************/
	
}
