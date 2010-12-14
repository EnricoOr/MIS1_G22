package mis;

public class Osservazione extends Evento{

	public Osservazione(Sequenziatore sequenziatore, double tempo){
		super(null, sequenziatore, tempo);
	}
    @Override
    public void evolvi() {
    	if (this.sequenziatore.insiemePassivo){
    		double popolazioneD1= this.sequenziatore.disk1.getCoda().size();
    		if (this.sequenziatore.disk1.isbusy())
    			popolazioneD1++;
    		double popolazioneD2= this.sequenziatore.disk2.getCoda().size();
    		if (this.sequenziatore.disk2.isbusy())
    			popolazioneD2++;
    		double popolazioneCpu= this.sequenziatore.cpu.getCoda().size();
    		if (this.sequenziatore.cpu.isbusy())
    			popolazioneCpu++;
    		this.sequenziatore.osservazioneD1.add(popolazioneD1);
    		this.sequenziatore.osservazioneTempoRisposta.add(this.sequenziatore.tempoUltimoJob);
    		this.sequenziatore.osservazioneRispostaPassiva.add(this.sequenziatore.tempoRispostaPassiva);
    		this.sequenziatore.throughputUltimoJob=this.sequenziatore.getNumeroJobAttivi()/this.sequenziatore.tempoUltimoJob;
    		this.sequenziatore.osservazionethroughput.add(this.sequenziatore.throughputUltimoJob);
    		/*Altre Osservazioni
        this.sequenziatore.osservazioneCpu.add(popolazioneCpu);
        this.sequenziatore.osservazioneD2.add(popolazioneD2);
    		 */
    		/*stampa su file dell'osservazione
        this.sequenziatore.printStream.println(this.sequenziatore.TEMPO + "\t" + new Double(this.sequenziatore.tempoUltimoJob).toString().replace(".", ",") + "\t" + popolazioneD1 + "\t" +  new Double(this.sequenziatore.throughputUltimoJob).toString().replace(".", ","));
    		 */
    	}
    	else {
    	
    		if (this.sequenziatore.osserva!=0){
    			this.sequenziatore.osservazionethroughput.add((double)this.sequenziatore.osservazioneJobTerminati/100);
    		}
    		this.sequenziatore.osservazioneJobTerminati=0;
    		//inizializzazione finestra di osservazione
    		this.sequenziatore.osserva=this.sequenziatore.TEMPO + 100;
    		//this.sequenziatore.osservazionethroughput.add(this.sequenziatore.getNumeroJobAttivi()/this.sequenziatore.tempoDiCicloUltimoJob);
    		this.sequenziatore.osservazioneTempoRisposta.add(this.sequenziatore.tempoDiCicloUltimoJob);
    	}
        this.sequenziatore.aggiungiOsservazione();
    }

}
