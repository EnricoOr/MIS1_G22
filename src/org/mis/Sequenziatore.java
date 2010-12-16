package org.mis;
//librerie per il log grafico
import java.awt.Component;
import javax.swing.JOptionPane;//*/

import org.mis.gen.*;

import java.io.*;
import java.util.*;

public class Sequenziatore {
	
    
	public Sequenziatore(){    
    }
    public Sequenziatore(int numeroJobAttivi,long tempoStabilizzazione,long seme, boolean insiemePassivo){
        this.TEMPOSTABILIZZAZIONE=tempoStabilizzazione;
        this.numeroJobAttivi=numeroJobAttivi;
        this.insiemePassivo=insiemePassivo;
        gu=new GeneratoreUniforme(seme);
        cpu = new Centro(new GeneratoreKerlangiano(3,0.028,this.gu.generaSeme()),new CodaFIFO());
        disk1= new Centro(new GeneratoreIperEsponenziale(0.6, 0.04, this.gu.generaSeme(), this.gu.generaSeme()),new CodaRAND(new GeneratoreUniforme(gu.generaSeme())));
        disk2 = new Centro(new GeneratoreIperEsponenziale(0.3,0.28,this.gu.generaSeme(),this.gu.generaSeme()),new CodaLIFO());
        sorgente=new GeneratorePoissoniano(50,this.gu.generaSeme()); 
        this.CalendarioEventi = new Evento[this.numeroJobAttivi+2]; //creo il calendario eventi 
        if (insiemePassivo){
        	this.tokenPool=new boolean[this.numeroJobAttivi]; //creo nuovo token pool per il caso dell'insieme passivo
        	for (int i=0; i<this.tokenPool.length; i++){
        		this.tokenPool[i]=true;
        	}
        }
        //polarizzazione iniziale

        this.TempoOsservazione=tempoStabilizzazione+1;
        if (insiemePassivo)
        	this.accendi((int)tempoStabilizzazione);
        else
        { 
        	this.accendiInsiemeAttivo((int)tempoStabilizzazione);
        	this.osservazioneTempidiCiclo.clear();
        	this.osserva=0;
        	this.osservazioneJobTerminati=0;
        }
        
        System.out.println("Sistema stabilizzato a " + this.TEMPO);
        this.CalendarioEventi[this.numeroJobAttivi+1]=new Osservazione(this, tempoStabilizzazione + tempoStabilizzazione/2);
        this.TempoOsservazione=tempoStabilizzazione;
        this.jobTerminati=0;
        this.jobAmmessi=0;
        this.tempoUltimoJob=0;
        this.throughputUltimoJob=0;

      
        //this.CalendarioEventi[this.numeroJobAttivi+1]=new Osservazione(this, TempoOsservazione);
        //stampa risultati su file
        /*file=new File("risultati/SecondoTest_Risultati_per_stabilizzare_" + numeroJobAttivi + "_job attivi_insiemechiuso.xls");
        try{
              fos = new FileOutputStream(file);
              printStream = new PrintStream(fos);
              printStream.println("simulazione per stabilizzazione "+numeroJobAttivi+" Job Attivi");
              printStream.println("osservazioni\ttempo di risposta\tvarianza");
        }catch(FileNotFoundException e){
              e.printStackTrace();
        } */       
    }
    public boolean isTokenPoolEmpty(){
        for (int i=0; i<numeroJobAttivi; i++)
        	if (this.tokenPool[i]==true)
        		return false;       
        return true;
    }
    public void generanuoviarrivi(int tempoPoisson){  //chiama la sorgente e svuota il tokenpool oppure riempe la codaesterna
    	int n=(int)sorgente.nextNumber();
    	int temp;
    	for (int i=0; i< n && this.codaJobEsterni.size()<20; i++){
    		Job nuovo= new Job(this.gu.nextNumber100_1000(), this.TEMPO);
    		this.codaJobEsterni.add(nuovo);
    	}
    	while (!isTokenPoolEmpty() && !this.codaJobEsterni.isempty()){
    		temp=decrementaTokenPool();
    		Job primoCoda= this.codaJobEsterni.successivo();
    		primoCoda.setRif(temp);
                primoCoda.setTempoIngresso(this.TEMPO);
    			CalendarioEventi[temp]=new Arrivo(primoCoda, this, this.TEMPO);
    	}
    }
    public int decrementaTokenPool(){ //restituisce la posizione del token rilasciato
    	for (int i=0; i<this.tokenPool.length; i++){
    		if (tokenPool[i]==true){
    			tokenPool[i]=false;
    			return i;
    		}
    	}
    	return -1;
    }
    public double getFineSimulazione(){
        return this.FINESIMULAZIONE;
    }
    public double getNumeroJobAttivi(){
        return this.numeroJobAttivi;
    }
    public void accendi(int secSim){
    	int nextev;
        this.FINESIMULAZIONE=secSim+1001;
        this.CalendarioEventi[numeroJobAttivi]=new FineSim(this, secSim);
        for(int i=0; i<secSim; i++){
        	this.generanuoviarrivi(i);
        	nextev=eventovicino(CalendarioEventi, i);
        	while (nextev<CalendarioEventi.length ){
        		this.TEMPO=CalendarioEventi[nextev].gettempo();
        		CalendarioEventi[nextev].evolvi();
        		/* Stampa log grafico
        		 if (TEMPO%(this.TEMPOSTABILIZZAZIONE/2) == 0){
        			String message="Numero cicli del Job: "+ this.CalendarioEventi[nextev] + "\nTEMPO: " + this.TEMPO +"\n Coda Esterna: " + this.codaJobEsterni.size() + "\nCoda CPU" + this.cpu.getCoda().size() + this.cpu.isbusy() + "\nCoda D1: " + this.disk1.getCoda().size()+this.disk1.isbusy() + "\nCoda D2: " + this.disk2.getCoda().size()+ this.disk2.isbusy() + "\nCalendarioEventi:" + Arrays.toString(this.CalendarioEventi) +"\n" + Arrays.toString(this.tokenPool); 
        			JOptionPane.showConfirmDialog((Component)null, message, "alert", JOptionPane.OK_CANCEL_OPTION);
        			//printStream.println(message);
        		}*/
        		nextev=eventovicino(CalendarioEventi, i);
        		}
        	}
    }
    public void accendiInsiemeAttivo(int secSim){
    	int nextev;
    	this.FINESIMULAZIONE=secSim+1001;
    	if (this.CalendarioEventi[0]==null){
    		Job nuovoJob;
    		for (int i=0; i<numeroJobAttivi; i++){ //se è il primo avvio inizializza un evento di arrivo per tutti i job
        		nuovoJob=new Job(0, this.TEMPO);
        		nuovoJob.setRif(i);
        		CalendarioEventi[i]=new Arrivo(nuovoJob, this, this.TEMPO);
        		}
    	}
    	this.CalendarioEventi[numeroJobAttivi]=new FineSim(this, secSim);
    	for(int i=0; i<secSim; i++){
    		nextev=eventovicino(CalendarioEventi, i);
    		while (nextev<CalendarioEventi.length ){
    			this.TEMPO=CalendarioEventi[nextev].gettempo();
    			CalendarioEventi[nextev].evolvi();
    			/*if (TEMPO >10000 && this.numeroJobAttivi>2){
        			String message="TEMPO: " + this.TEMPO +"\nPopolazione CPU" + (this.cpu.getCoda().size() + (this.cpu.isbusy()?1:0)) + "\nPopolazione D1: " + (this.disk1.getCoda().size()+(this.disk1.isbusy()?1:0)) + "\nPopolazione D2: " + (this.disk2.getCoda().size()+ (this.disk2.isbusy()?1:0) + "\nCalendarioEventi:" + Arrays.toString(this.CalendarioEventi) +"\n" + "Tempo ultimo job: " + this.tempoDiCicloUltimoJob); 
        			JOptionPane.showConfirmDialog((Component)null, message, "alert", JOptionPane.OK_CANCEL_OPTION);
        		}*/
    			nextev=eventovicino(CalendarioEventi, i);
    		}
    	}
    }
    public int eventovicino(Evento[] cal, int T){
    	Evento min=cal[this.numeroJobAttivi];
    	int minint=cal.length;
    	for (int i=0; i<cal.length;i++ ){
    		if (cal[i]!=null){
    			if (cal[i]!=null && cal[i].gettempo() < min.gettempo() && ((cal[i].gettempo()-(double)T)<1) ){
    				minint=i;
    				min=cal[i];
    			}
    		}
    	}
    	return minint;   	
    }
    public void aggiungiArrivo(Job job){
    	double vecchioTempo=this.CalendarioEventi[job.getrif()].gettempo();
    	this.CalendarioEventi[job.getrif()]=new Arrivo(job, this, vecchioTempo  );
    }
    public void aggiungiFineCpu(Job job){
        this.CalendarioEventi[job.getrif()]= new FineCPU(job, this, this.cpu.generaTempo(this.TEMPO) );   
    }
    public void aggiungiFineDisk1(Job job){
    	this.CalendarioEventi[job.getrif()]= new FineD1(job, this, this.disk1.generaTempo(this.TEMPO) );    
    }
    public void aggiungiFineDisk2(Job job){        
    	this.CalendarioEventi[job.getrif()]= new FineD2(job, this, this.disk2.generaTempo(this.TEMPO) );   
    }
    public void aggiungiFineSimulazione(){
    	int posizione = this.numeroJobAttivi;
    	this.CalendarioEventi[posizione]=new FineSim(this, FINESIMULAZIONE);    
    }
    public void aggiungiOsservazione(){
        int posizione = this.numeroJobAttivi + 1;
        double nuovoTempoOsservazione = this.CalendarioEventi[posizione].gettempo() + this.TempoOsservazione; 
        this.CalendarioEventi[posizione] = new Osservazione(this , nuovoTempoOsservazione );   
    }
    public Job liberaTokenPool(int indice){
    	this.tokenPool[indice]=true;
    	Job job= this.codaJobEsterni.successivo();
    	job.setRif(this.decrementaTokenPool());
        job.setTempoIngresso(this.TEMPO);
        this.jobAmmessi++;
    	return job;
    }
    public void reset(){
    	this.cpu.getCoda().free();
    	this.cpu.libera();
    	this.disk1.getCoda().free();
    	this.disk1.libera();
    	this.disk2.getCoda().free();
    	this.disk2.libera();
    	this.codaJobEsterni.free();
    	for (int i=0; i<this.numeroJobAttivi; i++){
    		this.CalendarioEventi[i]=null;
    	} 
    	for (int i=0; i<this.tokenPool.length; i++){
    		this.tokenPool[i]=true;
    	}
     } 
   
