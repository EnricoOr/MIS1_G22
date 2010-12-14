package org.mis;


public class FineCPU extends Evento {
       
    public FineCPU(){
        
    }
    public FineCPU(Job job, Sequenziatore sequenziatore, double tempo){
        super(job, sequenziatore, tempo);
        FineCPU.visiteCpu++;
    }    
    public int getVisite(){
        return FineCPU.visiteCpu;
    }
    @Override
    public void evolvi() {   
        this.sequenziatore.cpu.libera();
        probDischi = this.sequenziatore.gu.nextNumber(); //calcola la probabilità di routing verso i dischi
        if(this.probDischi <= 0.035){ //fine ciclo
        	if (this.sequenziatore.insiemePassivo){
        		this.job.deccicli();
        		if (this.job.getnumcicli()==0){ 
        			double tempoRispostaPassiva=this.sequenziatore.TEMPO - this.job.getTempoNascita(); //tempo di risposta nell'insieme passivo
        			this.sequenziatore.tempoRispostaPassiva=tempoRispostaPassiva;
        			double tempoCoda= this.job.getTempoIngresso() - this.job.getTempoNascita();
        			this.sequenziatore.osservazioneTempoCoda.add(tempoCoda);
        			this.sequenziatore.osservazioneCodaE.add(this.sequenziatore.codaJobEsterni.size());
        			this.sequenziatore.jobTerminati++;
        			Job nuovo= this.sequenziatore.liberaTokenPool(this.job.getrif());
        			this.job=nuovo;
        		}
        	}
        	else {
        		this.sequenziatore.jobTerminati++;
        		if (this.sequenziatore.osserva>this.sequenziatore.TEMPO){ //verifica se la finestra di osservazione è aperta
        			this.sequenziatore.osservazioneJobTerminati++;
        		}
        		if (this.job.getTempoInizioCiclo()==0){
        			this.sequenziatore.tempoDiCicloUltimoJob= this.sequenziatore.TEMPO - this.job.getTempoNascita();
        		}
        		else {
        			this.sequenziatore.tempoDiCicloUltimoJob =  this.sequenziatore.TEMPO - this.job.getTempoInizioCiclo();
        		}
        		this.sequenziatore.osservazioneTempidiCiclo.add(this.sequenziatore.tempoDiCicloUltimoJob); //usato per la stabilizzazione
        	}
        	this.job.setTempoInizioCiclo(this.sequenziatore.TEMPO);	
            this.sequenziatore.aggiungiArrivo(job);
        }
        else if(this.probDischi<=0.263 && this.probDischi>0.035){ //CPU->D2
             if(!this.sequenziatore.disk2.isbusy()){
                this.sequenziatore.disk2.occupa();
                this.sequenziatore.aggiungiFineDisk2(this.job);
            }
             else  {
                 this.sequenziatore.disk2.getCoda().add(this.job);
                 this.tempo=this.sequenziatore.FINESIMULAZIONE;
             }}
        else { //CPU->D1
                 if(!this.sequenziatore.disk1.isbusy()){
                     this.sequenziatore.disk1.occupa();
                     this.sequenziatore.aggiungiFineDisk1(this.job);
                 }
                 else {
                 	this.sequenziatore.disk1.getCoda().add(this.job);
                 	this.tempo=this.sequenziatore.FINESIMULAZIONE;  
                 }
             }
        if(!this.sequenziatore.cpu.getCoda().isempty()){
            Job successivo=this.sequenziatore.cpu.getCoda().successivo();
            this.sequenziatore.cpu.occupa();
            this.sequenziatore.aggiungiFineCpu(successivo);
        }   
    }
    
    private static int visiteCpu =0;
    private double probDischi;
}
