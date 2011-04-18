package org.mis.sim;

import org.mis.processi.Processo;

/**
 * La classe si occupa di salvare tutti i dati durante la simulazione
 * 
 * @author Daniele Battista
 * @author Luca Dell'Anna
 * @author Enrico Orsini
 */
public class Osservazione extends Processo {

	private int ncli;
	public int nOss;
	private int n;
	private double dT;
	private int jobToHost = 0;
	private int jobToDisk = 0;
	private double totTempoRisp = 0;
	private double[] media;
	private double[] mediaTr;
	private int[] distDisk;
	private double varianza;
	private double throughput = 0;

	/**
	 * Costruttore utilizzato durante la stabilizzazione.
	 * 
	 * @param nOss
	 *            numero di osservazioni
	 * @param dT
	 *            finestra di osservazione
	 */
	public Osservazione(int nOss, double dT) {

		super("Osservazione", TipoProcesso.Osservazione);
		this.nOss = nOss;
		this.dT = dT;
		media = new double[nOss];
	}

	/**
	 * Costruttore utilizzato durante la generazione dei risultati.
	 * 
	 * @param nOss
	 *            numero di osservazioni
	 * @param ncli
	 *            numero clients
	 * @param dT
	 *            finestra di osservazione
	 */
	public Osservazione(int nOss, int ncli, double dT) {

		super("Osservazione", TipoProcesso.Osservazione);
		this.ncli = ncli;
		this.nOss = nOss;
		this.n = 0;
		this.dT = dT;
		media = new double[nOss];
		mediaTr = new double[nOss];
		distDisk = new int[31];
	}

	/**
	 * Metodo che incrementa il contatore che tiene conto del numero di jobs che
	 * vanno all'host
	 */
	public final void jobtoHost() {
		jobToHost++;
	}

	/**
	 * Metodo che incrementa il contatore che tiene conto del numero di jobs che
	 * vanno al Disk
	 */
	public final void jobtoDisk() {
		jobToDisk++;
	}

	/**
	 * Metodo che resetta le variabili d'osservazione.
	 */
	public final void resetOss() {
		jobToDisk = 0;
		totTempoRisp = 0;
		jobToHost = 0;
	}

	/**
	 * Metodo che memorizza i dati relativi al throughput in entrata all'Host.
	 */
	public final void setThrHost() {
		throughput = jobToHost / dT;
		media[n] = throughput;
		jobToHost = 0;
	}

	/**
	 * Metodo che ritorna il throughput corrente
	 * 
	 * @return throughput il throughput corrente
	 */
	public final double getThrHst() {
		return throughput;
	}

	/**
	 * Metodo che serve per accumulare i tempi di risposta ai Job nel Centro
	 * Disk
	 */
	public final void aggTempoR(double tempo) {
		distDisk[(int) (tempo * 100)]++;
		totTempoRisp += tempo;
	}

	/**
	 * Getter per il vettore della distribuzione tempi risposta Disk
	 * 
	 * @return int[] vettore tempi risposta Disk
	 */
	public final int[] getDistDisk() {
		return distDisk;
	}

	/**
	 * Metodo che memorizza i dati relativi al tempo di risposta del Disk.
	 * 
	 */
	public final void setThrDisk() {
		if (jobToDisk != 0)
			mediaTr[n] = totTempoRisp / jobToDisk;
		else
			mediaTr[n] = 0;
	}

	/**
	 * Metodo che ritorna la media del throughtput host
	 * 
	 * @return media media del throughtput host
	 */
	public final double getMedia() {
		double med = 0;
		for (int i = 0; i < nOss; i++) {
			med += media[i];
		}
		return (med / nOss);
	}

	/**
	 * Metodo che ritorna la media del tempo di risposta disk
	 * 
	 * @return media del tempo di risposta disk
	 */
	public final double getMediaTr() {
		double med = 0;
		for (int i = 0; i < nOss; i++) {
			med += mediaTr[i];
		}
		return (med / nOss);
	}

	/**
	 * Metodo che ritorna la varianza durante la stabilizzazione
	 * 
	 * @return varianza
	 */
	public final double getVarianza() {
		double med = getMedia();
		double campQua = 0.0;

		for (int i = 0; i < nOss; i++) {
			campQua += Math.pow(media[i] - med, 2);
		}

		varianza = (1.0 / (nOss - 1.0)) * campQua;

		return varianza;
	}

	/**
	 * Metodo che ritorna la varianza del tempo di risposta Disk
	 * 
	 * @return varianza del tempo di risposta Disk
	 */
	public final double getVarianzaTr() {
		double med = getMediaTr();
		double campQua = 0.0;

		for (int i = 0; i < nOss; i++) {
			campQua += (double) Math.pow(mediaTr[i] - med, 2);
		}

		varianza = (double) (1.0 / (nOss - 1.0)) * campQua;

		return varianza;
	}

	/**
	 * Metodo che memorizza i dati relativi alle osservazioni durante la
	 * stabilizzazione
	 * 
	 * @param oss
	 */
	public final void aggOssStab() {
		this.setThrHost();
		n++;
	}

	/**
	 * Metodo che ritorna l'intervallo di confidenza
	 * 
	 * @return double[] limite inferiore e superiore dell'intervallo di
	 *         confidenza
	 */
	public final double[] getIntervConfid() {
		double min = getMedia() - 1.645 * Math.sqrt(getVarianza() / nOss);
		double max = getMedia() + 1.645 * Math.sqrt(getVarianza() / nOss);
		return new double[] { min, max };
	}

	/**
	 * Metodo che ritorna l'intervallo di confidenza per il tempo di risposta
	 * Disk
	 * 
	 * @return double[] limite inferiore e superiore dell'intervallo di
	 *         confidenza per il tempo di risposta Disk
	 */
	public final double[] getIntervConfidTr() {
		double min = getMediaTr() - 1.645 * Math.sqrt(getVarianzaTr() / nOss);
		double max = getMediaTr() + 1.645 * Math.sqrt(getVarianzaTr() / nOss);
		return new double[] { min, max };
	}

	/**
	 * Metodo che memorizza i dati relativi al tempo medio di risposta e di crea
	 * l'evento di fine simulazione nel caso tutti i dati necessari siano stati
	 * ottenuti
	 * 
	 * @param oss
	 */
	public final void aggOss() {
		if (ncli == 20) {
			this.setThrDisk();

		}
		this.setThrHost();
		n++;
	}
}