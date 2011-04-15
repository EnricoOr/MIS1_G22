/**
 * 
 */
package org.mis.sim;

import org.mis.processi.Processo;

/**
 * @author Daniele Battista
 * @author Luca Dell'Anna
 * @author Enrico Orsini
 */
public class FineSim extends Processo{

	/**
	 * @param nome
	 */
	public FineSim() {
		super("Fine Simulazione", TipoProcesso.FineSimulazione);
		
	}

}
