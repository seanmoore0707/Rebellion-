/**
 * Team members:
 * Yifan Fan 962284
 * Chen(Christina) Xu 945756
 * Haonan Chen 930614
 * 
 * This class represents the turtles in the NetLogo. 
 * Agent and Cop are the child class of this class. 
 * It includes the common attributes and methods of Agent and Cop.
 * 
 */

import java.util.ArrayList;

public abstract class Turtle{
	//the world where this turtle exists.
	private World world;
	//the position of this turtle.
	private int xPosition;
	private int yPosition;
	//represents the current tick.
	private int tick;

	/**
	 * initiate an turtle
	 * @param world the world associated with turtle
	 * @param xPosition x-coordinate position of turtle
	 * @param yPosition y-coordinate position of turtle
	 */
	public Turtle(World world, int xPosition, int yPosition) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.world = world;
        this.tick = 1;
    }
	
	/**
	 * get the type of a turtle(Cop or Agent)
	 * @return the type of turtle
	 */
	public abstract String getType() ;
	/**
	 * move to unoccupied patches 
	 */
	public abstract void move() ;
	
	/**
	 * decide whether an observed patch is worthy of considering
	 * @param formove decide whether the turtle can move
	 * @param observeX x-coordinate of the current patch
	 * @param observeY y-coordinate of the current patch
	 * @param result a list of locations that are valid for turtle to move
	 */
	public abstract void addLocation(boolean formove, 
			int observeX, int observeY, ArrayList<int[]> result);
	
	/**
	 * get the patches inside the vision of this turtle.
	 * @param formove decide whether the turtle can move
	 * @return the list of patches valid for the turtle to move
	 */
	public ArrayList<int[]> observe(boolean formove) {
		int observeX;
		int observeY;

		int visionInteger=(int)Setting.VISION;
		ArrayList<int[]> result= new ArrayList<int[]>();
		
		for(int i=(-1)*visionInteger;i<=visionInteger;i++) {
			//decide the x of the patch to observe.
			//the x must always be inside the world. 
			if(this.getX()+i<0) 
				observeX=this.getX()+i+Setting.WORLD_LENGTH;
			else if(this.getX()+i>=Setting.WORLD_LENGTH) 
				observeX=this.getX()+i-Setting.WORLD_LENGTH;
			else 
				observeX=this.getX()+i;
			
			for(int j=(-1)*visionInteger;j<=visionInteger;j++) {
				//only consider the neighbors inside the vision
				if((float)(i*i+j*j)<=Setting.VISION*Setting.VISION) {	
					
					//decide the y of the patch to observe
					//the y must always be inside the world 
					if(this.getY()+j<0) 
						observeY=this.getY()+j+Setting.WORLD_WIDTH;
					else if(this.getY()+j>=Setting.WORLD_WIDTH) 
						observeY=this.getY()+j-Setting.WORLD_WIDTH;
					else 
						observeY=this.getY()+j;
					
					this.addLocation(formove, observeX, observeY, result);
		
				}
			}
		}
		return result;
	}
	
	//The methods below are setters and getters.
	/**
	 * get the x-coordinate of turtle
	 * @return x-coordinate of turtle
	 * */
	public int getX() {
		return xPosition;
	}
	
	/**
	 * get the y-coordinate of turtle
	 * @return y-coordinate of turtle
	 * */
	public int getY() {
		return yPosition;
	}
	
	/**
	 * set the x-coordinate of turtle
	 * @param xPosition value of x-coordinate
	 * */
	public void setX(int xPosition) {
		this.xPosition= xPosition;
	}
	
	/**
	 * set the y-coordinate of turtle
	 * @param yPosition value of y-coordinate
	 * */
	public void setY(int yPosition) {
		this.yPosition= yPosition;
	}
	/**
	 * get the world associated with turtle
	 * @return the world associated with turtle
	 * */
	public World getWorld() {
		return world;
	}
	
	/**
	 * set the world associated with turtle
	 * @param world the world associated with turtle
	 * */
	public void setWorld(World world) {
		this.world = world;
	}
	
	
}
