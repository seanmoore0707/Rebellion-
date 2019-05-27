/**
 * 
 * This class represents the patch which holds a list of turtles
 */
import java.util.ArrayList;

public class Patch {

	private ArrayList<Turtle> turtles;
	
	public Patch() {
		this.turtles = new ArrayList<Turtle>(); 
	}
	
	/**
	 * put a turtle into the array list 
	 * @param turtle the turtle on the patch
	 */
	public void hold(Turtle turtle) {
		turtles.add(turtle);
	}
	
	/**
	 * remove a turtle from the array list
	 * @param turtle the turtle on the patch
	 */
	public void free(Turtle turtle) {
		turtles.remove(turtle);
	}
	
	/**
	 * get currently-holding turtles
	 * @return a list of turtles on the patch
	 */
	public ArrayList<Turtle> getHolding(){
		return this.turtles;
	}
}
