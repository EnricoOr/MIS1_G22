/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mis.processi;

import java.util.Vector;

import org.mis.code.Coda;
import org.mis.gen.Generatore;
import org.mis.gen.GeneratoreIperEsponenziale;
import org.mis.gen.Random;
import org.mis.gen.Seme;
import org.mis.gen.Job;

/**
 * La classe terminale è una classe derivata dalla classe astratta centro. La classe rappresenta 
 * un centro senza coda poichè i client prodocuno job dai terminali e aspettano che il job 
 * ritorni al centro prima di lanciarne uno nuovo.
 * @author 
 * @author 
 * @author 
 */

public class Terminale extends Centro{

	private static int id = 0;
	private static int identificatore=0; 
	private Erlang gen2erl;
	private final double tx = 10;
	private final int k =2;
	private boolean occupato;
	private Job job = null; 

	/**
	 * E' il costruttore della classe il quale istanzia un terminale con un id univoco incrementale.
	 * La legge di distribuzione nel tempo è la 2-erlangiana.
	 */
	
	public Terminali(){
		super("Terminale " + (identificatore=(id++) % Simulatore.getNClient()));
		gen2erl = new Erlang(Seme.getSeme(), this.tx, this.k);
		occupato = false;
	}

	/**
	 * La funzione ritorna l'id univoco del terminale.
	 * @return id
	 */
	
	public int getId(){
		return identificatore;
	}

	/**
	 * Funzione la quale ritorna un tempo con distribuzione 2-erlagiana. E' stato effettuato 
	 * l'override del metodo della superclasse centro. 
	 * @return 2-Erl
	 */
	
	@Override
	public double getTempoCentro() {
		
		return gen2erl.nextErlang();
	}

	/**
	 * Crea un nuovo job con un id univoco incrementale generato da un terminale
	 * @return nuovo job
	 * @see Job
	 */
	
	public Job nextJob() {
		job = new Job(this);
		return job;
	}

	/**
	 * Funzione la quale restituisce il job
	 * @return job
	 */
	
	public Job getJob()
	{
		return job;
	}

	/**
	 * Funzione la quale ritorna true se il centro e' occupato e false se il centro è libero. 
	 * E' stato effettuato l'override del metodo della superclasse centro.
	 * @return occupato
	 */
	
	@Override
	public boolean getOccupato() {
		return occupato;
	}

	/**
	 * Funzione la quale setta a true il booleano occupato. E' stato effettuato l'override del 
	 * metodo della superclasse centro.
	 * @param occ
	 */
	
	@Override
	public void setOccupato(boolean occ) {
		this.occupato = occ;
	}

	@Override
	public Job pop() {
		return null;
	}

	@Override
	public void push(Job job) {
		
	}

}
