package org.mis.processi;

import org.mis.gen.GeneratoreIperEsponenziale;
import org.mis.gen.Random;
import org.mis.gen.Seme;
import org.mis.sim.Simulatore;


/**
 * La classe terminale è una classe derivata dalla classe astratta Processo. La classe rappresenta 
 * un centro senza coda poichè i client prodocuno job dai terminali e aspettano che il job 
 * ritorni al centro prima di lanciarne uno nuovo.
 * @author Daniele Battista
 * @author Luca Dell'Anna
 * @author Enrico Orsini 
 */
public class Terminale extends Processo{

	private static int id = 0;
	private int identificatore; 
	private Random rand = new Random(Seme.getSeme());
	private GeneratoreIperEsponenziale genIper3;
	private final double tx = 10;
	private Job job = null; 
	private double dT;

	/**
	 * E' il costruttore della classe il quale istanzia un terminale con un id univoco incrementale.
	 *
	 */
	public Terminale(){
		super("Terminale", TipoProcesso.Terminale);
		identificatore=(id++) % Simulatore.getNClient();
		genIper3 = new GeneratoreIperEsponenziale(tx, rand, 0.3);

	}

	/**
	 * Metodo che ritorna l'id univoco del terminale.
	 * @return id
	 */
	public final int getId(){
		return identificatore;
	}

	/**
	 * Metodo che genera e ritorna un tempo con distribuzione Iperesponenziale p=0.3.
	 * @return tempo con distribuzione Iperesponenziale p=0.3
	 */
	public double getTempoCentro() {
		
		dT=genIper3.nextIperExp();
		return dT;
	}
	
	/**
	 * Metodo che ritorna un tempo con distribuzione Iperesponenziale p=0.3.
	 * @return tempo con distribuzione Iperesponenziale p=0.3
	 */
	public final double getdT() {
		
		return dT;
	}


	/**
	 * Crea un nuovo job con un id univoco incrementale generato da un terminale
	 * @return nuovo job
	 * @see Job
	 */
	public final Job nextJob() {
		if (job==null)
		job = new Job(this);
		
		return job;
	}

	/**
	 * Metodo che restituisce il job
	 * @return job
	 */
	public final Job getJob()
	{
		return job;
	}
}
