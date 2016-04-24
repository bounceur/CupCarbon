package script;

import device.SensorNode;
import wisen_simulation.SimLog;
import wisen_simulation.WisenSimulation;

public class Command_TIME extends Command {

	protected String arg = "" ;
	
	public Command_TIME(SensorNode sensor, String arg) {
		this.sensor = sensor ;
		this.arg = arg ;
	}

	@Override
	public double execute() {
		String v = "" ;
		SimLog.add("S" + sensor.getId() + " GET TIME.");
		v = ""+ (WisenSimulation.time * sensor.getDriftTime());
		sensor.getScript().addVariable(arg, v);
		return 0;
	}

	@Override
	public String toString() {
		return "GET TIME";
	}

}
