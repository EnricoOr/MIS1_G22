package org.mis.gen;


public class GeneratoreIperEsponenziale extends GeneratoreEsponenziale {
	
		private double tx;
		private double P;
		private Random ran;
		
		/**
		 * Il primo parametro è il seme, il secondo parametro è il tempo 
		 * medio di arrivo o di servizio del centro.
		 * @param tx
		 */
		
		public GeneratoreIperEsponenziale(double tx, Random rand, double p) {
			super(1, rand);
			this.tx = tx;
			this.P = p;
			ran = rand;
		}
		
		/**
		 * Funzione che ritorna il prossimo numero della sequenza 
		 * pseudocasuale della distribuzione uniforme iperesponenziale 
		 * con probabilità P compreso tra 0 e 1.
		 * @return nextIperExp
		 */
		
		public double nextIperExp() {
			if (ran.nextNumber()>P) return (super.nextExp()*(tx/(2*(1-P))));
			else return (super.nextExp()*(tx/(2*P)));
		}

	}

