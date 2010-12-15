package org.mis.code;

import org.mis.gen.GeneratoreUniforme;
import org.mis.processi.Job;


public class CodaRAND extends Coda {
        private GeneratoreUniforme g;
        
	public CodaRAND(GeneratoreUniforme g){
		this.g=g;
	}
	public Job successivo() {
		int n=(int)(g.nextNumber()*coda.size()) %coda.size();
		Job job=coda.get(n);
		coda.remove(n);
		return job;
	}

}