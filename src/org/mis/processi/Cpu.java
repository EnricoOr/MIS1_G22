package org.mis.processi;

import org.mis.code.CodaFIFO;
import org.mis.gen.GeneratoreIperEsponenziale;
import org.mis.gen.Random;
import org.mis.gen.Seme;

/**
 * La classe Cpu e' una classe derivata dalla classe astratta Processo. La
 * classe rappresenta un centro con coda di tipo FIFO e tempo di servizio con
 * distribuzione iperesponenziale con probabilità 0,6.
 * @author Daniele Battista
 * @author Luca Dell'Anna
 * @author Enrico Orsini
 */
public class Cpu extends Processo {
	private double genIpExp_p06;
	private GeneratoreIperEsponenziale[] genIpExp_p06c = new GeneratoreIperEsponenziale[3];
	private final double[] txc = { 0.058, 0.07, 0.028 };
	private Random rand = new Random(Seme.getSeme());
	private CodaFIFO coda;
	private Job current;

	/**
	 * Costruttore della classe. I istanzia una cpu. La legge di distribuzione
	 * nel tempo è iperesponenziale con probabilità 0,6.
	 */
	public Cpu() {
		super("CPU", TipoProcesso.CPU);
		for (int i = 0; i < txc.length; i++) {
			genIpExp_p06c[i] = new GeneratoreIperEsponenziale(txc[i], rand, 0.6);
		}
		coda = new CodaFIFO("Coda " + super.getNome());
	}

	/**
	 * Metodo che ritorna il tempo di servizio per il Job corrente. E' stato
	 * effettuato l'override del metodo della superclasse Processo.
	 * 
	 * @return tempo di servizio per il Job corrente, con distribuzione
	 *         iperesponeziale con e probabilità 0,6.
	 */
	public double getTempoCentro(Job jobCorrente) {
		this.current = jobCorrente;

		genIpExp_p06 = genIpExp_p06c[jobCorrente.getJobClass() - 1]
				.nextIperExp();
		return genIpExp_p06;
	}

	/**
	 * Metodo che indica se la coda per il centro Cpu è vuota.
	 * 
	 * @return true se la coda è vuota
	 */
	public final boolean codaVuota() {
		return coda.isEmpty();
	}

	/**
	 * Metodo che estrae dalla testa della coda un job.
	 * 
	 * @return il Job in testa alla coda
	 */
	public final Job pop() {
		return coda.pop();
	}

	/**
	 * Metodo che inserisce un job in coda.
	 * 
	 * @param job
	 *            il Job da inserire in coda
	 */
	public final void push(Job job) {
		coda.push(job);
	}

	/**
	 * Metodo che restituisce il prossimo numero random della sequenza
	 * 
	 * @return un numero random tra 0 e 1
	 */
	public double nextRand() {
		return rand.nextNumber();
	}

	/**
	 * Metodo che restituisce il Job in esecuzione nel centro
	 * 
	 * @return il job corrente
	 */
	public final Job getJobCorrente() {
		return current;
	}

	/**
	 * Metodo per resettare lo stato del centro
	 */
	public final void reset() {
		coda.resetCoda();
		this.passivate();
	}
}