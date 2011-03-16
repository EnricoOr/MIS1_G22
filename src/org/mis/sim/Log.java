package org.mis.sim;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.mis.processi.Job;
import org.mis.processi.Processo;


/**
 * Si occupa della creazione del file di log sul disco e di registrare gli eventi che vengono eseguiti.
 * Si occupa anche della chiusura dei file quando richiesto.
 * @author Valerio Gentile
 * @author Andrea Giancarli
 * @author Alessandro Mastracci
 */
public class Log {
	
	private String pathName;
	private PrintWriter fout;
	private boolean verbose = false;
	private boolean stab = false;
	private boolean logR = false;
	private static int num = 0;
	
	/**
	 * Crea la cartella log, se non esiste, e tutti i file per il log richiesti.
	 * @param numero di file di log, uno per ogni client
	 * @param per abilitare il verbose mode.
	 * @see #creaDir()
	 */
	public Log(int n, boolean verbose) {
		this.verbose = verbose;
		if (verbose) {
			creaDir();
			pathName = "./log/log"+n+".txt";
			File file = new File(pathName);
			try {
				file.createNewFile();
				fout = new PrintWriter(new BufferedWriter(new FileWriter(file)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Crea la cartella log, se non esiste, e tutti i file per il log richiesti.
	 * Inoltre aggiunge una stringa al nome del file per identificare il tipo di log.
	 * @param numero del client
	 * @param specifica il tipo di log 
	 */
	public Log(int n, String tipo) {
		creaDir();
		if(!Simulatore.stab()){
			pathName = "./log/log"+tipo+n+".txt";
			File file = new File(pathName);
			logR = true;
			try {
				file.createNewFile();
				fout = new PrintWriter(new BufferedWriter(new FileWriter(file)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Log(int n, String tipo, boolean stab) {
		this.stab = stab;
		if (stab) {
			creaDir();
			pathName = "./log/log"+tipo+n+".txt";
			File file = new File(pathName);
			try {
				file.createNewFile();
				fout = new PrintWriter(new BufferedWriter(new FileWriter(file)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * E' una funzione ausiliaria che crea la cartella log nel path di esecuzione se non esiste
	 * gia'.
	 */
	private void creaDir() {
		File f = new File("./log/");
		if (!f.exists())
			f.mkdir();
	}
	
	/**
	 * Ritorna il percorso del file
	 */
	public String getPathName() {
		return pathName;
	}
	
	/**
	 * chiude il file posseduto dall'oggetto log.
	 */
	public void close() {
		fout.close();
	}
	
	/**
	 * fa stampare nel log solo le prime cinque cifre decimali. Solo per migliorare la lettura
	 * @param number
	 * @return numero con massimo cinque tre decimali.
	 */
	public double tronca(double n) {
		return (Math.floor (n * 1000) / 1000);
	}
	
	/**
	 * Questa funzione restituisce l'istante attuale del tempo simulato in versione troncata
	 * @return clock
	 */
	
	public double tempo(SimTime t)
	{
		return tronca(t.getSimTime());
	}
	
	/**
	 * Questa funzione restituisce l'istante attuale del tempo simulato senza troncamenti
	 * @return clock
	 */
	
	public double tempo2(SimTime t)
	{
		return t.getSimTime();
	}
	
	/**
	 * Scrive nel file il testo che gli viene passato
	 * @param testo di log
	 */
	public void scrivi(String log) {
		if (verbose) {
			fout.println(log);
		}
		if (stab) {
			fout.println(log);
		}
		if(logR){
			fout.println(log);
		}
			
	}
	
	/**
	 * Scrive nel file il testo che gli viene passato
	 * Usato per stampare la matrice di stabilizzazione
	 * @param testo di log
	 */
	public void scriviMat(String log) {
			fout.print(log);
	}
	
	/**
	 * Questa funzione stampa l'evento fine simulazione
	 */
	public void scrivi(SimTime t) {
			scrivi("{" + tempo(t) + "} La simulazione finisce.");
	}
	
	/**
	 * Questa funzione stampa il cambio di classe di un job
	 * @param job
	 * @param classe
	 */
	public void scrivi(Job job, int classe, SimTime t) {
		scrivi("{" + tempo(t) + "} Il " + job.getNome() + " diventa di classe " + classe + ".");
	}

	/**
	 * Questa funzione stampa che il centro è libero
	 * @param centro
	 */
	public void scrivi(Processo centro, SimTime t)
	{
		scrivi("{" + tempo(t) + "} Il " + centro.getNome() + " è ora libero.");
	}
	
	/**
	 * Questa funzione stampa l'istante in cui prendiamo il tempo per il TMR
	 * @param centro
	 */
	public void scriviTMR(Job job, SimTime t)
	{
		num++;
		scrivi("{" + tempo(t) + "} -------------------------------------------------------- Il " + job.getNome() + " sta andando verso stampante. TMR " + (tempo2(t)-job.getIngresso()) + " num: " + num);
	}
	
	/**
	 * Questa funzione stampa l'evento di generazione Job
	 * @param Evento di arrivo
	 */
	public void scrivi(EventoArrivoUtente e) {
		scrivi("{" + tempo() + "} Il <" + e.getJobEseguito().getGeneratoDa().getNome() +
				"> sta generando il " + e.getJobEseguito().getNome() + " che arrivera' a {" +
				tronca(e.getTempoServizio()) + "}.");
	}

	/**
	 * Questa funzione stampa le uscite di un job da un centro
	 * @param job attivo
	 * @param centro d'uscita
	 */
	public void scrivi(Job jobEseguito, Processo centro, SimTime t) {
		scrivi("{" + tempo(t) + "} Il " + jobEseguito.getNome() + " esce da <" +
				centro.getNome() + ">.");
	}
	
	/**
	 * Questa funzione stampa gli eventi di servizio da un centro all'altro
	 * @param Evento di servizio
	 */
	public void scrivi(Processo provenienza, Evento e, SimTime t) {
		
			scrivi("{" + tempo(t) + "} Il " + e.getJobEseguito().getNome() +
					" da <" + provenienza.getNome() + "> occupa <" + e.getCentro().getNome() +
					"> fino a {" + tronca(e.getTempoServizio()) + "}.");
	}

	/**
	 * Questa funzione stampa gli eventi di servizio di job che un centro preleva dalla coda
	 * @param Evento di servizio
	 */
	public void scrivi(Processo e, SimTime t) {
		scrivi("{" + tempo(t)+"} <" + e.getNome() +
				"> preleva il " + e.getNome() + " dalla sua coda e lo serve" +
				" fino a {" + tronca(e.getTempoServizio()) + "}.");
	}
	
	/**
	 * Questa funzione stampa gli accodamenti
	 * @param jobEseguito
	 * @param tempo
	 * @param centro
	 */
	public void scrivi(Job jobEseguito, Processo prov, Processo centro, SimTime t) {
		
				scrivi("{" + tempo(t) + "} <" + centro.getNome() + "> occupato. Il "+
						jobEseguito.getNome() + " da <"+prov.getNome() + "> viene messo in coda <"
						+ centro.getNome() + ">.");
	}

	/**
	 * Questa funzione stampa l'uscita di un job.
	 * @param jobEseguito
	 * @param tempo
	 */
	public void scrivi(Job jobEseguito, SimTime t) {
			scrivi("{" + tempo(t) + "} Il " + jobEseguito.getNome() + " esce dal sistema"
					+ " e torna da " + jobEseguito.getGeneratoDa().getNome() + ". Risposta = " +
					tronca(tempo(t) - jobEseguito.getIngresso()));
	}

	/**
	 * Scrive la media del throughput
	 * @param tempoInizio
	 * @param tempoFine
	 * @param mediaOsservazione
	 */
	public void scrivi(double tempoInizio, double tempoFine, double mediaOsservazione) {
		scrivi("{" + tronca(tempoFine) + "} La media del throughput tra {" +
				tronca(tempoInizio)+"} e {" + tronca(tempoFine) + "} e' <" +
				tronca(mediaOsservazione) + ">.");
	}
	
}
