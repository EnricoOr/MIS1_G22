package mis;

public class GeneratoreKerlangiano extends Generatore{
	private int k;
	private Generatore g;
    private long numeroGenerazioni;
	
	public GeneratoreKerlangiano(int stadi,double ts,long seme)
	{
		this.g=new GeneratoreEsponenziale(ts/stadi,seme);
		this.k=stadi;
        this.numeroGenerazioni=0;
	}
	@Override
	public double nextNumber()
	{
        this.numeroGenerazioni++;
		double x=0;
		for(int i=0;i<k;i++)
			x+=this.g.nextNumber();           
		return x;
	}
    public long getNumeroGenerazioni(){
        return this.numeroGenerazioni;
    }    
    public void calcolaMediaVarianza(GeneratoreKerlangiano gen, int numeroGenerazioni){
        double media,media2,varianza,generato;
        double somma=0;
        double somma2=0;
        for (int i=0;i<numeroGenerazioni;i++){
        	generato=gen.nextNumber();
            somma+=generato;
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

