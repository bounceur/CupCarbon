package senscript;

import arduino.Bracket;
import device.SensorNode;
import wisen_simulation.SimLog;

public class Command_WAIT extends Command {
	
	protected String arg = "";
	
	public Command_WAIT(SensorNode sensor) {
		this.sensor = sensor ;
	}
	
	public Command_WAIT(SensorNode sensor, String arg) {
		this.sensor = sensor ;
		//this.arg = ""+ ((long)(Long.parseLong(sensor.getScript().getVariableValue(arg)) * sensor.getRadioDataRate() / 1000.)) ;
		this.arg = arg;
	}

	@Override
	public double execute() {		
		double event = 0 ;

		if (sensor.dataAvailable()) {			
			SimLog.add("S" + sensor.getId() + " Buffer available, exit waiting.");
			sensor.getScript().setWiting(false);
			return 0 ;
		} 
		else {
			SimLog.add("S" + sensor.getId() + " is waiting for data ...");
			
			sensor.getScript().setWiting(true);
			
			if (arg.equals(""))
				event = Double.MAX_VALUE;
			else {
				event = (Double.parseDouble(sensor.getScript().getVariableValue(arg))/1000.);
			}
		}
		
		return event;
	}

	@Override
	public boolean isWait() {
		return true;
	}
	
	@Override
	public String getArduinoForm() {
		Bracket.n++;
		Bracket.n++;
		
		String s = "";
		s += "\t" + "xbee.readPacket("+arg+");\n";
		s += "\tif (xbee.getResponse().isAvailable()) {\n";
		s += "\tif (xbee.getResponse().getApiId() == RX_64_RESPONSE) {\n";
		//s += "\txbee.getResponse().getRx64Response(rx);\n";
		//s += "\trdata = rx.getData();\n";
		//s += "\twhile(Serial.available()>0) {";
		return s ;
	}

	@Override
	public String toString() {
		return "WAIT";
	}

	@Override
	public String finishMessage() {
		return ("S" + sensor.getId() + " has finished waiting.");
	}
	
}
