package org.mis.sim;

import java.util.ArrayList;


import org.mis.gen.Random;
import org.mis.gen.Seme;
import org.mis.processi.Processo;

/**
 * La classe si occupa di salvare tutti i dati durante la simulazione
 * @author 
 * @author
 * @author 
 */

public class Osservazione extends Processo{
	
	private int run;
	private int jobT;
	private int nRun = 0, nOss = 0, n = 0, nTh = 0;
	private int jobCompletati = 0;
	private int jobToHost=0;
	private double totTempoRisp = 0;
	private double[] media;
	private double[] mediaTh;
	private double[] varianza;
	private int[] nj;
	private int[] throughput;
	private ArrayList<Double> singOss;
	private double tempSommaOss = 0;
	private double tempSommaRun = 0;
	private Random rand = null;


	/**
	 * Questo costruttore viene utilizzato durante la stabilizzazione
	 * @param run
	 * @param jobT
	 */
	
	public Osservazione(int run, int jobT) {
		super("osservazione");
		this.run = run;
		this.jobT = jobT;
		media = new double[jobT];
		varianza = new double[jobT];
		singOss = new ArrayList<Double>();
	}
	
	/**
	 * Questo costruttore viene utilizzato durante la generazione dei risultati
	 * @param run
	 */
	
	public Osservazione(int run) {
		super("osservazione");
		this.run = run;
		media = new double[run];
		mediaTh = new double[run];
		nj = new int[run];
		rand = new Random(Seme.getSeme());
		jobT = 50 + (int)(rand.nextNumber()*50);
		nj[0] = jobT;
		throughput = new int[50];
		for(int n=0; n<throughput.length; n++) throughput[n] = 0;
	}
	
	/**
	 * Questo metodo incrementa il contatore che tiene conto del numero dei Job completati
	 */
	public void jobCompletato()
	{
		jobCompletati++;
	}
	
	/**
	 * Questo metodo incrementa il contatore che tiene conto del numero che vanno all'host
	 */
	public void jobtoHost()
	{
		jobToHost++;
	}
	
	/**
	 * Questa funzione si occupa di memorizzare i dati relativi al throughput in entrata all'Host.
	 * @param dT
	 */
	
	public void setThrHost(double dT)
	{
		throughput[(int)(jobToHost/dT)]++;	
		mediaTh[nRun] += jobToHost/dT;
		if(nTh == jobT-1)
		{

			if(nRun < run-1) 
			{
				nRun++;
				nTh = 0;
				nj[nRun] = jobT;

			}
		}
		else nTh++;
		jobCompletati = 0;
	}
	
	/**
	 * Questa funzione serve per prelevare il throughput della finestra di osservazione corrente
	 * @return throughput
	 */
	
	public int[] getThrHst()
	{
		return throughput;
	}
	
	
	
	public void aggTempoR(double tempo)
	{
		totTempoRisp += tempo;
	}

	/**
	 * Funzione che serve per prelevare i valori di Nj
	 * @return nj
	 */
	
	public int getNj()
	{
		return nj;
	}
	
	/**
	 * Funzione che serve per prelevare la media dei tempi di osservazione
	 * @return media
	 */
	
	public double getMedia()
	{
		return media;
	}
	
	/**
	 * Funzione che serve per prelevare la media del throughput
	 * @return
	 */
	
	public double getMediaTh()
	{
		return mediaTh;
	}
	
	/**
	 * Funzione che serve per prelevare la varianza durante la stabilizzazione
	 * @return
	 */
	
	public double getVarianza()
	{
		return varianza;
	}
	
	
	/**
	 * Questa funzione si occupa di memorizzare i dati relativi alle osservazioni durante la stabilizzazione
	 * @param oss
	 */
	
	public void aggOssStab(double oss)
	{
		tempSommaOss += oss;
		if(n == nOss)
		{
		

			if(nRun < run-1) 
			{
				tempSommaRun += tempSommaOss / (n+1);
				singOss.add(tempSommaOss / (n+1));
				tempSommaOss = 0;
				nRun++;
				n = 0;
			}
			else
			{
				tempSommaRun += tempSommaOss / (n+1);
				singOss.add(tempSommaOss / (n+1));
				tempSommaOss = 0;
				if(nOss%10==0)System.out.println("nOss " + nOss);
				media[nOss] = tempSommaRun / run;
				for(int c=0; c<singOss.size(); c++)
				{
					varianza[nOss] += Math.pow(singOss.get(c) - media[nOss], 2);
				}
				varianza[nOss] /= (singOss.size()-1);
				singOss.clear();
				tempSommaRun = 0;
				nOss++;
				nRun = 0;
				n = 0;
			}
		}
		else n++;
	}

	/**
	 * Questa funzione si occupa di memorizzare i dati relativi al tempo medio di risposta
	 * e di creare l'evento di fine simulazione nel caso tutti i dati necessari siano stati ottenuti
	 * @param oss
	 */
	
	public void aggOss(double oss)
	{

			media[nRun] += oss;
			if(n==Simulatore.getNClient()-1)
			if(n == jobT)
			{
		
				if(nRun < run-1) 
				{
					n = 0;
					if(!EventoOsservatore.inOsservazione())
					{
						nRun++;
						jobT = 50 + (int)(rand.nextNumber()*50);
						nj[nRun] = jobT;
					}
					else inThr = false;
				}
			}
			
			n++;
		}

}