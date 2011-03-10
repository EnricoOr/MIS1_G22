package simulatore;

import centri.*;
import eventi.Calendario;
import eventi.Evento;
import eventi.EventoArrivoUtente;
import eventi.EventoCpu;

/**
 * La classe si occupa di istanziare tutte le componenti del nostro impianto e di avviare la simulazione
 * @author Valerio Gentile
 * @author Andrea Giancarli
 * @author Alessandro Mastracci
 */

public class Simulatore {

	private static int nClient = 120;
	private Cpu cpu;
	private Disk disk;
	private Host[] host;
	private Stampante[] stampanti;
	private Terminali[] client;
	private ReportOsservazione osservazione;
	private final int run = 50;
	private final int oss = 6000;
	private Calendario calendario;
	private static Log log;
	
	private static boolean stab = false;
	private boolean logging = false;
	private static String logAcc = "0123456789";
	
	/*
	 * logAcc decide l'accuratezza del file di log
	 * 1 cambio centro
	 * 2 job uscito da centro
	 * 3 preleva coda
	 * 4 accodamento
	 * 5 nuovo job
	 * 6 uscita job
	 * 7 numero cicli
	 * 8 cambio classe
	 * 9 centro libero
	 * 0 TMR
	 */
	
	
	public Simulatore(int nClient, boolean stab, boolean log, String logAcc)
	{
		Simulatore.nClient = nClient;
		EventoCpu.resetS();
		Simulatore.stab = stab;
		this.logging = log;
		Simulatore.logAcc = logAcc;
	}
	
	/**
	 * Questa funzione avvia la simulazione sia per la stabilizzazione
	 * che per l'analisi dei risultati
	 */
	
	public void avvia()
	{
		calendario = new Calendario(this);
		
		log = new Log(1, logging, calendario);

		creaCentri();
		
		if(stab) osservazione = new ReportOsservazione(calendario, run, oss);
		else osservazione = new ReportOsservazione(calendario, run);
		creaJob();
		
		
		System.out.println("***Inizio Simulazione " + nClient +" client ***");
		boolean continua = true;
		while (continua) {
			Evento ev = calendario.prossimoEvento();
			continua = ev.esecuzioneEvento(calendario);
		}
		System.out.println("***Fine Simulazione " + nClient +" client ***\n");
		if(logging) log.close();
	}
	
	/**
	 * Funzione che crea i vari centri dell'impianto
	 */
	
	public void creaCentri()
	{
		creaTerminali();
		creaHost();
		creaStampanti();
		cpu = new Cpu();
		disk = new Disk();
	}
	
	/**
	 * Questa funzione crea i client
	 */
	
	public void creaTerminali()
	{
		client = new Terminali[nClient];
		for(int t=0; t<nClient; t++)
		{
			client[t] = new Terminali();
		}
	}
	
	/**
	 * Questa funzione crea gli host
	 */
	
	public void creaHost()
	{
		host = new Host[nClient];
		for(int t=0; t<nClient; t++)
		{
			host[t] = new Host();
		}
	}
	
	/**
	 * Questa funzione crea le stampanti
	 */
	
	public void creaStampanti()
	{
		stampanti = new Stampante[nClient];
		for(int t=0; t<nClient; t++)
		{
			stampanti[t] = new Stampante();
		}
	}
		
	/**
	 * Questa funzione crea un job per ogni client
	 */
	
	public void creaJob()
	{
		for(int t=0; t<nClient; t++)
		{
			client[t].nextJob();
			EventoArrivoUtente e = new EventoArrivoUtente("evento arrivo utente job: " + client[t].getJob().getId(), Calendario.getClock() + client[t].getTempoCentro(), client[t].getJob(), client[t]);
			
			if(Simulatore.logAcc("5")) log.scrivi(e);
			
			calendario.aggiungiEvento(e);
		}
	}
	
	/**
	 * Questa funzione restituisce true se è attiva la modalita di stabilizzazione
	 * @return stab
	 */
	
	public static boolean stab()
	{
		return stab;
	}
	
	/**
	 * Questa funzione serve per filtrare il dettaglio del log
	 */
	
	public static boolean logAcc(String c)
	{
		if(logAcc.contains(c)) return true;
		else return false;
	}
	
	/**
	 * Questa funzione restituisce la CPU del sistema
	 * @return cpu
	 */
	
	public Cpu getCpu()
	{
		return cpu;
	}
	
	/**
	 * Questa funzione restituisce il Disk del sistema
	 * @return disk
	 */
	
	public Disk getDisk()
	{
		return disk;
	}
	
	/**
	 * Questa funzione restituisce il vettore con gli Host del sistema
	 * @return host
	 */
	
	public Host[] getHost()
	{
		return host;
	}
	
	/**
	 * Questa funzione restituisce il vettore con le stampanti del sistema
	 * @return stampanti
	 */
	
	public Stampante[] getStampante()
	{
		return stampanti;
	}
	
	/**
	 * Questa funzione restituisce il report delle osservazioni della simulazione
	 * @return osservazione
	 */
	
	public ReportOsservazione getReportOsservazione()
	{
		return osservazione;
	}
	
	/**
	 * Questa funzione restituisce il vettore con gli Host del sistema
	 * @return log
	 */
	
	public Log getLog()
	{
		return log;
	}
	
	/**
	 * Questa funzione restituisce il numero dei client della simulazione in esecuzione
	 * @return nClient
	 */
	
	public static int getNClient()
	{
		return nClient;
	}
}
