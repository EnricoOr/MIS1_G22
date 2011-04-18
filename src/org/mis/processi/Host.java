package org.mis.processi;

import org.mis.gen.GeneratoreKerlangiano;
import org.mis.gen.Seme;
import org.mis.processi.Job;


/**
 * La classe Host è una classe derivata dalla classe astratta Processo. La classe rappresenta 
 * un centro senza coda poiché di tipo IS, cioè ha capacità infinita.
 * @author Daniele Battista
 * @author Luca Dell'Anna
 * @author Enrico Orsini
 */
public class Host extends Processo{
	private GeneratoreKerlangiano kerl;
	private final double tx = 0.085;
	private Job current;

	/**
	 * Costruttore della classe
	 */
	public Host(){
		super("Host", TipoProcesso.Host);
		kerl = new GeneratoreKerlangiano(Seme.getSeme(),this.tx, 3);
	}

	/**
	 * Metodo che ritorna un tempo con distribuzione 3-Erlangiana.
	 * @return tempo con distribuzione 3-Erlangiana
	 */
	public double getTempoCentro() {
		return kerl.nextErlang();
	}
	
	/**
	 * Metodo per impostare il job corrente del centro
	 * @param c il job da impostare
	 */
	public void setCurJob(Job c){
		current=c;
	}


	/**
	 * Metodo che restituisce il job corrente del centro
	 * @return il job corrente del centro
	 */
	public final Job getJobCorrente(){
		
		return current;
	}
}
