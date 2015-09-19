package script;

import wisen_simulation.SimLog;
import device.SensorNode;

public class Command_GETPOS extends Command {

	protected String arg = "" ;
	
	public Command_GETPOS(SensorNode sensor, String arg) {
		this.sensor = sensor ;
		this.arg = arg ;
	}

	@Override
	public int execute() {
		String v = "" ;
		SimLog.add("S" + sensor.getId() + " GET POSITION.");
		v = sensor.getLatitude()+"#"+sensor.getLongitude();		
		sensor.getScript().addVariable(arg, v);
		return 0;
	}

	@Override
	public String toString() {
		return "GETPOS";
	}

}
