package org.mis.processi;

import org.mis.gen.Random;
import org.mis.gen.Seme;


/**
 * La classe Printer e' una classe derivata dalla classe astratta centro. La classe rappresenta 
 * un centro senza coda poichè di tipo IS, cioè ha infiniti posti in esecuzione.
 * @author Daniele Battista
 * @author Luca Dell'Anna
 * @author Enrico Orsini
 */
public class Printer extends Processo{

	private Random genUn2_78;
	private Job current;

	/**
	 * E' il costruttore della classe il quale istanzia una stampante, il booleano occupato
	 * è ovviamente impostato a false essendo il centro di tipo IS.
	 * La legge di distribuzione nel tempo è uniforme 2-78.
	 */	
	public Printer(){
		super("Stampante", TipoProcesso.Stampante);
		genUn2_78 = new Random(Seme.getSeme());
	}

	/**
	 * Metodo che ritorna un tempo con distribuzione uniforme 2-78. 
	 */
	public double getTempoCentro() {
		return genUn2_78.nextNumber2_78();
	}
	
	/**
	 * Metodo per impostare il job corrente nel centro
	 * @param c il job da impostare
	 */
	public final void setCurJob(Job c){
		current=c;
	}

	/**
	 * Metodo che ritorna il job correntemente in elaborazione nel centro
	 */
	public final Job getJobCorrente(){
		
		return current;
	}
}
