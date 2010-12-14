package mis;


public class CodaFIFO extends Coda {

	public Job successivo() {
		Job job=coda.get(0);
		coda.remove(0);
		return job;
	}

}

