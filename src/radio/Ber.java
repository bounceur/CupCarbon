package radio;

import device.Device;
import interference.Transeiver;
import wisen_simulation.SimulationInputs;

public class Ber {

	public static final int PROBABILITY = 0;
	public static final int ALPHA_D = 1;
	
	public static boolean berOk(String packet, Device device) {
		if (SimulationInputs.ack) {			
			if (SimulationInputs.ackType == PROBABILITY)
				return (Math.random() <= SimulationInputs.ackProba?true:false);
			if (SimulationInputs.ackType == ALPHA_D) {				
				return (Integer.valueOf(Transeiver.receviedPacketWithAlphaD(packet, device)[1]) == 0);
			}
		}
		return true;
	}
	
}
