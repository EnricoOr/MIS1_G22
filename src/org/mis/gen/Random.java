package org.mis.gen;

/**
 * Classe che serve a generare numeri pseudocasuali. Dato un seme "ix" in
 * ingresso, in base ai parametri consigliati da Gordon a=5^31 (1220703125) e
 * m=2^31 (2147483648), genera il successivo numero pseudocasuale della
 * sequenza.
 * @author Daniele Battista
 * @author Luca Dell'Anna
 * @author Enrico Orsini
 */
public class Random {
	private final long a = (long) Math.pow(5, 13);
	private final long m = (long) Math.pow(2, 31);
	private long ix;

	/**
	 * Costruttore del generatore Random
	 * 
	 * @param ix
	 *            seme del generatore
	 */
	public Random(int ix) {
		this.ix = ix;
	}

	/**
	 * Metodo che restituisce il prossimo numero pseudocasuale tra 0 e 1.
	 * Essendo il passaggio da una sequenza uniforme di numeri interi a una
	 * sequenza di numeri razionali la nuova distribuzione sarà ancora uniforme.
	 * Se dopo aver effettuato il prodotto "a * ix" si ottiene un numero
	 * negativo a seguito di un trabocco che investe il bit di segno, questo
	 * viene sommato al modulo "m". Il ritorno quindi sarà il numero così
	 * ottenuto diviso "m".
	 */
	public double nextNumber() {
		ix = (a * ix) % m;
		return (double) ix / (m - 1);
	}

	/**
	 * Metodo che restituisce il prossimo numero pseudocasuale intero tra 2 e
	 * 78.
	 */
	public final int nextNumber2_78() {
		return (int) ((76 * (this.nextNumber())) + 2);
	}
}