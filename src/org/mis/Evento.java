package org.mis;

import org.mis.processi.Job;

public class Evento {
  
    public Evento(){
        
    }
    public Evento(Job job, Sequenziatore sequenziatore, double tempo){
    	this.job=job;
        this.sequenziatore=sequenziatore;
        this.tempo=tempo;
    }
    public double gettempo(){
    	return tempo;
    }
    public void evolvi(){
        
    }
    public String toString(){
    	return this.getClass().getName() + "(" + tempo + ")";
    }
    
    protected Job job;
    protected double tempo;
    protected Sequenziatore sequenziatore;

}
