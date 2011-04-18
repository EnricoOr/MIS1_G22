package org.mis.sim;

import org.mis.processi.Processo;

/**
 * La classe FineSim e' una classe derivata dalla classe astratta Processo. La
 * classe rappresenta il processo di fine simulazione.
 * @author Daniele Battista
 * @author Luca Dell'Anna
 * @author Enrico Orsini
 */
public class FineSim extends Processo{

	/**
	 * Costruttore della classe.
	 */
	public FineSim() {
		super("Fine Simulazione", TipoProcesso.FineSimulazione);
	}
}
