/**
 * 
 */
package org.mis.sim;

/**
 * @author 
 *
 */
public class SimTime {
	
	private double timeStart, timeStop;
	private double timeSim;
	
	public SimTime(){
		this.timeStart = System.currentTimeMillis();
		setSimTime (0.0);
	}
	
	public void add(double dT){
		
		this.timeSim += dT;
	}
	
	public double getSimTime(){
		
		return this.timeSim;
	}
	
	public void setSimTime (double t){
		
		this.timeSim = t;
	}
	
	public void stopSimTime(){
		
		this.timeStop = System.currentTimeMillis();
	}
	
	public double getSimDuration (){
		
		stopSimTime();
		return this.timeStop - this.timeStart;
		
	}

}
