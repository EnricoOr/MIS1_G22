package org.mis.processi;


public abstract class Processo implements Comparable<Processo>{
	private String nome;
	private Stato state;
	private Double hTime;
	
	
		
	/**
	 * Prende come parametro il solo nome del centro, il quale verra' utilizzato nella fase di
	 * analisi del log del programma.
	 * @param nome
	 */
	
	public Processo(String nome) {
		this.nome=nome;
		this.state=Stato.PASSIVO;
	}
	
	
	/**
	 * Funzione che ritorna il nome del centro
	 * @return nome centro
	 */
	
	public String getNome() {
		return nome;
	}
	
	public Stato getStato() {
		return state;
	}
	
	public void setState (Stato s){
		this.state=s;
	}
	
	public void hold(double temp){
		hTime=Double.valueOf(temp);
		this.state=Stato.HOLD;
	}
	
	public void activate(){
		this.state=Stato.ATTIVO;
	}
	
	public void passivate(){
		this.state=Stato.PASSIVO;
	}
	
	public Double getTime(){
		
		return hTime;
	}
	
	public int compareTo(Processo other) {
        return hTime.compareTo(other.hTime);
    }
	
}
