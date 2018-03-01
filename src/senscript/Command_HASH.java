package senscript;

import device.SensorNode;
import simulation.WisenSimulation;

public class Command_HASH extends Command {

	protected String arg1 = "";
	protected String arg2 = "";
	
	public Command_HASH(SensorNode sensor, String arg1, String arg2) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
	}

	@Override
	public double execute() {
		String s1 = sensor.getScript().getVariableValue(arg2);
		WisenSimulation.simLog.add("S" + sensor.getId() + " " + arg1 + " = hash = "+ s1.hashCode());
		sensor.getScript().addVariable(arg1, "" + s1.hashCode());
		return 0 ;
	}

	@Override
	public String toString() {
		return "Binary NOT";
	}
}
