package script;

import device.DeviceList;
import device.SensorNode;
import radio_module.Standard;
import radio_module.XBeeFrameGenerator;
import radio_module.XBeeToArduinoFrameGenerator;
import wisen_simulation.SimLog;

public class Command_ATID extends Command {

	protected String arg = "" ;
	
	public Command_ATID(SensorNode sensor, String arg) {
		this.sensor = sensor ;
		this.arg = arg ;
	}

	@Override
	public double execute() {
		SimLog.add("S" + sensor.getId() + " ATNI "+arg);
		String args = sensor.getScript().getVariableValue(arg);
		sensor.setId(Integer.valueOf(args));
		if (DeviceList.propagationsCalculated)
			DeviceList.calculatePropagations();
		
		String message = "NI" + Integer.toHexString(Integer.parseInt(args)).toUpperCase();
		
		String frame = message;
		if(sensor.getStandard() == Standard.ZIGBEE_802_15_4)
			frame = XBeeFrameGenerator.at(message);
		
		double ratio = 1.0/sensor.getUartDataRate();
		return (ratio*(frame.length()*8.));
	}
	
	@Override
	public String getArduinoForm() {
		String s = XBeeToArduinoFrameGenerator.at("NI"+arg); 
		return s;
	}
	
	@Override
	public String toString() {
		return "ATID";
	}
}
