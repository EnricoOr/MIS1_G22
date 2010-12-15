package org.mis;

import java.util.*;
import java.io.*;

import org.mis.gen.GeneratoreUniforme;

public class Main {
	       
	public static double calcolaMediaCampionaria(ArrayList<Integer> v){
		int sum=0;
		for (int n: v){
			sum += n;
		}
		return (double)(sum/v.size());
		
	}    
	public static double calcolaMediaDouble(ArrayList<Double> v){
		double sommaMedie=0.0;
		for (double n: v){
			sommaMedie+=n;
		}
		return sommaMedie/v.size();
	}    
	public static double calcolaVarianzaCampionaria(ArrayList<Double> medieCampionarie, double sm){	
		double differenzaQ=0.0;
		for (int i=0; i<medieCampionarie.size(); i++){
			differenzaQ+=(medieCampionarie.get(i)-sm)*(medieCampionarie.get(i)-sm);
		}              
		return (differenzaQ/(medieCampionarie.size()-1));
	}
	public static void stimatoriMV(int k, ArrayList<ArrayList<Double>> tempiDiRisposta, PrintStream ps){
		ArrayList<Double> medie=new ArrayList<Double>();
        double m =0;
		double sv =0;
		double sm=0;            
		for (int i=0; i<100; i++){
			for (int j=1; j<k; j++){
				m+=tempiDiRisposta.get(i).get(j);
			}
			sm=m/k;
			m=0;
			medie.add(sm);
		}
		sm=calcolaMediaDouble(medie);
		sv=calcolaVarianzaCampionaria(medie,sm);
		ps.println(k + "\t" + new Double(sm).toString().replace(".", ",") + "\t" + new Double(sv).toString().replace(".", ","));
		medie.clear();	
		}
        public static void stimatoriMV2(int k, ArrayList<Double> tempiDiRisposta, PrintStream ps){
		ArrayList<Double> medie=new ArrayList<Double>();
                double m =0;
		double sv =0;
		double sm=0;       
		for (int i=0; i<k; i++){
			m+=tempiDiRisposta.get(i);
			sm=m/k;
			m=0;
			medie.add(sm);
		}
		sv=calcolaVarianzaCampionaria(medie,sm);
		ps.println(k + "\t" + new Double(sm).toString().replace(".", ",") + "\t" + new Double(sv).toString().replace(".", ","));
		medie.clear();	
		}
    /*private static int trovaMinimo(ArrayList<Integer> jobTerminati) {      
    	int temp =10000;  
        for(int i=0; i<jobTerminati.size(); i++){
        	if (temp>jobTerminati.get(i))
          		temp=jobTerminati.get(i);
        }
        return temp;
    }*/
        
		
    
