package org.mis.sim;

import java.util.Collections;
import java.util.Vector;

import org.mis.gen.Random;
import org.mis.processi.*;

/**
 * genero il tempo
aggiungo il tempo al simtime add
chiamo hold(getSimTime)
aggiungo alla lista di hold

 * La classe si occupa di istanziare tutte le componenti del nostro impianto e di avviare la simulazione
 * @author 
 * @author 
 * @author 
 */

public class Simulatore {

	private static int nClient = 10;
	private Cpu cpu;
	private Disk disk;
	private Host[] host;
	private Printer[] stampanti;
	private Terminale[] client;
	private Osservazione osservazione;
	private SimTime clock;
	private FineSim end;
	private Random rand;
	private final int run = 50;
	private final int oss = 6000;
	private static Log log;
	
	public Vector<Processo> hold;
	public Vector<Processo> passivate;
	public Vector<Job> jobSis;
	
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
		boolean stop=false;
		passivate = new Vector<Processo>();
		hold = new Vector<Processo>();
		jobSis = new Vector<Job>();

		log = new Log(1, logging);

		creaCentri();
		clock = new SimTime();
		end = new FineSim();
		if(stab) osservazione = new Osservazione(run, oss);
		else osservazione = new Osservazione(run);
		creaJob();
		
		System.out.println("***Inizio Simulazione " + nClient +" client ***");
		//aggiungo il processo fine simulazione alla lista degli oggetti hold
		end.hold(60000);
		end.setState(Stato.HOLD);
		hold.add(end);
		Collections.sort(hold);
		
