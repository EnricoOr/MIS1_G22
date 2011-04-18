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
	
	/**
	 * Costruttore della classe. Memorizza da quale terminale il job è stato generato.
	 * @param t il terminale che ha generato il job
	 */
	public Job(Terminale t){
		super("job " + identificatore++, TipoProcesso.Job);
		id=identificatore;
		jobClass = 1;
		generatoDa = t;
	}
	
	
	/**
	 * Metodo che restituisce l'id del job
	 * @return id l'id del job
	 */	
	public int getId(){
		return id;
	}
	
	/**
	 * Metodo che serve per impostare la classe del Job
	 * @param int jc la classe del job da impostare
	 */	
	public void setJobClass(int jc) {
		jobClass = jc;
	}
	
	/**
	 * Metodo che serve per prelevare la classe del Job
	 * @return la classe del job
	 */
	public int getJobClass() {
		return jobClass;
	}
	
	/**
	 * Metodo che restituisce il terminale che ha generato il job
	 * @return Terminale generatoDa il terminale che ha generato il job
	 */	
	public Terminale getGeneratoDa()
	{
		return generatoDa;
	}
	
	/**
	 * Metodo che memorizza il tempo di ingresso del Job nel sistema
	 * @param tempo istante di ingresso del job nel sistema
	 */	
	public void setIngresso(double tempo)
	{
		this.tempoIngrSist = tempo;
	}
	
	/**
	 * Metodo che restituisce il tempo di ingresso del Job nel sistema
	 * @return tempoIngrSist l'istante di ingresso del job nel sistema
	 */	
	public double getIngresso()
	{
		return this.tempoIngrSist;
	}

	
	/**
	 * Metodo che memorizza l'istante di tempo in cui il job si accoda ad un centro
	 * @param t il tempo di ingresso nella coda del centro
	 */	
	public void setIngCoda(double t)
	{
		this.tempoIngrCoda = t;
	}
	
	/**
	 * Metodo che restituisce l'istante in cui il job si è accodato in un centro.
	 * @return tempoIngrCoda il tempo in cui il job si è accodato in un centro
	 */	
	public double getIngCoda()
	{
		return this.tempoIngrCoda;
	}
}