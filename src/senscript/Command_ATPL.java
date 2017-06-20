package senscript;

import device.DeviceList;
import device.SensorNode;
import radio_module.RadioStandard;
import radio_module.XBeeFrameGenerator;
import radio_module.XBeeToArduinoFrameGenerator;
import wisen_simulation.SimLog;

public class Command_ATPL extends Command {

	protected String arg = "" ;
	
	public Command_ATPL(SensorNode sensor, String arg) {
		this.sensor = sensor ;
		this.arg = arg ;
	}

	@Override
	public double execute() {
		SimLog.add("S" + sensor.getId() + " ATPL "+arg);
		String args = sensor.getScript().getVariableValue(arg);
		
		int v = (Double.valueOf(args)).intValue();
		
		sensor.setPl(v);
		if (DeviceList.propagationsCalculated)
			DeviceList.calculatePropagations();
		
		String message = "PL" + Integer.toHexString(v).toUpperCase();
		
		String frame = message;
		if(sensor.getStandard() == RadioStandard.ZIGBEE_802_15_4)
			frame = XBeeFrameGenerator.at(message);

		double ratio = 1.0/sensor.getUartDataRate();
		return (ratio*(frame.length()*8.)) ;
	}
	
	@Override
	public String getArduinoForm() {
		String s = XBeeToArduinoFrameGenerator.at("PL"+arg); 
		return s;
	}
	
	@Override
	public String toString() {
		return "ATPL";
	}
}
