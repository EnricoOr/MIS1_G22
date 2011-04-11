package org.mis.sim;

import java.util.ArrayList;
import java.util.PriorityQueue;

import org.mis.gen.Random;
import org.mis.gen.Seme;
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

	private static int nClient;
	private Cpu cpu;
	private Disk disk;
	private Host[] host;
	private Printer[] stampanti;
	private Terminale[] client;
	private Osservazione osservazione;
	private SimTime clock;
	private FineSim end;
	private Random rand = new Random(Seme.getSeme());

	private static Log log;
	private static double tau;
	private int nOsser;
	private int nOssAn;
	private Job[] jobSis;
	
	public PriorityQueue<Processo> hold;
	public ArrayList<Processo> passivate;
	
	private static boolean stab = false;
	private boolean logging = false;
	private boolean observing = false;
	
	public Simulatore(int nClient, boolean stab, boolean logi, int n)
	{
		Simulatore.nClient = nClient;
		Simulatore.stab = stab;
		this.logging = logi;
		tau=5;
		this.nOsser=n;
		log = new Log((int)System.currentTimeMillis(), logging);
	}
	
	public Simulatore(int nClient, boolean stab, boolean logi, int n, double t)
	{
		Simulatore.nClient = nClient;
		Simulatore.stab = stab;
		this.logging = logi;
		tau=t;
		this.nOsser=n;
		this.nOssAn=50;
		log = new Log((int)System.currentTimeMillis(), logging);
		
	}
	
	/**
	 * Questo metodo inizializza i vari processi e variabii della simulazione
	 */
	public final void simInit(){
		
		passivate = new ArrayList<Processo>();
		hold = new PriorityQueue<Processo>();
		jobSis = new Job[nClient];
		creaCentri();
		
	}
	
	/**
	 * Questa funzione avvia la simulazione sia per la stabilizzazione
	 * che per l'analisi dei risultati
	 */
	
	public final void avvia()
	{
		boolean stop=false;
		
		clock = new SimTime();
		end = new FineSim();
		if(stab) osservazione = new Osservazione(nOsser, tau);
		else osservazione = new Osservazione(50, nClient, tau);
		creaJob();
		
		if(!stab) System.out.println("***Inizio Simulazione " + nClient +" client ***");
		//aggiungo il processo fine simulazione alla lista degli oggetti hold
		if (stab)
			this.osservazione.hold(tau);
		else
			this.osservazione.hold(tau*(nOsser+((nOsser-1)/2.0)));
		if (stab) 
			this.end.hold(nOsser*tau+0.001);
		else
			this.end.hold((nOsser*tau*51)+0.001);
		this.hold.add(osservazione);
		this.hold.add(end);
//		log.print_h(hold);
		
		//inizio ciclo di simulazione
		while (!stop)
		{
			//estraggo sempre l'oggetto in testa alla lista ordinata di hold
			Processo curr = this.hold.poll();
			double time;
		
			switch(curr.getTipo())
			{
			case Terminale:
				log.scrivi("TERMINALE TERMINA HOLD");
				Terminale term = (Terminale) curr;
//				clock.add(term.getdT());		
				clock.setSimTime(term.getTime());

				Job jt = term.nextJob();
				jobSis[term.getId()] = jt;

				log.scrivi("Job generato dal terminale: "+term.getId());
				term.passivate();
				this.passivate.add(term);
//				log.print_p(passivate);
				
				//se la cpu è passiva il job l'attiva altrimenti si mette in coda
				if (cpu.getStato()==Stato.PASSIVO)
				{
					this.passivate.remove(cpu);
					cpu.activate();
					log.scrivi("CPU ATTIVATA E RIMOSSA DA CODA PASSIVA");
					time = clock.getSimTime()+cpu.getTempoCentro(jobSis[term.getId()]);
					cpu.hold(time);
					log.scrivi("hold della cpu per t="+time);
					this.hold.add(cpu);
//					log.print_h(hold);
				}
				else 
				{
					cpu.push(jt);
					log.scrivi(jt, term, cpu, clock);	//salva l'accodamento
				}
				break;

			case CPU:
				log.scrivi("CPU TERMINA HOLD");
//				clock.add(cpu.getTempoCentro());
				clock.setSimTime(cpu.getTime());
				
				Job jc = cpu.getJobCorrente();
				
				//da classe 1 il job cambia classe
				switch(jc.getJobClass())
				{
				case 1:
					if (rand.nextNumber()<=0.3)
					{ 
						jc.setJobClass(2);
						log.scrivi(jc, 2, clock);				//salva il cambio di classe
						cpu.push(jc);
						this.osservazione.jobtoHost();
						log.scrivi(jc, cpu, cpu, clock);			//salva l'accodamento
					}
					else 
					{
						jc.setJobClass(3);
						log.scrivi(jc, 3, clock);				//salva il cambio di classe
						cpu.push(jc);
						log.scrivi(jc, cpu, cpu, clock);			//salva l'accodamento
					}
					break;
					
				case 2:
					Host currH = null;
					log.scrivi(jc, cpu, clock);					//stampa l'uscita dal centro di cpu
					
					currH=host[jc.getGeneratoDa().getId()];
						
					this.passivate.remove(currH);
					currH.activate();
					log.scrivi("HOST ATTIVATO E RIMOSSA DA CODA PASSIVA");
					currH.setCurJob(jc);
					
					time = clock.getSimTime()+currH.getTempoCentro();
					currH.hold(time);
					this.hold.add(currH);						
					//log.print_h(hold);
					break;
					
				case 3:
					log.scrivi(jc, cpu, clock);					//stampa l'uscita dal centro di cpu
					
					if (rand.nextNumber()>0.1)
					{
						//se il disk è passivo il job l'attiva altrimenti si mette in coda
						if (disk.getStato()==Stato.PASSIVO)
						{
							this.passivate.remove(disk);
							if (nClient==20) jc.setIngresso(clock.getSimTime());
							disk.activate(jc);
							log.scrivi("DISK ATTIVATO E RIMOSSO DA CODA PASSIVA");
						
							time = clock.getSimTime()+disk.getTempoCentro();
							disk.hold(time);
							this.hold.add(disk);
						}
						else
						{
							if (nClient==20) jc.setIngresso(clock.getSimTime());
							disk.push(jc);
							log.scrivi(jc, cpu, disk, clock);		//stampa l'accodamento nella coda del disk
						}
					}
					else
					{
						Printer currP = null;
						currP=stampanti[jc.getGeneratoDa().getId()];

						this.passivate.remove(currP);
						currP.activate();
						log.scrivi("STAMPANTE ATTIVATA E RIMOSSA DA CODA PASSIVA");
						currP.setCurJob(jc);
						
						time = clock.getSimTime()+currP.getTempoCentro();
						currP.hold(time);
						this.hold.add(currP);
					}
					break;
				}
				
				//se la coda della cpu è vuota l'oggetto si passiva
				if (cpu.codeVuote())
				{
					cpu.passivate();
					this.passivate.add(cpu);
				}
				else
				{
					Job next = cpu.pop();
					log.scrivi(cpu, clock); 		//stampa estrazione job dalla coda
					time = clock.getSimTime()+cpu.getTempoCentro(next);
					cpu.hold(time);
					this.hold.add(cpu);
				}
				break;
				
			case Disk:
//				clock.add(disk.getdT());
				clock.setSimTime(disk.getTime());
				log.scrivi("DISK TERMINA HOLD");

				Job workingJob = disk.getJobCorrente();
				if (observing && nClient == 20) 
				{
					osservazione.jobtoDisk();
					osservazione.aggTempoR(clock.getSimTime() - workingJob.getIngresso());
					workingJob.setIngresso(0);
				}
				
				workingJob.setJobClass(3);
				
				//se la cpu è passiva il job l'attiva altrimenti si mette in coda
				if (cpu.getStato()==Stato.PASSIVO)
				{
					this.passivate.remove(cpu);
					cpu.activate();
					log.scrivi("CPU ATTIVATA E RIMOSSA DA CODA PASSIVA");
					time = clock.getSimTime()+cpu.getTempoCentro(workingJob);
					cpu.hold(time);
					this.hold.add(cpu);
				}
				else 
				{
					cpu.push(workingJob);
					log.scrivi(workingJob, disk, cpu, clock);	//salva l'accodamento
				}
				
				//se la coda del disk è vuota l'oggetto si passiva
				if(disk.getCodaVuota())
				{
					disk.passivate();
					this.passivate.add(disk);
				}
				else
				{
					disk.pop();
//					log.scrivi(workingJob, clock);			//salva estrazione del job dalla coda del disk

					time = clock.getSimTime()+disk.getTempoCentro();
					disk.hold(time);
					this.hold.add(disk);
				}
				log.scrivi(workingJob, disk, clock);			//salva l'uscita del job dal centro disk				
				break;
			
			case Host:
				Host ht = (Host) curr;
				clock.setSimTime(ht.getTime());
				log.scrivi("HOST TERMINA HOLD");
				ht.passivate();
				this.passivate.add(ht);
				
				Job hostJob = ht.getJobCorrente();
				
				Printer currP = null;
				currP=stampanti[hostJob.getGeneratoDa().getId()];
					
				this.passivate.remove(currP);
				currP.activate();
				currP.setCurJob(hostJob);
				time = clock.getSimTime()+currP.getTempoCentro();
				currP.hold(time);
				this.hold.add(currP);
				log.scrivi(hostJob, ht, clock);			//stampa l'uscita del job dal centro host
				break;				
				
			case Stampante:
				Printer pt = (Printer) curr;
				clock.setSimTime(pt.getTime());
				log.scrivi("STAMPANTE TERMINA HOLD");
				
				Job printJob = pt.getJobCorrente();
				
				printJob.setJobClass(1);
				pt.passivate();
				this.passivate.add(pt);
				printJob.setStampa(false);//job termina il suo ciclo
				
				log.scrivi(printJob, pt, clock);			//stampa l'uscita del job dalla stampante
				
				//genero un nuovo job dal terminale passivo da cui quello terminato era stato generato.
				this.passivate.remove(printJob.getGeneratoDa());
				printJob.getGeneratoDa().activate();
				time = clock.getSimTime()+printJob.getGeneratoDa().getTempoCentro();
				printJob.getGeneratoDa().hold(time);
				this.hold.add(printJob.getGeneratoDa());

				log.scrivi(printJob, clock);				//stampa l'uscita del job dal sistema
				break;
								
			case Osservazione:
				clock.setSimTime(osservazione.getTime());
				log.scrivi("**OSSERVAZIONE....");
			
				if (stab)
					osservazione.aggOssStab();
				else if (observing)
					osservazione.aggOss();
				
				log.scrivi("****Thruoghput Host corr = "+ osservazione.getThrHst());
				
				if (stab) nOsser--;
				else if (observing)
					nOssAn--;
				
				if(nOsser!=0 && stab)
				{
					osservazione.hold(clock.getSimTime()+tau);
					this.hold.add(osservazione);
				}
				else if (nOssAn!=0 && !stab){
					osservazione.resetOss();
					osservazione.hold((observing ? clock.getSimTime()+(nOsser-1)*tau : clock.getSimTime()+tau));
					this.hold.add(osservazione);
					observing = !observing;
				}
				break;
				
			case FineSimulazione:
				clock.setSimTime(end.getTime());
				clock.stopSimTime();
				
				if(!stab)
				{
					System.out.println("***Fine Simulazione " + nClient +" client ***\n");
					System.out.println("****Media: "+osservazione.getMedia()+"\n");
					System.out.println("****Varianza: "+osservazione.getVarianza()+"\n");
					log.scrivi(clock);				//stampa la fine della simulazione
					log.scrivi("***Media: "+osservazione.getMedia()+"***\n"+"***Varianza: "+osservazione.getVarianza()+"***\n");
					log.scrivi("****Media tempo Risp Disk = "+ osservazione.getMediaTr());
				}

				if(logging) log.close();
				stop = true;
				break;
			}
			//log.scrivi("CODE: " + cpu.getLenCode() + " - Coda DISK = " + disk.getCodaSize());
		}
	}
	
	/**
	 * Funzione che crea i vari centri dell'impianto
	 */
	
	public final void creaCentri()
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
	
	public final void creaTerminali()
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
	
	public final void creaHost()
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
	
	public final void creaStampanti()
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
	
	public final void creaJob()
	{
		for(int t=0; t<nClient; t++)
		{
			this.passivate.remove(client[t]);
			client[t].activate();
			double time = clock.getSimTime()+client[t].getTempoCentro();
			client[t].hold(time);
			this.hold.add(client[t]);			
			
			log.scrivi("Client "+t+" in hold");

		}
	}
		
	/**
	 * Questa funzione restituisce il report delle osservazioni della simulazione
	 * @return osservazione
	 */
	
	public final Osservazione getOsservazioni()
	{
		return osservazione;
	}
	
	/**
	 * Questo metodo serve per resettare lo stato del sistema ad ogni fine run
	 * durante la stabilizzazione
	 */
	
	public final void resetSim()
	{

		this.hold.clear();
		this.passivate.clear();
		this.creaCentri();
		for(int t=0; t<nClient; t++)
		{
			jobSis[t]=null;
			
		}
		this.nOsser=this.osservazione.nOss;
		log = new Log((int)System.currentTimeMillis(), logging);
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
	 * Questa funzione restituisce il numero dei client della simulazione in esecuzione
	 * @return nClient
	 */
	
	public static int getNClient()
	{
		return nClient;
	}
}
