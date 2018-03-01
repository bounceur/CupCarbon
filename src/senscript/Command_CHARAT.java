package senscript;

import device.SensorNode;
import simulation.WisenSimulation;

public class Command_CHARAT extends Command {

	protected String arg1 = "";
	protected String arg2 = "";
	protected String arg3 = "";
	
	public Command_CHARAT(SensorNode sensor, String arg1, String arg2, String arg3) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
		this.arg3 = arg3 ;
	}

	@Override
	public double execute() {
		String v = sensor.getScript().getVariableValue(arg2);
		String n = sensor.getScript().getVariableValue(arg3);
		WisenSimulation.simLog.add("S" + sensor.getId() + " charat "+v+" "+n);
		String r = ""+(v.charAt(Double.valueOf(n).intValue()));
		sensor.getScript().addVariable(arg1, "" + r);
		return 0 ;
	}

	@Override
	public String toString() {
		return "CHARAT";
	}
}
