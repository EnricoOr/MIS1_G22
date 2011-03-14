package org.mis.gen;

public class GeneratoreKerlangiano extends Random {
	private double tx;
	private int k;
	/**
	 * I tre parametri sono rispettivamente: il seme, la media del centro K-Erlangiano
	 * e il numero degli stadi del centro K-Erlangiano
	 * @param ix
	 * @param tx
	 * @param k
	 */
	public GeneratoreKerlangiano(int ix, double tx, int k ) {
		super(ix);
		this.tx = tx;
		this.k = k;
		
	}
	
	/**
	 * Funzione che ritorna il prossimo numero della sequenza 
	 * pseudocasuale della distribuzione uniforme k-Erlangiana 
	 * compreso tra 0 e 1.
	 * @return double nextErlang
	 */
	public double nextErlang() {
		double p;
		do  {
			p=1;		
			for (int i=1; i<=k; i++) {
				p *= super.nextNumber();
			}
		}
		while (p==0);	
		return ((-tx/k)*Math.log(p));
	}

}