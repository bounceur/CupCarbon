package senscript;

import wisen_simulation.SimLog;
import device.SensorNode;

public class Command_CBUFFER extends Command {
	
	public Command_CBUFFER(SensorNode sensor) {
		this.sensor = sensor ;
	}

	@Override
	public double execute() {	
		SimLog.add("S" + sensor.getId() + " CLEAR BUFFER.");
		sensor.initBuffer();
		return 0 ;
	}

	@Override
	public String toString() {
		return "FLUSH";
	}
	
}
