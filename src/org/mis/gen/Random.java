package generatori;
/**
 * Classe che serve a generare numeri pseudocasuali. Dato un seme "ix" in ingresso, 
 * in base ai parametri consigliati da Gordon a=5^31 (1220703125) e m=2^31 (2147483648), 
 * genera il successivo numero pseudocasuale della sequenza. 
 * @author Valerio Gentile
 * @author Andrea Giancarli
 * @author Alessandro Mastracci
 */
public class Random {
	private final long a=(long)Math.pow(5, 13);
	private final long m=(long)Math.pow(2, 31);
	private long ix;
	/**
	 * Il parametro da passare e' il seme
	 * @param ix
	 */
	
	public Random(int ix) {
		this.ix=ix;
	}
	/**
	 * Restituisce il prossimo numero pseudocasuale tra 0 e 1.
	 * Essendo il passaggio da una sequenza uniforme di numeri interi
	 * a una sequenza di numeri razionali la nuova distribuzione sarà ancora uniforme.
	 * Se dopo aver effettuato il prodotto "a * ix" si ottiene un numero negativo a 
	 * seguito di un trabocco che investe il bit di segno, questo viene sommato al modulo "m". 
	 * Il ritorno quindi sara' il numero cosi' ottenuto diviso "m".  
	 */ 
	
	public double nextNumber() {
		ix = (a*ix)%m;
		return (double) ix/(m-1);	
	}
	 
}