    public double TEMPO = 0.0;
    public GeneratoreUniforme gu;
    public Centro cpu ;
    public Centro disk1;
    public Centro disk2;
    public Evento[] CalendarioEventi;
    public ArrayList<Double> osservazioneD1= new ArrayList<Double>();
    public ArrayList<Double> osservazioneTempoRisposta = new ArrayList<Double>();
    public ArrayList<Double> osservazioneRispostaPassiva = new ArrayList<Double>();
    public ArrayList<Double> osservazioneD2 = new ArrayList<Double>();
    public ArrayList<Double> osservazioneCpu = new ArrayList<Double>();
    public ArrayList<Double> osservazioneTempoRispostaTuttiJob = new ArrayList<Double>();
    public ArrayList<Double> osservazionetempisimulatore = new ArrayList<Double>();
    public ArrayList<Double> osservazionethroughput = new ArrayList<Double>();
    public ArrayList<Double> osservazioneTempoCoda= new ArrayList<Double>();
    public ArrayList<Integer> osservazioneCodaE = new ArrayList<Integer>();
    public ArrayList<Double> osservazioneTempidiCiclo= new ArrayList<Double>();
    public double FINESIMULAZIONE=1000000;
    public double tempoUltimoJob=0.0;
    public double tempoRispostaPassiva=0.0;
    public double throughputUltimoJob=0.0;
    public PrintStream printStream;
    public int jobTerminati=0;
    public int jobAmmessi =0;
    public long TEMPOSTABILIZZAZIONE;
    public double TempoOsservazione=50;
    public boolean insiemePassivo=true;
    private File file;
    private FileOutputStream fos;
    private GeneratoreIperEsponenziale sorgente;
    private int numeroJobAttivi;
    private boolean[] tokenPool;
	public double tempoDiCicloUltimoJob;
	public double osserva=100000000;
	public int osservazioneJobTerminati=-1;
	
    
}
