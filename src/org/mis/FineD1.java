package org.mis;


public class FineD1 extends Evento{
    
    public FineD1(){    
    }
    public FineD1(Job job, Sequenziatore sequenziatore, double tempo){
        super(job, sequenziatore, tempo);
        FineD1.accessi++;
    }
    public int getAccessi(){
        return FineD1.accessi;
    } 
    @Override
    public void evolvi() {    
        this.sequenziatore.disk1.libera();
        if(this.sequenziatore.cpu.isbusy()){ //riporta il job alla CPU
            this.sequenziatore.cpu.getCoda().add(this.job);
            this.tempo=this.sequenziatore.FINESIMULAZIONE;
        }
        else{
            this.sequenziatore.cpu.occupa();
            this.sequenziatore.aggiungiFineCpu(this.job);
        }
        
        if(!this.sequenziatore.disk1.getCoda().isempty()){ //riceve un nuovo job dalla coda
            Job successivo=this.sequenziatore.disk1.getCoda().successivo();
            this.sequenziatore.disk1.occupa();
            this.sequenziatore.aggiungiFineDisk1(successivo);
        }
        
    }
    
    private static int accessi;
}
