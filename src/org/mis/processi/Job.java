package org.mis.processi;


/**
 * La classe Job è una classe derivata dalla classe astratta Processo. La
 * classe rappresenta un Job che viene generato da un Terminale e ad esso ritorna
 * una volta terminato il suo ciclo di vita all'interno dell'impianto.
 * @author Daniele Battista
 * @author Luca Dell'Anna
 * @author Enrico Orsini
 */

public class Job extends Processo {
	
	private int id;
	private static int identificatore=0;
	private int jobClass;
	private Terminale generatoDa;
	private double tempoIngrSist; //istante in cui il job è entrato nel sistema
	private double tempoIngrCoda; //istante in cui il job si accoda ad un centro
	private boolean stampa;
	
	public Job(Terminale t){
		super("job " + identificatore++, TipoProcesso.Job);
		id=identificatore;
		jobClass = 1;
		generatoDa = t;
		setStampa(false);
	}
	
	
	/**
	 * Questa funzione restituisce l'id del job
	 * @return id
	 */	
	public int getId(){
		return id;
	}
	
	/**
	 * Funzione che serve per impostare la classe del Job
	 * @param int jc
	 */	
	public void setJobClass(int jc) {
		jobClass = jc;
	}
	
	/**
	 * Funzione che serve per prelevare la classe del Job
	 * @return
	 */
	public int getJobClass() {
		return jobClass;
	}
	
	/**
	 * Questa funzione restituisce il terminale che l'ha generato
	 * @return Terminale generatoDa
	 */	
	public Terminale getGeneratoDa()
	{
		return generatoDa;
	}
	
	/**
	 * Questa funzione memorizza il tempo di ingresso del Job nel sistema
	 * @param tempo
	 */	
	public void setIngresso(double tempo)
	{
		this.tempoIngrSist = tempo;
	}
	
	/**
	 * Questa funzione preleva il tempo di ingresso del Job nel sistema
	 * @return tempoIngrSist
	 */	
	public double getIngresso()
	{
		return this.tempoIngrSist;
	}

	
	/**
	 * Questo metodo memorizza l'istante di tempo in cui il job si accoda ad un centro
	 * @param t
	 */	
	public void setIngCoda(double t)
	{
		this.tempoIngrCoda = t;
	}
	
	/**
	 * Il metodo restituisce l'istante di clock in cui il job si è accodato in un centro.
	 * @return tempoIngrCoda
	 */	
	public double getIngCoda()
	{
		return this.tempoIngrCoda;
	}


	/**
	 * Metodo per settare la stampa o meno del job
	 * @param stampa the stampa to set
	 */
	public void setStampa(boolean stampa) {
		this.stampa = stampa;
	}


	/**
	 * Getter per il valore di stampa
	 * @return the stampa
	 */
	public boolean getStampa() {
		return stampa;
	}

}