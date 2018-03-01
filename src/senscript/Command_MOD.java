package senscript;

import device.SensorNode;
import simulation.WisenSimulation;

public class Command_MOD extends Command {

	protected String arg1 = "";
	protected String arg2 = "";
	protected String arg3 = "";
	
	public Command_MOD(SensorNode sensor, String arg1, String arg2, String arg3) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
		this.arg3 = arg3 ;
	}

	@Override
	public double execute() {
		String v1 = sensor.getScript().getVariableValue(arg2);
		String v2 = sensor.getScript().getVariableValue(arg3);
		double z = 0;
		z = Double.valueOf(v1) % Double.valueOf(v2);
		WisenSimulation.simLog.add("S" + sensor.getId() + " " + arg1 + "=" + Double.valueOf(v1) + "%" + Double.valueOf(v2) + " -> " + z);
		sensor.getScript().addVariable(arg1, "" + z);
		return 0 ;
	}

	@Override
	public String toString() {
		return "MOD";
	}
	
}
