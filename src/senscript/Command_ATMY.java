package senscript;

import device.SensorNode;
import radio_module.RadioStandard;
import radio_module.XBeeFrameGenerator;
import radio_module.XBeeToArduinoFrameGenerator;
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
		if(sensor.getStandard() == RadioStandard.ZIGBEE_802_15_4)
			frame = XBeeFrameGenerator.at(message);
		
		double ratio = 1.0/sensor.getUartDataRate();
		return (ratio*(frame.length()*8.)) ;
	}
	
	@Override
	public String getArduinoForm() {
		String s = XBeeToArduinoFrameGenerator.at("MY"+sensor.getScript().getVariableValue(arg)); 
		return s;
	}
	
	@Override
	public String toString() {
		return "ATMY";
	}

}
