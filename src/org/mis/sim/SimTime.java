/**
 * 
 */
package org.mis.sim;

/**
 * @author Daniele Battista
 * @author Luca Dell'Anna
 * @author Enrico Orsini
 */
public class SimTime {
	
	private double timeStart, timeStop;
	private double timeSim;
	
	public SimTime(){
		this.timeStart = System.currentTimeMillis();
		setSimTime (0.0);
	}
	
	public final void add(double dT){
		
		this.timeSim += dT;
	}
	
	public final double getSimTime(){
		
		return this.timeSim;
	}
	
	public final void setSimTime (double t){
		
		this.timeSim = t;
	}
	
	public final void stopSimTime(){
		
		this.timeStop = System.currentTimeMillis();
	}
	
	public final double getSimDuration (){
		
		stopSimTime();
		return this.timeStop - this.timeStart;
		
	}

}
