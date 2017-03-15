package senscript;

import wisen_simulation.SimLog;
import arduino.BeginInstructions;
import device.SensorNode;

public class Command_DREADSENSOR extends Command {

	protected String arg =  "";
	
	public Command_DREADSENSOR(SensorNode sensor, String arg) {
		this.sensor = sensor ;
		this.arg = arg ;
	}

	@Override
	public double execute() {
		String value = sensor.isSensorDetecting()?"1":"0";
		SimLog.add("S" + sensor.getId() + " READ SENSOR: "+value);
		sensor.getScript().addVariable(arg, value);
		return 0 ;
	}
	
	@Override
	public String getArduinoForm() {
		BeginInstructions.add("pinMode(2, INPUT);");
		String s = "\tString " + arg + ";\n"; 
		s += "\t" + arg + " = digitalRead(2); \n";		
		return s;
	}
	
	@Override
	public String toString() {
		return "DREADSENSOR";
	}
}
