package mis;


public class FineD2 extends Evento{
    
    public FineD2(){
        
    }
    public FineD2(Job job, Sequenziatore sequenziatore, double tempo){
        super(job, sequenziatore, tempo);
        FineD2.accessi++;
    }
    @Override
    public void evolvi() {
        this.sequenziatore.disk2.occupato=false;
        if(this.sequenziatore.cpu.isbusy()){ //riporta il job alla CPU
            this.sequenziatore.cpu.getCoda().add(job);
            this.tempo=this.sequenziatore.FINESIMULAZIONE;
        }
        else{
            this.sequenziatore.cpu.occupa();
            this.sequenziatore.aggiungiFineCpu(job);
        }
        
        if(!this.sequenziatore.disk2.getCoda().isempty()){ //riceve un nuovo job dalla coda
            Job successivo=this.sequenziatore.disk2.getCoda().successivo();
            this.sequenziatore.disk2.occupa();
            this.sequenziatore.aggiungiFineDisk2(successivo);
        }
        
    }
    
    private static int accessi;

}
