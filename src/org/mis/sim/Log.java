package org.mis.sim;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;

import org.mis.processi.Job;
import org.mis.processi.Processo;

/**
 * Classe che si occupa della creazione del file di log sul disco, della
 * registrazione degli eventi che vengono eseguiti e della chiusura dei file
 * quando richiesto.
 * 
 * @author Daniele Battista
 * @author Luca Dell'Anna
 * @author Enrico Orsini
 */
public class Log {

	private String pathName;
	private PrintWriter fout;
	private boolean verbose = false;
	private boolean stab = false;
	private boolean logR = false;

	/**
	 * Costruttore della classe. Crea la cartella log, se non esiste, e tutti i
	 * file per il log richiesti.
	 * 
	 * @param n
	 *            numero di file di log, uno per ogni client
	 * @param verbose
	 *            true per abilitare il verbose mode.
	 */
	public Log(int n, boolean verbose) {
		this.verbose = verbose;
		if (verbose) {
			creaDir();
			pathName = "./log/log" + n + ".txt";
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
	 * Costruttore della classe. Crea la cartella log, se non esiste, e tutti i
	 * file per il log richiesti. Inoltre aggiunge una stringa al nome del file
	 * per identificare il tipo di log.
	 * 
	 * @param n
	 *            numero del client
	 * @param tipo
	 *            specifica il tipo di log
	 */
	public Log(int n, String tipo) {
		creaDir();
		if (!Simulatore.stab()) {
			pathName = "./log/log" + tipo + n + ".txt";
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

	/**
	 * Costruttore della classe. Se in fase di stabilizzazione, crea la cartella
	 * log, se non esiste, e tutti i file per il log richiesti, inoltre aggiunge
	 * una stringa al nome del file per identificare il tipo di log.
	 * 
	 * @param n
	 *            numero del client
	 * @param tipo
	 *            specifica il tipo di log
	 * @param stab
	 *            true se in stabilizzazione
	 */
	public Log(int n, String tipo, boolean stab) {
		this.stab = stab;
		if (stab) {
			creaDir();
			pathName = "./log/log" + tipo + n + ".txt";
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
	 * Metodo che crea la cartella log nel path di esecuzione se non esiste già.
	 */
	private void creaDir() {
		File f = new File("./log/");
		if (!f.exists())
			f.mkdir();
	}

	/**
	 * Metodo che ritorna il percorso del file di log corrente
	 * 
	 * @return il percorso del file di log corrente
	 */
	public String getPathName() {
		return pathName;
	}

	/**
	 * Metodo che chiude il file posseduto dall'oggetto log.
	 */
	public void close() {
		fout.close();
	}

	/**
	 * Metodo che imposta la precisione delle cifre decimali nel log alle sole
	 * prime cinque.
	 * 
	 * @param n
	 *            il numero da troncare
	 * @return numero con massimo tre decimali.
	 */
	public double tronca(double n) {
		return (Math.floor(n * 1000) / 1000);
	}

	/**
	 * Metodo che restituisce l'istante attuale del tempo simulato in versione
	 * troncata
	 * 
	 * @return il clock troncato
	 */
	public double tempo(SimTime t) {
		return tronca(t.getSimTime());
	}

	/**
	 * Metodo che restituisce l'istante attuale del tempo simulato senza
	 * troncamenti
	 * 
	 * @return il clock non troncato
	 */
	public double tempo2(SimTime time) {
		return time.getSimTime();
	}

	/**
	 * Metodo che scrive nel file il testo che gli viene passato
	 * 
	 * @param testo
	 *            di log
	 */
	public void scrivi(String log) {
		if (verbose) {
			fout.println(log);
		}
		if (stab) {
			fout.println(log);
		}
		if (logR) {
			fout.println(log);
		}
	}

	/**
	 * Metodo che stampa l'evento fine simulazione
	 */
	public void scrivi(SimTime t) {
		scrivi("{"
				+ tempo(t)
				+ "} La simulazione finisce.\n-->Tempo impiegato dalla simulazione: "
				+ t.getSimDuration() / 60000 + " minuti.");
	}

	/**
	 * Metodo che stampa la coda di Hold
	 * 
	 * @param hold
	 *            la coda di Hold
	 */
	public void print_h(PriorityQueue<Processo> hold) {
		ArrayList<Processo> holdtmp = new ArrayList<Processo>(hold);
		Collections.sort(holdtmp);
		scrivi("----lista oggetti in hold----");
		for (int i = 0; i < holdtmp.size(); i++) {
			scrivi("-->" + holdtmp.get(i).getNome() + " htime:"
					+ holdtmp.get(i).getTime());
		}
		scrivi("----Fine lista oggetti in hold----");
	}

	/**
	 * Metodo che stampa la coda degli oggetti passivi
	 * 
	 * @param passivate
	 *            la coda degli oggetti passivi
	 */
	public void print_p(ArrayList<Processo> passivate) {

		scrivi("----lista oggetti passivi----");
		for (int i = 0; i < passivate.size(); i++) {
			scrivi("-->" + passivate.get(i).getNome());
		}
		scrivi("----Fine lista oggetti passivi----");

	}

	/**
	 * Metodo che stampa il cambio di classe di un job
	 * 
	 * @param job
	 *            il job
	 * @param classe
	 *            la nuova classe
	 * @param t
	 *            il clock della simulazione
	 */
	public void scrivi(Job job, int classe, SimTime t) {
		scrivi("{" + tempo(t) + "} Il " + job.getNome() + " diventa di classe "
				+ classe + ".");
	}

	/**
	 * Metodo che stampa l'uscita di un job da un centro
	 * 
	 * @param jobEseguito
	 *            job attivo
	 * @param centro
	 *            centro d'uscita
	 * @param t
	 *            il clock della simulazione
	 */
	public void scrivi(Job jobEseguito, Processo centro, SimTime t) {
		scrivi("{" + tempo(t) + "} Il " + jobEseguito.getNome() + " esce da <"
				+ centro.getNome() + ">.");
	}

	/**
	 * Metodo che stampa gli eventi di servizio di job che un centro preleva
	 * dalla coda
	 * 
	 * @param e
	 *            il centro che serve il job
	 * @param t
	 *            il clock della simulazione
	 */
	public void scrivi(Processo e, SimTime t) {
		scrivi("{" + tempo(t) + "} <" + e.getNome() + "> preleva il "
				+ e.getNome() + " dalla sua coda e lo serve" + " fino a {"
				+ tronca(e.getTime()) + "}.");
	}

	/**
	 * Metodo che stampa gli accodamenti
	 * 
	 * @param jobEseguito
	 *            il job attivo
	 * @param prov
	 *            il centro di provenienza
	 * @param centro
	 *            il centro di destinazione
	 * @param t
	 *            il clock della simulazione
	 */
	public void scrivi(Job jobEseguito, Processo prov, Processo centro,
			SimTime t) {

		scrivi("{" + tempo(t) + "} <" + centro.getNome() + "> occupato. Il "
				+ jobEseguito.getNome() + " da <" + prov.getNome()
				+ "> viene messo in coda <" + centro.getNome() + ">.");
	}

	/**
	 * Metodo che stampa l'uscita di un job dal sistema.
	 * 
	 * @param jobEseguito
	 *            il job attivo
	 * @param t
	 *            il clock della simulazione
	 */
	public void scrivi(Job jobEseguito, SimTime t) {
		scrivi("{" + tempo(t) + "} Il " + jobEseguito.getNome()
				+ " esce dal sistema" + " e torna da "
				+ jobEseguito.getGeneratoDa().getNome() + ". Risposta = "
				+ tronca(tempo(t) - jobEseguito.getIngresso()));
	}

	/**
	 * Metodo che scrive la media del throughput
	 * 
	 * @param tempoInizio
	 *            clock iniziale dell'osservazione
	 * @param tempoFine
	 *            clock finale dell'osservazione
	 * @param mediaOsservazione
	 *            media del throughput
	 */
	public void scrivi(double tempoInizio, double tempoFine,
			double mediaOsservazione) {
		scrivi("{" + tronca(tempoFine) + "} La media del throughput tra {"
				+ tronca(tempoInizio) + "} e {" + tronca(tempoFine) + "} e' <"
				+ tronca(mediaOsservazione) + ">.");
	}
}