		//inizio ciclo di simulazione
		while (!stop){
			//estraggo sempre l'oggetto in testa alla lista ordinata di hold
			Processo curr = hold.firstElement();
			
			//inizio blocco centro terminale
			if(curr.getNome().equals("Terminale")){
				Terminale term = (Terminale) curr;
				clock.add(term.getdT());


				Job j = term.nextJob();
				jobSis.add(j.getId(), j);
				term.passivate();
				passivate.add(term);
				
				//se la cpu è passiva il job l'attiva altrimenti si mette in coda
				if (cpu.getStato()==Stato.PASSIVO){
					passivate.remove(cpu);
					cpu.activate();
					double time = clock.getSimTime()+cpu.getTempoCentro(jobSis.lastElement());
					cpu.hold(time);
					this.hold.add(cpu);
					Collections.sort(hold);
				}
				else {
					cpu.push(j);
				}
					
			}//fine blocco centro terminale
			
			//inizio blocco centro cpu
			if (curr.getNome().equals("Cpu")){
				clock.add(cpu.getTempoCentro());
				
				Job j = cpu.getJobCorrente();
				
				//da classe 1 il job cambia classe 2 con p=0.3, 3 con p=0.7
				if (j.getJobClass()==1){
					
					double n = rand.nextNumber();
					if (n<=0.3){ 
						j.setJobClass(2);
						cpu.push(j);
					}
					else {
						j.setJobClass(3);
						cpu.push(j);
					}
					
				}
				else if (j.getJobClass()==2){
					
					Host currH = null;
					//seleziono il primo host passivo e lo attivo
					for(int t=0; t<nClient; t++)
					{
						if (host[t].getStato()==Stato.PASSIVO){
							currH=host[t];
							break;
						}
					}
					passivate.remove(currH);
					currH.activate();
					currH.setCurJob(j);
					
					double time = clock.getSimTime()+currH.getTempoCentro();
					currH.hold(time);
					this.hold.add(currH);
					Collections.sort(hold);
					
				}
				else if (j.getJobClass()==3){
					//il job di classe tre va con p=0.1 alla stampante e con p=0.9 al disk
					double n = rand.nextNumber();
					if (n>0.1){
					
					//se il disk è passivo il job l'attiva altrimenti si mette in coda
					if (disk.getStato()==Stato.PASSIVO){
						passivate.remove(disk);
						disk.activate();
						
						double time = clock.getSimTime()+disk.getTempoCentro();
						disk.hold(time);
						this.hold.add(disk);
						Collections.sort(hold);
						
					}
					else{
						disk.push(j);
					}
					}
					else{
						Printer currP = null;
						//seleziona la prima stampante passiva e la attiva
						for(int t=0; t<nClient; t++)
						{
							if (stampanti[t].getStato()==Stato.PASSIVO){
								currP=stampanti[t];
								break;
							}
						}
						passivate.remove(currP);
						currP.activate();
						currP.setCurJob(j);
						
						double time = clock.getSimTime()+currP.getTempoCentro();
						currP.hold(time);
						this.hold.add(currP);
						Collections.sort(hold);
					}
				}
				
				//se la coda della cpu è vuota l'oggetto si passiva
				if (cpu.codeVuote()) {
					
					cpu.passivate();
					passivate.add(cpu);
				}
				else{
					Job next = cpu.pop();
					double time = clock.getSimTime()+cpu.getTempoCentro(next);
					cpu.hold(time);
					this.hold.add(cpu);
					Collections.sort(hold);
				}
				
			}//fine blocco centro cpu
			
			//inizio blocco centro disk
			if (curr.getNome().equals("Disk")){
				clock.add(disk.getdT());
				

				Job j= disk.getJobCorrente();
				j.setJobClass(3);
				cpu.push(j);
				
				//se la coda del disk è vuota l'oggetto si passiva
				if(disk.getCodaSize()==0){
					disk.passivate();
					this.passivate.add(disk);
				}
				else{
					disk.pop();
					double time = clock.getSimTime()+disk.getTempoCentro();
					disk.hold(time);
					this.hold.add(disk);
					Collections.sort(hold);
				}
				
			}//fine blocco centro disk
			
			//inizio blocco centro host
			if (curr.getNome().equals("Host")){
				Host ht = (Host) curr;
				clock.setSimTime(ht.getTime().doubleValue());
				
				Job j = ht.getJobCorrente();
				
				Printer currP = null;
				for(int t=0; t<nClient; t++)
				{
					if (stampanti[t].getStato()==Stato.PASSIVO){
						currP=stampanti[t];
						break;
					}
				}
				this.passivate.remove(currP);
				currP.activate();
				currP.setCurJob(j);
				double time = clock.getSimTime()+currP.getTempoCentro();
				currP.hold(time);
				this.hold.add(currP);
				Collections.sort(hold);
				
			}//fine blocco centro host

			//inizio blocco centro stampante
			if (curr.getNome().equals("Stampante")){
				Printer pt = (Printer) curr;
				clock.setSimTime(pt.getTime().doubleValue());
				
				Job j = pt.getJobCorrente();
				
				j.setJobClass(1);
				pt.passivate();
				passivate.add(pt);
				jobSis.remove(j); //job termina il suo ciclo
				
				//genero un nuovo job dal primo terminale passivo.
				for(int t=0; t<nClient; t++)
				{
					if (client[t].getStato()==Stato.PASSIVO){
						this.passivate.remove(client[t]);
						client[t].activate();
						double time = clock.getSimTime()+client[t].getTempoCentro();
						client[t].hold(time);
						this.hold.add(client[t]);
						Collections.sort(hold);
						break;
					}
				}
					
			}//fine blocco centro stampante
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
		this.passivate.add(cpu);
		disk = new Disk();
		this.passivate.add(disk);
	}
	
	/**
	 * Questa funzione crea i client
	 */
	
	public void creaTerminali()
	{
		client = new Terminale[nClient];
		for(int t=0; t<nClient; t++)
		{
			client[t] = new Terminale();
			this.passivate.add(client[t]);
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
			this.passivate.add(host[t]);
		}
	}
	
	/**
	 * Questa funzione crea le stampanti
	 */
	
	public void creaStampanti()
	{
		stampanti = new Printer[nClient];
		for(int t=0; t<nClient; t++)
		{
			stampanti[t] = new Printer();
			this.passivate.add(stampanti[t]);
		}
	}
		
	/**
	 * Questa funzione crea un job per ogni client
	 */
	
	public void creaJob()
	{
		for(int t=0; t<nClient; t++)
		{
			double time = clock.getSimTime()+client[t].getTempoCentro();
			client[t].hold(time);
			this.hold.add(client[t]);
			Collections.sort(hold);
			
			
			if(Simulatore.logAcc("5")) log.scrivi("Client "+t+" in hold");

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
	
	public Printer[] getStampante()
	{
		return stampanti;
	}
	
	/**
	 * Questa funzione restituisce il report delle osservazioni della simulazione
	 * @return osservazione
	 */
	
	public Osservazione getReportOsservazione()
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
