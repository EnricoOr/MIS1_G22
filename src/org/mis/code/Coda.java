package org.mis.code;

import java.util.Vector;
import org.mis.processi.Job;

/**
 * La classe coda e' una classe astratta da cui vengono derivate le altre classi per le code dei
 * centri. E' utile a definire le altre classi come appartenenti alla stessa categoria ed 
 * implementare il polimorfismo su gli oggetti che le conterranno.
 */

public abstract class Coda {
	
	public Vector<Job> coda = new Vector<Job>();
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
	
	public int getDimensione() {
		return coda.size();
	}
	
	/**
	 * Prende come parametro un job, il quale verrà aggiunto nella coda.
	 * @param Job push
	 */
	
	public void push(Job push){
		coda.add(push);
	}
	
	/**
	 * Funzione astratta la quale estrae un job dalla coda in base alla disciplia in gioco, 
	 * sarà necessario fae un override.
	 */
	
	public abstract Job pop();
	
	

}

