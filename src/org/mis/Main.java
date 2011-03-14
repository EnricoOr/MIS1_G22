package org.mis;

import org.jfree.ui.RefineryUtilities;
import java.util.*;
import java.io.*;

import org.mis.gen.*;
import org.mis.gen.Random;
import org.mis.sim.*;


/**
 * La classe main si occupa dell'elaborazione dei parametri passati al programma e,
 * a seconda di questi, procede al test dei generatori
 * oppure instanzia la classe simulatore con i parametri appropriati
 * @author 
 * @author 
 * @author 
 */

public class Main {

		private static int k;
		private static final int N =1000000;
		private static int ix;
		private static int range=100;
		private static int[] istogramma=new int[range+1];
		private static double tx = 0.0333;
		private static Istogramma ist;
		private static boolean stab = false;
		private static boolean logMode = false;
		private static boolean testMode = false;
		private static String logAcc = "0123456789";

		
		/**
		 * Funzione che si occupa di eseguire il test del generatore random
		 * e di stamparne i risultati
		 */
		
		private static void TestGenRandom() {
			Random gen = new Random(ix);
			double tot = 0;
			double tot2 = 0;	
			double x;
			resetIst();
			for (int i = 0; i< N; ++i) {
				x = gen.nextNumber();
				istogramma[(int)(x*range)]++;
				tot += x;
				tot2 += Math.pow(x,2);
			}
			System.out.println("Test generatore random\nmedia:    "+tot/N+" (0.5)\nvarianza: "+(tot2/N-Math.pow((tot/N),2))+" (0.0)\n");
			ist = new Istogramma("Test generatore random");
			stampaIst(ist);
		}
		
		/**
		 * Funzione che si occupa di eseguire il test del generatore erlangiano
		 * e di stamparne i risultati
		 */
		
		private static void TestGenErlang() {
			
			GeneratoreKerlangiano erl = new GeneratoreKerlangiano(ix, tx, k);
			double tot = 0;
			double tot2 = 0;
			double x;
			resetIst();
			
			for (int i = 0; i< N; ++i) {
				x = erl.nextErlang();
				istogramma[(int)(x*range)]++;
				tot += x;
				tot2 += Math.pow(x,2);
			}
			System.out.println("Test generatore " + k + "-erlangiano\nmedia:    "+tot/N+" ("+(tx)+")\nvarianza: "+(tot2/N-Math.pow((tot/N),2))+" ("+Math.pow(tx,2)/k+")\n");
			ist = new Istogramma("Test generatore " + k + "-erlangiano");
			stampaIst(ist);
		}

		/**
		 * Funzione che si occupa di eseguire il test del generatore esponenziale
		 * e di stamparne i risultati
		 */
		
		private static void TestGenEsp() {
			GeneratoreEsponenziale exp = new GeneratoreEsponenziale(tx, new Random(Seme.getSeme()));
			double tot = 0;
			double tot2 = 0;
			double x;
			resetIst();
			for (int i = 0; i< N; ++i) {
				x = exp.nextExp();
				istogramma[(int)(x*range)]++;
				tot += x;
				tot2 += Math.pow(x,2);
			}

			System.out.println("Test generatore esponenziale\nmedia:    "+tot/N+" ("+(tx)+")\nvarianza: "+(tot2/N-Math.pow((tot/N),2))+" ("+Math.pow(tx,2)+")\n");
			ist = new Istogramma("Test generatore esponenziale");
			stampaIst(ist);
		}

		/**
		 * Funzione che si occupa di eseguire il test del generatore iperesponenziale p=0.6
		 * e di stamparne i risultati
		 */
		
		private static void TestGenIperesp() {
			Random rand = new Random(Seme.getSeme());
			GeneratoreIperEsponenziale ipexp = new GeneratoreIperEsponenziale(tx, rand, 0.6);
			double tot = 0;
			double tot2 = 0;
			double x;
			resetIst();
			for (int i = 0; i< N; ++i) {
				x = ipexp.nextIperExp();
				istogramma[(int)(x*range)]++;
				tot += x;
				tot2 += Math.pow(x,2);
			}
			System.out.println("Test generatore iperesponenziale\nmedia:    "+tot/N+" ("+(tx)+")\nvarianza: "+(tot2/N-Math.pow((tot/N),2))+" ("+Math.pow(tx,2)*(1/(2*0.4*0.6)-1)+")\n");
			ist = new Istogramma("Test generatore iperesponenziale");
			stampaIst(ist);
		}
		
		/**
		 * Funzione che si occupa di eseguire il test del generatore iperesponenziale p=0.3
		 * e di stamparne i risultati
		 */
		
		private static void TestGenIperesp2() {
			Random rand = new Random(Seme.getSeme());
			GeneratoreIperEsponenziale ipexp = new GeneratoreIperEsponenziale(tx, rand, 0.3);
			double tot = 0;
			double tot2 = 0;
			double x;
			resetIst();
			for (int i = 0; i< N; ++i) {
				x = ipexp.nextIperExp();
				istogramma[(int)(x*range)]++;
				tot += x;
				tot2 += Math.pow(x,2);
			}
			System.out.println("Test generatore iperesponenziale\nmedia:    "+tot/N+" ("+(tx)+")\nvarianza: "+(tot2/N-Math.pow((tot/N),2))+" ("+Math.pow(tx,2)*(1/(2*0.4*0.3)-1)+")\n");
			ist = new Istogramma("Test generatore iperesponenziale");
			stampaIst(ist);
		}
		
