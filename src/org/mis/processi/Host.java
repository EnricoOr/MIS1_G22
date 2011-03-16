package org.mis.processi;


import org.mis.gen.GeneratoreKerlangiano;
import org.mis.gen.Seme;
import org.mis.processi.Job;


/**
 * La classe stampante e' una classe derivata dalla classe astratta centro. La classe rappresenta 
 * un centro senza coda poiche' di tipo IS, cioè ha infiniti posti in esecuzione.
 * @author 
 * @author 
 * @author 
 */

public class Host extends Processo{

	private GeneratoreKerlangiano kerl;
	private final double tx = 0.085;
	private Job current;



	/**
	 * E' il costruttore della classe il quale istanzia una stampante, il booleano occupato
	 * è ovviamente impostato a false essendo il centro di tipo IS.
	 * La legge di distribuzione nel tempo è esponenziale.
	 */
	
	public Host(){
		super("Host");
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


	public Job getJobCorrente(){
		
		return current;
	}

}
