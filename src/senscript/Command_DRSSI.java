package senscript;

import device.SensorNode;

public class Command_DRSSI extends Command {

	protected String arg1 = "" ;
	
	public Command_DRSSI(SensorNode sensor, String arg1) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
	}

	@Override
	public double execute() {
		sensor.getScript().addVariable(arg1, "" + sensor.getDrssi());
		
		return 0 ;
	}

	@Override
	public String toString() {
		return "DISTANCE";
	}
	
}
