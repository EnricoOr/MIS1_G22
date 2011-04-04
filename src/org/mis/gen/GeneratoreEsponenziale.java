package org.mis.gen;


public class GeneratoreEsponenziale{
	private double tx;
	private Random ran;
	/**
	 * Il parametro è il tempo medio di arrivo oppure di servizio
	 * @param tx
	 */
	
	
	public GeneratoreEsponenziale(double tx, Random rand) {
		this.tx =tx;
		ran = rand;
	}
	
	/**
	 * Funzione che ritorna il prossimo numero della sequenza 
	 * pseudocasuale della distribuzione uniforme esponenziale 
	 * compreso tra 0 e 1.
	 * @return double nextExp
	 */
	
	public double nextExp() {
		double temp=(-tx*Math.log(1-ran.nextNumber()));
		return temp;
	}
  
}
