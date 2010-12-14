package mis;


public class FineSim extends Evento{

	public FineSim(Sequenziatore sequenziatore, double tempo){
		super(null, sequenziatore, tempo);
	}
    @Override
    public void evolvi() {
        throw new UnsupportedOperationException("Fine Simulazione non evolve.");
    }

}
