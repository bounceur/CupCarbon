package senscript;

import device.SensorNode;

public class Command_READ extends Command {

	
	protected String arg = ""; 
	
	public Command_READ(SensorNode sensor, String arg) {
		this.sensor = sensor ;
		this.arg = arg ;
	}

	@Override
	public double execute() {
		sensor.readMessage(arg);
		return 0 ;
	}

	@Override
	public String getArduinoForm() {
		String s = "";
		s += "\txbee.getResponse().getRx64Response(rx);\n";
		s += "\trdata = rx.getData();\n";
		s += "\tString "+arg+" = \"\" ;\n";
		
		s += "\tfor(int i=0; i<20; i++) {\n";
		s += "\t\t" + arg + " += (char) rdata[i];\n";
		s += "\t}\n";
		
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
