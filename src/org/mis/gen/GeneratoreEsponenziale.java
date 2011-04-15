package org.mis.gen;

/**
 * La classe GeneratoreEsponenziale implementa un generatore Esponenziale
 * @author Daniele Battista
 * @author Luca Dell'Anna
 * @author Enrico Orsini
 */
public class GeneratoreEsponenziale {
	private double tx;
	private Random ran;

	/**
	 * Il parametro è il tempo medio di arrivo oppure di servizio
	 * 
	 * @param tx
	 */
	public GeneratoreEsponenziale(double tx, Random rand) {
		this.tx = tx;
		ran = rand;
	}

	/**
	 * Funzione che ritorna il prossimo numero della sequenza pseudocasuale
	 * della distribuzione uniforme esponenziale compreso tra 0 e 1.
	 * 
	 * @return double nextExp
	 */
	public double nextExp() {
		double temp = (-tx * Math.log(ran.nextNumber()));
		return temp;
	}
}
