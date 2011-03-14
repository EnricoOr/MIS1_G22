package org.mis.sim;

import java.util.ArrayList;


import org.mis.gen.Random;
import org.mis.gen.Seme;

/**
 * La classe si occupa di salvare tutti i dati durante la simulazione
 * @author Valerio Gentile
 * @author Andrea Giancarli
 * @author Alessandro Mastracci
 */

public class Osservazione {
	
	private int run;
	private int jobT;
	private int nRun = 0, nOss = 0, n = 0, nTh = 0;
	private int jobCompletati = 0;
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
	private boolean inThr = true;
	private static double RN = 0;

	/**
	 * Questo costruttore viene utilizzato durante la stabilizzazione
	 * @param calendario
	 * @param run
	 * @param jobT
	 */
	
	public Osservazione(int run, int jobT) {
		this.run = run;
		this.jobT = jobT;
		media = new double[jobT];
		varianza = new double[jobT];
		singOss = new ArrayList<Double>();
	}
	
	/**
	 * Questo costruttore viene utilizzato durante la generazione dei risultati
	 * @param calendario
	 * @param run
	 */
	
	public Osservazione(int run) {
		this.run = run;
		media = new double[run];
		mediaTh = new double[run];
		nj = new int[run];
		rand = new Random(Seme.getSeme());
		jobT = 50 + (int)(rand.nextNumber()*50);
		nj[0] = jobT;
		throughput = new int[50];
		for(int n=0; n<throughput.length; n++) throughput[n] = 0;
		RN = 0;
	}
	
	/**
	 * Questa funzione incrementa il contatore che tiene conto del numero dei Job completati
	 */
	
	public void jobCompletato()
	{
		jobCompletati++;
	}
	
	/**
	 * Questa funzione si occupa di memorizzare i dati relativi al throughput della CPU
	 * e di creare l'evento di fine simulazione nel caso tutti i dati necessari siano stati ottenuti
	 * @param dT
	 */
	
	public void setThrCpu(double dT)
	{
		throughput[(int)(jobCompletati/dT)]++;	
		mediaTh[nRun] += jobCompletati/dT;
		if(nTh == jobT-1)
		{

			if(nRun < run-1) 
			{
				nRun++;
				nTh = 0;
				jobT = 50 + (int)(rand.nextNumber()*50);
				nj[nRun] = jobT;
				inThr = true;
			}
			else calendario.aggiungiEvento(new FineSimulazione("Fine simulazione", Calendario.getClock()));
		}
		else nTh++;
		jobCompletati = 0;
	}
	
	/**
	 * Questa funzione serve per prelevare il throughput della finestra di osservazione corrente
	 * @return throughput
	 */
	
	public int[] getThrCpu()
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
	
	public int[] getNj()
	{
		return nj;
	}
	
	/**
	 * Funzione che serve per prelevare la media dei tempi di osservazione
	 * @return media
	 */
	
	public double[] getMedia()
	{
		return media;
	}
	
	/**
	 * Funzione che serve per prelevare la media del throughput
	 * @return
	 */
	
	public double[] getMediaTh()
	{
		return mediaTh;
	}
	
	/**
	 * Funzione che serve per prelevare la varianza durante la stabilizzazione
	 * @return
	 */
	
	public double[] getVarianza()
	{
		return varianza;
	}
	
	public double getRN()
	{
		return RN/50;
	}
	
	/**
	 * Questa funzione si occupa di memorizzare i dati relativi alle osservazioni durante la stabilizzazione
	 * e di creare l'evento di fine simulazione nel caso tutti i dati necessari siano stati ottenuti
	 * @param oss
	 */
	
	public void aggOssStab(double oss)
	{
		tempSommaOss += oss;
		if(n == nOss)
		{
			if(nRun == run-1) calendario.resetSeme();
			else calendario.resetSistema();
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
				//if(nOss==4)System.out.println("prova");
				nRun = 0;
				n = 0;
				if(nOss == jobT) calendario.aggiungiEvento(new FineSimulazione("Fine simulazione", Calendario.getClock()));
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
		if(!EventoOsservatore.inOsservazione() || inThr)
		{
			media[nRun] += oss;
			if(n==Simulatore.getNClient()-1) RN += media[nRun];
			if(n == jobT)
			{
				if(!EventoOsservatore.inOsservazione()) calendario.ripristinaStato();
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
				else if(!EventoOsservatore.inOsservazione()) calendario.aggiungiEvento(new FineSimulazione("Fine simulazione", Calendario.getClock()));
						else inThr = false;
			}
			else n++;
		}
	}
}