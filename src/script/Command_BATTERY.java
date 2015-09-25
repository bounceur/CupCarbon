package script;

import wisen_simulation.SimLog;
import device.SensorNode;

public class Command_BATTERY extends Command {

	protected String arg = "" ;
	
	public Command_BATTERY(SensorNode sensor, String arg) {
		this.sensor = sensor ;
		this.arg = arg ;
	}

	@Override
	public long execute() {
		String v = "" ;
		SimLog.add("S" + sensor.getId() + " BATTERY .");
		v = sensor.getBatteryLevel()+"";
		sensor.getScript().addVariable(arg, v);
		return 0;
	}

	@Override
	public String toString() {
		return "GETPOS";
	}

}
