package org.mis.gen;

/**
 * La classe GeneratoreKerlangiano estende la classe Random e implementa un
 * generatore k-Erlangiano
 */
public class GeneratoreKerlangiano extends Random {
	private double tx;
	private int k;

	/**
	 * Costruttore del generatore K-Erlangiano
	 * 
	 * @param ix
	 *            seme del generatore Random
	 * @param tx
	 *            media del centro K-Erlangiano
	 * @param knumero
	 *            degli stadi del centro K-Erlangiano
	 */
	public GeneratoreKerlangiano(int ix, double tx, int k) {
		super(ix);
		this.tx = tx;
		this.k = k;
	}

	/**
	 * Metodo che ritorna il prossimo numero della sequenza pseudocasuale della
	 * distribuzione uniforme k-Erlangiana compreso tra 0 e 1.
	 * 
	 * @return nextErlang
	 */
	public double nextErlang() {
		double p;
		do {
			p = 1;
			for (int i = 1; i <= k; i++) {
				p *= super.nextNumber();
			}
		} while (p == 0);
		return ((-tx / k) * Math.log(p));
	}
}