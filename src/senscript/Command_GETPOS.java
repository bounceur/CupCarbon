package senscript;

import wisen_simulation.SimLog;
import device.SensorNode;

public class Command_GETPOS extends Command {

	protected String arg = "" ;
	
	public Command_GETPOS(SensorNode sensor, String arg) {
		this.sensor = sensor ;
		this.arg = arg ;
	}

	@Override
	public double execute() {
		String v = "" ;
		SimLog.add("S" + sensor.getId() + " GET POSITION.");
		v = sensor.getLongitude()+"#"+sensor.getLatitude();		
		sensor.getScript().addVariable(arg, v);
		return 0 ;
	}

	@Override
	public String toString() {
		return "GETPOS";
	}

}
