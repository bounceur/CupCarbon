package physical_layer;

import wisen_simulation.SimulationInputs;

public class Ber {

	public static boolean berOk() {
		return (Math.random() <= SimulationInputs.ackProba?true:false);
	}
	
}
