package script;

import wisen_simulation.SimLog;
import device.SensorNode;

public class Command_INITPREC extends Command {

	protected String arg = "" ;
	
	public Command_INITPREC(SensorNode sensor, String arg) {
		this.sensor = sensor ;
		this.arg = arg ;
	}

	@Override
	public int execute() {
		SimLog.add("S" + sensor.getId() + " INITPREC");
		double lat = sensor.getLatitude();
		lat = lat - 1;
		double lon = sensor.getLongitude();
		String pos = String .valueOf(lat)+"#"+String.valueOf(lon);
		sensor.getScript().addVariable(arg, pos);
		return 0;
	}

	@Override
	public String toString() {
		return "INITPREC";
	}
}
