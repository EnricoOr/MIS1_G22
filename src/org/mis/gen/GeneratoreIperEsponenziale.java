package org.mis.gen;


public class GeneratoreIperEsponenziale extends GeneratoreEsponenziale {
	
		private double tx;
		private double P;
		private Random ran;
		private double molt1;
		private double molt2;
		
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
			molt1 = this.tx / (2 * (1 - P));
			molt2 = this.tx / (2 * P);
		}
		
		/**
		 * Funzione che ritorna il prossimo numero della sequenza 
		 * pseudocasuale della distribuzione uniforme iperesponenziale 
		 * con probabilità P compreso tra 0 e 1.
		 * @return nextIperExp
		 */
		
		public double nextIperExp() {
			if (ran.nextNumber()>P) 
				return (super.nextExp()*molt1);
			else 
				return (super.nextExp()*molt2);
		}

	}

