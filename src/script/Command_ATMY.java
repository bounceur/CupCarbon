package script;

import device.SensorNode;
import radio.Standard;
import radio.XBeeFrameGenerator;
import radio.XBeeToArduinoFrameGenerator;
import wisen_simulation.SimLog;

public class Command_ATMY extends Command {

	protected String arg = "" ;
	
	public Command_ATMY(SensorNode sensor, String arg) {
		this.sensor = sensor ;
		this.arg = arg ;
	}

	@Override
	public double execute() {
		SimLog.add("S" + sensor.getId() + " ATMY "+arg);
		String args = sensor.getScript().getVariableValue(arg);
		sensor.setMy(Integer.valueOf(args));
		
		String message = "MY" + Integer.toHexString(Integer.parseInt(args)).toUpperCase();
		
		String frame = message;
		if(sensor.getStandard() == Standard.ZIGBEE_802_15_4)
			frame = XBeeFrameGenerator.at(message);
		
		double ratio = 1.0/sensor.getUartDataRate();
		return (ratio*(frame.length()*8.));
	}
	
	@Override
	public String getArduinoForm() {
		String s = XBeeToArduinoFrameGenerator.at("MY"+arg); 
		return s;
	}
	
	@Override
	public String toString() {
		return "ATMY";
	}
}
