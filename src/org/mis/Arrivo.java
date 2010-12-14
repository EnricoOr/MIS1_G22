
package mis;


public class Arrivo extends Evento{
    
    Arrivo(){
        
    }
    
    public Arrivo(Job job, Sequenziatore sequenziatore,double tempo){
        super(job, sequenziatore,tempo);
        
    }
    
    

    @Override
    public void evolvi() {
        //se la cpu è libera occupala, altrimenti mettiti in coda
        if(!this.sequenziatore.cpu.isbusy()){
            this.sequenziatore.cpu.occupa();
            this.sequenziatore.aggiungiFineCpu(this.job);
        }
        else{
            this.sequenziatore.cpu.getCoda().add(this.job );
            this.tempo=this.sequenziatore.FINESIMULAZIONE;
        }
            
        
    }  

}
