package org.mis.processi;

import java.util.Vector;

import org.mis.code.CodaFIFO;
import org.mis.gen.GeneratoreIperEsponenziale;
import org.mis.gen.Random;
import org.mis.gen.Seme;



/**
 * La classe cpu e' una classe derivata dalla classe astratta processo. La classe rappresenta 
 * un centro con coda di tipo FIFO e tempo con distribuzione iperesponenziale con 
 * probabilità 0,6. 
 * @author 
 * @author 
 * @author 
 */

public class Cpu extends Processo {

	private double genIpExp_p06;
	private GeneratoreIperEsponenziale genIpExp_p06c1;
	private GeneratoreIperEsponenziale genIpExp_p06c2;
	private GeneratoreIperEsponenziale genIpExp_p06c3;
	private final double txc1 = 0.058;
	private final double txc2 = 0.074;
	private final double txc3 = 0.0285;
	private Random rand = new Random(Seme.getSeme());
	private CodaFIFO coda1 = new CodaFIFO("Coda1" + super.getNome());
	private CodaFIFO coda2 = new CodaFIFO("Coda2" + super.getNome());
	private CodaFIFO coda3 = new CodaFIFO("Coda3" + super.getNome());
	public Vector<Job> coda1s;
	public Vector<Job> coda2s;
	public Vector<Job> coda3s;
	private Job current;

	
	/**
	 * E' il costruttore della classe il quale istanzia una cpu.
	 * La legge di distribuzione nel tempo è iperesponnziale con probabilità 0,6.
	 */
	
	public Cpu(){
		super("CPU");
		genIpExp_p06c1 = new GeneratoreIperEsponenziale(txc1, rand, 0.6);
		genIpExp_p06c2 = new GeneratoreIperEsponenziale(txc2, rand, 0.6);
		genIpExp_p06c3 = new GeneratoreIperEsponenziale(txc3, rand, 0.6);

	}
	
	/**
	 * Funzione la quale ritorna un tempo con distribuzione iperesponeziale con probabilità 0,6. 
	 * E' stato effettuato l'override del metodo della superclasse centro. 
	 * @return IpExp_P06
	 */
	
	public double getTempoCentro(Job jobCorrente) {
		
		this.current=jobCorrente;

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
	

	public double getTempoCentro() {
		return genIpExp_p06;
	}


	/**
	 * Funzione la quale estrae dalla coda un job in base alla disciplina in gioco 
	 * random. E' stato effettuato l'override del metodo della superclasse centro.
	 * @return job
	 */
	

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
	
	public Job getJobCorrente(){
		
		return current;
	}
	
	/**
	 * Questa funzione resetta lo stato del centro
	 */
	
	public void reset()
	{
		coda1.resetCoda();
		coda2.resetCoda();
		coda3.resetCoda();
		this.passivate();
	}
}
