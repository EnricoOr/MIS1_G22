package org.mis.code;

import org.mis.processi.Job;


public class CodaFIFO extends Coda {
	
	/**
	 * Prende come parametro il nome della coda e si effettua una chiamata al costrutture della 
	 * classe madre coda. 
	 */
	
	public CodaFIFO(String nomeCoda) {
		super(nomeCoda);
	}
	
	@Override
	public Job pop() {
		Job pop = coda.firstElement();
		coda.remove(0);
		return pop;
	}

}

