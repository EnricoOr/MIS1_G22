package org.mis.gen;


public class GeneratorePoissoniano extends Generatore{
	
	GeneratoreUniforme g;
	double lambda,n,e,p;
	
	public GeneratorePoissoniano(double lambda,long seme)
	{
		this.lambda=lambda;
		this.p=1.0;
		this.n=0.0;
		this.e=Math.pow(Math.E,-lambda);
		this.g=new GeneratoreUniforme(seme);
	}
	@Override
	public double nextNumber()
	{
		double retn;
		p=p*(g.nextNumber());
		if(p<e){
			retn=n;
			n=0;
			p=1.0;
			return retn;
		}
		else
		{
			n+=1;
			return nextNumber();
		}
	}

    
    public void calcolaMediaVarianza(GeneratorePoissoniano gen, int numeroGenerazioni){
    	double media,media2,varianza,generato;
        double somma=0;
        double somma2=0;
        for (int i=0;i<numeroGenerazioni;i++){
        	generato=this.nextNumber();
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
