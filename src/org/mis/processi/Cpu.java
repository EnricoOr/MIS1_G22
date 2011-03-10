package org.mis.processi;

import java.util.Vector;

import org.mis.code.Coda;
import org.mis.gen.Generatore;
import org.mis.gen.GeneratoreIperEsponenziale;
import org.mis.gen.Random;
import org.mis.gen.Seme;
import org.mis.gen.Job;


/**
 * La classe cpu e' una classe derivata dalla classe astratta centro. La classe rappresenta 
 * un centro con coda di tipo random e tempo con distribuzione iperesponenziale con 
 * probabilità 0,6. 
 * @author 
 * @author 
 * @author 
 */

public class Cpu extends Centro {

	private double genIpExp_p06;
	private GeneratoreIperEsponenziale genIpExp_p06c1;
	private GeneratoreIperEsponenziale genIpExp_p06c2;
	private GeneratoreIperEsponenziale genIpExp_p06c3;
	private final double txc1 = 0.058;
	private final double txc2 = 0.074;
	private final double txc3 = 0.0285;
	private Random rand = new Random(Seme.getSeme());
	private CodaRand coda1 = new CodaRand("Coda1" + super.getNome(), rand);
	private CodaRand coda2 = new CodaRand("Coda2" + super.getNome(), rand);
	private CodaRand coda3 = new CodaRand("Coda3" + super.getNome(), rand);
	public Vector<Job> coda1s;
	public Vector<Job> coda2s;
	public Vector<Job> coda3s;
	private boolean occupato;
	private boolean occupatos;
	
	/**
	 * E' il costruttore della classe il quale istanzia una cpu.
	 * La legge di distribuzione nel tempo è iperesponnziale con probabilità 0,6.
	 */
	
	public Cpu(){
		super("Cpu");
		genIpExp_p06c1 = new GeneratoreIperEsponenziale(txc1, rand);
		genIpExp_p06c2 = new GeneratoreIperEsponenziale(txc2, rand);
		genIpExp_p06c3 = new GeneratoreIperEsponenziale(txc3, rand);
		occupato = false;
	}
	
	/**
	 * Funzione la quale ritorna un tempo con distribuzione iperesponeziale con probabilità 0,6. 
	 * E' stato effettuato l'override del metodo della superclasse centro. 
	 * @return IpExp_P06
	 */
	
	public double getTempoCentro(Job jobCorrente) {

		if(jobCorrente.getJobClass() == 2) {
			return genIpExp_p06 = genIpExp_p06c2.nextIperExp();
		}
		else if(jobCorrente.getJobClass() == 3) {
			genIpExp_p06 = genIpExp_p06c3.nextIperExp();
			return genIpExp_p06;
		}
		else {
			genIpExp_p06 = genIpExp_p06c1.nextIperExp();
			return genIpExp_p06;
		}
	}
	
	/**
	 * Funzione la quale ritorna il valore delle code per il centro cpu con valore nullo. 
	 * @return code vuote
	 */
	
	public boolean codeVuote() {
		return (coda1.getDimensione()==0 && coda2.getDimensione()==0 && coda3.getDimensione()==0);
	}

	/**
	 * Funzione la quale ritorna un tempo con distribuzione iperesponeziale con probabilità 0,6. 
	 * E' stato effettuato l'override del metodo della superclasse centro. 
	 * @return IpExp_P06
	 */
	
	@Override
	public double getTempoCentro() {
		return genIpExp_p06;
	}
	
	/**
	 * Funzione la quale ritorna true se il centro e' occupato e false se il centro è libero. 
	 * E' stato effettuato l'override del metodo della superclasse centro.
	 * @return occupato
	 */
	
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

	/**
	 * Funzione la quale estrae dalla coda un job in base alla disciplina in gioco 
	 * random. E' stato effettuato l'override del metodo della superclasse centro.
	 * @return job
	 */
	
	@Override
	public Job pop() {
		while(true)
		{
			int n = (int)(rand.nextNumber() * 3);
			if(n==1 && coda1.getDimensione() != 0) return coda1.pop();
			else if ((n==2 || n==3) && coda2.getDimensione() != 0) return coda2.pop();
			else if(n==0 && coda3.getDimensione() != 0) return coda3.pop();
		}
	}

	/**
	 * Funzione la quale inserisce un job in coda. E' stato effettuato l'override 
	 * del metodo della superclasse centro.
	 * @param job
	 */
	
	@Override
	public void push(Job job) {
		if(job.getJobClass() == 2) {
			coda2.push(job);
		}
		else if(job.getJobClass() == 3) {
			coda3.push(job);
		}
		else {
			coda1.push(job);
		}
	}

	/**
	 * Questa funzione restituisce il prossimo numero random della sequenza
	 * @return rand.nextNumber()
	 */
	
	public double nextRand()
	{
		return rand.nextNumber();
	}
	
}
