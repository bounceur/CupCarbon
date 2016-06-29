package script;

import device.SensorNode;
import wisen_simulation.SimLog;

public class Command_INT extends Command {

	protected String arg1 = "";
	
	public Command_INT(SensorNode sensor, String arg1) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
	}

	@Override
	public double execute() {
		String v1 = sensor.getScript().getVariableValue("$"+arg1);
		int z = Double.valueOf(v1).intValue();
		SimLog.add("S" + sensor.getId() + " " + arg1 + " = " + z);
		sensor.getScript().addVariable(arg1, "" + z);
		return 0 ;
	}

	@Override
	public String toString() {
		return "INT";
	}
}
