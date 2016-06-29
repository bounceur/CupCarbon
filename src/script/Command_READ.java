package script;

import device.SensorNode;
import radio_module.XBeeToArduinoFrameGenerator;

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
		s += "\t\t\tif (xbee.getResponse().getApiId() == RX_16_RESPONSE) {\n";
		s += "\t\t\t\txbee.getResponse().getRx16Response(rx16);\n";
		s += "\t\t\t\trdata = rx16.getData();\n";
		s += "\t\t\t}\n";
		s += "\t\t\tif (xbee.getResponse().getApiId() == RX_64_RESPONSE) {\n";
		s += "\t\t\t\txbee.getResponse().getRx64Response(rx64);\n";
		s += "\t\t\t\trdata = rx64.getData();\n";
		s += "\t\t\t}\n";
		
		//String ss = "";		
		s += "\t\tString "+arg+" = \"\" ;\n";
		
		s += "\t\tfor(int i=0; i<30; i++) {\n";
		s += "\t\t\t" + arg + " += (char) rdata[i];\n";
		s += "\t\t}\n";
		
		//s += "\t" + arg + " = \""+ss+"\";\n";
		
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
	
	public static void main(String [] args) {
		String s = XBeeToArduinoFrameGenerator.at("MY65535");
		System.out.println(s);
	}
}
