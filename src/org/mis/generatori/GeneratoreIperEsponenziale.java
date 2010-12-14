package org.mis;


public class GeneratoreIperEsponenziale extends Generatore {
	private double prob;
    private long numeroGenerazioni;
	private double t1,t2;    
	private Generatore genU,genE1,genE2;
	
	public GeneratoreIperEsponenziale(double prob,double t,long seme1,long seme2)
	{
		this.prob=prob;
		this.t1=t/(2*prob);
		this.t2=t/(2*(1-prob));
		this.genE1=new GeneratoreEsponenziale(this.t1,seme1);
        this.genE2=new GeneratoreEsponenziale(this.t2,seme2);
		this.genU=new GeneratoreUniforme(seme2);
        this.numeroGenerazioni=0;
	}
	@Override
	public double nextNumber()
	{
        this.numeroGenerazioni++;
		if(genU.nextNumber()<prob)
			return this.genE1.nextNumber();
		else
			return this.genE2.nextNumber();
	}
    public long getNumeroGenerazioni(){
        return this.numeroGenerazioni;
    }    
    public void calcolaMediaVarianza(GeneratoreIperEsponenziale gen, int numeroGenerazioni){
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

