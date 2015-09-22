package script;

import device.SensorNode;

public class Command_READ extends Command {

	
	protected String arg = ""; 
	
	public Command_READ(SensorNode sensor, String arg) {
		this.sensor = sensor ;
		this.arg = arg ;
	}

	@Override
	public int execute() {
		sensor.readMessage(arg);
		return 0;
	}

	@Override
	public String getArduinoForm() {
		String s = "int x ;\n";
		s += "Serial.read();\n";
		s += "Serial.read();\n";
		s += "Serial.read();\n";
		s += "Serial.read();\n";
		s += "Serial.read();\n";
		s += "Serial.read();\n";
		s += "Serial.read();\n";
		s += "x = 1-('1'-Serial.read());\n";
		s += "Serial.read();\n";
		s += "Serial.read();\n";
		s += "delay(100);\n";
		return s;
	}

	@Override
	public String toString() {
		return "READ";
	}

	@Override
	public String finishMessage() {
		return ("S" + sensor.getId() + " has finished reading.");
	}
}
