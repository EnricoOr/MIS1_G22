/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mis.processi;

import org.mis.gen.GeneratorePoissoniano;

/**
 *
 * @author fabrizio
 */
public class Terminale {
    
   public Terminale(){
        
    }
    
   public Terminale(GeneratorePoissoniano generatore){
       this.generatore=generatore;
   }
   
   public double GeneraTempo(double T){
       this.tempoDiGenerazione=this.generatore.nextNumber()+T;
       return this.tempoDiGenerazione;
   }
   
   public double getTempoRisposta(double T){
       return T-this.tempoDiGenerazione;
   }
    
   
   private GeneratorePoissoniano generatore;
   private double tempoDiGenerazione;
}
