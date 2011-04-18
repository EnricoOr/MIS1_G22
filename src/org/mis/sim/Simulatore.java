package org.mis.sim;

import java.util.ArrayList;
import java.util.PriorityQueue;

import org.mis.gen.Random;
import org.mis.gen.Seme;
import org.mis.processi.*;

/**
 * La classe si occupa di istanziare tutte le componenti del nostro impianto e di avviare la simulazione
 * @author Daniele Battista
 * @author Luca Dell'Anna
 * @author Enrico Orsini
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
	private boolean b2 = false;
	private boolean logging = false;
	private boolean observing = false;
	private boolean trObserved =false;
	
	
	/**
	 * Costruttore della classe. Utilizzato nella fase di stabilizzazione.
	 * @param nClient il numero di client da usare per la simulazione
	 * @param logi se true, crea i files di log
	 * @param n numero di osservazioni
	 */
	public Simulatore(int nClient, boolean logi, int n)
	{
		Simulatore.nClient = nClient;
		Simulatore.stab = true;
		this.logging = logi;
		tau=5;
		this.nOsser=n;
		log = new Log((int)System.currentTimeMillis(), logging);
	}
	
	/**
	 * Costruttore della classe. Utilizzato nella fase di analisi.
	 * @param nClient il numero di client da usare per la simulazione
	 * @param logi se true, crea i files di log
	 * @param n lunghezza del batch
	 * @param t tau (durata osservazione)
	 * @param b2 se true esegue l'analisi del tempo di risposta Disk 
	 */
	public Simulatore(int nClient, boolean logi, int n, double t, boolean b2)
	{
		Simulatore.nClient = nClient;
		this.logging = logi;
		tau=t;
		this.b2=b2;
		this.nOsser=n;
		this.nOssAn=50;
		log = new Log((int)System.currentTimeMillis(), logging);		
	}
	
	/**
	 * Metodo che inizializza i vari processi e variabili della simulazione
	 */
	public final void simInit(){
		
		passivate = new ArrayList<Processo>();
		hold = new PriorityQueue<Processo>();
		jobSis = new Job[nClient];
		creaCentri();		
	}
	
	/**
	 * Metodo che avvia la simulazione sia per la stabilizzazione
	 * che per l'analisi dei risultati
	 */
	public final void avvia()
	{
		boolean stop=false;
		
		clock = new SimTime();
		end = new FineSim();
		if(stab) osservazione = new Osservazione(nOsser, tau);
		else if (b2) osservazione = new Osservazione(1000, nClient, tau);
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
		else if (b2){
			this.end.hold((nOsser*tau*1001)+0.001);
			this.nOssAn=1000;
		}
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
				if (cpu.codaVuota())
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
				if (nClient == 20 && !trObserved && clock.getSimTime()>=(tau*(nOsser+((nOsser-1)/2.0)))) 
				{
					osservazione.jobtoDisk();
					osservazione.aggTempoR(clock.getSimTime() - workingJob.getIngresso());
					workingJob.setIngresso(0);
					trObserved=true;
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
					if(observing) trObserved=false;
					
				}
				break;
				
			case FineSimulazione:
				clock.setSimTime(end.getTime());
				clock.stopSimTime();
				
				if(!stab && !b2)
				{
					System.out.println("***Fine Simulazione " + nClient +" client ***\n");
					//System.out.println("****Media: "+osservazione.getMedia()+"\n");
					//System.out.println("****Varianza: "+osservazione.getVarianza()+"\n");
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
	 * Metodo che crea i vari centri dell'impianto
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
	 * Metodo che crea i client
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
	 * Metodo che crea gli host
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
	 * Metodo che crea le stampanti
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
	 * Metodo che crea un job per ogni client
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
	 * Metodo che restituisce il report delle osservazioni della simulazione
	 * @return osservazione il report delle osservazioni della simulazione
	 */
	
	public final Osservazione getOsservazioni()
	{
		return osservazione;
	}
	
	/**
	 * Metodo che serve per resettare lo stato del sistema ad ogni fine run
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
	 * Metodo per verificare se è attiva la modalita di stabilizzazione
	 * @return stab true se è attiva la modalita di stabilizzazione
	 */
	public static boolean stab()
	{
		return stab;
	}
	
	/**
	 * Metodo che restituisce il numero dei client della simulazione in esecuzione
	 * @return nClient numero dei client della simulazione
	 */	
	public static int getNClient()
	{
		return nClient;
	}
}
