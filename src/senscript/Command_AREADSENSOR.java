package script;

import wisen_simulation.SimLog;
import device.SensorNode;

public class Command_AREADSENSOR extends Command {

	protected String arg =  "";
	
	public Command_AREADSENSOR(SensorNode sensor, String arg) {
		this.sensor = sensor ;
		this.arg = arg ;
	}

	@Override
	public double execute() {
		String value = sensor.getSensorValues();
		SimLog.add("S" + sensor.getId() + " READ SENSOR: "+value);
		sensor.getScript().addVariable(arg, value);
		return 0;
	}
	
	@Override
	public String toString() {
		return "AREADSENSOR";
	}
}
