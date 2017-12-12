package senscript;

import device.SensorNode;
import simulation.WisenSimulation;

public class Command_LOOP extends Command {
	
	public Command_LOOP(SensorNode sensor) {
		this.sensor = sensor ;
	}

	@Override
	public double execute() {
		WisenSimulation.simLog.add("S" + sensor.getId() + " Starts the loop section.");
		sensor.getScript().setIndexToLoopIndex();
		return 0 ;
	}

	@Override
	public String getArduinoForm() { 
		return "}\nvoid loop() {";
	}

	@Override
	public String toString() {
		return "LOOP";
	}
}
