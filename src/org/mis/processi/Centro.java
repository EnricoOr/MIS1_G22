package org.mis.processi;

import org.mis.code.Coda;
import org.mis.gen.Generatore;

public abstract class Centro {
	private String nome;
		
	/**
	 * Prende come parametro il solo nome del centro, il quale verra' utilizzato nella fase di
	 * analisi del log del programma.
	 * @param nome
	 */
	
	public Centro(String nome) {
		this.nome=nome;
	}
	
	/**
	 * Funzione astratta la quale ritorna il tempo del centro in base alle leggi di probabilità 
	 * in gioco, sara' necessario fare un override.
	 */
	
	public abstract double getTempoCentro();
	
	/**
	 * Funzione che ritorna il nome del centro
	 * @return nome centro
	 */
	
	public String getNome() {
		return nome;
	}
	
	/**
	 * Funzione astratta la quale ritorna true se il centro e' occupato e false se il centro 
	 * è libero, sara' necessario fare un override.
	 */
	
	public abstract boolean getOccupato();
	
	/**
	 * Funzione astratta la quale setta a true il booleano occupato, sara' necessario fare 
	 * un override.
	 * @param occ
	 */
	
	public abstract void setOccupato(boolean occ);
	
	/**
	 * Funzione astratta la quale inserisce un job in coda, sara' necessario fare un override.
	 * @param job
	 */
	
	public abstract void push(Job job);
	
	/**
	 * Funzione astratta la quale estrae dalla coda un job in base alla disciplina in gioco, 
	 * sara' necessario fare un override.
	 */
	
	public abstract Job pop();
	
	/**
	 * Funzione astratta che si occupa di salvare lo stato stabile al termine del run pilota
	 */
	
	public abstract void salvaStato();

	/**
	 * Funzione astratta che si occupa di ripristinare lo stato stabile al termine di ogni run
	 */
	
	public abstract void ripristinaStato();

}
