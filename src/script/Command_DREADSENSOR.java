package script;

import wisen_simulation.SimLog;
import device.SensorNode;

public class Command_DREADSENSOR extends Command {

	protected String arg =  "";
	
	public Command_DREADSENSOR(SensorNode sensor, String arg) {
		this.sensor = sensor ;
		this.arg = arg ;
	}

	@Override
	public long execute() {
		String value = sensor.isSensorDetecting()?"1":"0";
		SimLog.add("S" + sensor.getId() + " READ SENSOR: "+value);
		sensor.getScript().addVariable(arg, value);
		return 0;
	}
	
	@Override
	public String getArduinoForm() { 
		return "digitalRead(8);";
	}
	
	@Override
	public String toString() {
		return "DREADSENSOR";
	}
}