		/*
		 * Questa funzione si occupa di resettare il vettore istogramma
		 * per il disegno dei grafici
		 */
		
		public static void resetIst()
		{
			for(int i =0;i<istogramma.length;i++){
				istogramma[i]=0;
			}
		}
		
		/*
		 * Classe main del nostro simulatore
		 * avvia la simulazione
		 * @param String[] args
		 */
		
		public static void main(String[] args) {
			long Tempo1;
			long Tempo2;
			long Tempo;
			
			Tempo1=System.currentTimeMillis();
			
			Seme.apri();
			
			ix= Seme.getSeme();
			
			if (elaboraOpzioni(args))
			{
				if (testMode)
				{
					System.out.println("Avviata modalita' test");
					TestGenRandom();
					k = 2;
					TestGenErlang();
					TestGenEsp();
					TestGenIperesp();
					TestGenIperesp2();
					k = 3;
					TestGenErlang();
					k = 12;
					TestGenErlang();
				}
				else
				{
					int clien = 120; //se stabilizzazione la simulazione parte solo con 120 client altrimenti prova da 10 a 120
					if (!stab) clien = 10;
					for(; clien<=120; clien += 10)
					{
						Simulatore simulatore = new Simulatore(clien, stab, logMode, logAcc);
						simulatore.avvia();
					}
				}
			}
			else
			{
				System.out.println("\nLe opzioni valide sono le seguenti: ");
				System.out.println("-l (per l'output dettagliato)");
				System.out.println("-a (per l'accuratezza dell'output)");
				System.out.println("-s (per la modalita' di stabilita')");
				System.out.println("-t (per la modalita' di test dei generatori)");
				System.out.println("-h (per visualizzare l'aiuto)"); 
				System.out.println("ATTENZIONE: si sconsiglia di abilitare il log mode" +
						" (-l) per un lungo periodo di simulazione perche' occupa molto spazio" +
						" su disco");
			}
			
			Tempo2=System.currentTimeMillis();

			Tempo=Tempo2-Tempo1;
			System.out.println("Tempo impiegato dalla simulazione: " + Tempo/60000 + " minuti.");
		}
		
		/**
		 * Funzione che si occupa di stampare nel terminale i valori per disegnare l'istogramma
		 * e in una finestra separata visualizza tale grafico
		 * @param Istogramma ist
		 */
		
		public static void stampaIst(Istogramma ist)
		{
			System.out.println("I valori per disegnare l'istogramma sono: ");
			System.out.print("[");
			for(int i = 0; i<istogramma.length;i++){
				System.out.print(istogramma[i]);
				ist.addvalues(istogramma[i], Integer.toString(i));
				if(i!=istogramma.length-1)
					System.out.print(" , ");
			}
			System.out.println("]");
			System.out.println();
			
			ist.pack();
			RefineryUtilities.centerFrameOnScreen(ist);
			ist.setVisible(true);
		}
		
		/**
		 * Questa funzione controlla che i parametri passati in ingresso al programma siano corretti
		 * @param argomenti passati al programma
		 * @return ritorna true se il parsing degli argomenti ha avuto successo
		 */
		
		public static boolean elaboraOpzioni(String args[]) {
			boolean aInserted = false;
			boolean lInserted = false;
			boolean sInserted = false;
			boolean tInserted = false;
			for (int i = 0; i < args.length; i++) {
				if ((args[i].charAt(0) == '-') && (args[i].length() == 2)) {
					if (args[i].charAt(1) == 'l') {
						if (!lInserted) {
							logMode = true;
							lInserted = true;
						} else {
							System.out.println("Parametro '" + args[i] + "' inserito 2 volte. ");
							return false;
						}
					} else if (args[i].charAt(1) == 's') {
						if (!sInserted) {
							stab = true;
							sInserted = true;
						} else {
							System.out.println("Parametro '" + args[i] + "' inserito 2 volte. ");
							return false;
						}
					} else if (args[i].charAt(1) == 't') {
						if (!tInserted) {
							testMode = true;
							tInserted = true;
						} else {
							System.out.println("Parametro '" + args[i] + "' inserito 2 volte. ");
							return false;
						}
					} else if (args[i].charAt(1) == 'a') {
							if (!aInserted) {
								try {									
									logAcc = (args[i+1]);
									aInserted = true;
								} catch (NumberFormatException e) {
									System.out.println("Valore '" + args[i+1] + "' non valido come" +
											" accuratezza del log. ");
									return false;
								} catch (ArrayIndexOutOfBoundsException e) {
									System.out.println("Non e' stato inserito un valore consentito. ");
									return false;
								}
								i++;
							} else {
								System.out.println("Parametro '" + args[i] + "' inserito 2 volte. ");
								return false;
							}
					} else if (args[i].charAt(1) == 'h') {
						return false;
					} else {
						System.out.println("Parametro '" + args[i] + "' non valido. ");
						return false;
					}
				} else {
					System.out.println("Parametro '" + args[i] + "' non valido. ");
					return false;
				}
			}
			return true;
		}
}
