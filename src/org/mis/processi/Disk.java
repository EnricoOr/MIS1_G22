package org.mis.processi;

import org.mis.code.CodaLIFO;
import org.mis.gen.GeneratoreKerlangiano;
import org.mis.gen.Seme;

/**
 * La classe disk e' una classe derivata dalla classe astratta Processo. La
 * classe rappresenta un centro con coda di tipo LIFO e tempo di servizio con
 * distribuzione 2-erlagiana.
 * @author Daniele Battista
 * @author Luca Dell'Anna
 * @author Enrico Orsini
 */
public class Disk extends Processo {
	private CodaLIFO coda = new CodaLIFO("Coda " + super.getNome());
	private GeneratoreKerlangiano gen2erl;
	private final int k = 2;
	private final double tx = 0.033;
	private double TotTempoRisp;
	private double dT;
	private Job current;

	/**
	 * Costruttore della classe il quale istanzia un Disk. La legge di
	 * distribuzione del tempo di servizio è 2-erlangiana.
	 */
	public Disk() {
		super("Disk", TipoProcesso.Disk);
		gen2erl = new GeneratoreKerlangiano(Seme.getSeme(), this.tx, this.k);
	}

	/**
	 * Metodo che genera e ritorna un tempo con distribuzione 2-Erlagiana.
	 * 
	 * @return tempo con distribuzione 2-Erlagiana
	 */
	public double getTempoCentro() {
		dT = gen2erl.nextErlang();
		return dT;
	}

	/**
	 * Metodo che ritorna un tempo con distribuzione 2-Erlagiana.
	 * 
	 * @return tempo con distribuzione 2-Erlagiana
	 */
	public final double getdT() {
		return dT;
	}

	/**
	 * Metodo che estrae dalla coda un job in base alla disciplina LIFO
	 * 
	 * @return il Job estratto secondo disciplina FIFO
	 */
	public final Job pop() {
		this.current = coda.pop();
		return current;
	}

	/**
	 * Metodo che inserisce un job in coda.
	 * 
	 * @param il
	 *            Job da inserire in coda
	 */
	public final void push(Job job) {
		coda.push(job);
	}

	/**
	 * Metodo che ritorna la dimensione della coda.
	 * 
	 * @return dimensione della coda
	 */
	public final int getCodaSize() {
		return coda.getDimensione();
	}

	/**
	 * Metodo che ritorna se la coda è vuota.
	 * 
	 * @return true se la coda è vuota
	 */
	public final boolean getCodaVuota() {
		return coda.isEmpty();
	}

	/**
	 * Metodo che incrementa il tempo totale di risposta per il centro Disk.
	 * 
	 * @parm tempo intervallo di tempo da aggiungere al tempo di risposta
	 */
	public final void setTempoRisposta(double tempo) {
		TotTempoRisp += tempo;
	}

	/**
	 * Metodo che ritorna il tempo totale di risposta per il centro Disk.
	 * 
	 * @return tempo totale di risposta Disk
	 */
	public final double getTempoRisposta() {
		return TotTempoRisp;
	}

	/**
	 * Metodo che restituisce il Job in esecuzione nel centro
	 * 
	 * @return il job corrente
	 */
	public final Job getJobCorrente() {
		return this.current;
	}

	/**
	 * Metodo che implementa la primitiva ACTIVATE. E' stato effettuato
	 * l'override del metodo nella classe astratta Processo.
	 * 
	 * @param j
	 *            il nuovo job da elaborare
	 */
	public void activate(Job j) {
		super.activate();
		current = j;
	}

	/**
	 * Metodo per resettare lo stato del centro
	 */
	public final void reset() {
		coda.resetCoda();
		this.passivate();
	}
}