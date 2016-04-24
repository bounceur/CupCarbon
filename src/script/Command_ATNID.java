package script;

import device.SensorNode;
import radio_module.Standard;
import radio_module.XBeeFrameGenerator;
import radio_module.XBeeToArduinoFrameGenerator;
import wisen_simulation.SimLog;

public class Command_ATNID extends Command {

	protected String arg = "" ;
	
	public Command_ATNID(SensorNode sensor, String arg) {
		this.sensor = sensor ;
		this.arg = arg ;
	}

	@Override
	public double execute() {
		SimLog.add("S" + sensor.getId() + " ATID "+arg);
		String args = sensor.getScript().getVariableValue(arg);
		sensor.setNId(Integer.valueOf(args));

		String message = "ID" + Integer.toHexString(Integer.parseInt(args)).toUpperCase();
		
		String frame = message;
		if(sensor.getStandard() == Standard.ZIGBEE_802_15_4)
			frame = XBeeFrameGenerator.at(message);

		double ratio = 1.0/sensor.getUartDataRate();
		return (ratio*(frame.length()*8.)) ;
	}
	
	@Override
	public String getArduinoForm() {
		String s = XBeeToArduinoFrameGenerator.at("ID"+arg); 
		return s;
	}
	
	@Override
	public String toString() {
		return "NID";
	}
}
