package org.mis.processi;

import java.util.Vector;

import org.mis.code.Coda;
import org.mis.gen.Generatore;
import org.mis.gen.GeneratoreIperEsponenziale;
import org.mis.gen.Random;
import org.mis.gen.Seme;
import org.mis.gen.Job;

/**
 * La classe disk e' una classe derivata dalla classe astratta centro. La classe rappresenta 
 * un centro con coda di tipo last-in first-out e tempo con distribuzione 3-erlagiana. 
 * @author 
 * @author 
 * @author 
 */

public class Disk extends Centro{

	private CodaLifo coda = new CodaLifo("Coda" + super.getNome());
	public Vector<Job> codas;
	private Erlang gen3erl;
	private final int k = 3;
	private final double tx = 0.0330;
	private boolean occupato;
	private boolean occupatos;
	private int jobEseguiti;
	private double TotTempoRisp;
	private double TotTempoCoda;
	
	/**
	 * E' il costruttore della classe il quale istanzia un disk.
	 * La legge di distribuzione nel tempo è la 3-erlangiana.
	 */
	
	public Disk(){
		super("Disk");
		gen3erl = new Erlang(Seme.getSeme(), this.tx, this.k);
		occupato = false;
	}

	/**
	 * Funzione la quale ritorna un tempo con distribuzione 3-erlagiana. E' stato effettuato 
	 * l'override del metodo della superclasse centro. 
	 * @return 3-Erl
	 */
	
	@Override
	public double getTempoCentro() {
		return gen3erl.nextErlang();
	}

	/**
	 * Funzione la quale ritorna true se il centro e' occupato e false se il centro è libero. 
	 * E' stato effettuato l'override del metodo della superclasse centro.
	 * @return occupato
	 */
	
	@Override
	public boolean getOccupato() {
		return occupato;
	}

	/**
	 * Funzione la quale setta a true il booleano occupato. E' stato effettuato l'override del 
	 * metodo della superclasse centro.
	 * @param occ
	 */
	
	@Override
	public void setOccupato(boolean occ) {
		this.occupato = occ;
	}

	/**
	 * Funzione la quale estrae dalla coda un job in base alla disciplina in gioco 
	 * last-in first-out. E' stato effettuato l'override del metodo della superclasse centro.
	 * @return job
	 */

	@Override
	public Job pop() {
		return coda.pop();
	}

	/**
	 * Funzione la quale inserisce un job in coda. E' stato effettuato l'override 
	 * del metodo della superclasse centro.
	 * @param job
	 */
	
	@Override
	public void push(Job job) {
		coda.push(job);
	}

	/**
	 * Funzione la quale ritorna la dimensione della coda di tipo lifo del centro disk.
	 * @return dimensione coda
	 */
	
	public int getCodaSize()
	{
		return coda.getDimensione();
	}

	/**
	 * Funzione che incrementa il numero dei job eseguiti dal cento disk e somma al temp totale di
	 * risposta del centro disk il tempo del job appena terminato.
	 * @param tempo
	 */
	
	public void jobCompletato(double tempo) {
		jobEseguiti++;
		TotTempoRisp += tempo;	
	}

	/**
	 * Funzione la quale ritorna il  numero di job eseguiti dal centro disk.
	 * @return jobEseguiti (numero dei job eseguiti dal centro disk)
	 */
	
	public int getJobCompletati()
	{
		return jobEseguiti;		
	}

	/**
	 * Funzione la quale somma al tempo totale di attesa in coda il tempo di attesa del job appena 
	 * entrato in disk. 
	 * @param tempo
	 */
	
	public void setTempoCoda(double tempo)
	{
		TotTempoCoda += tempo;
	}

	/**
	 * Funzione la quale ritorna il tempo totale di attesa dei job in coda al centro disk.
	 * @return tempo totale coda centro disk
	 */
	
	public double getTempoCoda()
	{
		return TotTempoCoda;
	}

	/**
	 * Funzione la quale ritorna il tempo totale di risposta per il centro disk.
	 * @return tempo totale risposta centro disk
	 */
	
	public double getTempoRisposta()
	{
		return TotTempoRisp;
	}


}
