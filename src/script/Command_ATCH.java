package script;

import wisen_simulation.SimLog;
import arduino.XBeeFrameGenerator;
import device.DataInfo;
import device.SensorNode;

public class Command_ATCH extends Command {
	
	protected String arg = "" ;
	
	public Command_ATCH(SensorNode sensor, String arg) {		
		this.sensor = sensor ;
		this.arg = arg ;
	}

	@Override
	public long execute() {
		SimLog.add("S" + sensor.getId() + " ATCH "+arg);
		String args = sensor.getScript().getVariableValue(arg);
		sensor.setCh(Integer.valueOf(args));
		double ratio = (DataInfo.ChDataRate*1.0)/(DataInfo.UartDataRate);
		String message = "ATCH "+args;
		return (long)(Math.round(message.length()*8.*ratio));
	}
	
	@Override
	public String getArduinoForm() {
		String s = XBeeFrameGenerator.at("CH"+arg); 
		return s;
	}
	
	@Override
	public String toString() {
		return "ATCH";
	}
}
