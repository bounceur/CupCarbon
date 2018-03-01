package senscript;

import device.SensorNode;
import simulation.WisenSimulation;

public class Command_LENGTH extends Command {

	protected String arg1 = "";
	protected String arg2 = "";
	
	public Command_LENGTH(SensorNode sensor, String arg1, String arg2) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
	}

	@Override
	public double execute() {
		String v = sensor.getScript().getVariableValue(arg2);
		WisenSimulation.simLog.add("S" + sensor.getId() + " LENGTH "+v);
		String r = ""+(v.length());
		sensor.getScript().addVariable(arg1, "" + r);
		return 0 ;
	}

	@Override
	public String toString() {
		return "LENGTH";
	}
}
