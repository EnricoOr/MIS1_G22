package org.mis.processi;

import java.util.Vector;

import org.mis.code.CodaLIFO;
import org.mis.gen.GeneratoreKerlangiano;
import org.mis.gen.Seme;


/**
 * La classe disk e' una classe derivata dalla classe astratta centro. La classe rappresenta 
 * un centro con coda di tipo last-in first-out e tempo con distribuzione 3-erlagiana. 
 * @author 
 * @author 
 * @author 
 */

public class Disk extends Processo{

	private CodaLIFO coda = new CodaLIFO("Coda " + super.getNome());
	public Vector<Job> codas;
	private GeneratoreKerlangiano gen2erl;
	private final int k = 2;
	private final double tx = 0.033;
	private double TotTempoRisp;
	private double dT;
	private Job current;
	
	/**
	 * E' il costruttore della classe il quale istanzia un disk.
	 * La legge di distribuzione nel tempo è la 3-erlangiana.
	 */
	
	public Disk(){
		super("Disk", TipoProcesso.Disk);
		gen2erl = new GeneratoreKerlangiano(Seme.getSeme(), this.tx, this.k);
	}

	/**
	 * Funzione la quale ritorna un tempo con distribuzione 2-erlagiana. E' stato effettuato 
	 * l'override del metodo della superclasse centro. 
	 * @return 3-Erl
	 */
	

	public double getTempoCentro() {
		dT=gen2erl.nextErlang();
		return dT;
	}
	
	public final double getdT() {
		
		return dT;
	}

	/**
	 * Funzione la quale estrae dalla coda un job in base alla disciplina in gioco 
	 * last-in first-out. E' stato effettuato l'override del metodo della superclasse centro.
	 * @return job
	 */


	public final Job pop() {
		this.current=coda.pop();
		return current;
	}

	/**
	 * Funzione la quale inserisce un job in coda. E' stato effettuato l'override 
	 * del metodo della superclasse centro.
	 * @param job
	 */
	

	public final void push(Job job) {
		coda.push(job);
	}

	/**
	 * Funzione la quale ritorna la dimensione della coda di tipo lifo del centro disk.
	 * @return dimensione coda
	 */
	
	public final int getCodaSize()
	{
		return coda.getDimensione();
	}
	
	/**
	 * Metodo che incrementa tempo totale di risposta per il centro disk.
	 * @parm tempo
	 */
	
	public final void setTempoRisposta(double tempo)
	{
		TotTempoRisp+=tempo;
	}

	/**
	 * Funzione la quale ritorna il tempo totale di risposta per il centro disk.
	 * @return tempo totale risposta disk
	 */
	
	public final double getTempoRisposta()
	{
		return TotTempoRisp;
	}
	
	public final Job getJobCorrente(){
		
		return this.current;
	}

	/**
	 * @param j
	 */
	public void activate(Job j) {
		super.activate();
		current=j;
		
	}
	
	/**
	 * Questa funzione resetta lo stato del centro
	 */
	
	public final void reset()
	{
		coda.resetCoda();
		this.passivate();
	}


}
