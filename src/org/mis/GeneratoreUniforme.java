package mis;


public class GeneratoreUniforme extends Generatore {
	
	private long a=1220703125; //moltiplicatore a=5^13
	private Long m=new Long("2147483648"); //modulo m=2^31
	private long seme;
    private long numeroGenerato;
    private long numeroGenerazioni;
	
	public GeneratoreUniforme(long seme)
	{
		this.seme=seme;
        this.numeroGenerato=seme;
        this.numeroGenerazioni=0;
	}
	public long generaSeme()						
	{
		for(int i=0;i<20;i++)
			seme=(long)((a*seme)%(m.doubleValue()));		
		return seme;
	}
	
	public double nextNumber()
	{
		this.numeroGenerato =((this.a*this.numeroGenerato)%(this.m.longValue()));
		this.numeroGenerazioni++;
        return (this.numeroGenerato/(this.m.doubleValue()));        
	}
    public int nextNumber100_1000()
	{
		this.numeroGenerato =((this.a*this.numeroGenerato)%(this.m.longValue()));
		this.numeroGenerazioni++;
        return (int)(1000*(this.numeroGenerato/(this.m.doubleValue())));               
	}   
    public long getNumeroGenerazioni(){
        return this.numeroGenerazioni;
    }   
    public long getNumeroGenerato(){
        return this.numeroGenerato;
    }       
    public long getSeme(){
        return this.seme;
    }    
    public void calcolaMediaVarianza(GeneratoreUniforme gen, int numeroGenerazioni){
        double media,varianza,media2,generato;
        double somma=0;
        double somma2=0;
        for(int i=0; i<numeroGenerazioni;i++){
        	generato=gen.nextNumber();
            somma += generato;
            somma2 += (generato*generato);
        }
        media=somma/numeroGenerazioni;
        media2=somma2/numeroGenerazioni;
        varianza=media2-(media*media);
        System.out.println("Media: " + media);
        System.out.println("Media dei uadrati: " + media2);
        System.out.println("Varianza: " + varianza);
    }
    
}

