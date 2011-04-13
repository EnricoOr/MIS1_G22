package org.mis.processi;

/**
 * La classe Processo è la classe astratta che fa da base per le classi dei
 * centri.
 */
public abstract class Processo implements Comparable<Processo> {
	private String nome;
	private TipoProcesso tipo;
	private Stato state;
	protected double hTime;

	/**
	 * Enum usato per identificare il tipo del centro rappresentato
	 * dall'oggetto.
	 */
	public enum TipoProcesso {
		Terminale, CPU, Disk, Host, Stampante, Osservazione, FineSimulazione, Job
	}

	/**
	 * Costruttore del Processo
	 * 
	 * @param nome
	 *            nome del centro, utilizzato nella fase di analisi del log del
	 *            programma
	 * @param tipo
	 *            tipo del centro, utilizzato da Simulatore per conoscere il
	 *            tipo del centro estratto dalla coda di Hold
	 */
	public Processo(String nome, TipoProcesso tipo) {
		this.nome = nome;
		this.tipo = tipo;
		this.state = Stato.PASSIVO;
	}

	/**
	 * Metodo che ritorna il nome del centro
	 * 
	 * @return nome del centro
	 */
	public final String getNome() {
		return nome;
	}

	/**
	 * Metodo che ritorna il tipo del centro
	 * 
	 * @return tipo del centro
	 */
	public final TipoProcesso getTipo() {
		return tipo;
	}

	/**
	 * Metodo che ritorna lo stato del centro
	 * 
	 * @return
	 */
	public final Stato getStato() {
		return state;
	}

	/**
	 * Metodo per impostare lo stato del centro
	 * 
	 * @param s
	 *            stato del centro da impostare
	 */
	public final void setState(Stato s) {
		this.state = s;
	}

	/**
	 * Metodo che implementa la primitiva HOLD
	 * 
	 * @param temp
	 *            istante di clock in cui il Centro termina l'hold
	 */
	public final void hold(double temp) {
		hTime = Double.valueOf(temp);
		this.state = Stato.HOLD;
	}

	/**
	 * Metodo che implementa la primitiva ACTIVATE
	 */
	public void activate() {
		this.state = Stato.ATTIVO;
	}

	/**
	 * Metodo che implementa la primitiva PASSIVATE
	 */
	public final void passivate() {
		this.state = Stato.PASSIVO;
	}

	/**
	 * Metodo che restituisce l'istante di clock in cui il Centro termina l'hold
	 * 
	 * @return istante di clock in cui il Centro termina l'hold
	 */
	public final double getTime() {
		return hTime;
	}

	/**
	 * Metodo che consente di avere i centri ordinati nella coda di hold
	 */
	public final int compareTo(Processo other) {
		return Double.compare(hTime, other.hTime);
	}
}