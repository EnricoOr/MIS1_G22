package org.mis.code;

import java.util.ArrayList;
import org.mis.processi.Job;

public abstract class Coda {
        
	protected ArrayList<Job> coda=new ArrayList<Job>();
	        
    public abstract Job successivo();
	
    public boolean isempty() {
	  	return coda.isEmpty();
	}
	    
	public void add(Job j) {
	  	coda.add(j);
	}
	    
	public int size() {
	   	return coda.size();
	}
	    
	public void free() {
		coda.clear();
	}
	
}

