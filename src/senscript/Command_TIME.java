package senscript;

import device.SensorNode;
import simulation.WisenSimulation;

public class Command_TIME extends Command {

	protected String arg = "" ;
	
	public Command_TIME(SensorNode sensor, String arg) {
		this.sensor = sensor ;
		this.arg = arg ;
	}

	@Override
	public double execute() {
		String v = "" ;
		WisenSimulation.simLog.add("S" + sensor.getId() + " GET TIME.");
		v = ""+ (WisenSimulation.time * sensor.getDriftTime());
		sensor.getScript().addVariable(arg, v);
		return 0;
	}

	@Override
	public String toString() {
		return "GET TIME";
	}

}
