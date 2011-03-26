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

	private static Log log;
	private double tau;
	private int nOsser;
	
	public Vector<Processo> hold;
	public Vector<Processo> passivate;
	public Vector<Job> jobSis;
	
	private static boolean stab = false;
	private boolean logging = false;
	
	
	public Simulatore(int nClient, boolean stab, boolean log, int n)
	{
		Simulatore.nClient = nClient;
		Simulatore.stab = stab;
		this.logging = log;
		this.tau=120;
		this.nOsser=n;
		
	}
	
	public Simulatore(int nClient, boolean stab, boolean log, int n, double tau)
	{
		Simulatore.nClient = nClient;
		Simulatore.stab = stab;
		this.logging = log;
		this.tau=tau;
		this.nOsser=n;
		
	}
	
	/**
	 * Questa funzione avvia la simulazione sia per la stabilizzazione
	 * che per l'analisi dei risultati
	 */
	
	public Osservazione avvia()
	{
		boolean stop=false;
		passivate = new Vector<Processo>();
		hold = new Vector<Processo>();
		jobSis = new Vector<Job>();

		log = new Log((int)System.currentTimeMillis(), logging);

		creaCentri();
		clock = new SimTime();
		end = new FineSim();
		if(stab) osservazione = new Osservazione(nOsser);
		else osservazione = new Osservazione(nOsser, nClient);
		creaJob();
		
		System.out.println("***Inizio Simulazione " + nClient +" client ***");
		//aggiungo il processo fine simulazione alla lista degli oggetti hold
		this.osservazione.hold(tau);
		this.end.hold(nOsser*tau);
		this.hold.add(osservazione);
		this.hold.add(end);
		Collections.sort(hold);
		log.print_h(hold);
		
		//inizio ciclo di simulazione
		while (!stop){
			//estraggo sempre l'oggetto in testa alla lista ordinata di hold
			Processo curr = this.hold.remove(0);
			
			//inizio processo centro terminale
			if(curr.getNome().equals("Terminale")){
				log.scrivi("TERMINALE TERMINA HOLD");
				Terminale term = (Terminale) curr;
				clock.add(term.getdT());
				

				Job j = term.nextJob();
				jobSis.add(j);
				log.scrivi("Job generato dal terminale: "+term.getId());
				term.passivate();
				this.passivate.add(term);
				log.print_p(passivate);
				
				//se la cpu è passiva il job l'attiva altrimenti si mette in coda
				if (cpu.getStato()==Stato.PASSIVO){
					this.passivate.remove(cpu);
					cpu.activate();
					log.scrivi("CPU ATTIVATA E RIMOSSA DA CODA PASSIVA");
					double time = clock.getSimTime()+cpu.getTempoCentro(jobSis.lastElement());
					cpu.hold(time);
					log.scrivi("hold della cpu per t="+time);
					this.hold.add(cpu);
					Collections.sort(hold);
					log.print_h(hold);
				}
				else {
					cpu.push(j);
					log.scrivi(j, term, cpu, clock);	//salva l'accodamento
					
				}
					
			}//fine processo centro terminale
			
			//inizio processo centro cpu
			if (curr.getNome().equals("Cpu")){
				log.scrivi("CPU TERMINA HOLD");
				clock.add(cpu.getTempoCentro());
				
				Job j = cpu.getJobCorrente();
				
				
				//da classe 1 il job cambia classe 2 con p=0.3, 3 con p=0.7
				if (j.getJobClass()==1){
					
					double n = rand.nextNumber();
					if (n<=0.3){ 
						j.setJobClass(2);
						log.scrivi( j, 2, clock);				//salva il cambio di classe
						cpu.push(j);
						log.scrivi(j, cpu, cpu, clock);			//salva l'accodamento
					}
					else {
						j.setJobClass(3);
						log.scrivi( j, 3, clock);				//salva il cambio di classe
						cpu.push(j);
						log.scrivi(j, cpu, cpu, clock);			//salva l'accodamento
					}
					
				}
				else if (j.getJobClass()==2){
					
					osservazione.jobtoHost();
					Host currH = null;
					log.scrivi(j, cpu, clock);					//stampa l'uscita dal centro di cpu
					//seleziono il primo host passivo e lo attivo
					for(int t=0; t<nClient; t++)
					{
						if (host[t].getStato()==Stato.PASSIVO){
							currH=host[t];
							break;
						}
					}
					this.passivate.remove(currH);
					currH.activate();
					log.scrivi("HOST ATTIVATO E RIMOSSA DA CODA PASSIVA");
					currH.setCurJob(j);
					
					double time = clock.getSimTime()+currH.getTempoCentro();
					currH.hold(time);
					this.hold.add(currH);
					Collections.sort(hold);
					log.print_h(hold);
				}
				else if (j.getJobClass()==3){
					log.scrivi(j, cpu, clock);					//stampa l'uscita dal centro di cpu
					//il job di classe tre va con p=0.1 alla stampante e con p=0.9 al disk
					double n = rand.nextNumber();
					if (n>0.1){
					
					//se il disk è passivo il job l'attiva altrimenti si mette in coda
					if (disk.getStato()==Stato.PASSIVO){
						this.passivate.remove(disk);
						disk.activate();
						log.scrivi("DISK ATTIVATO E RIMOSSO DA CODA PASSIVA");
						
						double time = clock.getSimTime()+disk.getTempoCentro();
						disk.hold(time);
						this.hold.add(disk);
						Collections.sort(hold);
						
					}
					else{
						disk.push(j);
						log.scrivi(j, cpu, disk, clock);		//stampa l'accdamento nella coda del disk
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
						this.passivate.remove(currP);
						currP.activate();
						log.scrivi("STAMPANTE ATTIVATA E RIMOSSA DA CODA PASSIVA");
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
					this.passivate.add(cpu);
				}
				else{
					Job next = cpu.pop();
					log.scrivi(cpu, clock); 		//stampa estrazione job dalla coda
					double time = clock.getSimTime()+cpu.getTempoCentro(next);
					cpu.hold(time);
					this.hold.add(cpu);
					Collections.sort(hold);
				}
				
			}//fine processo centro cpu
			
			//inizio processo centro disk
			if (curr.getNome().equals("Disk")){
				clock.add(disk.getdT());
				log.scrivi("DISK TERMINA HOLD");

				Job j= disk.getJobCorrente();
				j.setJobClass(3);
				
				//se la cpu è passiva il job l'attiva altrimenti si mette in coda
				if (cpu.getStato()==Stato.PASSIVO){
					this.passivate.remove(cpu);
					cpu.activate();
					log.scrivi("CPU ATTIVATA E RIMOSSA DA CODA PASSIVA");
					double time = clock.getSimTime()+cpu.getTempoCentro(j);
					cpu.hold(time);
					this.hold.add(cpu);
					Collections.sort(hold);
				}
				else {
					cpu.push(j);
					log.scrivi(j, disk, cpu, clock);	//salva l'accodamento
				}
				
				//se la coda del disk è vuota l'oggetto si passiva
				if(disk.getCodaSize()==0){
					disk.passivate();
					this.passivate.add(disk);
				}
				else{
					disk.pop();
					log.scrivi(j, clock);			//salva estrazione del job dalla coda del disk
					double time = clock.getSimTime()+disk.getTempoCentro();
					disk.hold(time);
					this.hold.add(disk);
					Collections.sort(hold);
				}
				log.scrivi(j, disk, clock);			//salva l'uscita del job dal centro disk
			}//fine processo centro disk
			
			//inizio processo centro host
			if (curr.getNome().equals("Host")){
				Host ht = (Host) curr;
				clock.setSimTime(ht.getTime().doubleValue());
				log.scrivi("HOST TERMINA HOLD");
				
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
				log.scrivi(j, ht, clock);			//stampa l'uscita del job dal centro host
				
			}//fine processo centro host

			//inizio processo centro stampante
			if (curr.getNome().equals("Stampante")){
				Printer pt = (Printer) curr;
				clock.setSimTime(pt.getTime().doubleValue());
				log.scrivi("STAMPANTE TERMINA HOLD");
				
				Job j = pt.getJobCorrente();
				
				j.setJobClass(1);
				pt.passivate();
				this.passivate.add(pt);
				jobSis.remove(j); //job termina il suo ciclo
				osservazione.jobCompletato();
				
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
			log.scrivi(j, pt, clock);			//stampa l'uscita del job dalla stampante
			osservazione.jobCompletato();
			log.scrivi(j, clock);				//stampa l'uscita del job dal sistema
			}//fine processo centro stampante
			
			
			
			//inizio processo osservazione della simulazone
			if (curr.getNome().equals("osservazione")){
				
				clock.setSimTime(osservazione.getTime().doubleValue());
				log.scrivi("**OSSERVAZIONE....");
			
				if (stab) osservazione.aggOssStab();
				else osservazione.aggOss();
				
				nOsser--;
				if(nOsser!=0){
					osservazione.hold(clock.getSimTime()+tau);
					this.hold.add(osservazione);
					Collections.sort(hold);
					log.print_h(hold);
				}
				
				
			}//fine processo osservazione della simulazione
			
			//inizio processo fine simulazione
			if (curr.getNome().equals("Fine Simulazione")){
				
				clock.setSimTime(end.getTime().doubleValue());
				clock.stopSimTime();
				
				System.out.println("***Fine Simulazione " + nClient +" client ***\n");
				System.out.println("****Media: "+osservazione.getMedia()+"\n");
				System.out.println("****Varianza: "+osservazione.getVarianza()+"\n");
				log.scrivi(clock);				//stampa la fine della simulazione
				log.scrivi("***Media: "+osservazione.getMedia()+"***\n"+"***Varianza: "+osservazione.getVarianza()+"***\n");

				if(logging) log.close();
				stop=true;
				
			}//fine processo fine simulazione
		}
		
		return osservazione;

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
			
			
			log.scrivi("Client "+t+" in hold");

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
