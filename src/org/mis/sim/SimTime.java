package org.mis.sim;

/**
 * La classe SimTime implementa il clock della simulazione.
 * 
 * @author Daniele Battista
 * @author Luca Dell'Anna
 * @author Enrico Orsini
 */
public class SimTime {

	private double timeStart, timeStop;
	private double timeSim;

	/**
	 * Costruttore della classe. Inizializza il clock della simulazione.
	 */
	public SimTime() {
		this.timeStart = System.currentTimeMillis();
		setSimTime(0.0);
	}

	/**
	 * Metodo che restituisce il tempo corrente della simulazione.
	 * 
	 * @return
	 */
	public final double getSimTime() {

		return this.timeSim;
	}

	/**
	 * Metodo che imposta il tempo corrente della simulazione.
	 * 
	 * @param t
	 *            il tempo da impostare come attuale per la simulazione
	 */
	public final void setSimTime(double t) {

		this.timeSim = t;
	}

	/**
	 * Metodo che imposta il tempo di fine della simulazione. Non riguarda il
	 * clock della simulazione ma il tempo reale. Usato per misurare la durata
	 * della simulazione.
	 */
	public final void stopSimTime() {

		this.timeStop = System.currentTimeMillis();
	}

	/**
	 * Metodo che restituisce la durata della simulazione. Non riguarda il clock
	 * della simulazione ma il tempo reale.
	 * 
	 * @return la durata reale della simulazione
	 */
	public final double getSimDuration() {

		stopSimTime();
		return this.timeStop - this.timeStart;
	}
}
