package org.mis.sim;

import org.mis.processi.Processo;

/**
 * La classe si occupa di salvare tutti i dati durante la simulazione
 * @author 
 * @author
 * @author 
 */

public class Osservazione extends Processo{
	
	private int ncli;
	public int nOss; 
	private int n;
	private double dT;
	private int jobToHost=0;
	private int jobToDisk=0;
	private double totTempoRisp = 0;
	private double[] media;
	private double[] mediaTr;
	private int[] distDisk;
	private double varianza;
	private double throughput=0;


	/**
	 * Questo costruttore viene utilizzato durante la stabilizzazione
	 * @param run
	 * @param jobT
	 */
	
	public Osservazione(int nOss, double dT) {
		
		super("Osservazione", TipoProcesso.Osservazione);
		this.nOss=nOss;
		this.dT=dT;
		media = new double[nOss];
	}
	
	/**
	 * Questo costruttore viene utilizzato durante la generazione dei risultati
	 * @param run
	 */
	
	public Osservazione(int nOss, int ncli, double dT) {
		
		super("Osservazione", TipoProcesso.Osservazione);
		this.ncli = ncli;
		this.nOss=nOss;
		this.n=0;
		this.dT=dT;
		media = new double[nOss];
		mediaTr = new double[nOss];
		distDisk = new int[160];
	}
	
	/**
	 * Questo metodo incrementa il contatore che tiene conto del numero che vanno all'host
	 */
	public final void jobtoHost()
	{
		jobToHost++;
	}
	
	/**
	 * Questo metodo incrementa il contatore che tiene conto del numero che vanno al Disk
	 */
	public final void jobtoDisk()
	{
		jobToDisk++;
	}
	
	/**
	 * Questo metodo resetta le varie variabili d'osservazione.
	 */
	public final void resetOss()
	{
		jobToDisk=0;
		totTempoRisp=0;
		jobToHost=0;
	}
	
	/**
	 * Questa funzione si occupa di memorizzare i dati relativi al throughput in entrata all'Host.
	 * @param dT
	 */
	
	public final void setThrHost()
	{	
		throughput = jobToHost/dT;
		media[n] = throughput;
		//System.out.println("job="+jobToHost+"th="+throughput);
		jobToHost=0;
	}
	
	/**
	 * Questa funzione serve per prelevare il throughput corrente
	 * @return throughput
	 */
	
	public final double getThrHst()
	{
		return throughput;
	}
	
	
	/**
	 * Funzione che serve per accumulare i tempi di risposta ai Job nel Centro Disk
	 * @return void
	 */
	public final void aggTempoR(double tempo)
	{
		distDisk[(int)(tempo * 100)]++;
		totTempoRisp += tempo;
	}
	
	
	/**
	 * Getter per il vettore della distribuzione tempi risposta disk
	 * @return int[] vettore tempi risposta Disk
	 */
	public final int[] getDistDisk()
	{
		return distDisk;
	}
	
	/**
	 * Questa funzione si occupa di memorizzare i dati relativi al throughput in entrata al Disk.
	 * @param dT
	 */
	
	public final void setThrDisk()
	{
		mediaTr[n] = totTempoRisp/jobToDisk;
	}
	
		
	/**
	 * Funzione che serve per prelevare la media del throughtput host
	 * @return media
	 */
	
	public final double getMedia()
	{
		double med=0;
		for (int i=0;i<nOss;i++){
			med+=media[i];
		}
		return (med/nOss);
	}
	
	/**
	 * Funzione che serve per prelevare la media del tempo di risposta disk
	 * @return
	 */
	
	public final double getMediaTr()
	{
		double med=0;
		for (int i=0;i<nOss;i++){
			med+=mediaTr[i];
		}
		return (med/nOss);
	}
	
	/**
	 * Funzione che serve per prelevare la varianza durante la stabilizzazione
	 * @return
	 */
	
	public final double getVarianza()
	{
		double campQua=0.000;
		double medQua=0.000;
		for (int i=0;i<nOss;i++){
			campQua += (double)Math.pow(media[i], 2);
			medQua+=media[i];
		}
		
		varianza = (double)(campQua/nOss)-Math.pow((double)(medQua/nOss), 2);
		
		return varianza;
	}
	
	
	/**
	 * Questa funzione si occupa di memorizzare i dati relativi alle osservazioni durante la stabilizzazione
	 * @param oss
	 */
	
	public final void aggOssStab()
	{
	
		this.setThrHost();
		n++;
			
	
	}

	/**
	 * Questa funzione si occupa di memorizzare i dati relativi al tempo medio di risposta
	 * e di creare l'evento di fine simulazione nel caso tutti i dati necessari siano stati ottenuti
	 * @param oss
	 */
	
	public final void aggOss()
	{

		if (ncli==20){
			this.setThrDisk();
			
		}
		this.setThrHost();
		n++;
		
	}
			

}