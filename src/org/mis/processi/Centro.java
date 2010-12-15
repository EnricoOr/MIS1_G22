package org.mis.processi;

import org.mis.code.Coda;
import org.mis.gen.Generatore;

public abstract class Centro {
	
	private Generatore g;
	private Coda coda;
	public boolean occupato=false;

	public Centro () {
		
	}
	
	public Centro (Generatore g, Coda coda) {
		this.g=g;
        this.coda=coda;
	}

	public double generaTempo(double T){
		return T + g.nextNumber();
	}
    public Coda getCoda(){
        return this.coda;
    }
}
