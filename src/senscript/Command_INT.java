package senscript;

import device.SensorNode;
import simulation.WisenSimulation;

public class Command_INT extends Command {

	protected String arg1 = "";
	protected String arg2 = "";
	
	public Command_INT(SensorNode sensor, String arg1, String arg2) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
	}

	@Override
	public double execute() {
		String v = sensor.getScript().getVariableValue(arg2);
		int z = Double.valueOf(v).intValue();
		WisenSimulation.simLog.add("S" + sensor.getId() + " INT -> " + arg1 + " = " + z);
		sensor.getScript().addVariable(arg1, "" + z);
		return 0 ;
	}

	@Override
	public String toString() {
		return "INT";
	}
}
