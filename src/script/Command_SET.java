package script;

import wisen_simulation.SimLog;
import device.SensorNode;

public class Command_SET extends Command {

	protected String arg1 = "";
	protected String arg2 = "";
	
	public Command_SET(SensorNode sensor, String arg1, String arg2) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
	}

	@Override
	public int execute() {
		SimLog.add("S" + sensor.getId() + " Set " + arg1 + "=" + arg2);
		String arg = sensor.getScript().getVariableValue(arg2);
		sensor.getScript().addVariable(arg1, arg);
		return 0;
	}

	@Override
	public String toString() {
		return "SET";
	}
	
}
