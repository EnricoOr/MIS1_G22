package org.mis.processi;

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
	private GeneratoreIperEsponenziale[] genIpExp_p06c = new GeneratoreIperEsponenziale[3];
	private final double[] txc = { 0.058, 0.074, 0.0285 };
	private Random rand = new Random(Seme.getSeme());
	private CodaFIFO[] coda = new CodaFIFO[3];

	private Job current;

	
	/**
	 * E' il costruttore della classe il quale istanzia una cpu.
	 * La legge di distribuzione nel tempo è iperesponnziale con probabilità 0,6.
	 */
	
	public Cpu()
	{
		super("CPU", TipoProcesso.CPU);
		for  (int i = 0; i < txc.length; i++)
		{
			genIpExp_p06c[i] = new GeneratoreIperEsponenziale(txc[i], rand, 0.6);
			coda[i] = new CodaFIFO("Coda " + i + " " + super.getNome());
		}
	}
	
	/**
	 * Funzione la quale ritorna un tempo con distribuzione iperesponeziale con probabilità 0,6. 
	 * E' stato effettuato l'override del metodo della superclasse centro. 
	 * @return IpExp_P06
	 */
	
	public double getTempoCentro(Job jobCorrente) {
		
		this.current=jobCorrente;

		genIpExp_p06 = genIpExp_p06c[jobCorrente.getJobClass() - 1].nextIperExp();
		return genIpExp_p06;
	}
	
	/**
	 * Funzione che indica se le code per il centro cpu sono vuote. 
	 * @return code vuote
	 */
	
	public final boolean codeVuote()
	{
		boolean empty = true;
		for (CodaFIFO c: coda)
		{
			empty = (empty & c.isEmpty());
		}
		return empty;
	}
	
	/**
	 * Funzione la quale ritorna il valore delle code per il centro cpu. SOLO PER TEST - DA RIMUOVERE
	 * @return code vuote
	 */
	
	public final String getLenCode()
	{
		String lenCode = "";
		
		for (CodaFIFO c: coda)
		{
			lenCode += c.getNome() + " = " + c.getDimensione() + ", ";
		}
		return lenCode;
	}

	/**
	 * Funzione la quale ritorna un tempo con distribuzione iperesponeziale con probabilità 0,6. 
	 * E' stato effettuato l'override del metodo della superclasse centro. 
	 * @return IpExp_P06
	 */
	

	public final double getTempoCentro() {
		return genIpExp_p06;
	}


	/**
	 * Funzione la quale estrae dalla coda un job in base alla disciplina in gioco 
	 * random. E' stato effettuato l'override del metodo della superclasse centro.
	 * @return job
	 */
	

	public final Job pop()
	{
		int n = (int)(rand.nextNumber() * coda.length);
		while(true)
		{
			if(!coda[n].isEmpty())
				return coda[n].pop();
			else
			{
				if (n < 2) n++;
				else n = 0;
			}
		}
	}

	/**
	 * Funzione la quale inserisce un job in coda. E' stato effettuato l'override 
	 * del metodo della superclasse centro.
	 * @param job
	 */
	

	public final void push(Job job)
	{
		coda[job.getJobClass() - 1].push(job);
	}

	/**
	 * Questa funzione restituisce il prossimo numero random della sequenza
	 * @return rand.nextNumber()
	 */
	
	public double nextRand()
	{
		return rand.nextNumber();
	}
	
	public final Job getJobCorrente(){
		
		return current;
	}
	
	/**
	 * Questa funzione resetta lo stato del centro
	 */
	
	public final void reset()
	{
		for (CodaFIFO c: coda)
			c.resetCoda();

		this.passivate();
	}
}
