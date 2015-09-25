package script;

import script_functions.ScriptFunctions;
import wisen_simulation.SimLog;
import device.SensorNode;

public class Command_FUNCTION extends Command {

	protected String arg1 = "";
	protected String arg2 = "";
	protected String arg3 = "";
	
	public Command_FUNCTION(SensorNode sensor, String arg1, String arg2, String arg3) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
		this.arg3 = arg3 ;
	}

	@Override
	public long execute() {
		SimLog.add("S" + sensor.getId() + " CALL FUNCTION "+arg1+"("+arg2+", "+arg3+")");
		String arg0 = arg1;
		String function = arg2;			
		String [] args = arg3.split(",");
		sensor.getScript().variablesToValues(args);
		String value = ScriptFunctions.function(function, args);
		sensor.getScript().addVariable(arg0, value);
		return 0;
	}

	@Override
	public String toString() {
		return "FUNCTION";
	}
	
}
