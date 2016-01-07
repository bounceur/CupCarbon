package physical_layer;

import interference.Interference;
import wisen_simulation.SimulationInputs;

public class Ber {

	public static final int PROBABILITY = 0;
	public static final int ALPHA_D = 1;
	
	public static boolean berOk(String message) {
		if (SimulationInputs.ack) {
			if (SimulationInputs.ackType == PROBABILITY)
				return (Math.random() <= SimulationInputs.ackProba?true:false);
			if (SimulationInputs.ackType == PROBABILITY)
				return Interference.alphaD(message);
		}
		return true;
	}
	
}
