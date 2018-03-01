package senscript;

import arduino.BeginInstructions;
import device.SensorNode;
import simulation.WisenSimulation;

public class Command_DREADSENSOR extends Command {

	protected String arg =  "";
	
	public Command_DREADSENSOR(SensorNode sensor, String arg) {
		this.sensor = sensor ;
		this.arg = arg ;
	}

	@Override
	public double execute() {
		sensor.getBattery().consume(0.0001);
		String value = sensor.isSensorDetecting()?"1":"0";
		WisenSimulation.simLog.add("S" + sensor.getId() + " READ SENSOR: "+value);
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
