package senscript;

import device.SensorNode;
import simulation.WisenSimulation;

public class Command_CBUFFER extends Command {
	
	public Command_CBUFFER(SensorNode sensor) {
		this.sensor = sensor ;
	}

	@Override
	public double execute() {	
		WisenSimulation.simLog.add("S" + sensor.getId() + " CBUFFER: CLEAR BUFFER");
		sensor.initBuffer();
		return 0 ;
	}

	@Override
	public String toString() {
		return "FLUSH";
	}
	
}
