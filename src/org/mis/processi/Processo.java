package org.mis.processi;

public abstract class Processo implements Comparable<Processo>
{
	private String nome;
	private TipoProcesso tipo;
	private Stato state;
	protected double hTime;
	
	public enum TipoProcesso
	{
		Terminale,
		CPU,
		Disk,
		Host,
		Stampante,
		Osservazione,
		FineSimulazione,
		Job
	}
		
	/**
	 * Prende come parametro il solo nome del centro, il quale verra' utilizzato nella fase di
	 * analisi del log del programma.
	 * @param nome
	 */	
	public Processo(String nome, TipoProcesso tipo)
	{
		this.nome=nome;
		this.tipo = tipo;
		this.state=Stato.PASSIVO;
	}	
	
	/**
	 * Funzione che ritorna il nome del centro
	 * @return nome centro
	 */
	public final String getNome()
	{
		return nome;
	}

	/**
	 * Funzione che ritorna il tipo del centro
	 * @return tipo centro
	 */
	public final TipoProcesso getTipo()
	{
		return tipo;
	}
	
	public final Stato getStato()
	{
		return state;
	}
	
	public final void setState (Stato s)
	{
		this.state=s;
	}
	
	public final void hold(double temp)
	{
		hTime=Double.valueOf(temp);
		this.state=Stato.HOLD;
	}
	
	public void activate()
	{
		this.state=Stato.ATTIVO;
	}
	
	public final void passivate()
	{
		this.state=Stato.PASSIVO;
	}
	
	public final double getTime()
	{
		return hTime;
	}
	
	public final int compareTo(Processo other)
	{
        return Double.compare(hTime, other.hTime);
    }	
}
