package script;

import wisen_simulation.SimLog;
import device.SensorNode;

public class Command_LOOP extends Command {
	
	public Command_LOOP(SensorNode sensor) {
		this.sensor = sensor ;
	}

	@Override
	public int execute() {
		SimLog.add("S" + sensor.getId() + " Starts the loop section.");
		sensor.getScript().setIndexToLoopIndex();
		return 0;
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
