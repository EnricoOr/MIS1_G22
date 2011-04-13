package org.mis.gen;

/**
 * La classe GeneratoreIperEsponenziale estende la classe GeneratoreEsponenziale
 * e implementa un generatore Iperesponenziale
 */
public class GeneratoreIperEsponenziale extends GeneratoreEsponenziale {
	private double tx;
	private double P;
	private Random ran;
	private double molt1;
	private double molt2;

	/**
	 * Costruttore del generatore Iperesponenziale
	 * 
	 * @param tx
	 *            tempo medio di arrivo o di servizio del centro
	 * @param rand
	 *            generatore Random utilizzato per decidere da quale dei 2
	 *            generatori esponenziali estrarre il prossimo numero
	 * @param p
	 *            probabilità soglia per la scelta tra i 2 generatori
	 *            esponenziali
	 */
	public GeneratoreIperEsponenziale(double tx, Random rand, double p) {
		super(1, rand);
		this.tx = tx;
		this.P = p;
		ran = rand;
		molt1 = this.tx / (2 * (1 - P));
		molt2 = this.tx / (2 * P);
	}

	/**
	 * Metodo che ritorna il prossimo numero della sequenza pseudocasuale della
	 * distribuzione uniforme iperesponenziale con probabilità P compreso tra 0
	 * e 1.
	 * 
	 * @return nextIperExp
	 */
	public double nextIperExp() {
		if (ran.nextNumber() > P)
			return (super.nextExp() * molt1);
		else
			return (super.nextExp() * molt2);
	}
}