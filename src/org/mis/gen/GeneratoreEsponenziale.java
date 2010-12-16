package org.mis.gen;


public class GeneratoreEsponenziale extends Generatore{
	GeneratoreUniforme g;
    private long numeroGenerazioni;
	double ts;
	
	public GeneratoreEsponenziale(double ts,long seme)
	{
		this.ts=ts;
		this.g=new GeneratoreUniforme(seme);
                this.numeroGenerazioni=0;
	}
	
	@Override
	public double nextNumber()
	{
                this.numeroGenerazioni++;
		return ((-this.ts)*Math.log(this.g.nextNumber()));
	}    
	
	public long getNumeroGenerazioni(){
        return this.numeroGenerazioni;
    }  
	
    public double getMedia(){
        return this.ts;
        }      
    
    public void calcolaMediaVarianza(GeneratoreEsponenziale gen, int numeroGenerazioni){
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
