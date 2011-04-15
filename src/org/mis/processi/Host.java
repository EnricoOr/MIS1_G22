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
	 * Funzione la quale ritorna un tempo con distribuzione esponenziale. E' stato effettuato 
	 * l'override del metodo della superclasse centro. 
	 * @return kerl
	 */
	public double getTempoCentro() {
		return kerl.nextErlang();
	}
	
	public void setCurJob(Job c){
		current=c;
	}


	public final Job getJobCorrente(){
		
		return current;
	}

}
