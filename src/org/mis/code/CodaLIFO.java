package org.mis.code;

import org.mis.processi.Job;


public class CodaLIFO extends Coda {

	public Job successivo() {
		Job job=coda.get(coda.size()-1);
		coda.remove(coda.size()-1);
		return job;
	}

}

