package mis;

public class Job {
	private int rif;
	private int numcicli;
	private double tempoNascita;
	private int cicliFatti;
    private double tempoIngresso;
    private double tempoInizioCiclo;
	public Job(int rif, int numcicli){
		this.rif=rif;
		this.numcicli=numcicli;
	}
	public Job(int n , double tempoNascita){
		this.tempoNascita=tempoNascita;
		numcicli=n;
		rif=-1;
	}
	public double getTempoNascita(){
		return this.tempoNascita;
	}     
    public void setTempoNascita(double t){
        this.tempoNascita=t;
    } 
    public double getTempoIngresso(){
        return this.tempoIngresso;
    }    
    public void setTempoIngresso(double tempo){
        this.tempoIngresso=tempo;
    }     
	public int getrif(){
		return rif;
	}
	public int getnumcicli(){
		return numcicli;
	}
	public void setRif(int rif){
		this.rif=rif;
	}
	public void deccicli(){
		numcicli--;
		cicliFatti++;
	}
	public int getCicliFatti(){
		return cicliFatti;
	}
	public void setTempoInizioCiclo(double tempoInizioCiclo) {
		this.tempoInizioCiclo = tempoInizioCiclo;
	}
	public double getTempoInizioCiclo() {
		return tempoInizioCiclo;
	}
}
