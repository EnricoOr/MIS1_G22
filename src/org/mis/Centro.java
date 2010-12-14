package mis;


public class Centro {
	private Generatore g;
	private Coda coda;
	public boolean occupato=false;
        
	public Centro(Generatore g, Coda coda){
		this.g=g;
                this.coda=coda;     
	}
	public boolean isbusy(){
		return occupato;
	}
	public void occupa(){
		if (occupato==true) System.err.println("Gia occupato");
		occupato=true;
		
	}
	public void libera(){              
		occupato=false;
	}
	public double generaTempo(double T){
		return T + g.nextNumber();
	}
    public Coda getCoda(){
        return this.coda;
    }
}
