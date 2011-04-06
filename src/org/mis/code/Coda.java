package org.mis.code;

import java.util.ArrayList;
import org.mis.processi.Job;

/**
 * La classe coda e' una classe astratta da cui vengono derivate le altre classi per le code dei
 * centri. E' utile a definire le altre classi come appartenenti alla stessa categoria ed 
 * implementare il polimorfismo su gli oggetti che le conterranno.
 */

public abstract class Coda {
	
	public ArrayList<Job> coda = new ArrayList<Job>();
	public String nomeCoda;
	
	/**
	 * Prende come parametro il solo nome dell coda del centro, il quale verra' utilizzato 
	 * nella fase di analisi del log del programma.
	 * @param nomeCoda
	 */
	
	public Coda(String nomeCoda) {
		this.nomeCoda = nomeCoda;
	}
	
	/**
	 * Funziona la quale ritorna il nome della coda di un centro.
	 * @return nomeCoda
	 */
	
	public String getNome(){
		return nomeCoda;
	}
	
	/**
	 * Funzione la quale ritorna la dimensione della coda di un centro.
	 * @return dimensione coda
	 */
	
	public final int getDimensione() {
		return coda.size();
	}
	
	/**
	 * Funzione la quale ritorna true se la coda è vuota.
	 * @return coda vuota
	 */
	
	public final boolean isEmpty() {
		return coda.isEmpty();
	}
	
	/**
	 * Prende come parametro un job, il quale verrà aggiunto nella coda.
	 * @param Job push
	 */
	
	public final void push(Job push){
		coda.add(push);
	}
	
	/**
	 * Funzione la quale rimuove tutti gli elementi dalla coda di un centro, sarà utile per
	 * resettare le code del sistema.
	 */
	
	public final void resetCoda() {
		coda.clear();
	}
	
	/**
	 * Funzione astratta la quale estrae un job dalla coda in base alla disciplia in gioco, 
	 * sarà necessario fae un override.
	 */
	
	public abstract Job pop();
	
	

}

