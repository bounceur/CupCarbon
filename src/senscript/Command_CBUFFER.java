package script;

import wisen_simulation.SimLog;
import device.SensorNode;

public class Command_FLUSH extends Command {
	
	public Command_FLUSH(SensorNode sensor) {
		this.sensor = sensor ;
	}

	@Override
	public double execute() {	
		SimLog.add("S" + sensor.getId() + " FLUSH.");
		sensor.initBuffer();
		return 0 ;
	}

	@Override
	public String toString() {
		return "FLUSH";
	}
	
}
