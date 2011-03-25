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
	private int nOss = 0, n=0;
	private int jobCompletati = 0;
	private int jobToHost=0;
	private double totTempoRisp = 0;
	private double[] media;
	private double[] mediaTr;
	private double varianza;
	private double throughput=0;


	/**
	 * Questo costruttore viene utilizzato durante la stabilizzazione
	 * @param run
	 * @param jobT
	 */
	
	public Osservazione(int nOss) {
		
		super("osservazione");
		this.nOss=nOss;
		media = new double[nOss];


	}
	
	/**
	 * Questo costruttore viene utilizzato durante la generazione dei risultati
	 * @param run
	 */
	
	public Osservazione(int nOss, int ncli) {
		
		super("osservazione");
		this.ncli = ncli;
		this.nOss=nOss;
		media = new double[nOss];
		mediaTr = new double[nOss];
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
	
	public void setThrHost()
	{
		
		throughput = jobToHost/hTime.doubleValue();
		media[n] = jobToHost/hTime.doubleValue();

	}
	
	/**
	 * Questa funzione serve per prelevare il throughput corrente
	 * @return throughput
	 */
	
	public double getThrHst()
	{
		return throughput;
	}
	
	
	
	public void aggTempoR(double tempo)
	{
		totTempoRisp += tempo;
	}
	
	/**
	 * Funzione che serve per prelevare la media del throughtput host
	 * @return media
	 */
	
	public double getMedia()
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
	
	public double getMediaTr()
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
	
	public double getVarianza()
	{
		double campQua=0;
		double medQua=0;
		for (int i=0;i<nOss;i++){
			campQua += Math.pow(media[i], 2);
			medQua+=media[i];
		}
		
		varianza = (campQua/nOss)-Math.pow((medQua/nOss), 2);
		
		return varianza;
	}
	
	
	/**
	 * Questa funzione si occupa di memorizzare i dati relativi alle osservazioni durante la stabilizzazione
	 * @param oss
	 */
	
	public void aggOssStab()
	{
	
		this.setThrHost();
		if(n<nOss) n++;
			
	
	}

	/**
	 * Questa funzione si occupa di memorizzare i dati relativi al tempo medio di risposta
	 * e di creare l'evento di fine simulazione nel caso tutti i dati necessari siano stati ottenuti
	 * @param oss
	 */
	
	public void aggOss()
	{

		if (ncli==20){
			
			
		}
		this.setThrHost();
		if(n<nOss) n++;
		
	}
			

}