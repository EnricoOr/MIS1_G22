package org.mis.processi;

import java.util.Vector;

import org.mis.code.Coda;
import org.mis.gen.Generatore;
import org.mis.gen.GeneratoreIperEsponenziale;
import org.mis.gen.Random;
import org.mis.gen.Seme;
import org.mis.gen.Job;

/**
 * La classe Printer e' una classe derivata dalla classe astratta centro. La classe rappresenta 
 * un centro senza coda poiche' di tipo IS, cioè ha infiniti posti in esecuzione.
 * @author 
 * @author 
 * @author 
 */

public class Printer extends Centro{

	private Erlang gen12erl;
	private final int tx = 40;
	private final int k = 12;
	private boolean occupato;

	/**
	 * E' il costruttore della classe il quale istanzia una stampante, il booleano occupato
	 * è ovviamente impostato a false essendo il centro di tipo IS.
	 * La legge di distribuzione nel tempo è la 12-erlangiana.
	 */
	
	public Printer(){
		super("Stampante");
		gen12erl = new Erlang(Seme.getSeme(), this.tx, this.k);
		occupato = false;
	}

	/**
	 * Funzione la quale ritorna un tempo con distribuzione 12-erlagiana. E' stato effettuato 
	 * l'override del metodo della superclasse centro. 
	 * @return 12-Erl
	 */
	
	@Override
	public double getTempoCentro() {
		return gen12erl.nextErlang();
	}

	/**
	 * Funzione la quale ritorna true se il centro e' occupato e false se il centro è libero. 
	 * E' un caso particolare perchè la stampante non sarà mai occupata perchè di tipo IS.
	 * E' stato effettuato l'override del metodo della superclasse centro.
	 * @return occupato(false)
	 */
	
	@Override
	public boolean getOccupato() {
		return occupato;
	}


	@Override
	public void setOccupato(boolean occ) {
		this.occupato = occ;
	}


	@Override
	public Job pop() {
		return null;
	}

}