    public static void main(String[] args) {
     /* Sezione per la verifica dei generatori
      * System.out.println("Generatore Uniforme:");
        GeneratoreUniforme gen = new GeneratoreUniforme(12573);
        gen.calcolaMediaVarianza(gen, 10000);
        System.out.println("Generatore Esponenziale:");
        GeneratoreEsponenziale gen2 = new GeneratoreEsponenziale(0.5,3576);
        gen2.calcolaMediaVarianza(gen2, 10000);
        System.out.println("Generatore IperEsponenziale:");
        GeneratoreIperEsponenziale gen3 = new GeneratoreIperEsponenziale(0.3,0.0394,67551569,196309533);
        gen3.calcolaMediaVarianza(gen3, 10000);
        System.out.println("Generatore KErlangiano:");
        GeneratoreKerlangiano gen4=new GeneratoreKerlangiano(3, 0.05, 897551);
        gen4.calcolaMediaVarianza(gen4, 10000);
        System.out.println("Generatore Poissoniano");
        */
    	/*Creazione array per le varie osservazioni fatte
        ArrayList<Integer> jobTerminati = new ArrayList<Integer>();
    	ArrayList<Double> medieOsservazioneD1;
    	ArrayList<Double> medieOsservazioneD2;
    	ArrayList<Double> medieOsservazioneCpu;
    	ArrayList<Double> medieTempiRisposta;
    	ArrayList<ArrayList<Double>> tempiDiRisposta= new ArrayList<ArrayList<Double>>();
    	ArrayList<ArrayList<Double>> tempiDiRispostaPassiva= new ArrayList<ArrayList<Double>>();
        /*ArrayList<ArrayList<Double>> codeD2= new ArrayList<ArrayList<Double>>();
        */
        ArrayList<GeneratoreUniforme> gen = new ArrayList<GeneratoreUniforme>();
        gen.add(new GeneratoreUniforme(12573));
        gen.add(new GeneratoreUniforme(2011356505));
        gen.add(new GeneratoreUniforme(1846812177));
        gen.add(new GeneratoreUniforme(154680585));
        gen.add(new GeneratoreUniforme(2126456897));
        //Sequenziatore seq = new Sequenziatore(7, 44900, 12573);
        //calcolo troughput
        //seq.accendi(4490000);
       ///*Sequenziatore per l'insieme attivo
        double tempidistab[]= {1005.12 , 1374, 1695, 1921, 2320, 7613, 8217, 8622, 9738, 10225};
        for (int i=0; i<10;i++){
        Sequenziatore seq = new Sequenziatore(i+1, (int)tempidistab[i], 12573, false );
        seq.accendiInsiemeAttivo((int)tempidistab[i]);
        double mediath = calcolaMediaDouble(seq.osservazionethroughput);
        double varth = calcolaVarianzaCampionaria(seq.osservazionethroughput, mediath);
        System.out.println("*********************" + (i+1) + "*******************");
        System.out.println(Arrays.toString(seq.osservazionethroughput.toArray()));
        System.out.println(Arrays.toString(seq.osservazioneTempoRisposta.toArray()));
       // System.out.println(seq.osservazioneTempidiCiclo.get(seq.osservazioneTempidiCiclo.size()-1));
        System.out.println("Media tempo risposta: " + calcolaMediaDouble(seq.osservazioneTempidiCiclo));
        System.out.println("Throughput media: "+ mediath + "\t Varianza:" + varth);
        System.out.println("Cicli terminati: " + seq.jobTerminati/seq.TEMPO);
        System.out.println("*********************" + (i+1) + "*******************");
        }
        // */
        //double mediats = calcolaMediaDouble(seq.osservazioneTempoRisposta);
        //double mediatp = calcolaMediaDouble(seq.osservazioneRispostaPassiva);
        //double mediath = calcolaMediaDouble(seq.osservazionethroughput);
        //double mediad1 = calcolaMediaDouble(seq.osservazioneD1);
        //double varts = calcolaVarianzaCampionaria(seq.osservazioneTempoRisposta, mediats);
        //double vartp = calcolaVarianzaCampionaria(seq.osservazioneRispostaPassiva, mediatp);
        //double varth = calcolaVarianzaCampionaria(seq.osservazionethroughput, mediath);
        //System.out.println("D1:" + mediad1);
        //System.out.println("Tempo di risposta passiva media:"+ mediatp + "\t Varianza:" +vartp);
        //System.out.println("Tempo medio di attesa sulla coda" + calcolaMediaDouble(seq.osservazioneTempoCoda));
        //System.out.println("Throughput media: "+ mediath + "\t Varianza:" + varth);
        //System.out.println(mediats/seq.getNumeroJobAttivi());
        //System.out.println(seq.jobAmmessi);
        //System.out.println(seq.jobTerminati);
//        
//        
//        try{
//              fos = new FileOutputStream(file);
//              printStream = new PrintStream(fos);
//              printStream.println("osservazioni\tpopolazione\trisposta passiva");
//              for (int i=0; i<seq.osservazioneD1.size(); i++){
//                  printStream.println(i+1 + "\t" + new Double(seq.osservazioneD1.get(i)).toString().replace(".", ",") + "\t"+ new Double(seq.osservazioneRispostaPassiva.get(i)).toString().replace(".", ",") );
//                  }
//             //Stream.println("Throughput media: "+ mediath + "\t Varianza:" + varth);
//        }
//        catch(FileNotFoundException e){
//              e.printStackTrace();
//        }
        //*/
        //}/*
        /*raccolta valori per la stabilizzazione
        GeneratoreUniforme gu=new GeneratoreUniforme(12573);
        ArrayList<Double> medieCodaE= new ArrayList<Double>();
        ArrayList<Double>medieTempoCoda = new ArrayList<Double>();
        for (int i=0; i<100; i++){
            gu = gen.get(i%gen.size());
    		seq=new Sequenziatore(7, 10000, gu.generaSeme() );
    		seq.accendi(100000);
    		//codeD2.add(seq.osservazioneD2);
                //tempiDiRisposta.add(seq.osservazioneTempoRispostaTuttiJob);
                tempiDiRispostaPassiva.add(seq.osservazioneRispostaPassiva);
                medieCodaE.add(calcolaMediaCampionaria(seq.osservazioneCodaE));
                medieTempoCoda.add(calcolaMediaDouble(seq.osservazioneTempoCoda));
                seq.osservazioneCodaE.clear();
                seq.osservazioneTempoCoda.clear();
                
                //jobTerminati.add(seq.jobTerminati);
    		seq.reset(); 
    	}
        System.out.println(tempiDiRispostaPassiva.size() + " " + seq.osservazioneRispostaPassiva);
        System.out.println("tempo attesa coda esterna:" +  calcolaMediaDouble(medieTempoCoda));
        System.out.println("size medio coda esterna:" +  calcolaMediaDouble(medieCodaE));
        
        //int massimo=trovaMinimo(jobTerminati); 
        //Stabilizzazione nel caso di sistema aperto
        /*
        ArrayList<ArrayList<Double>> tempiSingoloCiclo = new ArrayList<ArrayList<Double>>();
        GeneratoreUniforme gu;
        Sequenziatore seq = new Sequenziatore();
        int minimoNumeroCicliTerminati=100000;
        File file;
        FileOutputStream fos;
        PrintStream printStream;
        for (int numeroJob =1 ; numeroJob<6; numeroJob++){
        	for (int i=0; i<100; i++){
        		gu = gen.get(i%gen.size());
        		seq=new Sequenziatore(numeroJob, 100001, gu.generaSeme(), false );
        		seq.accendiInsiemeAttivo(10000);
        		tempiSingoloCiclo.add(seq.osservazioneTempidiCiclo);
        		System.out.println("[" + numeroJob + " JOB ATTIVI]: Esecuzione " + i + "terminata, " + seq.jobTerminati + "cicli fatti");
        		if (seq.osservazioneTempidiCiclo.size()<minimoNumeroCicliTerminati){
        			minimoNumeroCicliTerminati=seq.osservazioneTempidiCiclo.size();
        		}
        	}
        	file=new File("risultati/Test_Risultati_per_stabilizzare_" + numeroJob + "_job_insieme_attivo.xls");
        	try{
        		fos = new FileOutputStream(file);
        		printStream = new PrintStream(fos);
        		printStream.println("simulazione per stabilizzazione "+numeroJob+" Job Attivi");
        		printStream.println("osservazioni\ttempo di ciclo\tvarianza");
        		for (int k=1; k < minimoNumeroCicliTerminati; k++){
        			stimatoriMV(k, tempiSingoloCiclo, printStream);

        		}
        	}catch(FileNotFoundException e){
        		e.printStackTrace();
        	}   
        

        }
        */// FINE STABILIZZAZIONE SISTEMA APERTO 
    }
}



