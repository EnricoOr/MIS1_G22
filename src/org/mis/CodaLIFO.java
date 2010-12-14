package mis;


public class CodaLIFO extends Coda {

	public Job successivo() {
		Job job=coda.get(coda.size()-1);
		coda.remove(coda.size()-1);
		return job;
	}

}

